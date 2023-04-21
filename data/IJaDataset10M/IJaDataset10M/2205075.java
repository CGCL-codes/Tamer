package net.sf.mustang.service;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.dom4j.Document;

public interface Request {

    public String getHeader(String name);

    public Vector<String> getHeaders(String name);

    public Object getAttribute(String name);

    public void setAttribute(String name, Object value);

    public Object removeAttribute(String name);

    public Enumeration<String> getAttributeNames();

    public String getParameter(String name);

    public String getParameter(String name, String defVal);

    public Vector<String> getParameters(String name);

    public Vector<String> getParameters(String name, Vector<String> defVal);

    public void setParameter(String name, String value);

    public void setParameters(String name, Vector<String> value);

    public String removeParameter(String name);

    public String removeParameter(String name, String defVal);

    public Vector<String> removeParameters(String name);

    public Vector<String> removeParameters(String name, Vector<String> defVal);

    public Enumeration<String> getParameterNames();

    public UploadedFile getFile(String name);

    public Vector<UploadedFile> getFiles();

    public Document getDocument();

    public Session getSession();

    public String getClientIp();

    public String getRemoteUser();

    public String getClientPersistentData(String key);

    public Enumeration<String> getClientPersistentDataNames();

    public Hashtable<String, Vector<String>> getArguments();

    public void setArguments(Hashtable<String, Vector<String>> arguments);

    public Vector<String> getMessages(String argument);

    public void setException(Exception e);

    public Exception getException();
}
