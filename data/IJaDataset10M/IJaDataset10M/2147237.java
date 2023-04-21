package br.com.exatisistemas.microdb.test;

import br.com.exatisistemas.microdb.Table;
import waba.fx.Color;
import waba.ui.Button;
import waba.ui.ControlEvent;
import waba.ui.Event;
import waba.ui.Grid;

public class ActorUI extends waba.ui.Window {

    public ActorUI() {
        super("Actors", TAB_ONLY_BORDER);
        setRect(CENTER, CENTER, waba.sys.Settings.screenWidth, waba.sys.Settings.screenHeight);
        setBackColor(Color.ORANGE);
    }

    private Grid g;

    private Button close;

    private Table tb;

    protected void onStart() {
        tb = Database.moviedb().actor;
        g = tb.setupNewGrid();
        g.setDataSource(tb, tb.recordCount());
        close = new Button("Close");
        this.add(g, LEFT + 1, TOP + 1);
        g.setRect(LEFT + 1, TOP + 1, this.height, this.width - 50);
        this.add(close, LEFT + 1, AFTER + 2);
        super.onStart();
        repaintNow();
    }

    protected void onPopup() {
        super.onPopup();
    }

    public void onEvent(Event e) {
        switch(e.type) {
            case ControlEvent.PRESSED:
                if (e.target == close) {
                    this.unpop();
                }
                break;
        }
    }
}
