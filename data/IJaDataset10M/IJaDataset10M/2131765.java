package au.com.cahaya.asas.ds.prefs.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *
 * @author Mathew Pole
 * @since May 2008
 * @version $Revision$
 */
@Entity
@DiscriminatorValue("F")
@Table(name = "preferencedefinefloat")
public class PreferenceDefineFloatModel extends PreferenceDefineModel {

    /** The private logger for this class */
    @Transient
    private Logger myLog = LoggerFactory.getLogger(PreferenceDefineFloatModel.class);

    /** Default value of the preference (double) */
    @Column(name = "zpdvalue")
    private double myValue;

    /** Units for the value of the preference */
    @Column(name = "zpdunits")
    private String myUnits;

    /** Minimum valid value (default is -MAX_VALUE) */
    @Column(name = "zpdminimum")
    private double myMinimum = -Double.MAX_VALUE;

    /** Maximum valid value (default is MAX_LONG) */
    @Column(name = "zpdmaximum")
    private double myMaximum = Double.MAX_VALUE;

    /**
   * Constructor
   */
    public PreferenceDefineFloatModel() {
    }

    /**
   * @return the value
   */
    public double getValue() {
        return myValue;
    }

    /**
   * @param value the value to set
   */
    public void setValue(double value) {
        myValue = value;
    }

    /**
   * @return the units
   */
    public String getUnits() {
        return myUnits;
    }

    /**
   * @param units the units to set
   */
    public void setUnits(String units) {
        myUnits = units;
    }

    /**
   * @return the minimum
   */
    public double getMinimum() {
        return myMinimum;
    }

    /**
   * @param minimum the minimum to set
   */
    public void setMinimum(double minimum) {
        myMinimum = minimum;
    }

    /**
   * @return the maximum
   */
    public double getMaximum() {
        return myMaximum;
    }

    /**
   * @param maximum the maximum to set
   */
    public void setMaximum(double maximum) {
        myMaximum = maximum;
    }

    /**
   *
   */
    @Override
    PreferenceValueModel createNewValue(String username) {
        return new PreferenceValueFloatModel(this, username);
    }
}
