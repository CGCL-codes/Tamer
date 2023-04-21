package org.opencms.module;

import org.opencms.configuration.CmsConfigurationManager;
import org.opencms.db.CmsPublishList;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsEvent;
import org.opencms.main.I_CmsEventListener;
import org.opencms.main.OpenCms;
import org.opencms.report.I_CmsReport;

/**
 * Simple test implementation of the module action interface.<p> 
 */
public class TestModuleActionImpl extends A_CmsModuleAction {

    /** Indicates the last event type catched. */
    public static int m_cmsEvent = -1;

    /** Indicates if the initialize() method was called. */
    public static boolean m_initialize = false;

    /** Indicates if the moduleUninstall() method was called. */
    public static boolean m_moduleUninstall = false;

    /** Indicates if the moduleUpdate() method was called. */
    public static boolean m_moduleUpdate = false;

    /** Indicates if the publishProject() method was called. */
    public static boolean m_publishProject = false;

    /** Indicates if the shutDown() method was called. */
    public static boolean m_shutDown = false;

    /**
     * Default constructor.<p>
     */
    public TestModuleActionImpl() {
    }

    /**
     * @see org.opencms.main.I_CmsEventListener#cmsEvent(org.opencms.main.CmsEvent)
     */
    public void cmsEvent(CmsEvent event) {
        super.cmsEvent(event);
        m_cmsEvent = event.getType();
    }

    /**
     * @see org.opencms.module.I_CmsModuleAction#initialize(org.opencms.file.CmsObject, CmsConfigurationManager, CmsModule)
     */
    public void initialize(CmsObject adminCms, CmsConfigurationManager configurationManager, CmsModule module) {
        super.initialize(adminCms, configurationManager, module);
        m_initialize = true;
        m_shutDown = false;
        OpenCms.addCmsEventListener(this, new int[] { I_CmsEventListener.EVENT_PUBLISH_PROJECT });
    }

    /**
     * @see org.opencms.module.I_CmsModuleAction#moduleUninstall(CmsModule)
     */
    public void moduleUninstall(CmsModule module) {
        super.moduleUninstall(module);
        m_moduleUninstall = true;
        OpenCms.removeCmsEventListener(this);
    }

    /**
     * @see org.opencms.module.I_CmsModuleAction#moduleUpdate(org.opencms.module.CmsModule)
     */
    public void moduleUpdate(CmsModule module) {
        super.moduleUpdate(module);
        m_moduleUpdate = true;
    }

    /**
     * @see org.opencms.module.I_CmsModuleAction#publishProject(org.opencms.file.CmsObject, org.opencms.db.CmsPublishList, int, org.opencms.report.I_CmsReport)
     */
    public void publishProject(CmsObject cms, CmsPublishList publishList, int publishTag, I_CmsReport report) {
        super.publishProject(cms, publishList, publishTag, report);
        m_publishProject = true;
    }

    /**
     * @see org.opencms.module.I_CmsModuleAction#shutDown(CmsModule)
     */
    public void shutDown(CmsModule module) {
        super.shutDown(module);
        m_shutDown = true;
    }
}
