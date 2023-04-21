package com.quikj.ace.installer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * 
 * @author amit
 */
public class TextPostInstaller implements ScreenPrinterInterface {

    private ConfigParams installParams = new ConfigParams();

    private DataSetter dataValidator = new DataSetter(installParams);

    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /** Creates a new instance of TextInstaller */
    public TextPostInstaller() {
    }

    private void collectContactCenterEmailInfo() {
        while (true) {
            String answer = collectUserInput("Enter the e-mail address for the default Contact Center group", "oos-email");
            String error = dataValidator.validateAndSetOOSEmail(answer, true);
            if (error != null) {
                System.out.println(error);
                continue;
            }
            break;
        }
    }

    private void collectDBHostInfo() {
        while (true) {
            String answer = collectUserInput("Enter the host name of the MySQL server", "sql-host", "localhost");
            String error = dataValidator.validateAndSetDBHostName(answer, true);
            if (error != null) {
                System.out.println(error);
                continue;
            }
            break;
        }
    }

    private void collectDBPasswordInfo() {
        while (true) {
            String answer = collectUserInput("Enter the MySQL password for the above user", "sql-password");
            String error = dataValidator.validateAndSetDBPassword(answer, answer, true);
            if (error != null) {
                System.out.println(error);
                continue;
            }
            break;
        }
    }

    private void collectDBUserInfo() {
        while (true) {
            String answer = collectUserInput("Enter the MySQL user name for the owner of the 'ace' database", "sql-user", "ace");
            String error = dataValidator.validateAndSetDBUser(answer, true);
            if (error != null) {
                System.out.println(error);
                continue;
            }
            break;
        }
    }

    private void collectOperatorEmailInfo() {
        while (true) {
            String answer = collectUserInput("Enter the e-mail address for the default operator group", "operator-email");
            String error = dataValidator.validateAndSetOperatorEmail(answer, true);
            if (error != null) {
                System.out.println(error);
                continue;
            }
            break;
        }
    }

    private void collectSMTPServerInfo() {
        while (true) {
            String answer = collectUserInput("Enter the SMTP server name", "smtp-server");
            String error = dataValidator.validateAndSetSMTP(answer, true);
            if (error != null) {
                System.out.println(error);
                continue;
            }
            break;
        }
    }

    private String collectUserInput(String prompt, String param) {
        return collectUserInput(prompt, param, null);
    }

    private String collectUserInput(String prompt, String param, String def) {
        ConfigElement e = installParams.find(param);
        System.out.print(prompt + " [");
        if (e != null) {
            System.out.print(e.getParamValue());
        } else if (def != null) {
            System.out.print(def);
        }
        System.out.print("] : ");
        System.out.flush();
        try {
            String line = reader.readLine().trim();
            if (line.length() == 0) {
                if (e != null) {
                    return e.getParamValue();
                } else if (def != null) {
                    return def;
                } else {
                    return "";
                }
            } else return line;
        } catch (IOException ex) {
            System.out.println("A fatal error occured while reading input. Terminating...");
            System.exit(1);
            return null;
        }
    }

    private boolean displayUserInput() {
        System.out.println("\n\nYou entered the following data\n");
        System.out.print(installParams.displayEnteredValues());
        while (true) {
            String answer = collectUserInput("Do you want to change any of the above information you entered? (yes/no)", "", "no");
            if ((answer.equals("yes") == true) || (answer.equalsIgnoreCase("y") == true)) {
                return false;
            } else if ((answer.equals("no") == true) || (answer.equalsIgnoreCase("N") == true)) {
                return true;
            } else {
                System.out.println("Invalid entry. You must enter 'yes' or 'no'");
                continue;
            }
        }
    }

