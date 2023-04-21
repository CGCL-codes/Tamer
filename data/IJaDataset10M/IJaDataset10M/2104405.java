package net.sourceforge.squirrel_sql.client.update.gui.installer;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class PreLaunchHelperFactoryImpl implements PreLaunchHelperFactory {

    /**
	 * @see net.sourceforge.squirrel_sql.client.update.gui.installer.PreLaunchHelperFactory#createPreLaunchHelper()
	 */
    public PreLaunchHelper createPreLaunchHelper() {
        String[] appCtx = new String[] { "classpath:net/sourceforge/squirrel_sql/fw/util/net.sourceforge.squirrel_sql.fw.util.applicationContext.xml", "classpath:net/sourceforge/squirrel_sql/client/update/gui/installer/net.sourceforge.squirrel_sql.client.update.gui.installer.applicationContext.xml", "classpath:net/sourceforge/squirrel_sql/client/update/gui/installer/event/net.sourceforge.squirrel_sql.client.update.gui.installer.event.applicationContext.xml", "classpath:net/sourceforge/squirrel_sql/client/update/gui/installer/util/net.sourceforge.squirrel_sql.client.update.gui.installer.util.applicationContext.xml", "classpath:net/sourceforge/squirrel_sql/client/update/util/net.sourceforge.squirrel_sql.client.update.util.applicationContext.xml" };
        ApplicationContext ctx = new ClassPathXmlApplicationContext(appCtx);
        return (PreLaunchHelper) ctx.getBean(PreLaunchHelper.class.getName());
    }
}
