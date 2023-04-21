package org.genxdm.processor.xpath.v10.patterns;

import java.lang.reflect.Array;
import org.genxdm.Model;
import org.genxdm.xpath.v10.ExprContextDynamic;
import org.genxdm.xpath.v10.ExprException;

/**
 * represents an "OR" (union) of match patterns
 */
class AlternativesPattern implements TopLevelPattern {

    private final TopLevelPattern pattern1;

    private final PathPattern pattern2;

    /**
	 * construct with a head pattern1 and tail pattern2
	 */
    AlternativesPattern(final TopLevelPattern pattern1, final PathPattern pattern2) {
        this.pattern1 = pattern1;
        this.pattern2 = pattern2;
    }

    /**
	 * evaluate to a boolean
	 */
    public <N> boolean matches(Model<N> model, final N node, final ExprContextDynamic<N> dynEnv) throws ExprException {
        return pattern1.matches(model, node, dynEnv) || pattern2.matches(model, node, dynEnv);
    }

    /**
	 * @return an array of all the alternative PathPatterns
	 */
    public PathPattern[] getAlternatives() {
        final PathPattern[] tem = pattern1.getAlternatives();
        final PathPattern[] result = (PathPattern[]) Array.newInstance(PathPattern.class, tem.length + 1);
        System.arraycopy(tem, 0, result, 0, tem.length);
        result[result.length - 1] = pattern2;
        return result;
    }
}
