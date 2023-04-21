package br.com.jmemory.criteria;

public class EqualToCriteria<B, V> extends Criteria<B, V> {

    public EqualToCriteria(String interfaceName, String property, V value) {
        super(interfaceName, property, value);
    }

    @Override
    protected boolean checkCriteria(V propValue) {
        return value.equals(propValue);
    }
}
