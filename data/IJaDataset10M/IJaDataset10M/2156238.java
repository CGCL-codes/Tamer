package net.sf.sail.webapp.dao.authentication.impl;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Cynick Young
 * 
 * @version $Id: AllTests.java 1230 2007-09-25 15:47:21Z laurel $
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ net.sf.sail.webapp.dao.authentication.impl.HibernateAclEntryDaoTest.class, net.sf.sail.webapp.dao.authentication.impl.HibernateAclSidDaoTest.class, net.sf.sail.webapp.dao.authentication.impl.HibernateAclTargetObjectDaoTest.class, net.sf.sail.webapp.dao.authentication.impl.HibernateAclTargetObjectIdentityDaoTest.class, net.sf.sail.webapp.dao.authentication.impl.HibernateGrantedAuthorityDaoTest.class, net.sf.sail.webapp.dao.authentication.impl.HibernateUserDetailsDaoTest.class })
public class AllTests {
}
