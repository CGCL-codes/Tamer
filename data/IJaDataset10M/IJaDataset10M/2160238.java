package cn.ekuma.pos.service.client;

import java.net.MalformedURLException;
import java.util.Date;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import com.caucho.hessian.client.HessianProxyFactory;
import com.openbravo.bean.crm.CustomerScoreDiary;
import com.openbravo.bean.crm.CustomerScoreOPEnum;
import com.openbravo.bean.crm.CustomerStoreValueCard;
import com.openbravo.bean.crm.CustomerStoreValueCardDiary;
import com.openbravo.bean.crm.StoreValueCardOPEnum;
import com.openbravo.bean.crm.voucher.VoucherDiary;
import com.openbravo.bean.crm.voucher.VoucherOPEnum;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.I_Session;
import com.openbravo.pos.base.AppConfig;
import cn.ekuma.data.dao.I_DataLogic;
import cn.ekuma.epos.datalogic.I_CustomerDebtProcesser;
import cn.ekuma.epos.datalogic.I_DataLogicSales;

@PluginImplementation
public class CustomerDebtProcesser implements I_CustomerDebtProcesser {

    String url;

    I_DataLogicSales basic;

    HessianProxyFactory factory;

    @Override
    public void init(I_DataLogic dataLogic) {
    }

    @Override
    public void updateCurDebt(double debt, Date date, String customerId) throws BasicException {
        try {
            if (basic != null) basic.updateCurDebt(debt, date, customerId); else throw new BasicException("no connet to .." + url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
    }

    @Override
    public void init(I_Session s) {
        url = AppConfig.getAppProperty(AppConfig.SERVICE_URL);
        factory = new HessianProxyFactory();
        try {
            basic = (I_DataLogicSales) factory.create(I_DataLogicSales.class, url);
        } catch (MalformedURLException e) {
            basic = null;
            e.printStackTrace();
        }
    }

    @Override
    public void updateCurScore(double score, Date date, String customerId) throws BasicException {
        try {
            if (basic != null) basic.updateCurScore(score, date, customerId); else throw new BasicException("no connet to .." + url);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
    }

    @Override
    public int customerScoreDiaryInsert(CustomerScoreOPEnum opType, CustomerScoreDiary diary) throws BasicException {
        try {
            if (basic != null) return basic.customerScoreDiaryInsert(opType, diary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
        throw new BasicException("no connet to .." + url);
    }

    @Override
    public int transferStoreValueCardValue(String descId, CustomerStoreValueCardDiary diary) throws BasicException {
        try {
            if (basic != null) return basic.transferStoreValueCardValue(descId, diary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
        throw new BasicException("no connet to .." + url);
    }

    @Override
    public CustomerStoreValueCard findStoreValueCardByNum(String value) throws BasicException {
        try {
            if (basic != null) return basic.findStoreValueCardByNum(value);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
        throw new BasicException("no connet to .." + url);
    }

    @Override
    public int customerStoreValueCardDiaryInsert(StoreValueCardOPEnum opType, CustomerStoreValueCardDiary diary) throws BasicException {
        try {
            if (basic != null) return basic.customerStoreValueCardDiaryInsert(opType, diary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
        throw new BasicException("no connet to .." + url);
    }

    @Override
    public int voucherDiaryInsert(VoucherOPEnum opType, VoucherDiary diary) throws BasicException {
        try {
            if (basic != null) return basic.voucherDiaryInsert(opType, diary);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BasicException(e);
        }
        throw new BasicException("no connet to .." + url);
    }
}
