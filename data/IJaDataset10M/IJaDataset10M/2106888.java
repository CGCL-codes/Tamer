package astcentric.structure.vl.basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import astcentric.structure.basic.Node;
import astcentric.structure.bl.Data;
import astcentric.structure.bl.DataFactory;
import astcentric.structure.query.ExtendedNodeCollection;
import astcentric.structure.query.FixedNodeCollection;
import astcentric.structure.regex.Matcher;
import astcentric.structure.regex.MatcherConverter;
import astcentric.structure.regex.MatcherHandler;
import astcentric.structure.regex.RegexEvaluator;
import astcentric.structure.regex.Result;

final class NodeRegexEvaluator implements RegexEvaluator<Node> {

    private final class IterableAdapter implements Iterable<Data> {

        private final Iterable<Node> _items;

        private IterableAdapter(Iterable<Node> items) {
            _items = items;
        }

        public Iterator<Data> iterator() {
            return new Iterator<Data>() {

                final Iterator<Node> iterator = _items.iterator();

                public void remove() {
                    iterator.remove();
                }

                public Data next() {
                    Node node = iterator.next();
                    return _dataFactory.create(node);
                }

                public boolean hasNext() {
                    return iterator.hasNext();
                }
            };
        }
    }

    private final RegexEvaluator<Data> _regexEvaluator;

    private final DataFactory<Object> _dataFactory;

    NodeRegexEvaluator(RegexEvaluator<Data> regexEvaluator, DataFactory<Object> dataFactory) {
        _regexEvaluator = regexEvaluator;
        _dataFactory = dataFactory;
    }

    public Result<Node> apply(final Iterable<Node> items) {
        Result<Data> result = _regexEvaluator.apply(new IterableAdapter(items));
        final List<Matcher<Node>> matchers = new ArrayList<Matcher<Node>>();
        result.traverseMatchers(new MatcherHandler<Data>() {

            public void handle(Matcher<Data> matcher) {
                if (matcher instanceof DataMatcher) {
                    Data data = ((DataMatcher) matcher).getData();
                    List<Data> children = data.getChildren();
                    if (children.isEmpty()) {
                        matchers.add(new NodeReferenceMatcher(new FixedNodeCollection(Collections.<Node>emptyList()).createInvertedCollection()));
                    } else {
                        Data child = children.get(0);
                        if (child instanceof NodeCollectionProvider) {
                            NodeCollectionProvider collectionData = (NodeCollectionProvider) child;
                            ExtendedNodeCollection collection = collectionData.getNodeCollection();
                            matchers.add(new NodeReferenceMatcher(collection));
                        }
                    }
                }
            }
        });
        return createResult(result, matchers);
    }

    @SuppressWarnings("unchecked")
    private Result<Node> createResult(Result<Data> result, final List<Matcher<Node>> matchers) {
        Matcher<Node>[] matcherArray = matchers.toArray(new Matcher[0]);
        return new Result<Node>(result.matched(), result.getLength(), matcherArray);
    }

    public <V> RegexEvaluator<V> createCopy(MatcherConverter<Node, V> converter) {
        return null;
    }
}
