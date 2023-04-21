package net.sf.mogbox.os.linux;

import java.io.BufferedInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.mogbox.os.Registry;

public class LinuxRegistry extends Registry {

    private static final Pattern KEY_PATTERN = Pattern.compile("^\\[(.*)\\]$((?:\r?\n?^[^\\[\n].*[^\\]\n]$)*)", Pattern.MULTILINE);

    private static final Pattern VALUE_PATTERN = Pattern.compile("^(?:@|\"(.*)\")=\"((?:[^\\\\\n]|\\\\.)*)\"$", Pattern.MULTILINE);

    private static final Pattern ESCAPE_PATTERN = Pattern.compile("\\\\(.)", Pattern.MULTILINE);

    private static Logger log = Logger.getLogger(LinuxRegistry.class.getName());

    private static volatile Registry singleton = null;

    public static Registry getInstance() {
        if (singleton == null) {
            synchronized (LinuxRegistry.class) {
                if (singleton == null) singleton = new LinuxRegistry();
            }
        }
        return singleton;
    }

    private LinuxRegistry() {
    }

    @Override
    public String query(int hKey, String key, String value) {
        switch(hKey) {
            case Registry.HKCR:
                key = "HKEY_CLASSES_ROOT\\" + key;
                break;
            case Registry.HKCU:
                key = "HKEY_CURRENT_USER\\" + key;
                break;
            case Registry.HKLM:
                key = "HKEY_LOCAL_MACHINE\\" + key;
                break;
        }
        try {
            String[] cmd = { "wine", "regedit", "-e", "-", key };
            Process proc = Runtime.getRuntime().exec(cmd);
            Reader in = new InputStreamReader(new BufferedInputStream(proc.getInputStream()));
            StringBuilder output = new StringBuilder();
            int length;
            char[] buffer = new char[512];
            do {
                length = in.read(buffer);
                if (length > 0) output.append(buffer, 0, length);
            } while (length >= 0);
            if (proc.waitFor() != 0) return null;
            Matcher m = KEY_PATTERN.matcher(output);
            while (m.find()) {
                if (key.equals(m.group(1))) {
                    m = VALUE_PATTERN.matcher(m.group(2));
                    while (m.find()) {
                        String name = m.group(1);
                        if (name == null ^ value == null) continue;
                        if (name != null && !name.equals(value)) continue;
                        return ESCAPE_PATTERN.matcher(m.group(2)).replaceAll("$1");
                    }
                    break;
                }
            }
        } catch (Throwable t) {
            log.log(Level.SEVERE, null, t);
        }
        return null;
    }
}
