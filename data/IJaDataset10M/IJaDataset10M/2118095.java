package net.community.chest.reflect.common;

import net.community.chest.Triplet;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * <P>Used to specify an attribute accessor proxy as an interface</P>
 * 
 * @author Lyor G.
 * @since Dec 30, 2008 8:54:03 AM
 */
public class AttributeInterfaceSpec extends Triplet<String, Class<?>, Class<?>> {

    public AttributeInterfaceSpec(String name, Class<?> type, Class<?> ifc) {
        super(name, type, ifc);
    }

    public AttributeInterfaceSpec() {
        this(null, null, null);
    }

    public String getName() {
        return getV1();
    }

    public void setName(String n) {
        setV1(n);
    }

    public Class<?> getType() {
        return getV2();
    }

    public void setType(Class<?> t) {
        setV2(t);
    }

    public Class<?> getInterface() {
        return getV3();
    }

    public void setInterface(Class<?> i) {
        setV3(i);
    }
}
