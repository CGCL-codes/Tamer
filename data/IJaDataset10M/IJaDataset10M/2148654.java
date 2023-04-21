package edu.hawaii.ics.ami.app.estream.view;

import edu.hawaii.ics.ami.app.estream.control.CreateExperimentFileAction;
import edu.hawaii.ics.ami.app.estream.control.OpenExperimentFileAction;
import edu.hawaii.ics.ami.app.estream.control.SaveExperimentFileAction;
import edu.hawaii.ics.ami.app.estream.control.OpenAnalysisFileAction;
import edu.hawaii.ics.ami.app.estream.control.OpenCollectionFileAction;
import edu.hawaii.ics.ami.app.estream.control.OpenViewFileAction;
import edu.hawaii.ics.ami.app.estream.control.ExportDataAction;
import edu.hawaii.ics.ami.app.estream.control.EditCutAction;
import edu.hawaii.ics.ami.app.estream.control.EditCopyAction;
import edu.hawaii.ics.ami.app.estream.control.EditPasteAction;
import edu.hawaii.ics.ami.app.estream.control.ExitAction;
import edu.hawaii.ics.ami.app.estream.model.DataAnalysis;
import edu.hawaii.ics.ami.app.estream.model.DataCollection;
import edu.hawaii.ics.ami.app.estream.model.DataView;
import edu.hawaii.ics.ami.app.estream.model.EventStream;
import edu.hawaii.ics.ami.app.experiment.model.Experiment;
import java.awt.Frame;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import java.io.IOException;
import king.lib.access.ResourceHookup;
import com.Ostermiller.util.Browser;

/**
 * Main menu bar for the event stream application.
 * 
 * @author  king
 * @since   October 4, 2004
 */
public class EventStreamMenuBar extends JMenuBar implements Observer {

    /** The EventStream basis class associated with this panel. */
    private EventStream eventStream;

    /** Menu item for save experiment. */
    private JMenuItem fileMenuSaveExp;

    /** Menu item for export data. */
    private JMenuItem exportData;

    /** Window group to group window menu items. */
    private ButtonGroup windowGroup = new ButtonGroup();

    /** Window menu. Shows all the available windows. */
    private JMenu windowMenu;

    /** List of to window menu corresponding components. */
    private List<Object> windowComponents = new ArrayList<Object>();

