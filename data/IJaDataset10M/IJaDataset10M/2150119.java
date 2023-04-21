package com.greentea.relaxation.jnmf;

import com.greentea.relaxation.jnmf.gui.MainFrame;
import com.greentea.relaxation.jnmf.localization.Localizer;
import javax.swing.*;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA. User: GreenTea Date: 29.01.2009 Time: 21:44:10 To change this template
 * use File | Settings | File Templates.
 */
public class Program {

    public static void main(String[] args) {
        if (args.length > 0) {
            Localizer.setLocale(new Locale(args[0]));
        }
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                MainFrame inst = new MainFrame();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }
}
