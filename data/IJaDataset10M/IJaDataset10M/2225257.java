package ch.skyguide.tools.requirement.data;

import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.prefs.PreferenceChangeListener;
import ch.skyguide.tools.requirement.hmi.RequirementTool;

public class Preferences implements IPreferences {

    public static final Preferences instance = new Preferences();

    private boolean cleanRecentFiles;

    public static void addUserPreferenceChangeListener(PreferenceChangeListener listener) {
        instance.getUserPreferences().addPreferenceChangeListener(listener);
    }

    public static void removeUserPreferenceChangeListener(PreferenceChangeListener listener) {
        instance.getUserPreferences().removePreferenceChangeListener(listener);
    }

    public static void exportPreferences(final File file) {
        final Thread runner = new Thread(new Runnable() {

            public void run() {
                try {
                    final FileOutputStream fis = new FileOutputStream(file);
                    try {
                        instance.getUserPreferences().exportNode(fis);
                    } finally {
                        fis.close();
                    }
                } catch (Exception e) {
                    Toolkit.getDefaultToolkit().beep();
                    Toolkit.getDefaultToolkit().beep();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        runner.start();
    }

    public static void importPreferences(final File file) {
        final Thread runner = new Thread(new Runnable() {

            public void run() {
                try {
                    final FileInputStream fis = new FileInputStream(file);
                    try {
                        java.util.prefs.Preferences.importPreferences(fis);
                    } finally {
                        fis.close();
                    }
                } catch (Exception e) {
                    Toolkit.getDefaultToolkit().beep();
                    Toolkit.getDefaultToolkit().beep();
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        });
        runner.start();
    }

    public static final String DISPLAY_CODE = "displayCode";

    public static final String DISPLAY_HEADING = "displayHeading";

    public static final String OPEN_OFFICE_FOLDER = "openOfficeFolder";

    private static final String OPEN_OFFICE_DOCUMENT_TEMPLATE = "openOfficeDocumentTemplate";

    private static final String OPEN_OFFICE_REQUIREMENT_TEMPLATE = "openOfficeRequirementTemplate";

    public static final String DATA_DEFINITION_JAR = "dataDefinitionJAR";

    public static final String LAST_NAME = "LastName";

    public static final String FIRST_NAME = "FirstName";

    public static final String DEPARTMENT = "Department";

    public static final String JIRA_URL = "JiraURL";

    public static final String JIRA_PASSWORD = "JiraPassword";

    public static final String JIRA_USERNAME = "JiraUsername";

    public static final String CODE_DELIMITER = "CodeDelimiter";

    public static final String NUMBERED_INDICES = "NumberedIndices";

    public static final String COLLAPSE_HISTORY_BY_VERSION = "CollapseHistoryByVersion";

    public static final String AUTOSAVE_DELAY = "AutosaveDelay";

    private java.util.prefs.Preferences userPreferences;

    private Preferences() {
        super();
    }

    public java.util.prefs.Preferences getUserPreferences() {
        if (userPreferences == null) {
            userPreferences = java.util.prefs.Preferences.userNodeForPackage(RequirementTool.class);
        }
        return userPreferences;
    }

    public File getOpenOfficeFolder() {
        return new File(getUserPreferences().get(OPEN_OFFICE_FOLDER, ""));
    }

    public void setOpenOfficeFolder(File folder) {
        if (folder == null) {
            getUserPreferences().put(OPEN_OFFICE_FOLDER, "");
        } else {
            getUserPreferences().put(OPEN_OFFICE_FOLDER, folder.toString());
        }
    }

    public boolean getDisplayCode() {
        return getUserPreferences().getBoolean(DISPLAY_CODE, true);
    }

    public void setDisplayCode(boolean _state) {
        getUserPreferences().putBoolean(DISPLAY_CODE, _state);
    }

    public boolean getDisplayHeading() {
        return getUserPreferences().getBoolean(DISPLAY_HEADING, true);
    }

    public void setDisplayHeading(boolean _state) {
        getUserPreferences().putBoolean(DISPLAY_HEADING, _state);
    }

    public URL[] getDataDefinitionClassPath() {
        final String pathNames = getUserPreferences().get(DATA_DEFINITION_JAR, "");
        if (pathNames.length() > 0) {
            final List<String> pathNameList = new ArrayList<String>();
            final StringTokenizer tokenizer = new StringTokenizer(pathNames, ";");
            while (tokenizer.hasMoreTokens()) {
                final String token = tokenizer.nextToken().trim();
                if (token.length() > 0) {
                    pathNameList.add(token);
                }
            }
            if (!pathNameList.isEmpty()) {
                final URL[] files = new URL[pathNameList.size()];
                int n = 0;
                for (String pathName : pathNameList) {
                    try {
                        files[n++] = new URL(pathName);
                    } catch (MalformedURLException e) {
                    }
                }
                return files;
            }
        }
        return null;
    }

    public void setDataDefinitionClassPath(URL[] files) {
        if (files == null || files.length == 0) {
            getUserPreferences().put(DATA_DEFINITION_JAR, "");
        } else {
            final StringBuilder buffer = new StringBuilder();
            boolean first = true;
            for (URL file : files) {
                if (first) {
                    first = false;
                } else {
                    buffer.append(';');
                }
                buffer.append(file);
            }
            getUserPreferences().put(DATA_DEFINITION_JAR, buffer.toString());
        }
    }

    public File getDocumentTemplate() {
        final String pathName = getUserPreferences().get(OPEN_OFFICE_DOCUMENT_TEMPLATE, "");
        if (pathName.length() == 0) {
            return null;
        }
        return new File(pathName);
    }

    public void setDocumentTemplate(File file) {
        if (file == null) {
            getUserPreferences().put(OPEN_OFFICE_DOCUMENT_TEMPLATE, "");
        } else {
            getUserPreferences().put(OPEN_OFFICE_DOCUMENT_TEMPLATE, file.toString());
        }
    }

    public File getRequirementTemplate() {
        final String pathName = getUserPreferences().get(OPEN_OFFICE_REQUIREMENT_TEMPLATE, "");
        if (pathName.length() == 0) {
            return null;
        }
        return new File(pathName);
    }

    public void setRequirementTemplate(File file) {
        if (file == null) {
            getUserPreferences().put(OPEN_OFFICE_REQUIREMENT_TEMPLATE, "");
        } else {
            getUserPreferences().put(OPEN_OFFICE_REQUIREMENT_TEMPLATE, file.toString());
        }
    }

    public String getLastName() {
        return getUserPreferences().get(LAST_NAME, null);
    }

    public void setLastName(String name) {
        if (name == null) {
            getUserPreferences().remove(LAST_NAME);
        } else {
            getUserPreferences().put(LAST_NAME, name);
        }
    }

    public String getFirstName() {
        return getUserPreferences().get(FIRST_NAME, null);
    }

    public void setFirstName(String name) {
        if (name == null) {
            getUserPreferences().remove(FIRST_NAME);
        } else {
            getUserPreferences().put(FIRST_NAME, name);
        }
    }

    public String getDepartment() {
        return getUserPreferences().get(DEPARTMENT, null);
    }

    public void setDepartment(String name) {
        if (name == null) {
            getUserPreferences().remove(DEPARTMENT);
        } else {
            getUserPreferences().put(DEPARTMENT, name);
        }
    }

    public String getCodeDelimiter() {
        return getUserPreferences().get(CODE_DELIMITER, "-");
    }

    public void setCodeDelimiter(String name) {
        if (name.length() != 1) {
            throw new IllegalArgumentException("Code delimiter must be a single character string");
        }
        if (name == null) {
            getUserPreferences().remove(CODE_DELIMITER);
        } else {
            getUserPreferences().put(CODE_DELIMITER, name);
        }
    }

    public boolean getCleanRecentFiles() {
        return cleanRecentFiles;
    }

    public void setCleanRecentFiles(boolean _cleanRecentFiles) {
        cleanRecentFiles = _cleanRecentFiles;
    }

    public String getJiraUsername() {
        String user = getUserPreferences().get(JIRA_USERNAME, null);
        if (user == null || user.trim().length() == 0) {
            user = System.getProperty("user.name");
        }
        return user;
    }

    public void setJiraUsername(String name) {
        if (name == null || name.trim().length() == 0) {
            getUserPreferences().remove(JIRA_USERNAME);
        } else {
            getUserPreferences().put(JIRA_USERNAME, name.trim());
        }
    }

    public String getJiraPassword() {
        String pwd = getUserPreferences().get(JIRA_PASSWORD, null);
        if (pwd == null || pwd.trim().length() == 0) {
            pwd = System.getProperty("user.name");
        }
        return pwd;
    }

    public void setJiraPassword(String name) {
        if (name == null || name.trim().length() == 0) {
            getUserPreferences().remove(JIRA_PASSWORD);
        } else {
            getUserPreferences().put(JIRA_PASSWORD, name.trim());
        }
    }

    public String getJiraURL() {
        String url = getUserPreferences().get(JIRA_URL, null);
        if (url == null || url.trim().length() == 0) {
            url = "156.135.81.118:28080";
        }
        return url;
    }

    public void setJiraURL(String name) {
        if (name == null || name.trim().length() == 0) {
            getUserPreferences().remove(JIRA_URL);
        } else {
            getUserPreferences().put(JIRA_URL, name.trim());
        }
    }

    public boolean getNumberedIndices() {
        return getUserPreferences().getBoolean(NUMBERED_INDICES, false);
    }

    public void setNumberedIndices(boolean flag) {
        getUserPreferences().putBoolean(NUMBERED_INDICES, flag);
    }

    public boolean getCollapseHistoryByVersion() {
        return getUserPreferences().getBoolean(COLLAPSE_HISTORY_BY_VERSION, false);
    }

    public void setCollapseHistoryByVersion(boolean flag) {
        getUserPreferences().putBoolean(COLLAPSE_HISTORY_BY_VERSION, flag);
    }

    public int getAutosaveDelay() {
        return getUserPreferences().getInt(AUTOSAVE_DELAY, 5);
    }

    public void setAutosaveDelay(final int delay) {
        getUserPreferences().putInt(AUTOSAVE_DELAY, delay);
    }
}
