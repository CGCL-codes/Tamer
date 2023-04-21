package rgzm.gui.gleis.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import rgzm.bean.Zug;
import rgzm.gui.gleis.GleisView;
import base.exception.BusinessException;
import base.exception.ExceptionHandler;
import base.resources.Resources;

public class CancelZugMenuItem extends JMenuItem implements ActionListener {

    public CancelZugMenuItem(final GleisView gv) {
        super();
        gleisView = gv;
        setText(Resources.getText("depart.popup.revoke_to_timetable"));
        addActionListener(this);
    }

    public void actionPerformed(final ActionEvent e) {
        Zug zug = gleisView.getGleis().getZug();
        try {
            zug.setState(zug.getState().zuruecknehmen());
            gleisView.getGleis().setZug(null);
        } catch (BusinessException t) {
            ExceptionHandler.ERRORHANDLER.handleException(t);
        }
    }

    public GleisView gleisView;
}
