package org.ufacekit.ui.swing.databinding.example.multithreads;

import java.awt.GridLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.Realm;
import org.ufacekit.ui.swing.databinding.example.model.User;
import org.ufacekit.ui.swing.databinding.swing.SwingEventConstants;
import org.ufacekit.ui.swing.databinding.swing.SwingObservables;
import org.ufacekit.ui.swing.databinding.swing.SwingRealm;

/**
 * Test with multi thread context. One thread (main) bind values and another Thread 
 * (set AAA into Model after waiting 2000 ms). This test Realm implementation wich
 * manage muti thread.
 * Bind JavaBean User name getter/setter (java.lang.String) with Swing
 * JTextField. Wait 2000 ms and set AAA into Model (width another Thread).
 * 
 */
public class BindBetweenTextboxAndPOJO {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 2, 20, 20));
        JTextField text1 = new JTextField();
        panel.add(text1);
        frame.getContentPane().add(panel);
        final User model = new User("djo");
        SwingRealm.createDefault();
        Realm realm = SwingObservables.getRealm();
        DataBindingContext context = new DataBindingContext(realm);
        context.bindValue(SwingObservables.observeText(text1, SwingEventConstants.Modify), BeansObservables.observeValue(realm, model, "name"), null, null);
        frame.pack();
        frame.setVisible(true);
        new Thread(new Runnable() {

            public void run() {
                try {
                    Thread.sleep(2000);
                    model.setName("AAA");
                } catch (InterruptedException e1) {
                }
            }
        }).start();
    }
}
