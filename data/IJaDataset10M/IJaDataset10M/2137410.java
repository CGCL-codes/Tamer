package com.icteam.fiji.persistence;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import com.icteam.fiji.model.CfgnKey;
import com.icteam.fiji.model.Ling;
import com.icteam.fiji.model.PermClas;
import com.icteam.fiji.model.PropCntto;
import com.icteam.fiji.model.RuoloEntPrcsGrpReg;
import com.icteam.fiji.model.RuoloOrgv;
import com.icteam.fiji.model.StaEntPrcsReg;
import com.icteam.fiji.model.TipAreaGeogr;
import com.icteam.fiji.model.TipAudt;
import com.icteam.fiji.model.TipBlkUten;
import com.icteam.fiji.model.TipCfgnKey;
import com.icteam.fiji.model.TipCnlCntto;
import com.icteam.fiji.model.TipCodEst;
import com.icteam.fiji.model.TipEntBsns;
import com.icteam.fiji.model.TipEntPrcs;
import com.icteam.fiji.model.TipFntEst;
import com.icteam.fiji.model.TipGrp;
import com.icteam.fiji.model.TipLocaz;
import com.icteam.fiji.model.TipLocazSoc;
import com.icteam.fiji.model.TipLvlSic;
import com.icteam.fiji.model.TipOperAudt;
import com.icteam.fiji.model.TipOprrMatmc;
import com.icteam.fiji.model.TipOrgn;
import com.icteam.fiji.model.TipParm;
import com.icteam.fiji.model.TipPerm;
import com.icteam.fiji.model.TipQlfr;
import com.icteam.fiji.model.TipRlznEntBsnsEntBsns;
import com.icteam.fiji.model.TipRuoloEntBsnsEntBsns;
import com.icteam.fiji.model.TipRuoloEntBsnsGrp;
import com.icteam.fiji.model.TipRuoloEntPrcsEntBsns;
import com.icteam.fiji.model.TipRuoloEntPrcsEntPrcs;
import com.icteam.fiji.model.TipRuoloEntPrcsGrp;
import com.icteam.fiji.model.TipRuoloGrpUten;
import com.icteam.fiji.model.TipSet;
import com.icteam.fiji.model.TipSoc;
import com.icteam.fiji.model.TipSocStruCtrl;
import com.icteam.fiji.model.TipStaCrcm;
import com.icteam.fiji.model.TipStaEntPrcs;
import com.icteam.fiji.model.TipStaUten;
import com.icteam.fiji.model.TipUtlCntto;
import com.icteam.fiji.model.TipWrkflw;

public class TipPersistenceMgr extends PersistenceMgr {

    public static Log logger = LogFactory.getLog(TipPersistenceMgr.class.getName());