    /**
   * Constructor for the menu bar.
   * 
   * @param frame  The frame this menu bar is assocatiated with. 
   * @param eventStream  The event stream object for data analysis and collection.
   */
    public EventStreamMenuBar(final Frame frame, final EventStream eventStream) {
        this.eventStream = eventStream;
        JMenu fileMenu = new JMenu("File");
        add(fileMenu);
        JMenuItem fileMenuCreateExp = new JMenuItem(new CreateExperimentFileAction(frame, eventStream));
        fileMenu.add(fileMenuCreateExp);
        JMenuItem fileMenuOpenExp = new JMenuItem(new OpenExperimentFileAction(frame, eventStream));
        fileMenu.add(fileMenuOpenExp);
        this.fileMenuSaveExp = new JMenuItem(new SaveExperimentFileAction(frame, eventStream));
        fileMenu.add(this.fileMenuSaveExp);
        this.fileMenuSaveExp.setEnabled(false);
        fileMenu.addSeparator();
        JMenuItem fileMenuRecord = new JMenuItem(new OpenCollectionFileAction(frame, eventStream));
        fileMenu.add(fileMenuRecord);
        fileMenu.addSeparator();
        JMenuItem fileMenuOpen = new JMenuItem(new OpenAnalysisFileAction(frame, eventStream));
        fileMenu.add(fileMenuOpen);
        this.exportData = new JMenuItem(new ExportDataAction(frame, this.eventStream));
        fileMenu.add(this.exportData);
        this.exportData.setEnabled(false);
        fileMenu.addSeparator();
        JMenuItem fileMenuView = new JMenuItem(new OpenViewFileAction(frame, eventStream));
        fileMenu.add(fileMenuView);
        fileMenu.addSeparator();
        JMenuItem fileMenuExit = new JMenuItem(new ExitAction(frame, eventStream));
        fileMenu.add(fileMenuExit);
        JMenu editMenu = new JMenu("Edit");
        add(editMenu);
        JMenuItem editMenuCut = new JMenuItem(new EditCutAction(frame, eventStream));
        editMenu.add(editMenuCut);
        JMenuItem editMenuCopy = new JMenuItem(new EditCopyAction(frame, eventStream));
        editMenu.add(editMenuCopy);
        JMenuItem editMenuPaste = new JMenuItem(new EditPasteAction(frame, eventStream));
        editMenu.add(editMenuPaste);
        this.windowMenu = new JMenu("Window");
        add(windowMenu);
        JMenu helpMenu = new JMenu("Help");
        add(helpMenu);
        Browser.init();
        JMenuItem helpMenuHelpContents = new JMenuItem("Help Contents");
        Image image = ResourceHookup.getInstance().getTrackedImage("conf/image/icon/help.gif");
        helpMenuHelpContents.setIcon(new ImageIcon(image));
        helpMenuHelpContents.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    Browser.displayURLinNew("http://www.dataexplorer.net");
                } catch (IOException e) {
                    System.err.println("Error opening URL: " + e);
                }
            }
        });
        helpMenu.add(helpMenuHelpContents);
        helpMenu.addSeparator();
        JMenuItem helpMenuAbout = new JMenuItem("About Event Stream");
        helpMenuAbout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                EventStreamAboutDialog dialog = new EventStreamAboutDialog(frame);
                Dimension frameSize = frame.getSize();
                Dimension dialogSize = dialog.getSize();
                dialog.setLocation((frameSize.width - dialogSize.width) / 2, (frameSize.height - dialogSize.height) / 2);
                dialog.setVisible(true);
            }
        });
        helpMenu.add(helpMenuAbout);
        this.eventStream.addObserver(this);
    }

    /**
   * Called, when the observable changed that this object is showing.
   * 
   * @param observable  The object that is being observed.
   * @param object  The change that occured.
   */
    public void update(Observable observable, Object object) {
        EventStream.Update update = (EventStream.Update) object;
        if (update == EventStream.Update.ACTIVATE) {
            Object component = this.eventStream.getActiveComponent();
            if (component instanceof DataAnalysis) {
                this.fileMenuSaveExp.setEnabled(false);
                this.exportData.setEnabled(true);
            } else if (component instanceof Experiment) {
                this.fileMenuSaveExp.setEnabled(true);
                this.exportData.setEnabled(false);
            } else {
                this.fileMenuSaveExp.setEnabled(false);
                this.exportData.setEnabled(false);
            }
            for (int i = 0; i < this.windowComponents.size(); i++) {
                if (component == this.windowComponents.get(i)) {
                    this.windowMenu.getItem(i).setSelected(true);
                }
            }
        } else if (update == EventStream.Update.ADD) {
            Object component = this.eventStream.getComponents().get(this.eventStream.getComponents().size() - 1);
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(component.toString());
            if (component instanceof DataAnalysis) {
                Image image = ResourceHookup.getInstance().getTrackedImage("conf/image/icon/open.gif");
                menuItem.setIcon(new ImageIcon(image));
            } else if (component instanceof DataCollection) {
                Image image = ResourceHookup.getInstance().getTrackedImage("conf/image/icon/record.gif");
                menuItem.setIcon(new ImageIcon(image));
            } else if (component instanceof DataView) {
                Image image = ResourceHookup.getInstance().getTrackedImage("conf/image/icon/empty.gif");
                menuItem.setIcon(new ImageIcon(image));
            } else if (component instanceof Experiment) {
                Image image = ResourceHookup.getInstance().getTrackedImage("conf/image/icon/edit.gif");
                menuItem.setIcon(new ImageIcon(image));
            }
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent actionEvent) {
                    JRadioButtonMenuItem source = (JRadioButtonMenuItem) actionEvent.getSource();
                    for (int i = 0; i < windowMenu.getItemCount(); i++) {
                        if (windowMenu.getItem(i) == source) {
                            eventStream.setActiveComponent(windowComponents.get(i));
                        }
                    }
                }
            });
            this.windowGroup.add(menuItem);
            this.windowMenu.add(menuItem);
            this.windowComponents.add(component);
            menuItem.setSelected(true);
        } else if (update == EventStream.Update.REMOVE) {
            List<Object> components = this.eventStream.getComponents();
            for (int i = 0; i < this.windowComponents.size(); i++) {
                boolean found = false;
                for (int k = 0; k < components.size(); k++) {
                    if (components.get(k) == this.windowComponents.get(i)) {
                        found = true;
                    }
                }
                if (!found) {
                    Object component = this.windowComponents.get(i);
                    JMenuItem menuItem = this.windowMenu.getItem(i);
                    this.windowGroup.remove(menuItem);
                    this.windowMenu.remove(menuItem);
                    this.windowComponents.remove(component);
                }
            }
        }
    }
}
