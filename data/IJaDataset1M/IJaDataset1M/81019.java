package org.apache.jetspeed.portal.controls;

import org.apache.turbine.services.velocity.TurbineVelocity;
import org.apache.turbine.services.pull.TurbinePull;
import org.apache.turbine.util.RunData;
import org.apache.jetspeed.portal.Portlet;
import org.apache.jetspeed.portal.PortletState;
import org.apache.jetspeed.portal.security.portlets.PortletWrapper;
import org.apache.jetspeed.services.TemplateLocator;
import org.apache.jetspeed.services.logging.JetspeedLogFactoryService;
import org.apache.jetspeed.services.logging.JetspeedLogger;
import org.apache.jetspeed.services.persistence.PersistenceManager;
import org.apache.jetspeed.services.resources.JetspeedResources;
import org.apache.jetspeed.services.rundata.JetspeedRunData;
import org.apache.jetspeed.util.template.JetspeedTool;
import org.apache.jetspeed.util.template.JetspeedLink;
import org.apache.jetspeed.util.template.JetspeedLinkFactory;
import org.apache.jetspeed.services.JetspeedSecurity;
import org.apache.jetspeed.om.security.JetspeedUser;
import org.apache.ecs.ConcreteElement;
import org.apache.ecs.StringElement;
import org.apache.velocity.context.Context;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;

/**
 * A Velocity based portlet control which implements all PortletState action
 *
 * <p>To use this control you need to define in your registry the following
 * entry or similar:</p>
 * <pre>
 *   <portlet-control-entry name="TitlePortletControl">
 *     <classname>org.apache.jetspeed.portal.controls.VelocityPortletControl</classname>
 *     <parameter name="theme" value="default.vm"/>
 *     <meta-info>
 *       <title>TitleControl</title>
 *       <description>The standard Jetspeed boxed control</description>
 *       <image>url of image (icon)</description>
 *     </meta-info>
 *     <media-type ref="html"/>
 *   </portlet-control-entry>
 * </pre>
 *
 * 
 * @author <a href="mailto:re_carrasco@bco011.sonda.cl">Roberto Carrasco</a>
 * @author <a href="mailto:raphael@apache.org">Rapha�l Luta</a>
 * @author <a href="mailto:morciuch@apache.org">Mark Orciuch</a> 
 *
 * @version $Id: VelocityPortletControl.java,v 1.30 2004/03/29 21:38:42 taylor Exp $
 *
 */
public class VelocityPortletControl extends AbstractPortletControl {

    /**
     * Static initialization of the logger for this class
     */
    private static final JetspeedLogger logger = JetspeedLogFactoryService.getLogger(VelocityPortletControl.class.getName());

    /** Disable content caching */
    public boolean isCacheable() {
        return false;
    }

    /**
     * Handles the content generation for this control using Velocity
     */
    public ConcreteElement getContent(RunData rundata) {
        Portlet portlet = getPortlet();
        JetspeedRunData jdata = (JetspeedRunData) rundata;
        if (portlet instanceof PortletWrapper) {
            PortletWrapper wrapper = (PortletWrapper) portlet;
            if (!wrapper.getAllowView(rundata)) {
                if (JetspeedResources.getBoolean("defaultportletcontrol.hide.decorator", true)) {
                    return new StringElement("");
                }
            }
        }
        Context context = TurbineVelocity.getContext();
        context.put("data", rundata);
        context.put("actions", buildActionList(rundata, portlet));
        context.put("conf", getConfig());
        context.put("skin", portlet.getPortletConfig().getPortletSkin());
        TurbinePull.populateContext(context, rundata);
        if (portlet.getName().equals(jdata.getCustomized()) && (!portlet.providesCustomization())) {
            context.put("portlet", JetspeedTool.getCustomizer(portlet));
            context.put("portlet_instance", JetspeedTool.getCustomizer(portlet));
        } else {
            context.put("portlet", portlet);
            if (PersistenceManager.getInstance(portlet, jdata) == null) {
                context.put("portlet_instance", portlet);
            } else {
                context.put("portlet_instance", PersistenceManager.getInstance(portlet, jdata));
            }
        }
        buildContext(rundata, context);
        String theme = getConfig().getInitParameter("theme", "default.vm");
        String s = "";
        try {
            String template = TemplateLocator.locateControlTemplate(rundata, theme);
            TurbineVelocity.handleRequest(context, template, rundata.getOut());
        } catch (Exception e) {
            logger.error("Exception while creating content ", e);
            s = e.toString();
        }
        TurbineVelocity.requestFinished(context);
        return new StringElement(s);
    }

