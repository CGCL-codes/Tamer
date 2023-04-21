package nl.jqno.equalsverifier;

import org.junit.Test;

public class WithPrefabValuesTest {

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionOtherClass() {
        EqualsVerifier.forClass(WithPrefabValuesTest.class).withPrefabValues(null, "red", "black");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionRed() {
        EqualsVerifier.forClass(WithPrefabValuesTest.class).withPrefabValues(String.class, null, "black");
    }

    @Test(expected = NullPointerException.class)
    public void nullPointerExceptionBlack() {
        EqualsVerifier.forClass(WithPrefabValuesTest.class).withPrefabValues(String.class, "red", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentExceptionEqualParameters() {
        EqualsVerifier.forClass(WithPrefabValuesTest.class).withPrefabValues(String.class, "red", "red");
    }
}
