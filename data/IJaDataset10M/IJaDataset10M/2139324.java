package com.quikj.application.utilities.postinstall;

import java.io.File;
import java.util.StringTokenizer;

/**
 * 
 * @author amit
 */
public class DataValidator {

    /** Holds value of property params. */
    private ConfigParams params;

    /** Creates a new instance of DataValidator */
    public DataValidator(ConfigParams params) {
        this.params = params;
    }

    public static boolean fileExists(String dir, String file) {
        File f = new File(dir, file);
        return f.exists();
    }

    public static String findAceHome(String os) {
        String def = null;
        File f = new File(".");
        String abs_path = f.getAbsolutePath();
        if (abs_path.endsWith(File.separator + ".") == true) {
            abs_path = abs_path.substring(0, abs_path.length() - 2);
        }
        if (fileExists(abs_path, "sql/init_ace.sql.orig") == true) {
            def = abs_path;
        } else if (os.startsWith("Windows") == true) {
            if (fileExists("C:\\", "ace") == true) {
                if (fileExists("C:\\ace", "sql/init_ace.sql.orig") == true) {
                    def = "C:\\ace";
                }
            }
        } else {
            if (fileExists("/opt", "ace") == true) {
                if (fileExists("/opt/ace", "sql/init_ace.sql.orig") == true) {
                    def = "/opt/ace";
                }
            } else if (fileExists("/usr", "ace") == true) {
                if (fileExists("/usr/ace", "sql/init_ace.sql.orig") == true) {
                    def = "/usr/ace";
                }
            }
            if (fileExists("/usr/local", "ace") == true) {
                if (fileExists("/usr/local/ace", "sql/init_ace.sql.orig") == true) {
                    def = "/usr/local/ace";
                }
            }
        }
        return def;
    }

    /**
	 * Getter for property param.
	 * 
	 * @return Value of property param.
	 * 
	 */
    public ConfigParams getParams() {
        return this.params;
    }

    /**
	 * Setter for property param.
	 * 
	 * @param param
	 *            New value of property param.
	 * 
	 */
    public void setParams(ConfigParams params) {
        this.params = params;
    }

    public String validateAceHome(String name, boolean write) {
        if (name.length() == 0) {
            return "** You must enter a valid folder location";
        }
        File f = new File(name);
        if (f.exists() == false) {
            return "** The specified folder does not exist";
        }
        if (f.isDirectory() == false) {
            return "** The specified name is not a folder";
        }
        if (f.canWrite() == false) {
            return "** You do not have permissions to modify files and folders";
        }
        File nf = new File(f.getAbsolutePath(), "sql/init_ace.sql.orig");
        if (nf.exists() == false) {
            return "** This does not look like a folder where Ace Operator is installed";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("ace-home");
            e.setParamValue(f.getAbsolutePath());
            e.setReplacePattern("$$ACE(ACE_HOME)");
            params.put(e);
        }
        return null;
    }

    public String validateDBHostName(String host, boolean write) {
        if (host.length() == 0) {
            return "** The host name cannot be blank";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("sql-host");
            e.setParamValue(host);
            e.setReplacePattern("$$ACE(ACE_SQL_HOST)");
            params.put(e);
        }
        return null;
    }

    public String validateDBPassword(String password, String verify_password, boolean write) {
        if (password.equals(verify_password) == false) {
            return "** The passwords do not match";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("sql-password");
            e.setParamValue(password);
            e.setReplacePattern("$$ACE(ACE_SQL_PASSWORD)");
            e.setDisplay(ConfigElement.DISPLAY_PARAM);
            params.put(e);
        }
        return null;
    }

    public String validateDBUser(String user, boolean write) {
        if (user.length() == 0) {
            return "** The user name field cannot be left blank";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("sql-user");
            e.setParamValue(user);
            e.setReplacePattern("$$ACE(ACE_SQL_USER)");
            params.put(e);
        }
        return null;
    }

    private boolean validateEmail(String email) {
        char[] cmail = email.toCharArray();
        for (int i = 0; i < cmail.length; i++) {
            if (Character.isWhitespace(cmail[i]) == true) {
                return false;
            }
        }
        StringTokenizer tokens = new StringTokenizer(email, "@");
        int count = tokens.countTokens();
        if (count != 2) {
            return false;
        }
        return true;
    }

