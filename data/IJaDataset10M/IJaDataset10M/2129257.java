package net.sf.eclipse.portlet.ui.internal.sse.contentassist;

import java.util.List;
import java.util.Vector;
import net.sf.eclipse.portlet.core.Constants;
import net.sf.eclipse.portlet.core.model.descriptor.CacheScope;
import net.sf.eclipse.portlet.ui.internal.Messages;
import org.eclipse.jface.text.IDocument;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;

/**
 * Implement autosuggestion for the cache scope enum.
 * 
 * @author fwjwiegerinck
 * @since 0.2
 */
public class CacheScopeContentAssistProcessor extends AbstractGenericXmlContentAssistProcessor<CacheScope> {

    /**
	 * Initialize based upon cache-scope element
	 */
    public CacheScopeContentAssistProcessor() {
        super(Constants.XML_DESCRIPTOR_ELEMENT_PORTLET_CACHESCOPE);
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#computeElementsProposal(org.eclipse.jface.text.IDocument, java.lang.String)
	 */
    @Override
    protected List<CacheScope> computeElementsProposal(IDocument document) {
        List<CacheScope> returnValue = new Vector<CacheScope>();
        for (CacheScope cacheScope : CacheScope.values()) {
            returnValue.add(cacheScope);
        }
        return returnValue;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getAdditionalInformation(java.lang.Object)
	 */
    @Override
    protected String getAdditionalInformation(CacheScope element) {
        return NLS.bind(Messages.sse_contentassist_cachescope_additionalInformation, element.toString());
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getDisplayString(java.lang.Object)
	 */
    @Override
    protected String getDisplayString(CacheScope element) {
        return NLS.bind(Messages.sse_contentassist_cachescope_displayString, element.toString());
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getImage(java.lang.Object)
	 */
    @Override
    protected Image getImage(CacheScope element) {
        return null;
    }

    /**
	 * @see net.sf.eclipse.portlet.ui.internal.sse.contentassist.AbstractGenericXmlContentAssistProcessor#getReplacement(java.lang.Object)
	 */
    @Override
    protected String getReplacement(CacheScope element) {
        return element.name();
    }
}
