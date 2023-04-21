package gwtm.server.util.files;

import gwtm.server.util.containers.MyTocs;
import java.io.*;
import java.util.*;

/**
 *
 * @author  George Tryfon 
 */
public class Dir {

    public FName m_FullPath;

    private File[] m_SubDirs = new File[0];

    private File[] m_Files = new File[0];

    public Dir() {
        m_FullPath = new FName();
    }

    public void freeMem() {
        m_FullPath.set("");
        m_SubDirs = new File[0];
        m_Files = new File[0];
    }

    public void loadDir(String Filter) throws Exception {
        FName fn = new FName(Filter);
        loadDir(fn);
    }

    public void loadDir(FName Filter) throws Exception {
        freeMem();
        m_FullPath.set(Filter);
        class Filt implements FilenameFilter {

            String m_Name;

            String m_Ext;

            Filt(String s) {
                FName fn = new FName(s);
                m_Name = fn.getName();
                m_Ext = fn.getExt();
            }

            public boolean accept(File f, String NameExt) {
                FName fn = new FName(NameExt);
                String Name = fn.getName();
                String Ext = fn.getExt();
                if (!_accept(m_Name, Name)) return false;
                if (!_accept(m_Ext, Ext)) return false;
                return true;
            }

            boolean _accept(String Mask, String s) {
                if (s.equalsIgnoreCase(Mask)) return true;
                if (Mask.equalsIgnoreCase("*")) return true;
                int pos = 0;
                for (int i = 0; i < Mask.length(); i++) {
                    if (pos >= s.length()) return false;
                    char c = Mask.charAt(i);
                    char cS = s.charAt(pos);
                    if (c == '?') {
                        pos++;
                        continue;
                    } else if (c == '*') return true; else {
                        if (c == cS) {
                            pos++;
                            continue;
                        } else return false;
                    }
                }
                return (pos == s.length());
            }
        }
        FName JustDir = new FName(Filter);
        JustDir.setNameExt("");
        if (JustDir.getFullPath().equals("")) JustDir.setDrivePath(".");
        Vector vSubDirs = new Vector();
        File fDirs = new File(JustDir.getFullPath());
        String AllDirs[] = fDirs.list();
        for (int i = 0; i < AllDirs.length; i++) {
            FName fn = new FName(JustDir);
            fn.append(AllDirs[i]);
            File f = new File(fn.getFullPath());
            if (f.isDirectory()) vSubDirs.addElement(f);
        }
        m_SubDirs = new File[vSubDirs.size()];
        for (int i = 0; i < vSubDirs.size(); i++) m_SubDirs[i] = (File) vSubDirs.elementAt(i);
        Vector vFiles = new Vector();
        File fFiles = new File(JustDir.getFullPath());
        Filt filt = new Filt(Filter.getNameExt());
        String[] AllFiles = fFiles.list(filt);
        for (int i = 0; i < AllFiles.length; i++) {
            FName fn = new FName(JustDir);
            fn.append(AllFiles[i]);
            File f = new File(fn.getFullPath());
            if (!f.isDirectory()) vFiles.addElement(f);
        }
        m_Files = new File[vFiles.size()];
        for (int i = 0; i < vFiles.size(); i++) m_Files[i] = (File) vFiles.elementAt(i);
    }

    public File[] getSubDirs() {
        return m_SubDirs;
    }

    public File[] getFiles() {
        return m_Files;
    }

    public static void createThisDirectory_(String fn) throws IOException {
        FName fn_ = new FName(fn);
        createThisDirectory(fn_.getDrivePath());
    }

    public static void createThisDirectory(String dir) throws IOException {
        String _dir = dir;
        if (_dir.endsWith("\\")) _dir = _dir.substring(0, _dir.length() - 1);
        File f = new File(_dir);
        if (f.exists()) return;
        FName fn = new FName(_dir);
        String dr = fn.getPathNameExt();
        MyTocs t = new MyTocs(dr, "\\/", false);
        FName fn_ = new FName(fn.getDrive());
        for (int i = 0; i < t.size(); i++) {
            fn_.append(t.ElementAt(i));
            File f_ = new File(fn_.getFullPath());
            if (!f_.exists()) f_.mkdir();
        }
    }
}
