package org.armedbear.j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public final class Keywords {

    private final Mode mode;

    private final boolean ignoreCase;

    private HashSet hashSet;

    public Keywords(Mode mode) {
        this(mode, false);
    }

    public Keywords(Mode mode, boolean ignoreCase) {
        this.mode = mode;
        this.ignoreCase = ignoreCase;
        load();
    }

    public void reload() {
        load();
    }

    private void load() {
        ArrayList list = new ArrayList(256);
        InputStream inputStream = null;
        String className = mode.getClass().getName();
        int index = className.lastIndexOf('.');
        if (index >= 0) className = className.substring(index + 1);
        FastStringBuffer sb = new FastStringBuffer(className);
        sb.append('.');
        sb.append("keywords");
        final String key = sb.toString();
        final String fileName = Editor.preferences().getStringProperty(key);
        if (fileName != null) {
            File file = File.getInstance(fileName);
            if (file != null) {
                if (file.isFile()) {
                    try {
                        inputStream = file.getInputStream();
                        Log.debug("loading " + className + " keywords from " + file);
                    } catch (IOException e) {
                        Log.error(e);
                    }
                } else Log.error("file not found " + file);
            } else Log.error("file is null, fileName = |" + fileName + "|");
        }
        if (inputStream == null) inputStream = mode.getClass().getResourceAsStream(key);
        if (inputStream != null) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String s;
                while ((s = reader.readLine()) != null) {
                    s = s.trim();
                    if (s.length() > 0) {
                        if (ignoreCase) list.add(s.toLowerCase()); else list.add(s);
                    }
                }
            } catch (IOException e) {
                Log.error(e);
            }
        } else Log.error("no resource " + key);
        hashSet = new HashSet(list);
    }

    public boolean isKeyword(String s) {
        if (ignoreCase) return hashSet.contains(s.toLowerCase()); else return hashSet.contains(s);
    }
}
