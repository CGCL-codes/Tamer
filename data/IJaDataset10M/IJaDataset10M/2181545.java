package net.sf.lm4j.util;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import net.sf.lm4j.xml.LogDirectoryConfig;
import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;
import org.json.me.JSONStringer;

public class FileUtil {

    public static JSONObject dirsToJson(List list) {
        if (list == null) return null;
        JSONObject jsobj = new JSONObject();
        JSONArray jsonarray = new JSONArray();
        for (Iterator iterator = list.iterator(); iterator.hasNext(); ) {
            LogDirectoryConfig config = (LogDirectoryConfig) iterator.next();
            File file = new File(config.getPath());
            jsonarray.put(dirToJson(file, config));
        }
        try {
            jsobj.put("dirs", jsonarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsobj;
    }

    /**
	 * get a json structure like this
	 * [{"name":"","type":"dir","files":[{id:"",name:"",type:"file"},...]},...]
	 * 
	 * @param file
	 * @return
	 */
    public static JSONObject dirToJson(File file, LogDirectoryConfig config) {
        if (file.isFile() && file.getName().lastIndexOf('.') != -1 && config.getExts().contains(file.getName().substring(file.getName().lastIndexOf('.') + 1))) {
            JSONObject fileobj = new JSONObject();
            try {
                fileobj.put("id", file.getAbsolutePath());
                fileobj.put("name", file.getName());
                fileobj.put("type", "file");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return fileobj;
        } else if (file.isDirectory()) {
            JSONObject dirObj = new JSONObject();
            try {
                dirObj.put("name", file.getName());
                dirObj.put("type", "dir");
                Vector v = new Vector();
                File[] subFiles = file.listFiles();
                for (int i = 0; i < subFiles.length; i++) {
                    JSONObject obj = dirToJson(subFiles[i], config);
                    if (obj != null) v.add(obj);
                }
                dirObj.put("files", v);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dirObj;
        } else return null;
    }
}
