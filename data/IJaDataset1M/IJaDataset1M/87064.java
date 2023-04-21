package org.mbari.vars.knowledgebase.ui.actions;

import javax.swing.JOptionPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.mbari.vars.dao.DAOException;
import org.mbari.vars.knowledgebase.model.Concept;
import org.mbari.vars.knowledgebase.model.LinkTemplate;
import org.mbari.vars.knowledgebase.model.dao.ConceptDAO;
import org.mbari.vars.util.AppFrameDispatcher;
import vars.knowledgebase.IConcept;

public class DeleteLinkTemplateTask {

    private static final Logger log = LoggerFactory.getLogger(DeleteLinkTemplateTask.class);

    private DeleteLinkTemplateTask() {
    }

    public static boolean delete(final LinkTemplate linkTemplate) {
        boolean okToProceed = (linkTemplate != null);
        final IConcept concept = linkTemplate.getConceptDelegate().getConcept();
        if (okToProceed) {
            final int option = JOptionPane.showConfirmDialog(AppFrameDispatcher.getFrame(), "Are you sure you want to delete '" + linkTemplate.stringValue() + "' ? Be aware that this will not effect existing annotations that use it.", "VARS - Delete LinkTemplate", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            okToProceed = (option == JOptionPane.YES_OPTION);
        }
        if (okToProceed) {
            try {
                concept.removeLinkTemplate(linkTemplate);
                ConceptDAO.getInstance().update((Concept) concept);
            } catch (DAOException e) {
                final String msg = "Failed to delete '" + linkTemplate.stringValue() + "'";
                log.error(msg, e);
                AppFrameDispatcher.showErrorDialog(msg);
                okToProceed = false;
            }
        }
        return okToProceed;
    }
}
