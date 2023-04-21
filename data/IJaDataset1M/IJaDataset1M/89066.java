package com.idna.trace.domain.face;

import net.icdpublishing.dream.domain.DomainField;
import net.icdpublishing.dream.domain.FieldCategory;
import net.icdpublishing.dream.domain.proxying.DomainFieldGetter;
import net.icdpublishing.dream.domain.proxying.DomainFieldSetter;
import net.icdpublishing.dream.domain.proxying.SubDomainObjectGetter;
import net.icdpublishing.dream.domain.proxying.SubDomainObjectSetter;

public interface Marriage {

    @SubDomainObjectGetter(category = FieldCategory.ADDRESS)
    Address getAddress();

    @DomainFieldGetter(field = DomainField.REG_DATE_MARRIAGE)
    String getRegistrationDate();

    @DomainFieldGetter(field = DomainField.DISTRICT)
    String getRegistrationDistrict();

    @DomainFieldGetter(field = DomainField.SPOUSE)
    String getSpouse();

    @DomainFieldGetter(field = DomainField.SPOUSE_FORENAME)
    String getSpouseForename();

    @DomainFieldGetter(field = DomainField.FORENAME)
    String getForename();

    @DomainFieldGetter(field = DomainField.MIDDLENAME)
    String getMiddlename();

    @DomainFieldGetter(field = DomainField.SURNAME)
    String getSurname();

    @SubDomainObjectSetter(category = FieldCategory.ADDRESS)
    void setAddress(Address address);

    @DomainFieldSetter(field = DomainField.REG_DATE_MARRIAGE)
    void setRegistrationDate(String regDate);

    @DomainFieldSetter(field = DomainField.SPOUSE)
    void setSpouse(String spouse);

    @DomainFieldSetter(field = DomainField.SPOUSE_FORENAME)
    void setSpouseForename(String spouseForename);

    @DomainFieldSetter(field = DomainField.SURNAME)
    void setSurname(String surname);

    @DomainFieldSetter(field = DomainField.FORENAME)
    void setForename(String forename);
}
