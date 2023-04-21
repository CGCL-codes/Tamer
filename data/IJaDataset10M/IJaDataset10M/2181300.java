package containers.unitfeatures;

import containers.IUnitFeature;
import containers.Unit;
import misc.HibernateUtil;

/**
 * Jednostki o tej cesze nie mogą być zniszczone (np kwatera główna) - w
 * momencie wywołania metody destroy() jednostki akcja jest anulowana gdy ta
 * jednostka ma ceche Indestructible
 */
public class Indestructible implements IUnitFeature {

    /**
   * Pole dziedziczone z interface'u IUnitFeature
   */
    private Unit unit;

    /**
   * Getter of the property <tt>unit</tt>
   * 
   * @return Returns the unit.
   */
    public Unit getUnit() {
        return unit;
    }

    /**
   * Setter of the property <tt>unit</tt>
   * 
   * @param unit
   *          The unit to set.
   */
    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    /**
   * Id field for Hibernate to use it as primary key
   */
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return (this.id);
    }

    /**
   * Zwraca klona tego feature'a z ustawionym polem unit na parametr
   * @author Tomek
   * @param unit
   */
    public IUnitFeature clone(Unit unit) {
        Indestructible feature = new Indestructible();
        feature.setUnit(unit);
        feature.setId(HibernateUtil.getDefaultIdFor(feature));
        return feature;
    }
}
