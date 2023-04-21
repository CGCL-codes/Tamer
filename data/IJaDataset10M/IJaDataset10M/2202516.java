package org.codehaus.groovy.grails.plugins.quartz;

/**
 * <p>Holds properties names from <code>GrailsTaskClass</code></p> 
 * 
 * @see GrailsTaskClass
 * @author Micha?? K??ujszo
 * @author Graeme Rocher
 * @author Marcel Overdijk
 * 
 * @since 0.2
 */
public interface GrailsTaskClassProperty {

    public static final String EXECUTE = "execute";

    public static final String TIMEOUT = "timeout";

    public static final String START_DELAY = "startDelay";

    public static final String CRON_EXPRESSION = "cronExpression";

    public static final String GROUP = "group";

    public static final String CONCURRENT = "concurrent";

    public static final String SESSION_REQUIRED = "sessionRequired";
}
