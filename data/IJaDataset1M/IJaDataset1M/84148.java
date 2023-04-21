package gui.dutsForms;

import gui.xmlClasses.Text;
import java.awt.BorderLayout;
import domain.core.GenericStudyFacade;

/** 
 * @author Gilles for FARS Design
 */
public class SubjDialog extends DUTSCUDDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** 
	 * the subjForm to display
	 */
    private SubjForm subjForm;

    /**
	 * 
	 * @param DUTSLabels
	 */
    public SubjDialog(Text DUTSLabels, GenericStudyFacade facade) {
        super(DUTSLabels);
        initGUI(facade);
    }

    /**
	 * Called whenever the apply button is pressed
	 */
    public void applyButtonPressed() {
        switch(this.getDialogMode()) {
            case DUTSCUDDialog.DIALOG_ADD:
                subjForm.create();
                break;
            case DUTSCUDDialog.DIALOG_MODIFY:
                subjForm.update();
                break;
            default:
                break;
        }
    }

    /**
	 * initializes the graphics components of this dialog window
	 * @param facade the studyFacade that the SemForm needs to interact 
	 * with the domain 
	 */
    public void initGUI(GenericStudyFacade facade) {
        this.setTitle(this.getDUTSLabels().getSemesterCreatWindowLabel());
        this.getGeneralPanel().setLayout(new BorderLayout());
        this.subjForm = new SubjForm(this.getDUTSLabels(), facade);
        this.getGeneralPanel().add(this.subjForm, BorderLayout.CENTER);
    }
}
