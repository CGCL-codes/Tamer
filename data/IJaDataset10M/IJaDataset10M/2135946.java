package cn.ekuma.epos.analysis.singleproduct;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.table.TableModel;
import com.openbravo.bean.erp.viewbean.ERPAnalysisInfo;
import com.openbravo.bean.erp.viewbean.FrontAnalysisInfo;
import com.openbravo.bean.erp.viewbean.ProductTransaction;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.query.QBFCompareEnum;
import cn.ekuma.epos.analysis.singleproduct.model.ProductTransactionTableModel;
import cn.ekuma.epos.analysis.singleproduct.model.SingleProductAnalysisTableModel;
import cn.ekuma.epos.businesslogic.OrderUtil;
import cn.ekuma.epos.datalogic.I_DataLogicERP;
import cn.ekuma.util.AbstractTimeGroupCalc;

public class AnalysisModelManager {

    I_DataLogicERP dlSales;

    List<ProductTransaction> productTrans;

    ProductTransactionTableModel dialeyTableModel;

    private ArrayList<String> transactionColumns;

    AbstractTimeGroupCalc groupCalc;

    List<SingleProductAnalysis> sumProductAnalysis;

    SingleProductAnalysisTableModel sumTableModel;

    public AnalysisModelManager(I_DataLogicERP dlSales) {
        this.dlSales = dlSales;
        dialeyTableModel = new ProductTransactionTableModel();
        sumTableModel = new SingleProductAnalysisTableModel();
        transactionColumns = new ArrayList<String>();
        groupCalc = new AbstractTimeGroupCalc<SingleProductAnalysis, ProductTransaction>() {

            @Override
            public SingleProductAnalysis createNew(ProductTransaction inObj, Date date) {
                SingleProductAnalysis obj = new SingleProductAnalysis();
                obj.setCurDate(date);
                obj.addProductTransaction(inObj);
                return obj;
            }

            @Override
            public Date getKey(ProductTransaction inObj) {
                return inObj.getCurDate();
            }

            @Override
            public void op(SingleProductAnalysis total, ProductTransaction inObj) {
                total.addProductTransaction(inObj);
            }
        };
    }

    public void query(Object[] beforeFilterParas) {
        List<ERPAnalysisInfo> datas;
        List<FrontAnalysisInfo> datas1;
        productTrans = new ArrayList<ProductTransaction>();
        transactionColumns.clear();
        try {
            datas = dlSales.list(ERPAnalysisInfo.class, createERPFilter(beforeFilterParas));
            datas1 = dlSales.list(FrontAnalysisInfo.class, beforeFilterParas);
        } catch (BasicException e) {
            e.printStackTrace();
            return;
        }
        String realOrderName;
        ProductTransaction pTran;
        for (ERPAnalysisInfo info : datas) {
            pTran = new ProductTransaction();
            pTran.setCurDate(info.getCurDate());
            pTran.multiply = info.getUnit();
            pTran.orderType = info.getOrderType();
            pTran.orderTypeName = info.getOrderTypeName();
            pTran.total = info.getTotalPrice();
            productTrans.add(pTran);
            realOrderName = pTran.getOrderTypeName() + "(" + (pTran.getOrderType() == 0 ? OrderUtil.ORDERTYPE_Normal : OrderUtil.ORDERTYPE_Revoerse) + ")";
            if (!transactionColumns.contains(realOrderName)) transactionColumns.add(realOrderName);
        }
        for (FrontAnalysisInfo info : datas1) {
            pTran = new ProductTransaction();
            pTran.setCurDate(info.getCurDate());
            pTran.multiply = info.getUnit();
            pTran.orderType = info.getOrderType();
            pTran.orderTypeName = "POS";
            pTran.total = info.getTotalPrice();
            productTrans.add(pTran);
            realOrderName = pTran.getOrderTypeName() + "(" + (pTran.getOrderType() == 0 ? OrderUtil.ORDERTYPE_Normal : OrderUtil.ORDERTYPE_Revoerse) + ")";
            if (!transactionColumns.contains(realOrderName)) transactionColumns.add(realOrderName);
        }
        dialeyTableModel.resert(productTrans);
    }

    public ProductTransactionTableModel getDialeyTableModel() {
        return dialeyTableModel;
    }

    public void setDialeyTableModel(ProductTransactionTableModel dialeyTableModel) {
        this.dialeyTableModel = dialeyTableModel;
    }

    private Object[] createERPFilter(Object[] inFilter) {
        Object[] afilter = new Object[17];
        afilter[0] = QBFCompareEnum.COMP_NONE;
        afilter[1] = null;
        for (int i = 0; i < 15; i++) afilter[i + 2] = inFilter[i];
        return afilter;
    }

    public TableModel getGroupTableModel() {
        return sumTableModel;
    }

    public void reCall(int i) {
        sumProductAnalysis = groupCalc.calc(productTrans, i);
        sumTableModel.resert(transactionColumns, sumProductAnalysis);
    }
}
