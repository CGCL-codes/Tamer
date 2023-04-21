package de.fzi.injectj.script.syntax;

/**
 * TODO mies : documentation
 * @author <a href="mies@fzi.de">Sebastian Mies</a>
 */
public class Quantifier {

    private AssignExpression list;

    private String variableId;

    private boolean foreach;

    /**
	 * 
	 */
    public Quantifier(String variableId, AssignExpression list, boolean foreach) {
        this.variableId = variableId;
        this.list = list;
        this.foreach = foreach;
    }

    public AssignExpression getList() {
        return list;
    }

    public boolean isForeach() {
        return foreach;
    }

    public boolean isExits() {
        return !foreach;
    }

    public String getVariableId() {
        return variableId;
    }
}
