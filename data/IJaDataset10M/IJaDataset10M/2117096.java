package net.sf.beanshield.session;

import net.sf.beanshield.session.spi.ShieldSessionFactory;
import net.sf.beanshield.test.AbstractBeanshieldTests;
import net.sf.beanshield.test.Company;
import net.sf.beanshield.test.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 *
 */
@SuppressWarnings({ "deprecation" })
public class NestedUnitsOfWorkTest extends AbstractBeanshieldTests {

    private final Logger log = LoggerFactory.getLogger(ShieldSessionTest.class);

    public void testSetCompanyNameAndRevertInnermost() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        final String originalName = snpp.getName();
        final String newName1 = "SNPP_1";
        final String newName2 = "SNPP_2";
        final String newName3 = "SNPP_3";
        log.info("begin {");
        Company companyProxy = session.getShieldProxy(snpp);
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(originalName, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("  changing to " + newName1);
        companyProxy.setName(newName1);
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.beginUnitOfWork();
        log.info("  begin {");
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("    changing to " + newName2);
        companyProxy.setName(newName2);
        log.info("    proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.beginUnitOfWork();
        log.info("    begin {");
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("      changing to " + newName3);
        companyProxy.setName(newName3);
        log.info("      proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName3, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.rollbackUnitOfWork();
        log.info("    } rollback");
        log.info("    proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.commitUnitOfWork();
        log.info("  } commit");
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.commit();
        log.info("} commit");
        log.info("proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(newName2, snpp.getName());
    }

    public void testSetCompanyNameAndCommitAll() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        final String originalName = snpp.getName();
        final String newName1 = "SNPP_1";
        final String newName2 = "SNPP_2";
        final String newName3 = "SNPP_3";
        log.info("begin {");
        Company companyProxy = session.getShieldProxy(snpp);
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(originalName, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("  changing to " + newName1);
        companyProxy.setName(newName1);
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.beginUnitOfWork("change name 2");
        log.info("  begin {");
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("    changing to " + newName2);
        companyProxy.setName(newName2);
        log.info("    proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.beginUnitOfWork("change name 3");
        log.info("    begin {");
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("      changing to " + newName3);
        companyProxy.setName(newName3);
        log.info("      proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName3, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.commitUnitOfWork();
        log.info("    } commit");
        log.info("    proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName3, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.commitUnitOfWork();
        log.info("  } commit");
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName3, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.commit();
        log.info("} commit");
        log.info("proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName3, companyProxy.getName());
        assertEquals(newName3, snpp.getName());
    }

    public void testSetCompanyNameAndRevertAll() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        final String originalName = snpp.getName();
        final String newName1 = "SNPP_1";
        final String newName2 = "SNPP_2";
        final String newName3 = "SNPP_3";
        log.info("begin {");
        Company companyProxy = session.getShieldProxy(snpp);
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(originalName, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("  changing to " + newName1);
        companyProxy.setName(newName1);
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.beginUnitOfWork();
        log.info("  begin {");
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("    changing to " + newName2);
        companyProxy.setName(newName2);
        log.info("    proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.beginUnitOfWork();
        log.info("    begin {");
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        log.info("      changing to " + newName3);
        companyProxy.setName(newName3);
        log.info("      proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName3, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.rollbackUnitOfWork();
        log.info("    } rollback");
        log.info("    proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName2, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.rollbackUnitOfWork();
        log.info("  } rollback");
        log.info("  proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(newName1, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
        session.rollback();
        log.info("} rollback");
        log.info("proxy.name=" + companyProxy.getName() + ", target.name=" + snpp.getName());
        assertEquals(originalName, companyProxy.getName());
        assertEquals(originalName, snpp.getName());
    }

    public void testRemoveDepartmentsAndCommitAll() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        Set<Department> originalDepartmentSet = new LinkedHashSet<Department>(snpp.getDepartments());
        int originalSize = originalDepartmentSet.size();
        log.info("begin {");
        Company companyProxy = session.getShieldProxy(snpp);
        log.info("  proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize, companyProxy.getDepartments().size());
        assertEquals(originalSize, snpp.getDepartments().size());
        log.info("  removing first department");
        companyProxy.getDepartments().remove(companyProxy.getDepartments().iterator().next());
        log.info("  proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize - 1, companyProxy.getDepartments().size());
        assertEquals(originalSize, snpp.getDepartments().size());
        {
            session.beginUnitOfWork();
            log.info("  begin {");
            assertEquals(originalSize - 1, companyProxy.getDepartments().size());
            assertEquals(originalSize, snpp.getDepartments().size());
            log.info("    removing first department");
            companyProxy.getDepartments().remove(companyProxy.getDepartments().iterator().next());
            log.info("    proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
            assertEquals(originalSize - 2, companyProxy.getDepartments().size());
            assertEquals(originalSize, snpp.getDepartments().size());
            {
                session.beginUnitOfWork();
                log.info("    begin {");
                assertEquals(originalSize - 2, companyProxy.getDepartments().size());
                assertEquals(originalSize, snpp.getDepartments().size());
                log.info("      removing first department");
                companyProxy.getDepartments().remove(companyProxy.getDepartments().iterator().next());
                log.info("      proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
                assertEquals(originalSize - 3, companyProxy.getDepartments().size());
                assertEquals(originalSize, snpp.getDepartments().size());
                session.commitUnitOfWork();
                log.info("    } commit");
            }
            log.info("    proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
            assertEquals(originalSize - 3, companyProxy.getDepartments().size());
            assertEquals(originalSize, snpp.getDepartments().size());
            session.commitUnitOfWork();
            log.info("  } commit");
        }
        log.info("    proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize - 3, companyProxy.getDepartments().size());
        assertEquals(originalSize, snpp.getDepartments().size());
        session.commit();
        log.info("} commit");
        log.info("proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize - 3, companyProxy.getDepartments().size());
        assertEquals(originalSize - 3, snpp.getDepartments().size());
    }

    public void testRemoveDepartmentsAndRevertInnermost() throws Exception {
        ShieldSession session = ShieldSessionFactory.getInstance().createShieldSession();
        Set<Department> originalDepartmentSet = new LinkedHashSet<Department>(snpp.getDepartments());
        int originalSize = originalDepartmentSet.size();
        log.info("begin {");
        Company companyProxy = session.getShieldProxy(snpp);
        log.info("  proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize, companyProxy.getDepartments().size());
        assertEquals(originalSize, snpp.getDepartments().size());
        log.info("  removing first department");
        companyProxy.getDepartments().remove(companyProxy.getDepartments().iterator().next());
        log.info("  proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize - 1, companyProxy.getDepartments().size());
        assertEquals(originalSize, snpp.getDepartments().size());
        {
            session.beginUnitOfWork();
            log.info("  begin {");
            assertEquals(originalSize - 1, companyProxy.getDepartments().size());
            assertEquals(originalSize, snpp.getDepartments().size());
            log.info("    removing first department");
            companyProxy.getDepartments().remove(companyProxy.getDepartments().iterator().next());
            log.info("    proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
            assertEquals(originalSize - 2, companyProxy.getDepartments().size());
            assertEquals(originalSize, snpp.getDepartments().size());
            {
                session.beginUnitOfWork();
                log.info("    begin {");
                assertEquals(originalSize - 2, companyProxy.getDepartments().size());
                assertEquals(originalSize, snpp.getDepartments().size());
                log.info("      removing first department");
                companyProxy.getDepartments().remove(companyProxy.getDepartments().iterator().next());
                log.info("      proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
                assertEquals(originalSize - 3, companyProxy.getDepartments().size());
                assertEquals(originalSize, snpp.getDepartments().size());
                session.rollbackUnitOfWork();
                log.info("    } rollback");
            }
            log.info("    proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
            assertEquals(originalSize - 2, companyProxy.getDepartments().size());
            assertEquals(originalSize, snpp.getDepartments().size());
            session.commitUnitOfWork();
            log.info("  } commit");
        }
        log.info("    proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize - 2, companyProxy.getDepartments().size());
        assertEquals(originalSize, snpp.getDepartments().size());
        session.commit();
        log.info("} commit");
        log.info("proxy.departments=" + companyProxy.getDepartments() + ", target.departments=" + snpp.getDepartments());
        assertEquals(originalSize - 2, companyProxy.getDepartments().size());
        assertEquals(originalSize - 2, snpp.getDepartments().size());
    }
}
