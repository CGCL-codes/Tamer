package ashrafbasic;

import java.awt.Toolkit;
import java.util.*;
import java.io.*;
import java.net.URISyntaxException;
import javax.swing.*;

public class ConfigurationCore {

    ArrayList<Setting> SettingCollection;

    String FileName = "MidiMania.conf";

    public ConfigurationCore(String filename) {
        FileName = filename;
        SettingCollection = new ArrayList<Setting>();
        LoadFromFile();
    }

    private void LoadFromFile() {
        try {
            java.awt.Toolkit tk = Toolkit.getDefaultToolkit();
            ClassLoader cl = ConfigurationCore.class.getClassLoader();
            java.net.URL fileURL = cl.getResource(FileName);
            File f;
            if (fileURL == null) {
                f = new File(FileName);
                f.createNewFile();
            } else {
                try {
                    f = new File(fileURL.toURI());
                } catch (URISyntaxException e) {
                    f = new File(FileName);
                    f.createNewFile();
                }
            }
            FileReader fr;
            try {
                fr = new FileReader(f);
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(null, "FileNotFound on ConfigurationCore");
                return;
            }
            ArrayList<Character> TempC = new ArrayList<Character>();
            boolean endreached = false;
            while (true) {
                String SettingName = "";
                String SettingValue = "";
                while (true) {
                    int ti = fr.read();
                    if (ti == -1) {
                        endreached = true;
                        break;
                    }
                    Character tc = (Character) (char) ti;
                    if (tc == '=') {
                        SettingName = new String(Arrayt(Arraytc(TempC.toArray())));
                        TempC.clear();
                        continue;
                    }
                    if (tc == Character.LINE_SEPARATOR) {
                        SettingValue = new String(Arrayt(Arraytc(TempC.toArray())));
                        TempC.clear();
                        break;
                    }
                    TempC.add((Character) tc);
                }
                if (!endreached) {
                    addSetting(SettingName, SettingValue);
                } else {
                    break;
                }
            }
            fr.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "IOException on ConfigurationCore");
        }
    }

    public void Close() {
        try {
            File cf = new File(System.getProperty("user.dir") + "\\" + FileName);
            if (cf.exists()) {
                if (cf.delete()) {
                    cf.createNewFile();
                } else {
                    JOptionPane.showMessageDialog(null, "Unable to save Setting Unable to delete File");
                    return;
                }
            }
            FileWriter FW = new FileWriter(cf);
            int i = 0;
            while (i < SettingCollection.size()) {
                FW.write(SettingCollection.get(i).SettingName);
                FW.write("=");
                FW.write(SettingCollection.get(i).SettingValue);
                FW.write(Character.LINE_SEPARATOR);
                i++;
            }
            FW.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Unable to save Setting");
        }
    }

    private char[] Arrayt(Character[] ArrayCharacter) {
        char[] ac = new char[ArrayCharacter.length];
        int i = 0;
        while (i < ac.length) {
            ac[i] = ArrayCharacter[i];
            i++;
        }
        return ac;
    }

    private Character[] Arraytc(Object[] ArrayCharacter) {
        Character[] ac = new Character[ArrayCharacter.length];
        int i = 0;
        while (i < ac.length) {
            ac[i] = (Character) ArrayCharacter[i];
            i++;
        }
        return ac;
    }

    private String[] Arrayts(Object[] ArrayCharacter) {
        String[] ac = new String[ArrayCharacter.length];
        int i = 0;
        while (i < ac.length) {
            ac[i] = (String) ArrayCharacter[i];
            i++;
        }
        return ac;
    }

    public void addSetting(String settingName, String value) {
        if (AnyEqual(settingName, value)) {
            return;
        }
        Setting s = new Setting();
        s.SettingName = settingName;
        s.SettingValue = value;
        SettingCollection.add(s);
    }

    public int getNumberOfValue(String SettingName) {
        String[] s = getValue(SettingName);
        return s.length;
    }

    public String[] getValue(String SettingName) {
        ArrayList<String> TempString = new ArrayList<String>();
        int i = 0;
        while (i < SettingCollection.size()) {
            if (SettingCollection.get(i).SettingName.equals(SettingName)) {
                TempString.add(SettingCollection.get(i).SettingValue);
            }
            i++;
        }
        return Arrayts(TempString.toArray());
    }

    public boolean AnyEqual(String SettingName, String value) {
        int i = 0;
        while (i < SettingCollection.size()) {
            if (SettingCollection.get(i).SettingName.equals(SettingName) && SettingCollection.get(i).SettingValue.equals(value)) {
                return true;
            }
            i++;
        }
        return false;
    }

    public void removeSetting(String settingName) {
        int i = 0;
        while (i < SettingCollection.size()) {
            if (SettingCollection.get(i).SettingName.equals(settingName)) {
                SettingCollection.remove(i);
                SettingCollection.trimToSize();
                i = 0;
                continue;
            }
            i++;
        }
    }

    public void removeSetting(String settingName, String value) {
        int i = 0;
        while (i < SettingCollection.size()) {
            if (SettingCollection.get(i).SettingName.equals(settingName) && SettingCollection.get(i).SettingValue.equals(value)) {
                SettingCollection.remove(i);
                SettingCollection.trimToSize();
                i = 0;
                continue;
            }
            i++;
        }
    }

    private class Setting {

        String SettingName;

        String SettingValue;
    }
}
