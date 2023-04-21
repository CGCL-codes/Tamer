package basys.client.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import basys.LocaleResourceBundle;
import basys.Util;
import basys.client.Project;
import basys.client.commands.AddDeviceToRoomCommand;
import basys.client.commands.AddSensorCommand;
import basys.client.commands.AddStructuralElementCommand;
import basys.client.ui.dialogs.NewEndDeviceDialog;

/**
 * EditorContextMenu.java
 * 
 * 
 * @author	olli
 * @version $Id: EditorContextMenu.java,v 1.1 2004/01/14 21:38:40 oalt Exp $
 * 
 */
public class EditorContextMenu extends JPopupMenu implements ActionListener {

    private static ResourceBundle locale = LocaleResourceBundle.getLocale();

    private Project p;

    private String id;

    /**
	 * 
	 */
    public EditorContextMenu(Project p, String id) {
        super();
        this.p = p;
        this.id = id;
        initUI();
    }

    private void initUI() {
        JMenuItem mitem;
        if (this.id.startsWith("project")) {
            mitem = new JMenuItem(locale.getString("km.newbuilding"));
            mitem.setIcon(Util.getIcon("building.png"));
            mitem.setActionCommand("new-building");
            mitem.addActionListener(this);
            this.add(mitem);
        } else if (this.id.startsWith("building")) {
            mitem = new JMenuItem(locale.getString("km.newfloor"));
            mitem.setActionCommand("new-floor");
            mitem.setIcon(Util.getIcon("floor.png"));
            mitem.addActionListener(this);
            this.add(mitem);
        } else if (this.id.startsWith("floor")) {
            mitem = new JMenuItem(locale.getString("km.newroom"));
            mitem.setIcon(Util.getIcon("room.png"));
            mitem.setActionCommand("new-room");
            mitem.addActionListener(this);
            this.add(mitem);
        } else if (this.id.startsWith("room")) {
            mitem = new JMenuItem(locale.getString("km.newjunctionbox"));
            mitem.setIcon(Util.getIcon("junctionbox.png"));
            mitem.setActionCommand("new-junctionbox");
            mitem.addActionListener(this);
            this.add(mitem);
            this.addSeparator();
            mitem = new JMenuItem(locale.getString("km.newlamp"));
            mitem.setIcon(Util.getIcon("lamp16x16.png"));
            mitem.setActionCommand("dev-" + NewEndDeviceDialog.LAMP);
            mitem.addActionListener(this);
            this.add(mitem);
            mitem = new JMenuItem(locale.getString("km.newdimlamp"));
            mitem.setIcon(Util.getIcon("dimminglamp16x16.png"));
            mitem.setActionCommand("dev-" + NewEndDeviceDialog.DIMMABLE_LAMP);
            mitem.addActionListener(this);
            this.add(mitem);
            mitem = new JMenuItem(locale.getString("km.newvalve"));
            mitem.setIcon(Util.getIcon("valve16x16.png"));
            mitem.setActionCommand("dev-" + NewEndDeviceDialog.VALVE);
            mitem.addActionListener(this);
            this.add(mitem);
            mitem = new JMenuItem(locale.getString("km.newjal"));
            mitem.setIcon(Util.getIcon("jal16x16.png"));
            mitem.setActionCommand("dev-" + NewEndDeviceDialog.JALOUSIE);
            mitem.addActionListener(this);
            this.add(mitem);
            this.addSeparator();
            mitem = new JMenuItem(locale.getString("km.newsensor"));
            mitem.setIcon(Util.getIcon("sensor16x16.png"));
            mitem.setActionCommand("sensor");
            mitem.addActionListener(this);
            this.add(mitem);
        }
        if (!this.id.startsWith("junctionbox") && !this.id.startsWith("project")) {
            this.addSeparator();
        }
        if (!this.id.startsWith("project")) {
            mitem = new JMenuItem(locale.getString("mi.cut"));
            mitem.setIcon(Util.getIcon("Cut.gif"));
            mitem.setActionCommand("cut");
            mitem.addActionListener(this);
            this.add(mitem);
            mitem = new JMenuItem(locale.getString("mi.copy"));
            mitem.setIcon(Util.getIcon("Copy.gif"));
            mitem.setActionCommand("copy");
            mitem.addActionListener(this);
            this.add(mitem);
            mitem = new JMenuItem(locale.getString("mi.paste"));
            mitem.setIcon(Util.getIcon("Paste.gif"));
            mitem.setActionCommand("paste");
            mitem.addActionListener(this);
            mitem.setEnabled(!this.p.getApplication().isBufferEmpty());
            this.add(mitem);
            mitem = new JMenuItem(locale.getString("mi.delete"));
            mitem.setIcon(Util.getIcon("Delete.gif"));
            mitem.setActionCommand("delete");
            mitem.addActionListener(this);
            this.add(mitem);
        }
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.startsWith("new-")) {
            AddStructuralElementCommand command = new AddStructuralElementCommand(null, p.getArchitecturalDataModel(), this.id, cmd.replaceAll("new-", ""));
            if (command.execute()) {
            }
        } else if (cmd.startsWith("dev-")) {
            AddDeviceToRoomCommand command = new AddDeviceToRoomCommand(null, this.p, this.id, cmd.replaceAll("dev-", ""), null);
            if (command.execute()) {
            }
        } else if (cmd.equals("sensor")) {
            AddSensorCommand command = new AddSensorCommand(null, this.p, this.id, null);
            if (command.execute()) {
            }
        }
    }
}