    public String validateEmbeddedWebServer(boolean ws, boolean write) {
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("ace-use-bundled-https");
            e.setReplacePattern("$$ACE(ACE_ENABLE_WEB)");
            if (ws == true) {
                e.setParamValue("yes");
            } else {
                e.setParamValue("no");
            }
            params.put(e);
        }
        return null;
    }

    public String validateHostName(String host, boolean write) {
        if (host.length() == 0) {
            return "** You must enter a valid host name";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("ace-host");
            e.setParamValue(host);
            e.setReplacePattern("$$ACE(ACE_HOST_NAME)");
            params.put(e);
        }
        return null;
    }

    public String validateJavaHome(String jname, boolean write) {
        if (jname.length() == 0) {
            return "** You must enter a valid Java home folder location";
        }
        File f = new File(jname);
        if (f.exists() == false) {
            return "** The specified java home folder does not exist";
        }
        if (f.isDirectory() == false) {
            return "** The specified java home folder name is not a folder";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("java-home");
            e.setParamValue(f.getAbsolutePath());
            e.setReplacePattern("$$ACE(JAVA_HOME)");
            params.put(e);
        }
        return null;
    }

    public String validateLogEmail(String log_email, boolean write) {
        if (log_email.length() > 0) {
            if (validateEmail(log_email) == false) {
                return "** The email address does not have a valid format";
            }
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("log-email");
            e.setParamValue(log_email);
            e.setReplacePattern("$$ACE(ACE_LOG_EMAIL)");
            params.put(e);
            if (log_email.length() == 0) {
                e = new ConfigElement();
                e.setParamName("log-email-comment-start");
                e.setParamValue("<!--");
                e.setReplacePattern("$$ACE(ACE_LOG_EMAIL_COMMENT_START)");
                e.setDisplay(ConfigElement.DISPLAY_NONE);
                params.put(e);
                e = new ConfigElement();
                e.setParamName("log-email-comment-end");
                e.setParamValue("-->");
                e.setReplacePattern("$$ACE(ACE_LOG_EMAIL_COMMENT_END)");
                e.setDisplay(ConfigElement.DISPLAY_NONE);
                params.put(e);
            } else {
                e = new ConfigElement();
                e.setParamName("log-email-comment-start");
                e.setParamValue("");
                e.setReplacePattern("$$ACE(ACE_LOG_EMAIL_COMMENT_START)");
                e.setDisplay(ConfigElement.DISPLAY_NONE);
                params.put(e);
                e = new ConfigElement();
                e.setParamName("log-email-comment-end");
                e.setParamValue("");
                e.setReplacePattern("$$ACE(ACE_LOG_EMAIL_COMMENT_END)");
                e.setDisplay(ConfigElement.DISPLAY_NONE);
                params.put(e);
            }
        }
        return null;
    }

    public String validateOOSEmail(String oos_email, boolean write) {
        if (oos_email.length() > 0) {
            if (validateEmail(oos_email) == false) {
                return "** The email address does not have a valid format";
            }
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("oos-email");
            e.setParamValue(oos_email);
            e.setReplacePattern("$$ACE(ACE_OOS_EMAIL)");
            params.put(e);
        }
        return null;
    }

    public String validateOperatorEmail(String op_email, boolean write) {
        if (op_email.length() > 0) {
            if (validateEmail(op_email) == false) {
                return "** The email address does not have a valid format";
            }
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("operator-email");
            e.setParamValue(op_email);
            e.setReplacePattern("$$ACE(ACE_OPERATOR_EMAIL)");
            params.put(e);
        }
        return null;
    }

    public String validateSMTP(String server, boolean write) {
        if (server.length() == 0) {
            return "** The SMTP server host name has not been entered";
        }
        if (write == true) {
            ConfigElement e = new ConfigElement();
            e.setParamName("smtp-server");
            e.setParamValue(server);
            e.setReplacePattern("$$ACE(ACE_SMTP_SERVER)");
            params.put(e);
        }
        return null;
    }
}
