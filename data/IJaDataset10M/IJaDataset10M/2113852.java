package logicsimulator.evaluator;

/**
 * OrOperator.java
 * @author Mauricio Kanada
 * @author Rafael M Cabrera
 * @since  March 18, 2004, 3:27 PM
 */
public class OrOperator implements LogicComponent {

    java.util.List<LogicComponent> parameters;

    public OrOperator() {
        parameters = new java.util.ArrayList<LogicComponent>();
    }

    /**
     * @see logicsimulator.evaluator.LogicComponent#eval()
     */
    public boolean eval() {
        java.util.Iterator<LogicComponent> it = parameters.iterator();
        LogicComponent p;
        while (it.hasNext()) {
            p = it.next();
            if (p.eval()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return "Operator OR"
     * @see logicsimulator.evaluator.LogicComponent#getName()
     */
    public String getName() {
        return "Operator OR";
    }

    /**
     * @return parameters.size()
     * @see logicsimulator.evaluator.LogicComponent#getParametersNumber()
     */
    public int getParametersNumber() {
        return parameters.size();
    }

    /**
     * @param parameterNumber 
     * @param parameter
     * @see logicsimulator.evaluator.LogicComponent#setParameter(int, logicsimulator.evaluator.LogicComponent)
     */
    public void setParameter(int parameterNumber, LogicComponent parameter) {
        parameters.set(parameterNumber, parameter);
    }

    /**
     * @param parameter
     * @see logicsimulator.evaluator.LogicComponent#addParameter(logicsimulator.evaluator.LogicComponent)
     */
    public void addParameter(LogicComponent parameter) {
        parameters.add(parameter);
    }

    /**
     * @return 2
     * @see logicsimulator.evaluator.LogicComponent#getMinParametersNumber()
     */
    public int getMinParametersNumber() {
        return 2;
    }

    /**
     * @return -1
     * @see logicsimulator.evaluator.LogicComponent#getMaxParametersNumber()
     */
    public int getMaxParametersNumber() {
        return -1;
    }
}
