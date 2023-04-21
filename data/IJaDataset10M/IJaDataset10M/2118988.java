package jaxlib.ee.jms.serial;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author  jw
 * @since   JaXLib 1.0
 * @version $Id: XmlStackTraceElement.java 2618 2008-06-16 13:13:20Z joerg_wassmer $
 */
@XmlType(name = "StackTraceElement", namespace = SerialMessage.XMLNS, propOrder = { "isNative", "file", "line", "method", "classname" })
@XmlAccessorType(XmlAccessType.FIELD)
final class XmlStackTraceElement extends Object implements Serializable {

    /**
   * @since JaXLib 1.0
   */
    private static final long serialVersionUID = 1L;

    @XmlAttribute(name = "class")
    private String classname;

    @XmlAttribute
    private String file;

    @XmlAttribute(name = "native")
    private Boolean isNative;

    @XmlAttribute
    private int line;

    @XmlAttribute
    private String method;

    /**
   * Just for JAXB.
   */
    private XmlStackTraceElement() {
        super();
    }

    XmlStackTraceElement(final StackTraceElement src) {
        super();
        this.classname = src.getClassName();
        this.file = src.getFileName();
        this.isNative = src.isNativeMethod() ? Boolean.TRUE : null;
        this.line = src.getLineNumber();
        this.method = src.getMethodName();
        if ((this.line <= 0) && Boolean.TRUE.equals(this.isNative)) this.line = -1;
    }

    final StackTraceElement unmarshal() {
        int line = this.line;
        if ((line <= 0) && Boolean.TRUE.equals(this.isNative)) line = -2;
        return new StackTraceElement(this.classname, this.method, this.file, line);
    }
}
