package org.koossery.adempiere.sisv.impl.ad;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.compiere.model.X_AD_Org;
import org.koossery.adempiere.sisv.impl.AbstractCommonSISV;
import org.springframework.beans.factory.InitializingBean;
import org.koossery.adempiere.core.contract.dto.ad.AD_OrgDTO;
import org.koossery.adempiere.core.contract.criteria.ad.AD_OrgCriteria;
import org.koossery.adempiere.core.backend.interfaces.dao.ad.IAD_OrgDAO;
import org.koossery.adempiere.core.backend.interfaces.sisv.ad.IAD_OrgSISV;
import org.koossery.adempiere.core.contract.exceptions.KTAdempiereAppException;

public class AD_OrgSISVImpl extends AbstractCommonSISV implements IAD_OrgSISV, InitializingBean {

    private IAD_OrgDAO adorgDAOImpl;

    private static Logger logger = Logger.getLogger(AD_OrgSISVImpl.class);

    public AD_OrgSISVImpl() {
    }

    public void afterPropertiesSet() throws Exception {
        try {
            if (this.getDaoController() == null) System.out.println("finder null");
            this.adorgDAOImpl = (IAD_OrgDAO) this.getDaoController().get("DAO/AD_Org");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int createAD_Org(Properties ctx, AD_OrgDTO aD_OrgDTO, String trxname) throws KTAdempiereAppException {
        try {
            X_AD_Org model = new X_AD_Org(ctx, 0, trxname);
            if (adorgDAOImpl.isDuplicate(aD_OrgDTO.getName())) {
                throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_000");
            } else {
                System.out.println("NO duplicates names");
                model.setDescription(aD_OrgDTO.getDescription());
                model.setName(aD_OrgDTO.getName());
                model.setValue(aD_OrgDTO.getValue());
                model.setIsSummary(aD_OrgDTO.getIsSummary() == "Y" ? true : false);
                model.setIsActive(aD_OrgDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_OrgDTO.setAd_Org_ID(model.get_ID());
                return model.get_ID();
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_001");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public AD_OrgDTO getAD_Org(Properties ctx, int ad_Org_ID) throws KTAdempiereAppException {
        try {
            AD_OrgCriteria criteria = new AD_OrgCriteria();
            criteria.setAd_Org_ID(ad_Org_ID);
            ArrayList<AD_OrgDTO> result = adorgDAOImpl.getAD_Org(criteria);
            if ((result != null) && (result.size() > 0)) return result.get(0); else return null;
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_002");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public ArrayList<AD_OrgDTO> findAD_Org(AD_OrgCriteria aD_OrgCriteria) throws KTAdempiereAppException {
        try {
            return (ArrayList<AD_OrgDTO>) adorgDAOImpl.getAD_Org(aD_OrgCriteria);
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_003");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public void updateAD_Org(Properties ctx, AD_OrgDTO aD_OrgDTO) throws KTAdempiereAppException {
        try {
            X_AD_Org model = new X_AD_Org(ctx, aD_OrgDTO.getAd_Org_ID(), null);
            if (model != null) {
                String oldname = model.getName() + "";
                String newname = aD_OrgDTO.getName() + "";
                if (oldname.compareToIgnoreCase(newname) != 0) {
                    if (adorgDAOImpl.isDuplicate(newname)) throw new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_000");
                }
                System.out.println("NO duplicates names for modification");
                model.setDescription(aD_OrgDTO.getDescription());
                model.setName(aD_OrgDTO.getName());
                model.setValue(aD_OrgDTO.getValue());
                model.setIsSummary(aD_OrgDTO.getIsSummary() == "Y" ? true : false);
                model.setIsActive(aD_OrgDTO.getIsActive() == "Y" ? true : false);
                this.setPO(model);
                this.save();
                aD_OrgDTO.setAd_Org_ID(model.get_ID());
            }
        } catch (KTAdempiereAppException k) {
            throw k;
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_004");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }

    public boolean updateAD_Org(AD_OrgCriteria aD_OrgCriteria) throws KTAdempiereAppException {
        try {
            return adorgDAOImpl.update(aD_OrgCriteria);
        } catch (Exception e) {
            e.printStackTrace();
            KTAdempiereAppException ktadappe = new KTAdempiereAppException(SISVErrorMsgXmlFileName, "AD_ORG_SISV_005");
            logger.error(ktadappe.getHumanMessage() + "\n", e);
            throw ktadappe;
        }
    }
}
