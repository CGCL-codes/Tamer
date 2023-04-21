package net.sf.rwp.core;

import net.sf.rwp.core.config.ApplicationConfig;
import net.sf.rwp.core.ui.UIManager;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;
import org.zkoss.zkplus.spring.DelegatingVariableResolver;
import org.zkoss.zkplus.spring.SpringUtil;

public class MainRichlet extends GenericRichlet {

    @Override
    public void service(Page page) {
        page.addVariableResolver(new DelegatingVariableResolver());
        page.getDesktop().setAttribute(CoreAttributes.MAIN_UI_PAGE, page);
        page.getDesktop().setAttribute(CoreAttributes.MAIN_UI_PAGE_ID, page.getId());
        ApplicationConfig config = (ApplicationConfig) SpringUtil.getBean("applicationConfig", ApplicationConfig.class);
        UIManager uiManager = (UIManager) SpringUtil.getBean("uiManager", UIManager.class);
        uiManager.openUI(config.getDefaultUI());
        uiManager.openUI(config.getHomeUI());
    }
}
