package com.googlecode.hyperrecord.view;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import com.googlecode.hyperrecord.dao.hibernate.View;
import com.googlecode.hyperutils.QueryParameter;

@Entity
@DiscriminatorValue("BETWEEN")
public class BetweenRestriction extends PersistentRestriction {

    public BetweenRestriction() {
        super();
    }

    public BetweenRestriction(View view, String propertyName, Object... params) {
        this.view = view;
        this.propertyName = propertyName;
        setObjectsAsStrings(params);
    }

    public Criterion render(QueryParameter... params) {
        return Restrictions.between(propertyName, getFirstParamForRender(params), getSecondParamForRender(params));
    }

    public PersistentRestriction clone() {
        return super.clone(new BetweenRestriction());
    }

    @Override
    public RestrictionType getType() {
        return RestrictionType.BETWEEN;
    }

    @Override
    public int getParamsNeeded() {
        return 2;
    }

    @Override
    public ParameterType[] getSupportedParamTypes() {
        return new ParameterType[] { ParameterType.DATE, ParameterType.NUMERIC, ParameterType.TEXT };
    }
}
