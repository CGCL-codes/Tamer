package gov.miamidade.hgowl.plugin.ui;

import gov.miamidade.hgowl.plugin.owl.HGOwlEditorKit;
import java.awt.event.ActionEvent;
import org.protege.editor.owl.ui.action.ProtegeOWLAction;

/**
 * HyperGraphShowStatisticsAction.
 * @author Thomas Hilpold (CIAO/Miami-Dade County)
 * @created Dec 9, 2011
 */
public class DeleteOntologyFromRepositoryAction extends ProtegeOWLAction {

    private static final long serialVersionUID = 12313124211234L;

    @Override
    public void initialise() throws Exception {
    }

    @Override
    public void dispose() throws Exception {
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        HGOwlEditorKit hgowlKit = (HGOwlEditorKit) getEditorKit();
        try {
            hgowlKit.handleDeleteFromRepositoryRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
