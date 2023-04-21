package testifie.ui.locator;

import java.util.ArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import testifie.ui.common.Constants;
import testifie.ui.common.TestifIEException;
import testifie.ui.runtime.TestifIEProxy;
import com.tapsterrock.jiffie.IHTMLDocument2;
import com.tapsterrock.jiffie.IHTMLElement;
import com.tapsterrock.jiffie.JiffieException;

/**
 * @author slips
 */
public class TableCellLocator extends AbstractLocator {

    private static final Log log = LogFactory.getLog(TableCellLocator.class);

    public TableCellLocator() {
        super.setElementTag(Constants.Tag.TD);
    }

    public IHTMLElement locate() throws JiffieException, TestifIEException {
        IHTMLDocument2 doc = super.getExplorer().getDocument(true);
        ArrayList filteredElements = filter(doc.getElementListByTag(super.getElementTag()));
        if (filteredElements.size() != 1) {
            if (log.isInfoEnabled()) {
                String msg = "can't find element, num matches: " + filteredElements.size();
                TestifIEProxy.antLog(msg);
                log.info(msg);
            }
            throw new TestifIEException("can't find element, num matches: " + filteredElements.size());
        }
        doc.release();
        return (IHTMLElement) filteredElements.iterator().next();
    }
}
