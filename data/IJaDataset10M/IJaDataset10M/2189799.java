package gov.lanl.Database;

import org.apache.log4j.Category;
import java.util.*;

/**
 * Class declaration
 *
 * @author <a href="mailto:Sascha@Koenig.net">Sascha A. Koenig<a>
 * @version $Revision: 3054 $ $Date: 2004-07-06 18:14:00 -0400 (Tue, 06 Jul 2004) $
 */
public class PassThruSearchFilter extends SearchFilter {

    static Category cat = Category.getInstance(PassThruSearchFilter.class.getName());

    PassThruCriteria criteria = new PassThruCriteria();

    /**
	 * Change the search filter to one that specifies an element to
	 * match or not match one of a list of values.
	 * The old search filter is deleted.
	 *
	 * @param elementName is the name of the element to be matched
	 * @param values is a vector of possible matches
	 * @param oper is the IN or NOT_IN operator to indicate how to matche
	 */
    public void matchList(String elementName, Vector values, int oper) {
        criteria = new PassThruCriteria();
        if (oper != NOT_IN) {
            try {
                for (int i = 0; i < values.size(); i++) {
                    PassThruCriteria tempCrit = new PassThruCriteria();
                    tempCrit.addEqualTo(elementName, values.elementAt(i));
                    criteria.addOrCriteria(tempCrit);
                }
            } catch (Exception e) {
                cat.error("Can't add 'OR' criteria. Skiping element! Exception: " + e);
            }
        } else {
            for (int i = 0; i < values.size(); i++) {
                criteria.addNotEqualTo(elementName, values.elementAt(i));
            }
        }
    }

    /**
	 * Change the search filter to one that specifies an element to not
	 * match one of a list of values.
	 * The old search filter is deleted.
	 *
	 * @param elementName is the name of the element to be matched
	 * @param values is an array of possible matches
	 * @param oper is the IN or NOT_IN operator to indicate how to matche
         *
         * GBO 20-8-02 used by 'what' part in query
	 */
    public void matchList(String elementName, String[] values, int oper) {
        criteria = new PassThruCriteria();
        if (oper != NOT_IN) {
            try {
                for (int i = 0; i < values.length; i++) {
                    PassThruCriteria tempCrit = new PassThruCriteria();
                    tempCrit.addEqualTo(elementName, values[i]);
                    criteria.addOrCriteria(tempCrit);
                }
            } catch (Exception e) {
                cat.error("Can't add 'OR' criteria. Skiping element! Exception: " + e);
            }
        } else {
            for (int i = 0; i < values.length; i++) {
                criteria.addNotEqualTo(elementName, values[i]);
            }
        }
    }

    /**
	 * Change the search filter to one that specifies an element to not
	 * match one of a list of integer values.
	 * The old search filter is deleted.
	 *
	 * @param elementName is the name of the element to be matched
	 * @param values is an array of possible integer matches
	 * @param oper is the IN or NOT_IN operator to indicate how to matche
	 */
    public void matchList(String elementName, int[] values, int oper) {
        if (oper != NOT_IN) {
            try {
                for (int i = 0; i < values.length; i++) {
                }
            } catch (Exception e) {
                cat.error("Can't add 'OR' criteria. Skiping element! Exception: " + e);
            }
        } else {
            for (int i = 0; i < values.length; i++) {
            }
        }
    }

    /**
	 * Change the search filter to one that specifies an element to not
	 * match one single value.
	 * The old search filter is deleted.
	 *
	 * @param elementName is the name of the element to be matched
	 * @param value is the value to be matched (or not matched)
	 * @param oper is the IN or NOT_IN operator to indicate how to match
	 */
    public void matchValue(String elementName, String value, int oper) {
        if (oper != NOT_IN) {
        } else {
        }
    }

    /**
	 * -----------------------------------------------------------
	 * @param elementName is the name of the element to be matched
	 * @param value is the int value to be matched (or not matched)
	 * @param oper is the IN or NOT_IN operator to indicate how to match
	 */
    public void matchValue(String elementName, int value, int oper) {
        if (oper != NOT_IN) {
        } else {
        }
    }

    /**
	 * Change the search filter to one that compares an element name to a value.
	 * The old search filter is deleted.
	 *
	 * @param elementName is the name of the element to be tested
	 * @param value is the value to be compared against
	 * @param oper is the binary comparison operator to be used
	 * @exception gov.lanl.Database.DBException
         *
         * GBO 20-8-02 Used for 'who' and 'when' parts of query
	 */
    public void compareFilter(String elementName, String value, int oper) throws DBException {
        criteria = new PassThruCriteria();
        if ((oper & BINARY_OPER_MASK) == 0) {
            throw new DBException();
        }
        switch(oper) {
            case LIKE:
                {
                    criteria.addLike(elementName, value);
                    break;
                }
            case EQUAL:
                {
                    criteria.addEqualTo(elementName, value);
                    break;
                }
            case NOT_EQUAL:
                {
                    criteria.addNotEqualTo(elementName, value);
                    break;
                }
            case LESS_THAN:
                {
                    criteria.addLessThan(elementName, value);
                    break;
                }
            case GREATER_THAN:
                {
                    criteria.addGreaterThan(elementName, value);
                    break;
                }
            case GREATER_EQUAL:
                {
                    criteria.addGreaterOrEqualThan(elementName, value);
                    break;
                }
            case LESS_EQUAL:
                {
                    criteria.addLessOrEqualThan(elementName, value);
                    break;
                }
            default:
                {
                    cat.error("Unsupported binary operation in OJBSearchFilter!");
                    throw new DBException();
                }
        }
    }

