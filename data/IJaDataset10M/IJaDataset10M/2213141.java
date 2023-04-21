package fr.cantor.addressbook.ui.uiv2;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import fr.cantor.addressbook.generated.AddressBook_Contact;
import fr.cantor.addressbook.generated.PAddressBook;
import fr.cantor.addressbook.ui.utils.AbstractCommandAddressBook;
import fr.cantor.addressbook.ui.utils.CommandAdd;
import fr.cantor.addressbook.ui.utils.CommandDelete;
import fr.cantor.addressbook.ui.utils.CommandModify;
import fr.cantor.addressbook.utils.ToolsBox;
import fr.cantor.commore.CommoreExecutionException;
import fr.cantor.commore.CommoreInternalException;

public class UIAddressBookV2 {

    private final PAddressBook pAddressBook;

    private CustomContactTableModel contactTableModel;

    private ContactTableSelectionModel selectionModel;

    private Stack<AbstractCommandAddressBook> stackUndoCommand = new Stack<AbstractCommandAddressBook>();

    private boolean isAddContact = false;

    private boolean isSearchFailed = false;

    private boolean isSearchDone = true;

    private String querySearch = null;

    private Integer selectedLine = null;

    private Integer idDelete = 0;

    private final ImageIcon userAddIcon = new ImageIcon(UIAddressBookV2.class.getResource("icons/business_user_add.png"));

    private final ImageIcon userDeleteIcon = new ImageIcon(UIAddressBookV2.class.getResource("icons/business_user_delete.png"));

    private final ImageIcon undoIcon = new ImageIcon(UIAddressBookV2.class.getResource("icons/undo_icon.png"));

    private JTextField firstNameTextField;

    private JTextField nameTextField;

    private JTextField eMailTextField;

    private JTextField phoneTextField;

    private JTextArea addressTextArea;

    private JButton applyButton;

    private JButton undoButton;

    private JTextField searchTextField;

    private JButton clearButton;

    private JTable contactTable;

    private JFrame frame;

    /**
	 * Listen the mouse action in the text field from jPanel 
	 */
    public class PanelTextFieldMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            applyButton.setEnabled(true);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    /**
	 * Defined the exit action
	 */
    public class ExitAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 8994337147130074773L;

