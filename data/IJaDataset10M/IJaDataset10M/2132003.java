package org.nakedobjects.xat.integ.junit4.interactions;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.nakedobjects.noa.facets.object.validate.ValidateObjectFacet;
import org.nakedobjects.nof.reflect.javax.ViewObject;
import org.nakedobjects.testlib.exceptions.InvalidException;
import org.nakedobjects.xat.integ.junit4.sample.domain.Customer;
import org.junit.Ignore;
import org.junit.Test;

public class SaveObjectsTest extends AbstractTest {

    @Ignore
    @Test
    public void invokingSaveThroughProxyMakesTransientObjectPersistent() {
        final Customer newCustomer = getCustomerRepository().newCustomer();
        assertThat(getDomainObjectContainer().isPersistent(newCustomer), is(false));
        final Customer proxiedNewCustomer = getTestViewer().view(newCustomer);
        final ViewObject<Customer> proxyNewCustomer = (ViewObject<Customer>) proxiedNewCustomer;
        proxyNewCustomer.save();
        assertThat(getDomainObjectContainer().isPersistent(newCustomer), is(true));
    }

    @Ignore
    @Test
    public void invokingSaveOnThroughProxyOnAlreadyPersistedObjectJustUpdatesIt() {
        final ViewObject<Customer> proxyNewCustomer = (ViewObject<Customer>) custRpVO;
        proxyNewCustomer.save();
        assertThat(getDomainObjectContainer().isPersistent(custRpDO), is(true));
    }

    @Ignore
    @Test
    public void whenValidateMethodExistsThenCalledBeforeSave() {
        final Customer newCustomer = getCustomerRepository().newCustomer();
        final Customer proxiedNewCustomer = getTestViewer().view(newCustomer);
        final ViewObject<Customer> proxyNewCustomer = (ViewObject<Customer>) proxiedNewCustomer;
        proxyNewCustomer.save();
        assertThat(newCustomer.validateCalled, is(true));
    }

    @Ignore
    @Test
    public void whenValidateMethodThenCanVetoSave() {
        final Customer newCustomer = getCustomerRepository().newCustomer();
        final Customer proxiedNewCustomer = getTestViewer().view(newCustomer);
        newCustomer.validate = "No shakes";
        final ViewObject<Customer> proxyNewCustomer = (ViewObject<Customer>) proxiedNewCustomer;
        try {
            assertThat(getDomainObjectContainer().isPersistent(newCustomer), is(false));
            proxyNewCustomer.save();
            fail("An InvalidImperativelyException should have been thrown");
        } catch (final InvalidException ex) {
            assertThat(ex.getAdvisorClass(), is(ValidateObjectFacet.class));
            assertThat(getDomainObjectContainer().isPersistent(newCustomer), is(false));
            assertThat(ex.getMessage(), equalTo("No shakes"));
            final String documentText = getDocumentor().toString();
            assertThat(documentText, equalTo("Attempt to save the object; this will be prevented (No shakes).\n"));
        }
    }
}
