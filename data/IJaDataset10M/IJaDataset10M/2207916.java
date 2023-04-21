package edu.iu.iv.toolkits.vwtk.gui.user;

import static edu.iu.iv.toolkits.vwtk.messages.GUIMessages.str;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.text.MessageFormat;
import javax.swing.JLabel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.AbstractUserAction;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.Arrival;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.ChatMessage;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.Click;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.Departure;
import edu.iu.iv.toolkits.vwtk.datamodel.user.impl.actions.TrailSegment;
import edu.iu.iv.toolkits.vwtk.listener.IObjectStateChangeListener;
import edu.iu.iv.toolkits.vwtk.properties.assignable.IDataAssignable;
import edu.iu.iv.toolkits.vwtk.util.TimeFunc;
import edu.iu.iv.toolkits.vwtk.viz.ISymbol;

public class UserActionInfoPop extends MouseMotionAdapter implements IObjectStateChangeListener<ISymbol>, MouseListener, MouseMotionListener {

    private JLabel lblInfo;

    private Component _owner;

    public UserActionInfoPop(Component owner) {
        lblInfo = new JLabel();
        lblInfo.setBackground(new Color(250, 250, 250, 200));
        this._owner = owner;
    }

    private Popup _popup;

    public void stateChanged(ISymbol symbol) {
        synchronized (this) {
            if (_popup != null) _popup.hide();
            if (symbol == null) return;
            if (!(symbol instanceof IDataAssignable)) return;
            Object o = ((IDataAssignable) symbol).getData();
            if (!(o instanceof AbstractUserAction)) return;
            lblInfo.setText(format((AbstractUserAction) o));
            _popup = PopupFactory.getSharedInstance().getPopup(_owner, lblInfo, _mouseX, _mouseY);
            _popup.show();
        }
    }

    private static final String MSG_FORMAT = str("user.info.msg.fmt");

    private static final String CLICK_SUB_MSG_FORMAT = str("user.info.click.msg.subfmt");

    private String format(AbstractUserAction aua) {
        return MessageFormat.format(MSG_FORMAT, TimeFunc.getFriendlyString(aua.getTimestamp()), aua.getUser().getName(), subformat(aua));
    }

    private String subformat(AbstractUserAction aua) {
        String s = null;
        if (aua instanceof ChatMessage) {
            s = ((ChatMessage) aua).getText();
        }
        if (aua instanceof Click) {
            Click c = (Click) aua;
            s = MessageFormat.format(CLICK_SUB_MSG_FORMAT, c.getObjectNo(), c.getCellX(), c.getCellY());
        }
        if (aua instanceof Arrival) {
            s = str("user.info.arrival.msg");
        }
        if (aua instanceof Departure) {
            s = str("user.info.departure.msg");
        }
        if (aua instanceof TrailSegment) {
            s = str("user.info.moving.msg");
        }
        return s;
    }

    private int _mouseX, _mouseY;

    @Override
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();
        SwingUtilities.convertPointToScreen(p, _owner);
        _mouseX = (int) p.getX() + _owner.getX() + 20;
        _mouseY = (int) p.getY() + _owner.getY() + 20;
    }

    public void mouseClicked(MouseEvent e) {
        if (_popup != null) {
            _popup.hide();
            _popup = null;
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
