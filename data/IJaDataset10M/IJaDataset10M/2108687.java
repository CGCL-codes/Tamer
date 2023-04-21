package com.ev.evgetme.chooser;

import java.util.Enumeration;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;
import com.ev.evgetme.FileSysExt;

/**
 * @author Ernestas Vaiciukevicius 
 *
 */
public class DirectoryChooser extends List implements Chooser, CommandListener {

    private ChooserListener listener;

    private String selectedDir;

    private FileSysExt fs;

    private Command backCmd, okCmd, cancelCmd;

    public DirectoryChooser(FileSysExt fsExt, String startDir) {
        super(startDir, List.IMPLICIT);
        this.fs = fsExt;
        setSelection(startDir);
        backCmd = new Command("Back", Command.BACK, 20);
        okCmd = new Command("Ok", Command.OK, 30);
        cancelCmd = new Command("Cancel", Command.CANCEL, 40);
        addCommand(backCmd);
        addCommand(okCmd);
        addCommand(cancelCmd);
        setCommandListener(this);
        populateList();
    }

    protected void populateList() {
        while (size() > 0) {
            delete(size() - 1);
        }
        setTitle(selectedDir);
        try {
            Enumeration enumer = fs.list(selectedDir);
            while (enumer.hasMoreElements()) {
                String fsObj = (String) enumer.nextElement();
                if (fsObj.endsWith("/") || fs.isDir(selectedDir + fsObj)) {
                    append(fsObj, null);
                }
            }
            removeCommand(backCmd);
            if (selectedDir.length() > 1) {
                addCommand(backCmd);
            }
        } catch (Exception e) {
            append("Error:" + e.toString(), null);
            append("FS: " + fs, null);
            removeCommand(okCmd);
            removeCommand(backCmd);
            e.printStackTrace();
        }
    }

    public void commandAction(Command cmd, Displayable arg1) {
        if (cmd == backCmd) {
            int dirInd = selectedDir.lastIndexOf('/', selectedDir.length() - 2);
            if (dirInd >= 0) {
                selectedDir = selectedDir.substring(0, dirInd + 1);
                populateList();
            }
        } else if (cmd == SELECT_COMMAND) {
            selectedDir += getString(getSelectedIndex());
            if (selectedDir.charAt(selectedDir.length() - 1) != '/') selectedDir += '/';
            populateList();
        } else if (cmd == okCmd) {
            if (getChooserListener() != null) {
                getChooserListener().objectSelected(this);
            }
        } else if (cmd == cancelCmd) {
            if (getChooserListener() != null) {
                getChooserListener().chooserCanceled(this);
            }
        }
    }

    public String getSelection() {
        return selectedDir;
    }

    public void setSelection(String selectedDir) {
        if (!selectedDir.endsWith("/")) {
            selectedDir += '/';
        }
        this.selectedDir = selectedDir;
        populateList();
    }

    public void setChooserListener(ChooserListener listener) {
        this.listener = listener;
    }

    public ChooserListener getChooserListener() {
        return listener;
    }
}
