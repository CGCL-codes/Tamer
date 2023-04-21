package edu.nwpu.oboe.test;

import java.util.Date;
import junit.framework.TestCase;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTransactionManager;
import edu.nwpu.oboe.oboist.model.Privilege;
import edu.nwpu.oboe.oboist.model.Role;
import edu.nwpu.oboe.oboist.model.type.PrivilegeType;
import edu.nwpu.oboe.oboist.model.type.RoleType;
import edu.nwpu.oboe.oboist.repository.RoleRepository;

/**
 * @copyright Oboe Project 2009
 * @author William
 * @Revision
 * @date 2009-6-27
 */
public class RepositoryRoleTest extends TestCase {

    RoleRepository roleRepository;

    static Long tempId;

    HibernateTransactionManager transactionManager;

    protected void setUp() throws Exception {
        super.setUp();
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath*:application.xml", "classpath*:META-INF/basetank/basetank.xml" });
        this.roleRepository = (RoleRepository) context.getBean("roleRepository");
        transactionManager = (HibernateTransactionManager) context.getBean("transactionManager");
    }

    public void testSave() {
        Role role = createNewRole();
        tempId = this.roleRepository.save(role);
        assertEquals("Save Role", this.roleRepository.get(tempId).getName());
    }

    /**
	 * @return
	 */
    private Role createNewRole() {
        Role role = new Role();
        role.setActive(true);
        role.setDescription("This is just for test " + new Date());
        role.setName("Save Role");
        role.setRoleType(RoleType.SystemRole);
        return role;
    }

    public void testUpdate() {
        Role pInstance = this.roleRepository.get(tempId);
        pInstance.setRoleType(RoleType.ProjectRole);
        this.roleRepository.update(pInstance);
        Role shadow = this.roleRepository.get(tempId);
        assertEquals(RoleType.ProjectRole, shadow.getRoleType());
    }

    public void testSavePrivilege() {
        Privilege p1 = createPrivlege();
        Role myrole = roleRepository.get(tempId);
        myrole.addPrivileges(p1);
        roleRepository.update(myrole);
        Role another = createNewRole();
        another.setRoleType(RoleType.ProjectRole);
        another.addPrivileges(createPrivlege());
        roleRepository.save(another);
        Privilege p2 = roleRepository.get(tempId).getPrivileges().get(0);
        System.out.println("the p2 is " + p2.getId());
    }

    /**
	 * @return
	 */
    private Privilege createPrivlege() {
        Privilege p1 = new Privilege();
        p1.setAbbr("test for Role");
        p1.setDescription("hello " + new Date());
        p1.setType(PrivilegeType.ProjectPrivilege);
        return p1;
    }
}
