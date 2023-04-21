package eln.install;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JOptionPane;
import javax.swing.JDialog;
import eln.client.ELNClient;
import emsl.JavaShare.EmslProperties;
import eln.VersionInfo;

public class IniSetup {

    private static String iniFileName = ELNClient.globalConfigFileName;

    private static String appFileName = "ELNapp" + VersionInfo.BuildNumber;

    private static String opSys = null;

    public IniSetup() {
    }

    public static void main(String[] args) {
        String systemOpSys = System.getProperty("os.name");
        System.out.println("opSys is:  " + systemOpSys);
        opSys = "UNIX";
        if (systemOpSys.startsWith("Windows")) {
            opSys = "WIN";
        } else if (systemOpSys.equalsIgnoreCase("Mac OS")) {
            opSys = "MAC";
        } else if (systemOpSys.equalsIgnoreCase("Linux")) {
            opSys = "LINUX";
        }
        String userHome = System.getProperty("user.home");
        String userDir = System.getProperty("user.dir");
        String localFileSep = System.getProperty("file.separator");
        String langCode = Locale.getDefault().getLanguage();
        userDir = userDir.replace(localFileSep.charAt(0), '/');
        String applicationPath = userDir + "/" + appFileName;
        String capturetoolPath = userDir + "/" + "ntbkcap.exe";
        if (opSys.equals("WIN")) {
            applicationPath = userDir + "/" + appFileName + ".exe";
            capturetoolPath = userDir + "/" + "Rectcap.exe";
        } else if (opSys.equals("LINUX")) {
            capturetoolPath = userDir + "/" + "LinuxImage";
        }
        String inputValue;
        int result = 2;
        if (userHome != null) {
            Object[] options = { "OK", "Change", "Cancel" };
            JOptionPane checkDefault = new JOptionPane("Use default path '" + userHome + "'", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, options);
            JDialog dialog = checkDefault.createDialog(null, "Input");
            dialog.show();
            Object selectedValue = checkDefault.getValue();
            if (selectedValue != null) {
                for (int counter = 0, maxCounter = options.length; counter < maxCounter; counter++) {
                    if (options[counter].equals(selectedValue)) result = counter;
                }
            }
        }
        if (result == 2) {
            System.exit(0);
        } else if (result == 1) {
            boolean doItAgain = true;
            inputValue = JOptionPane.showInputDialog("Please enter the user home path as shown exactly on the download page\nrunning on this computer\nhttp://collaboratory.emsl.pnl.gov/download/eln/downloadnbclient5.2.html", userHome);
            if (inputValue != null) {
                while (doItAgain) {
                    File testFile = new File(inputValue);
                    if (testFile.isDirectory()) {
                        doItAgain = false;
                    } else {
                        inputValue = JOptionPane.showInputDialog("That directory doesn't exist or is not readable.\nPlease enter a new directory");
                        if (inputValue != null) {
                            doItAgain = true;
                        } else {
                            System.exit(0);
                        }
                    }
                }
                userHome = inputValue;
            } else {
                System.exit(0);
            }
        }
        System.out.println("Userhome:  " + userHome);
        System.out.println("applicationPath is: " + applicationPath);
        System.out.println("capturetoolPath is: " + capturetoolPath);
        inputValue = JOptionPane.showInputDialog("Please enter the two letter language code. Default is " + langCode, langCode);
        if (inputValue != null) {
            if (inputValue.length() > 0) {
                langCode = inputValue;
            }
            System.out.println("Langcode set to " + langCode);
        }
        EmslProperties cfig;
        String iniPath = userHome + "/" + iniFileName;
        File elnIni = new File(iniPath);
        EmslProperties.setApplicationPropertiesFile("eln.ini", userHome);
        if (elnIni.exists()) {
            System.out.println("Found eln.ini");
            try {
                cfig = EmslProperties.getApplicationProperties();
                cfig.put("application_path", applicationPath);
                cfig.put("capturetool", capturetoolPath);
                if (opSys.equals("LINUX")) {
                    cfig.put("capturetype", "jpg");
                } else {
                    cfig.put("capturetype", "gif");
                }
                cfig.put("langCode", langCode);
                cfig.store("ELN: " + VersionInfo.Version + " Configuration file");
            } catch (IOException io) {
                System.out.println("IOException while writing application_path information to eln.ini: " + io);
            }
        } else {
            System.out.println("Creating eln.ini");
            try {
                FileOutputStream configFile = new FileOutputStream(userHome + "/" + iniFileName);
                Properties firstProps = new Properties();
                firstProps.store(configFile, "ELN: " + VersionInfo.Version + " Configuration file");
                configFile.close();
            } catch (IOException io) {
                System.out.println("IOException caught while creating eln.ini:  " + io);
            }
            try {
                cfig = EmslProperties.getApplicationProperties();
                cfig.put("application_path", applicationPath);
                cfig.put("tmpdir", userHome + "/" + "elntmpdir");
                cfig.put("maxInMemoryDataLength", "100000");
                if (opSys.equals("UNIX")) {
                    cfig.put("capturetool", userDir + "/" + "ntbkcap.exe");
                } else if (opSys.equals("WIN")) {
                    cfig.put("capturetool", userDir + "/" + "Rectcap.exe");
                }
                if (opSys.equals("LINUX")) {
                    cfig.put("capturetype", "jpg");
                } else {
                    cfig.put("capturetype", "gif");
                }
                cfig.put("langCode", langCode);
                cfig.store("ELN: " + VersionInfo.Version + " Configuration file");
            } catch (IOException io) {
                System.out.println("IOException caught while adding items to eln.ini:  " + io);
            } catch (Exception ex) {
                System.out.println("Exception caught while saving eln.ini after create:  " + ex);
            }
        }
        System.exit(0);
    }
}
