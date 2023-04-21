package org.examcity.domain.jpa;

import org.examcity.domain.User;
import org.examcity.domain.UserRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository("userRepository")
@Transactional(readOnly = true)
public class UserRepositoryImpl extends GenericRepositoryImpl<User, Long> implements UserRepository {
}
