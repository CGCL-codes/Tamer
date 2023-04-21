package net.sf.jerkbot.plugins.svn;

import net.sf.jerkbot.bot.BotService;
import net.sf.jerkbot.plugins.jmx.services.JerkBotManagedBean;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.felix.scr.annotations.Service;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;

/**
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 *         JMX managed command to administer the SVN log poller
 * @version 0.0.1
 */
@Component(immediate = true)
@Service(value = JerkBotManagedBean.class)
public class SVNManagedBean implements JerkBotManagedBean {

    private static final Logger Log = LoggerFactory.getLogger(SVNManagedBean.class.getName());

    private final String ALIAS = "svn";

    private final String REF_NAME = "net.sf.jerkbot.plugins.svn:type=SVNPoller,name=SVNPoller";

    private Object svnMBean;

    @Reference(cardinality = ReferenceCardinality.MANDATORY_UNARY, bind = "bindBotService", unbind = "unbindBotService", referenceInterface = BotService.class)
    private BotService botService;

    protected void bindBotService(BotService botService) {
        this.botService = botService;
    }

    protected void unbindBotService(BotService botService) {
        this.botService = null;
    }

    protected void activate(ComponentContext context) {
        try {
            DAVRepositoryFactory.setup();
            SVNRepositoryFactoryImpl.setup();
            FSRepositoryFactory.setup();
            svnMBean = new SVNPoller(botService);
        } catch (Exception e) {
            Log.error("Error creating SVN Poller MBEAN", e);
        }
    }

    public Object getMBean() {
        return svnMBean;
    }

    public String getObjectName() {
        return REF_NAME;
    }

    public String getObjectNameNick() {
        return ALIAS;
    }
}
