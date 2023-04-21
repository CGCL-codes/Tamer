package com.google.gwt.inject.rebind.binding;

import com.google.gwt.dev.util.Preconditions;
import com.google.gwt.inject.rebind.reflect.NoSourceNameException;
import com.google.gwt.inject.rebind.reflect.ReflectUtil;
import com.google.gwt.inject.rebind.util.InjectorMethod;
import com.google.gwt.inject.rebind.util.NameGenerator;
import com.google.gwt.inject.rebind.util.SourceSnippet;
import com.google.gwt.inject.rebind.util.SourceSnippetBuilder;
import com.google.gwt.inject.rebind.util.SourceSnippets;
import com.google.inject.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Binding implementation that replaces one type with another.
 */
public class BindClassBinding extends AbstractBinding implements Binding {

    private final Key<?> sourceClassKey;

    private final Key<?> boundClassKey;

    BindClassBinding(Key<?> boundClassKey, Key<?> sourceClassKey, Context context) {
        super(context, sourceClassKey);
        this.boundClassKey = Preconditions.checkNotNull(boundClassKey);
        this.sourceClassKey = Preconditions.checkNotNull(sourceClassKey);
    }

    public SourceSnippet getCreationStatements(NameGenerator nameGenerator, List<InjectorMethod> methodsOutput) throws NoSourceNameException {
        String type = ReflectUtil.getSourceName(sourceClassKey.getTypeLiteral());
        return new SourceSnippetBuilder().append(type).append(" result = ").append(SourceSnippets.callGetter(boundClassKey)).append(";").build();
    }

    public Collection<Dependency> getDependencies() {
        Context context = getContext();
        Collection<Dependency> dependencies = new ArrayList<Dependency>();
        dependencies.add(new Dependency(Dependency.GINJECTOR, sourceClassKey, context));
        dependencies.add(new Dependency(sourceClassKey, boundClassKey, context));
        return dependencies;
    }
}
