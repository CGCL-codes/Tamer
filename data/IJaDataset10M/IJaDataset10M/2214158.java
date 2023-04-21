package net.sf.ahtutils.jsf.menu;

import net.sf.ahtutils.test.UtilsJsfTstBootstrap;
import net.sf.ahtutils.xml.navigation.Menu;
import net.sf.exlp.util.xml.JaxbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TstMenu {

    static final Logger logger = LoggerFactory.getLogger(TstMenu.class);

    public TstMenu() {
    }

    public void debug() {
        logger.debug("Debugging XML menu");
        Menu menu = DummyMenuFactory.create();
        JaxbUtil.debug(this.getClass(), menu);
    }

    public static void main(String[] args) {
        UtilsJsfTstBootstrap.init();
        TstMenu test = new TstMenu();
        test.debug();
    }
}
