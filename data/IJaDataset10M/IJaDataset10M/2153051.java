package net.sf.warpcore.ejb;

import java.io.*;

public class UniquePK extends File {

    String _group;

    public UniquePK(String group, File parent, String child) {
        super(parent, child);
        _group = group;
    }

    public UniquePK(String group, String pathname) {
        super(pathname);
        _group = group;
    }

    public UniquePK(String group, String parent, String child) {
        super(parent, child);
        _group = group;
    }

    public UniquePK[] listChildren() {
        String[] files = list();
        UniquePK[] result = new UniquePK[files.length];
        for (int i = 0; i < files.length; i++) {
            result[i] = new UniquePK(_group, this, files[i]);
        }
        return result;
    }

    public String getGroup() {
        return _group;
    }

    public void setGroup(String group) {
        _group = group;
    }
}