        public ExitAction() {
            super("Exit");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }
    }

    /**
	 * Defined the change connection action
	 */
    public class ChangeConnectionAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        private final UIAddressBookV2 uiAB;

        public ChangeConnectionAction(UIAddressBookV2 uiAB) {
            super("Change connection");
            this.uiAB = uiAB;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ToolsBox.launchUIProperties(uiAB);
        }
    }

    /**
	 * Defined the action in "Apply" Jbutton
	 */
    public class ApplyAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = -7543875389615826562L;

        public ApplyAction() {
            super("Apply");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = nameTextField.getText();
            String firstName = firstNameTextField.getText();
            String eMail = eMailTextField.getText();
            String address = addressTextArea.getText();
            String phone = phoneTextField.getText();
            AddressBook_Contact contact = new AddressBook_Contact();
            contact.setLastName(name);
            contact.setFirstName(firstName);
            contact.setEMail(eMail);
            contact.setAddress(address);
            contact.setPhone(phone);
            AbstractCommandAddressBook command = null;
            if (isAddContact) {
                selectedLine = null;
                command = new CommandAdd(frame, pAddressBook, null, contact);
                undoButton.setEnabled(true);
            } else {
                if (selectedLine != null) {
                    int id = contactTableModel.getIdFromLineInSet(selectedLine);
                    command = new CommandModify(frame, pAddressBook, id, contact);
                    undoButton.setEnabled(true);
                }
            }
            if (command != null) {
                command.doCommand();
                stackUndoCommand.push(command);
            }
            searchTextField.requestFocus();
            applyButton.setEnabled(false);
        }
    }

    /**
	 * Defined a selection model to the contact table 
	 */
    public class ContactTableSelectionModel extends DefaultListSelectionModel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 8076720081387642783L;

        @Override
        public void setSelectionInterval(int index0, int index1) {
            super.setSelectionInterval(index0, index1);
            isAddContact = false;
            selectedLine = index1;
            if (!isSearchFailed) {
                displayContactInPanel();
                firstNameTextField.requestFocus();
            }
        }

        @Override
        public void clearSelection() {
            if (selectedLine == null || isAddContact) {
                super.clearSelection();
                selectedLine = null;
            }
        }

        @Override
        public int getSelectionMode() {
            return ListSelectionModel.SINGLE_SELECTION;
        }
    }

    /**
	 * Defined a delete action 
	 */
    public class DeleteAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 8951262202102017248L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (isSearchFailed) {
                return;
            }
            if (selectedLine != null) {
                isAddContact = false;
                isSearchDone = false;
                int id = contactTableModel.getIdFromLineInSet(selectedLine);
                selectedLine = null;
                AbstractCommandAddressBook commandDelete = new CommandDelete(frame, pAddressBook, id, null, idDelete);
                idDelete++;
                modifyStackDoDeleted(id, commandDelete.getIdDelete());
                commandDelete.doCommand();
                stackUndoCommand.push(commandDelete);
                if (!undoButton.isEnabled()) {
                    undoButton.setEnabled(true);
                }
                clearContactPanel();
                selectionModel.clearSelection();
            }
        }
    }

    /**
	 * Defined a add action  
	 */
    public class AddAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = -1098358432364139906L;

        @Override
        public void actionPerformed(ActionEvent e) {
            clearTextFieldContact();
            setEnabledTextFieldPanel(true);
            applyButton.setEnabled(true);
            firstNameTextField.requestFocus();
            isAddContact = true;
            isSearchFailed = false;
            isSearchDone = false;
            selectionModel.clearSelection();
        }
    }

    /**
	 * Listen the mouse action in the search text field 
	 */
    public class SearchTextFieldMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            selectedLine = null;
            clearContactPanel();
            selectionModel.clearSelection();
            isAddContact = false;
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
    }

    /**
	 * Defined a search action  
	 */
    public class SearchAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 2123702901146173595L;

        public SearchAction() {
            super("Search");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            isSearchFailed = false;
            isAddContact = false;
            querySearch = searchTextField.getText();
            if (!querySearch.equals("")) {
                setEnabledTextFieldPanel(false);
                List<Integer> listSearch = new ArrayList<Integer>();
                try {
                    pAddressBook.searchContact(querySearch, listSearch);
                    contactTableModel.updateWithContactList(listSearch);
                    isSearchDone = true;
                } catch (Exception e1) {
                    isSearchFailed = true;
                    contactTableModel.failedSearch("No search results found");
                } finally {
                    clearButton.setEnabled(true);
                }
            } else {
                isSearchFailed = true;
                contactTableModel.failedSearch("No query");
                clearButton.setEnabled(true);
            }
        }
    }

    /**
	 * Defined a refresh action  
	 */
    public class ForceRefreshAction extends AbstractAction {

        public ForceRefreshAction() {
            super("Force Refresh");
        }

        /**
		 * 
		 */
        private static final long serialVersionUID = 3359557808684816613L;

        @Override
        public void actionPerformed(ActionEvent e) {
            isAddContact = false;
            updateTableModel(null, true);
            if (selectedLine != null) {
                displayContactInPanel();
            }
        }
    }

    /**
	 * Defined a clear action  
	 */
    public class ClearAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 7541907456338647665L;

        public ClearAction() {
            super("Clear");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            selectedLine = null;
            isAddContact = false;
            isSearchFailed = false;
            isSearchDone = false;
            searchTextField.setText("");
            searchTextField.requestFocus();
            refresh(null, false);
            clearButton.setEnabled(false);
        }
    }

    /**
	 * Defined a undo action  
	 */
    public class UndoAction extends AbstractAction {

        /**
		 * 
		 */
        private static final long serialVersionUID = 3201817035523230499L;

        @Override
        public void actionPerformed(ActionEvent e) {
            if (stackUndoCommand.empty()) {
                undoButton.setEnabled(false);
                idDelete = 0;
            } else {
                try {
                    isAddContact = false;
                    selectedLine = null;
                    isSearchDone = false;
                    clearContactPanel();
                    selectionModel.clearSelection();
                    AbstractCommandAddressBook command = stackUndoCommand.pop();
                    command.undoCommand();
                    if (command instanceof CommandDelete) {
                        modifyStackUndoDeleted(command.getId(), command.getIdDelete());
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                if (stackUndoCommand.empty()) {
                    undoButton.setEnabled(false);
                }
            }
        }
    }

    /** 
	 * Creates new form UIAddressBook
	 * @throws CommoreExecutionException 
	 * @throws CommoreInternalException
	 */
    public UIAddressBookV2(PAddressBook addressbook, JFrame jFrame) throws CommoreInternalException, CommoreExecutionException {
        this.pAddressBook = addressbook;
        this.frame = jFrame;
        initComponents();
    }

    /**
	 * Refresh the UI
	 */
    public void refresh(Integer id, boolean hasBeenModify) {
        updateTableModel(id, hasBeenModify);
        applyButton.setEnabled(false);
    }

    /**
	 * Display a selected contact in the contact panel
	 */
    public void displayContactInPanel() {
        if (selectedLine != null) {
            Integer id = contactTableModel.getIdFromLineInSet(selectedLine);
            AddressBook_Contact contact = new AddressBook_Contact();
            try {
                pAddressBook.read(id, contact);
            } catch (Exception e) {
                ToolsBox.showConnectionError(frame, e.getMessage());
                return;
            }
            setEnabledTextFieldPanel(true);
            applyButton.setEnabled(true);
            setTextFieldContact(contact);
        }
    }

    /**
	 * Update a new table model
	 */
    public void updateTableModel(Integer id, boolean hasBeenModify) {
        contactTableModel.updateContact(id, hasBeenModify);
    }

    /**
	 * Enabled a text field in the panel
	 * @param bool
	 */
    public void setEnabledTextFieldPanel(boolean bool) {
        firstNameTextField.setEnabled(bool);
        nameTextField.setEnabled(bool);
        addressTextArea.setEnabled(bool);
        eMailTextField.setEnabled(bool);
        phoneTextField.setEnabled(bool);
    }

    /**
	 * Clear the text field in the contact panel
	 */
    public void clearTextFieldContact() {
        firstNameTextField.setText("");
        nameTextField.setText("");
        addressTextArea.setText("");
        eMailTextField.setText("");
        phoneTextField.setText("");
    }

    /**
	 * Clear all object in the contact panel. 
	 */
    public void clearContactPanel() {
        clearTextFieldContact();
        setEnabledTextFieldPanel(false);
        applyButton.setEnabled(false);
    }

    public void setTextFieldContact(AddressBook_Contact contact) {
        nameTextField.setText(contact.getLastName());
        firstNameTextField.setText(contact.getFirstName());
        eMailTextField.setText(contact.getEMail());
        phoneTextField.setText(contact.getPhone());
        addressTextArea.setText(contact.getAddress());
    }

    /**
	 * Init the components to the jFrame
	 */
    private void initComponents() {
        JPanel infoContactPanel = new JPanel();
        JLabel searchLabel = new JLabel();
        JLabel firstNameLabel = new JLabel();
        firstNameTextField = new JTextField();
        JLabel nameLabel = new JLabel();
        nameTextField = new JTextField();
        JLabel eMailLabel = new JLabel();
        eMailTextField = new JTextField();
        JLabel phoneLabel = new JLabel();
        phoneTextField = new JTextField();
        JLabel addressLabel = new JLabel();
        JScrollPane areaScrollPane = new JScrollPane();
        addressTextArea = new JTextArea();
        applyButton = new JButton();
        JMenu editMenu = new JMenu();
        JMenuItem changeConectionFileMenuItem = new JMenuItem();
        searchTextField = new JTextField();
        searchTextField.addCaretListener(new CaretListener() {

            @Override
            public void caretUpdate(CaretEvent e) {
                boolean textFieldIsEmpty = searchTextField.getText().equals("");
                boolean researchActive = isSearchDone || isSearchFailed;
                if (!researchActive && textFieldIsEmpty) {
                    clearButton.setEnabled(false);
                } else {
                    clearButton.setEnabled(true);
                }
            }
        });
        JButton searchButton = new JButton();
        clearButton = new JButton();
        contactTable = new JTable();
        JScrollPane tableScrollPane = new JScrollPane();
        JToolBar toolBar = new JToolBar();
        undoButton = new JButton();
        JButton addButton = new JButton();
        JButton deleteButton = new JButton();
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        JMenuItem exitFileMenuItem = new JMenuItem();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setName("jFrame");
        frame.setFocusable(true);
        frame.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    new ForceRefreshAction().actionPerformed(null);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }
        });
        tableScrollPane.setName("tableScrollPane");
        contactTableModel = new CustomContactTableModel(this);
        selectionModel = new ContactTableSelectionModel();
        contactTable.setModel(contactTableModel);
        contactTable.setSelectionModel(selectionModel);
        contactTable.setName("contactTable");
        tableScrollPane.setViewportView(contactTable);
        searchLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        searchLabel.setName("searchLabel");
        searchLabel.setText("Enter request");
        searchTextField.setName("searchTextField");
        searchTextField.setAction(new SearchAction());
        searchTextField.addMouseListener(new SearchTextFieldMouseListener());
        searchButton.setAction(new SearchAction());
        searchButton.setName("searchButton");
        clearButton.setAction(new ClearAction());
        clearButton.setEnabled(false);
        clearButton.setName("cleanButton");
        infoContactPanel.setBorder(BorderFactory.createEtchedBorder());
        infoContactPanel.setName("infoContactPanel");
        firstNameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        firstNameLabel.setText("First Name");
        firstNameLabel.setName("firstNameLabel");
        firstNameTextField.setName("firstNameTextField");
        firstNameTextField.setEnabled(false);
        firstNameTextField.addMouseListener(new PanelTextFieldMouseListener());
        nameLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        nameLabel.setText("Last Name");
        nameLabel.setName("nameLabel");
        nameTextField.setName("nameTextField");
        nameTextField.setEnabled(false);
        nameTextField.addMouseListener(new PanelTextFieldMouseListener());
        eMailLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        eMailLabel.setText("E-mail");
        eMailLabel.setName("eMailLabel");
        eMailTextField.setName("eMailTextField");
        eMailTextField.setEnabled(false);
        eMailTextField.addMouseListener(new PanelTextFieldMouseListener());
        phoneLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        phoneLabel.setText("Phone");
        phoneLabel.setName("phoneLabel");
        phoneTextField.setName("phoneTextField");
        phoneTextField.setEnabled(false);
        phoneTextField.addMouseListener(new PanelTextFieldMouseListener());
        addressLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        addressLabel.setText("Address");
        addressLabel.setName("addressLabel");
        areaScrollPane.setName("jScrollPane2");
        addressTextArea.setColumns(20);
        addressTextArea.setRows(5);
        addressTextArea.setName("addressTextArea");
        addressTextArea.setEnabled(false);
        addressTextArea.addMouseListener(new PanelTextFieldMouseListener());
        areaScrollPane.setViewportView(addressTextArea);
        applyButton.setAction(new ApplyAction());
        applyButton.setEnabled(false);
        GroupLayout jPanel1Layout = new GroupLayout(infoContactPanel);
        infoContactPanel.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(firstNameLabel, GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE).addComponent(eMailLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addressLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(eMailTextField).addComponent(firstNameTextField, GroupLayout.DEFAULT_SIZE, 199, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(phoneLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(nameLabel, GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameTextField, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE).addComponent(phoneTextField, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))).addComponent(areaScrollPane, GroupLayout.DEFAULT_SIZE, 508, Short.MAX_VALUE))).addComponent(applyButton, GroupLayout.Alignment.TRAILING)).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(27, 27, 27).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(firstNameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(firstNameLabel).addComponent(nameLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(eMailTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(phoneTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(eMailLabel).addComponent(phoneLabel)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(areaScrollPane, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(applyButton)).addComponent(addressLabel)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.setName("toolBar");
        addButton.setAction(new AddAction());
        addButton.setIcon(userAddIcon);
        addButton.setFocusable(false);
        addButton.setHorizontalTextPosition(SwingConstants.CENTER);
        addButton.setName("addButton");
        addButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolBar.add(addButton);
        deleteButton.setAction(new DeleteAction());
        deleteButton.setIcon(userDeleteIcon);
        deleteButton.setFocusable(false);
        deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
        deleteButton.setName("deleteButton");
        deleteButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolBar.add(deleteButton);
        undoButton.setAction(new UndoAction());
        undoButton.setIcon(undoIcon);
        undoButton.setFocusable(false);
        undoButton.setEnabled(false);
        undoButton.setHorizontalTextPosition(SwingConstants.CENTER);
        undoButton.setName("undoButton");
        undoButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        toolBar.add(undoButton);
        menuBar.setName("menuBar");
        fileMenu.setText("File");
        fileMenu.setName("fileMenu");
        exitFileMenuItem.setAction(new ExitAction());
        exitFileMenuItem.setName("exitFileMenuItem");
        JMenuItem forceRefresh = new JMenuItem();
        forceRefresh.setAction(new ForceRefreshAction());
        fileMenu.add(forceRefresh);
        fileMenu.add(exitFileMenuItem);
        menuBar.add(fileMenu);
        editMenu.setText("Edit");
        editMenu.setName("editMenu");
        changeConectionFileMenuItem.setAction(new ChangeConnectionAction(this));
        changeConectionFileMenuItem.setName("changeConectionFileMenuItem");
        editMenu.add(changeConectionFileMenuItem);
        menuBar.add(editMenu);
        frame.setJMenuBar(menuBar);
        GroupLayout layout = new GroupLayout(frame.getContentPane());
        frame.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(toolBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(searchLabel, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, 234, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(searchButton).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(clearButton)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(tableScrollPane, GroupLayout.PREFERRED_SIZE, 206, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(infoContactPanel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(toolBar, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(searchButton).addComponent(clearButton).addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(searchLabel)))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(tableScrollPane, GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE).addComponent(infoContactPanel, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        frame.pack();
        addKeyListenerForAllComponent(frame);
    }

    public JTable getContactTable() {
        return contactTable;
    }

    /**
	 * Change the stack during delete order.
	 * Put has null the first id of the identical stack with id.
	 * @param id
	 * @param idDelete
	 */
    private void modifyStackDoDeleted(Integer id, Integer idDelete) {
        Stack<AbstractCommandAddressBook> tempStack = new Stack<AbstractCommandAddressBook>();
        AbstractCommandAddressBook command;
        boolean isSameId = false;
        while (!stackUndoCommand.empty() && !isSameId) {
            command = stackUndoCommand.pop();
            Integer commandId = command.getId();
            if (commandId != null) {
                isSameId = commandId.equals(id);
            }
            if (isSameId) {
                command.setId(null);
                command.setIdDelete(idDelete);
            }
            tempStack.push(command);
        }
        while (!tempStack.empty()) {
            stackUndoCommand.push(tempStack.pop());
        }
    }

    /**
	 * Change the stack during the undo delete order.
	 * Initialize the first id has null has id, if it is the same idDelete.
	 * @param id
	 * @param idDelete
	 */
    private void modifyStackUndoDeleted(Integer id, Integer idDelete) {
        Stack<AbstractCommandAddressBook> tempStack = new Stack<AbstractCommandAddressBook>();
        AbstractCommandAddressBook command;
        boolean isIdNull = false;
        boolean isSameIdDelete = false;
        while (!stackUndoCommand.empty() && !isIdNull) {
            command = stackUndoCommand.pop();
            isIdNull = command.getId() == null;
            isSameIdDelete = command.getIdDelete() == idDelete;
            if (isIdNull && isSameIdDelete) {
                command.setId(id);
            }
            tempStack.push(command);
        }
        while (!tempStack.empty()) {
            stackUndoCommand.push(tempStack.pop());
        }
    }

    public void setAddContact(boolean isAddContact) {
        this.isAddContact = isAddContact;
    }

    public Integer getSelectedLine() {
        return selectedLine;
    }

    public boolean isAddContact() {
        return isAddContact;
    }

    public void setSelectedLine(Integer selectedLine) {
        this.selectedLine = selectedLine;
    }

    public boolean isSearch() {
        return isSearchDone;
    }

    public CustomContactTableModel getContactTableModel() {
        return contactTableModel;
    }

    public PAddressBook getPAddressbook() {
        return pAddressBook;
    }

    public String getQuerySearch() {
        return querySearch;
    }

    public JFrame getJFrame() {
        return frame;
    }

    private void addKeyListenerForAllComponent(JFrame frame) {
        for (Component component : getAllSubComponent(frame)) {
            for (KeyListener keyListener : frame.getKeyListeners()) {
                component.addKeyListener(keyListener);
            }
        }
    }

    private ArrayList<Component> getAllSubComponent(Container _container) {
        ArrayList<Component> list = new ArrayList<Component>();
        Component[] _listComponent = _container.getComponents();
        for (int i = 0; i < _listComponent.length; i++) {
            list.add(_listComponent[i]);
            try {
                list.addAll(getAllSubComponent((Container) _listComponent[i]));
            } catch (Exception e) {
            }
        }
        return list;
    }
}
