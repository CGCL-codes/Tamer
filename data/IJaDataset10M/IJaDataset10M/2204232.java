package cn.ekuma.epos.linkshop.datalogic;

import java.util.Date;
import net.xeoh.plugins.base.annotations.PluginImplementation;
import cn.ekuma.data.dao.I_DataLogic;
import cn.ekuma.epos.datalogic.I_CustomerDebtProcesser;
import cn.ekuma.epos.datalogic.dao.CustomerDebtDiaryDAO;
import cn.ekuma.epos.linkshop.bean.CustomerDebtDiary;
import com.openbravo.bean.crm.CustomerScoreDiary;
import com.openbravo.bean.crm.CustomerScoreOPEnum;
import com.openbravo.bean.crm.CustomerStoreValueCard;
import com.openbravo.bean.crm.CustomerStoreValueCardDiary;
import com.openbravo.bean.crm.StoreValueCardOPEnum;
import com.openbravo.bean.crm.voucher.VoucherDiary;
import com.openbravo.bean.crm.voucher.VoucherOPEnum;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.I_Session;

@PluginImplementation
public class LinkShopCustomerDebtProcesser implements I_CustomerDebtProcesser {

    CustomerDebtDiaryDAO customerDebtDiaryDAO;

    @Override
    public void init(I_DataLogic dataLogic) {
    }

    @Override
    public void updateCurDebt(double debt, Date date, String customerId) throws BasicException {
        CustomerDebtDiary customerDebtDiary = new CustomerDebtDiary();
        customerDebtDiary.setCustomerId(customerId);
        customerDebtDiary.setDebt(debt);
        customerDebtDiary.setCurDate(date);
        customerDebtDiaryDAO.insert(customerDebtDiary);
    }

    @Override
    public void init(I_Session s) {
        customerDebtDiaryDAO = new CustomerDebtDiaryDAO(s);
    }

    @Override
    public void updateCurScore(double score, Date date, String customerId) throws BasicException {
    }

    @Override
    public int customerScoreDiaryInsert(CustomerScoreOPEnum opType, CustomerScoreDiary diary) throws BasicException {
        return 0;
    }

    @Override
    public int transferStoreValueCardValue(String descId, CustomerStoreValueCardDiary diary) throws BasicException {
        return 0;
    }

    @Override
    public CustomerStoreValueCard findStoreValueCardByNum(String value) throws BasicException {
        return null;
    }

    @Override
    public int customerStoreValueCardDiaryInsert(StoreValueCardOPEnum opType, CustomerStoreValueCardDiary diary) throws BasicException {
        return 0;
    }

    @Override
    public int voucherDiaryInsert(VoucherOPEnum opType, VoucherDiary diary) throws BasicException {
        return 0;
    }
}
