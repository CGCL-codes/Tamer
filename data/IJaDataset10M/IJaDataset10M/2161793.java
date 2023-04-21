package net.sf.gluent.doc.fluentpatterns.immutableselfbuilder;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ImmutableSelfBuilderTest {

    @Test
    public void testImmutableSelfBuildersAreProtectedFromEachOther() {
        Person somePersonWith = Person.with().firstName("defaultFirstName").lastName("defaultLastName").birthYear(1900);
        Person someMeikalainenWith = somePersonWith.lastName("Meikäläinen");
        Person mattiMeikalainen = someMeikalainenWith.firstName("Matti").birthYear(1973);
        Person kalleKustaa = somePersonWith.firstName("Kalle").lastName("Kustaa");
        assertEquals("Matti Meikäläinen 1973", mattiMeikalainen.toString());
        assertEquals("Kalle Kustaa 1900", kalleKustaa.toString());
    }
}
