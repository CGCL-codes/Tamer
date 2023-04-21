package de.ah7.lib.db.sql;

import de.ah7.lib.render.Renderable;
import java.util.Collection;

/**
 * @author Andreas Huber <dev@ah7.de>
 */
public interface TableReference extends Renderable {

    public Collection<Table> getTables();
}
