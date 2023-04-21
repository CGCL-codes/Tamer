package org.chernovia.lib.netgames;

public class JPlayer implements Runnable {

    public static int MAX_VERBOSITY = 10;

    private Thread AskThread;

    private JBoard curbo;

    private String RESPONSE;

    private int VERBOSITY;

    private boolean AUTO, AI;

    private boolean LEAVING, JOINING;

    public String name;

    public JPlayer(String n, JBoard b) {
        name = n;
        curbo = b;
        VERBOSITY = MAX_VERBOSITY;
        AUTO = false;
        AI = false;
        RESPONSE = null;
        AskThread = null;
        LEAVING = false;
        JOINING = false;
    }

    public boolean isAuto() {
        return AUTO;
    }

    public boolean isBorg() {
        return AI;
    }

    public boolean isLeaving() {
        return LEAVING;
    }

    public void setLeaving(boolean leaving) {
        LEAVING = leaving;
    }

    public boolean isJoining() {
        return JOINING;
    }

    public void setJoining(boolean joining) {
        JOINING = joining;
    }

    public void automate(boolean a) {
        AUTO = a;
        if (a) wakeup();
    }

    public void borg(boolean b) {
        AI = b;
        if (b) wakeup();
    }

    public int getVerbosity() {
        return VERBOSITY;
    }

    public void setVerbosity(int level) {
        VERBOSITY = level;
    }

    public JBoard getBoard() {
        return curbo;
    }

    public String ask(String h) {
        return ask(h, "", "");
    }

    public String ask(String h, String d) {
        return ask(h, "", d);
    }

    public int ask(String h, int d) {
        try {
            return Integer.parseInt(ask(h, "", "" + d));
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public int ask(String h, String m, int d) {
        try {
            return Integer.parseInt(ask(h, m, "" + d));
        } catch (NumberFormatException e) {
            return d;
        }
    }

    public String ask(String headstr, String msg, String def) {
        if (curbo.getManualPlayers() == 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignore) {
            }
        }
        if (AUTO || AI || curbo.isClosing()) return def;
        RESPONSE = "";
        if (!def.equals("")) {
            tell(headstr + " (Default: " + def + ")");
        } else tell(headstr);
        if (!msg.equals("")) qtell(msg);
        JBoard.getBot().log.println("Waiting for: " + name);
        AskThread = new Thread(this);
        AskThread.start();
        try {
            AskThread.join();
        } catch (InterruptedException ignore) {
        }
        if (RESPONSE.equals("")) RESPONSE = def;
        JBoard.getBot().log.println(name + " responded with: " + RESPONSE);
        AskThread = null;
        return RESPONSE;
    }

    public void wakeup() {
        if (isWaiting()) AskThread.interrupt();
    }

    public boolean isWaiting() {
        return (AskThread != null);
    }

    public void run() {
        try {
            Thread.sleep(curbo.getTimeout() * 1000);
        } catch (InterruptedException e) {
            return;
        }
        curbo.timeout(this);
    }

    public void tell(String msg) {
        JBoard.getBot().getServ().say(name, msg);
    }

    public void qtell(String msg) {
        JBoard.getBot().getServ().qSay(name, msg);
    }

    public boolean confirm(String msg, String def) {
        String confstr = ask(msg + " (y/n)", def).toLowerCase();
        if (confstr.startsWith("y")) return true; else return false;
    }

    public void setResponse(String respstr) {
        RESPONSE = respstr;
        wakeup();
    }

    public String toString(String fingerer) {
        return toString();
    }
}
