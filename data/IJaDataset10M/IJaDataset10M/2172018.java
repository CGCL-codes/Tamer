package no.balder.rules.predicate;

import static org.junit.Assert.*;
import org.junit.Test;

public class ToStringPredicateVisitorTest {

    @Test
    public void testToStringPredicate() {
        Predicate predicate = new EqualsPredicate(1, 1).and(new EqualsPredicate(1, 2)).or(new EqualsPredicate("$a", 2)).and(new NotPredicate(new EqualsPredicate("V1", "V2")));
        ToStringPredicateVisitor toStringPredicateVisitor = new ToStringPredicateVisitor();
        String s = toStringPredicateVisitor.toString(predicate);
        assertNotNull(s);
        System.out.println(s);
        assertEquals(" 1 == 1 AND 1 == 2 OR $a == 2 AND NOT V1 == V2", s);
    }
}
