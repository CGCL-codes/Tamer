package com.fitso.model.bean.nutrition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import com.fitso.model.bean.measure.energy.Calorie;
import com.fitso.model.bean.measure.mass.Gram;
import com.fitso.model.bean.measure.mass.Mass;

/**
 * <p>
 * Organic compound of lipids that serve both structural and metobolic
 * functions.
 * </p>
 * 
 * @author timothy storm
 * @see <a href="http://en.wikipedia.org/wiki/Fat">Fat</a>
 */
public class Fats extends Nutrient implements Serializable {

    private static final long serialVersionUID = 1L;

    private Mass _monoUnsaturated, _polyUnsaturated, _saturated, _trans;

    public Fats() {
        super();
    }

    public Fats(Mass totalMass) {
        super(totalMass);
    }

    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        Fats other = (Fats) o;
        return new EqualsBuilder().appendSuper(super.equals(other)).append(_monoUnsaturated, other._monoUnsaturated).append(_polyUnsaturated, other._polyUnsaturated).append(_saturated, other._saturated).append(_trans, other._trans).isEquals();
    }

    protected Calorie getCaloriesPerGram() {
        return new Calorie(9.0);
    }

    /**
	 * Magic because food manufacturers are not required to record all fats and
	 * of the fats they are required to report they are allowed to round down or
	 * not record at all if they are below .5g
	 * 
	 * @return
	 */
    public Mass getMagicFatMass() {
        Gram magicMass = new Gram(getMass());
        magicMass.subtract(getMonoUnsaturated());
        magicMass.subtract(getPolyUnsaturated());
        magicMass.subtract(getSaturated());
        magicMass.subtract(getTrans());
        return magicMass;
    }

    public Mass getMonoUnsaturated() {
        return _monoUnsaturated;
    }

    public Mass getPolyUnsaturated() {
        return _polyUnsaturated;
    }

    public Mass getSaturated() {
        return _saturated;
    }

    /**
	 * Equivalent to {@link #getMass()} but is available for clarity
	 * 
	 * @return
	 */
    public Mass getTotal() {
        return super.getMass();
    }

    public Mass getTrans() {
        return _trans;
    }

    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).append(_monoUnsaturated).append(_polyUnsaturated).append(_saturated).append(_trans).toHashCode();
    }

    public void setMonoUnsaturated(Mass monoUnsaturated) {
        _monoUnsaturated = monoUnsaturated;
    }

    public void setPolyUnsaturated(Mass polyUnsaturated) {
        _polyUnsaturated = polyUnsaturated;
    }

    public void setSaturated(Mass saturated) {
        _saturated = saturated;
    }

    /**
	 * Equivalent to {@link #setMass(Mass)} but is available for clarity
	 * 
	 * @return
	 */
    public void setTotal(Mass mass) {
        super.setMass(mass);
    }

    public void setTrans(Mass trans) {
        _trans = trans;
    }

    public String toString() {
        return new ReflectionToStringBuilder(this).toString();
    }

    public Mass[] geAllFats() {
        List<Mass> fats = new ArrayList<Mass>();
        fats.add(getTotal());
        fats.add(_monoUnsaturated);
        fats.add(_polyUnsaturated);
        fats.add(_saturated);
        fats.add(_trans);
        return fats.toArray(new Mass[fats.size()]);
    }
}
