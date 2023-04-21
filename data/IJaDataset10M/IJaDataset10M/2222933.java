package it.infodea.tapestrydea.pages;

import it.infodea.tapestrydea.services.breadcrumb.BreadcrumbService;
import it.infodea.tapestrydea.support.annotations.PageInfo;
import it.infodea.tapestrydea.support.enumerations.SecurityGroup;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Start page of application tapestrydea.
 */
@PageInfo(allowedSecurityGroup = SecurityGroup.ANONYMOUS, mode = PageInfo.MODE_NORMAL)
public class Index {

    @Inject
    private ComponentResources resources;

    @Inject
    private BreadcrumbService breadcrumbService;

    @SetupRender
    void setup() {
        breadcrumbService.reset();
        breadcrumbService.addBreadcrumb(resources);
    }
}
