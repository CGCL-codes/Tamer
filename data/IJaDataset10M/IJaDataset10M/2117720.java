package org.imogene.ws.criteria;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;

public class HibernateDaoUtil {

    private static Logger logger = Logger.getLogger(HibernateDaoUtil.class.getName());

    /**
	 * 
	 * @param junction
	 * @return
	 */
    public static Criterion addMedooJunction(MedooJunction junction) {
        Junction criterion = null;
        if (junction.getType().equals(MedooDisjunction.TYPE)) {
            criterion = Restrictions.disjunction();
        } else {
            criterion = Restrictions.conjunction();
        }
        for (MedooCriterion crit : junction.getCriterions()) {
            if (crit instanceof BasicCriteria) {
                addCriterion(criterion, (BasicCriteria) crit);
            } else {
                criterion.add(addMedooJunction((MedooJunction) crit));
            }
        }
        return criterion;
    }

    /**
	 * 
	 * @param junction 
	 * @param criteria
	 */
    public static void addCriterion(Junction junction, BasicCriteria criteria) {
        String property = criteria.getField();
        String operator = criteria.getOperation();
        String value = criteria.getValue();
        try {
            if (operator.equals(CriteriaConstants.STRING_OPERATOR_CONTAINS)) {
                junction.add(Restrictions.ilike(property, "%" + value + "%"));
            } else if (operator.equals(CriteriaConstants.STRING_OPERATOR_EQUAL)) {
                junction.add(Restrictions.eq(property, value));
            } else if (operator.equals(CriteriaConstants.STRING_OPERATOR_STARTWITH)) {
                junction.add(Restrictions.ilike(property, value + "%"));
            } else if (operator.equals(CriteriaConstants.STRING_OPERATOR_DIFF)) {
                junction.add(Restrictions.ne(property, value));
            } else if (operator.equals(CriteriaConstants.DATE_OPERATOR_BEFORE)) {
                try {
                    Long longValue = Long.valueOf(value);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(longValue);
                    cal.set(Calendar.HOUR_OF_DAY, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    junction.add(Restrictions.le(property, cal.getTime()));
                } catch (Exception e) {
                    value = value + " 23:59:59";
                    junction.add(Restrictions.le(property, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(value)));
                }
            } else if (operator.equals(CriteriaConstants.DATE_OPERATOR_AFTER)) {
                try {
                    Long longValue = Long.valueOf(value);
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(longValue);
                    cal.set(Calendar.HOUR_OF_DAY, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    cal.set(Calendar.MILLISECOND, 0);
                    junction.add(Restrictions.ge(property, cal.getTime()));
                } catch (Exception e) {
                    junction.add(Restrictions.ge(property, new SimpleDateFormat("dd/MM/yyyy").parse(value)));
                }
            } else if (operator.equals(CriteriaConstants.DATE_OPERATOR_EQUAL)) {
                try {
                    Long longValue = Long.valueOf(value);
                    Date dateValue = new Date(longValue);
                    junction.add(Restrictions.eq(property, dateValue));
                } catch (Exception e) {
                    junction.add(Restrictions.eq(property, new SimpleDateFormat("dd/MM/yyyy").parse(value)));
                }
            } else if (operator.equals(CriteriaConstants.INT_OPERATOR_EQUAL)) {
                junction.add(Restrictions.eq(property, Integer.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.INT_OPERATOR_SUP)) {
                junction.add(Restrictions.ge(property, Integer.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.INT_OPERATOR_INF)) {
                junction.add(Restrictions.le(property, Integer.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_EQUAL)) {
                junction.add(Restrictions.eq(property, Float.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_SUP)) {
                junction.add(Restrictions.ge(property, Float.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.FLOAT_OPERATOR_INF)) {
                junction.add(Restrictions.le(property, Float.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL)) {
                junction.add(Restrictions.eq(property, value));
            } else if (operator.equals(CriteriaConstants.RELATIONFIELD_OPERATOR_EQUAL_NULL)) {
                junction.add(Restrictions.isNull(property));
            } else if (operator.equals(CriteriaConstants.BOOLEAN_OPERATOR_EQUAL)) {
                junction.add(Restrictions.eq(property, Boolean.valueOf(value)));
            } else if (operator.equals(CriteriaConstants.MULTIENUM_OPERATOR_EQUAL)) {
                Junction dis = Restrictions.disjunction();
                dis.add(Restrictions.ilike(property, value + CriteriaConstants.FIELDVALUE_SEPARATOR + "%"));
                dis.add(Restrictions.ilike(property, "%" + CriteriaConstants.FIELDVALUE_SEPARATOR + value + CriteriaConstants.FIELDVALUE_SEPARATOR + "%"));
                dis.add(Restrictions.ilike(property, "%" + CriteriaConstants.FIELDVALUE_SEPARATOR + value));
                dis.add(Restrictions.ilike(property, value));
                junction.add(dis);
            } else if (operator.equals(CriteriaConstants.OPERATOR_ISNULL)) {
                junction.add(Restrictions.isNull(property));
            } else {
                logger.error("Search operator not found for query on property: " + property + " with value: " + value);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }
}
