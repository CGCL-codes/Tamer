package net.sourceforge.eclipsesyslog.views;

public abstract class SyslogThread extends Thread {

    public abstract boolean getFinishThread();

    public abstract void setFinishThread(boolean finish);
}
