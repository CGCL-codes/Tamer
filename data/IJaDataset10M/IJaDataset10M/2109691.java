package jEcoSim.Server.Reporting.iReport;

import java.util.Vector;
import jEcoSim.Model.ProfitAndLossResult;
import jEcoSim.Server.Reporting.FactorEntry;

;

/**
 * 
 * @author wulfaro
 *
 * Factory class, that returns example objects for the report designer iReport.
 * Only necessary for report development.
 */
public class ProfitAndLossBeanDSFactory {

    public static Vector<ProfitAndLossResult> createBean() {
        Vector<ProfitAndLossResult> v = new Vector<ProfitAndLossResult>();
        ProfitAndLossResult p = new ProfitAndLossResult();
        p.setSalesExposure(19598200);
        p.setSalariesAndWages(11103376);
        p.setAmortisation(115000);
        p.setOtherOperationalExpenditure(5805900);
        p.setExtraordinaryExpenditure(0);
        p.setTaxesEarning(1364238);
        p.setAccumulatedDeficit(0);
        p.setCapitalReserveAllocation(0);
        p.setRetainedEarningsInclusion(682119);
        Vector<FactorEntry> material = new Vector<FactorEntry>();
        material.add(new FactorEntry("Material", 10f));
        Vector<FactorEntry> assembly = new Vector<FactorEntry>();
        assembly.add(new FactorEntry("assembly1", 10f));
        Vector<FactorEntry> product = new Vector<FactorEntry>();
        product.add(new FactorEntry("product1", 10f));
        p.setPurchases(material);
        p.setInventoryChangeAssembly(assembly);
        p.setInventoryChangeProduct(product);
        v.add(p);
        return v;
    }
}
