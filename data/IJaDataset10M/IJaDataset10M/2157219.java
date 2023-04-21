package server.messaging;

import java.util.Observable;
import common.protocol.IMessageDispatcher;
import server.pd.player.Figure;

public class FigureMessager extends Observable implements IMessager {

    private Figure objFigure;

    private int iLastFieldSuffix;

    public FigureMessager(Figure _objFigure) {
        iLastFieldSuffix = _objFigure.getSuffix();
        objFigure = _objFigure;
        objFigure.addObserver(this);
    }

    public void send(int _iReceiver, IMessageDispatcher _objMsgDisp) {
    }

    public void update(Observable _objObserv, Object _obj) {
        setChanged();
        notifyObservers();
    }

    public int getActualFieldSuffix() {
        return objFigure.getCurrentFieldSuffix();
    }
}
