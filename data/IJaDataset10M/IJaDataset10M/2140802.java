package org.xmlcml.cmlimpl.jumbo;

import java.util.Hashtable;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/** monitors all Window and shuts down program when last one has been closed.To use
<pre>
 		JFrame jf = new JFrame();
		JumboWindowListener.addWindow(jf);
</pre>
*/
public class JumboWindowListener extends WindowAdapter {

    static Hashtable windowTable = new Hashtable();

    static JumboWindowListener theJWL = new JumboWindowListener();

    private JumboWindowListener() {
    }

    public void windowClosing(WindowEvent we) {
        Window w = we.getWindow();
        w.setVisible(false);
        w.dispose();
        windowTable.remove(w);
        if (windowTable.size() == 0) System.exit(0);
    }

    public static void addWindow(Window w) {
        w.addWindowListener(theJWL);
        windowTable.put(w, "");
    }

    public static void testFrame() {
        JFrame jf = new JFrame();
        jf.setSize(400, 400);
        JumboWindowListener.addWindow(jf);
        jf.setVisible(true);
    }

    public static void main(String[] args) {
        testFrame();
        testFrame();
    }
}
