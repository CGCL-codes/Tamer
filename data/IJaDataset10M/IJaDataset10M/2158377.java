package org.gaea.lib.selector;

import java.util.Collection;
import java.util.Vector;
import org.gaea.common.exception.GaeaException;
import org.gaea.common.exception.OQLTreatmentException;
import org.gaea.common.metadata.__gaea_joint;
import org.gaea.lib.struct.select.attribute.*;
import org.gaea.lib.struct.select.constraint.*;

/**
 * Class JointureTranslateState for Jointure select expression
 * 
 * @author bbahi
 * @author hpicard
 */
public class JointureTranslateState extends TranslateState {

    /**
	 * Constructor
	 * 
	 * @param translate
	 */
    public JointureTranslateState(Translate translate) {
        super(translate);
    }

    @Override
    public Collection runQuery() throws GaeaException {
        Vector<__gaea_joint> joints = new Vector<__gaea_joint>();
        OQLOrdering oOrder = this.getTranslate().getOqlOrdering();
        if (oOrder != null) {
            ConstraintOrderBy coo = oOrder.getConstraint();
            Vector<AttributeOrderBy> attr = coo.getAttribute();
            this.getTranslate().getTables().sortForOrdering(attr);
            joints = this.FillJoints(attr.toArray(new AttributeOrderBy[0]), true);
        } else {
            joints = this.FillJoints(false);
        }
        Filter oFilter = this.getTranslate().getFilter();
        if (oFilter != null) {
            return oFilter.applyFilter(joints);
        }
        return joints;
    }

    /**
	 * Default starting method for getting the joints' content. The starting
	 * index used is 0.
	 * 
	 * @return vector containg the joint content
	 */
    private Vector<__gaea_joint> FillJoints(boolean ordered) throws GaeaException {
        return this.FillJoints(0, ordered);
    }

    /**
	 * Default starting method for getting the joints' content. The starting
	 * index used is 0.
	 * 
	 * @param orderbyInfos
	 *            AttributeOrderBy of the request
	 * @return vector containg the joint content
	 */
    private Vector<__gaea_joint> FillJoints(AttributeOrderBy[] orderbyInfos, boolean ordered) throws GaeaException {
        return this.FillJoints(0, orderbyInfos, ordered);
    }

    /**
	 * Recursive method that create the joint content of x tables. The starting
	 * index is the one received in parameter. The ordering values used is
	 * <c>null</c>, so no ordering will be made on the tables.
	 * 
	 * @param tableIndex
	 *            table's index
	 * @return vector containg the joint content
	 */
    private Vector<__gaea_joint> FillJoints(int tableIndex, boolean ordered) throws GaeaException {
        return this.FillJoints(tableIndex, null, ordered);
    }

