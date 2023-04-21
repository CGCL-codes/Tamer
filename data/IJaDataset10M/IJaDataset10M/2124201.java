package metso.dal.impl;

import java.util.Collection;
import java.util.List;
import metso.dal.DalException;
import metso.dal.query.OperatorType;
import metso.dal.query.Order;
import metso.dal.query.QueryExpression;
import metso.dal.query.QueryLoadMode;
import metso.dal.query.QueryOperator;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;

public class CriteriaImpl {

    private static Logger log = Logger.getLogger(CriteriaImpl.class);

    public static List<?> LoadCriteria(Class<?> clazz, Session session, QueryExpression expression) throws DalException {
        Criteria hcr = getCriteria(clazz, session, expression);
        return (List<?>) hcr.list();
    }

    public static Object LoadObjectByCriteria(Class<?> clazz, Session session, QueryExpression expression) throws DalException {
        return getCriteria(clazz, session, expression).uniqueResult();
    }

    private static Criteria getCriteria(Class<?> clazz, Session session, QueryExpression c) throws DalException {
        try {
            Criteria criteria = setAliases(session.createCriteria(clazz), c);
            if (c.getCondition() != null) {
                criteria.add(getExpression(c.getCondition()));
            }
            if (c.getOrder() != null) {
                criteria.addOrder(getOrderHib(c.getOrder()));
            }
            return criteria;
        } catch (Throwable e) {
            log.error("Sollevata Eccezione nel GetCriteria: ", e);
            throw new DalException("Sollevata Eccezione nel GetCriteria: ", e);
        }
    }

    private static org.hibernate.criterion.Order getOrderHib(Order order) {
        if (order.getOrd() == Order.Ord.Ascending) {
            return org.hibernate.criterion.Order.asc(order.getProperty());
        } else {
            return org.hibernate.criterion.Order.desc(order.getProperty());
        }
    }

    private static Criteria setAliases(Criteria hibCriteria, QueryExpression medCriterio) {
        Criteria result = hibCriteria;
        if (medCriterio.getAliases() != null) {
            for (QueryOperator op : medCriterio.getAliases()) {
                if (op.getOperatorType() == OperatorType.Alias) {
                    result = result.createAlias((String) op.getFirstParameter(), (String) op.getSecondParameter());
                } else {
                    QueryLoadMode loadMode = (QueryLoadMode) op.getSecondParameter();
                    switch(loadMode) {
                        case Join:
                            result = result.setFetchMode((String) op.getFirstParameter(), FetchMode.JOIN);
                            break;
                        case Select:
                            result = result.setFetchMode((String) op.getFirstParameter(), FetchMode.SELECT);
                            break;
                        case Lazy:
                        case Complete:
                        default:
                            result = result.setFetchMode((String) op.getFirstParameter(), FetchMode.DEFAULT);
                            break;
                    }
                }
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private static Criterion getExpression(QueryOperator operatore) {
        switch(operatore.getOperatorType()) {
            case And:
                return Expression.and(getExpression((QueryOperator) operatore.getFirstParameter()), getExpression((QueryOperator) operatore.getSecondParameter()));
            case Or:
                return Expression.or(getExpression((QueryOperator) operatore.getFirstParameter()), getExpression((QueryOperator) operatore.getSecondParameter()));
            case Not:
                return Expression.not(getExpression((QueryOperator) operatore.getFirstParameter()));
            case Eq:
                return Expression.eq((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case EqProperty:
                return Expression.eqProperty((String) operatore.getFirstParameter(), (String) operatore.getSecondParameter());
            case Ge:
                return Expression.ge((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case Gt:
                return Expression.gt((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case In:
                if (operatore.getSecondParameter() instanceof Collection) {
                    return Expression.in((String) operatore.getFirstParameter(), (Collection) operatore.getSecondParameter());
                } else {
                    return Expression.in((String) operatore.getFirstParameter(), (Object[]) operatore.getSecondParameter());
                }
            case InsensitiveLike:
                return Expression.ilike((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case IsNotNull:
                return Expression.isNotNull((String) operatore.getFirstParameter());
            case IsNull:
                return Expression.isNull((String) operatore.getFirstParameter());
            case Le:
                return Expression.le((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case LeProperty:
                return Expression.leProperty((String) operatore.getFirstParameter(), (String) operatore.getSecondParameter());
            case Like:
                return Expression.like((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case Lt:
                return Expression.lt((String) operatore.getFirstParameter(), operatore.getSecondParameter());
            case LtProperty:
                return Expression.ltProperty((String) operatore.getFirstParameter(), (String) operatore.getSecondParameter());
            default:
                return null;
        }
    }
}
