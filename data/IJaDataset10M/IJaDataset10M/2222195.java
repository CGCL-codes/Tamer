package org.gudy.azureus2.platform.win32.access.impl;

public interface AEWin32AccessCallback {

    public long windowsMessage(int msg, int param1, long param2);

    public long generalMessage(String msg);
}
