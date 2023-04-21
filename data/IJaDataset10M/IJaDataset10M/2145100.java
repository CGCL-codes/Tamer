package sg.edu.nus.comp.simTL.engine;

import org.apache.log4j.Logger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import sg.edu.nus.comp.simTL.engine.IModel.ModelType;
import sg.edu.nus.comp.simTL.engine.exceptions.SimTLException;
import sg.edu.nus.comp.simTL.engine.exceptions.SynchronizingException;
import sg.edu.nus.comp.simTL.engine.tracing.ModelCorrespondence;
import sg.edu.nus.comp.simTL.engine.tracing.Reference2Attribute;
import sg.edu.nus.comp.simTL.engine.tracing.Reference2Element;
import sg.edu.nus.comp.simTL.engine.tracing.ReferenceFromParent;

/**
 * @author Marcel B�hme
 * Comment created on: 07-Jul-2010
 */
public class SimTLEngine implements IModelChangedListener {

    private static Logger log = Logger.getLogger(SimTLEngine.class);

    private final ITemplate template;

    private final URI outURI;

    private final IDecisionMaker decisionMaker;

    private boolean stillNotifying_temp;

    private ISynchronizer synchronizer;

    private IModel templateInstance;

    public SimTLEngine(ITemplate template, URI outURI, IDecisionMaker decisionMaker) throws SimTLException {
        this.template = template;
        this.outURI = outURI;
        this.decisionMaker = decisionMaker;
        replay();
    }

    private void replay() throws SimTLException {
        stillNotifying_temp = false;
        IInterpreter interpreter = SimTLFactory.createInterpreter();
        InterpretationResult ir = interpreter.interprete(template, outURI);
        templateInstance = ir.getInstance();
        ModelCorrespondence modelCorrespondence = ir.getModelCorrespondence();
        if (templateInstance == null) {
            throw new SynchronizingException("TemplateInstanceModel is null. Stop synchronization.");
        } else if (modelCorrespondence == null || modelCorrespondence.isEmpty()) {
            throw new SynchronizingException("No explicit ModelCorrespondence given. Stop synchronization.");
        }
        synchronizer = SimTLFactory.createSynchronizer();
        synchronizer.startSynchronization(template, templateInstance, modelCorrespondence, decisionMaker);
        templateInstance.addListener(this);
    }

    @Override
    public void notifyModelChanged(IModel sourceModel, Notification notification) throws SimTLException {
        if (stillNotifying_temp) {
            log.debug("Another model initialized these notifications. " + "As there are no indirect Views we can " + "ignore changeNotifactions while the initial changeNotification: " + sourceModel.getName() + " :: " + notification);
            throw new SynchronizingException("BUG: May not happen, as method is synchronized");
        }
        stillNotifying_temp = true;
        if (sourceModel.getModelType() == ModelType.TEMPLATE_MODEL || sourceModel.getModelType() == ModelType.INPUT_MODEL) {
            log.warn("Changes in templateModel or instanceModel are ignored, yet");
            return;
        }
        Object notifier = notification.getNotifier();
        if (notifier instanceof EObject) {
            EObject source = (EObject) notifier;
            EStructuralFeature newSFInSource = (EStructuralFeature) notification.getFeature();
            int position = notification.getPosition() == Notification.NO_INDEX ? Util.NO_POSITION : notification.getPosition();
            ReferenceFromParent referenceFromParent = null;
            if (newSFInSource instanceof EAttribute) {
                referenceFromParent = new Reference2Attribute(source, (EAttribute) newSFInSource);
            } else {
                referenceFromParent = new Reference2Element(source, (EReference) newSFInSource, template);
            }
            switch(notification.getEventType()) {
                case Notification.ADD:
                    {
                    }
                case Notification.SET:
                    {
                        synchronizer.addStructuralFeature(sourceModel, referenceFromParent, position, notification.getNewValue());
                    }
                    break;
                case Notification.UNSET:
                    {
                    }
                case Notification.REMOVE:
                    {
                        synchronizer.removeStructuralFeature(sourceModel, referenceFromParent, position, notification.getOldValue());
                    }
                    break;
                case Notification.REMOVE_MANY:
                    {
                        log.warn("REMOVE_MANY not yet implemented");
                    }
                    break;
                case Notification.ADD_MANY:
                    {
                        log.warn("ADD_MANY not yet implemented");
                    }
                    break;
                case Notification.MOVE:
                    {
                        log.warn("MOVE not yet implemented");
                    }
                    break;
                default:
                    log.warn("Unknown notification type: " + notification.getEventType());
            }
        } else {
            log.warn("Notifier is no EObject: " + notifier);
        }
        stillNotifying_temp = false;
    }

    public IModel getTemplateInstance() {
        return templateInstance;
    }
}
