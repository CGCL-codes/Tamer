package org.ztemplates.property.deprecated;

@Deprecated
public class ZIntListProperty extends ZListProperty<Integer> {

    public ZIntListProperty(String separator) {
        super(separator);
    }

    public ZIntListProperty() {
    }

    @Override
    protected Integer parseListElement(String formattedValue) throws Exception {
        Integer i = Integer.valueOf(formattedValue);
        return i;
    }

    @Override
    protected String formatListElement(Integer obj) {
        return obj.toString();
    }
}
