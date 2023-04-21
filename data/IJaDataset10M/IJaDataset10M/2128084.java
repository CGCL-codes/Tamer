package org.eclipse.swt.internal.win32;

public class SHELLEXECUTEINFO {

    public int cbSize;

    public int fMask;

    public int hwnd;

    public int lpVerb;

    public int lpFile;

    public int lpParameters;

    public int lpDirectory;

    public int nShow;

    public int hInstApp;

    public int lpIDList;

    public int lpClass;

    public int hkeyClass;

    public int dwHotKey;

    public int hIcon;

    public int hProcess;

    public static final int sizeof = 60;
}
