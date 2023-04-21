package de.jdufner.sudoku.solver.strategy.simple;

import java.util.ArrayList;
import java.util.Collection;
import de.jdufner.sudoku.commands.Command;
import de.jdufner.sudoku.commands.RemoveCandidatesCommand.RemoveCandidatesCommandBuilder;
import de.jdufner.sudoku.solver.strategy.Strategy;
import de.jdufner.sudoku.solver.strategy.configuration.StrategyNameEnum;

/**
 * 
 * @author <a href="mailto:jdufner@users.sf.net">Jürgen Dufner</a>
 * @since 2010-03-08
 * @version $Revision$
 */
public final class SimpleBlockStrategyTest extends AbstractSimpleStrategyTestCase {

    @Override
    protected Strategy getStrategy() {
        return new SimpleBlockStrategy(sudoku);
    }

    @Override
    protected Collection<Command> getCommands() {
        final Collection<Command> commands = new ArrayList<Command>();
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 7, 6).addCandidate(1, 2).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 7, 7).addCandidate(1, 2).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 7, 8).addCandidate(1, 2).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 8, 6).addCandidate(1, 2).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 6, 3).addCandidate(4, 5, 8, 9).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 6, 4).addCandidate(4, 5, 8, 9).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 7, 3).addCandidate(4, 5, 8, 9).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 7, 5).addCandidate(4, 5, 8, 9).build());
        commands.add(new RemoveCandidatesCommandBuilder(StrategyNameEnum.SIMPLE, 8, 4).addCandidate(4, 5, 8, 9).build());
        return commands;
    }
}
