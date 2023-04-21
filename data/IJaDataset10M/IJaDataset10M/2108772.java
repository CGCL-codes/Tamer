package net.sourceforge.deco.parsers.utils;

import org.objectweb.asm.Type;

/** 
 * Block all primitive type.
 */
public class BasicTypeChecker {

    public boolean block(Type depClass) {
        return depClass.getSort() != Type.OBJECT;
    }
}
