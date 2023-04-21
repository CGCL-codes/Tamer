package beans;

import java.math.BigDecimal;

public class Event2 {

    private BigDecimal id;

    private String name;

    private String obs;

    @Override
    public String toString() {
        return String.format("[%s %s %s]", id, name, obs);
    }
}
