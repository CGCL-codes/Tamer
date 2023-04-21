package uk.co.kimble.cobra.criteria;

import java.util.Vector;
import java.util.Enumeration;
import uk.co.kimble.cobra.PersistentException;

/**
 *  Persistent Criteria can be used to retrieve sets of objects based on their attributes
 *  Normally each attribute is ANDed together, an OR can be performed by creating a new
 *  PersistentCriteria and adding it.
 *  <P>
 *  Criteria are used, rather than a simple string, because they can be precompiled for
 *  efficiency.
 * 
 *  @author		David B George
 *  @version	$\Revision$
 */
public class PersistentCriteria implements java.io.Serializable {

    private Vector criteria;

    private int version;

    private String orderby = null;

    public PersistentCriteria() {
        criteria = new Vector();
        version = 1;
    }

    public Enumeration getElements() {
        return criteria.elements();
    }

    /**
     *  Adds and equals (=) criteria,
     *      customer_id = 10034
     *  <P>
     *  Where Value is a string simple pattern matching can be used, the
     *  character * is a wildcard, e.g.
     *      customer_name = Ander*
     *  <P>
     *  Would match the customers: Anderson and Anderton.  The character ? matches a single
     *  occurence of a character.
     *
     *  @param  attribute   The field name to be used
     *  @param  value       An object representing the value of the field
     */
    public void addEqualTo(String attribute, Object value) {
        if (value instanceof String) {
            String pattern = (String) value;
            pattern = pattern.replace('*', '%');
            pattern = pattern.replace('?', '_');
            if (value != pattern) {
                LikeCriteria c = new LikeCriteria(attribute, pattern);
                criteria.addElement(c);
            } else {
                EqualToCriteria c = new EqualToCriteria(attribute, value);
                criteria.addElement(c);
            }
        } else {
            EqualToCriteria c = new EqualToCriteria(attribute, value);
            criteria.addElement(c);
        }
        version++;
    }

    /**
     *  Adds Greater Than (>) criteria,
     *      customer_id > 10034
     *
     *  @param  attribute   The field name to be used
     *  @param  value       An object representing the value of the field
     */
    public void addGreaterThan(String attribute, Object value) {
        GreaterThanCriteria c = new GreaterThanCriteria(attribute, value);
        criteria.addElement(c);
        version++;
    }

    /**
     *  Adds Less Than (<) criteria,
     *      customer_id < 10034
     *
     *  @param  attribute   The field name to be used
     *  @param  value       An object representing the value of the field
     */
    public void addLessThan(String attribute, Object value) {
        LessThanCriteria c = new LessThanCriteria(attribute, value);
        criteria.addElement(c);
        version++;
    }

    /**
     *  ORs two sets of criteria together:
     *      active = true AND balance < 0 OR active = true AND overdraft = 0
     *
     *  @param  persistent criteria
     *  @throws PersisentException when there are no previous criteria to OR with
     */
    public void addOrCriteria(PersistentCriteria pc) throws PersistentException {
        if (!criteria.isEmpty()) {
            criteria.addElement(pc);
        } else {
            PersistentException pex = new PersistentException("No previous criteria");
        }
        version++;
    }

    /**
     *  ORs two sets of criteria together:
     *      active = true AND balance < 0 OR active = true AND overdraft = 0
     *
     *  @param  persistent criteria
     *  @throws PersisentException when there are no previous criteria to OR with
     */
    public void orderBy(String column) {
        orderby = column;
    }

    public String getOrderby() {
        return orderby;
    }

    public String toString() {
        return criteria.toString() + version + orderby;
    }
}
