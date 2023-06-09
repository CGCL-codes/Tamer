package alchemy.apps;

import alchemy.core.Context;
import alchemy.fs.File;
import alchemy.nlib.NativeApp;
import alchemy.util.IO;
import alchemy.util.UTFReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Vector;
import alchemy.apps.ConsoleForm.ConsoleInputStream;
import alchemy.util.I18N;

/**
 * Native shell.
 * @author Sergey Basalaev
 */
public class Shell extends NativeApp {

    private static final String C_USAGE = I18N._("Usage: sh -c <cmd> <args>...");

    public Shell() {
    }

    public int main(Context c, String[] args) {
        try {
            InputStream scriptinput;
            if (args.length == 0) {
                scriptinput = c.stdin;
            } else if (args[0].equals("-c")) {
                if (args.length < 2) {
                    IO.println(c.stderr, C_USAGE);
                    return -1;
                }
                StringBuffer cmdline = new StringBuffer(args[1]);
                for (int i = 2; i < args.length; i++) {
                    cmdline.append(' ').append(args[i]);
                }
                scriptinput = new ByteArrayInputStream(IO.utfEncode(cmdline.toString()));
            } else {
                scriptinput = c.fs().read(c.toFile(args[0]));
            }
            if (c.stdin instanceof ConsoleInputStream) {
                ((ConsoleInputStream) c.stdin).setPrompt(c.getCurDir().toString() + '>');
            }
            UTFReader r = new UTFReader(scriptinput);
            while (true) try {
                String line = r.readLine();
                if (line == null) {
                    if (scriptinput instanceof ConsoleInputStream) continue; else break;
                }
                line = line.trim();
                if (line.length() == 0) continue;
                if (line.charAt(0) == '#') continue;
                ShellCommand cc = null;
                try {
                    cc = split(line);
                } catch (IllegalArgumentException e) {
                    IO.println(c.stderr, r.lineNumber() + ':' + e.getMessage());
                    if (scriptinput instanceof ConsoleInputStream) continue; else return 1;
                }
                if (cc.cmd.equals("exit")) {
                    int ret = 0;
                    if (cc.args.length > 0) try {
                        ret = Integer.parseInt(cc.args[0]);
                    } catch (NumberFormatException e) {
                        ret = 1;
                    }
                    return ret;
                } else if (cc.cmd.equals("cd")) {
                    if (cc.args.length > 0) {
                        File newdir = c.toFile(cc.args[0]);
                        c.setCurDir(newdir);
                        if (c.stdin instanceof ConsoleInputStream) {
                            ((ConsoleInputStream) c.stdin).setPrompt(c.getCurDir().toString() + '>');
                        }
                    } else {
                        IO.println(c.stderr, I18N._("cd: no directory specified"));
                    }
                } else if (cc.cmd.equals("cls")) {
                    if (c.stdin instanceof ConsoleInputStream) {
                        ((ConsoleInputStream) c.stdin).clearScreen();
                    }
                } else {
                    Context child = new Context(c);
                    if (cc.in != null) {
                        child.stdin = c.fs().read(c.toFile(cc.in));
                        child.addStream(child.stdin);
                    }
                    if (cc.out != null) {
                        File outfile = c.toFile(cc.out);
                        if (cc.appendout) {
                            child.stdout = c.fs().append(outfile);
                        } else {
                            child.stdout = c.fs().write(outfile);
                        }
                        child.addStream(child.stdout);
                    }
                    if (cc.err != null) {
                        File errfile = c.toFile(cc.err);
                        if (cc.appenderr) {
                            child.stderr = c.fs().append(errfile);
                        } else {
                            child.stderr = c.fs().write(errfile);
                        }
                        child.addStream(child.stderr);
                    }
                    child.startAndWait(cc.cmd, cc.args);
                    Throwable err = child.getError();
                    if (err != null) {
                        IO.println(c.stderr, err);
                        IO.println(c.stderr, child.dumpCallStack());
                    }
                }
            } catch (Throwable t) {
                IO.println(c.stderr, t);
            }
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            IO.println(c.stderr, e);
            return 1;
        }
    }

    private static final int MODE_CMD = 0;

    private static final int MODE_ARG = 1;

    private static final int MODE_QARG = 2;

    private static final int MODE_IN = 3;

    private static final int MODE_OUT_W = 4;

    private static final int MODE_OUT_A = 5;

    private static final int MODE_ERR_W = 6;

    private static final int MODE_ERR_A = 7;

    private ShellCommand split(String line) throws IllegalArgumentException {
        ShellCommand cc = new ShellCommand();
        Vector argv = new Vector();
        int mode = MODE_CMD;
        while (line.length() > 0) {
            int end;
            String token;
            if (line.charAt(0) == '\'') {
                end = line.indexOf('\'', 1);
                if (end < 0) throw new IllegalArgumentException(I18N._("Unclosed '"));
                token = line.substring(1, end);
                line = line.substring(end + 1).trim();
                if (mode == MODE_ARG) mode = MODE_QARG;
            } else if (line.charAt(0) == '"') {
                end = line.indexOf('"', 1);
                if (end < 0) throw new IllegalArgumentException(I18N._("Unclosed \""));
                token = line.substring(1, end);
                line = line.substring(end + 1).trim();
                if (mode == MODE_ARG) mode = MODE_QARG;
            } else if (line.charAt(0) == '#') {
                break;
            } else {
                end = line.indexOf(' ');
                if (end < 0) end = line.length();
                token = line.substring(0, end);
                line = line.substring(end).trim();
            }
            if (token.length() == 0) continue;
            switch(mode) {
                case MODE_CMD:
                    cc.cmd = token;
                    mode = MODE_ARG;
                    break;
                case MODE_IN:
                    cc.in = token;
                    mode = MODE_ARG;
                    break;
                case MODE_OUT_W:
                    cc.out = token;
                    cc.appendout = false;
                    mode = MODE_ARG;
                    break;
                case MODE_OUT_A:
                    cc.out = token;
                    cc.appendout = true;
                    mode = MODE_ARG;
                    break;
                case MODE_ERR_W:
                    cc.err = token;
                    cc.appenderr = false;
                    mode = MODE_ARG;
                    break;
                case MODE_ERR_A:
                    cc.err = token;
                    cc.appenderr = true;
                    mode = MODE_ARG;
                    break;
                case MODE_ARG:
                    if (token.equals(">") || token.equals("1>")) mode = MODE_OUT_W; else if (token.equals(">>") || token.equals("1>>")) mode = MODE_OUT_A; else if (token.equals("2>")) mode = MODE_ERR_W; else if (token.equals("2>>")) mode = MODE_ERR_A; else if (token.equals("<")) mode = MODE_IN; else argv.addElement(token);
                    break;
                case MODE_QARG:
                    argv.addElement(token);
                    mode = MODE_ARG;
            }
        }
        String[] args = new String[argv.size()];
        for (int i = 0; i < argv.size(); i++) {
            args[i] = argv.elementAt(i).toString();
        }
        cc.args = args;
        return cc;
    }

    private static class ShellCommand {

        public String cmd;

        public String[] args;

        public String in;

        public String out;

        public boolean appendout;

        public String err;

        public boolean appenderr;
    }
}
