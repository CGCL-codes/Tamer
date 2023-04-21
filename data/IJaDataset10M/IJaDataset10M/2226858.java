package org.nakedobjects.plugins.dnd.viewer.border;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.ButtonAction;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractButtonAction;
import org.nakedobjects.plugins.dnd.viewer.content.RootObject;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;

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

        @Override
        public Consent disabled(final View view) {
            return canSave(view);
        }

        public void execute(final Workspace workspace, final View view, final Location at) {
            save(view);
            final ViewSpecification spec = view.getSpecification();
            final View newView = spec.createView(view.getContent(), null);
            workspace.replaceView(view, newView);
        }
    }

    private static Consent canSave(final View view) {
        final NakedObject transientNO = view.getContent().getNaked();
        final SaveState saveState = new SaveState();
        checkFields(saveState, view, transientNO);
        StringBuilder errorBuf = new StringBuilder(saveState.getMessage());
        NakedObjectSpecification viewContentSpec = view.getContent().getSpecification();
        final Consent consent = viewContentSpec.isValid(transientNO);
        if (consent.isVetoed()) {
            if (errorBuf.length() > 0) {
                errorBuf.append("; ");
            }
            errorBuf.append(consent.getReason());
        }
        if (errorBuf.length() == 0) {
            return Allow.DEFAULT;
        } else {
            return new Veto(errorBuf.toString());
        }
    }

    private static void checkFields(final SaveState saveState, final View view, final NakedObject forObject) {
        if (view.getContent().getNaked() != forObject) {
            return;
        }
        final View[] subviews = view.getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            final View fieldView = subviews[i];
            final Content content = fieldView.getContent();
            if (content instanceof RootObject) {
                checkFields(saveState, fieldView, forObject);
            } else if (content instanceof FieldContent) {
                final boolean isMandatory = ((FieldContent) content).isMandatory();
                final NakedObject field = content.getNaked();
                final boolean isFieldEmpty = field == null;
                if (isMandatory && isFieldEmpty) {
                    final String parameterName = ((FieldContent) content).getFieldName();
                    saveState.addMissingField(parameterName);
                } else if (fieldView.getState().isInvalid()) {
                    final String parameterName = ((FieldContent) content).getFieldName();
                    saveState.addInvalidField(parameterName);
                }
            }
        }
    }

    private static class SaveAndCloseAction extends AbstractButtonAction {

        public SaveAndCloseAction() {
            super("Save & Close");
        }

        @Override
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
        final NakedObject transientObject = view.getContent().getNaked();
        try {
            getPersistenceSession().makePersistent(transientObject);
        } catch (final RuntimeException e) {
            LOG.info("exception saving " + transientObject + ", aborting transaction" + e.getMessage());
            throw e;
        }
        return transientObject;
    }

    public SaveTransientObjectBorder(final View view) {
        super(new ButtonAction[] { new SaveAction(), new SaveAndCloseAction(), new CloseAction() }, view);
    }

    private static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }
}
