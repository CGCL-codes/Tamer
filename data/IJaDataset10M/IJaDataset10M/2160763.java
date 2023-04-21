package org.qfirst.batavia.test;

import javax.swing.*;
import java.util.*;
import org.qfirst.batavia.*;
import org.qfirst.batavia.ui.*;
import org.qfirst.batavia.mime.ui.*;
import org.qfirst.batavia.mime.*;

/**
 *  Description of the Class
 *
 * @author	francisdobi
 * @created    May 27, 2004
 */
public class TestView {

    /**
	 *  Description of the Method
	 *
	 * @param  args  Description of the Parameter
	 */
    public static void main(String args[]) {
        JFrame f = new JFrame();
        MimeOption mo = new MimeOption();
        MimeConfiguration mc = new MimeConfiguration();
        mc.setMimeType("image/png");
        mc.addHandler(new ExternalMimeHandler("gimp {file}", ExternalMimeHandler.EDITOR));
        mo.updateGUI(mc);
        f.getContentPane().add((JComponent) mo.getComponentAt(0));
        f.pack();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.show();
    }
}
