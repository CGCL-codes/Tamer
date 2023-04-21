package lv.odylab.evedb.domain;

import org.junit.Test;
import java.util.Arrays;
import java.util.Comparator;
import static org.junit.Assert.assertTrue;

public class InvTypeNameComparatorTest {

    private Comparator<InvType> comparator = new InvTypeNameComparator();

    @Test
    public void testCompare_Basic() throws Exception {
        InvType invType1 = new InvType();
        invType1.setTypeNameTokens(Arrays.asList("EXPANDED", "CARGOHOLD", "I"));
        InvType invType2 = new InvType();
        invType2.setTypeNameTokens(Arrays.asList("EXPANDED", "CARGOHOLD", "I", "BLUEPRINT"));
        InvType invType3 = new InvType();
        invType3.setTypeNameTokens(Arrays.asList("EXPANDED", "CARGOHOLD", "II"));
        InvType invType4 = new InvType();
        invType4.setTypeNameTokens(Arrays.asList("EXPANDED", "CARGOHOLD", "II", "BLUEPRINT"));
        assertTrue(comparator.compare(invType1, invType2) < 0);
        assertTrue(comparator.compare(invType2, invType3) < 0);
        assertTrue(comparator.compare(invType3, invType4) < 0);
        assertTrue(comparator.compare(invType2, invType1) > 0);
        assertTrue(comparator.compare(invType3, invType2) > 0);
        assertTrue(comparator.compare(invType4, invType3) > 0);
        assertTrue(comparator.compare(invType1, invType1) == 0);
        assertTrue(comparator.compare(invType2, invType2) == 0);
        assertTrue(comparator.compare(invType3, invType3) == 0);
        assertTrue(comparator.compare(invType4, invType4) == 0);
    }

    @Test
    public void testCompare_WordOrder() throws Exception {
        InvType invType1 = new InvType();
        invType1.setTypeNameTokens(Arrays.asList("A", "B"));
        InvType invType2 = new InvType();
        invType2.setTypeNameTokens(Arrays.asList("A", "C", "B"));
        assertTrue(comparator.compare(invType1, invType2) < 0);
        assertTrue(comparator.compare(invType2, invType1) > 0);
        assertTrue(comparator.compare(invType1, invType1) == 0);
        assertTrue(comparator.compare(invType2, invType2) == 0);
    }

    @Test
    public void testCompare_WordOrder_Complex() throws Exception {
        InvType invType1 = new InvType();
        invType1.setTypeNameTokens(Arrays.asList("A", "B"));
        InvType invType2 = new InvType();
        invType2.setTypeNameTokens(Arrays.asList("A", "B", "C"));
        assertTrue(comparator.compare(invType1, invType2) < 0);
        assertTrue(comparator.compare(invType2, invType1) > 0);
        assertTrue(comparator.compare(invType1, invType1) == 0);
        assertTrue(comparator.compare(invType2, invType2) == 0);
    }
}
