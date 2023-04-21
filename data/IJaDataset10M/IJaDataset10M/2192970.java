package com.mycompany.custmgmt.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import org.nakedobjects.nof.reflect.javax.exceptions.HiddenDeclarativelyException;
import org.nakedobjects.nof.reflect.javax.exceptions.HiddenImperativelyException;
import org.nakedobjects.nof.reflect.javax.exceptions.HiddenNotAuthorizedException;

public class MemberHiddenTest extends AbstractTest {

    public void testIfValueHiddenImperativelyThenWhenModifyThrowsException() {
        String[] values = new String[] { "Dick", null };
        for (String value : values) {
            custRP.hideFirstName = true;
            try {
                proxiedCustRP.setFirstName(value);
                fail("Should have thrown exception");
            } catch (HiddenImperativelyException e) {
                assertThat(e.getName(), equalTo("First Name"));
            }
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'First Name' property is currently hidden, it cannot be modified.\n" + "Because the 'First Name' property is currently hidden, it cannot be cleared.\n"));
    }

    public void testIfValueHiddenImperativelyThenWhenReadThrowsException() {
        custRP.hideFirstName = true;
        try {
            proxiedCustRP.getFirstName();
            fail("Should have thrown exception");
        } catch (HiddenImperativelyException e) {
            assertThat(e.getName(), equalTo("First Name"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'First Name' property is currently hidden, it cannot be read.\n"));
    }

    public void testIfAssociationHiddenImperativelyThenWhenModifyThrowsException() {
        custRP.hideCountryOfBirth = true;
        Country[] values = new Country[] { countryUSA, null };
        for (Country value : values) {
            try {
                proxiedCustRP.setCountryOfBirth(value);
                fail("Should have thrown exception");
            } catch (HiddenImperativelyException e) {
                assertThat(e.getName(), equalTo("Country Of Birth"));
            }
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Country Of Birth' property is currently hidden, it cannot be modified.\n" + "Because the 'Country Of Birth' property is currently hidden, it cannot be cleared.\n"));
    }

    public void testIfAssociationHiddenImperativelyThenWhenReadThrowsException() {
        custRP.hideCountryOfBirth = true;
        try {
            proxiedCustRP.getCountryOfBirth();
            fail("Should have thrown exception");
        } catch (HiddenImperativelyException e) {
            assertThat(e.getName(), equalTo("Country Of Birth"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Country Of Birth' property is currently hidden, it cannot be read.\n"));
    }

    public void testIfCollectionHiddenImperativelyThenAddToThrowsException() {
        custRP.hideVisitedCountries = true;
        try {
            proxiedCustRP.addToVisitedCountries(countryGBR);
            fail("Should have thrown exception");
        } catch (HiddenImperativelyException e) {
            assertThat(e.getName(), equalTo("Visited Countries"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Visited Countries' collection is currently hidden, it cannot be added to.\n"));
    }

    public void testIfCollectionHiddenImperativelyThenRemoveFromThrowsException() {
        custRP.hideVisitedCountries = true;
        custRP.addToVisitedCountries(countryGBR);
        try {
            proxiedCustRP.removeFromVisitedCountries(countryGBR);
            fail("Should have thrown exception");
        } catch (HiddenImperativelyException e) {
            assertThat(e.getName(), equalTo("Visited Countries"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Visited Countries' collection is currently hidden, it cannot be removed from.\n"));
    }

    public void testIfCollectionHiddenImperativelyThenWhenReadThrowsException() {
        custRP.hideVisitedCountries = true;
        custRP.addToVisitedCountries(countryGBR);
        try {
            proxiedCustRP.getVisitedCountries();
            fail("Should have thrown exception");
        } catch (HiddenImperativelyException e) {
            assertThat(e.getName(), equalTo("Visited Countries"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Visited Countries' collection is currently hidden, it cannot be read.\n"));
    }

    public void testIfActionHiddenImperativelyThenThrowsException() {
        custRP.hidePlaceOrder = true;
        try {
            proxiedCustRP.placeOrder(product355, 3);
            fail("Should have thrown exception");
        } catch (HiddenImperativelyException e) {
            assertThat(e.getName(), equalTo("Place Order"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Place Order' action is currently hidden, it cannot be invoked.\n"));
    }

    public void testIfValueHiddenDeclarativelyThenWhenModifyThrowsException() {
        String[] values = new String[] { "Dick", null };
        for (String value : values) {
            try {
                proxiedCustRP.setAlwaysHiddenValue(value);
                fail("Should have thrown exception");
            } catch (HiddenDeclarativelyException e) {
                assertThat(e.getName(), equalTo("Always Hidden Value"));
            }
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Value' property is declared as being hidden, it cannot be modified.\n" + "Because the 'Always Hidden Value' property is declared as being hidden, it cannot be cleared.\n"));
    }

    public void testIfValueHiddenDeclarativelyThenWhenReadThrowsException() {
        try {
            proxiedCustRP.getAlwaysHiddenValue();
            fail("Should have thrown exception");
        } catch (HiddenDeclarativelyException e) {
            assertThat(e.getName(), equalTo("Always Hidden Value"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Value' property is declared as being hidden, it cannot be read.\n"));
    }

    public void testIfAssociationHiddenDeclarativelyThenWhenModifyThrowsException() {
        Country[] values = new Country[] { countryUSA, null };
        for (Country value : values) {
            try {
                proxiedCustRP.setAlwaysHiddenAssociation(value);
                fail("Should have thrown exception");
            } catch (HiddenDeclarativelyException e) {
                assertThat(e.getName(), equalTo("Always Hidden Association"));
            }
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Association' property is declared as being hidden, it cannot be modified.\n" + "Because the 'Always Hidden Association' property is declared as being hidden, it cannot be cleared.\n"));
    }

    public void testIfAssociationHiddenDeclarativelyThenWhenReadThrowsException() {
        try {
            proxiedCustRP.getAlwaysHiddenAssociation();
            fail("Should have thrown exception");
        } catch (HiddenDeclarativelyException e) {
            assertThat(e.getName(), equalTo("Always Hidden Association"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Association' property is declared as being hidden, it cannot be read.\n"));
    }

    public void testIfCollectionHiddenDeclarativelyThenAddToThrowsException() {
        try {
            proxiedCustRP.addToAlwaysHiddenCollection(countryUSA);
            fail("Should have thrown exception");
        } catch (HiddenDeclarativelyException e) {
            assertThat(e.getName(), equalTo("Always Hidden Collection"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Collection' collection is declared as being hidden, it cannot be added to.\n"));
    }

    public void testIfCollectionHiddenDeclarativelyThenRemoveFromThrowsException() {
        custRP.removeFromAlwaysHiddenCollection(countryUSA);
        try {
            proxiedCustRP.removeFromAlwaysHiddenCollection(countryUSA);
            fail("Should have thrown exception");
        } catch (HiddenDeclarativelyException e) {
            assertThat(e.getName(), equalTo("Always Hidden Collection"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Collection' collection is declared as being hidden, it cannot be removed from.\n"));
    }

    public void testIfCollectionHiddenDeclarativelyThenWhenReadThrowsException() {
        try {
            proxiedCustRP.getAlwaysHiddenCollection();
            fail("Should have thrown exception");
        } catch (HiddenDeclarativelyException e) {
            assertThat(e.getName(), equalTo("Always Hidden Collection"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Always Hidden Collection' collection is declared as being hidden, it cannot be read.\n"));
    }

    /**
     * TODO: commented out because declaratively hidden actions are
     * currently ignored by the reflector (hence there is no action
     * to invoke).
     */
    public void xxxtestIfActionHiddenDeclarativelyThenThrowsException() {
        try {
            proxiedCustRP.alwaysHiddenAction();
            fail("Should have thrown exception");
        } catch (HiddenDeclarativelyException e) {
            assertThat(e.getName(), equalTo("Always Hidden Action"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo(""));
    }

    public void testIfValueHiddenNotAuthorizedThenWhenModifyThrowsException() {
        String[] values = new String[] { "Dick", null };
        for (String value : values) {
            try {
                proxiedCustRP.setSessionHiddenValue(value);
                fail("Should have thrown exception");
            } catch (HiddenNotAuthorizedException e) {
                assertThat(e.getName(), equalTo("Session Hidden Value"));
            }
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Value' property is hidden for this user, it cannot be modified.\n" + "Because the 'Session Hidden Value' property is hidden for this user, it cannot be cleared.\n"));
    }

    public void testIfValueHiddenNotAuthorizedThenWhenReadThrowsException() {
        try {
            proxiedCustRP.getSessionHiddenValue();
            fail("Should have thrown exception");
        } catch (HiddenNotAuthorizedException e) {
            assertThat(e.getName(), equalTo("Session Hidden Value"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Value' property is hidden for this user, it cannot be read.\n"));
    }

    public void testIfAssociationHiddenNotAuthorizedThenWhenModifyThrowsException() {
        Country[] values = new Country[] { countryUSA, null };
        for (Country value : values) {
            try {
                proxiedCustRP.setSessionHiddenAssociation(value);
                fail("Should have thrown exception");
            } catch (HiddenNotAuthorizedException e) {
                assertThat(e.getName(), equalTo("Session Hidden Association"));
            }
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Association' property is hidden for this user, it cannot be modified.\n" + "Because the 'Session Hidden Association' property is hidden for this user, it cannot be cleared.\n"));
    }

    public void testIfAssociationHiddenNotAuthorizedThenWhenReadThrowsException() {
        try {
            proxiedCustRP.getSessionHiddenAssociation();
            fail("Should have thrown exception");
        } catch (HiddenNotAuthorizedException e) {
            assertThat(e.getName(), equalTo("Session Hidden Association"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Association' property is hidden for this user, it cannot be read.\n"));
    }

    public void testIfCollectionHiddenNotAuthorizedThenAddToThrowsException() {
        try {
            proxiedCustRP.addToSessionHiddenCollection(countryUSA);
            fail("Should have thrown exception");
        } catch (HiddenNotAuthorizedException e) {
            assertThat(e.getName(), equalTo("Session Hidden Collection"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Collection' collection is hidden for this user, it cannot be added to.\n"));
    }

    public void testIfCollectionHiddenNotAuthorizedThenRemoveFromThrowsException() {
        custRP.addToSessionHiddenCollection(countryUSA);
        try {
            proxiedCustRP.removeFromSessionHiddenCollection(countryUSA);
            fail("Should have thrown exception");
        } catch (HiddenNotAuthorizedException e) {
            assertThat(e.getName(), equalTo("Session Hidden Collection"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Collection' collection is hidden for this user, it cannot be removed from.\n"));
    }

    public void testIfCollectionHiddenNotAuthorizedThenWhenReadThrowsException() {
        try {
            proxiedCustRP.getSessionHiddenCollection();
            fail("Should have thrown exception");
        } catch (HiddenNotAuthorizedException e) {
            assertThat(e.getName(), equalTo("Session Hidden Collection"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Collection' collection is hidden for this user, it cannot be read.\n"));
    }

    public void testIfActionHiddenNotAuthorizedThenThrowsException() {
        try {
            proxiedCustRP.sessionHiddenAction();
            fail("Should have thrown exception");
        } catch (HiddenNotAuthorizedException e) {
            assertThat(e.getName(), equalTo("Session Hidden Action"));
        }
        String documentText = getInMemoryDocumentor().toString();
        assertThat(documentText, equalTo("Because the 'Session Hidden Action' action is hidden for this user, it cannot be invoked.\n"));
    }
}
