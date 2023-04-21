package parameterFactories;

/**
 * A parameter factory for the payroll processing scenario: It
 * calculates the difference of the original salary payment
 * and the new one, i.e. it calculates the amount of money that
 * has to be refunded.
 * 
 * @author Michael Sch�fer
 *
 */
public class RefundSalaryDifferenceParameterFactory implements IParameterFactory {

    public Object[] createParameters(Object[] originalParameters, Object[] newParameters) {
        Object[] parameters = new Object[3];
        parameters[0] = new Integer(0);
        parameters[1] = new Integer(0);
        parameters[2] = new Integer(0);
        try {
            int originalAmount = (Integer) originalParameters[2];
            int newAmount = (Integer) newParameters[2];
            int difference = originalAmount - newAmount;
            if (difference > 0) {
                parameters[2] = new Integer(difference);
            }
            parameters[0] = originalParameters[1];
            parameters[1] = originalParameters[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters;
    }
}
