package org.openscience.nmrshiftdb.om;

import java.util.Vector;
import org.apache.turbine.om.NumberKey;
import org.apache.turbine.util.db.Criteria;

/**
 *  You should add additional methods to this class to meet the
 *  application requirements.  This class will only be generated as
 *  long as it does not already exist in the output directory.
 *
 * @author     shk3
 * @created    February 19, 2003
 */
public class DBConditionPeer extends org.openscience.nmrshiftdb.om.BaseDBConditionPeer {

    /**
   *  Gets a DBCondition by a value compared with like
   *
   * @param      value      The value to compare
   * @return                The result
   * @exception  Exception  Database problems
   */
    public static Vector getByValueLike(String value) throws Exception {
        Criteria crit = new Criteria();
        crit.add(DBConditionPeer.VALUE, (Object) ("%" + value + "%"), Criteria.LIKE);
        return (DBConditionPeer.doSelect(crit));
    }

    /**
   *  Gets a DBCondition by a value 
   *
   * @param      value      The value to findcompare
   * @return                The result
   * @exception  Exception  Database problems
   */
    public static Vector getByValue(String value) throws Exception {
        Criteria crit = new Criteria();
        crit.add(DBConditionPeer.VALUE, value);
        return (DBConditionPeer.doSelect(crit));
    }

    public static DBCondition makeNewOrGetExisting(NumberKey type, String value, NmrshiftdbUser user) throws Exception {
        Criteria crit = new Criteria();
        crit.add(DBConditionPeer.CONDITION_TYPE_ID, type);
        crit.add(DBConditionPeer.VALUE, value);
        Vector v = DBConditionPeer.doSelect(crit);
        if (v.size() > 0) {
            return (DBCondition) v.get(0);
        } else {
            DBCondition cond = new DBCondition();
            cond.setConditionTypeId(type);
            cond.setValue(value);
            cond.setUserId(user.getUserId());
            cond.save();
            return cond;
        }
    }
}
