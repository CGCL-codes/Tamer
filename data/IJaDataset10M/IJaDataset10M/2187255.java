package com.capsicum.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import com.capsicum.exceptions.InvalidParameterException;

public class VelocityLoader {

    private String mBaseDirectory = null;

    private VelocityEngine mEngine = null;

    private VelocityContext mContext = null;

    public VelocityLoader(String baseDir) {
        this.mBaseDirectory = baseDir;
        this.mEngine = new VelocityEngine();
        Properties vConfig = new Properties();
        vConfig.setProperty("file.resource.loader.path", baseDir);
        try {
            this.mEngine.init(vConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mContext = new VelocityContext();
        this.populateMandatoryValues();
    }

    private void populateMandatoryValues() {
        this.mContext.put("runtime", this);
    }

    public void loadScriptFile(String filePath) {
        try {
            StringWriter sw = new StringWriter();
            Template template = this.mEngine.getTemplate(filePath);
            template.merge(this.mContext, sw);
            sw.flush();
            System.out.println(sw.toString());
            sw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadScriptFile(String filePath, Map<String, Object> values) {
        this.populateValues(values);
        this.loadScriptFile(filePath);
    }

    public void log(String message, int level) {
    }

    private void populateValues(Map<String, Object> values) {
        if (values == null) {
            return;
        }
        Set<String> keySet = values.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            this.mContext.put(key, values.get(key));
        }
    }

    private String getFilePath(String path) {
        return this.mBaseDirectory + File.separatorChar + path;
    }
}
