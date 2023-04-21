package org.digitall.apps.cashflow.classes;

import java.util.Vector;
import org.digitall.common.cashflow.classes.Entity;
import org.digitall.common.cashflow.classes.EntityTypes;
import org.digitall.common.cashflow.classes.Money;

public class Person extends Entity {

    private static final int entityType = EntityTypes.PERSON;

    /**
     * @associates <{org.digitall.libs.logistic.ManHour}>
     */
    private ManHour manHour;

    /**
     * @associates <{org.digitall.libs.logistic.Money}>
     */
    private Money money;

    /**
     * @associates <{org.digitall.libs.logistic.ConsumerGood}>
     */
    private Vector consumerGood;

    /**
     * @associates <{org.digitall.libs.logistic.UserGood}>
     */
    private Vector userGood;

    /**
     * @associates <{org.digitall.libs.logistic.Service}>
     */
    private Vector service;

    /**
     * @associates <{org.digitall.libs.logistic.ServiceHours}>
     */
    private Vector serviceHour;

    public Person(double _amount) {
        super(entityType);
        produceManHours(_amount);
    }

    private boolean produceManHours(double _amount) {
        boolean _produce = false;
        if (doProduction(EntityTypes.MAN_HOUR, _amount)) {
            manHour = new ManHour(_amount);
        }
        return _produce;
    }

    public boolean assignMoney(Money _money, double _amount) {
        return _money.doTransfer(money, _amount);
    }

    private boolean consumeEntity(Entity _entity, double _amount) {
        boolean _consume = false;
        if (doConsumption(_entity.getEntityType(), _amount)) {
            int index = getEntityIndex(_entity);
            if (index >= 0) {
                _consume = ((Entity) getContainerVector(_entity).elementAt(index)).consume(_amount);
            }
        }
        return _consume;
    }

    /**
     * Returns the index of the Entity in any Vector
     * @param _entity
     * @return
     */
    private int getEntityIndex(Entity _entity) {
        int index = -1;
        if (haveEntity(_entity)) {
            Vector _entitiesVector = getContainerVector(_entity);
            int i = 0;
            boolean found = false;
            while (i < _entitiesVector.size() && !found) {
                if (_entity == (Entity) _entitiesVector.elementAt(i)) {
                    index = i;
                    found = true;
                }
                i++;
            }
        }
        return index;
    }

    private boolean haveEntity(Entity _entity) {
        return getContainerVector(_entity).contains(_entity);
    }

    public boolean addEntity(Entity _entity) {
        boolean _add = false;
        if (!haveEntity(_entity)) {
            _add = getContainerVector(_entity).add(_entity);
        }
        return _add;
    }

    private Vector getContainerVector(Entity _entity) {
        Vector _entitiesVector = new Vector();
        switch(_entity.getEntityType()) {
            case EntityTypes.SERVICE:
                _entitiesVector = service;
                break;
            case EntityTypes.SERVICE_HOUR:
                _entitiesVector = serviceHour;
                break;
            case EntityTypes.CONSUMPTION_GOOD:
                _entitiesVector = consumerGood;
                break;
            case EntityTypes.USE_GOOD:
                _entitiesVector = userGood;
                break;
        }
        return _entitiesVector;
    }

    private boolean requestEntity(Entity _entity, double _amount) {
        boolean _request = false;
        if (doRequest(_entity.getEntityType(), _amount)) {
            int index = getEntityIndex(_entity);
            if (index >= 0) {
                _request = ((Entity) getContainerVector(_entity).elementAt(index)).request(_amount);
            }
        }
        return _request;
    }
}
