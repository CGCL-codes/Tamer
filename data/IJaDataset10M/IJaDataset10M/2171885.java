package uk.org.dbmm.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import uk.org.dbmm.model.List;
import uk.org.dbmm.model.Roster;
import uk.org.dbmm.view.ListWindow;
import uk.org.dbmm.view.RosterPrinter;

/**
 * @author tpartrid
 * 
 */
public class DB1 implements ActionListener {

    private static DB1 db1;

    private ListWindow listWindow;

    private Roster roster;

    private List list;

    private final JFileChooser fc = new JFileChooser();

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        db1 = new DB1();
        db1.initialise();
    }

    public static DB1 getDb1() {
        return db1;
    }

    private DB1() {
    }

    private void initialise() {
        list = new List();
        roster = new Roster(list);
        showListWindow();
    }

    private File chooseRosterFile(String title, Component parent) {
        int returnVal = fc.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        } else {
            return null;
        }
    }

    private File chooseListFile(String title, Component parent) {
        int returnVal = fc.showOpenDialog(listWindow);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        } else {
            return null;
        }
    }

    /**
	 * private void setupList(List list) { StaticLists.setupList(list); }
	 * 
	 */
    public List getList() {
        return list;
    }

    /**
	 * 
	 * @return
	 */
    public ListWindow getListWindow() {
        return listWindow;
    }

    public void actionPerformed(ActionEvent e) {
        if ("List New".equals(e.getActionCommand())) {
            list = new List();
            roster = new Roster(list);
            listWindow.setList(list);
            listWindow.setRoster(roster);
        } else if ("List Open".equals(e.getActionCommand())) {
            File file = chooseListFile("", null);
            if (file != null) {
                if (list != null) {
                    list.open(file);
                } else {
                    list = new List(file);
                }
            } else {
                list = new List();
            }
            listWindow.setList(list);
        } else if ("Roster New".equals(e.getActionCommand())) {
            roster = new Roster(list);
            listWindow.setRoster(roster);
        } else if ("Roster Open".equals(e.getActionCommand())) {
            File file = chooseRosterFile("", null);
            if (file != null) {
                roster.open(file);
                listWindow.setRoster(roster);
            } else {
                System.out.println("No roster file specified");
            }
        } else if ("Roster Save".equals(e.getActionCommand())) {
            int returnVal = fc.showSaveDialog(listWindow);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                boolean success = roster.save(file);
                if (success) {
                    JOptionPane.showMessageDialog(listWindow, "Roster Saved");
                } else {
                    JOptionPane.showMessageDialog(listWindow, "The roster could not be saved for some reason.", "An error occured", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if ("Roster Save As".equals(e.getActionCommand())) {
        } else if ("Roster Print".equals(e.getActionCommand())) {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPrintable(new RosterPrinter());
            boolean doPrint = job.printDialog();
            if (doPrint) {
                try {
                    job.print();
                } catch (PrinterException pe) {
                    System.out.println("The job did not print");
                    pe.printStackTrace();
                }
            }
        } else if ("List Print".equals(e.getActionCommand())) {
        } else if ("Help About".equals(e.getActionCommand())) {
            String aboutString = "A utility to help design DBMM Army Rosters.\n";
            aboutString += "c Toby Partridge 2006.\n";
            JOptionPane.showMessageDialog(listWindow, aboutString, "About DBMM Builder v0.1", JOptionPane.INFORMATION_MESSAGE);
        } else if ("Help Guide".equals(e.getActionCommand())) {
        }
    }

    public void startNewRoster(List list) {
        System.out.println("DB1::startNewRoster");
        Roster r = new Roster(list);
        r.setListFile(list.getListFile());
        listWindow.setRoster(r);
    }

    private void showListWindow() {
        if (list == null) {
            list = new List();
        }
        if (roster == null) {
            roster = new Roster(list);
        }
        if (listWindow == null) {
            listWindow = new ListWindow("", list, roster);
        }
        listWindow.showListWindow();
        list.fireTableDataChanged();
    }

    public Roster getRoster() {
        return roster;
    }
}
