package net.sf.doolin.gui.ext.template.support;

import net.sf.doolin.gui.ext.template.TemplateEngine;
import net.sf.doolin.gui.ext.template.velocity.VelocityTemplateEngine;

/**
 * Default template manager implementation.
 * 
 * By default, only the velocity engine is registered. Other can be added
 * declaratively by merging with the initial list:
 * 
 * <pre>
 *  &lt;bean
 *  		id=&quot;net.sf.doolin.gui.ext.template.TemplateManager&quot;
 *  		class=&quot;net.sf.doolin.gui.ext.template.support.DefaultTemplateManager&quot;&gt;
 *  		&lt;property name=&quot;engines&quot;&gt;
 *  			&lt;map merge=&quot;true&quot;&gt;
 *  				&lt;entry key=&quot;myEngineName&quot;&gt;
 *  					&lt;bean class=&quot;my.Engine&quot;/&gt;
 *  				&lt;/entry&gt;
 *  			&lt;/map&gt;
 *  		&lt;/property&gt;
 *  &lt;/bean&gt;
 * </pre>
 * 
 * @author Damien Coraboeuf
 * @version $Id: DefaultTemplateManager.java,v 1.3 2007/08/15 15:13:29 guinnessman Exp $
 */
public class DefaultTemplateManager extends AbstractTemplateManager {

    /**
	 * Default list of engines.
	 * 
	 * By default, only the velocity engine is registered.
	 */
    public DefaultTemplateManager() {
        registerEngine(TemplateEngine.VELOCITY, new VelocityTemplateEngine());
    }
}
