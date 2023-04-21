package modeller.databasedesignmodel.transaction;

import modeller.databasedesignmodel.servicedemand.calculator.IServiceDemandCalculator;
import modeller.databasedesignmodel.servicedemand.IServiceDemand;

/**
 * Created by IntelliJ IDEA.
 * User: Jason Ye
 * Date: 21/01/2012
 * Time: 17:08

 */
public interface IProceduralStatement {

    public String myToString();

    public IServiceDemand acceptCalculation(IServiceDemandCalculator visitor);
}
