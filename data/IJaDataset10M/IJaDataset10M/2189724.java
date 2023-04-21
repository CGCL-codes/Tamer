package net.sourceforge.domian.predicate;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.Validate;
import net.sourceforge.domian.specification.Specification;

/**
 * A {@link Specification} disguised as a Jakarta Commons Collection Predicate.
 *
 * @author Eirik Torske
 * @see <a href="http://commons.apache.org/collections/api-release/org/apache/commons/collections/Predicate.html"><code>org.apache.commons.collections.Predicate</code></a>
 * @since 0.3
 */
public class SpecificationPredicate implements Predicate {

    Specification specification;

    public static Predicate getPredicate(final Specification specification) {
        Validate.notNull(specification, "Specification parameter cannot be null");
        return new SpecificationPredicate(specification);
    }

    SpecificationPredicate(final Specification specification) {
        this.specification = specification;
    }

    @SuppressWarnings("unchecked")
    public boolean evaluate(final Object object) {
        return this.specification.isSatisfiedBy(object);
    }
}
