package net.ar.guia.helpers;

import java.lang.reflect.*;

public class PropertySetEvent extends PropertyAccessedEvent {

    public PropertySetEvent(Object aSource, Method aMethod, Object[] anArguments, String aPropertyName) {
        super(aSource, aMethod, anArguments, aPropertyName);
    }
}
