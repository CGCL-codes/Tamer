package pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.lineage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import pt.igeo.snig.mig.editor.constants.Constants;
import pt.igeo.snig.mig.editor.i18n.StringsManager;
import pt.igeo.snig.mig.editor.i18n.ToolTipStringsManager;
import pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.common.FixedListFormatter;
import pt.igeo.snig.mig.editor.validation.NotEmptyVerifier;
import fi.mmm.yhteinen.swing.core.component.YPanel;
import fi.mmm.yhteinen.swing.core.component.YTitledBorder;
import fi.mmm.yhteinen.swing.core.component.list.YComboBox;
import fi.mmm.yhteinen.swing.core.component.text.YTextArea;
import fi.mmm.yhteinen.swing.core.tools.YUIToolkit;

/**
 * View for the lineage form.
 * 
 * @author Ant�nio Silva
 * @version $Revision: 9175 $
 * @since 1.0
 */
public class LineageView extends YPanel {

    /** Java requirement */
    private static final long serialVersionUID = 603978160365302431L;

    /** statement */
    private YTextArea statement = new YTextArea();

    /** statement label */
    private JLabel statementLabel = new JLabel("a");

    /** language */
    private YComboBox language = new YComboBox();

    /** titled border */
    private YTitledBorder border = new YTitledBorder("a");

    /**
	 * Constructor for this view
	 * 
	 */
    public LineageView() {
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
        statement.setRows(Constants.defaultTextRows + 5);
        JScrollPane jStatement = new JScrollPane(statement);
        statement.setLineWrap(true);
        statement.setWrapStyleWord(true);
        JComponent[][] comps = { { statementLabel, jStatement, language } };
        int[][] widths = { { 4, 10, 3 } };
        addComponents(comps, widths, 17);
        setToolTips();
    }

    /**
	 * set tool tips
	 */
    public void setToolTips() {
        statement.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.lineage.statement"));
        language.setToolTipText(ToolTipStringsManager.getInstance().getString("pt.igeo.snig.mig.editor.ui.recordEditor.formFiller.forms.lineage.language"));
    }

    /**
	 * Attach verifiers to fields
	 * @param data
	 */
    protected void addVerifiers(Object data) {
        NotEmptyVerifier statementVerifier = new NotEmptyVerifier(data);
        statementVerifier.setData(statement, "lineage", "statement");
        statement.setInputVerifier(statementVerifier);
        statement.getInputVerifier().verify(statement);
    }

    /**
	 * Set MVC names
	 */
    private void setMVCNames() {
        statement.setMvcName("lineage.lineageStatement");
        language.setFormatter(FixedListFormatter.getInstance());
        language.setComboModelField("languageCode");
        language.setMvcName("lineage.language");
    }

    /**
	 * translates this component
	 */
    public void translate() {
        setToolTips();
        StringsManager sm = StringsManager.getInstance();
        border.setTitle(sm.getString("lineage"));
        statementLabel.setText(sm.getString("statement"));
    }
}
