package org.chon.cms.template;

import org.chon.cms.core.JCRApplication;
import org.chon.cms.core.ResTplConfiguredActivator;
import org.chon.cms.template.internal.ext.template.TemplateExtension;

public class Activator extends ResTplConfiguredActivator {

    @Override
    protected String getName() {
        return "org.chon.cms.template";
    }

    @Override
    protected void registerExtensions(JCRApplication app) {
        app.regExtension("template", new TemplateExtension(app));
    }
}
