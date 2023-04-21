package playground.benjamin.emissions.types;

/**
 * @author benjamin
 *
 */
public class HbefaVehicleAttributes {

    private String hbefaTechnology = "average";

    private String hbefaSizeClass = "average";

    private String hbefaEmConcept = "average";

    public HbefaVehicleAttributes() {
    }

    public String getHbefaTechnology() {
        return this.hbefaTechnology;
    }

    public void setHbefaTechnology(String hbefaTechnology) {
        this.hbefaTechnology = hbefaTechnology;
    }

    public String getHbefaSizeClass() {
        return this.hbefaSizeClass;
    }

    public void setHbefaSizeClass(String hbefaSizeClass) {
        this.hbefaSizeClass = hbefaSizeClass;
    }

    public String getHbefaEmConcept() {
        return this.hbefaEmConcept;
    }

    public void setHbefaEmConcept(String hbefaEmConcept) {
        this.hbefaEmConcept = hbefaEmConcept;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HbefaVehicleAttributes)) {
            return false;
        }
        HbefaVehicleAttributes key = (HbefaVehicleAttributes) obj;
        return hbefaTechnology.equals(key.getHbefaTechnology()) && hbefaSizeClass.equals(key.getHbefaSizeClass()) && hbefaEmConcept.equals(key.getHbefaEmConcept());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return hbefaTechnology + "; " + hbefaSizeClass + "; " + hbefaEmConcept;
    }
}
