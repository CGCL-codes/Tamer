package net.sf.ezmorph.primitive;

import net.sf.ezmorph.Morpher;

/**
 * Base class for primitive value conversion.<br>
 * 
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public abstract class AbstractPrimitiveMorpher implements Morpher {

    private boolean useDefault = false;

    public AbstractPrimitiveMorpher() {
    }

    public AbstractPrimitiveMorpher(boolean useDefault) {
        this.useDefault = useDefault;
    }

    /**
    * Returns if this morpher will use a default value.
    */
    public boolean isUseDefault() {
        return useDefault;
    }

    /**
    * Returns true if the Morpher supports conversion from this Class.<br>
    * Supports any type that is not an Array.
    * 
    * @param clazz the source Class
    * @return true if clazz is supported by this morpher, false otherwise.
    */
    public boolean supports(Class clazz) {
        return !clazz.isArray();
    }
}
