package org.criticalfailure.anp.core.school.management.ui.views;

import org.criticalfailure.anp.core.school.management.ui.Messages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.osgi.context.BundleContextAware;
import org.springframework.stereotype.Component;

/**
 * @author pauly
 * 
 */
@Component("schoolWeekView")
@Scope("prototype")
public class SchoolWeekView extends ViewPart implements ApplicationContextAware, BundleContextAware, DisposableBean, InitializingBean {

    public static final String ID = "org.criticalfailure.anp.ui.views.SchoolWeek";

    /**
     * 
     */
    public SchoolWeekView() {
    }

    @Override
    public void createPartControl(Composite arg0) {
        setPartName(Messages.getString("school.ui.view.week.name"));
    }

    @Override
    public void setFocus() {
    }

    public void setApplicationContext(ApplicationContext arg0) throws BeansException {
    }

    public void setBundleContext(BundleContext arg0) {
    }

    public void destroy() throws Exception {
    }

    public void afterPropertiesSet() throws Exception {
    }
}
