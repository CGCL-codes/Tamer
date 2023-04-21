package org.helianto.core.filter;

import org.helianto.core.Identity;
import org.helianto.core.PersonalAddress;
import org.helianto.core.criteria.OrmCriteriaBuilder;
import org.helianto.core.def.AddressType;
import org.helianto.core.filter.base.AbstractPersonalFilterAdapter;

/**
 * Personal address filter adapter.
 * 
 * @author Mauricio Fernandes de Castro
 * @deprecated
 * @see PersonalAddressFormFilterAdapter
 */
public class PersonalAddressFilterAdapter extends AbstractPersonalFilterAdapter<PersonalAddress> {

    private static final long serialVersionUID = 1L;

    /**
	 * Default constructor.
	 * 
	 * @param form
	 */
    public PersonalAddressFilterAdapter(PersonalAddress form) {
        super(form);
    }

    /**
	 * Key constructor.
	 * 
	 * @param identity
	 * @param addressType
	 */
    public PersonalAddressFilterAdapter(Identity identity, AddressType addressType) {
        super(new PersonalAddress(identity, addressType));
    }

    public void reset() {
        getForm().reset();
    }

    @Override
    public boolean isSelection() {
        return getForm().getAddressType() != ' ';
    }

    @Override
    protected void doSelect(OrmCriteriaBuilder mainCriteriaBuilder) {
        appendEqualFilter("addressType", getForm().getAddressType(), mainCriteriaBuilder);
    }

    @Override
    public void doFilter(OrmCriteriaBuilder mainCriteriaBuilder) {
    }

    @Override
    public String getOrderByString() {
        return "addressType";
    }
}
