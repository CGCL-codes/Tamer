package org.eaasyst.eaa.apps.oe;

import java.util.ArrayList;
import java.util.List;
import org.eaasyst.eaa.apps.ListApplicationBase;
import org.eaasyst.eaa.data.impl.ResourceGroupDabFactory;
import org.eaasyst.eaa.syst.data.transients.ApplicationActionSpecification;
import org.eaasyst.eaa.syst.data.transients.ColumnSpecification;
import org.eaasyst.eaa.utils.StringUtils;

/**
 * <p>This application displays the resource group information.</p>
 *
 * @version 2.6.3
 * @author Jeff Chilton
 */
public class ResourceGroupList extends ListApplicationBase {

    /**
	 * <p>Constructs a new "ResourceGroupList" object.</p>
	 *
	 * @since 2.3.3
	 */
    public ResourceGroupList() {
        super();
        className = StringUtils.computeClassName(getClass());
        setApplTitleKey("title.resource.group.list");
        setFormName("resourceGroupList");
        setInsertApplication("org.eaasyst.eaa.apps.oe.ResourceGroupEdit");
        setKeyFieldName("idString");
        setKeyLength(12);
        setDynamic(true);
        setAccessBeanFactory(new ResourceGroupDabFactory());
        List colSpecs = new ArrayList();
        ApplicationActionSpecification actionSpec = new ApplicationActionSpecification("Link", ApplicationActionSpecification.ACTION_TYPE_PUSH, "org.eaasyst.eaa.apps.oe.ResourceGroupBrowse");
        ColumnSpecification colSpec = new ColumnSpecification("label.id", true, ColumnSpecification.CONTENT_TYPE_DATA, "idString", actionSpec);
        colSpecs.add(colSpec);
        colSpec = new ColumnSpecification("label.title", true, ColumnSpecification.CONTENT_TYPE_DATA, "title");
        colSpecs.add(colSpec);
        actionSpec = new ApplicationActionSpecification("Link", ApplicationActionSpecification.ACTION_TYPE_PUSH, "org.eaasyst.eaa.apps.oe.ResourceGroupEdit");
        colSpec = new ColumnSpecification("label.edit", "center", false, ColumnSpecification.CONTENT_TYPE_ICON, "edit", actionSpec);
        colSpecs.add(colSpec);
        actionSpec = new ApplicationActionSpecification("Link", ApplicationActionSpecification.ACTION_TYPE_PUSH, "org.eaasyst.eaa.apps.oe.ResourceGroupMemberList");
        colSpec = new ColumnSpecification("label.members", "center", false, ColumnSpecification.CONTENT_TYPE_ICON, "members", actionSpec);
        colSpecs.add(colSpec);
        actionSpec = new ApplicationActionSpecification("Link", ApplicationActionSpecification.ACTION_TYPE_PUSH, "org.eaasyst.eaa.apps.oe.ResourceGroupDelete");
        colSpec = new ColumnSpecification("label.delete", "center", false, ColumnSpecification.CONTENT_TYPE_ICON, "delete", actionSpec);
        colSpecs.add(colSpec);
        setColumnSpecifications(colSpecs);
    }
}
