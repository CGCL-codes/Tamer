package syndicus.gui.end;

import javax.swing.JComponent;
import org.springframework.richclient.dialog.TitledApplicationDialog;

public class CloseYearWizard extends TitledApplicationDialog {

    DataHolder dataHolder = new DataHolder();

    public CloseYearWizard() {
        super.setTitle("endyear");
        init();
    }

    @Override
    protected JComponent createTitledDialogContentPane() {
        setTitle("Eigendom type");
        setTitlePaneTitle("Word later gebruikt voor het beheer van de kostenposten");
        setDescription("Kost ingave word verdeeld met verdeelsleutel over eigendomtypes");
        return new ResidentialPage(dataHolder).createFormControl();
    }

    @Override
    protected boolean onFinish() {
        return true;
    }
}
