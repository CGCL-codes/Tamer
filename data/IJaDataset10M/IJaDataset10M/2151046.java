package br.usp.iterador.plugin.manifold;

import java.awt.geom.Rectangle2D;
import org.apache.log4j.Logger;
import br.usp.iterador.gui.SimpleIteratorFrame;
import br.usp.iterador.internal.logic.CompileTimeException;
import br.usp.iterador.logic.PulgaAction;
import br.usp.iterador.model.Application;
import br.usp.iterador.plugin.PluginManager;
import br.usp.iterador.plugin.gui.WindowManager;
import br.usp.iterador.task.LongTaskManager;

public class StableManifoldExecution implements PulgaAction {

    private static final Logger LOG = Logger.getLogger(StableManifoldExecution.class);

    public void execute(Application app, WindowManager windows, StableManifoldData data, SimpleIteratorFrame frame, PluginManager pluginManager) throws CompileTimeException {
        Rectangle2D focus = data.getOrderedRectangle();
        LOG.debug("Using rectangle for stable manifold execution " + focus);
        StableManifoldExecutorListener listener = new StableManifoldExecutorListener(app, focus, pluginManager);
        StableManifoldTask task = new StableManifoldTask(app, listener);
        LongTaskManager manager = new LongTaskManager(windows, task, frame);
        manager.start();
        data.getView().reset();
        frame.getCanvas().clearShapes();
    }
}
