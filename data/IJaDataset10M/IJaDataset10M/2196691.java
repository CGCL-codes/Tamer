package org.pcorp.space.persistance.test;

import org.pcorp.space.helper.SpringBeanFactory;
import org.pcorp.space.persistance.dao.EquipDAO;
import org.pcorp.space.persistance.dao.SouteDAO;
import org.pcorp.space.persistance.exception.PersistanceException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import junit.framework.TestCase;

public class TestEquipement extends TestCase {

    XmlBeanFactory xmlbean;

    public TestEquipement(String name) {
        super(name);
        xmlbean = SpringBeanFactory.getXmlFactory();
    }

    public void testGetEquipementVx() {
        EquipDAO equipdao = (EquipDAO) xmlbean.getBean("equipDAO");
        try {
            System.out.println(equipdao.getEquipementVx(1));
        } catch (PersistanceException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testSoute() {
        System.out.println("TEST_SOUTE");
        SouteDAO souteDAO = (SouteDAO) xmlbean.getBean("souteDAO");
        try {
            System.out.println(souteDAO.getSouteFromVaisseau(1, true));
            System.out.println(souteDAO.getSouteFromVaisseau(1, false));
        } catch (PersistanceException e) {
            e.printStackTrace();
            System.err.println(e.getM_message());
            fail();
        }
    }
}
