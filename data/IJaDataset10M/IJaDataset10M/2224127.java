package com.hack23.cia.web.impl.ui.page.dev;

import java.util.List;
import java.util.Map;
import org.vaadin.navigator7.Page;
import org.vaadin.vaadinvisualizations.OrganizationalChart;
import org.vaadin.vaadinvisualizations.PieChart;
import com.hack23.cia.model.external.riksdagen.person.impl.RoleStatus;
import com.hack23.cia.model.external.riksdagen.person.impl.SexType;
import com.hack23.cia.service.api.DataContainer;
import com.hack23.cia.service.api.DataSummary;
import com.hack23.cia.service.api.DataSummary.DataSummaryId;
import com.hack23.cia.web.impl.ui.page.user.AbstractUserPage;
import com.vaadin.ui.GridLayout;

/**
 * The Class TestPage.
 */
@Page(uriName = "test", crawlable = true)
@SuppressWarnings("serial")
public final class TestPage extends AbstractUserPage {

    /** The content. */
    private final GridLayout content = new GridLayout(2, 2);

    /**
	 * Instantiates a new test page.
	 */
    public TestPage() {
        super();
        final DataContainer<DataSummary, DataSummaryId> dataContainer = getApplicationManager().getDataContainer(DataSummary.class);
        final List<DataSummary> all = dataContainer.getAll();
        final DataSummary dataSummary = all.iterator().next();
        final OrganizationalChart oc = new OrganizationalChart();
        oc.setSizeFull();
        oc.setOption("size", "medium");
        oc.setOption("allowCollapse", false);
        oc.add("Riksdagen", "", "Riksdagen Content");
        oc.add("ParliamentMember", "Riksdagen", "ParliamentMembers :" + dataSummary.personSize);
        oc.add("Active", "ParliamentMember", "Active members :" + dataSummary.roleStatusMap.get(RoleStatus.TJÄNSTGÖRANDE_RIKSDAGSLEDAMOT));
        oc.add("Substitute", "ParliamentMember", "Substitute members :" + dataSummary.roleStatusMap.get(RoleStatus.TJÄNSTGÖRANDE_ERSÄTTARE));
        content.addComponent(oc);
        final PieChart roleStatusPieChart = new PieChart();
        roleStatusPieChart.setOption("is3D", true);
        roleStatusPieChart.setOption("width", 200);
        roleStatusPieChart.setOption("height", 200);
        final Map<RoleStatus, Integer> roleStatusMap = dataSummary.roleStatusMap;
        for (final RoleStatus roleStatus : roleStatusMap.keySet()) {
            roleStatusPieChart.add(roleStatus.toString(), roleStatusMap.get(roleStatus));
        }
        content.addComponent(roleStatusPieChart);
        final PieChart sexTypePieChart = new PieChart();
        sexTypePieChart.setOption("is3D", true);
        sexTypePieChart.setOption("width", 200);
        sexTypePieChart.setOption("height", 200);
        final Map<SexType, Integer> sexTypeMap = dataSummary.sexMap;
        for (final SexType sexType : sexTypeMap.keySet()) {
            sexTypePieChart.add(sexType.toString(), sexTypeMap.get(sexType));
        }
        content.addComponent(sexTypePieChart);
        content.setSizeFull();
        content.setMargin(true);
        content.setSpacing(true);
        setCompositionRoot(content);
        setHeight(getScreenHeight());
    }

    @Override
    public String getPageTitle() {
        return "";
    }
}
