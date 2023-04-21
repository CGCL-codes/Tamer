package pl.edu.agh.uddiProxy.types;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import pl.edu.agh.uddiProxy.AuthenticationHelper;
import pl.edu.agh.uddiProxy.UDDIClient;
import pl.edu.agh.uddiProxy.dao.DAOHelper;
import pl.edu.agh.uddiProxy.dao.DynamicParameterDAO;

public class DynamicParameterHibernateDAOTest {

    private static DynamicParameterDAO dynamicParameterDAO;

    private static UDDIClient client;

    private static Set<String> tModelSet = new HashSet<String>();

    @BeforeClass
    public static void setUp() {
        DAOHelper.test();
        dynamicParameterDAO = DAOHelper.getDynamicParameterDAO();
        client = DAOHelper.getUDDIClient();
    }

    @Test
    public void setUP() {
        assertNotNull(dynamicParameterDAO);
    }

    @Before
    public void deleteAll() {
        for (ServiceDynamicParameters parameters : dynamicParameterDAO.getAll()) {
            if (dynamicParameterDAO.exists(parameters.getId())) {
                dynamicParameterDAO.delete(parameters);
            }
        }
    }

    public static void tearDown() {
        String[] s = new String[tModelSet.size()];
        DAOHelper.getUDDIClient().getPublication().deleteTModels(tModelSet.toArray(s));
    }

    @Test
    public void create() {
        ServiceDynamicParameters dynamicParameters = new ServiceDynamicParameters();
        dynamicParameters.setTModelKey("tmodel1");
        dynamicParameters.setParameter("availability", "32");
        assertFalse(dynamicParameterDAO.exists(dynamicParameters.getTModelKey()));
        dynamicParameterDAO.create(dynamicParameters);
        assertTrue(dynamicParameterDAO.exists(dynamicParameters.getTModelKey()));
        try {
            dynamicParameterDAO.create(dynamicParameters);
        } catch (DataIntegrityViolationException e) {
            assertTrue(true);
        }
        dynamicParameterDAO.delete(dynamicParameters);
    }

    @Test
    public void delete() {
        ServiceDynamicParameters dynamicParameters = new ServiceDynamicParameters();
        dynamicParameters.setTModelKey("tmodel1");
        assertFalse(dynamicParameterDAO.exists(dynamicParameters.getTModelKey()));
        dynamicParameterDAO.create(dynamicParameters);
        assertTrue(dynamicParameterDAO.exists(dynamicParameters.getTModelKey()));
        dynamicParameterDAO.delete(dynamicParameters);
        assertFalse(dynamicParameterDAO.exists(dynamicParameters.getTModelKey()));
    }

    @Test
    public void getAll() {
        ServiceDynamicParameters dynamicParameters1 = new ServiceDynamicParameters();
        dynamicParameters1.setTModelKey("tmodel1");
        ServiceDynamicParameters dynamicParameters2 = new ServiceDynamicParameters();
        dynamicParameters2.setTModelKey("tmodel2");
        for (ServiceDynamicParameters parameters : dynamicParameterDAO.getAll()) {
            dynamicParameterDAO.delete(parameters);
        }
        assertEquals(0, dynamicParameterDAO.getAll().size());
        dynamicParameterDAO.create(dynamicParameters1);
        dynamicParameterDAO.create(dynamicParameters2);
        assertEquals(2, dynamicParameterDAO.getAllIds().size());
        dynamicParameterDAO.delete(dynamicParameters1);
        assertEquals(1, dynamicParameterDAO.getAll().size());
        dynamicParameterDAO.delete(dynamicParameters2);
        assertEquals(0, dynamicParameterDAO.getAll().size());
    }

    @Test
    public void update() {
        ServiceDynamicParameters dynamicParameters1 = new ServiceDynamicParameters();
        dynamicParameters1.setTModelKey("tmodel3");
        dynamicParameterDAO.create(dynamicParameters1);
        assertTrue(dynamicParameterDAO.getByTModel(dynamicParameters1.getTModelKey()).equals(dynamicParameters1));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken("admin", "admin");
        AuthenticationHelper.login(authentication);
        dynamicParameterDAO.update(dynamicParameters1);
        assertTrue(dynamicParameterDAO.getByTModel(dynamicParameters1.getTModelKey()).equals(dynamicParameters1));
        dynamicParameters1.setTModelKey("tmodel4");
        dynamicParameterDAO.update(dynamicParameters1);
        assertTrue(dynamicParameterDAO.getByTModel(dynamicParameters1.getTModelKey()).equals(dynamicParameters1));
        AuthenticationHelper.logout(authentication);
    }

