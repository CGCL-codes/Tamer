package com.nokia.ats4.appmodel.perspective.modeldesign.controller;

import com.nokia.ats4.appmodel.MainApplication;
import com.nokia.ats4.appmodel.event.KendoEvent;
import com.nokia.ats4.appmodel.event.KendoEventListener;
import com.nokia.ats4.appmodel.exception.ExceptionHandler;
import com.nokia.ats4.appmodel.exception.KendoException;
import com.nokia.ats4.appmodel.grapheditor.event.CreateUseCasePathEvent;
import com.nokia.ats4.appmodel.grapheditor.event.SelectCellEvent;
import com.nokia.ats4.appmodel.grapheditor.swing.JGraphEditor;
import com.nokia.ats4.appmodel.grapheditor.swing.UseCaseDialog;
import com.nokia.ats4.appmodel.main.swing.SelectCellsDialog;
import com.nokia.ats4.appmodel.model.domain.DomainObject;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.model.domain.Transition;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCase;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCaseModel;
import com.nokia.ats4.appmodel.model.impl.MainApplicationModel;
import com.nokia.ats4.appmodel.perspective.DesignPerspective;
import com.nokia.ats4.appmodel.util.UseCasePathGenerator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.jgraph.graph.DefaultGraphCell;

/**
 * CreateUseCaseCommand handles the creation of a new Use Cases.
 *
 * @author Hannu-Pekka Hakam&auml;ki
 * @version $Revision: 40 $
 */
public class CreateUseCaseCommand implements KendoEventListener {

    /** Logging */
    private static final Logger log = Logger.getLogger(CreateUseCaseCommand.class);

    private MainApplicationModel appModel = null;

    private State StartState = null;

    private SelectCellsDialog selectionDialog = null;

    private Thread delayThread = null;

    /**
     * Creates a new instance of CreateUseCasePathCommand
     */
    public CreateUseCaseCommand(MainApplicationModel model) {
        this.appModel = model;
    }

    @Override
    public void processEvent(KendoEvent event) {
        log.debug("processing CreateUseCasePathEvent");
        if (event instanceof CreateUseCasePathEvent) {
            CreateUseCasePathEvent evt = (CreateUseCasePathEvent) event;
            UseCasePathGenerator generator = new UseCasePathGenerator();
            if (evt.getStartOrEnd().equals(CreateUseCasePathEvent.CreationOption.PATH_FROM_SET)) {
                List<State> states = new ArrayList<State>();
                Object[] cellObjects = evt.getCells();
                for (Object cellObject : cellObjects) {
                    DefaultGraphCell cell = (DefaultGraphCell) cellObject;
                    Object userObject = cell.getUserObject();
                    if (userObject instanceof State) {
                        states.add((State) userObject);
                    }
                }
                addCreatedPath(generator.generateUseCase(states));
            } else if (evt.getStartOrEnd().equals(CreateUseCasePathEvent.CreationOption.PATH_START)) {
                this.StartState = (State) ((DefaultGraphCell) evt.getCells()[0]).getUserObject();
            } else if (evt.getStartOrEnd().equals(CreateUseCasePathEvent.CreationOption.PATH_END) && this.StartState != null) {
                addCreatedPath(generator.createPathsBetweenNodes(this.StartState, (DomainObject) ((DefaultGraphCell) evt.getCells()[0]).getUserObject()));
            } else if (evt.getStartOrEnd().equals(CreateUseCasePathEvent.CreationOption.ORDERER_PATH)) {
                Transition t = null;
                if (evt.getCells().length == 1 && evt.getCells()[0] instanceof DefaultGraphCell) {
                    DefaultGraphCell cell = (DefaultGraphCell) evt.getCells()[0];
                    if (cell.getUserObject() instanceof Transition) {
                        t = (Transition) cell.getUserObject();
                    }
                }
                selectionDialog = new SelectCellsDialog(MainApplication.getMainWindow(), false, this, t);
            }
            ;
        } else if (selectionDialog != null && selectionDialog.isVisible() && event instanceof SelectCellEvent) {
            if (this.delayThread == null || !this.delayThread.isAlive() && ((SelectCellEvent) event).getUserObject() instanceof Transition) {
                selectionDialog.addObjectToList((Transition) ((SelectCellEvent) event).getUserObject());
                this.delayThread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                this.delayThread.start();
            }
        }
    }

    /**
     * This asks the name for usecase and adds it to model. This also displays
     * a preview of created path in grap.
     *
     * @param paths -a list of usecase paths to be added to model
     */
    private void addCreatedPath(List<List<Transition>> paths) {
        DesignPerspective p = (DesignPerspective) this.appModel.getActivePerspective();
        JGraphEditor je = p.getActiveGraphEditor().getJGraphEditor();
        UseCasePathGenerator generator = new UseCasePathGenerator();
        if (paths != null && paths.size() > 0) {
            Object selection = getName();
            UseCaseModel ucModel = this.appModel.getActiveProject().getUseCaseModel();
            if (selection != null) {
                if (selection instanceof UseCase) {
                    generator.displayPreview(paths, je);
                    for (int i = 0; i < paths.size(); i++) {
                        try {
                            ((UseCase) selection).createPath(paths.get(i));
                        } catch (IllegalArgumentException e) {
                            log.warn("Error while creating path", e);
                            throw new KendoException("Path begins with invalid event", KendoException.PATH_BEGINS_WITH_INVALID_EVENT, e);
                        }
                    }
                } else {
                    String name = "" + (String) selection;
                    Iterator<List<Transition>> iter = paths.iterator();
                    ucModel.createUseCase(name, iter, -1);
                    generator.displayPreview(paths, je);
                    generator.displayPreview(paths, je);
                }
            }
        }
    }

    public void addPathFromDialog(List<Transition> transitions) {
        UseCasePathGenerator generator = new UseCasePathGenerator();
        try {
            addCreatedPath(generator.generateUseCasePath(transitions));
        } catch (KendoException ex) {
            ExceptionHandler handler = new ExceptionHandler(MainApplication.getMainWindow());
            handler.handle(ex);
        }
        selectionDialog = null;
    }

    /**
     * Displays a pop-up for entering a name for the new model.
     */
    private Object getName() {
        Object selection = "";
        List<UseCase> cases = this.appModel.getActiveProject().getUseCaseModel().getUseCases();
        Object[] o = new Object[cases.size()];
        int i = 0;
        for (UseCase u : cases) {
            o[i] = u;
            i++;
        }
        UseCaseDialog d = new UseCaseDialog(MainApplication.getMainWindow(), true, o, false);
        d.setLocationRelativeTo(this.appModel.getActivePerspective().getView().getSwingComponent());
        selection = d.setVisible();
        return selection;
    }
}
