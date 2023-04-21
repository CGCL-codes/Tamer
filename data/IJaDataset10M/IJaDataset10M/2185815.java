package gov.sns.apps.launcher;

import gov.sns.application.*;
import gov.sns.tools.messaging.MessageCenter;
import gov.sns.tools.apputils.iconlib.IconLib;
import gov.sns.tools.apputils.iconlib.IconLib.IconGroup;
import java.io.*;
import java.util.*;
import java.text.*;
import javax.swing.*;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Point;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.datatransfer.*;

/**
 * LaunchWindow is the main document window for the launcher application.
 *
 * @author t6p
 */
class LaunchWindow extends XalWindow implements SwingConstants {

    /** The main model for the document */
    protected final LaunchModel _model;

    /** The controller holds the selection state of groups and applications */
    protected final LaunchController _controller;

    /** file chooser for adding applications to a group */
    protected JFileChooser _appFileChooser;

    /** 
	 * Creates a new instance of LaunchWindow
	 * @param aDocument The document for this window
	 */
    public LaunchWindow(final LaunchDocument aDocument) {
        super(aDocument);
        _model = aDocument.getModel();
        _controller = new LaunchController();
        setSize(600, 400);
        makeContents();
    }

    /**
     * Do not show the toolbar.
     */
    @Override
    public boolean usesToolbar() {
        return false;
    }

