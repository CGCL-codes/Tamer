package com.pjsofts.eurobudget;

import com.pjsofts.eurobudget.beans.*;
import com.pjsofts.eurobudget.data.FileType;
import com.pjsofts.gui.PasswordChangePanel;
import com.pjsofts.gui.PasswordLoginPanel;
import java.beans.ExceptionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * EuroBudget File Library
 * Note on xml encoding/decoding:seems to fail on ArrayList;TreeSet and some others ! (see java.beans.MetaData, XMLEncoder)
 * Contains all methods to load/save files.
 *
 * @author  pj
 */
public abstract class FileLoader {

    private static final ResourceBundle i18n = com.pjsofts.eurobudget.EBConstants.i18n;

    /** Salt for encryption */
    private static final byte[] salt = { (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c, (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99 };

    /** last password used */
    private static char[] lastPassword = null;

    /** Creates a new instance of FileLoader */
    private FileLoader() {
    }

    /**
     * @param name Name or description (static variable defined in this class) of a file type.
     * @return FileFilter corresponding to this file type or null if wrong */
    public static FileFilter getFileFilter(String name) {
        if (name == null) return null;
        if (name.equalsIgnoreCase(FileType.TYPE_EBF.name) || name.equalsIgnoreCase(FileType.TYPE_EBF.desc)) {
            return FileType.TYPE_EBF;
        } else if (name.equalsIgnoreCase(FileType.TYPE_EBZ.name) || name.equalsIgnoreCase(FileType.TYPE_EBZ.desc)) {
            return FileType.TYPE_EBZ;
        } else if (name.equalsIgnoreCase(FileType.TYPE_EBP.name) || name.equalsIgnoreCase(FileType.TYPE_EBP.desc)) {
            return FileType.TYPE_EBP;
        } else if (name.equalsIgnoreCase(FileType.TYPE_QIF.name) || name.equalsIgnoreCase(FileType.TYPE_QIF.desc)) {
            return FileType.TYPE_QIF;
        }
        return null;
    }

    /**
     * convenient method : load a file based on its type (extension).
     * @return a FileData if ok ; null otherwhise */
    public static FileData fileLoad(File f) {
        FileData result = null;
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            if (FileType.TYPE_EBZ.isFileType(f)) {
                FileLoader.lastPassword = null;
                result = fileLoadEBZ(is);
            } else if (FileType.TYPE_EBP.isFileType(f)) {
                result = fileLoadEBP(is);
            } else {
                FileLoader.lastPassword = null;
                result = fileLoadEBF(is);
            }
        } catch (IOException ioe) {
            Log.logException(ioe);
        }
        return result;
    }

    /** @return a FileData if ok ; null otherwhise */
    public static FileData fileLoadEBF(InputStream f) {
        ObjectInputStream e = null;
        FileData fd = null;
        try {
            e = new ObjectInputStream(new BufferedInputStream(f));
            String stamp = e.readUTF();
            if (!stamp.startsWith(FileData.FILE_STAMP)) {
                JOptionPane.showMessageDialog(null, i18n.getString("error_File_is_not_a_good_EuroBudget_file"), i18n.getString("Warning"), JOptionPane.WARNING_MESSAGE);
            }
            int fversion = e.readInt();
            if (fversion == 1) {
                fd = (FileData) e.readObject();
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("error_File_is_not_of_the_right_version"), i18n.getString("Warning"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException fne) {
            fne.printStackTrace();
            fd = null;
        } catch (FileNotFoundException fne) {
            fd = null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            fd = null;
        } finally {
            if (e != null) {
                try {
                    e.close();
                } catch (IOException ioe) {
                }
            }
            try {
                f.close();
            } catch (IOException ioe) {
            }
        }
        return fd;
    }

    /** @return a FileData if ok ; null otherwhise */
    public static FileData fileLoadEBP(InputStream f) {
        InputStream in = null;
        FileData fd = null;
        try {
            in = new BufferedInputStream(f);
            ObjectInputStream clearIn = new ObjectInputStream(in);
            String stamp = clearIn.readUTF();
            if (!stamp.startsWith(FileData.FILE_STAMP)) {
                JOptionPane.showMessageDialog(null, i18n.getString("error_File_is_not_a_good_EuroBudget_file"), i18n.getString("Warning"), JOptionPane.WARNING_MESSAGE);
            }
            int fversion = clearIn.readInt();
            String passwordHint = clearIn.readUTF();
            if (fversion == 1) {
                PBEKeySpec pbeKeySpec;
                PBEParameterSpec pbeParamSpec;
                SecretKeyFactory keyFac;
                SecretKey pbeKey;
                Cipher pbeCipher = null;
                PasswordLoginPanel pwPanel = new PasswordLoginPanel(passwordHint);
                int pcpChoice = JOptionPane.showConfirmDialog(null, pwPanel, "Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (pcpChoice == JOptionPane.OK_OPTION) {
                    char[] password = pwPanel.getPassword();
                    FileLoader.lastPassword = password;
                    int count = 20;
                    pbeParamSpec = new PBEParameterSpec(salt, count);
                    pbeKeySpec = new PBEKeySpec(password);
                    keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
                    pbeKey = keyFac.generateSecret(pbeKeySpec);
                    pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
                    pbeCipher.init(Cipher.DECRYPT_MODE, pbeKey, pbeParamSpec);
                    ObjectInputStream cryptIn = new ObjectInputStream(new CipherInputStream(in, pbeCipher));
                    fd = (FileData) cryptIn.readObject();
                    cryptIn.close();
                    clearIn.close();
                }
            } else {
                JOptionPane.showMessageDialog(null, i18n.getString("error_File_is_not_of_the_right_version"), i18n.getString("Warning"), JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            fd = null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ioe) {
                }
            }
            try {
                f.close();
            } catch (IOException ioe) {
            }
        }
        return fd;
    }

    /** @return a FileDate if ok ; null otherwhise */
    public static FileData fileLoadEBZ(InputStream f) {
        FileData fd = null;
        XMLDecoder e = null;
        try {
            e = new XMLDecoder(new BufferedInputStream(f));
            e.setExceptionListener(new MyExceptionListener());
            fd = (FileData) e.readObject();
        } finally {
            if (e != null) {
                e.close();
            }
            try {
                f.close();
            } catch (IOException ioe) {
            }
        }
        return fd;
    }

    /**
     * Manage EBZ, EBF, and EBP file formats (filters).
     * General method that will call specialized one.
     *
     * @param fd Data to save
     * @param f Wished saved file but may change (could add extension or change format)
     * @param filter define the file format asked (could be null, in that case take format of extension )
     * @param saveAsMode force ask for current password (works with EBP only) (saveAs mode)
     * @return the file saved, could be different as the one given in parameter */
    public static File fileSave(FileData fd, File f, FileType filter, boolean saveAsMode) {
        OutputStream os = null;
        try {
            if (filter == null) {
                os = new FileOutputStream(f, false);
                if (FileType.TYPE_EBZ.isFileType(f)) {
                    FileLoader.lastPassword = null;
                    fileSaveEBZ(fd, os);
                } else if (FileType.TYPE_EBP.isFileType(f)) {
                    if (saveAsMode) FileLoader.lastPassword = null;
                    fileSaveEBP(fd, os, FileLoader.lastPassword);
                } else {
                    FileLoader.lastPassword = null;
                    fileSaveEBF(fd, os);
                }
            } else {
                if (!filter.isFileType(f)) {
                    f = filter.addExtension(f);
                }
                os = new FileOutputStream(f, false);
                if (filter == FileType.TYPE_EBZ) {
                    FileLoader.lastPassword = null;
                    fileSaveEBZ(fd, os);
                } else if (filter == FileType.TYPE_EBP) {
                    if (saveAsMode) FileLoader.lastPassword = null;
                    fileSaveEBP(fd, os, FileLoader.lastPassword);
                } else {
                    FileLoader.lastPassword = null;
                    fileSaveEBF(fd, os);
                }
            }
        } catch (IOException ioe) {
            Log.logException(ioe);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ioe) {
                }
            }
        }
        return f;
    }

    /**
     * XML persistence
     * @return File written or null */
    public static boolean fileSaveEBZ(FileData fd, OutputStream f) {
        XMLEncoder xe = null;
        try {
            xe = new XMLEncoder(new BufferedOutputStream(f));
            xe.setExceptionListener(new MyExceptionListener());
            synchronized (fd) {
                xe.writeObject(fd);
            }
            return true;
        } finally {
            if (xe != null) {
                try {
                    xe.close();
                } catch (Exception e) {
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /** less portable than ebz, better when stay on same computer with same java
     * @return File written or null */
    public static boolean fileSaveEBF(FileData fd, OutputStream f) {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new BufferedOutputStream(f));
            synchronized (fd) {
                os.writeUTF(FileData.FILE_STAMP + new Date());
                os.writeInt(1);
                os.writeObject(fd);
            }
            return true;
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ie) {
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    /**
     * EBP: password protected binary file
     * @param password current password if already entered during the session (avoid to ask a second input)
     * @return File written or null if error(s) */
    public static boolean fileSaveEBP(FileData fd, OutputStream f, char[] password) {
        OutputStream out = null;
        try {
            int pcpChoice = JOptionPane.OK_OPTION;
            boolean needToAskPassword = password == null;
            while (needToAskPassword) {
                String hint = fd.getPasswordHint();
                PasswordChangePanel pwPanel = new PasswordChangePanel(fd.getPasswordHint());
                pcpChoice = JOptionPane.showConfirmDialog(null, pwPanel, "Password", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                char[] password1 = pwPanel.getPassword1();
                char[] password2 = pwPanel.getPassword2();
                boolean areSamePassword = (password1 != null) && Arrays.equals(password1, password2);
                needToAskPassword = (pcpChoice == JOptionPane.OK_OPTION && !areSamePassword);
                if (!areSamePassword) {
                    JOptionPane.showMessageDialog(null, i18n.getString("message_passwords_not_consistent"), i18n.getString("Error"), JOptionPane.ERROR_MESSAGE);
                }
                if (pcpChoice == JOptionPane.OK_OPTION && areSamePassword) {
                    hint = pwPanel.getPasswordHint();
                    password = pwPanel.getPassword1();
                    fd.setPasswordHint(hint);
                    FileLoader.lastPassword = password;
                }
            }
            if (pcpChoice == JOptionPane.CANCEL_OPTION) {
                return false;
            } else if (pcpChoice == JOptionPane.OK_OPTION) {
                assert password != null;
                PBEKeySpec pbeKeySpec;
                PBEParameterSpec pbeParamSpec;
                SecretKeyFactory keyFac;
                SecretKey pbeKey;
                Cipher pbeCipher = null;
                int count = 20;
                pbeParamSpec = new PBEParameterSpec(salt, count);
                pbeKeySpec = new PBEKeySpec(password);
                keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
                pbeKey = keyFac.generateSecret(pbeKeySpec);
                pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
                pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
                out = new BufferedOutputStream(f);
                ObjectOutputStream clearOut = new ObjectOutputStream(out);
                synchronized (fd) {
                    clearOut.writeUTF(FileData.FILE_STAMP + new Date());
                    clearOut.writeInt(1);
                    clearOut.writeUTF(fd.getPasswordHint());
                    clearOut.flush();
                }
                ObjectOutputStream cryptOut = new ObjectOutputStream(new CipherOutputStream(out, pbeCipher));
                synchronized (fd) {
                    cryptOut.writeObject(fd);
                    cryptOut.flush();
                }
                if (cryptOut != null) {
                    try {
                        cryptOut.close();
                    } catch (IOException ie) {
                    }
                }
                if (clearOut != null) {
                    try {
                        clearOut.close();
                    } catch (IOException ie) {
                    }
                }
            }
            return true;
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ie) {
                }
            }
            if (f != null) {
                try {
                    f.close();
                } catch (Exception e) {
                }
            }
        }
        return false;
    }

    private static class MyExceptionListener implements ExceptionListener {

        private static final int LOG_MAX = 10;

        private int counter = 0;

        public void exceptionThrown(Exception e) {
            counter++;
            if (counter < LOG_MAX) e.printStackTrace();
        }
    }
}