    public List<Ling> selectLings() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(Ling.class);
        criteria.addOrder(Order.asc("NLing"));
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<Ling>();
        }
    }

    public RuoloOrgv selectRuoloOrgv(String nRuoloOrgv) {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(RuoloOrgv.class).add(Restrictions.eq("NRuoloOrgv", nRuoloOrgv));
        try {
            return (RuoloOrgv) criteria.uniqueResult();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return null;
        }
    }

    public List<RuoloOrgv> selectRuoloOrgvs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(RuoloOrgv.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return new ArrayList<RuoloOrgv>();
        }
    }

    public List<TipRlznEntBsnsEntBsns> selectTipRlznEntBsnsEntBsnses() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRlznEntBsnsEntBsns.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return new ArrayList<TipRlznEntBsnsEntBsns>();
        }
    }

    public List<TipSocStruCtrl> selectTipSocStruCtrls() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipSocStruCtrl.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return new ArrayList<TipSocStruCtrl>();
        }
    }

    public List<TipAreaGeogr> selectTipAreaGeogrs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipAreaGeogr.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipAreaGeogr>();
        }
    }

    public List<TipCodEst> selectTipCodEsts() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipCodEst.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipCodEst>();
        }
    }

    public List<TipEntBsns> selectTipEntBsnses() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipEntBsns.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipEntBsns>();
        }
    }

    public List<TipFntEst> selectTipFntEsts() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipFntEst.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipFntEst>();
        }
    }

    public List<TipRuoloEntPrcsGrp> selectTipRuoloEntPrcsGrps() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRuoloEntPrcsGrp.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipRuoloEntPrcsGrp>();
        }
    }

    public List<TipPerm> selectTipPerms() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipPerm.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipPerm>();
        }
    }

    public List<TipLocaz> selectTipLocazes() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipLocaz.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipLocaz>();
        }
    }

    public List<TipLocazSoc> selectTipLocazSocs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipLocazSoc.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipLocazSoc>();
        }
    }

    public List<TipLvlSic> selectTipLvlSics() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipLvlSic.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipLvlSic>();
        }
    }

    @SuppressWarnings(value = "unchecked")
    public List<TipEntPrcs> selectTipEntPrcses() {
        List tipEntPrcses = null;
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipEntPrcs.class);
        try {
            tipEntPrcses = criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            tipEntPrcses = new ArrayList<TipEntPrcs>();
        }
        return tipEntPrcses;
    }

    public List<TipWrkflw> selectTipWrkflws() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipWrkflw.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipWrkflw>();
        }
    }

    public List<TipSet> selectTipSets() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipSet.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipSet>();
        }
    }

    public List<TipSoc> selectTipSocs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipSoc.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipSoc>();
        }
    }

    public TipSet selectTipSet(Long cTipSet) {
        return selectObject(TipSet.class, cTipSet);
    }

    public List<PermClas> selectPermClases() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(PermClas.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<PermClas>();
        }
    }

    public List<TipGrp> selectGrps() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipGrp.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipGrp>();
        }
    }

    public List<TipStaEntPrcs> selectTipStaEntPrcses() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipStaEntPrcs.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipStaEntPrcs>();
        }
    }

    public List<TipAudt> selectTipAudts() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipAudt.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipAudt>();
        }
    }

    public List<TipStaCrcm> selectTipStaCrcms() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipStaCrcm.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipStaCrcm>();
        }
    }

    public List<TipStaUten> selectTipStaUtens() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipStaUten.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipStaUten>();
        }
    }

    @SuppressWarnings("unchecked")
    public List<TipBlkUten> selectTipBlkUtens() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipBlkUten.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipBlkUten>();
        }
    }

    public List<TipRuoloEntPrcsEntBsns> selectTipRuoloEntPrcsEntBsnses() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRuoloEntPrcsEntBsns.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipRuoloEntPrcsEntBsns>();
        }
    }

    public List<TipOrgn> selectTipOrgns() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipOrgn.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipOrgn>();
        }
    }

    public List<TipRuoloGrpUten> selectTipRuoloGrpUtens() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRuoloGrpUten.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipRuoloGrpUten>();
        }
    }

    public TipLocazSoc selectCTipLocazSocByNTipLocazSocMap(String nTipLocazSocMap) {
        TipLocazSoc tipLocazSoc = null;
        if (nTipLocazSocMap != null) {
            nTipLocazSocMap = nTipLocazSocMap.toUpperCase();
        }
        Query query = PersistenceManager.getCurrentSession().createQuery("select tlsm.tipLocazSoc from TipLocazSocMap tlsm " + "where upper(tlsm.NTipLocazSocMap) = :nTipLocazSocMap").setParameter("nTipLocazSocMap", nTipLocazSocMap);
        try {
            tipLocazSoc = (TipLocazSoc) query.uniqueResult();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
        }
        return tipLocazSoc;
    }

    public List<TipRuoloEntPrcsEntPrcs> selectTipRuoloEntPrcsEntPrcses() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRuoloEntPrcsEntPrcs.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Pirla", ex);
            return new ArrayList<TipRuoloEntPrcsEntPrcs>();
        }
    }

    public List<TipRuoloEntBsnsEntBsns> selectTipRuoloEntBsnsEntBsnses() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRuoloEntBsnsEntBsns.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return new ArrayList<TipRuoloEntBsnsEntBsns>();
        }
    }

    public List<TipRuoloEntBsnsGrp> selectTipRuoloEntBsnsGrps() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipRuoloEntBsnsGrp.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return new ArrayList<TipRuoloEntBsnsGrp>();
        }
    }

    public List<TipCfgnKey> selectTipCfgnKeys() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipCfgnKey.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipCfgnKey>();
        }
    }

    public List<CfgnKey> selectCfgnKeys() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(CfgnKey.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<CfgnKey>();
        }
    }

    public List<StaEntPrcsReg> selectStaEntPrcsRegs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(StaEntPrcsReg.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<StaEntPrcsReg>();
        }
    }

    public List<RuoloEntPrcsGrpReg> selectRuoloEntPrcsGrpRegs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(RuoloEntPrcsGrpReg.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<RuoloEntPrcsGrpReg>();
        }
    }

    public List<TipParm> selectTipParms() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipParm.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipParm>();
        }
    }

    public List<TipUtlCntto> selectTipUtlCnttos() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipUtlCntto.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipUtlCntto>();
        }
    }

    public List<TipCnlCntto> selectTipCnlCnttos() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipCnlCntto.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipCnlCntto>();
        }
    }

    public List<PropCntto> selectPropCnttos() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(PropCntto.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<PropCntto>();
        }
    }

    public List<TipOprrMatmc> selectTipOprrMatmcs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipOprrMatmc.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipOprrMatmc>();
        }
    }

    public List<TipOperAudt> selectTipOperAudts() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipOperAudt.class);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<TipOperAudt>();
        }
    }

    /**
	 * @ejb.interface-method
	 */
    public List<TipQlfr> selectTipQlfrs() {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(TipQlfr.class);
        try {
            return criteria.list();
        } catch (Exception ex) {
            logger.debug("Exception", ex);
            return new ArrayList<TipQlfr>();
        }
    }

    public <T extends Object> List<T> selectTips(Class<T> p_tClass) {
        Criteria criteria = PersistenceManager.getCurrentSession().createCriteria(p_tClass);
        try {
            return criteria.list();
        } catch (Exception e) {
            logger.debug("Exception", e);
            return new ArrayList<T>();
        }
    }
}
