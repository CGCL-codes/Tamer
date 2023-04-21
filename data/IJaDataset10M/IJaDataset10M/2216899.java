package com.anaxima.eslink.sasparser.nodes;

import com.anaxima.eslink.sascode.DatasetDefinition;
import com.anaxima.eslink.sasparser.sasbase.SasBaseParser;

public class ASTDataStepStatement extends com.anaxima.eslink.sasparser.nodes.ASTNode {

    private DatasetDefinition _targetDatasetDefinition;

    public ASTDataStepStatement(int id) {
        super(id);
    }

    public ASTDataStepStatement(SasBaseParser p, int id) {
        super(p, id);
    }

    public static Node jjtCreate(int id) {
        return new ASTDataStepStatement(id);
    }

    public static Node jjtCreate(SasBaseParser p, int id) {
        return new ASTDataStepStatement(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(SasBaseParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    /**
     * This implementation returns "data &lt;target&gt;" as label.
     */
    @Override
    public String getLabel() {
        return "data " + _targetDatasetDefinition.getName();
    }

    /**
	 * Sets the target dataset definition.
	 * @param argDsd
	 */
    public void setTargetDatasetDefinition(DatasetDefinition argDsd) {
        _targetDatasetDefinition = argDsd;
    }

    /**
	 * @return the targetDatasetDefinition
	 */
    public DatasetDefinition getTargetDatasetDefinition() {
        return _targetDatasetDefinition;
    }
}
