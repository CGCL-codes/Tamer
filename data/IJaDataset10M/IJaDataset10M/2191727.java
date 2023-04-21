package com.narirelays.ems.olap.sample.chapter5;

import oracle.olapi.data.source.DataProvider;
import oracle.olapi.data.source.NumberSource;
import oracle.olapi.data.source.Source;
import oracle.olapi.data.source.StringSource;
import com.narirelays.ems.olap.sample.*;
import oracle.olapi.metadata.mdm.MdmAttribute;
import oracle.olapi.metadata.mdm.MdmLevel;
import oracle.olapi.metadata.mdm.MdmLevelHierarchy;
import oracle.olapi.metadata.mdm.MdmMeasure;
import oracle.olapi.metadata.mdm.MdmPrimaryDimension;
import oracle.olapi.metadata.mdm.MdmStandardDimension;
import oracle.olapi.metadata.mdm.MdmStandardMember;
import java.util.Iterator;
import java.util.List;

/**
 * This program contains the code that appears in the Creating a
 * Custom Member That Assigns an Aggregated Value example in the
 * Understanding Source Objects chapter in the Oracle OLAP Developer's Guide 
 * to the OLAP API.
 * This program creates a custom member of the Product dimension.
 * For the custom member Oracle OLAP automatically adds an Assignment object
 * to the Number MdmDimensionCalculationModel for the dimension.
 * The value that Oracle OLAP assigns for the custom member is the result of
 * an aggregation of the values specified by other dimension members.
 *
 * @author Oracle Corporation
 */
public class CreateCustomMemberWithAggVal extends ContextExample {

    protected void run() throws Exception {
        println("Creating a Custom Member That Assigns an Aggregated Value\n");
        DataProvider dp = getContext().getDataProvider();
        MdmMeasure mdmSales = getMdmMeasure("SALES_AW");
        NumberSource sales = (NumberSource) mdmSales.getSource();
        MdmStandardDimension mdmProdStdDim = (MdmStandardDimension) getMdmPrimaryDimension("PRODUCT_AW");
        MdmPrimaryDimension mdmTimeDim = getMdmPrimaryDimension("TIME_AW");
        MdmPrimaryDimension mdmChanDim = getMdmPrimaryDimension("CHANNEL_AW");
        MdmPrimaryDimension mdmCustDim = getMdmPrimaryDimension("CUSTOMER_AW");
        Source prodStdDim = mdmProdStdDim.getSource();
        MdmAttribute mdmMktMngrAttr = getContext().getAttributeByName(mdmProdStdDim, "MARKETING_MANAGER_AW");
        Source mktMngrAttr = mdmMktMngrAttr.getSource();
        MdmLevelHierarchy mdmCalendar = (MdmLevelHierarchy) mdmTimeDim.getDefaultHierarchy();
        MdmLevelHierarchy mdmProdHier = (MdmLevelHierarchy) mdmProdStdDim.getDefaultHierarchy();
        MdmLevelHierarchy mdmChanHier = (MdmLevelHierarchy) mdmChanDim.getDefaultHierarchy();
        MdmLevelHierarchy mdmShipHier = (MdmLevelHierarchy) mdmCustDim.getDefaultHierarchy();
        StringSource calendar = (StringSource) mdmCalendar.getSource();
        StringSource prodHier = (StringSource) mdmProdHier.getSource();
        StringSource chanHier = (StringSource) mdmChanHier.getSource();
        StringSource shipHier = (StringSource) mdmShipHier.getSource();
        Source ph = dp.getFundamentalMetadataProvider().getNumberPlaceholder().getSource();
        MdmLevel mdmItemLevel = getContext().getLevelByName(mdmProdHier, "ITEM_AW");
        Source itemLevel = mdmItemLevel.getSource();
        Source prodForManager = itemLevel.join(mktMngrAttr, "Jackson");
        MdmAttribute mdmProdShortDescr = mdmProdStdDim.getShortValueDescriptionAttribute();
        Source prodShortDescr = mdmProdShortDescr.getSource();
        Source calc = ((NumberSource) (ph.join(prodHier, new String[] { "PRODUCT_PRIMARY_AW::ITEM_AW::24", "PRODUCT_PRIMARY_AW::ITEM_AW::25", "PRODUCT_PRIMARY_AW::ITEM_AW::26", "PRODUCT_PRIMARY_AW::ITEM_AW::33", "PRODUCT_PRIMARY_AW::ITEM_AW::34", "PRODUCT_PRIMARY_AW::ITEM_AW::35", "PRODUCT_PRIMARY_AW::ITEM_AW::36", "PRODUCT_PRIMARY_AW::ITEM_AW::37", "PRODUCT_PRIMARY_AW::ITEM_AW::38", "PRODUCT_PRIMARY_AW::ITEM_AW::39" }))).total();
        MdmStandardMember mdmMktMngrTotal = mdmProdStdDim.createCustomMember("65", mdmItemLevel, "4", calc, 10);
        mdmMktMngrTotal.setShortDescription("Marketing Manager Total");
        Source mktMngrWithTotal = prodForManager.appendValue(prodHier.selectValue("PRODUCT_PRIMARY_AW::ITEM_AW::65"));
        Source result = sales.join(prodShortDescr.join(mktMngrWithTotal)).join(shipHier, "SHIPMENTS_AW::SHIP_TO_AW::106").join(chanHier, "CHANNEL_PRIMARY_AW::TOTAL_CHANNEL_AW::1").join(calendar, "CALENDAR_YEAR_AW::YEAR_AW::3");
        prepareAndCommit();
        getContext().displayResult(result);
    }

    public static void main(String[] args) {
        new CreateCustomMemberWithAggVal().execute(args);
    }
}
