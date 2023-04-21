package net.sf.appfw.spring;

import net.sf.appfw.slm.LifecycleManagableService;

/**
 * 
 * A multiple inheritance of LifecycleManagableService and ApplicationContextService. 
 * @see ApplicationContextService
 * @author zmhu
 */
public interface LifecycleManagableApplicationContextService extends LifecycleManagableService, ApplicationContextService {
}
