package com.opennms.internal.expenses.model;

import java.util.Collection;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.stereotype.Repository;

/**
 * UserRespository that allows users to be searched for, updated or created
 *
 * @author brozow
 */
@Repository
public interface UserRepository extends UserDetailsService {

    User get(String username);

    void saveOrUpdate(User user);

    Collection<User> findAll();
}
