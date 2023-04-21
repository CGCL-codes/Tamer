package com.zzsoft.app.stock.analyseengine.arithmetic;

import java.util.Map;
import java.util.Set;
import com.zzsoft.app.stock.agent.StockTradeStatement2;
import com.zzsoft.app.stock.tradeinformation.TradeKDayAdm;
import com.zzsoft.app.stock.tradeinformation.TradeInforModI;

public class Simple1 implements OpStatementI {

    TradeInforModI tradeInforMod;

    public StockTradeStatement2 makeStatement() {
        mDayK = tradeInforMod.getMDayK();
        Set<String> codes = mDayK.keySet();
        if (codes == null) return null;
        for (String code : codes) {
            TradeKDayAdm tradeKDayAdm = mDayK.get(code);
        }
    }
}
