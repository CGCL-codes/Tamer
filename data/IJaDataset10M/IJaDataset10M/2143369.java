package funiture.domains.model.bom;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import furniture.core.model.ValueObject;

@Embeddable
public class BarCode implements ValueObject {

    @Column(name = "barcode")
    private String name;

    BarCode() {
    }

    public BarCode(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public static final BarCode EMPTY = new BarCode();
}
