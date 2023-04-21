package com.ecmdeveloper.plugin.search.commands;

import org.eclipse.gef.commands.Command;
import com.ecmdeveloper.plugin.search.model.Query;
import com.ecmdeveloper.plugin.search.model.QueryElement;

/**
 * @author ricardo.belfor
 *
 */
public class SetMainQueryCommand extends Command {

    private final QueryElement mainQuery;

    private QueryElement previousMainQuery;

    private final Query query;

    public SetMainQueryCommand(QueryElement mainQuery, Query query) {
        super("Set Main Query");
        this.mainQuery = mainQuery;
        this.query = query;
        this.previousMainQuery = query.getMainQuery();
    }

    @Override
    public void execute() {
        redo();
    }

    @Override
    public void redo() {
        query.setMainQuery(mainQuery);
    }

    @Override
    public void undo() {
        query.setMainQuery(previousMainQuery);
    }
}