    @Test
    public void query() {
        TModel model = Factory.newInstance();
        model.getModel().addNewName().setStringValue("testTmodel");
        ServiceDynamicParameters dynamicParameter = new ServiceDynamicParameters();
        dynamicParameter.setParameter("availability", "13");
        dynamicParameter.setParameter("trust", "43");
        model.setDynamicParameter(dynamicParameter);
        model = client.getPublication().saveTModel(model).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel[] models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey() }, "availability > 15");
        assertEquals(0, models.length);
        models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey() }, "trust > 15");
        assertEquals(1, models.length);
        models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey() }, "availability < 25 AND trust > 15");
        assertEquals(1, models.length);
    }

    @Test
    public void query2() {
        TModel model = Factory.newInstance();
        model.getModel().addNewName().setStringValue("testTmodel");
        ServiceDynamicParameters dynamicParameter = new ServiceDynamicParameters();
        dynamicParameter.setParameter("availability", "13");
        dynamicParameter.setParameter("trust", "43");
        model.setDynamicParameter(dynamicParameter);
        model = client.getPublication().saveTModel(model).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel model2 = Factory.newInstance();
        model2.getModel().addNewName().setStringValue("testTmodel");
        ServiceDynamicParameters dynamicParameter2 = new ServiceDynamicParameters();
        dynamicParameter2.setParameter("availability", "65");
        dynamicParameter2.setParameter("trust", "32");
        model2.setDynamicParameter(dynamicParameter2);
        model2 = client.getPublication().saveTModel(model2).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel[] models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey() }, "availability > 15");
        assertEquals(1, models.length);
        models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey() }, "trust > 32");
        assertEquals(1, models.length);
        models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey() }, "availability < 25 AND trust > 15");
        assertEquals(1, models.length);
        models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey() }, "(availability < 25) OR (trust > 15)");
        assertEquals(2, models.length);
        models = client.getInquiryClient().getTModels(new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey() }, "(availability < 25) OR (trust > 32)");
        assertEquals(1, models.length);
    }

    @Test
    public void query3() {
        TModel model = Factory.newInstance();
        model.getModel().addNewName().setStringValue("testTmodel");
        ServiceDynamicParameters dynamicParameter = new ServiceDynamicParameters();
        dynamicParameter.setParameter("availability", "99");
        dynamicParameter.setParameter("price", "6.8");
        dynamicParameter.setParameter("time", "1.3");
        dynamicParameter.setParameter("serviceRank", "7.3");
        dynamicParameter.setParameter("downloadWSDL", "6");
        model.setDynamicParameter(dynamicParameter);
        model = client.getPublication().saveTModel(model).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel model2 = Factory.newInstance();
        model2.getModel().addNewName().setStringValue("testTmodel2");
        ServiceDynamicParameters dynamicParameter2 = new ServiceDynamicParameters();
        dynamicParameter2.setParameter("availability", "95.8");
        dynamicParameter2.setParameter("price", "4.9");
        dynamicParameter2.setParameter("time", "5.3");
        dynamicParameter2.setParameter("serviceRank", "5.3");
        dynamicParameter2.setParameter("downloadWSDL", "6");
        model2.setDynamicParameter(dynamicParameter2);
        model2 = client.getPublication().saveTModel(model2).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel model3 = Factory.newInstance();
        model3.getModel().addNewName().setStringValue("testTmodel3");
        ServiceDynamicParameters dynamicParameter3 = new ServiceDynamicParameters();
        dynamicParameter3.setParameter("availability", "98.8");
        dynamicParameter3.setParameter("price", "6.8");
        dynamicParameter3.setParameter("time", "2.0");
        dynamicParameter3.setParameter("serviceRank", "6.3");
        dynamicParameter3.setParameter("downloadWSDL", "6");
        model3.setDynamicParameter(dynamicParameter3);
        model3 = client.getPublication().saveTModel(model3).execute();
        tModelSet.add(model.getModel().getTModelKey());
        String[] s = new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey(), model3.getModel().getTModelKey() };
        TModel[] models = client.getInquiryClient().getTModels(s, "availability > 90");
        assertEquals(3, models.length);
        assertArrayEquals(new String[] { model.getModel().getTModelKey(), model3.getModel().getTModelKey(), model2.getModel().getTModelKey() }, new String[] { models[0].getModel().getTModelKey(), models[1].getModel().getTModelKey(), models[2].getModel().getTModelKey() });
        models = client.getInquiryClient().getTModels(s, "time <= 8");
        assertEquals(3, models.length);
        assertArrayEquals(new String[] { model.getModel().getTModelKey(), model3.getModel().getTModelKey(), model2.getModel().getTModelKey() }, new String[] { models[0].getModel().getTModelKey(), models[1].getModel().getTModelKey(), models[2].getModel().getTModelKey() });
        models = client.getInquiryClient().getTModels(s, "price = 6.8");
        assertEquals(2, models.length);
        models = client.getInquiryClient().getTModels(s, "price >= 6.8");
        assertEquals(2, models.length);
        models = client.getInquiryClient().getTModels(s, "price >= 6.8 and price < 6.9");
        assertEquals(2, models.length);
        models = client.getInquiryClient().getTModels(s, "price >= 6.8 and serviceRank > 6.3");
        assertEquals(1, models.length);
        assertArrayEquals(new String[] { model.getModel().getTModelKey() }, new String[] { models[0].getModel().getTModelKey() });
        models = client.getInquiryClient().getTModels(s, "(price > 4.5 and serviceRank < 7)");
        assertEquals(2, models.length);
        assertArrayEquals(new String[] { model2.getModel().getTModelKey(), model3.getModel().getTModelKey() }, new String[] { models[0].getModel().getTModelKey(), models[1].getModel().getTModelKey() });
        models = client.getInquiryClient().getTModels(s, "(price >= 6.8 and serviceRank > 6.3) or (price > 4.5 and serviceRank < 7)");
        assertEquals(3, models.length);
        models = client.getInquiryClient().getTModels(s, "price >= 6.8 and serviceRank > 6.3 or price > 4.5 and serviceRank < 7");
        assertEquals(3, models.length);
        models = client.getInquiryClient().getTModels(s, "(price >= 6.8 and serviceRank > 6.3) or (price > 4.5 and serviceRank < 7) and price < 5");
        assertEquals(2, models.length);
        models = client.getInquiryClient().getTModels(s, "(time < 5.5 and price <= 6.8) and downloadWSDL > 1");
        assertEquals(3, models.length);
        assertArrayEquals(new String[] { model.getModel().getTModelKey(), model3.getModel().getTModelKey(), model2.getModel().getTModelKey() }, new String[] { models[0].getModel().getTModelKey(), models[1].getModel().getTModelKey(), models[2].getModel().getTModelKey() });
    }

    @Test
    public void query4() {
        TModel model = Factory.newInstance();
        model.getModel().addNewName().setStringValue("testTmodel");
        ServiceDynamicParameters dynamicParameter = new ServiceDynamicParameters();
        dynamicParameter.setParameter("downloadWSDL", "1");
        model.setDynamicParameter(dynamicParameter);
        model = client.getPublication().saveTModel(model).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel model2 = Factory.newInstance();
        model2.getModel().addNewName().setStringValue("testTmodel2");
        ServiceDynamicParameters dynamicParameter2 = new ServiceDynamicParameters();
        dynamicParameter2.setParameter("downloadWSDL", "2");
        model2.setDynamicParameter(dynamicParameter2);
        model2 = client.getPublication().saveTModel(model2).execute();
        tModelSet.add(model.getModel().getTModelKey());
        TModel model3 = Factory.newInstance();
        model3.getModel().addNewName().setStringValue("testTmodel3");
        ServiceDynamicParameters dynamicParameter3 = new ServiceDynamicParameters();
        dynamicParameter3.setParameter("downloadWSDL", "3");
        model3.setDynamicParameter(dynamicParameter3);
        model3 = client.getPublication().saveTModel(model3).execute();
        tModelSet.add(model.getModel().getTModelKey());
        String[] s = new String[] { model.getModel().getTModelKey(), model2.getModel().getTModelKey(), model3.getModel().getTModelKey() };
        TModel[] models = client.getInquiryClient().getTModels(s, "downloadWSDL >= 0");
        assertEquals(3, models.length);
        assertArrayEquals(new String[] { model3.getModel().getTModelKey(), model2.getModel().getTModelKey(), model.getModel().getTModelKey() }, new String[] { models[0].getModel().getTModelKey(), models[1].getModel().getTModelKey(), models[2].getModel().getTModelKey() });
    }

    @Test
    public void query5() {
        List<String> list = Arrays.asList(new String[] { "uddi:uddi" });
        List<ServiceDynamicParameters> result = dynamicParameterDAO.getDynamicParameters("downloadWSDL < 10", list);
        assertTrue(result.size() >= 0);
    }
}