    /**
	 * Change the search filter to one that specifies a set of elements and their values
	 * that must match, and the operator to use to combine the elements.
	 * Each key is compared for an equal match to the value, and all
	 * comparisons are combined by the specified logical operator (OR or AND).
	 * The old search filter is deleted.
	 *
	 * @param elements is a hashtable holding key-value pairs
	 * @param combine_op is the logical operator to be used to combine the comparisons
	 * @param compare_op is the binary operator to be used for the comparisons
	 * @exception gov.lanl.Database.DBException
	 */
    public void matchSet(Hashtable elements, int combine_op, int compare_op) throws DBException {
        if ((compare_op & BINARY_OPER_MASK) == 0) {
            throw new DBException();
        }
        if (combine_op == AND) {
            for (Enumeration e = elements.keys(); e.hasMoreElements(); ) {
                String elementName = (String) e.nextElement();
                String elementValue = (String) elements.get(elementName);
                switch(compare_op) {
                    case LIKE:
                        {
                            break;
                        }
                    case EQUAL:
                        {
                            break;
                        }
                    case NOT_EQUAL:
                        {
                            break;
                        }
                    case LESS_THAN:
                        {
                            break;
                        }
                    case GREATER_THAN:
                        {
                            break;
                        }
                    case GREATER_EQUAL:
                        {
                            break;
                        }
                    case LESS_EQUAL:
                        {
                            break;
                        }
                    default:
                        {
                            cat.error("Unsupported binary operation in OJBSearchFilter!");
                            throw new DBException();
                        }
                }
            }
        } else if (combine_op == OR) {
            try {
                for (Enumeration e = elements.keys(); e.hasMoreElements(); ) {
                    String elementName = (String) e.nextElement();
                    String elementValue = (String) elements.get(elementName);
                    switch(compare_op) {
                        case LIKE:
                            {
                                break;
                            }
                        case EQUAL:
                            {
                                break;
                            }
                        case NOT_EQUAL:
                            {
                                break;
                            }
                        case LESS_THAN:
                            {
                                break;
                            }
                        case GREATER_THAN:
                            {
                                break;
                            }
                        case GREATER_EQUAL:
                            {
                                break;
                            }
                        case LESS_EQUAL:
                            {
                                break;
                            }
                        default:
                            {
                                cat.error("Unsupported binary operation in OJBSearchFilter!");
                                throw new DBException();
                            }
                    }
                }
            } catch (Exception e) {
                cat.error("Can't combine search criteria in OJBSearchFilter.matchSet(...) " + e);
                throw new DBException();
            }
        } else {
            throw new DBException();
        }
    }

    /**
	 * Change the search filter to one that specifies a set of elements and their values
	 * that must match, and the operator to use to combine the elements.
	 * Each element name is compared for an equal match to the value, and all
	 * comparisons are combined by the specified logical operator (OR or AND).
	 * The old search filter is deleted.
	 *
	 * @param elementNames is an array of names of elements to be tested
	 * @param elementValues is an array of values for the corresponding element
	 * @param op is the logical operator to be used to combine the comparisons
	 * @exception gov.lanl.Database.DBException
	 */
    public void matchSet(String[] elementNames, String[] elementValues, int op) throws DBException {
        if (op == OR) {
            try {
                for (int i = 0; i < elementNames.length; i++) {
                }
            } catch (Exception e) {
                cat.error("Can't add 'OR' criteria. Skiping element! Exception: " + e);
            }
        } else if (op == AND) {
            for (int i = 0; i < elementNames.length; i++) {
            }
        } else {
            throw new DBException();
        }
    }

    /**
	 * Combine other search filters with this one, using the specific operator.
	 *
	 * @param new_filters is a vector of SearchFilter classes to be combined
	 * @param op is the logical operator to be used to combine the filters
	 * @exception gov.lanl.Database.DBException
	 */
    public void combine(Vector new_filters, int op) throws DBException {
        for (Enumeration elems = new_filters.elements(); elems.hasMoreElements(); ) {
            SearchFilter filter = (SearchFilter) elems.nextElement();
            combine(filter, op);
        }
    }

    /**
	 * Combine one other search filters with this one, using the specific operator.
	 *
	 * @param new_filter is the SearchFilter class to be combined
	 * @param op is the logical operator to be used to combine the filters
	 * @exception gov.lanl.Database.DBException
         *
         * Used to combine 'who', 'what', and 'when' parts of the query
	 */
    public void combine(SearchFilter new_filter, int op) throws DBException {
        PassThruSearchFilter anyFilter = (PassThruSearchFilter) new_filter;
        switch(op) {
            case OR:
                {
                    try {
                        criteria.addOrCriteria(anyFilter.getCriteria());
                        break;
                    } catch (Exception e) {
                        throw new DBException();
                    }
                }
            case AND:
                {
                    criteria.addAndCriteria(anyFilter.getCriteria());
                    break;
                }
            default:
                {
                    throw new DBException();
                }
        }
    }

    /**
	 * -----------------------------------------------------------
	 * @return
	 */
    public String toString() {
        return criteria.toString();
    }

    public PassThruCriteria getCriteria() {
        return criteria;
    }
}
