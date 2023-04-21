package org.demis.elf.customerCustomfieldValue;

import java.util.Collection;

/**
  * DAO (Data Access Object) interface for CustomerCustomfieldValue.
  */
public interface CustomerCustomfieldValueDAO {

    public CustomerCustomfieldValue findById(java.lang.String customerCustomfieldValueId);

    public Collection<CustomerCustomfieldValue> findByExemple(CustomerCustomfieldValue customerCustomfieldValue);

    public int findCount(final CustomerCustomfieldValue customerCustomfieldValue);

    public void save(CustomerCustomfieldValue customerCustomfieldValue);

    public void saveAll(final Collection<CustomerCustomfieldValue> customerCustomfieldValues);

    public void delete(CustomerCustomfieldValue customerCustomfieldValue);

    public void deleteAll(final Collection<CustomerCustomfieldValue> customerCustomfieldValues);
}
