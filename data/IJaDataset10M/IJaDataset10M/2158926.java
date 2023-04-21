package org.rvsnoop.actions;

import java.awt.event.ActionEvent;
import javax.swing.Action;
import org.rvsnoop.Application;
import org.rvsnoop.NLSUtils;
import org.rvsnoop.ui.MainFrame;
import rvsnoop.Record;

/**
 * Clear the contents of the record ledger.
 *
 * @author <a href="mailto:ianp@ianp.org">Ian Phillips</a>
 * @version $Revision: 401 $, $Date: 2008-07-25 07:55:13 -0400 (Fri, 25 Jul 2008) $
 * @since 1.5
 */
public final class ClearLedger extends RvSnoopAction {

    static {
        NLSUtils.internationalize(ClearLedger.class);
    }

    private static final long serialVersionUID = -3552599952765687823L;

    public static final String COMMAND = "clearLedger";

    static String MNEMONIC, NAME, TOOLTIP;

    public ClearLedger(Application application) {
        super(NAME, application);
        putValue(Action.ACTION_COMMAND_KEY, COMMAND);
        putSmallIconValue(COMMAND);
        putValue(Action.SHORT_DESCRIPTION, TOOLTIP);
        putMnemonicValue(MNEMONIC);
    }

    @Override
    public final void actionPerformed(ActionEvent event) {
        application.getLedger().clear();
        application.getSubjectHierarchy().reset();
        Record.resetSequence();
        MainFrame.INSTANCE.clearDetails();
    }
}
