package fasper.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

class BaseMenuBar extends JMenuBar {

    private Fasper parent;

    private ModelHub model;

    private JMenu file = new JMenu("File"), help = new JMenu("Help");

    private JToolBar toolBar = null;

    private LoadAction load;

    private ReloadAction reload;

    private SaveAction save;

    private ExitAction exit;

    private HelpAction helpa;

    private AboutAction about;

    /**
    * 
    */
    public BaseMenuBar(Fasper _parent, ModelHub _model) {
        super();
        parent = _parent;
        model = _model;
        load = new LoadAction(model);
        reload = new ReloadAction(model);
        save = new SaveAction(model);
        exit = new ExitAction(model);
        helpa = new HelpAction(model);
        about = new AboutAction(model);
        file.add(load.getMenuItem());
        file.add(reload.getMenuItem());
        file.add(save.getMenuItem());
        file.addSeparator();
        file.add(exit.getMenuItem());
        help.add(helpa.getMenuItem());
        help.add(about.getMenuItem());
        super.add(file);
        super.add(Box.createHorizontalGlue());
        super.add(help);
        super.setBorder(BorderFactory.createEtchedBorder());
        super.setBorderPainted(true);
        initToolBar();
        parent.setJMenuBar(this);
        showToolBar();
    }

    private void initToolBar() {
        toolBar = new JToolBar(JToolBar.HORIZONTAL);
        toolBar.setFloatable(true);
        toolBar.setOpaque(true);
        toolBar.add(load.getButton());
        toolBar.add(reload.getButton());
        toolBar.addSeparator();
        toolBar.add(save.getButton());
    }

    private void showToolBar() {
        toolBar.setEnabled(true);
        parent.getContentPane().add(toolBar, BorderLayout.NORTH);
        parent.validate();
    }

    void setToolBarEnable(boolean _enable) {
        Component[] _buts = toolBar.getComponents();
        for (int _i = 0; _i < _buts.length; _i++) {
            if (_buts[_i] instanceof RollOverButton) ((JButton) _buts[_i]).setEnabled(_enable);
        }
    }

    class LoadAction extends BaseAction {

        public LoadAction(ModelHub _parent) {
            super(_parent, "Load", "resources/load.gif");
        }

        public void actionPerformed(ActionEvent e) {
            model.load(true);
        }
    }

    class ReloadAction extends BaseAction {

        public ReloadAction(ModelHub _parent) {
            super(_parent, "Reload", "resources/reload.gif");
        }

        public void actionPerformed(ActionEvent e) {
            model.reload();
        }
    }

    class SaveAction extends BaseAction {

        public SaveAction(ModelHub _parent) {
            super(_parent, "Save...", "resources/save.gif");
        }

        public void actionPerformed(ActionEvent e) {
            model.save();
        }
    }

    class ExitAction extends BaseAction {

        public ExitAction(ModelHub _parent) {
            super(_parent, "Exit");
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("\nFasper exits . . .");
            System.exit(0);
        }
    }

    class AboutAction extends BaseAction {

        public AboutAction(ModelHub _parent) {
            super(_parent, "About", "resources/about.gif");
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("Soon there will be an AboutAction");
        }
    }

    class HelpAction extends BaseAction {

        public HelpAction(ModelHub _parent) {
            super(_parent, "Help Contents", "resources/help.gif");
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("Soon there will be a HelpAction");
        }
    }
}
