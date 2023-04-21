package org.koossery.adempiere.svco.impl.generated;

import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import javax.xml.parsers.FactoryConfigurationError;
import org.compiere.util.Env;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.koossery.adempiere.core.contract.criteria.generated.C_TaxPostalCriteria;
import org.koossery.adempiere.core.contract.dto.generated.C_TaxPostalDTO;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereException;
import org.koossery.adempiere.core.contract.interfaces.generated.IC_TaxPostalSVCO;
import org.koossery.adempiere.svco.impl.KTADempiereBaseTest;
import org.springframework.beans.BeansException;

public class C_TaxPostalSVCOImplTest extends KTADempiereBaseTest {

    private static IC_TaxPostalSVCO ctaxpostalSVCOImpl = null;

    private static C_TaxPostalDTO newcTaxPostalDTO = new C_TaxPostalDTO();

    private static C_TaxPostalDTO loadcTaxPostalDTO = new C_TaxPostalDTO();

    private static Date d = new Date();

    static {
        try {
            init();
            ctaxpostalSVCOImpl = (IC_TaxPostalSVCO) finder.get("SVCO/C_TaxPostal");
            newcTaxPostalDTO.setC_Tax_ID(106);
            newcTaxPostalDTO.setPostal("P" + d);
            newcTaxPostalDTO.setPostal_To("postal_To");
            newcTaxPostalDTO.setIsActive("Y");
        } catch (BeansException e) {
            e.printStackTrace();
        } catch (FactoryConfigurationError e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        close();
    }

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testcreateC_TaxPostal() {
        try {
            int c_TaxPostal_ID = ctaxpostalSVCOImpl.createC_TaxPostal(ctx, newcTaxPostalDTO, null);
            assertNotSame("C_TaxPostal creation failed ID=0", c_TaxPostal_ID, 0);
            newcTaxPostalDTO.setC_TaxPostal_ID(c_TaxPostal_ID);
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testfindOneC_TaxPostal() {
        try {
            int key = newcTaxPostalDTO.getC_TaxPostal_ID();
            loadcTaxPostalDTO = ctaxpostalSVCOImpl.findOneC_TaxPostal(ctx, key);
            assertEquals(loadcTaxPostalDTO.getC_Tax_ID(), newcTaxPostalDTO.getC_Tax_ID());
            assertEquals(loadcTaxPostalDTO.getC_TaxPostal_ID(), newcTaxPostalDTO.getC_TaxPostal_ID());
            assertEquals(loadcTaxPostalDTO.getPostal_To(), newcTaxPostalDTO.getPostal_To());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    public void testfindC_TaxPostal() {
        ArrayList<C_TaxPostalDTO> resultlist;
        try {
            C_TaxPostalCriteria criteria = new C_TaxPostalCriteria();
            resultlist = ctaxpostalSVCOImpl.findC_TaxPostal(ctx, criteria);
            assertNotNull(resultlist);
            for (C_TaxPostalDTO dto : resultlist) {
                System.out.println("ID=" + dto.getC_TaxPostal_ID() + " : Name=" + dto.getPostal());
                if (dto.getC_TaxPostal_ID() == newcTaxPostalDTO.getC_TaxPostal_ID()) assertEquals(dto.getPostal(), newcTaxPostalDTO.getPostal());
            }
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testupdateC_TaxPostal() {
        try {
            newcTaxPostalDTO.setPostal(loadcTaxPostalDTO.getPostal() + "M");
            newcTaxPostalDTO.setIsActive("Y");
            ctaxpostalSVCOImpl.updateC_TaxPostal(ctx, newcTaxPostalDTO);
            int key = newcTaxPostalDTO.getC_TaxPostal_ID();
            loadcTaxPostalDTO = ctaxpostalSVCOImpl.findOneC_TaxPostal(ctx, key);
            assertEquals(loadcTaxPostalDTO.getPostal() + "M", newcTaxPostalDTO.getPostal());
            assertEquals(loadcTaxPostalDTO.getC_TaxPostal_ID(), newcTaxPostalDTO.getC_TaxPostal_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    public void testDesactiverC_TaxPostal() {
        try {
            C_TaxPostalCriteria criteria = new C_TaxPostalCriteria();
            criteria.setC_TaxPostal_ID(newcTaxPostalDTO.getC_TaxPostal_ID());
            criteria.setIsActive("N");
            ctaxpostalSVCOImpl.updateC_TaxPostal(criteria);
            int key = newcTaxPostalDTO.getC_TaxPostal_ID();
            loadcTaxPostalDTO = ctaxpostalSVCOImpl.findOneC_TaxPostal(ctx, key);
            assertEquals(loadcTaxPostalDTO.getIsActive(), "N");
            assertEquals(loadcTaxPostalDTO.getC_TaxPostal_ID(), newcTaxPostalDTO.getC_TaxPostal_ID());
        } catch (KTAdempiereException e) {
            e.printStackTrace();
        }
    }

    protected String getWhereClause(Properties ctx, C_TaxPostalCriteria criteria) {
        StringBuffer temp = new StringBuffer();
        temp.append("( ad_org_id=" + Env.getAD_Org_ID(ctx) + ")");
        temp.append(" and (ad_client_id=" + Env.getAD_Client_ID(ctx) + ")");
        if (criteria.getC_Tax_ID() > 0) temp.append(" and (c_tax_id=" + criteria.getC_Tax_ID() + ")");
        if (criteria.getC_TaxPostal_ID() > 0) temp.append(" and (c_TaxPostal_id=" + criteria.getC_TaxPostal_ID() + ")");
        if (criteria.getIsActive() != null) temp.append(" and (isActive='" + criteria.getIsActive() + "')");
        if (criteria.getPostal() != null) temp.append(" and (POSTAL='" + criteria.getPostal() + "')");
        if (criteria.getPostal_To() != null) temp.append(" and (POSTAL_TO='" + criteria.getPostal_To() + "')");
        return temp.toString();
    }
}
