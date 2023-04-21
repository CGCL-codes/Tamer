package customsrc;

import org.eclipse.ui.IWorkbenchPage;

public class ChangeToOrContributionAction extends SetLineTypeAction {

    private String privateID = "ChangeToOrContributionAction";

    private String privateCommandLabelText = "OR";

    ;

    protected ChangeToOrContributionAction(IWorkbenchPage workbenchPage) {
        super(workbenchPage);
        this.ID = privateID;
        this.commandName = privateCommandLabelText;
        init();
        this.setChangeTo(privateCommandLabelText);
    }

    public void init() {
        super.init();
        setId(ID);
        setText(privateCommandLabelText);
        refresh();
    }
}
