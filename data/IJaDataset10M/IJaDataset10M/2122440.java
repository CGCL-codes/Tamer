package com.hp.hpl.guess.ui;

import com.hp.hpl.guess.Guess;
import com.hp.hpl.guess.InterpreterAbstraction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class NodeEditorPopup extends GraphElementEditorPopup {

    private static final long serialVersionUID = 3633040646227588067L;

    public static EditorPopup singleton = null;

    private static JMenu animMenu = null;

    public static JMenuItem addItemToAnimation(String s) {
        EditorPopup ep = getPopup();
        if (!ep.sep) {
            ep.addSeparator();
            ep.sep = true;
        }
        if (animMenu == null) {
            animMenu = new JMenu("Animations");
            ep.add(animMenu);
        }
        JMenuItem jmi = ep.createJMI(s);
        animMenu.add(jmi);
        return jmi;
    }

    private static JMenu layoutMenu = null;

    public static JMenuItem addLayoutItem(String s) {
        EditorPopup ep = getPopup();
        if (!ep.sep) {
            ep.addSeparator();
            ep.sep = true;
        }
        if (layoutMenu == null) {
            layoutMenu = new JMenu("Layout");
            ep.add(layoutMenu);
        }
        JMenuItem jmi = ep.createJMI(s);
        layoutMenu.add(jmi);
        return (jmi);
    }

    public static JMenuItem addItem(String s) {
        EditorPopup ep = getPopup();
        if (!ep.sep) {
            ep.addSeparator();
            ep.sep = true;
        }
        JMenuItem jmi = ep.createJMI(s);
        ep.add(jmi);
        return (jmi);
    }

    public static EditorPopup getPopup() {
        if (singleton == null) {
            singleton = new NodeEditorPopup(Guess.getInterpreter());
        }
        return (singleton);
    }

    protected NodeEditorPopup(InterpreterAbstraction jython) {
        super(jython);
        setLabel("Node Menu");
    }
}
