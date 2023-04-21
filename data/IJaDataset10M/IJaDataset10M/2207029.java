package org.pachyderm.authoring;

import org.pachyderm.foundation.PXComponentTemplate;
import org.pachyderm.foundation.PXTemplateRegistry;
import org.pachyderm.apollo.app.MCInspectPage;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

/**
 * @author jarcher
 *
 */
public class TemplateRegistryTester extends WOComponent {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2006367100233107516L;

    /**
	 * 
	 */
    public String identifier;

    /**
	 * @param context
	 */
    public TemplateRegistryTester(WOContext context) {
        super(context);
    }

    /**
	 * @return
	 */
    public PXTemplateRegistry templateRegistry() {
        return PXTemplateRegistry.sharedRegistry();
    }

    /**
	 * @return
	 */
    public WOComponent inspectTemplate() {
        PXComponentTemplate tmpl = templateRegistry().componentTemplateForIdentifier(identifier);
        MCInspectPage ip = (MCInspectPage) pageWithName("InspectComponentTemplatePage");
        ip.setObject(tmpl);
        return ip;
    }

    /**
	 * @return
	 */
    public int registeredComponentTemplateIdentifiers() {
        return templateRegistry().registeredComponentTemplateIdentifiers().count();
    }
}
