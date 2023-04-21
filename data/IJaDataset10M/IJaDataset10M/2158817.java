package org.qfirst.batavia.actions.selection;

import org.qfirst.batavia.actions.BataviaAction;
import java.awt.event.*;
import javax.swing.*;
import org.qfirst.i18n.*;
import java.util.regex.*;
import org.apache.commons.lang.*;
import org.qfirst.misc.utils.*;
import org.qfirst.batavia.utils.*;
import org.qfirst.batavia.*;
import org.qfirst.historytextfield.*;

/**
 *  Description of the Class
 *
 * @author     francisdobi
 * @created    July 11, 2004
 */
public abstract class AbstractSelectAction extends BataviaAction {

    /**
	 *  Description of the Field
	 */
    protected Box selectPanel;

    /**
	 *  Description of the Field
	 */
    protected HistoryTextField pattern;

    /**
	 *  Description of the Field
	 */
    protected JCheckBox regexpCheckBox;

    protected HistoryManager historyManager = Application.getInstance().getHistoryManager();

    /**
	 */
    protected AbstractSelectAction() {
        selectPanel = Box.createVerticalBox();
        pattern = new HistoryTextField(historyManager.createModel("actions.selection.AbstractSelectAction"));
        selectPanel.add(pattern);
        regexpCheckBox = new JCheckBox(Message.getInstance().getMessage("dialog.regexp", "Regular expression"));
        selectPanel.add(regexpCheckBox);
    }

    /**
	 *  Description of the Field
	 */
    protected String regexp;

    /**
	 *  Description of the Field
	 */
    protected Pattern p;

    /**
	 *  Description of the Method
	 */
    protected boolean validateAndCompile() {
        regexp = pattern.getText();
        if (StringUtils.isBlank(regexp)) {
            return false;
        }
        if (!regexpCheckBox.isSelected()) {
            regexp = MiscUtils.convertFilePatternToRegexp(regexp);
        }
        try {
            p = Pattern.compile(regexp);
        } catch (PatternSyntaxException ex) {
            UIUtils.showErrorDialog(Batavia.getCurrentCommanderWindow(), ex, Message.getInstance().getMessage("dialog.invalid_regexp", "Invalid regular expression: {0}", new Object[] { regexp }));
            return false;
        }
        pattern.saveText();
        pattern.setText(null);
        return true;
    }
}
