package research.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import research.entity.EntityFactory;
import research.entity.EntityType;
import research.ui.parameters.NewEntityType;

public class AddEntityHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        EntityType type = (EntityType) NewEntityType.parameters.get(event.getParameter(NewEntityType.ID));
        EntityFactory.openNew(type, null);
        return null;
    }
}
