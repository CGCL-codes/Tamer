package org.rubato.math.module.morphism;

import org.rubato.math.module.ModuleElement;
import org.rubato.math.module.ZElement;
import org.rubato.math.module.ZRing;

/**
 * The abstract base class for morphisms in <i>Z</i>.
 * 
 * @author Gérard Milmeister
 */
public abstract class ZAbstractMorphism extends ModuleMorphism {

    public ZAbstractMorphism() {
        super(ZRing.ring, ZRing.ring);
    }

    public ModuleElement map(ModuleElement x) throws MappingException {
        if (getDomain().hasElement(x)) {
            int v = ((ZElement) x.getComponent(0)).getValue();
            return new ZElement(mapValue(v));
        } else {
            throw new MappingException("ZAbstractMorphism.map: ", x, this);
        }
    }

    /**
     * The low-level map method.
     * This must be implemented in subclasses.
     */
    public abstract int mapValue(int x);

    public ModuleMorphism getRingMorphism() {
        return getIdentityMorphism(ZRing.ring);
    }
}
