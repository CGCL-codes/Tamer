package com.yubarta.docman;

import java.util.TreeSet;

public interface DocPermission {

    public boolean userCanRead(String string);

    public void setUserCanRead(String string, boolean bool);

    public boolean userCanWrite(String string);

    public void setUserCanWrite(String string, boolean bool);

    public boolean groupCanRead(String string);

    public void setGroupCanRead(String string, boolean bool);

    public TreeSet<String> listReaderUsers();

    public TreeSet<String> listWriterUsers();

    public TreeSet<String> listReaderGroups();

    public TreeSet<String> listWriterGroups();

    public boolean groupCanWrite(String string);

    public void setGroupCanWrite(String string, boolean bool);

    public boolean othersCanRead();

    public void setOthersCanRead(boolean bool);

    public boolean othersCanWrite();

    public void setOthersCanWrite(boolean bool);
}
