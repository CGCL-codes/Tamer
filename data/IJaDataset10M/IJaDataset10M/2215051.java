package genetic.component.statementlist.mutator;

import genetic.BuildException;
import genetic.Foundation;
import genetic.GeneticFoundation;
import genetic.MutatorAction;
import genetic.component.statement.Statement;
import genetic.component.statementlist.StatementList;
import java.util.List;

/**
 *
 * @author Calvin Ashmore
 */
public class MutateStatement extends MutatorAction<StatementList> {

    public MutateStatement() {
        addMeta("selectionWeight", 3.0);
    }

    @Override
    public boolean mutate(StatementList target) throws BuildException {
        List<Statement> statements = target.getStatements();
        GeneticFoundation foundation = Foundation.getInstance();
        int index = foundation.getBuilderRandom().nextInt(statements.size());
        return foundation.getStatementMutator().mutate(statements.get(index));
    }
}
