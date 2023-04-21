package testifie.ui.commands.input;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import testifie.ui.common.TestifIEException;
import testifie.ui.locator.ElementLocatorFactory;
import testifie.ui.runtime.TestifIEProxy;
import testifie.ui.runtime.Utilities;
import com.tapsterrock.jiffie.IHTMLDocument2;
import com.tapsterrock.jiffie.IHTMLElement;
import com.tapsterrock.jiffie.IHTMLInputElement;
import com.tapsterrock.jiffie.IHTMLTextAreaElement;
import com.tapsterrock.jiffie.JiffieException;
import com.tapsterrock.jiffie.JiffieUtility;

/**
 * @author slips
 */
public class TypeTextCommand extends AbstractInputCommand {

    private static final Log log = LogFactory.getLog(ClickCommand.class);

    private String _valueToEnter = "";

    public void setEnterValue(String value) {
        if (!getInitialized()) {
            _valueToEnter = value;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Can't set Enter Value after initialization.");
            }
        }
    }

    public String getEnterValue() {
        return _valueToEnter;
    }

    public void initialize() throws JiffieException, TestifIEException {
        if (!getInitialized()) {
            super.initialize();
            IHTMLElement element = null;
            element = ElementLocatorFactory.createLocator(getExplorer(), getElementType(), getAttributes()).locate();
            if (!(element instanceof IHTMLInputElement) && !(element instanceof IHTMLTextAreaElement)) {
                if (log.isInfoEnabled()) {
                    String msg = "TypeTextCommand::initialize: incorrect type of element: " + element.getClassName();
                    TestifIEProxy.antLog(msg);
                    log.info(msg);
                }
                throw new TestifIEException("incorrect type of element: " + element.getClassName());
            }
            setElement(element);
            super.setInitialized(true);
        }
    }

    public void execute() throws JiffieException, TestifIEException {
        super.execute();
        if (log.isInfoEnabled()) {
            String msg = "enter value: " + _valueToEnter;
            TestifIEProxy.antLog(msg);
            log.info(msg);
        }
        String fieldValue = "";
        IHTMLElement element = getElement();
        if (element instanceof IHTMLInputElement) {
            fieldValue = ((IHTMLInputElement) getElement()).getValue();
        } else if (getElement() instanceof IHTMLTextAreaElement) {
            fieldValue = ((IHTMLTextAreaElement) getElement()).getValue();
        }
        getElement().call("focus");
        IHTMLDocument2 doc = getExplorer().getDocument(true);
        if (!"".equals(fieldValue)) {
            JiffieUtility.sendKeys(doc.getTitle(), "{HOME}", doc);
            Utilities.doSleep();
            JiffieUtility.sendKeys(doc.getTitle(), "+{END}", doc);
            Utilities.doSleep();
            JiffieUtility.sendKeys(doc.getTitle(), "{DELETE}", doc);
            Utilities.doSleep();
        }
        JiffieUtility.sendKeys(doc.getTitle(), getEnterValue(), doc);
        Utilities.doSleep();
        getElement().call("blur");
        fireEvents(getElement());
        doc.release();
        getElement().release();
    }
}
