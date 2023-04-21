package net.pms.gui;

public interface IFrame {

    public void append(String msg);

    public void setValue(int v, String msg);

    public void setReadValue(long v, String msg);

    public void setStatusCode(int code, String msg, String icon);

    public void addRendererIcon(int code, String msg, String icon);

    public void setReloadable(boolean reload);

    public void addEngines();

    public void setStatusLine(String line);

    public void serverReady();

    public void setScanLibraryEnabled(boolean flag);
}
