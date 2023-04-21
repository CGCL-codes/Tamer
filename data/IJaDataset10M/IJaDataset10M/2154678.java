package org.oclc.da.ndiipp.struts.domain.util.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.oclc.da.common.query.AdvancedQuery;
import org.oclc.da.common.query.AdvancedQueryBuilder;
import org.oclc.da.common.query.Query;
import org.oclc.da.exceptions.DAException;
import org.oclc.da.informationobject.InformationObjectType;
import org.oclc.da.ndiipp.common.DataObject;
import org.oclc.da.ndiipp.common.pvt.UniversalDataContainer;
import org.oclc.da.ndiipp.helper.factory.ToolsetFactory;
import org.oclc.da.ndiipp.struts.core.util.CountBean;
import org.oclc.da.ndiipp.struts.domain.util.DomainBean;
import org.oclc.da.ndiipp.struts.domain.util.DomainHelperFacade;
import org.oclc.da.ndiipp.struts.entity.util.ShortEntityBean;

/**
 * Tests the domain helper facade.
 * @author tadgerm Created on Feb 18, 2005
 */
public class DomainHelperFacadeTest extends TestCase {

    /**
     * The domain helper facade.
     */
    private DomainHelperFacade dhf;

    /**
     * @see TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        String session = ToolsetFactory.load(ToolsetFactory.CONFIG_TOOLSET_PUBLIC).getLoginService().login("Marsel1", "peregrine1spring");
        dhf = new DomainHelperFacade(session);
        super.setUp();
    }

    /**
     * @see TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <P>
     * @throws DAException
     */
    public void testImportExportDomains() throws DAException {
        InputStream inputStream = null;
        try {
            String fileName = "DomainsImportHelperTest.1.txt";
            URL resourceLocationURL = this.getClass().getResource(fileName);
            System.out.println("Made it to here");
            inputStream = resourceLocationURL.openStream();
            if (inputStream == null) {
                throw new Exception("error opening input testfile " + fileName);
            }
        } catch (Exception ex) {
            Assert.fail("open inputstream #5.1 failed: exception " + ex.getClass().getName() + ", message " + ex.getMessage());
        }
        CountBean count = dhf.importDomains(inputStream);
        System.out.println(count.toString());
        OutputStream outputStream = null;
        try {
            String iniName = "DomainImportExportData.output.txt";
            File outputFile = new File(iniName);
            System.out.println("writing to: " + outputFile.getAbsolutePath());
            if (outputFile.exists()) {
                outputFile.delete();
            }
            outputFile = new File(iniName);
            outputFile.createNewFile();
            outputStream = new FileOutputStream(iniName);
        } catch (Exception ex) {
            Assert.fail("open outputstream #2.1 failed: exception " + ex.getClass().getName() + ", message " + ex.getMessage());
        }
        count = dhf.exportDomains(outputStream);
        System.out.println(count.toString());
        AdvancedQuery aEVQuery = null;
        aEVQuery = new AdvancedQuery(InformationObjectType.ENTITY, AdvancedQuery.ALPHA_SORT, new String[0]);
        AdvancedQueryBuilder builder = new AdvancedQueryBuilder();
        DataObject contactQuery = builder.build(aEVQuery);
        Query query = new UniversalDataContainer(null, contactQuery);
        ArrayList data = dhf.getDomainDataSet(query);
        dhf.manageDomains(new ArrayList<DataObject>(), new ArrayList<DataObject>(), data, false);
    }

    /**
     * <P>
     * @throws DAException
     */
    @SuppressWarnings("unchecked")
    public void testManageDomains() throws DAException {
        DomainBean dbean = new DomainBean();
        dbean.setName("testRelationship");
        ShortEntityBean entBean = new ShortEntityBean();
        entBean.setGuid("e45786da8a3c2c1e:74dd25e8:1021816fa99:-7ff8");
        entBean.setKeyName("c3");
        ArrayList entities = new ArrayList();
        entities.add(entBean);
        dbean.setAssociatedEntities(entities);
        ArrayList add = new ArrayList();
        add.add(dbean);
        ArrayList data = dhf.manageDomains(add, new ArrayList(), new ArrayList(), false);
        Assert.assertTrue("#1 - must return something", data.size() > 0);
        Assert.assertTrue("#1 - must return something", !(data.get(0) instanceof DAException));
        dhf.manageDomains(new ArrayList(), new ArrayList(), data, false);
    }
}
