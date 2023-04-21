package net.sf.refactorit.netbeans.common.vcs;

import net.sf.refactorit.common.util.StringUtil;
import org.netbeans.modules.vcscore.VcsConfigVariable;
import org.netbeans.modules.vcscore.VcsFileSystem;
import org.netbeans.modules.vcscore.commands.VcsCommand;
import java.util.Vector;

class BinaryFilesMode implements RestorableSetting {

    private static final String variableName = "PROCESSING_BINARY_FILES";

    private final VcsFileSystem fs;

    private final VcsCommand cmd;

    private String oldValue = null;

    BinaryFilesMode(VcsFileSystem fs, VcsCommand cmd) {
        this.fs = fs;
        this.cmd = cmd;
    }

    void setBinaryFilesMode(boolean isBinary) {
        removeAddCommandPropertyHackIfNeeded();
        String newValue = isBinary ? Boolean.TRUE.toString() : "";
        oldValue = setVariable(variableName, newValue);
    }

    public void restore() {
        setVariable(variableName, oldValue);
    }

    private String setVariable(String name, String newValue) {
        synchronized (fs) {
            Vector variables = fs.getVariables();
            try {
                return setVariable(name, newValue, variables);
            } finally {
                fs.setVariables(variables);
            }
        }
    }

    private String setVariable(String name, String newValue, Vector variables) {
        VcsConfigVariable existing = findVariable(name, variables);
        if (existing != null) {
            return setValue(existing, newValue, variables);
        } else {
            return createVariable(newValue, name, variables);
        }
    }

    private String createVariable(String newValue, String name, Vector variables) {
        String result;
        result = null;
        if (newValue != null) {
            variables.add(new VcsConfigVariable(name, null, newValue, false, false, false, null));
        }
        return result;
    }

    private String setValue(VcsConfigVariable existing, String newValue, Vector allVariables) {
        String result;
        result = existing.getValue();
        if (newValue != null) {
            existing.setValue(newValue);
        } else {
            allVariables.remove(existing);
        }
        return result;
    }

    private VcsConfigVariable findVariable(String name, Vector variables) {
        VcsConfigVariable result = null;
        for (int i = 0; i < variables.size(); i++) {
            VcsConfigVariable var = (VcsConfigVariable) variables.get(i);
            if (var.getName().equals(name)) {
                result = var;
            }
        }
        return result;
    }

    /**
   * Restores ADD command's confing to what it was before a 
   * previous version of RefactorIT changed it. 
   */
    private void removeAddCommandPropertyHackIfNeeded() {
        String exec = (String) cmd.getProperty("exec");
        String from = "add -kb ";
        String to = "add ";
        if (exec.indexOf(from) >= 0) {
            exec = StringUtil.replace(exec, from, to);
            cmd.setProperty("exec", exec);
        }
    }
}
