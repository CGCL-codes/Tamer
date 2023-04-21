package astcentric.structure.validation.basic;

import astcentric.structure.basic.Node;
import astcentric.structure.validation.regex.MatcherFactory;
import astcentric.structure.vl.basic.ChildFeatureMatcher;

public interface ChildFeatureMatcherFactory extends MatcherFactory<Node> {

    public ChildFeatureMatcher create(Node specification);
}
