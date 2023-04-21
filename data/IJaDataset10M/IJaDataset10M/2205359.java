package net.ontopia.topicmaps.impl.tmapi2;

import java.math.BigDecimal;
import java.math.BigInteger;
import net.ontopia.infoset.core.LocatorIF;
import net.ontopia.infoset.impl.basic.URILocator;
import net.ontopia.topicmaps.core.DataTypes;
import org.tmapi.core.DatatypeAware;
import org.tmapi.core.Locator;

/**
 * INTERNAL: OKS->TMAPI 2 object wrapper.
 */
public abstract class DatatypeAwareImpl extends ScopedImpl implements DatatypeAware {

    private static final LocatorIF XSD_INT = URILocator.create("http://www.w3.org/2001/XMLSchema#int");

    public DatatypeAwareImpl(TopicMapImpl topicMap) {
        super(topicMap);
    }

    /**
   * Sets the value / datatype pair.
   * 
   * Methods which invoke this method have to ensure that value and datatype
   * is never null.
   */
    protected abstract void setValue(String value, LocatorIF datatype);

    public BigDecimal decimalValue() {
        return new BigDecimal(getValue());
    }

    public float floatValue() {
        return decimalValue().floatValue();
    }

    public int intValue() {
        return decimalValue().intValue();
    }

    public BigInteger integerValue() {
        return decimalValue().toBigInteger();
    }

    public long longValue() {
        return decimalValue().longValue();
    }

    public void setValue(BigDecimal value) {
        Check.valueNotNull(this, value);
        setValue(value.toString(), DataTypes.TYPE_DECIMAL);
    }

    public void setValue(BigInteger value) {
        Check.valueNotNull(this, value);
        setValue(value.toString(), DataTypes.TYPE_INTEGER);
    }

    public void setValue(long value) {
        setValue(Long.toString(value), DataTypes.TYPE_LONG);
    }

    public void setValue(float value) {
        setValue(Float.toString(value), DataTypes.TYPE_FLOAT);
    }

    public void setValue(int value) {
        setValue(Integer.toString(value), XSD_INT);
    }

    public void setValue(String value, Locator datatype) {
        Check.valueNotNull(this, value, datatype);
        setValue(value, topicMap.unwrapLocator(datatype));
    }
}
