package gui.models;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.sf.json.JSONObject;

public class PersistentWindowSizes extends Persistent {

    private File file;

    private String filePath;

    private ArrayList<String> keys;

    private ArrayList<String> values;

    public PersistentWindowSizes(String fileName) throws IOException {
        filePath = fileName;
        if (!filePath.endsWith(".json")) {
            filePath += ".json";
        }
        file = new File(filePath);
        keys = new ArrayList<String>();
        values = new ArrayList<String>();
        if (!file.exists()) {
            File parentFile = new File(file.getParent());
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        parse();
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONObject parse() throws IOException {
        BufferedReader br = null;
        String temp = null;
        String full = "";
        br = new BufferedReader(new FileReader(file));
        while ((temp = br.readLine()) != null) {
            full = full.concat(temp);
        }
        if (full.length() >= 0 && full.startsWith("{")) {
            JSONObject json = JSONObject.fromObject(full);
            Iterator<String> it = (Iterator<String>) json.keys();
            String key = null;
            while (it.hasNext()) {
                key = (String) it.next();
                keys.add(key);
                values.add((String) json.get(key));
            }
        }
        return null;
    }

    public Dimension getGraphSize() {
        int w = -1;
        int h = -1;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equalsIgnoreCase("GraphWidth")) {
                w = Integer.parseInt(values.get(i));
            } else if (keys.get(i).equalsIgnoreCase("GraphHeight")) {
                h = Integer.parseInt(values.get(i));
            }
        }
        if (w < 0 || h < 0) {
            return null;
        } else {
            return new Dimension(w, h);
        }
    }

    public void setGraphSize(Dimension d) throws IOException {
        boolean setW = false, setH = false;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equalsIgnoreCase("GraphWidth")) {
                values.set(i, Integer.toString(d.width));
                setW = true;
            } else if (keys.get(i).equalsIgnoreCase("GraphHeight")) {
                values.set(i, Integer.toString(d.height));
                setH = true;
            }
        }
        if (!setW) {
            keys.add("GraphWidth");
            values.add(Integer.toString(d.width));
        }
        if (!setH) {
            keys.add("GraphHeight");
            values.add(Integer.toString(d.height));
        }
        flushToConfig();
    }

    public Dimension getParameterSize() {
        int w = -1;
        int h = -1;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equalsIgnoreCase("ParameterWidth")) {
                w = Integer.parseInt(values.get(i));
            } else if (keys.get(i).equalsIgnoreCase("ParameterHeight")) {
                h = Integer.parseInt(values.get(i));
            }
        }
        if (w < 0 || h < 0) {
            return null;
        } else {
            return new Dimension(w, h);
        }
    }

    public Dimension getHistorySize() {
        int w = -1;
        int h = -1;
        for (int i = 0; i < keys.size(); i++) {
            if (keys.get(i).equalsIgnoreCase("HistoryWidth")) {
                w = Integer.parseInt(values.get(i));
            } else if (keys.get(i).equalsIgnoreCase("HistoryHeight")) {
                h = Integer.parseInt(values.get(i));
            }
        }
        if (w < 0 || h < 0) {
            return null;
        } else {
            return new Dimension(w, h);
        }
    }

    public void flushToConfig() throws IOException {
        JSONObject obj = new JSONObject();
        for (int i = 0; i < Math.min(keys.size(), values.size()); i++) {
            obj.put(keys.get(i), values.get(i));
        }
        writeOutJson(obj, filePath);
    }
}
