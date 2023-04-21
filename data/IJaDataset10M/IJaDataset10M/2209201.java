package com.googlecode.pinthura.traverser;

import com.googlecode.pinthura.traverser.collection.CollectionElementHandler;
import com.googlecode.pinthura.traverser.collection.CollectionElementWithIndexHandler;
import com.googlecode.pinthura.traverser.collection.CollectionElementWithResultHandler;
import java.util.Collection;

public interface CollectionTraverser {

    <Input, Target, Output> Output forEach(Collection<? extends Input> collection, String path, CollectionElementHandler<Target, Output> handler);

    <Input, Output> Output forEach(Collection<? extends Input> collection, CollectionElementHandler<Input, Output> handler);

    <Input, Output> Output forEachWithIndex(Collection<? extends Input> collection, CollectionElementWithIndexHandler<Input, Output> handler);

    <Input, Target, Output> Output forEachWithIndex(Collection<? extends Input> collection, String path, CollectionElementWithIndexHandler<Target, Output> handler);

    <Input, Output> Output forEachWithResult(Collection<? extends Input> collection, CollectionElementWithResultHandler<Input, Output> handler, Output prevResult);

    <Input, Target, Output> Output forEachWithResult(Collection<? extends Input> collection, String path, CollectionElementWithResultHandler<Target, Output> handler, Output prevResult);
}
