package nps.compiler;

import java.io.*;
import nps.exception.NpsException;
import nps.core.*;

/**
 * ����ͨ��ģ��Ļ���
 * based object for common template
 * 
 * Copyright (c) 2007
 * @author jialin
 * @version 1.0
 */
public abstract class CommonClassBase implements IPublishable {

    protected NpsContext ctxt = null;

    private StringWriter writer = null;

    private File output = null;

    protected JavaWriter out = null;

    public CommonClassBase(NpsContext inCtxt) {
        ctxt = inCtxt;
        writer = new StringWriter();
        out = new JavaWriter(new PrintWriter(writer));
    }

    public CommonClassBase(NpsContext inCtxt, File output) throws FileNotFoundException, SecurityException, UnsupportedEncodingException {
        ctxt = inCtxt;
        this.output = output;
        out = new JavaWriter(new PrintWriter(new OutputStreamWriter(new FileOutputStream(output), "UTF-8")));
    }

    public String GetResult() {
        if (writer != null) return writer.getBuffer().toString();
        return null;
    }

    public void service() throws Exception {
        try {
            _service();
        } catch (Exception e) {
            nps.util.DefaultLog.error(e);
        } finally {
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }

    protected abstract void _service() throws Exception;

    public boolean HasField(String fieldName) {
        if (fieldName == null || fieldName.length() == 0) return false;
        String key = fieldName.trim();
        if (key.length() == 0) return false;
        key = key.toUpperCase();
        return ctxt.HasField(key);
    }

    private IPublishable GetPublishableObject(String fieldName) {
        if (fieldName == null || fieldName.length() == 0) return null;
        String key = fieldName.trim();
        if (key.length() == 0) return null;
        key = key.toUpperCase();
        if (ctxt.HasField(key)) return ctxt;
        return null;
    }

    public Object GetField(String fieldName) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName);
    }

    public String GetField(String fieldName, int wordcount) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName, wordcount);
    }

    public String GetField(String fieldName, String format) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName, format);
    }

    public String GetField(String fieldName, int width, int height) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName, width, height);
    }

    public String GetField(String fieldName, int wordcount, String append) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName, wordcount, append);
    }

    public String GetField(String fieldName, String format, int wordcount) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName, format, wordcount);
    }

    public String GetField(String fieldName, String format, int wordcount, String append) throws NpsException {
        IPublishable pub_obj = GetPublishableObject(fieldName);
        if (pub_obj == null) return null;
        return pub_obj.GetField(fieldName, format, wordcount, append);
    }

    public File GetOutputFile() {
        return output;
    }

    public String GetURL() {
        return null;
    }
}
