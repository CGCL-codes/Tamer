package org.jtools.tmpl.compiler.api;

import java.util.ArrayList;
import java.util.Collection;
import org.jpattern.helper.Helper;
import org.jtools.util.DelegatedCollection;

/**
 * TODO type-description
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class DefinitionCollectionHelper<T> implements Helper<Collection<T>> {

    private Collection<T> definitions = null;

    public DefinitionCollectionHelper<T> addDefinition(T definition) {
        if (definitions == null) definitions = new ArrayList<T>();
        definitions.add(definition);
        return this;
    }

    public Collection<T> toInstance() {
        Collection<T> group = new DelegatedCollection<T>(definitions);
        definitions = null;
        return group;
    }
}