    /**
	 * Recursive method that create the joint content of x tables
	 * 
	 * @param tableIndex
	 *            table's index
	 * @param orderbyInfos
	 *            AttributeOrderBy of the request
	 * @return vector containg the joint content
	 */
    private Vector<__gaea_joint> FillJoints(int tableIndex, AttributeOrderBy[] orderbyInfos, boolean ordered) throws GaeaException {
        Vector<__gaea_joint> jointVector = new Vector<__gaea_joint>();
        __gaea_joint oJoint;
        if (tableIndex + 2 < this.getTranslate().getTables().getCount()) {
            OQLQuery query;
            Object[] table1;
            Vector<__gaea_joint> joints;
            OQLTableAttribute oTable;
            String ordering;
            if (ordered) {
                ordering = "";
                oTable = this.getTranslate().getTables().getOrderedTable(tableIndex);
                ordering = findMatchingOrderBy(tableIndex, orderbyInfos);
                query = new OQLQuery(oTable, "", ordering);
                oTable.addJointName("obj1");
                table1 = query.execute(this.getTranslate().getConnector()).toArray();
                for (int i = tableIndex + 1; i < this.getTranslate().getTables().getOrderedCount(); i++) {
                    oTable = this.getTranslate().getTables().getOrderedTable(i);
                    oTable.addJointName("obj2");
                }
            } else {
                oTable = this.getTranslate().getTables().getTable(tableIndex);
                query = new OQLQuery(oTable, "", "");
                oTable.addJointName("obj1");
                table1 = query.execute(this.getTranslate().getConnector()).toArray();
                for (int i = tableIndex + 1; i < this.getTranslate().getTables().getCount(); i++) {
                    oTable = this.getTranslate().getTables().getTable(i);
                    oTable.addJointName("obj2");
                }
            }
            joints = FillJoints(tableIndex + 1, orderbyInfos, ordered);
            for (int i = 0; i < table1.length; i++) {
                for (int j = 0; j < joints.size(); j++) {
                    oJoint = new __gaea_joint(table1[i], joints.get(j));
                    jointVector.add(oJoint);
                }
            }
        } else {
            OQLQuery query;
            Object[] table1;
            Object[] table2;
            OQLTableAttribute oTable;
            String ordering;
            if (ordered) {
                ordering = "";
                oTable = this.getTranslate().getTables().getOrderedTable(tableIndex);
                ordering = findMatchingOrderBy(tableIndex, orderbyInfos);
                query = new OQLQuery(oTable, "", ordering);
                oTable.addJointName("obj1");
                table1 = query.execute(this.getTranslate().getConnector()).toArray();
                ordering = "";
                oTable = this.getTranslate().getTables().getOrderedTable(tableIndex + 1);
                ordering = this.findMatchingOrderBy(tableIndex + 1, orderbyInfos);
                query = new OQLQuery(oTable, "", ordering);
                oTable.addJointName("obj2");
                table2 = query.execute(this.getTranslate().getConnector()).toArray();
            } else {
                oTable = this.getTranslate().getTables().getTable(tableIndex);
                query = new OQLQuery(oTable, "", "");
                oTable.addJointName("obj1");
                table1 = query.execute(this.getTranslate().getConnector()).toArray();
                oTable = this.getTranslate().getTables().getTable(tableIndex + 1);
                query = new OQLQuery(oTable, "", "");
                oTable.addJointName("obj2");
                table2 = query.execute(this.getTranslate().getConnector()).toArray();
            }
            for (int i = 0; i < table1.length; i++) {
                for (int j = 0; j < table2.length; j++) {
                    oJoint = new __gaea_joint(table1[i], table2[j]);
                    jointVector.add(oJoint);
                }
            }
        }
        return jointVector;
    }

    /**
	 * Finds and formats the string to be used with JDO to get the content of
	 * the table with the wanted ordering. If none applicable, and empty string
	 * is returned
	 * 
	 * @param index
	 *            index of the table in treatment
	 * @param orderbyInfos
	 *            list of order by of the request
	 * @return orderby string
	 * @throws OQLTreatmentException 
	 */
    private String findMatchingOrderBy(int index, AttributeOrderBy[] orderbyInfos) throws OQLTreatmentException {
        String sOrder = "";
        if (orderbyInfos != null) {
            OQLTableAttribute table = this.getTranslate().getTables().getOrderedTable(index);
            for (int i = 0; i < orderbyInfos.length; ++i) {
                Attribute attr = orderbyInfos[i].getAttribute();
                Attribute attrTable = table.getAttribute();
                Attribute attrOrder = ((AttributeIdentifier) attr).getIdentifier();
                if (attrTable.equals(attrOrder)) {
                    AttributeIdentifier orderingAttr = (AttributeIdentifier) orderbyInfos[i].getAttribute();
                    sOrder = orderingAttr.getChild().toString();
                    if (orderbyInfos[i].isAscending()) {
                        sOrder = sOrder + " ascending";
                    } else {
                        sOrder = sOrder + " descending";
                    }
                }
            }
        }
        return sOrder;
    }
}
