package testifie.ui.locator;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import testifie.ui.common.Constants;
import testifie.ui.common.TestifIEException;
import testifie.ui.runtime.TestifIEProxy;
import com.tapsterrock.jiffie.IHTMLDocument2;
import com.tapsterrock.jiffie.IHTMLElement;
import com.tapsterrock.jiffie.IHTMLSelectElement;
import com.tapsterrock.jiffie.JiffieException;

/**
 * @author slips
 */
public class SelectBoxLocator extends AbstractLocator {

    private static final Log log = LogFactory.getLog(SelectBoxLocator.class);

    public SelectBoxLocator() {
        super.setElementTag(Constants.Tag.SELECT);
    }

    public IHTMLElement locate() throws JiffieException, TestifIEException {
        IHTMLDocument2 doc = super.getExplorer().getDocument(true);
        ArrayList filteredElements = filter(doc.getElementListByTag(super.getElementTag()));
        if (filteredElements.size() != 1) {
            throw new TestifIEException("can't find element, num matches: " + filteredElements.size());
        }
        doc.release();
        return (IHTMLSelectElement) filteredElements.iterator().next();
    }
}
