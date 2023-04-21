package net.sourceforge.javautil.gui.swing.handler;

import java.awt.Component;
import net.sourceforge.javautil.gui.handler.GUITagConfig;

/**
 * The base for most {@link ISwingletHandler}'s.
 * 
 * @author elponderador
 * @author $Author$
 * @version $Id$
 */
public abstract class SwingletHandlerBase<L extends ILayoutApplicator> implements ISwingletHandler<L> {

    protected final GUITagConfig config;

    public SwingletHandlerBase(GUITagConfig config) {
        this.config = config;
    }

    public GUITagConfig getConfig() {
        return config;
    }
}
