package net.sourceforge.pplay.digraphmain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import net.sourceforge.pplay.digraph.*;
import net.sourceforge.pplay.digraphfile.*;
import net.sourceforge.pplay.digraphview.*;

/**
 * in this class, the listener first checks to see if the string containing
 * the pathname is not empty. If the string is empty it executes the 
 * behavior of SaveAsListener, other wise it makes a call to the Graphics 
 * subsystem to get the current digraph(the graphics subsystem makes a call 
 * to the data subsystem internally to get the digraph). Then it calls the 
 * file handler subsystem to execute the save, passing it the digraph and the path. 
 * 
 * @param void
 * @return void
 * @version 3-25-2002 v0.9
 */
public class SaveItem extends JMenuItem {

    public SaveItem(DigraphView view, mainFrame frame) {
        super("Save");
        setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        final mainFrame frame2 = frame;
        final DigraphView view2 = view;
        addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (frame2.savedOnce()) {
                    File tobeSaved = new File(frame2.getFilePath());
                    FrameUtility.saveDigraph(frame2, view2, tobeSaved);
                } else {
                    FrameUtility.saveDigraph(frame2, view2);
                }
            }
        });
    }
}
