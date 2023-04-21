package org.digitall.apps.cashflow.classes;

import org.digitall.common.cashflow.classes.Entity;
import org.digitall.common.cashflow.classes.EntityTypes;
import org.digitall.common.cashflow.classes.Money;

public class Investment extends Entity {

    private static final int entityType = EntityTypes.INVESTMENT;

    /**
     * @associates <{org.digitall.libs.logistic.Money}>
     */
    private Money money;

    public Investment(double _amount) {
        super(entityType);
        money = new Money(_amount);
    }

    private boolean produceMoney(double _amount) {
        boolean _produce = false;
        if (doProduction(EntityTypes.MONEY, _amount)) {
            _produce = money.produce(_amount);
        }
        return _produce;
    }
}
