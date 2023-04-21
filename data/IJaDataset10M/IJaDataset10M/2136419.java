package org.xaware.designer.lint.validator.bizFile;

import java.util.Iterator;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.xaware.designer.lint.validator.BizFileValidator;
import org.xaware.designer.lint.validator.ResultList;
import org.xaware.designer.lint.validator.Util;
import org.xaware.shared.util.XAwareConstants;

/**
 * <p>
 * Title: CheckUnusedParams
 * </p>
 * 
 * <p>
 * Description: This class is a BizFile validator that looks at the defined parameter list, and reports if any of the
 * parameters are unused in this BizFile. This indicates parameters that can be safely removed from the interface
 * without a change to the component's behavior.
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author Kirstan Vandersluis
 * @version 1.0
 */
public class CheckUnusedParams implements BizFileValidator {

    public CheckUnusedParams() {
    }

    public int validate(final String filename, final Document bizFile, final ResultList messages, final String level, final String configData) throws JDOMException {
        final Element inputElem = bizFile.getRootElement().getChild("input", XAwareConstants.xaNamespace);
        if (inputElem == null) {
            return 0;
        }
        final List paramList = inputElem.getChildren("param", XAwareConstants.xaNamespace);
        final Iterator iter = paramList.iterator();
        while (iter.hasNext()) {
            final Element param = (Element) iter.next();
            final String sName = param.getAttributeValue("name", XAwareConstants.xaNamespace);
            if (!Util.findInContent(bizFile.getRootElement(), "%" + sName + "%")) {
                messages.addMessage(sName, "Input Parameter '" + sName + "' defined but not used", filename + "#" + Util.calculatePath(param), level, configData);
            }
        }
        return 0;
    }
}
