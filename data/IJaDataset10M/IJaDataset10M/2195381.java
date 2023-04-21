package com.obsidiandynamics.needle.translator;

import java.lang.reflect.*;
import java.util.*;
import com.obsidiandynamics.needle.*;

/**
 *  Translates to a <code>Set</code>.
 *  
 *  @author Emil Koutanov, Obsidian Dynamics.
 */
public final class SetTranslator extends TranslatorSupport {

    @Override
    public Class<?>[] getSupportedTypes() {
        return new Class[] { Set.class };
    }

    @Override
    public Object translate(String value, Type type, Field field) {
        final Separator separator = field.getAnnotation(Separator.class);
        final String separatorRegex = separator != null ? separator.value() : ",";
        final Type componentType = getComponentType(type, 0);
        final String[] splitValues = value.split(separatorRegex);
        final Set<Object> list = new LinkedHashSet<Object>(splitValues.length * 2);
        for (String splitValue : splitValues) {
            final Object translated = Translators.translate(splitValue, componentType, field);
            if (!list.add(translated)) throw new NeedleException("Duplicate entry '%s'", splitValue);
        }
        return list;
    }
}
