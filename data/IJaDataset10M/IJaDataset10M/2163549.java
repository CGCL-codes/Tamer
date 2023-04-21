package org.apache.harmony.x.print.ipp;

import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.apache.harmony.x.print.ipp.util.IppMimeType;

public class IppDocument {

    static int doccount;

    protected String docname;

    protected String format;

    protected Object document;

    protected IppAttributeGroupSet agroups;

    public IppDocument(String name, String mime, Object data) throws IppException {
        if (data != null && mime != null && (data instanceof InputStream || data instanceof byte[] || data instanceof char[] || data instanceof String || data instanceof Reader || data instanceof URL)) {
            this.document = data;
            this.format = new IppMimeType(mime).getIppSpecificForm();
            if (name == null || name.equals("")) {
                this.docname = new String((String) AccessController.doPrivileged(new PrivilegedAction() {

                    public Object run() {
                        return System.getProperty("user.name");
                    }
                }) + "-" + doccount);
            } else {
                this.docname = name;
            }
            this.agroups = new IppAttributeGroupSet();
        } else {
            throw new IppException("Wrong argument(s) for IPP document");
        }
    }

    public IppAttributeGroupSet getAgroups() {
        return agroups;
    }

    public void setAgroups(IppAttributeGroupSet attrgroups) {
        this.agroups = attrgroups;
    }

    public String getName() {
        return docname;
    }

    public void setName(String name) {
        this.docname = name;
    }

    public Object getDocument() {
        return document;
    }

    public void setDocument(Object data) throws IppException {
        if (data instanceof InputStream || data instanceof byte[] || data instanceof char[] || data instanceof String || data instanceof Reader || data instanceof URL) {
            this.document = data;
        } else {
            throw new IppException("Wrong type for IPP document");
        }
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String mime) {
        this.format = new IppMimeType(mime).getIppSpecificForm();
    }

    public boolean setAttribute(String aname, int avalue) {
        return setAttribute(aname, new Integer(avalue));
    }

    public boolean setAttribute(String aname, String avalue) {
        return setAttribute(aname, (Object) avalue);
    }

    public boolean setAttribute(String aname, Object avalue) {
        return agroups.setAttribute(aname, avalue);
    }
}
