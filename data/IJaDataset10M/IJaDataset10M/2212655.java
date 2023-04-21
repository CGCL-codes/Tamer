package pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.legalConstraint;

import javax.swing.JComponent;
import javax.swing.JLabel;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.i18n.StringsManager;
import pt.igeo.snig.mig.editor.i18n.ToolTipStringsManager;
import pt.igeo.snig.mig.editor.list.ListValueManager;
import pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.common.FixedListFormatter;
import pt.igeo.snig.mig.editor.validation.NotEmptyVerifier;
import fi.mmm.yhteinen.swing.core.component.YPanel;
import fi.mmm.yhteinen.swing.core.component.YTitledBorder;
import fi.mmm.yhteinen.swing.core.component.list.YNaiveDualList;
import fi.mmm.yhteinen.swing.core.component.text.YTextField;
import fi.mmm.yhteinen.swing.core.tools.YUIToolkit;

/**
 * View for the legal constraint form.
 * 
 * @author Ant�nio Silva
 * @version $Revision: 9175 $
 * @since 1.0
 */
public class LegalConstraintView extends YPanel {

    /** Java requirement */
    private static final long serialVersionUID = 3465365600961641073L;

    /** use limitation */
    private YTextField useLimitation = new YTextField();

    /** use limitation label */
    private JLabel useLimitationLabel = new JLabel("a");

    /** access constraints dual list */
    private YNaiveDualList accessConstraints = new YNaiveDualList();

    /** use constraints dual list */
    private YNaiveDualList useConstraints = new YNaiveDualList();

    /** titled border */
    private YTitledBorder border = new YTitledBorder("a");

    /** access constraints border */
    private YTitledBorder acBorder = new YTitledBorder("a");

    /** use constraints border */
    private YTitledBorder ucBorder = new YTitledBorder("a");

    /**
	 * Constructor for this view
	 * 
	 */
    public LegalConstraintView() {
        prepareComponents();
        setMVCNames();
        translate();
        YUIToolkit.guessViewComponents(this);
    }

    /**
	 * Adding components to the panel.
	 */
    private void prepareComponents() {
        setBorder(border);
        accessConstraints.setBorder(acBorder);
        useConstraints.setBorder(ucBorder);
        JComponent[][] comps = { { useLimitationLabel, useLimitation }, { null }, { accessConstraints }, { null }, { useConstraints } };
        int[][] widths = { { 4, 11 }, { 15 }, { 15 }, { 15 }, { 15 } };
        addComponents(comps, widths, 15);
        setToolTips();
    }

    /**
	 * set tool tips
	 */
    public void setToolTips() {
        useLimitation.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.legalConstraint.useLimitation"));
        accessConstraints.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.legalConstraint.accessConstraints"));
        useConstraints.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.legalConstraint.useConstraints"));
    }

    /**
	 * Set MVC names
	 */
    private void setMVCNames() {
        useLimitation.setMvcName("legalConstraint.useLimitation");
        accessConstraints.setMvcName("legalConstraint.accessConstraints");
        useConstraints.setMvcName("legalConstraint.useConstraints");
        accessConstraints.setListModel(ListValueManager.getInstance().getFixedLists(Constants.restrictionCodeList), FixedListFormatter.getInstance(), false);
        useConstraints.setListModel(ListValueManager.getInstance().getFixedLists(Constants.restrictionCodeList), FixedListFormatter.getInstance(), false);
    }

    /**
	 * translates this component
	 */
    public void translate() {
        setToolTips();
        StringsManager sm = StringsManager.getInstance();
        useLimitationLabel.setText(sm.getString("useLimitation"));
        border.setTitle(sm.getString("legalConstraints"));
        acBorder.setTitle(sm.getString(Constants.accessConstraints));
        ucBorder.setTitle(sm.getString(Constants.useConstraints));
    }

    /**
	 * @param data
	 */
    protected void addVerifiers(Object data) {
        NotEmptyVerifier identificationVerifier = new NotEmptyVerifier(data);
        identificationVerifier.setData(useLimitation, "legalConstraints", "useLimitation");
        useLimitation.setInputVerifier(identificationVerifier);
        useLimitation.getInputVerifier().verify(useLimitation);
    }
}
