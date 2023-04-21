package preprocessing.visual.presentationtier.view;

import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import preprocessing.visual.presentationtier.controller.CopyController;
import preprocessing.visual.presentationtier.controller.RedoController;
import preprocessing.visual.presentationtier.controller.RunController;
import preprocessing.visual.presentationtier.controller.UndoController;

/**
 *
 * @author Milos Kovalcik. 
 * Class of horizontal menu
 */
public class Menu extends JMenuBar {

    JMenu file, edit, tools, help;

    /**
 * Creates, fills and sets horizontal menu
 */
    Menu(WorkSpace workSpace) {
        file = new JMenu("File");
        edit = new JMenu("Edit");
        tools = new JMenu("Tools");
        help = new JMenu("Help");
        JMenuItem openItem = new JMenuItem("Open");
        openItem.addActionListener(new LoadAction(workSpace));
        openItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.Event.CTRL_MASK));
        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new SaveAction((workSpace)));
        saveItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.Event.CTRL_MASK));
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        exitItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.Event.CTRL_MASK));
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(new UndoController(workSpace));
        undoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.Event.CTRL_MASK));
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(new RedoController(workSpace));
        redoItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.Event.CTRL_MASK));
        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new CopyController(workSpace));
        copyItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.Event.CTRL_MASK));
        JMenuItem runItem = new JMenuItem("Run");
        runItem.addActionListener(new RunController(workSpace));
        runItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.Event.CTRL_MASK));
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(new About());
        aboutItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.Event.CTRL_MASK));
        file.add(openItem);
        file.add(saveItem);
        file.add(exitItem);
        edit.add(undoItem);
        edit.add(redoItem);
        tools.add(runItem);
        tools.add(copyItem);
        help.add(aboutItem);
        this.add(file);
        this.add(edit);
        this.add(tools);
        this.add(help);
    }
}

class About implements ActionListener {

    JPanel aboutPanel;

    Label myLabel;

    @Override
    public void actionPerformed(ActionEvent e) {
        aboutPanel = new JPanel();
        String text = "O autorovi: \n" + "    Meno:  " + "Milos Kovalcik \n" + "    Email:  kovalmil@fel.cvut.cz ";
        JOptionPane.showMessageDialog(new JFrame(), text, "About", JOptionPane.PLAIN_MESSAGE);
    }
}
