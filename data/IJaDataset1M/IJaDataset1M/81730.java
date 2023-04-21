package test.googlecode.genericdao.dao.hibernate.dao.original;

import org.springframework.stereotype.Repository;
import test.googlecode.genericdao.model.Address;

@Repository
public class AddressDAOImpl extends BaseGenericDAOImpl<Address, Long> implements AddressDAO {
}
