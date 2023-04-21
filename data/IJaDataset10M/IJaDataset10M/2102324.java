package org.fudaa.dodico.h2d.type;

import org.fudaa.ctulu.CtuluLibString;
import org.fudaa.dodico.commun.DodicoEnumType;
import org.fudaa.dodico.h2d.resource.H2dResource;

/**
 * @author deniger
 * @version $Id: H2dBcType.java,v 1.13 2006-09-19 14:43:25 deniger Exp $
 */
public final class H2dBcType extends DodicoEnumType {

    /**
   * Type mixte, utilise pour les recherches de prop communes.
   */
    public static final H2dBcType MIXTE = new H2dBcType(H2dResource.getS("Mixte"));

    /**
   * Type libre.
   */
    public static final H2dBcType LIBRE = new H2dBcType(H2dResource.getS("Libre"));

    /**
   * Type permanent.
   */
    public static final H2dBcType PERMANENT = new H2dBcType(H2dResource.getS("Permanent"));

    /**
   * Type transitoire.
   */
    public static final H2dBcType TRANSITOIRE = new H2dBcType(H2dResource.getS("Transitoire"));

    public static H2dBcType[] getConstantArray() {
        return new H2dBcType[] { MIXTE, LIBRE, PERMANENT, TRANSITOIRE };
    }

    H2dBcType(final String _nom) {
        super(_nom);
    }

    H2dBcType() {
        super(CtuluLibString.EMPTY_STRING);
    }

    public DodicoEnumType[] getArray() {
        return getConstantArray();
    }
}
