package modeller.databasedesignmodel.relation.index;

import modeller.databasedesignmodel.Attribute;
import modeller.databasedesignmodel.servicedemand.calculator.IServiceDemandCalculator;
import modeller.databasedesignmodel.servicedemand.calculator.IServiceDemandCalculatorFetcher;
import java.util.List;

/**
 * Created by:  Jason Ye
 * Date:        25/03/2012
 * Time:        16:01
 */
public class UniqueClusteredTreeIndex extends ClusteredTreeIndex {

    public UniqueClusteredTreeIndex(List<Attribute> attributesForIndex) {
        this.m_attributeList = attributesForIndex;
    }

    @Override
    public boolean isUnique() {
        return true;
    }

    @Override
    public IServiceDemandCalculator offerCalculator(IServiceDemandCalculatorFetcher visitorFetcher) {
        return visitorFetcher.fetch(this);
    }
}
