package org.ourgrid.worker.ui.async.gui.actions;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import org.ourgrid.worker.WorkerComponent;
import org.ourgrid.worker.WorkerComponentContextFactory;
import org.ourgrid.worker.ui.async.client.WorkerAsyncInitializer;
import org.ourgrid.worker.ui.async.model.WorkerAsyncUIModel;
import br.edu.ufcg.lsd.commune.container.ContainerContext;
import br.edu.ufcg.lsd.commune.container.contextfactory.PropertiesFileParser;

public class StartWorkerAction extends AbstractAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Component panel;

    public StartWorkerAction(Component contentPane) {
        super("Start");
        this.panel = contentPane;
    }

    public void actionPerformed(ActionEvent arg0) {
        this.setEnabled(false);
        this.panel.setCursor(new java.awt.Cursor(Cursor.WAIT_CURSOR));
        WorkerComponentContextFactory contextFactory = new WorkerComponentContextFactory(new PropertiesFileParser("worker.properties"));
        WorkerAsyncUIModel model = null;
        try {
            ContainerContext context = contextFactory.createContext();
            model = WorkerAsyncInitializer.getInstance().getModel();
            new WorkerComponent(context);
            model.setWorkerStartOnRecovery(true);
            model.workerStarted();
            WorkerAsyncInitializer.getInstance().initComponentClient(context, model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error on worker startup", JOptionPane.ERROR_MESSAGE);
            model.workerStopped();
        }
    }
}
