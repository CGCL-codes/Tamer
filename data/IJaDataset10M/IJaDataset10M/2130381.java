package org.nakedobjects.nos.client.dnd.border;

import org.apache.log4j.Logger;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.NakedValue;
import org.nakedobjects.noa.persist.NakedObjectPersistor;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.core.reflect.Veto;
import org.nakedobjects.nos.client.dnd.ButtonAction;
import org.nakedobjects.nos.client.dnd.Content;
import org.nakedobjects.nos.client.dnd.FieldContent;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewSpecification;
import org.nakedobjects.nos.client.dnd.Workspace;
import org.nakedobjects.nos.client.dnd.action.AbstractButtonAction;
import org.nakedobjects.nos.client.dnd.drawing.Location;

class SaveTransientObjectBorder extends ButtonBorder {

    private static final Logger LOG = Logger.getLogger(SaveTransientObjectBorder.class);

    private static class CloseAction extends AbstractButtonAction {

        public CloseAction() {
            super("Discard");
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            close(workspace, view);
        }
    }

    private static class SaveAction extends AbstractButtonAction {

        public SaveAction() {
            super("Save");
        }

        public Consent disabled(final View view) {
            return canSave(view);
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            save(view);
            ViewSpecification spec = view.getSpecification();
            View newView = spec.createView(view.getContent(), null);
            workspace.replaceView(view, newView);
        }
    }

    private static Consent canSave(final View view) {
        View[] subviews = view.getSubviews();
        StringBuffer missingFields = new StringBuffer();
        StringBuffer invalidFields = new StringBuffer();
        for (int i = 0; i < subviews.length; i++) {
            View field = subviews[i];
            Content content = field.getContent();
            if (content instanceof FieldContent) {
                if (((FieldContent) content).isMandatory() && (content.getNaked() == null || (content.getNaked() instanceof NakedValue && ((NakedValue) content.getNaked()).isEmpty()))) {
                    String parameterName = ((FieldContent) content).getFieldName();
                    if (missingFields.length() > 0) {
                        missingFields.append(", ");
                    }
                    missingFields.append(parameterName);
                } else if (field.getState().isInvalid()) {
                    String parameterName = ((FieldContent) content).getFieldName();
                    if (invalidFields.length() > 0) {
                        invalidFields.append(", ");
                    }
                    invalidFields.append(parameterName);
                }
            }
        }
        String error = "";
        if (missingFields.length() > 0) {
            if (error.length() > 0) {
                error += "; ";
            }
            error += "Fields needed: " + missingFields;
        }
        if (invalidFields.length() > 0) {
            if (error.length() > 0) {
                error += "; ";
            }
            error += "Invalid fields: " + invalidFields;
        }
        NakedObject transientObject = (NakedObject) view.getContent().getNaked();
        Consent consent = view.getContent().getSpecification().isPersistable(transientObject);
        if (consent.isVetoed()) {
            if (error.length() > 0) {
                error += "; ";
            }
            error += consent.getReason();
        }
        if (error.length() == 0) {
            return Allow.DEFAULT;
        } else {
            return new Veto(error);
        }
    }

    private static class SaveAndCloseAction extends AbstractButtonAction {

        public SaveAndCloseAction() {
            super("Save & Close");
        }

        public Consent disabled(final View view) {
            return canSave(view);
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            save(view);
            close(workspace, view);
        }
    }

    private static void close(final Workspace workspace, final View view) {
        view.dispose();
    }

    private static NakedObject save(final View view) {
        NakedObject transientObject = (NakedObject) view.getContent().getNaked();
        NakedObjectPersistor persistor = NakedObjectsContext.getObjectPersistor();
        try {
            persistor.startTransaction();
            persistor.makePersistent(transientObject);
            persistor.endTransaction();
        } catch (RuntimeException e) {
            LOG.info("exception saving " + transientObject + ", aborting transaction", e);
            try {
                persistor.abortTransaction();
            } catch (Exception e2) {
                LOG.error("failure during abort", e2);
            }
            throw e;
        }
        return transientObject;
    }

    public SaveTransientObjectBorder(final View view) {
        super(new ButtonAction[] { new SaveAction(), new SaveAndCloseAction(), new CloseAction() }, view);
    }
}
