package org.germinus.cardea.MuleServiceBundle.components;

import org.germinus.cardea.MuleServiceBundle.impl.ContextBean;

public class ReceiverComponent {

    public String getStock(StockString stock) {
        int numeroStock;
        String typeMedicine = stock.getMedicineType();
        ContextBean cBean = ContextBean.getContextBean();
        numeroStock = cBean.getStock(typeMedicine);
        return "El stock para el medicamento " + typeMedicine + " es de:" + numeroStock + " unidades";
    }
}
