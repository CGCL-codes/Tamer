package testifie.ui.locator;

import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.tapsterrock.jiffie.IHTMLElement;
import com.tapsterrock.jiffie.InternetExplorer;
import testifie.ui.common.Constants;
import testifie.ui.common.DispIdTranslator;
import testifie.ui.common.TestifIEException;

/**
 * @author slips
 */
public abstract class AbstractLocator implements ILocator {

    private static final Log log = LogFactory.getLog(AbstractLocator.class);

    private InternetExplorer _explorer = null;

    private String _elementTag = null;

    private String _elementType = null;

    private Hashtable _filterAttrs = null;

    public void setExplorer(InternetExplorer explorer) {
        _explorer = explorer;
    }

    protected InternetExplorer getExplorer() {
        return _explorer;
    }

    public void setElementTag(String tag) {
        _elementTag = tag;
    }

    public String getElementTag() {
        return _elementTag;
    }

    public void setElementType(String type) {
        _elementType = type;
    }

    public String getElementType() {
        return _elementType;
    }

    public void setFilterAttributes(Hashtable attrs) {
        _filterAttrs = attrs;
    }

    public Hashtable getFilterAttributes() {
        return _filterAttrs;
    }

    protected ArrayList filter(List unfilteredElements) throws TestifIEException {
        ArrayList filteredCandidates = null;
        ArrayList candidates = new ArrayList();
        candidates.addAll(unfilteredElements);
        IHTMLElement element = null;
        if (!"".equals(getElementType()) && null != getElementType()) {
            if (log.isDebugEnabled()) {
                log.debug("\tAbstractLocator::filter for type " + getElementType() + ", size candidates: " + candidates.size());
            }
            filteredCandidates = new ArrayList();
            for (Iterator it = candidates.iterator(); it.hasNext(); ) {
                element = (IHTMLElement) it.next();
                if (getElementType().equals(element.getStringProperty(Constants.TYPE))) {
                    filteredCandidates.add(element);
                }
            }
            candidates = filteredCandidates;
        }
        String instcountstr = (String) _filterAttrs.get(Constants.INSTANCECOUNT);
        int instcount = -1;
        if (instcountstr != null) {
            instcount = Integer.parseInt(instcountstr);
            if (log.isDebugEnabled()) {
                log.debug("Will use element number " + instcount + " in case of multiple matches.");
            }
        }
        Enumeration filterKeys = _filterAttrs.keys();
        String filterName = null;
        String filterValue = null;
        String attrValue = null;
        while (filterKeys.hasMoreElements()) {
            filterName = (String) filterKeys.nextElement();
            if (filterName.equals(Constants.INSTANCECOUNT)) {
                continue;
            }
            filterValue = (String) _filterAttrs.get(filterName);
            if (log.isDebugEnabled()) {
                log.debug("\tAbstractLocator::filter for attribute <" + filterName + ">, attribute value: <" + filterValue + ">,  size candidates: " + candidates.size());
            }
            filteredCandidates = new ArrayList();
            for (Iterator it = candidates.iterator(); it.hasNext(); ) {
                element = (IHTMLElement) it.next();
                attrValue = element.getStringProperty(DispIdTranslator.translateAttributeName(filterName));
                if (log.isDebugEnabled()) {
                    log.debug("\t\tevaluating attribute value: <" + attrValue + ">");
                }
                if (filterValue.equals(attrValue)) {
                    filteredCandidates.add(element);
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("\tFinished filter <" + filterName + ">, size filteredCandidates: " + filteredCandidates.size());
            }
            candidates = filteredCandidates;
        }
        if ((candidates.size() > 1) && (instcount > -1)) {
            if (log.isDebugEnabled()) {
                log.debug("Found " + candidates.size() + " matches. Using element " + instcount);
            }
            if (instcount < candidates.size()) {
                ArrayList nthelement = new ArrayList();
                nthelement.add(candidates.get(instcount));
                candidates = nthelement;
            } else {
                throw new TestifIEException("instancecount value " + instcount + " was more than the number of matches " + candidates.size());
            }
        }
        return candidates;
    }
}
