package net.sf.anima;

import java.io.File;
import net.sf.anima.gui.AnimaGui;
import net.sf.anima.persistence.AnimaPersistenceFacade;
import net.sf.anima.persistence.impl.AnimaJaxb;
import net.sf.anima.xmpp.XmppProxy;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;
import de.kisner.util.LoggerInit;

public class Anima {

    static Logger logger = Logger.getLogger(Anima.class);

    public static String homedir;

    AnimaPersistenceFacade apf;

    public Anima() {
    }

    public void start() {
        String version = this.getClass().getPackage().getImplementationVersion();
        if (version != null) {
            logger.info("This is Anima (Version " + version + ")");
        }
        apf = new AnimaJaxb();
        XmppProxy xmppProxy = new XmppProxy(apf);
        xmppProxy.start();
        new AnimaGui(xmppProxy);
    }

    public void checkAnimaDir() {
        File animaHome = new File(SystemUtils.getUserHome() + SystemUtils.FILE_SEPARATOR + ".anima");
        if (!animaHome.exists()) {
            animaHome.mkdir();
            logger.info("Anima-Directory created: " + animaHome.getAbsolutePath());
        }
        homedir = animaHome.getAbsolutePath();
        logger.debug(SystemUtils.getUserHome());
    }

    public static final void main(String[] args) throws Exception {
        LoggerInit loggerInit = new LoggerInit("log4j.xml");
        loggerInit.addAltPath("resources/config");
        loggerInit.init();
        Anima anima = new Anima();
        anima.checkAnimaDir();
        anima.start();
    }
}
