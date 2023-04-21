package com.inetmon.jn.statistic.application.views;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import com.inetmon.jn.statistic.application.jdbc.LoadDriverJDBC;

/**
 * Create add entry wizard page
 * @author   inetmon
 */
public class AddEntryWizard extends Wizard {

    private AddEntyPage namePage;

    private Shell shell;

    LoadDriverJDBC jdbc;

    /**
	 * AddEntryWizard constructor
	 */
    public AddEntryWizard(LoadDriverJDBC jdbc) {
        this.jdbc = jdbc;
        namePage = new AddEntyPage(jdbc);
        addPage(namePage);
        setWindowTitle("Add new application");
    }

    /**
	 * Called when user clicks Finish Creates the entry in the address book
	 * @return 
	 * 
	 * @throws Exception
	 */
    public boolean performFinish() {
        connectToDB();
        String ID;
        ResultSet rs;
        if (((namePage.getportNumber()).matches("")) || ((namePage.getportNumber()).matches("0"))) namePage.setportNumber("0");
        if (((namePage.getSignature()).matches("")) || ((namePage.getSignature()).matches("NONE"))) namePage.setSignature("NONE");
        if ((namePage.getSignature()).contains(";")) {
            String[] tempSig;
            tempSig = namePage.getSignature().split(";");
            for (int h = 0; h < tempSig.length; h++) {
                try {
                    String temp_sigX = tempSig[h];
                    if (temp_sigX.contains("&")) {
                        String[] temp_sig1 = temp_sigX.split("&");
                        for (int k = 0; k < temp_sig1.length; k++) {
                            if (temp_sig1[k].contains(":")) {
                                String[] temp_sig2 = temp_sig1[k].split(":");
                                if (temp_sig2[0].regionMatches(0, "r", 0, 1)) {
                                } else {
                                    Integer.parseInt(temp_sig2[0]);
                                    try {
                                        boolean okay = true;
                                        for (int i = 0; i < temp_sig2[1].length() && okay; i++) {
                                            if ((temp_sig2[1].substring(i, i + 1).matches("a")) || (temp_sig2[1].substring(i, i + 1).matches("b")) || (temp_sig2[1].substring(i, i + 1).matches("c")) || (temp_sig2[1].substring(i, i + 1).matches("d")) || (temp_sig2[1].substring(i, i + 1).matches("e")) || (temp_sig2[1].substring(i, i + 1).matches("f")) || (temp_sig2[1].substring(i, i + 1).matches("0")) || (temp_sig2[1].substring(i, i + 1).matches("1")) || (temp_sig2[1].substring(i, i + 1).matches("2")) || (temp_sig2[1].substring(i, i + 1).matches("3")) || (temp_sig2[1].substring(i, i + 1).matches("4")) || (temp_sig2[1].substring(i, i + 1).matches("5")) || (temp_sig2[1].substring(i, i + 1).matches("6")) || (temp_sig2[1].substring(i, i + 1).matches("7")) || (temp_sig2[1].substring(i, i + 1).matches("8")) || (temp_sig2[1].substring(i, i + 1).matches("9"))) {
                                            } else {
                                                okay = false;
                                            }
                                        }
                                        if (okay == false) Integer.parseInt("ERROR");
                                    } catch (Exception ex) {
                                        MessageDialog.openWarning(null, "Invalid input(s)", "Not Valid Hex Values (0-9 & a-f)");
                                        return false;
                                    }
                                }
                            } else {
                                try {
                                    boolean okay = true;
                                    for (int i = 0; i < temp_sig1[k].length() && okay; i++) {
                                        if ((temp_sig1[k].substring(i, i + 1).matches("a")) || (temp_sig1[k].substring(i, i + 1).matches("b")) || (temp_sig1[k].substring(i, i + 1).matches("c")) || (temp_sig1[k].substring(i, i + 1).matches("d")) || (temp_sig1[k].substring(i, i + 1).matches("e")) || (temp_sig1[k].substring(i, i + 1).matches("f")) || (temp_sig1[k].substring(i, i + 1).matches("0")) || (temp_sig1[k].substring(i, i + 1).matches("1")) || (temp_sig1[k].substring(i, i + 1).matches("2")) || (temp_sig1[k].substring(i, i + 1).matches("3")) || (temp_sig1[k].substring(i, i + 1).matches("4")) || (temp_sig1[k].substring(i, i + 1).matches("5")) || (temp_sig1[k].substring(i, i + 1).matches("6")) || (temp_sig1[k].substring(i, i + 1).matches("7")) || (temp_sig1[k].substring(i, i + 1).matches("8")) || (temp_sig1[k].substring(i, i + 1).matches("9"))) {
                                        } else {
                                            okay = false;
                                        }
                                    }
                                    if (okay == false) Integer.parseInt("ERROR");
                                } catch (Exception ex) {
                                    MessageDialog.openWarning(null, "Invalid input(s)", "Not Valid Hex Values (0-9 & a-f)");
                                    return false;
                                }
                            }
                        }
                    } else {
                        try {
                            if (temp_sigX.contains(":")) {
                                String[] temp_sig2 = temp_sigX.split(":");
                                if (temp_sig2[0].regionMatches(0, "r", 0, 1)) {
                                } else {
                                    Integer.parseInt(temp_sig2[0]);
                                    boolean okay = true;
                                    for (int i = 0; i < temp_sig2[1].length() && okay; i++) {
                                        if ((temp_sig2[1].substring(i, i + 1).matches("a")) || (temp_sig2[1].substring(i, i + 1).matches("b")) || (temp_sig2[1].substring(i, i + 1).matches("c")) || (temp_sig2[1].substring(i, i + 1).matches("d")) || (temp_sig2[1].substring(i, i + 1).matches("e")) || (temp_sig2[1].substring(i, i + 1).matches("f")) || (temp_sig2[1].substring(i, i + 1).matches("0")) || (temp_sig2[1].substring(i, i + 1).matches("1")) || (temp_sig2[1].substring(i, i + 1).matches("2")) || (temp_sig2[1].substring(i, i + 1).matches("3")) || (temp_sig2[1].substring(i, i + 1).matches("4")) || (temp_sig2[1].substring(i, i + 1).matches("5")) || (temp_sig2[1].substring(i, i + 1).matches("6")) || (temp_sig2[1].substring(i, i + 1).matches("7")) || (temp_sig2[1].substring(i, i + 1).matches("8")) || (temp_sig2[1].substring(i, i + 1).matches("9"))) {
                                        } else {
                                            okay = false;
                                        }
                                    }
                                    if (okay == false) Integer.parseInt("ERROR");
                                }
                            } else {
                                boolean okay = true;
                                for (int i = 0; i < temp_sigX.length() && okay; i++) {
                                    if ((temp_sigX.substring(i, i + 1).matches("a")) || (temp_sigX.substring(i, i + 1).matches("b")) || (temp_sigX.substring(i, i + 1).matches("c")) || (temp_sigX.substring(i, i + 1).matches("d")) || (temp_sigX.substring(i, i + 1).matches("e")) || (temp_sigX.substring(i, i + 1).matches("f")) || (temp_sigX.substring(i, i + 1).matches("0")) || (temp_sigX.substring(i, i + 1).matches("1")) || (temp_sigX.substring(i, i + 1).matches("2")) || (temp_sigX.substring(i, i + 1).matches("3")) || (temp_sigX.substring(i, i + 1).matches("4")) || (temp_sigX.substring(i, i + 1).matches("5")) || (temp_sigX.substring(i, i + 1).matches("6")) || (temp_sigX.substring(i, i + 1).matches("7")) || (temp_sigX.substring(i, i + 1).matches("8")) || (temp_sigX.substring(i, i + 1).matches("9"))) {
                                    } else {
                                        okay = false;
                                    }
                                }
                                if (okay == false) Integer.parseInt("ERROR");
                            }
                        } catch (Exception ex) {
                            MessageDialog.openWarning(null, "Invalid input(s)", "Not Valid Hex Values (0-9 & a-f)");
                            return false;
                        }
                    }
                } catch (Exception e) {
                    MessageDialog.openWarning(null, "Invalid input(s)", "Please insert a valid signature structure");
                    return false;
                }
                ID = "0";
                rs = jdbc.JDBCexecuteQuerySearch("select * from APPLICATION");
                try {
                    while (rs.next()) if (rs.last()) ID = rs.getString("ID");
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String queryInsert = "INSERT INTO application (ID, PORT, PROTOCOL, NAME, DESCRIPTION, SIGNATURE)" + "VALUES ('" + String.valueOf(Integer.parseInt(ID) + 1) + "','" + namePage.getportNumber() + "','" + namePage.getProtocol() + "','" + namePage.getApplicationName() + "','" + namePage.getDescription1() + "','" + tempSig[h] + "')";
                jdbc.JDBCexecuteQuery(queryInsert);
            }
        } else {
            if ((namePage.getSignature().compareTo("NONE")) != 1) {
                try {
                    String temp_sig = namePage.getSignature();
                    if (temp_sig.contains("&")) {
                        String[] temp_sig1 = temp_sig.split("&");
                        for (int k = 0; k < temp_sig1.length; k++) {
                            if (temp_sig1[k].contains(":")) {
                                String[] temp_sig2 = temp_sig1[k].split(":");
                                if (temp_sig2[0].regionMatches(0, "r", 0, 1)) {
                                } else {
                                    Integer.parseInt(temp_sig2[0]);
                                    try {
                                        boolean okay = true;
                                        for (int i = 0; i < temp_sig2[1].length() && okay; i++) {
                                            if ((temp_sig2[1].substring(i, i + 1).matches("a")) || (temp_sig2[1].substring(i, i + 1).matches("b")) || (temp_sig2[1].substring(i, i + 1).matches("c")) || (temp_sig2[1].substring(i, i + 1).matches("d")) || (temp_sig2[1].substring(i, i + 1).matches("e")) || (temp_sig2[1].substring(i, i + 1).matches("f")) || (temp_sig2[1].substring(i, i + 1).matches("0")) || (temp_sig2[1].substring(i, i + 1).matches("1")) || (temp_sig2[1].substring(i, i + 1).matches("2")) || (temp_sig2[1].substring(i, i + 1).matches("3")) || (temp_sig2[1].substring(i, i + 1).matches("4")) || (temp_sig2[1].substring(i, i + 1).matches("5")) || (temp_sig2[1].substring(i, i + 1).matches("6")) || (temp_sig2[1].substring(i, i + 1).matches("7")) || (temp_sig2[1].substring(i, i + 1).matches("8")) || (temp_sig2[1].substring(i, i + 1).matches("9"))) {
                                            } else {
                                                okay = false;
                                            }
                                        }
                                        if (okay == false) Integer.parseInt("ERROR");
                                    } catch (Exception ex) {
                                        MessageDialog.openWarning(null, "Invalid input(s)", "Not Valid Hex Values (0-9 & a-f)");
                                        return false;
                                    }
                                }
                            } else {
                                try {
                                    boolean okay = true;
                                    for (int i = 0; i < temp_sig1[k].length() && okay; i++) {
                                        if ((temp_sig1[k].substring(i, i + 1).matches("a")) || (temp_sig1[k].substring(i, i + 1).matches("b")) || (temp_sig1[k].substring(i, i + 1).matches("c")) || (temp_sig1[k].substring(i, i + 1).matches("d")) || (temp_sig1[k].substring(i, i + 1).matches("e")) || (temp_sig1[k].substring(i, i + 1).matches("f")) || (temp_sig1[k].substring(i, i + 1).matches("0")) || (temp_sig1[k].substring(i, i + 1).matches("1")) || (temp_sig1[k].substring(i, i + 1).matches("2")) || (temp_sig1[k].substring(i, i + 1).matches("3")) || (temp_sig1[k].substring(i, i + 1).matches("4")) || (temp_sig1[k].substring(i, i + 1).matches("5")) || (temp_sig1[k].substring(i, i + 1).matches("6")) || (temp_sig1[k].substring(i, i + 1).matches("7")) || (temp_sig1[k].substring(i, i + 1).matches("8")) || (temp_sig1[k].substring(i, i + 1).matches("9"))) {
                                        } else {
                                            okay = false;
                                        }
                                    }
                                    if (okay == false) Integer.parseInt("ERROR");
                                } catch (Exception ex) {
                                    MessageDialog.openWarning(null, "Invalid input(s)", "Not Valid Hex Values (0-9 & a-f)");
                                    return false;
                                }
                            }
                        }
                    } else {
                        try {
                            if (temp_sig.contains(":")) {
                                String[] temp_sig2 = temp_sig.split(":");
                                if (temp_sig2[0].regionMatches(0, "r", 0, 1)) {
                                } else {
                                    Integer.parseInt(temp_sig2[0]);
                                    boolean okay = true;
                                    for (int i = 0; i < temp_sig2[1].length() && okay; i++) {
                                        if ((temp_sig2[1].substring(i, i + 1).matches("a")) || (temp_sig2[1].substring(i, i + 1).matches("b")) || (temp_sig2[1].substring(i, i + 1).matches("c")) || (temp_sig2[1].substring(i, i + 1).matches("d")) || (temp_sig2[1].substring(i, i + 1).matches("e")) || (temp_sig2[1].substring(i, i + 1).matches("f")) || (temp_sig2[1].substring(i, i + 1).matches("0")) || (temp_sig2[1].substring(i, i + 1).matches("1")) || (temp_sig2[1].substring(i, i + 1).matches("2")) || (temp_sig2[1].substring(i, i + 1).matches("3")) || (temp_sig2[1].substring(i, i + 1).matches("4")) || (temp_sig2[1].substring(i, i + 1).matches("5")) || (temp_sig2[1].substring(i, i + 1).matches("6")) || (temp_sig2[1].substring(i, i + 1).matches("7")) || (temp_sig2[1].substring(i, i + 1).matches("8")) || (temp_sig2[1].substring(i, i + 1).matches("9"))) {
                                        } else {
                                            okay = false;
                                        }
                                    }
                                    if (okay == false) Integer.parseInt("ERROR");
                                }
                            } else {
                                boolean okay = true;
                                for (int i = 0; i < temp_sig.length() && okay; i++) {
                                    if ((temp_sig.substring(i, i + 1).matches("a")) || (temp_sig.substring(i, i + 1).matches("b")) || (temp_sig.substring(i, i + 1).matches("c")) || (temp_sig.substring(i, i + 1).matches("d")) || (temp_sig.substring(i, i + 1).matches("e")) || (temp_sig.substring(i, i + 1).matches("f")) || (temp_sig.substring(i, i + 1).matches("0")) || (temp_sig.substring(i, i + 1).matches("1")) || (temp_sig.substring(i, i + 1).matches("2")) || (temp_sig.substring(i, i + 1).matches("3")) || (temp_sig.substring(i, i + 1).matches("4")) || (temp_sig.substring(i, i + 1).matches("5")) || (temp_sig.substring(i, i + 1).matches("6")) || (temp_sig.substring(i, i + 1).matches("7")) || (temp_sig.substring(i, i + 1).matches("8")) || (temp_sig.substring(i, i + 1).matches("9"))) {
                                    } else {
                                        okay = false;
                                    }
                                }
                                if (okay == false) Integer.parseInt("ERROR");
                            }
                        } catch (Exception ex) {
                            MessageDialog.openWarning(null, "Invalid input(s)", "Not Valid Hex Values (0-9 & a-f)");
                            return false;
                        }
                    }
                } catch (Exception e) {
                    MessageDialog.openWarning(null, "Invalid input(s)", "Please insert a valid signature structure");
                    return false;
                }
            }
            ID = "0";
            rs = jdbc.JDBCexecuteQuerySearch("select * from APPLICATION");
            try {
                while (rs.next()) if (rs.last()) ID = rs.getString("ID");
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String queryInsert = "INSERT INTO application (ID, PORT, PROTOCOL, NAME, DESCRIPTION, SIGNATURE)" + "VALUES ('" + String.valueOf(Integer.parseInt(ID) + 1) + "','" + namePage.getportNumber() + "','" + namePage.getProtocol() + "','" + namePage.getApplicationName() + "','" + namePage.getDescription1() + "','" + namePage.getSignature() + "')";
            jdbc.JDBCexecuteQuery(queryInsert);
        }
        System.out.println("Insertion Success");
        Provider.loadServices();
        return true;
    }

    private void connectToDB() {
    }
}
