package genestudio.Controllers.ORFFind;

import genestudio.Controllers.Main.MainWindowCode;
import genestudio.Interfaces.ORFFinder.ORFFinder;

/**
 *
 * @author nrovinskiy
 */
public class ORFFindController extends ORFFinder {

    public static final int DEFAULT_MINIMAL_ORF_LENGTH = 500;

    public static final int DEFAULT_MAXIMAL_ORF_LENGTH = 2500000;

    public static final boolean USE_AUG = true;

    public static final boolean USE_GTG = false;

    MainWindowCode mwcMain;

    public ORFFindController(MainWindowCode main) {
        super(main);
        mwcMain = main;
        PopulateDefaults();
        btnReset.addActionListener(new ResetListener(this));
        btnSearch.addActionListener(new FindListener(this, mwcMain));
    }

    public void PopulateDefaults() {
        txtMax.setText(Integer.toString(DEFAULT_MAXIMAL_ORF_LENGTH));
        txtMin.setText(Integer.toString(DEFAULT_MINIMAL_ORF_LENGTH));
        chkATG.setSelected(USE_AUG);
        chkGTG.setSelected(USE_GTG);
    }
}
