package com.microfly.js;

import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Context;
import org.apache.commons.httpclient.NameValuePair;
import com.microfly.job.html.HtmlFetcher;
import com.microfly.job.html.ImageHelper;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.ArrayList;

/**
 * NpsHtmlFetcher
 *    ����Զ��HTML�ļ����Զ����ء��ֶη�����ͼƬ��������ļ�����
 *
 * Description: a new publishing system
 * Copyright (c) 2007
 * @author jialin
 * @version 1.0
 */
public class NpsHtmlFetcher extends ScriptableObject {

    private HtmlFetcher fetcher = new HtmlFetcher();

    private List<NameValuePair> fields = new ArrayList();

    public NpsHtmlFetcher() {
    }

    public String getClassName() {
        return "NpsHtmlFetcher";
    }

    public void jsFunction_ClearFields() {
        fields.clear();
    }

    public void jsFunction_AddNameValuePair(String name, String value) {
        fields.add(new NameValuePair(name, value));
    }

    public void jsFunction_Login(String url) throws IOException {
        fetcher.Login(url, fields.toArray(new NameValuePair[fields.size()]));
    }

    public NpsHtmlParser jsFunction_GetHtmlParser(String url, String encoding) throws Exception {
        Reader r = fetcher.GetHtml(url);
        NpsHtmlParser parser = null;
        try {
            parser = new NpsHtmlParser(r, encoding);
        } finally {
            try {
                r.close();
            } catch (Exception e) {
            }
        }
        Object[] args = new Object[] { url, encoding };
        Context cx = Context.getCurrentContext();
        Scriptable scope = ScriptableObject.getTopLevelScope(this);
        Scriptable obj = cx.newObject(scope, "NpsHtmlParser", args);
        parser.setPrototype(obj.getPrototype());
        return parser;
    }

    public void jsFunction_SaveHtml2File(String url, NpsFile output) throws IOException {
        fetcher.GetHtml(url, output.GetFile());
    }

    public void jsFunction_GetBinary(String url, NpsFile output) throws IOException {
        fetcher.GetBinary(url, output.GetFile());
    }

    public void jsFunction_ScaleTo(NpsFile src, NpsFile dest, int width, int height) throws Exception {
        ImageHelper imagehelper_m = new ImageHelper(src.GetFile());
        imagehelper_m.ScaleTo(dest.GetFile(), width, height);
    }
}
