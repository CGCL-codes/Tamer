package org.remus.infomngmnt.search.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.remus.infomngmnt.search.provider.SearchPlugin;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class NextSearchResultHandler extends AbstractHandler {

    public Object execute(ExecutionEvent event) throws ExecutionException {
        SearchPlugin.getPlugin().getSearchContext().openNextResult();
        return null;
    }
}
