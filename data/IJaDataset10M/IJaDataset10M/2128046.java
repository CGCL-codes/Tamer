package net.sf.dozer.util.mapping.vo;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class WeirdGetterPrime2 extends BaseTestObject {

    private String value;

    public WeirdGetterPrime2() {
    }

    ;

    public String buildValue() {
        return value;
    }

    public void placeValue(String value) {
        this.value = value;
    }
}
