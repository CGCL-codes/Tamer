package uk.co.ordnancesurvey.confluence.ui.view.concept;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.semanticweb.owlapi.model.OWLClass;
import uk.ac.leeds.comp.ui.bridge.SwingBridge;
import uk.ac.leeds.comp.ui.factory.UIControllerFactory;
import uk.ac.leeds.comp.ui.factory.UIModelFactory;
import uk.co.ordnancesurvey.confluence.IRooEditorKit;
import uk.co.ordnancesurvey.confluence.ui.IRooWorkspace;
import uk.co.ordnancesurvey.confluence.ui.rabbitframe.ProtegeSelectedRootObjectRbtFrameModelImpl;
import uk.co.ordnancesurvey.rabbitgui.frame.SwingRbtFrameController;

/**
 * This class defines a view component for editing rabbit sentences linked to a
 * particular OWLClass.
 * 
 * Note that this component actually shows the selected entity, so it's not just
 * usable for concepts, but also for relations. In fact TODO: we should change
 * its parent from {@link AbstractConfluenceClassViewComponent} to
 * {@link org.protege.editor.owl.ui.view.AbstractOWLViewComponent} since we
 * don't use the selection detection provided by the current parent.
 * 
 * @author rdenaux
 * 
 */
public class RabbitConceptEditorViewComponent extends AbstractConfluenceClassViewComponent {

    private static final long serialVersionUID = -3491392714944539882L;

    private static final Logger log = Logger.getLogger(RabbitConceptEditorViewComponent.class.getName());

    private static final Level logLevel = Level.FINE;

    public static final String VIEW_ID = "RabbitConceptEditorViewComponent";

    private SwingRbtFrameController fController;

    private ProtegeSelectedRootObjectRbtFrameModelImpl fModel;

    /**
	 * This class cleans up any resources used by this view component.
	 * 
	 * @see org.protege.editor.owl.ui.view.AbstractOWLSelectionViewComponent#disposeView()
	 */
    @Override
    public void disposeView() {
        if (fController != null) {
            fController.dispose();
        }
        if (fModel != null) {
            fModel.dispose();
        }
    }

    private void initController() {
        IRooEditorKit rooEdKit = getRooEditorKit();
        log.log(logLevel, "initialising rabbit concept editor with rooEdKit " + rooEdKit);
        IRooWorkspace workspace = rooEdKit.getRooWorkspace();
        UIModelFactory modelFactory = workspace.getUIModelFactory();
        UIControllerFactory controllerFactory = workspace.getUIControllerFactory();
        fModel = modelFactory.createModel(ProtegeSelectedRootObjectRbtFrameModelImpl.class);
        fController = controllerFactory.createController(SwingRbtFrameController.class);
        fController.setModel(fModel);
        add(SwingBridge.createScrollPane(SwingBridge.getAsAWTComponent(fController.getView())), BorderLayout.CENTER);
    }

    /**
	 * Initialise the view components.
	 * 
	 * @see org.protege.editor.owl.ui.view.AbstractOWLClassViewComponent#initialiseClassView()
	 */
    @Override
    public void initialiseConfluenceClassView() {
        setName("RabbitConceptEditorViewComponent");
        setLayout(new BorderLayout());
        initController();
    }

    /**
	 * Use this method to update this view whenever a new class has been
	 * selected.
	 * 
	 * @see org.protege.editor.owl.ui.view.AbstractOWLClassViewComponent#updateView(org.semanticweb.owlapi.model.OWLClass)
	 */
    @Override
    protected OWLClass updateView(OWLClass selectedClass) {
        return selectedClass;
    }
}
