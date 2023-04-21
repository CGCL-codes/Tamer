package org.monet.manager.presentation.user.agents;

import java.io.Writer;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.monet.kernel.agents.AgentLogger;
import org.monet.kernel.constants.Strings;
import org.monet.kernel.exceptions.SystemException;
import org.monet.manager.configuration.Configuration;
import org.monet.manager.core.constants.ErrorCode;
import org.monet.manager.core.constants.SiteDirectories;
import org.monet.manager.presentation.user.util.Context;

public class AgentTemplates {

    private VelocityEngine velocityEngine;

    private Configuration oConfiguration;

    private static AgentTemplates oInstance;

    private AgentTemplates() {
        try {
            this.velocityEngine = new VelocityEngine();
            this.oConfiguration = Configuration.getInstance();
            this.init();
        } catch (Exception oException) {
            throw new SystemException(ErrorCode.AGENT_TEMPLATES, null, oException);
        }
    }

    private Boolean init() throws Exception {
        this.velocityEngine.addProperty(Velocity.RESOURCE_LOADER, "classpath");
        this.velocityEngine.addProperty("classpath." + Velocity.RESOURCE_LOADER + ".class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        this.velocityEngine.addProperty(Velocity.RUNTIME_LOG, this.oConfiguration.getTempDir() + Strings.BAR45 + SiteDirectories.LOGS + "/velocity.log");
        this.velocityEngine.addProperty(Velocity.INPUT_ENCODING, "UTF-8");
        this.velocityEngine.addProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
        this.velocityEngine.init();
        return true;
    }

    public static synchronized AgentTemplates getInstance() {
        if (oInstance == null) oInstance = new AgentTemplates();
        return oInstance;
    }

    public void merge(String sFilename, Context oContext, Writer writer) {
        VelocityContext oVelocityContext;
        Template oTemplate;
        oVelocityContext = new VelocityContext(oContext.get());
        try {
            oTemplate = this.velocityEngine.getTemplate(sFilename);
            oTemplate.merge(oVelocityContext, writer);
        } catch (Exception ex) {
            AgentLogger.getInstance().error(ex);
        }
    }
}
