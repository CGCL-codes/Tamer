package com.narirelays.ems.olap.sample;

import oracle.olapi.data.source.Source;
import oracle.olapi.data.source.StringSource;
import oracle.olapi.metadata.mdm.MdmAttribute;
import oracle.olapi.metadata.mdm.MdmLevelHierarchy;
import oracle.olapi.metadata.mdm.MdmPrimaryDimension;

/**
 * Complete code for the examples in the descriptions of the following methods
 * of the Source class in the Oracle OLAP Java API Reference.
 *
 * <UL>
 *   <LI>Example 1:
 *       recursiveJoin(Source joined, Source comparison, Source parent,
 *                     int comparisonRule, boolean parentsFirst,
 *                     boolean parentsRestrictedToBase, int maxIterations,
 *                     boolean visible)
 *   <LI>Example 2:
 *       recursiveJoin(Source joined, String comparison, Source parent,
 *                     int comparisonRule)
 *   <LI>Example 3:
 *       recursiveJoin(Source joined, String[] comparison, Source parent,
 *                     int comparisonRule)
 *   <LI>Example 4: selectDescendants(Source comparison, Source parent)
 *   <LI>Example 5:
 *       sortDescendingHierarchically(Source joined, Source parent,
 *                                    boolean parentsFirst,
 *                                    boolean parentsRestrictedToBase)
 * </UL>
 *
 * Examples 1, 2, and 4 produce the same results: the same parent value and its
 * children.
 * Example 3 has two parents values and their children.
 * Example 5 uses a parent value from a higher level ands sorts the parent and
 * child values hierarchically in descending order, with the parent values
 * appearing after their children.
 *
 * @author Oracle Corporation
 */
public class RecursiveJoinAndShortcutsExamples extends ContextExample {

    public RecursiveJoinAndShortcutsExamples() {
    }

    public void run() throws Exception {
        MdmPrimaryDimension mdmCustDim = getMdmPrimaryDimension("CUSTOMER_AW");
        MdmLevelHierarchy mdmShipmentsHIer = (MdmLevelHierarchy) getContext().getHierarchyByName(mdmCustDim, "SHIPMENTS_AW");
        Source custDim = mdmCustDim.getSource();
        StringSource shipments = (StringSource) mdmShipmentsHIer.getSource();
        MdmAttribute mdmParentAttr = mdmShipmentsHIer.getParentAttribute();
        MdmAttribute mdmCustValDescAttr = mdmCustDim.getValueDescriptionAttribute();
        Source shipmentsParentAttr = mdmParentAttr.getSource();
        Source custValDescAttr = mdmCustValDescAttr.getSource();
        Source parentValue = shipments.selectValue("SHIPMENTS_AW::WAREHOUSE_AW::17");
        Source parentAndChildren = shipments.recursiveJoin(custDim.value(), parentValue, shipmentsParentAttr, Source.COMPARISON_RULE_SELECT, true, true, 5, false);
        Source parentAndChildrenWithDescr = custValDescAttr.join(parentAndChildren);
        prepareAndCommit();
        println("\nUsing the full recursiveJoin method signature.");
        getContext().displayResult(parentAndChildrenWithDescr);
        Source parentAndChildrenShortcut = shipments.recursiveJoin(custDim.value(), "SHIPMENTS_AW::WAREHOUSE_AW::17", shipmentsParentAttr, Source.COMPARISON_RULE_SELECT);
        Source parentAndChildrenShortcutWithDescr = custValDescAttr.join(parentAndChildrenShortcut);
        prepareAndCommit();
        println("\nUsing the recursiveJoin(Source joined, " + "String comparison,\n" + "Source parent, int comparisonRule) method.");
        getContext().displayResult(parentAndChildrenShortcutWithDescr);
        Source parentsAndChildrenShortcut = shipments.recursiveJoin(custDim.value(), new String[] { "SHIPMENTS_AW::WAREHOUSE_AW::17", "SHIPMENTS_AW::WAREHOUSE_AW::18" }, shipmentsParentAttr, Source.COMPARISON_RULE_SELECT);
        Source parentsAndChildrenShortcutWithDescr = custValDescAttr.join(parentsAndChildrenShortcut);
        prepareAndCommit();
        println("\nUsing the recursiveJoin(Source joined, " + "String[] comparison,\n" + "Source parent, int comparisonRule) method.");
        getContext().displayResult(parentsAndChildrenShortcutWithDescr);
        Source parentAndChildrenShortcut2 = shipments.selectDescendants(getExpressDataProvider().createConstantSource("SHIPMENTS_AW::WAREHOUSE_AW::17"), shipmentsParentAttr);
        Source parentAndChildrenShortcut2WithDescr = custValDescAttr.join(parentAndChildrenShortcut2);
        prepareAndCommit();
        println("\nUsing the selectDescendants(Source comparison, " + "Source parent) method.");
        getContext().displayResult(parentAndChildrenShortcut2WithDescr);
        Source custSel = shipments.selectDescendants(getExpressDataProvider().createConstantSource("SHIPMENTS_AW::REGION_AW::8"), shipmentsParentAttr);
        Source custSelSortedDescending = custSel.sortDescendingHierarchically(custDim.value(), shipmentsParentAttr, false, true);
        Source custSelSortedDescendingWithDescr = custValDescAttr.join(custSelSortedDescending);
        prepareAndCommit();
        println("\nUsing the sortDescendingHierarchically(Source joined, " + "Source parent,\nboolean parentsFirst, " + "boolean parentsRestrictedToBase) method.");
        getContext().displayResult(custSelSortedDescendingWithDescr);
    }

    public static void main(String[] args) {
        new RecursiveJoinAndShortcutsExamples().execute(args);
    }
}
