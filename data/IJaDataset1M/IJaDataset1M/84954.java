package com.openbravo.def;

import com.openbravo.bean.shard.IncludeTaxs;
import java.util.List;

/**
 *
 * @author Administrator
 */
public interface I_CalcTaxes {

    public static int TAXTYPE_Ticket = 0;

    public static int TAXTYPE_Order = 1;

    public List<I_TaxCalcLine> getTaxCalcLines();

    public void setTaxes(List<IncludeTaxs> l);

    public int getTaxType();
}
