package com.sderhy;

import java.awt.*;
import java.awt.event.*;

public class myFrame extends java.awt.Frame implements WindowListener, ActionListener, ComponentListener {

    public String[] menuFile = new String[] { "Close", "Print...", "Save...", "" };

    public String[] fileCommands = new String[] { "close", "print", "save", "" };

    public String[] menuEdit = new String[] { "Undo", "Cut", "Copy", "Past", "Reset" };

    public String[] editCommands = new String[] { "undo", "cut", "copy", "past", "reset" };

    public String[] menuScale = new String[] { "Normal Size", " 25 %", " 50 %", " 75 %", " 200 %", " 300 %" };

    public String[] scaleCommands = new String[] { "NormalSize", "by25", "by50", "by75", "by200", "by300" };

    public String[] menuProcess = new String[] { "Reset", "Invert", "256 Grays ", "FlipVertical", "Flip Horizontal", "Brighten Up", "Pseudo Colors", "Rotate Left", "Rotate Right", "Sharpen", "Blur" };

    public String[] processCommands = new String[] { "reset", "invertLut", "gray", "flipV", "flipH", "brighten", "pseudoColor", "rotateL", "rotateR", "sharpen", "blur" };

    public String[] menuWindow = new String[] { "Next Window", "Previous Window" };

    public String[] windowCommand = new String[] { "next", "previous" };

    protected static int numWindows = 0;

    private int isNumber;

    public MenuBar mb;

    public myFrame() {
        this("");
    }

    public myFrame(String title) {
        super(title);
        this.addWindowListener(this);
        this.arrange();
        isNumber = numWindows++;
    }

    protected void panelize() {
        this.setBackground(Color.black);
        this.add("South", new Panel());
        this.add("North", new Panel());
        this.pack();
    }

    public int getWindowNumber() {
        return isNumber;
    }

    public int getTotalNumberOfWindowsOpened() {
        return numWindows;
    }

    protected void arrange() {
        MenuItem m = null;
        int index = 0;
        mb = new MenuBar();
        this.setMenuBar(mb);
        Menu file = new Menu("File");
        file.add(m = new MenuItem(menuFile[index], new MenuShortcut(KeyEvent.VK_W)));
        m.addActionListener(this);
        m.setActionCommand(fileCommands[index]);
        file.add(m = new MenuItem(menuFile[++index], new MenuShortcut(KeyEvent.VK_P)));
        m.addActionListener(this);
        m.setActionCommand(fileCommands[index]);
        file.add(m = new MenuItem(menuFile[++index], new MenuShortcut(KeyEvent.VK_S)));
        m.addActionListener(this);
        m.setActionCommand(fileCommands[index]);
        mb.add(file);
        index = 0;
        Menu edit = new Menu("Edit");
        edit.add(m = new MenuItem(menuEdit[index], new MenuShortcut(KeyEvent.VK_U)));
        m.addActionListener(this);
        m.setActionCommand(editCommands[index]);
        edit.add(m = new MenuItem(menuEdit[++index], new MenuShortcut(KeyEvent.VK_X)));
        m.addActionListener(this);
        m.setActionCommand(editCommands[index]);
        edit.add(m = new MenuItem(menuEdit[++index], new MenuShortcut(KeyEvent.VK_C)));
        m.addActionListener(this);
        m.setActionCommand(editCommands[index]);
        edit.add(m = new MenuItem(menuEdit[++index], new MenuShortcut(KeyEvent.VK_V)));
        m.addActionListener(this);
        m.setActionCommand(editCommands[index]);
        edit.addSeparator();
        edit.add(m = new MenuItem(menuEdit[++index], new MenuShortcut(KeyEvent.VK_R)));
        m.addActionListener(this);
        m.setActionCommand(editCommands[index]);
        mb.add(edit);
        Menu win = new Menu("Window");
        win.add(m = new MenuItem(menuWindow[0], new MenuShortcut(KeyEvent.VK_N)));
        m.addActionListener(this);
        m.setActionCommand(windowCommand[0]);
        win.add(m = new MenuItem(menuWindow[1], new MenuShortcut(KeyEvent.VK_L)));
        m.addActionListener(this);
        m.setActionCommand(windowCommand[1]);
        mb.add(win);
        Menu scale = new Menu("Scale");
        for (int i = 0; i < menuScale.length; i++) {
            scale.add(m = new MenuItem(menuScale[i], new MenuShortcut(KeyEvent.VK_0 + i)));
            m.addActionListener(this);
            m.setActionCommand(scaleCommands[i]);
        }
        mb.add(scale);
        Menu process = new Menu("Process");
        process.add(m = new MenuItem(menuProcess[0], new MenuShortcut(KeyEvent.VK_R)));
        m.addActionListener(this);
        m.setActionCommand(processCommands[0]);
        process.addSeparator();
        process.add(m = new MenuItem(menuProcess[1], new MenuShortcut(KeyEvent.VK_I)));
        m.addActionListener(this);
        m.setActionCommand(processCommands[1]);
        process.add(m = new MenuItem(menuProcess[2], new MenuShortcut(KeyEvent.VK_G)));
        m.addActionListener(this);
        m.setActionCommand(processCommands[2]);
        process.add(m = new MenuItem(menuProcess[3]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[3]);
        process.add(m = new MenuItem(menuProcess[4]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[4]);
        process.add(m = new MenuItem(menuProcess[5]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[5]);
        process.add(m = new MenuItem(menuProcess[6]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[6]);
        process.add(m = new MenuItem(menuProcess[7]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[7]);
        process.add(m = new MenuItem(menuProcess[8]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[8]);
        process.add(m = new MenuItem(menuProcess[9]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[9]);
        process.add(m = new MenuItem(menuProcess[10]));
        m.addActionListener(this);
        m.setActionCommand(processCommands[10]);
        mb.add(process);
    }

    public void actionPerformed(ActionEvent e) {
        String commande = e.getActionCommand();
        if (commande.equals("close")) dispose(); else if (commande.equals("next")) Winager.next(); else if (commande.equals("previous")) Winager.previous(); else tools.Tools.debug("This command " + commande + " is not yet implemented");
    }

    public void hide() {
        numWindows--;
        Winager.remove(this);
        super.hide();
    }

    public void show() {
        Winager.add(this);
        super.show();
    }

    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        tools.Tools.debug(this, "from component Listener");
    }

    ;

    public void componentMoved(ComponentEvent e) {
    }

    ;

    public void componentShown(ComponentEvent e) {
    }

    ;

    public void componentHidden(ComponentEvent e) {
    }

    ;
}