    private void initDB() {
        System.out.println("\nNext, we will initialize the Ace database");
        String ace_folder = installParams.find("ace-home").getParamValue();
        try {
            boolean skip_db_init = false;
            DBAccess db = null;
            String answer = collectUserInput("Please make sure that you have started the database and then press the ENTER key", "");
            while (true) {
                String password = collectUserInput("Enter the root password for the database, enter 'skip' to skip this step", "");
                if (password.equals("skip") == true) {
                    skip_db_init = true;
                    System.out.println("The database has not been initialized. To initialize the database manually, using a standard MYSQL client, " + "run the SQL commands specified in the file " + installParams.find("ace-home").getParamValue() + File.separator + "sql" + File.separator + "init_ace.sql");
                    break;
                }
                try {
                    db = new DBAccess("com.mysql.jdbc.Driver", installParams.find("sql-host").getParamValue(), "root", password, "test");
                    break;
                } catch (SQLException ex) {
                    System.out.println("Login failed. Please try again. If you are unable to login and you think you have the correct " + "root password, it may be because " + "the database is not setup to accept a JDBC connection. You can bypass this step by skipping the " + "database initialization and initialize it manually as per the instructions in the SYSADMIN manual.");
                }
            }
            if (skip_db_init == false) {
                if (db.databaseAlreadyExists() == true) {
                    while (true) {
                        answer = collectUserInput("Ace database already exists, do you want to re-initialize the database? (yes/no)", "", "no");
                        if ((answer.equals("yes") == true) || (answer.equalsIgnoreCase("Y") == true)) {
                            System.out.println("Clearing out the old database...");
                            String path = ace_folder + File.separator + "sql" + File.separator + "clear_ace.sql";
                            String error = db.executeSQLStatements(path);
                            if (error != null) {
                                System.out.println("Failed to clear out the old database: " + error);
                                System.exit(1);
                            }
                            System.out.println("Old database cleared");
                        } else if ((answer.equals("no") == true) || (answer.equalsIgnoreCase("N") == true)) {
                            System.out.println("The database will not be initialized");
                            return;
                        } else {
                            System.out.println("Please enter 'yes' or 'no'");
                            continue;
                        }
                        break;
                    }
                }
                System.out.println("Initializing the database ...");
                String path = ace_folder + File.separator + "sql" + File.separator + "init_ace.sql";
                String error = db.executeSQLStatements(path);
                if (error != null) {
                    System.out.println("Failed to initialize the database: " + error);
                    System.exit(1);
                }
                System.out.println("Database initialized");
            }
        } catch (Exception ex1) {
            System.out.println("Fatal error connecting to the database. Installer failed to update database");
            System.exit(1);
        }
    }

    public void install() {
        System.out.println("Welcome to the Ace Operator Installation Tool.\n" + "This tool will help you setup Ace Operator as per your requirements.\n" + "Please enter the information below. If you want to change some of the\n" + "information you provided, you will be able to do so after the installer\n" + "collects all the data.\n\n");
        String error = dataValidator.validateAndSetAceHome(System.getProperty("user.home") + File.separator + ".ace", true);
        if (error != null) {
            System.out.println(error);
            System.exit(1);
        }
        boolean noContinue = false;
        while (noContinue == false) {
            processUserInput();
            noContinue = displayUserInput();
        }
        if (performReplace() == false) {
            System.exit(1);
        }
        initDB();
        if (installParams.save(installParams.find("ace-home").getParamValue(), "install.log") == false) {
            System.out.println("IO Error writing the installation log");
        }
        System.out.println("All done");
        System.exit(0);
    }

    private boolean performReplace() {
        System.out.println("\nWe will now configure the system with the values you entered.");
        boolean result = installParams.replace(installParams.find("ace-home").getParamValue(), ".orig", this);
        if (result == false) {
            System.out.println(installParams.getErrorMessage());
            return false;
        }
        System.out.println("\nConfiguration files changed.");
        return true;
    }

    public void print(String message) {
        System.out.print(message);
    }

    public void println(String message) {
        System.out.println(message);
    }

    private void processUserInput() {
        System.out.println("Please read the license agreements for Ace Operator and other bundled products carefully.\n" + "The license agreements are located in " + "http://www.quik-j.com/license.htm");
        do {
            String accept = collectUserInput("Type \"accept\" to accept the terms of the license, type exit to exit installation? (accept/exit)", "", "");
            if (accept.equals("accept") == true) {
                break;
            } else if (accept.equals("exit") == true) {
                System.exit(0);
            }
        } while (true);
        System.out.println("\nWe will setup your database parameters.\n" + "Please enter the following information.\n");
        collectDBHostInfo();
        collectDBUserInfo();
        collectDBPasswordInfo();
        System.out.println("\nNext, we will setup your e-mail parameters.\n" + "Please enter the following information.\n");
        collectSMTPServerInfo();
        collectOperatorEmailInfo();
        collectContactCenterEmailInfo();
    }
}
