package org.km.xplane.fixes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class FixData {

    private HashMap<String, Fix> _fixes = new HashMap<String, Fix>();

    private String _header = "";

    public FixData(File file) throws IOException {
        new XPFixParser().parse(file, this, false);
    }

    public void write(File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(_header);
        Iterator<String> it = getSortedKeys();
        while (it.hasNext()) writer.write(_fixes.get(it.next()).toString());
        writer.write("99\r\n");
        writer.close();
    }

    private Iterator<String> getSortedKeys() {
        ArrayList<String> keys = new ArrayList<String>(_fixes.keySet());
        Collections.sort(keys);
        return keys.iterator();
    }

    public void cleanUp() throws IOException {
        Iterator<Fix> it = _fixes.values().iterator();
        while (it.hasNext()) it.next().cleanUp();
    }

    public void importData(File sourceImp) throws IOException {
        new VasFixParser().parse(sourceImp, this, true);
    }

    public void addHeaderLine(String line) {
        _header += line + "\r\n";
    }

    public void addFixPosition(String name, FixPosition pos) throws IOException {
        Fix fix = _fixes.get(name);
        if (fix == null) {
            fix = new Fix(name, pos);
            _fixes.put(fix.getName(), fix);
        } else {
            fix.addPosition(pos);
        }
    }
}
