package net.sf.dozer.util.mapping.vo;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class CustomDoubleObject extends BaseTestObject implements CustomDoubleObjectIF {

    private double theDouble;

    private String name;

    public double getTheDouble() {
        return theDouble;
    }

    public void setTheDouble(double theDouble) {
        this.theDouble = theDouble;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