    /**
     * This method allows subclasses of the VelocityPortletControl
     * to populate the context of this control before rendering by
     * the template engine.
     *
     * @param rundata the RunData object for this request
     * @param context the Context used by the template
     */
    public void buildContext(RunData rundata, Context context) {
    }

    /** Builds a list of possible window actions for this portlet
     *  instance. For best results, the portlet should also implement the
     *  PortletState interface.
     *
     * @param rundata the request RunData
     * @param the portlet instance managed by this control
     * @return a list of ordered PortletAction objects describing the
     * the actions available for this portlet
     */
    protected List buildActionList(RunData rundata, Portlet portlet) {
        List actions = new Vector();
        JetspeedLink jsLink = null;
        JetspeedRunData jdata = (JetspeedRunData) rundata;
        if (JetspeedSecurity.areActionsDisabledForAllUsers()) {
            return actions;
        }
        JetspeedUser user = jdata.getJetspeedUser();
        if (JetspeedSecurity.areActionsDisabledForAnon() && false == user.hasLoggedIn()) {
            return actions;
        }
        if (portlet instanceof PortletState) {
            PortletState state = (PortletState) portlet;
            boolean customized = (jdata.getMode() == JetspeedRunData.CUSTOMIZE);
            boolean maximized = customized || (jdata.getMode() == JetspeedRunData.MAXIMIZE);
            boolean infoAdded = false;
            if (state.allowCustomize(rundata)) {
                if (!customized) {
                    actions.add(new PortletAction("customize", "Customize"));
                }
            } else {
                if (state.allowInfo(rundata)) {
                    actions.add(new PortletAction("info", "Information"));
                    infoAdded = true;
                }
            }
            if ((!customized) && state.allowPrintFriendly(rundata)) {
                actions.add(new PortletAction("print", "Print Friendly Format"));
            }
            if ((!customized) && state.allowInfo(rundata) && (!infoAdded)) {
                actions.add(new PortletAction("info", "Information"));
            }
            if ((!customized) && (!maximized) && state.allowClose(rundata)) {
                actions.add(new PortletAction("close", "Close"));
            }
            if (state.isMinimized(rundata) || maximized) {
                actions.add(new PortletAction("restore", "Restore"));
            } else {
                if (state.allowMinimize(rundata)) {
                    actions.add(new PortletAction("minimize", "Minimize"));
                }
                if (state.allowMaximize(rundata)) {
                    actions.add(new PortletAction("maximize", "Maximize"));
                }
            }
        } else {
            if (portlet.getAllowEdit(rundata)) {
                actions.add(new PortletAction("info", "Information"));
            }
            if (portlet.getAllowMaximize(rundata)) {
                actions.add(new PortletAction("maximize", "Maximize"));
            }
        }
        Iterator i = actions.iterator();
        while (i.hasNext()) {
            PortletAction action = (PortletAction) i.next();
            try {
                jsLink = JetspeedLinkFactory.getInstance(rundata);
            } catch (Exception e) {
                logger.error("Exception in buildActionList", e);
            }
            action.setLink(jsLink.setAction(getAction(action.getName()), portlet).toString());
            JetspeedLinkFactory.putInstance(jsLink);
            jsLink = null;
        }
        return actions;
    }

    /** Transforms an Action name in Turbine valid action name, by
     *  adding a controls package prefix and capitalizing the first
     *  letter of the name.
     */
    protected static String getAction(String name) {
        StringBuffer buffer = new StringBuffer("controls.");
        buffer.append(name.substring(0, 1).toUpperCase());
        buffer.append(name.substring(1, name.length()));
        return buffer.toString();
    }

    /** This utility class is used to give information about the actions 
     *  available in a control theme template
     */
    public class PortletAction {

        String name = null;

        String link = null;

        String alt = null;

        /**
         * Constructor
         * 
         * @param name   Name of the action
         * @param alt    Alternative text description (localized)
         */
        protected PortletAction(String name, String alt) {
            this.name = name;
            this.alt = alt;
        }

        public String getName() {
            return this.name;
        }

        public String getLink() {
            return this.link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getAlt() {
            return this.alt;
        }
    }
}
