package org.tinymarbles.model.value;

import org.tinymarbles.model.PAttribute;
import org.tinymarbles.model.PObject;
import org.tinymarbles.model.PValue;
import org.tinymarbles.model.PValueAbstractTest;

/**
 * @author kamakura
 * 
 */
public class PFloatTest extends PValueAbstractTest<Float> {

    @Override
    public PValue<Float> create(PAttribute att, PObject owner) {
        return new PFloat(att, owner);
    }

    @Override
    public Class<PFloat> getValueClass() {
        return PFloat.class;
    }

    @Override
    public Float createDefaultContent() {
        return Float.valueOf(60f);
    }

    @Override
    public Float[] createComparableContent() {
        return new Float[] { 5.999999f, 6.1f };
    }
}
