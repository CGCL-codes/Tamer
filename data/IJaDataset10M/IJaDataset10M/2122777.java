package ru.unislabs.dbtier.tests;

import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.unislabs.dbtier.DAO;
import ru.unislabs.dbtier.dto.TestAnnotatedDTO;
import ru.unislabs.dbtier.factory.DAOFactory;

public class RegisterClassTestCase {

    private static DAO dao;

    @BeforeClass
    public static void setUp() {
        DAOFactory.getInstance().setDefaultDAOImplementation("ru.unislabs.dbtier.implementation.dao.hibernate");
        dao = DAOFactory.getInstance().getDAO();
    }

    @Test
    public void testRegisterClass() {
        if (!dao.getAnnotationsSupport().isAnnotatedClassRegistered(TestAnnotatedDTO.class)) dao.getAnnotationsSupport().registerAnnotatedClass(TestAnnotatedDTO.class);
        assertTrue(dao.getAnnotationsSupport().isAnnotatedClassRegistered(TestAnnotatedDTO.class));
    }
}
