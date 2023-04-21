package com.technoetic.xplanner.security.module;

import javax.security.auth.Subject;
import net.sf.xplanner.domain.Person;
import org.hibernate.HibernateException;
import com.technoetic.xplanner.security.AuthenticationException;

public interface LoginSupport {

    Person populateSubjectPrincipalFromDatabase(Subject subject, String userId) throws AuthenticationException;

    Person getPerson(String userId) throws HibernateException;

    Subject createSubject();
}
