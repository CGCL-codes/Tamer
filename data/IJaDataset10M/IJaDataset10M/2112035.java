package org.fudaa.dodico.common;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import com.memoire.bu.BuPreferencesFrame;
import org.fudaa.dodico.commun.DodicoLib;
import org.fudaa.dodico.commun.DodicoPreferencesPanel;

/**
 * @author fred deniger
 * @version $Id: TestDodicoPrefPanel.java,v 1.2 2007-01-19 13:07:19 deniger Exp $
 */
public class TestDodicoPrefPanel {

    /**
   * Permet d'editer le panneau de preferences.
   *
   * @param args
   */
    public static void main(String[] args) {
        final JFrame frame = new JFrame(DodicoLib.getS("Preferences des serveurs"));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final BuPreferencesFrame iframe = new BuPreferencesFrame();
        iframe.addInternalFrameListener(new InternalFrameAdapter() {

            public void internalFrameClosed(InternalFrameEvent _e) {
                frame.dispose();
            }
        });
        iframe.addTab(new DodicoPreferencesPanel());
        iframe.setClosable(false);
        iframe.setIconifiable(false);
        frame.setContentPane(iframe);
        iframe.setTitle("Dodico");
        iframe.pack();
        iframe.show();
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.show();
    }
}
