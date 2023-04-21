package com.t_oster.notenschrank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import com.t_oster.notenschrank.data.SettingsManager;
import com.t_oster.notenschrank.gui.MainFrame;
import com.t_oster.notenschrank.gui.PrintWizzardDialog;
import com.t_oster.notenschrank.gui.SettingsDialog;
import com.t_oster.notenschrank.gui.SortingDialog;

public class Notenschrank implements ActionListener {

    private MainFrame mainFrame;

    private boolean running = true;

    private void checkPaths() {
        if (!SettingsManager.getInstance().getArchivePath().exists()) {
            int response = JOptionPane.showConfirmDialog(null, "Fehler: Der Archivordner '" + SettingsManager.getInstance().getArchivePath() + "' wurde nicht gefunden\n" + "Soll ein neuer angelegt werden?", "Fehler", JOptionPane.YES_NO_CANCEL_OPTION);
            if (response == JOptionPane.NO_OPTION) {
                SettingsDialog.showDialog(mainFrame, "Einstellungen");
                checkPaths();
            } else if (response == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            } else {
                if (!SettingsManager.getInstance().getArchivePath().mkdirs()) {
                    JOptionPane.showMessageDialog(null, "Fehler: Konnte Archivordner nicht anlegen.");
                    System.exit(1);
                }
            }
        }
        if (!SettingsManager.getInstance().getStackPath().exists()) {
            int response = JOptionPane.showConfirmDialog(null, "Fehler: Der Scannerordner '" + SettingsManager.getInstance().getStackPath() + "' wurde nicht gefunden\n" + "Soll ein neuer angelegt werden?", "Fehler", JOptionPane.YES_NO_CANCEL_OPTION);
            if (response == JOptionPane.NO_OPTION) {
                SettingsDialog.showDialog(mainFrame, "Einstellungen");
                checkPaths();
            } else if (response == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            } else {
                if (!SettingsManager.getInstance().getStackPath().mkdirs()) {
                    JOptionPane.showMessageDialog(null, "Fehler: Konnte Scannerordner nicht anlegen.");
                    System.exit(1);
                }
            }
        }
    }

    public Notenschrank() {
        this.mainFrame = new MainFrame();
        this.mainFrame.addActionListener(this);
        this.checkPaths();
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Couldn't load system style. running in java style");
        }
        Notenschrank ns = new Notenschrank();
        while (ns.running) {
            try {
                synchronized (ns.mainFrame) {
                    ns.mainFrame.wait();
                }
            } catch (InterruptedException e) {
            }
        }
    }

    private void showSortWizzard() {
        new SortingDialog(mainFrame, SettingsManager.getInstance().getPreferredSortingDialogLayout()).showDialog();
    }

    private void showPrintWizzard() {
        new PrintWizzardDialog(mainFrame).showDialog();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(this.mainFrame)) {
            MainFrame.Action a = MainFrame.Action.values()[e.getID()];
            switch(a) {
                case actionPrintClicked:
                    {
                        this.showPrintWizzard();
                        break;
                    }
                case actionScanClicked:
                    {
                        this.showSortWizzard();
                        break;
                    }
                case actionSettingsClicked:
                    {
                        SettingsDialog.showDialog(mainFrame, "Einstellungen");
                        break;
                    }
                case actionCloseClicked:
                    {
                        this.running = false;
                        this.mainFrame.dispose();
                        synchronized (this.mainFrame) {
                            this.mainFrame.notify();
                        }
                        break;
                    }
            }
        }
    }
}
