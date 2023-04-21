package org.exist.xslt;

import javax.xml.transform.Source;
import org.w3c.dom.Document;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class SourceImpl implements Source {

    public static final int NOT_DEFAINED = -1;

    public static final int STRING = 1;

    public static final int DOM = 2;

    public static final int EXIST_Sequence = 3;

    String systemId = null;

    Object source;

    int type = NOT_DEFAINED;

    public SourceImpl(Document source) {
        this.source = source;
        type = DOM;
    }

    public SourceImpl(org.exist.source.Source source) {
        this.source = source;
        type = EXIST_Sequence;
    }

    public SourceImpl(String source) {
        this.source = source;
        type = STRING;
    }

    public int getType() {
        return type;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public Object getSource() {
        return source;
    }

    public String toString() {
        if (type == STRING) {
            return (String) source;
        }
        return super.toString();
    }
}
