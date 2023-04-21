package net.sourceforge.domian.entity;

import static org.junit.Assert.assertFalse;
import org.junit.Test;
import static net.sourceforge.domian.test.domain.Testdata.maleCustomer;
import static net.sourceforge.domian.test.domain.Testdata.order22;

public class AbstractEntityTest {

    @Test
    public void shouldNotAllowEqualsWhereNotEvenTheTypeIsCorrect() {
        assertFalse(maleCustomer.equals(order22));
    }
}
