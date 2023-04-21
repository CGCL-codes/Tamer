package com.ecmdeveloper.plugin.search.layout;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

/**
 * @author ricardo.belfor
 *
 */
public abstract class AbstractLayoutCommand extends Command {

    public abstract void setConstraint(Rectangle rect);

    public abstract void setModel(Object model);
}
