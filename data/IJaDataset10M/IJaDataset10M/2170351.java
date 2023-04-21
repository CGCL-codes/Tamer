package net.sf.opentranquera.ui.struts.actions.demo;

import java.util.Collection;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import net.sf.opentranquera.baseapp.service.OperatorService;
import net.sf.opentranquera.ui.struts.actions.admin.AdministrationUseCaseDelegate;
import org.apache.commons.beanutils.DynaBean;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author Guille
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OperatorDelegate implements AdministrationUseCaseDelegate {

    private OperatorService service;

    OperatorDelegate(ServletContext ctx) {
        ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(ctx);
        this.service = (OperatorService) appContext.getBean("operator");
    }

    public Collection list(DynaBean bean, HttpServletRequest request) {
        return this.service.list(bean);
    }

    public DynaBean detail(DynaBean bean, HttpServletRequest request) {
        return this.service.detail(bean);
    }

    public void save(DynaBean bean, HttpServletRequest request) {
        System.out.println(bean.toString());
        this.service.save(bean);
    }

    public void update(DynaBean bean, HttpServletRequest request) {
        System.out.println(bean.toString());
        this.service.update(bean);
    }
}
