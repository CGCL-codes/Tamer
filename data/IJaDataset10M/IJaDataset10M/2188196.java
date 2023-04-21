package net.sf.ajaxplus.tool;

import java.util.ArrayList;
import java.util.List;

public class ProjectUWRiskLimit {

    static String PROJECT_ROOT = "C:\\Documents and Settings\\shelby.liu\\workspace\\fvcaps3\\";

    static String CLASS_BASE_DIR = PROJECT_ROOT + "src\\";

    static String CLASS_PACKAGE_DIR = "bm\\validusre\\vcaps\\report";

    static String WEB_BASE_DIR = PROJECT_ROOT + "web\\";

    static String WEB_JSP_DIR = "jsp\\report";

    private static MetaFactory metaFactory = new MetaFactory();

    static {
        metaFactory.setProjectRoot(PROJECT_ROOT);
        List<MetaUIColumn> columns = new ArrayList<MetaUIColumn>();
        MetaModel riskUWLimitGranularityTypeModel = new MetaModel("RiskUWLimitGranularityType", CLASS_PACKAGE_DIR, CLASS_BASE_DIR, true);
        riskUWLimitGranularityTypeModel.setDefaultOrderByField("itemOrder");
        riskUWLimitGranularityTypeModel.setKeyValueFlag(true);
        metaFactory.addModel(riskUWLimitGranularityTypeModel);
        columns = new ArrayList<MetaUIColumn>();
        columns.add(new MetaUIColumn().setDBColumnName("riskUWLimitGranularityTypeId").setKey().setHidden());
        columns.add(new MetaUIColumn().setDBColumnName("riskUWLimitGranularityTypeId").setHidden());
        columns.add(new MetaUIColumn().setDBColumnName("riskUWLimitGranularityTypeName").setLabel("Risk Granularity Type Name"));
        MetaPage riskUWLimitGranularityTypeListPage = new MetaPage().dirName(WEB_BASE_DIR + WEB_JSP_DIR).setPageType("List").addTable(new MetaUITable().setMetaModel(riskUWLimitGranularityTypeModel).addMetaUIColumns(columns));
        riskUWLimitGranularityTypeListPage.setMainMetaModel(riskUWLimitGranularityTypeModel);
        MetaPage riskUWLimitGranularityTypeEditPage = new MetaPage().dirName(WEB_BASE_DIR + WEB_JSP_DIR).setPageType("Edit").addTable(new MetaUITable().setMetaModel(riskUWLimitGranularityTypeModel).addMetaUIColumns(columns));
        riskUWLimitGranularityTypeEditPage.setMainMetaModel(riskUWLimitGranularityTypeModel);
        metaFactory.addPage(riskUWLimitGranularityTypeListPage);
        metaFactory.addPage(riskUWLimitGranularityTypeEditPage);
    }

    public static MetaFactory getMetaFactory() {
        return metaFactory;
    }
}
