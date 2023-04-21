package net.sf.jvifm.control;

import java.io.File;
import net.sf.jvifm.Main;
import net.sf.jvifm.model.Preference;
import net.sf.jvifm.ui.FileManager;
import net.sf.jvifm.ui.Util;
import net.sf.jvifm.ui.shell.AboutShell;
import net.sf.jvifm.ui.shell.QuickRunShell;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.swt.program.Program;

public class MetaCommand extends Command {

    private static String[] cmdNames = new String[] { "quit", "hide", "only", "split", "help", "sync", "conf", "locate", "sh", "bookmarks", "history", "folder", "shortcuts", "hidesidebar", "kill", "about", "preview", "nopreview", "brief", "detail" };

    private String cmd;

    private FileManager fileManager = Main.fileManager;

    public static String[] getCmdNames() {
        return cmdNames;
    }

    public MetaCommand(String cmdText) {
        this.cmd = cmdText;
    }

    public void execute() {
        if (cmd.equals("kill")) {
            if (Main.currentJob != null) Main.currentJob.abort();
            return;
        }
        if (cmd.equals("about")) {
            AboutShell aboutShell = new AboutShell();
            aboutShell.showGUI();
        }
        if (cmd.equals("quit")) {
            Main.exit();
            return;
        }
        if (cmd.equals("hide")) {
            fileManager.hide();
            return;
        }
        if (cmd.equals("only")) {
            fileManager.only();
        }
        if (cmd.equals("split")) {
            fileManager.split();
        }
        if (cmd.equals("preview")) {
            fileManager.getActivePanel().addListener(fileManager);
            fileManager.getActivePanel().notifyChangeSelection();
        }
        if (cmd.equals("nopreview")) {
            fileManager.getActivePanel().removeListener(fileManager);
            fileManager.nopreview();
        }
        if (cmd.equals("detail")) {
            fileManager.getActivePanel().detail();
        }
        if (cmd.equals("brief")) {
            fileManager.getActivePanel().brief();
        }
        if (cmd.equals("help")) {
            String EDITOR = Preference.getInstance().getEditorApp();
            String workdir = System.getProperty("user.dir");
            String path = new File(workdir).getParent() + "/doc/help.txt";
            String cmd[] = { EDITOR, path };
            try {
                Runtime.getRuntime().exec(cmd);
            } catch (Exception ex) {
                String ext = FilenameUtils.getExtension(path);
                Program program = Program.findProgram(ext);
                if (program != null) program.execute(path);
            }
            return;
        }
        if (cmd.equals("sync")) {
            if (inActiveFileLister != null) inActiveFileLister.visit(pwd);
            return;
        }
        if (cmd.equals("conf")) {
            Util.openPreferenceShell(fileManager.getShell());
            return;
        }
        if (cmd.equals("sh")) {
            Util.openTerminal(pwd);
        }
        if (cmd.equals("bookmarks")) {
            fileManager.showBookmarkSidevew();
            fileManager.activeSideView();
        }
        if (cmd.equals("history")) {
            fileManager.showHistorySideview();
            fileManager.activeSideView();
        }
        if (cmd.equals("shortcuts")) {
            fileManager.showShortcutsSideview();
            fileManager.activeSideView();
        }
        if (cmd.equals("folder")) {
            fileManager.showFileTree();
            fileManager.activeSideView();
        }
        if (cmd.equals("hidesidebar")) {
            Main.fileManager.hideSideBar();
        }
    }

    public static boolean isMetaCommand(String cmd) {
        for (int i = 0; i < cmdNames.length; i++) {
            if (cmdNames[i].equals(cmd)) return true;
        }
        return false;
    }
}
