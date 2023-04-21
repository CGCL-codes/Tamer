package org.xmlsh.commands.posix;

import java.io.File;
import java.util.List;
import org.xmlsh.core.InvalidArgumentException;
import org.xmlsh.core.Options;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;

public class chmod extends XCommand {

    private static class Perm {

        boolean bRead = false;

        boolean bWrite = false;

        boolean bExecute = false;

        Perm(File f) {
            bRead = f.canRead();
            bWrite = f.canWrite();
            bExecute = f.canExecute();
        }

        Perm(String s) {
            bRead = s.contains("r");
            bWrite = s.contains("w");
            bExecute = s.contains("x");
        }

        void add(Perm p) {
            bRead = bRead || p.bRead;
            bWrite = bWrite || p.bWrite;
            bExecute = bExecute || p.bExecute;
        }

        void sub(Perm p) {
            if (p.bRead) bRead = false;
            if (p.bWrite) bWrite = false;
            if (p.bExecute) bExecute = false;
        }
    }

    private boolean bRecurse;

    private void applyMode(String mode, File file) throws InvalidArgumentException {
        for (String s : mode.split(",")) {
            applyMode2(s, file);
        }
        if (bRecurse && file.isDirectory()) {
            for (File child : file.listFiles()) {
                applyMode(mode, child);
            }
        }
    }

    private void applyMode2(String mode, File file) throws InvalidArgumentException {
        boolean bOther = false;
        boolean bOwner = false;
        String[] fl = mode.split("[-+=]");
        if (fl.length != 2) throw new InvalidArgumentException("Unexpected mode string: " + mode);
        String users = fl[0];
        String perms = fl[1];
        String plusminus = mode.substring(users.length(), mode.length() - perms.length());
        Perm permFile = new Perm(file);
        Perm permAll = permFile;
        Perm permUser = permFile;
        Perm permMode = new Perm(perms);
        bOther = users.contains("o") || users.contains("a");
        bOwner = users.contains("u") || users.contains("a");
        if (plusminus.equals("+")) {
            if (bOther) permAll.add(permMode);
            if (bOwner) permUser.add(permMode);
        } else if (plusminus.equals("-")) {
            if (bOther) permAll.sub(permMode);
            if (bOwner) permUser.sub(permMode);
        } else if (plusminus.equals("=")) {
            if (bOther) permAll = permMode;
            if (bOwner) permUser = permMode;
        }
        if (bOther) {
            file.setReadable(permAll.bRead, false);
            file.setExecutable(permAll.bExecute, false);
            file.setWritable(permAll.bWrite, false);
        }
        if (bOwner) {
            file.setReadable(permUser.bRead, true);
            file.setExecutable(permUser.bExecute, true);
            file.setWritable(permUser.bWrite, true);
        }
    }

    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("R=recurse");
        opts.parse(args);
        args = opts.getRemainingArgs();
        bRecurse = opts.hasOpt("R");
        String mode = args.remove(0).toString();
        for (XValue arg : args) {
            File f = this.getFile(arg);
            applyMode(mode, f);
        }
        return 0;
    }
}
