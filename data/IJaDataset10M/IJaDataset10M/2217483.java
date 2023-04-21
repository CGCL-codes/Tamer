package fr.thomascoffin.mocaftest;

import fr.thomascoffin.mocaf.NullableComparableComparator;
import fr.thomascoffin.mocaf.NullableComparator;
import fr.thomascoffin.mocaf.NullableComparatorWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

/**
 * This class is both a test of the NullableComparator system (NullableComparableComparator and NullableComparatorWrapper
 * are tested as well), and an example of how to use this system.
 * <p/>
 * <p/>
 * This class is released by Thomas Coffin (thomas.coffin@gmail.com) under the <a href="http://www.gnu.org/copyleft/lesser.html" target="_blank">LGPL License</a>
 * as a component of the <a href="http://code.google.com/p/mocaf" target="_blank">mocaf project</a>
 * <p/>
 * (c) Thomas Coffin 2008.
 */
public class NullableComparatorTest {

    private final List<String> _toSort;

    private final List<String> _caseSensitiveOrder;

    private final List<String> _ignoreCaseOrder;

    private final List<String> _reverseCaseSensitiveOrderNullFirst;

    private final List<String> _caseSensitiveOrderNullAtEnd;

    public NullableComparatorTest() {
        _toSort = new ArrayList<String>();
        _caseSensitiveOrder = new ArrayList<String>();
        _ignoreCaseOrder = new ArrayList<String>();
        _reverseCaseSensitiveOrderNullFirst = new ArrayList<String>();
        _caseSensitiveOrderNullAtEnd = new ArrayList<String>();
    }

    @Before
    public void setUp() {
        _toSort.clear();
        _toSort.addAll(Arrays.asList("String", "Integer", null, "int", "Double", "double", null));
        _caseSensitiveOrder.clear();
        _caseSensitiveOrder.addAll(Arrays.asList(null, null, "Double", "Integer", "String", "double", "int"));
        _caseSensitiveOrderNullAtEnd.clear();
        _caseSensitiveOrderNullAtEnd.addAll(Arrays.asList("Double", "Integer", "String", "double", "int", null, null));
        _ignoreCaseOrder.clear();
        _ignoreCaseOrder.addAll(Arrays.asList(null, null, "Double", "double", "int", "Integer", "String"));
        _reverseCaseSensitiveOrderNullFirst.clear();
        _reverseCaseSensitiveOrderNullFirst.addAll(Arrays.asList(null, null, "int", "double", "String", "Integer", "Double"));
    }

    private void checkSort(Comparator<String> stringComparator, List<String> expectedResult) {
        Collections.sort(_toSort, stringComparator);
        Assert.assertEquals(expectedResult, _toSort);
    }

    @Test
    public void testDefaultNullableComparator() {
        NullableComparator<String> stringComparator = new NullableComparator<String>() {

            protected int compareNotNull(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        checkSort(stringComparator, _caseSensitiveOrder);
    }

    @Test
    public void testNullLastNullableComparator() {
        NullableComparator<String> nullAtEndComparator = new NullableComparator<String>(false) {

            protected int compareNotNull(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        checkSort(nullAtEndComparator, _caseSensitiveOrderNullAtEnd);
    }

    @Test
    public void testNullableComparableComparator() {
        NullableComparableComparator<String> nullableComparableComparator = new NullableComparableComparator<String>();
        checkSort(nullableComparableComparator, _caseSensitiveOrder);
    }

    @Test
    public void testNullableComparatorWrapper() {
        Comparator<String> ignoreCaseComparator = new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        };
        NullableComparatorWrapper<String> nullableIgnoreCase = new NullableComparatorWrapper<String>(ignoreCaseComparator);
        checkSort(nullableIgnoreCase, _ignoreCaseOrder);
    }

    @Test
    public void testNullableComparatorWrapperNullFirst() {
        Comparator<String> reverseComparator = new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        };
        NullableComparatorWrapper<String> nullableReverse = new NullableComparatorWrapper<String>(reverseComparator, true);
        checkSort(nullableReverse, _reverseCaseSensitiveOrderNullFirst);
    }

    @Test
    public void testNotNullableComparatorFails() {
        Comparator<String> notNullableComparator = new Comparator<String>() {

            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        try {
            checkSort(notNullableComparator, _caseSensitiveOrder);
            Assert.fail("A null pointer exception must occur when using standard comparator on null values");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testEqualsAndBothEqualsNullValuesBehaviour() {
        String notNullString = "not null String";
        String notNullStringCopy = new String(notNullString);
        String nullString = null;
        try {
            Assert.assertFalse("A null value must not be considered equal to a not null value by equals()", NullableComparator.equals(nullString, notNullString));
            Assert.assertFalse("A null value must not be considered equal to a not null value by equals()", NullableComparator.equals(notNullString, nullString));
            Assert.assertTrue("Null values must be considered equal to each other by equals()", NullableComparator.equals(nullString, nullString));
            Assert.assertTrue("Two different instances of the same String must be considered equal to each other by equals()", NullableComparator.equals(notNullString, notNullStringCopy));
        } catch (NullPointerException npe) {
            Assert.fail("A null pointer exception should not occur, since NullableComparator.equals() is null-compliant");
        }
        try {
            Assert.assertFalse("A null value must not be considered equal to a not null value by bothEquals()", NullableComparator.equals(nullString, notNullString));
            Assert.assertFalse("A null value must not be considered equal to a not null value by bothEquals()", NullableComparator.equals(notNullString, nullString));
            Assert.assertTrue("Null values must be considered equal to each other by bothEquals()", NullableComparator.equals(nullString, nullString));
            Assert.assertTrue("Two different instances of the same String must be considered equal to each other by bothEquals()", NullableComparator.equals(notNullString, notNullStringCopy));
        } catch (NullPointerException npe) {
            Assert.fail("A null pointer exception should not occur, since NullableComparator.bothEquals() is null-compliant");
        }
    }

    @Test
    public void testBothEqualsSpecificFeature() {
        Name name = new Name("Coffin");
        NameAndFirstName nameAndFirstName = new NameAndFirstName("Coffin", "Thomas");
        Assert.assertTrue("Simple equals should not check if o2.equals(o1)", NullableComparator.equals(name, nameAndFirstName));
        Assert.assertFalse("bothEquals should check also if o2.equals(o1)", NullableComparator.bothEquals(name, nameAndFirstName));
        Assert.assertTrue("bothEquals basic error : o1==o2 (both are Name), and should be considered equal to each other", NullableComparator.bothEquals(name, name));
        Assert.assertTrue("bothEquals basic error : o1==o2 (both are NameAndFirstName), and should be considered equal to each other", NullableComparator.bothEquals(nameAndFirstName, nameAndFirstName));
    }

    private class Name {

        private final String _name;

        public Name(String name) {
            _name = name;
        }

        public boolean equals(Object o) {
            if (o instanceof Name) {
                Name name = (Name) o;
                return _name.equals(name._name);
            } else {
                return false;
            }
        }
    }

    private class NameAndFirstName extends Name {

        private final String _firstName;

        public NameAndFirstName(String name, String firstName) {
            super(name);
            _firstName = firstName;
        }

        public boolean equals(Object o) {
            if (o instanceof NameAndFirstName) {
                NameAndFirstName nameAndFirstName = (NameAndFirstName) o;
                return super.equals(o) && _firstName.equals(nameAndFirstName._firstName);
            } else {
                return false;
            }
        }
    }
}
