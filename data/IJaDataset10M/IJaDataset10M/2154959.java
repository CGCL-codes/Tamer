package oopex.openjpa2.jpa2.relationships.model.units;

import java.math.BigDecimal;
import javax.persistence.Embeddable;

@Embeddable
public class Length {

    public enum Unit {

        mm, cm, m, km
    }

    private Unit unit;

    private BigDecimal value;

    public Length(BigDecimal value, Unit unit) {
        this.value = value;
        this.unit = unit;
    }

    public Unit getUnit() {
        return unit;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", this.value, this.unit);
    }
}
