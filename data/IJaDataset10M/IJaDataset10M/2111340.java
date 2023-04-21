package net.sourceforge.fluxion.runcible.io.command.impl;

import net.sourceforge.fluxion.runcible.io.command.ChangeCommand;
import net.sourceforge.fluxion.runcible.DataClause;
import net.sourceforge.fluxion.runcible.Rule;
import net.sourceforge.fluxion.runcible.impl.DataClauseImpl;
import net.sourceforge.fluxion.runcible.impl.SelectDataValue;
import java.util.List;
import java.net.URI;

/**
 * Javadocs go here.
 *
 * @author Tony Burdett
 * @version 1.0
 * @date 05-Jun-2007
 */
public class AddDataClauseCommand implements ChangeCommand {

    private Rule rule;

    private DataClause clause;

    public AddDataClauseCommand(Rule ruleToAddFactsTo, String variableId, URI dataPropertyURI) {
        this.rule = ruleToAddFactsTo;
        SelectDataValue selection = new SelectDataValue();
        selection.setDataPropertyURI(dataPropertyURI);
        clause = new DataClauseImpl();
        clause.setVariableID(variableId);
        clause.setDataSelection(selection);
    }

    public void execute() {
        rule.addDataClause(clause);
    }

    public void undo() {
        List<DataClause> clauses = rule.getDataClauses();
        clauses.remove(clause);
        rule.setDataClauses(clauses);
    }

    public DataClause getClause() {
        return clause;
    }
}
