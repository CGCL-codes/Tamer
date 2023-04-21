package com.ixora.rms.ui.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.ixora.common.MessageRepository;
import com.ixora.common.ui.UIExceptionMgr;
import com.ixora.common.ui.UIUtils;
import com.ixora.rms.ui.dataviewboard.DataViewBoardHandler;
import com.ixora.rms.ui.messages.Msg;

/**
 * Tiles the view boards.
 */
public final class ActionTileViewBoards extends AbstractAction {

    private static final long serialVersionUID = -3344971819658060600L;

    /** DataViewBoardHandler */
    private DataViewBoardHandler dataViewBoardHandler;

    /**
	 * Constructor.
	 * @param dbh
	 */
    public ActionTileViewBoards(DataViewBoardHandler dbh) {
        super();
        this.dataViewBoardHandler = dbh;
        UIUtils.setUsabilityDtls(MessageRepository.get(Msg.ACTIONS_TILE_VIEWBOARDS), this);
    }

    /**
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent arg0) {
        try {
            this.dataViewBoardHandler.tileBoards();
        } catch (Exception e) {
            UIExceptionMgr.userException(e);
        }
    }
}
