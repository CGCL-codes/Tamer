package ctrl;

import db.DatabaseAccess;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import model.Model;
import view.Gui;

public class Main {

    public Main() {
        SecurePrompt sp = new SecurePrompt(null);
        while (true) {
            sp.setVisible(true);
            while (sp.isVisible()) try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException ex) {
            }
            if (sp._uid.getText().length() + sp._pwd.getPassword().length == 0) {
                sp.dispose();
                return;
            }
            if (!Model.login(sp._uid.getText(), String.valueOf(sp._pwd.getPassword()))) {
                JOptionPane.showMessageDialog(sp, "Incorrect username and/or password.");
            } else break;
        }
        Gui ui = new Gui();
        Model mdl = new Model();
        Ctrl ctrl = new Ctrl(ui, mdl);
        ui.set(ctrl);
        ui.init();
        mdl.removeOrphans();
        DatabaseAccess.getInstance().close();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception exception) {
                    System.out.println("Could not set " + "look and feel.");
                }
                UIManager.put("swing.boldMetal", false);
                new Main();
            }
        });
    }
}
