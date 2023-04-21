package es.uclm.inf_cr.alarcos.desglosa_web.control;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.xml.bind.JAXBException;
import model.gl.knowledge.GLFactory;
import model.gl.knowledge.GLObject;
import model.util.City;
import model.util.Neighborhood;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.CompanyNotFoundException;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.EntityNotSupportedException;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.FactoryNotFoundException;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.GroupByOperationNotSupportedException;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.IncompatibleTypesException;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.MarketNotFoundException;
import es.uclm.inf_cr.alarcos.desglosa_web.exception.ProjectNotFoundException;
import es.uclm.inf_cr.alarcos.desglosa_web.utils.SpringTestCaseBase;

public class GLObjectManagerTest extends SpringTestCaseBase {

    private String groupBy;

    private List<?> entities;

    private String profileName;

    private City city;

    public void testFactoryDefaultProfileGroupByCompany() {
        entities = FactoryManager.getAllFactories();
        groupBy = GLObjectManager.GROUP_BY_COMPANY;
        profileName = "factory-FactoryDefault-1325612322955.xml";
        try {
            city = GLObjectManager.createGLObjects(entities, groupBy, 0, profileName);
            assertNotNull(city);
            assertEquals(2, city.getNeighborhoods().size());
            for (Neighborhood nbh : city.getNeighborhoods()) {
                String companyName = "";
                int flats = 0;
                if (nbh.getName().equals("test company name 1")) {
                    companyName = "test company name 1";
                    flats = 2;
                } else if (nbh.getName().equals("test company name 2")) {
                    companyName = "test company name 2";
                    flats = 1;
                }
                assertEquals(nbh.getFlats().size(), flats);
                for (GLObject globj : nbh.getFlats()) {
                    assertTrue(globj instanceof GLFactory);
                    assertTrue(FactoryManager.getFactory(globj.getId()).getCompany().getName().equals(companyName));
                }
            }
        } catch (SecurityException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (JAXBException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        } catch (EntityNotSupportedException e) {
            fail(e.getMessage());
        } catch (GroupByOperationNotSupportedException e) {
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IncompatibleTypesException e) {
            fail(e.getMessage());
        } catch (FactoryNotFoundException e) {
            fail(e.getMessage());
        } catch (CompanyNotFoundException e) {
            fail(e.getMessage());
        } catch (MarketNotFoundException e) {
            fail(e.getMessage());
        } catch (ProjectNotFoundException e) {
            fail(e.getMessage());
        }
    }

    public void testFactoryDefaultProfileGroupByProject() {
        entities = FactoryManager.getAllFactories();
        groupBy = GLObjectManager.GROUP_BY_PROJECT;
        profileName = "factory-FactoryDefault-1325612322955.xml";
        try {
            city = GLObjectManager.createGLObjects(entities, groupBy, 0, profileName);
            assertNotNull(city);
            assertEquals(2, city.getNeighborhoods().size());
            for (Neighborhood nbh : city.getNeighborhoods()) {
                int flats = 0;
                if (nbh.getName().equals("desglosa")) {
                    flats = 2;
                } else if (nbh.getName().equals("where to publish")) {
                    flats = 2;
                }
                assertEquals(nbh.getFlats().size(), flats);
            }
        } catch (SecurityException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (JAXBException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        } catch (EntityNotSupportedException e) {
            fail(e.getMessage());
        } catch (GroupByOperationNotSupportedException e) {
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IncompatibleTypesException e) {
            fail(e.getMessage());
        } catch (FactoryNotFoundException e) {
            fail(e.getMessage());
        } catch (CompanyNotFoundException e) {
            fail(e.getMessage());
        } catch (MarketNotFoundException e) {
            fail(e.getMessage());
        } catch (ProjectNotFoundException e) {
            fail(e.getMessage());
        }
    }

    public void testFactoryDefaultProfileGroupByMarket() {
        entities = FactoryManager.getAllFactories();
        groupBy = GLObjectManager.GROUP_BY_MARKET;
        profileName = "factory-FactoryDefault-1325612322955.xml";
        try {
            city = GLObjectManager.createGLObjects(entities, groupBy, 0, profileName);
            assertNotNull(city);
        } catch (SecurityException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (JAXBException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        } catch (EntityNotSupportedException e) {
            fail(e.getMessage());
        } catch (GroupByOperationNotSupportedException e) {
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IncompatibleTypesException e) {
            fail(e.getMessage());
        } catch (FactoryNotFoundException e) {
            fail(e.getMessage());
        } catch (CompanyNotFoundException e) {
            fail(e.getMessage());
        } catch (MarketNotFoundException e) {
            fail(e.getMessage());
        } catch (ProjectNotFoundException e) {
            fail(e.getMessage());
        }
    }

    public void testFactoryDefaultProfileNoGroupBy() {
        entities = FactoryManager.getAllFactories();
        groupBy = GLObjectManager.NO_GROUP_BY;
        profileName = "factory-FactoryDefault-1325612322955.xml";
        try {
            city = GLObjectManager.createGLObjects(entities, groupBy, 0, profileName);
            assertNotNull(city);
            assertEquals(1, city.getNeighborhoods().size());
        } catch (SecurityException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (JAXBException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        } catch (EntityNotSupportedException e) {
            fail(e.getMessage());
        } catch (GroupByOperationNotSupportedException e) {
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IncompatibleTypesException e) {
            fail(e.getMessage());
        } catch (FactoryNotFoundException e) {
            fail(e.getMessage());
        } catch (CompanyNotFoundException e) {
            fail(e.getMessage());
        } catch (MarketNotFoundException e) {
            fail(e.getMessage());
        } catch (ProjectNotFoundException e) {
            fail(e.getMessage());
        }
    }

    public void testProjectDefaultProfileGroupByFactory() {
        entities = ProjectManager.getAllProjects();
        groupBy = GLObjectManager.GROUP_BY_FACTORY;
        profileName = "project-ProjectDefault-1325631634846.xml";
        try {
            city = GLObjectManager.createGLObjects(entities, groupBy, 0, profileName);
            assertNotNull(city);
            assertEquals(2, city.getNeighborhoods().size());
            for (Neighborhood nbh : city.getNeighborhoods()) {
                int flats = 0;
                if (nbh.getName().equals("test factory name 1")) {
                    flats = 2;
                } else if (nbh.getName().equals("test factory name 2")) {
                    flats = 2;
                }
                assertEquals(nbh.getFlats().size(), flats);
            }
        } catch (SecurityException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (JAXBException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        } catch (EntityNotSupportedException e) {
            fail(e.getMessage());
        } catch (GroupByOperationNotSupportedException e) {
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IncompatibleTypesException e) {
            fail(e.getMessage());
        } catch (FactoryNotFoundException e) {
            fail(e.getMessage());
        } catch (CompanyNotFoundException e) {
            fail(e.getMessage());
        } catch (MarketNotFoundException e) {
            fail(e.getMessage());
        } catch (ProjectNotFoundException e) {
            fail(e.getMessage());
        }
    }

    public void testProjectBoolean2FloatRangeProfileNoGroupBy() {
        entities = ProjectManager.getAllProjects();
        groupBy = GLObjectManager.GROUP_BY_FACTORY;
        profileName = "project-Boolean2FloatRange-1325962027137.xml";
        try {
            city = GLObjectManager.createGLObjects(entities, groupBy, 0, profileName);
            assertNotNull(city);
            assertEquals(2, city.getNeighborhoods().size());
        } catch (SecurityException e) {
            fail(e.getMessage());
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        } catch (JAXBException e) {
            fail(e.getMessage());
        } catch (IOException e) {
            fail(e.getMessage());
        } catch (InstantiationException e) {
            fail(e.getMessage());
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        } catch (ClassNotFoundException e) {
            fail(e.getMessage());
        } catch (NoSuchMethodException e) {
            fail(e.getMessage());
        } catch (InvocationTargetException e) {
            fail(e.getMessage());
        } catch (EntityNotSupportedException e) {
            fail(e.getMessage());
        } catch (GroupByOperationNotSupportedException e) {
            fail(e.getMessage());
        } catch (NoSuchFieldException e) {
            fail(e.getMessage());
        } catch (IncompatibleTypesException e) {
            fail(e.getMessage());
        } catch (FactoryNotFoundException e) {
            fail(e.getMessage());
        } catch (CompanyNotFoundException e) {
            fail(e.getMessage());
        } catch (MarketNotFoundException e) {
            fail(e.getMessage());
        } catch (ProjectNotFoundException e) {
            fail(e.getMessage());
        }
    }
}