    /**
	 * Construct the contents of this window
	 */
    protected void makeContents() {
        Component groupView = makeGroupView();
        Component appsView = makeAppsView();
        Component infoView = makeInfoView();
        JSplitPane selectorView = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groupView, appsView);
        selectorView.setDividerSize(3);
        selectorView.setResizeWeight(0.5);
        selectorView.setContinuousLayout(true);
        JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, selectorView, infoView);
        mainPane.setDividerSize(3);
        mainPane.setResizeWeight(0.3);
        mainPane.setDividerLocation(0.5);
        mainPane.setContinuousLayout(true);
        Box windowView = new Box(BoxLayout.Y_AXIS);
        windowView.add(mainPane);
        windowView.add(makeMessageBoard());
        getContentPane().add(windowView);
    }

    /**
	 * Show this window
	 */
    @Override
    public void showWindow() {
        super.showWindow();
        _controller.setSelectedGroup(_model.getGroup(0));
    }

    /**
	 * Convenience method for getting the index of the list cell enclosing the specified point.
	 * Unlike locationToIndex(), this method tests that the location is actually in the bounds
	 * of the cell.  This is important to avoid associating an event that occurs at the bottom of 
	 * the list but outside of any cell.
	 * @param list The JList to test
	 * @param location The location of the event as a point
	 * @return the cell in the list corresponding to the location or -1 if no cell is associated
	 */
    protected static int getIndexAtEvent(JList list, Point location) {
        int index = list.locationToIndex(location);
        Rectangle cellBounds = list.getCellBounds(index, index);
        return (cellBounds.contains(location)) ? index : -1;
    }

    /**
	 * Build a message board for posting application wide messages
	 * @return message board view
	 */
    protected Component makeMessageBoard() {
        final DateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
        final JLabel messageLabel = new JLabel("Message Board");
        MessageCenter.defaultCenter().registerTarget(new LaunchBoardListener() {

            /**
			 * Post an application wide message from the source
			 * @param source The source of the message
			 * @param message The message posted
			 */
            public void postMessage(Object source, String message) {
                displayMessage(message);
            }

            /**
			 * Post an application wide error message from the source
			 * @param source The source of the message
			 * @param message The message posted
			 */
            public void postErrorMessage(Object source, String message) {
                displayErrorMessage(message);
            }

            /**
			 * Display a message in the message board with the specified font color
			 * @param message The message to display
			 * @param fontColor The font color used to display the message
			 */
            protected void displayMessage(final String message, final String fontColor) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("<html><body>");
                buffer.append(TIMESTAMP_FORMAT.format(new Date()));
                buffer.append(" - ");
                buffer.append("<font COLOR=" + fontColor + ">");
                buffer.append(message);
                buffer.append("</font></body></html>");
                messageLabel.setText(buffer.toString());
            }

            /**
			 * Display a message in the message board with a font color of blue.
			 * @param message The message to display
			 */
            protected void displayMessage(final String message) {
                displayMessage(message, "#000088");
            }

            /**
			 * Display an error message in the message board with a font color of red.
			 * @param message The message to display
			 */
            protected void displayErrorMessage(final String message) {
                displayMessage(message, "#ff0000");
            }
        }, LaunchBoardListener.class);
        return messageLabel;
    }

    /**
	 * Build the view for displaying and managing the groups.  This includes a label, a
	 * scrollable JList of the groups and a button for adding a new group.
	 * @return The view for displaying and managing the groups
	 */
    protected Component makeGroupView() {
        Box groupBox = new Box(BoxLayout.Y_AXIS);
        Box groupLabelBox = new Box(BoxLayout.X_AXIS);
        groupLabelBox.add(new JLabel("Groups:"));
        groupLabelBox.add(Box.createHorizontalGlue());
        groupBox.add(groupLabelBox);
        final JList groupList = new JList();
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupBox.add(new JScrollPane(groupList));
        groupList.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    AppGroup group = _controller.getSelectedGroup();
                    if (group != null) {
                        _model.removeGroup(group);
                        groupList.clearSelection();
                        _controller.setSelectedGroup(null);
                    }
                }
            }
        });
        groupList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) return;
                int selectedIndex = groupList.getSelectedIndex();
                if (selectedIndex < 0) {
                    _controller.setSelectedGroup(null);
                } else {
                    AppGroup group = _model.getGroup(selectedIndex);
                    _controller.setSelectedGroup(group);
                }
            }
        });
        groupList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int index = getIndexAtEvent(groupList, event.getPoint());
                    if (index < 0) return;
                    AppGroup group = _model.getGroup(index);
                    if (group.allowsLabelEdit()) {
                        String name = JOptionPane.showInputDialog(LaunchWindow.this, "New Group Label: ", "Rename the group", JOptionPane.QUESTION_MESSAGE);
                        if (name != null) {
                            name = name.trim();
                            if (name != null && name.length() > 0) group.setLabel(name);
                        }
                    } else {
                        java.awt.Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
        });
        final GroupListModel listModel = new GroupListModel(_model);
        groupList.setModel(listModel);
        groupList.setSelectedIndex(0);
        groupList.setDragEnabled(true);
        groupList.setTransferHandler(new GroupListTransferHandler());
        JButton groupAddButton = new JButton(IconLib.getIcon(IconGroup.GENERAL, "Add24.gif"));
        groupAddButton.setToolTipText("Add a new group");
        groupAddButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                String name = JOptionPane.showInputDialog(LaunchWindow.this, "Group Label: ", "Label the group", JOptionPane.QUESTION_MESSAGE);
                if (name != null) {
                    _model.addGroup(new AppGroup(name));
                }
            }
        });
        Box groupButtonRow = new Box(BoxLayout.X_AXIS);
        groupButtonRow.add(groupAddButton);
        groupButtonRow.add(Box.createHorizontalGlue());
        groupBox.add(groupButtonRow);
        return groupBox;
    }

    /**
	 * Build the view for displaying and managing the applications.  This includes a label, a 
	 * scrollable JList for displaying the applications and a button for adding an application
	 * to the selected group.
	 * @return The view for displaying and managing the applications
	 */
    protected Component makeAppsView() {
        Box appsBox = new Box(BoxLayout.Y_AXIS);
        Box appLabelBox = new Box(BoxLayout.X_AXIS);
        appLabelBox.add(new JLabel("Applications:"));
        appLabelBox.add(Box.createHorizontalGlue());
        appsBox.add(appLabelBox);
        final JList appList = new JList();
        appList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        appList.setDragEnabled(true);
        appList.setTransferHandler(new AppListTransferHandler());
        appsBox.add(new JScrollPane(appList));
        appList.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(final ListSelectionEvent event) {
                if (event.getValueIsAdjusting()) return;
                AppGroup group = _controller.getSelectedGroup();
                int index = appList.getSelectedIndex();
                if (index < 0) return;
                App app = group.getApp(index);
                _controller.setSelectedApp(app);
            }
        });
        appList.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    App app = _controller.getSelectedApp();
                    if (app != null) {
                        AppGroup group = _controller.getSelectedGroup();
                        group.removeApp(app);
                        appList.clearSelection();
                        _controller.setSelectedApp(null);
                    }
                }
            }
        });
        _controller.addLaunchControllerListener(new LaunchControllerListener() {

            public void selectedGroupChanged(LaunchController source, AppGroup newSelectedGroup) {
                appList.clearSelection();
            }

            public void selectedAppChanged(LaunchController source, App newSelectedApp) {
            }
        });
        final AppListModel appListModel = new AppListModel();
        _controller.addLaunchControllerListener(appListModel);
        appList.setModel(appListModel);
        appList.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    int index = getIndexAtEvent(appList, event.getPoint());
                    if (index < 0) return;
                    AppGroup group = appListModel.getGroup();
                    try {
                        App app = group.getApp(index);
                        if (app.exists()) {
                            app.launch();
                        }
                    } catch (IOException exception) {
                        System.err.println(exception);
                        LaunchWindow.this.displayError("Launch Error", "Error launching application:", exception);
                    }
                }
            }
        });
        final JButton appAddButton = new JButton(IconLib.getIcon(IconGroup.GENERAL, "Add24.gif"));
        appAddButton.setToolTipText("Add a new app");
        appAddButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent event) {
                if (_appFileChooser == null) {
                    _appFileChooser = new JFileChooser();
                    _appFileChooser.setDialogTitle("Select Application Scripts");
                    _appFileChooser.setMultiSelectionEnabled(true);
                }
                int status = _appFileChooser.showOpenDialog(LaunchWindow.this);
                if (status == JFileChooser.APPROVE_OPTION) {
                    File[] selections = _appFileChooser.getSelectedFiles();
                    AppGroup group = _controller.getSelectedGroup();
                    _model.addAppsToGroup(selections, group);
                }
            }
        });
        appAddButton.setEnabled(_controller.getSelectedGroup() != null);
        _controller.addLaunchControllerListener(new LaunchControllerListener() {

            /**
			 * Handle the event indicating that the launch controller has a new selected group.
			 * Enable the button that allows adding applictions if a group has been selected.
			 * If not, disable the button.
			 * @param source The launch controller
			 * @param newSelectedGroup The new selected group
			 */
            public void selectedGroupChanged(LaunchController source, AppGroup group) {
                appAddButton.setEnabled(group != null);
            }

            /**
			 * Handle the event indicating that the launch controller has a new selected application.
			 * This implementation does nothing.
			 * @param source The launch controller
			 * @param newSelectedApp the new selected application
			 */
            public void selectedAppChanged(LaunchController source, App app) {
            }
        });
        Box appButtonRow = new Box(BoxLayout.X_AXIS);
        appButtonRow.add(appAddButton);
        appButtonRow.add(Box.createHorizontalGlue());
        appsBox.add(appButtonRow);
        return appsBox;
    }

    /**
	 * Construct the view for displaying information about the selected application.  A label
	 * displays the application's file path and a text box allows the user to edit the information
	 * describing the application.
	 * @return a view for displaying information about the selected application
	 */
    protected Component makeInfoView() {
        Box infoBox = new Box(BoxLayout.Y_AXIS);
        Box row;
        final JLabel pathLabel = new JLabel("Path");
        row = new Box(BoxLayout.X_AXIS);
        row.add(pathLabel);
        row.add(Box.createHorizontalGlue());
        infoBox.add(row);
        infoBox.add(Box.createVerticalStrut(5));
        JLabel noteLabel = new JLabel("Notes:");
        row = new Box(BoxLayout.X_AXIS);
        row.add(noteLabel);
        row.add(Box.createHorizontalGlue());
        infoBox.add(row);
        final JTextArea infoTextView = new JTextArea();
        infoTextView.setColumns(40);
        infoTextView.setEditable(false);
        infoTextView.setLineWrap(true);
        infoTextView.setWrapStyleWord(true);
        infoBox.add(new JScrollPane(infoTextView));
        final JToggleButton editButton = new JToggleButton(IconLib.getIcon(IconGroup.GENERAL, "Edit24.gif"));
        editButton.setToolTipText("Edit the app information");
        editButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                infoTextView.setEditable(!infoTextView.isEditable());
                editButton.setSelected(infoTextView.isEditable());
            }
        });
        editButton.setEnabled(false);
        final JButton runButton = new JButton(IconLib.getIcon(IconGroup.MEDIA, "Play24.gif"));
        runButton.setToolTipText("Run the application");
        runButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent event) {
                final App app = _controller.getSelectedApp();
                if (app != null) {
                    try {
                        app.launch();
                    } catch (java.io.IOException exception) {
                        displayError("Launch Error", "Exception during launch of " + app, exception);
                    }
                }
            }
        });
        runButton.setEnabled(false);
        final Box infoButtonRow = new Box(BoxLayout.X_AXIS);
        infoButtonRow.add(editButton);
        infoButtonRow.add(runButton);
        infoButtonRow.add(Box.createHorizontalGlue());
        infoBox.add(infoButtonRow);
        final AppNotesHandler notesHandler = new AppNotesHandler();
        infoTextView.getDocument().addDocumentListener(notesHandler);
        _controller.addLaunchControllerListener(new LaunchControllerListener() {

            /**
			 * Handle the event indicating that the launch controller has a new selected group.
			 * This event is ignored.
			 * @param source The launch controller
			 * @param newSelectedGroup The new selected group
			 */
            public void selectedGroupChanged(LaunchController source, AppGroup group) {
            }

            /**
			 * Handle the event indicating that the launch controller has a new selected application.
			 * Update the info text box to show the notes for the selected application and enable
			 * the edit button.  If no application is selected, disable the edit button and show empty information.
			 * @param source The launch controller
			 * @param app the new selected application
			 */
            public void selectedAppChanged(final LaunchController source, final App app) {
                if (app != null) {
                    notesHandler.setEnabled(false);
                    infoTextView.setText(app.getNotes());
                    notesHandler.setEnabled(true);
                    editButton.setEnabled(true);
                    runButton.setEnabled(true);
                    pathLabel.setText("<html><body>Path: <i>" + app.getID() + "</i></body></html>");
                } else {
                    infoTextView.setText("");
                    infoTextView.setEditable(false);
                    editButton.setSelected(false);
                    editButton.setEnabled(false);
                    runButton.setEnabled(false);
                    pathLabel.setText("Path");
                }
            }
        });
        return infoBox;
    }

    /** App list transfer handler */
    class AppListTransferHandler extends TransferHandler {

        /** group that is the source of the apps being copied or moved */
        protected AppGroup _sourceGroup;

        /** transfer knobs from the knobs list */
        @Override
        protected Transferable createTransferable(final JComponent component) {
            final App app = _controller.getSelectedApp();
            return new AppTransferable(app);
        }

        /** provides copy or move operation */
        @Override
        public int getSourceActions(final JComponent component) {
            _sourceGroup = _controller.getSelectedGroup();
            return _sourceGroup == _model.getMainGroup() ? COPY : COPY_OR_MOVE;
        }

        /** perform cleanup operations */
        @Override
        protected void exportDone(final JComponent component, Transferable transferable, int action) {
            switch(action) {
                case TransferHandler.MOVE:
                    if (_sourceGroup != null && _sourceGroup != _model.getMainGroup() && transferable != null) {
                        try {
                            _sourceGroup.removeApp((App) transferable.getTransferData(AppTransferable.APP_FLAVOR));
                        } catch (java.awt.datatransfer.UnsupportedFlavorException exception) {
                            exception.printStackTrace();
                        } catch (java.io.IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /** Group list tranfer handler */
    class GroupListTransferHandler extends TransferHandler {

        /** determine if the group list can import at least one of the tranferable flavors */
        @Override
        public boolean canImport(final JComponent component, final DataFlavor[] flavors) {
            for (DataFlavor flavor : flavors) {
                if (flavor == AppTransferable.APP_FLAVOR) return true;
            }
            return false;
        }

        /** import the transferable */
        @Override
        public boolean importData(final JComponent component, final Transferable transferable) {
            final JList groupList = (JList) component;
            final int groupIndex = groupList.getSelectedIndex();
            final AppGroup group = _model.getGroup(groupIndex);
            try {
                final App app = (App) transferable.getTransferData(AppTransferable.APP_FLAVOR);
                group.addApp(app);
                return true;
            } catch (UnsupportedFlavorException exception) {
                exception.printStackTrace();
                return false;
            } catch (java.io.IOException exception) {
                exception.printStackTrace();
                return false;
            } catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
        }
    }

    /**
	 * Class to handle document events in the note text view.
	 */
    class AppNotesHandler implements DocumentListener {

        /** Enable editing of notes */
        protected boolean _enabled;

        /**
		 * Constructor
		 */
        public AppNotesHandler() {
            _enabled = false;
        }

        /**
		 * Set to false to prevent editing the application notes unintentionally such as 
		 * when a new application has been selected and momentarily the document is updated 
		 * to reveal the text of the newly selected application.  Set to true to enable
		 * editing the application notes.
		 * 
		 */
        public void setEnabled(boolean state) {
            _enabled = state;
        }

        /**
		 * DocumentListener event indicating that the notes document has changed.  Update
		 * the selected application's notes.
		 * @param event The document event
		 */
        public void changedUpdate(DocumentEvent event) {
            updateApp(event);
        }

        /**
		 * DocumentListener event indicating that text has been inserted into the notes document.  
		 * Update the selected application's notes.
		 * @param event The document event
		 */
        public void insertUpdate(DocumentEvent event) {
            updateApp(event);
        }

        /**
		 * DocumentListener event indicating that text has been removed from the notes document.  
		 * Update the selected application's notes.
		 * @param event The document event
		 */
        public void removeUpdate(DocumentEvent event) {
            updateApp(event);
        }

        /**
		 * If an application is selected and if editing is enabled, then update the notes for
		 * the selected application.
		 * @param event The document event
		 */
        protected void updateApp(DocumentEvent event) {
            App app = _controller.getSelectedApp();
            if (app == null || !_enabled) return;
            Document document = event.getDocument();
            try {
                app.setNotes(document.getText(0, document.getLength()));
            } catch (BadLocationException exception) {
                System.err.println(exception);
                LaunchWindow.this.displayError("Edit Error", "Exception while editing notes:", exception);
            }
        }
    }
}

/**
 * Implement a Transferable for the applications being dragged
 */
class AppTransferable implements Transferable {

    /** define the app flavor */
    public static final DataFlavor APP_FLAVOR;

    /** the list of flavors associated with application transfer */
    public static final DataFlavor[] FLAVORS;

    /** The application being transferred */
    protected final App _app;

    /**
	 * static initializer
	 */
    static {
        APP_FLAVOR = new DataFlavor(App.class, "App");
        FLAVORS = new DataFlavor[] { APP_FLAVOR };
    }

    /**
	 * Constructor
	 * @param app The application being transferred
	 */
    public AppTransferable(App app) {
        _app = app;
    }

    /**
	 * Get the data being transfered which in this case is simply the application
	 * @param flavor The flavor of the transfer
	 * @return The application being transfered
	 */
    public Object getTransferData(DataFlavor flavor) {
        return _app;
    }

    /**
	 * The flavors handled by this transferable which is presently just APP_FLAVOR
	 * @return the array of flavors handled
	 */
    public DataFlavor[] getTransferDataFlavors() {
        return FLAVORS;
    }

    /**
	 * Test if the specified flavor is supported by this instance.  Only APP_FLAVOR is
	 * currently supported.
	 * @param flavor The flavor to test.
	 * @return true if the flavor is among the supported flavors and false otherwise.
	 */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (int index = 0; index < FLAVORS.length; index++) {
            if (FLAVORS[index].equals(flavor)) return true;
        }
        return false;
    }
}
