package net.sf.clairv.common;

/**
 * @author qiuyin
 *
 */
public class BaseEnum {

    private String value;

    protected BaseEnum(String value) {
        this.value = value;
    }

    public boolean equals(Object o) {
        if (o instanceof BaseEnum) {
            return ((BaseEnum) o).value.equals(value);
        }
        return false;
    }

    public String toString() {
        return value;
    }
}
