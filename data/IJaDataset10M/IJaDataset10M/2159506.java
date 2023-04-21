package net.sf.ovanttasks.ovanttasks;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import net.sf.ovanttasks.ovnative.win32.LnkFile;
import net.sf.ovanttasks.ovnative.win32.UrlFile;

/**
 * @version 	1.0
 * @since 		30.10.2003
 * @author		lars.gersmann@orangevolt.com
 *
 * (c) Copyright 2005 Orangevolt (www.orangevolt.com).
 */
public class Win32ShortcutTask extends Task {

    private String workingDirectory = null;

    private String iconFile = null;

    private int iconIndex = -1;

    private URL url = null;

    private String execute = null;

    private int showCommand = -1;

    private int hotKeyModifier = -1;

    private int hotKeyKeyCode = -1;

    private String modified = null;

    private File file = null;

    private String comment = null;

    private String arguments = null;

    public void setHotKeyKeyCode(int i) {
        hotKeyKeyCode = i;
    }

    public void setHotKeyModifier(int i) {
        hotKeyModifier = i;
    }

    public void setFile(File file) {
        if (file.getName().endsWith(".url") || file.getName().endsWith(".lnk")) {
            this.file = file;
        } else {
            throw new BuildException("Attribute file can only have suffix \".lnk\" or \".url\"");
        }
    }

    public void setIconFile(String string) {
        iconFile = string;
    }

    public void setComment(String string) {
        comment = string;
    }

    public void setIconIndex(int i) {
        iconIndex = i;
    }

    public void setModified(String string) {
        modified = string;
    }

    public void setShowCommand(String showCommand) {
        showCommand = showCommand.toLowerCase().trim();
        if (showCommand.equals("normal")) {
            this.showCommand = UrlFile.SHOWCOMMAND_NORMAL;
        } else if (showCommand.equals("minimized")) {
            this.showCommand = UrlFile.SHOWCOMMAND_MINIMIZED;
        } else if (showCommand.equals("maximized")) {
            this.showCommand = UrlFile.SHOWCOMMAND_MAXIMIZED;
        } else {
            throw new BuildException("Invalid Attribute showCommand value " + showCommand + " : Valid values are normal, minimized or maximized.");
        }
    }

    public class Url {

        public void addText(String s) {
            if (url != null) {
                throw new BuildException("Property \"url\" is always defined. You have to use either <url> or the attribute \"url\" of task win32.shortcut");
            }
            try {
                url = new URL(getProject().replaceProperties(s.trim()));
            } catch (MalformedURLException ex) {
                throw new BuildException("url is not valid : " + ex.getMessage(), ex);
            }
        }
    }

    public Url createUrl() {
        if (url != null) {
            throw new BuildException("Property \"url\" is always defined. You have to use either <url> or the attribute \"url\" of task win32.shortcut");
        }
        return new Url();
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public class Execute {

        public void addText(String s) {
            execute = getProject().replaceProperties(s.trim());
        }
    }

    public Execute createExecute() {
        if (execute != null) {
            throw new BuildException("Property \"execute\" is always defined. You have to use either <execute> or the attribute \"execute\" of task win32.shortcut");
        }
        return new Execute();
    }

    public void setExecute(String string) {
        if (execute != null) {
            throw new BuildException("Property \"execute\" is always defined. You have to use either <execute> or the attribute \"execute\" of task win32.shortcut");
        }
        execute = getProject().replaceProperties(string);
    }

    public class WorkingDirectory {

        public void addText(String s) {
            execute = getProject().replaceProperties(s.trim());
        }
    }

    public WorkingDirectory createWorkingDirectory() {
        if (workingDirectory != null) {
            throw new BuildException("Property \"workingdirectory\" is always defined. You have to use either <workingdirectory> or the \"workingdirectory\"attribute of task win32.shortcut");
        }
        return new WorkingDirectory();
    }

    public void setWorkingDirectory(String string) {
        if (workingDirectory != null) {
            throw new BuildException("Property \"workingdirectory\" is always defined. You have to use either <workingdirectory> or the \"workingdirectory\"attribute of task win32.shortcut");
        }
        workingDirectory = string;
    }

    public void execute() throws BuildException {
        if (file == null) {
            throw new BuildException("Attribute \"file\" is required by task win23.shortcut");
        } else {
            log("Creating shortcut " + file.getAbsolutePath(), Project.MSG_INFO);
        }
        if ((url != null) == (execute != null)) {
            throw new BuildException("Either attribute \"url\" or \"execute\" has to be set in win23.shortcut");
        }
        try {
            if (url != null) {
                UrlFile shortcut = new UrlFile(file, false);
                if (hotKeyKeyCode == -1) {
                    log("Defined \"arguments\" will be ignored for .url shortcuts", Project.MSG_WARN);
                } else {
                    shortcut.setHotKey(hotKeyModifier, hotKeyKeyCode);
                }
                shortcut.setIconFile(iconFile);
                shortcut.setIconIndex(iconIndex);
                shortcut.setModified(modified);
                shortcut.setShowCommand(showCommand);
                if (comment != null) {
                    log("Defined \"comment\" will be ignored for .url shortcuts", Project.MSG_WARN);
                }
                shortcut.setUrl(url);
                shortcut.setWorkingDirectory(workingDirectory);
                shortcut.save();
            } else {
                File parent = file.getParentFile();
                if (parent.exists() && !parent.isDirectory()) {
                    throw new BuildException("parent file " + parent.getAbsolutePath() + " is not a directory !");
                }
                if (!parent.exists()) {
                    boolean success = parent.mkdirs();
                    if (!success) {
                        throw new BuildException("Creating parent directory " + parent.getAbsolutePath() + " failed.");
                    }
                }
                LnkFile shortcut = new LnkFile(file, false);
                shortcut.setArguments(arguments);
                if (hotKeyKeyCode == -1) {
                    log("Defined \"hotkey\" will be ignored for .lnk shortcuts", Project.MSG_WARN);
                } else {
                    shortcut.setHotkey(hotKeyModifier, hotKeyKeyCode);
                }
                shortcut.setIconLocation(iconFile);
                shortcut.setIconIndex(iconIndex);
                if (modified != null) {
                    log("Defined \"modified\" will be ignored for .lnk shortcuts", Project.MSG_WARN);
                }
                if (showCommand != -1) {
                    log("Defined \"showcommand\" will be ignored for .lnk shortcuts", Project.MSG_WARN);
                }
                shortcut.setPath(execute);
                shortcut.setWorkingDirectory(workingDirectory);
                shortcut.setDescription(comment);
                shortcut.save();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BuildException("Error writing shortcut " + file.getAbsolutePath() + " : " + ex.getMessage(), ex);
        }
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }
}
