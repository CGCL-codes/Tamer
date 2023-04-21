package testifie.ui.commands;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import testifie.ui.common.TestifIEException;
import testifie.ui.common.beans.Attribute;
import testifie.ui.runtime.TestifIEProxy;
import com.tapsterrock.jiffie.IHTMLAttributeCollection;
import com.tapsterrock.jiffie.IHTMLDOMAttribute;
import com.tapsterrock.jiffie.IHTMLElement;
import com.tapsterrock.jiffie.InternetExplorer;
import com.tapsterrock.jiffie.JiffieException;

/**
 * @author slips
 */
public abstract class AbstractCommand implements ICommand {

    private static final Log log = LogFactory.getLog(AbstractCommand.class);

    private InternetExplorer _explorer = null;

    private Hashtable _attrs = new Hashtable();

    private String _elementType = null;

    private String _comment = null;

    private boolean _initialized = false;

    private boolean _isReInitializing = false;

    private String _classname = null;

    private IHTMLElement _element = null;

    public void setExplorer(InternetExplorer explorer) {
        _explorer = explorer;
    }

    public InternetExplorer getExplorer() {
        return _explorer;
    }

    public void setElement(IHTMLElement element) {
        _element = element;
    }

    public IHTMLElement getElement() {
        return _element;
    }

    public void setClassname(String classname) {
        _classname = classname;
    }

    public void setElementType(String type) {
        _elementType = type;
    }

    public String getElementType() {
        return _elementType;
    }

    public void setComment(String comment) {
        _comment = comment;
    }

    public String getComment() {
        return _comment;
    }

    public void setInitialized(boolean initialized) {
        _initialized = initialized;
    }

    public boolean getInitialized() {
        return _initialized;
    }

    public void isReInitializing(boolean reInit) {
        _isReInitializing = reInit;
    }

    public boolean isReInitializing() {
        return _isReInitializing;
    }

    public void addAttribute(Attribute attr) {
        _attrs.put(attr.getName(), attr.getValue());
    }

    public String getAttributeValue(String attrName) {
        return (String) _attrs.get(attrName);
    }

    public Hashtable getAttributes() {
        return _attrs;
    }

    public void initialize() throws JiffieException, TestifIEException {
        if (!isReInitializing()) {
            String comment = getComment();
            if (null != comment && !"".equals(comment)) {
                if (log.isInfoEnabled()) {
                    log.info("");
                    log.info(">> " + comment);
                }
                TestifIEProxy.antLog("");
                TestifIEProxy.antLog(">> " + comment);
            }
            String logString = "\telement type: " + getElementType();
            if (log.isInfoEnabled()) {
                log.info(logString.toString());
                TestifIEProxy.antLog(logString.toString());
            }
            Enumeration attrKeys = _attrs.keys();
            String attrKey, attrVal;
            while (attrKeys.hasMoreElements()) {
                attrKey = (String) attrKeys.nextElement();
                attrVal = getAttributeValue(attrKey);
                logString = "\t" + attrKey + ": " + attrVal;
                if (log.isInfoEnabled()) {
                    log.info(logString.toString());
                    TestifIEProxy.antLog(logString.toString());
                }
            }
        }
    }

    public void execute() throws JiffieException, TestifIEException {
    }
}
