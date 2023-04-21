package com.leclercb.commons.api.utils.properties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import com.leclercb.commons.api.properties.SortedProperties;

public class RemoveProperties {

    public static void removeProperty(File file, String keyStartsWith) throws Exception {
        SortedProperties p = new SortedProperties();
        p.load(new InputStreamReader(new FileInputStream(file), "UTF-8"));
        Set<Object> keys = new HashSet<Object>(p.keySet());
        for (Object key : keys) {
            if (!key.toString().startsWith(keyStartsWith)) continue;
            System.out.println("Property \"" + key + "\" removed from: " + file.getName());
            p.remove(key);
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        p.store(output, null);
        IOUtils.write(output.toString(), new FileOutputStream(file), "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        String keyStartsWith = args[1];
        if (!file.exists() || !file.isFile()) throw new IllegalArgumentException();
        final String bundle = file.getName().substring(0, (file.getName().indexOf('_') == -1 ? file.getName().indexOf('.') : file.getName().indexOf('_')));
        File[] files = file.getParentFile().listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.getName().startsWith(bundle);
            }
        });
        for (File f : files) {
            removeProperty(f, keyStartsWith);
        }
    }
}
