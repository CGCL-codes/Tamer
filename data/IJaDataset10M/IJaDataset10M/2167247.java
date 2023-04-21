package cn.easyact.tdl.state;

import javax.microedition.lcdui.Graphics;
import cn.easyact.engine.ui.Key;
import cn.easyact.tdl.state.main.GanttSubState;
import cn.easyact.tdl.state.main.TableSubState;
import cn.easyact.tdl.ui.Component;
import cn.easyact.tdl.ui.NullComponent;
import cn.easyact.tdl.ui.debug.Debug;
import cn.easyact.tdl.ui.debug.NullDebug;

public class MainGame extends State {

    private Debug debug = new NullDebug();

    private State menuState;

    private State reflect;

    private Component help;

    public State step() {
        if (isPressed(Key.GAME_C_PRESSED)) {
            return pushState(reflect);
        }
        if (isPressed(Key._SOFT_LEFT)) {
            return pushState(menuState);
        }
        if (isPressed(Key._0)) {
            return getScriptNext();
        }
        return null;
    }

    protected void endPaint(Graphics g) {
        g.setClip(0, 0, getWidth(), getHeight());
        g.translate(0, getWidth() - getHelp().getHeight());
        getHelp().paint(g);
        debug.paint(getGraphicsAdaptor(), g);
    }

    public void update() {
        State preState = popState();
        if (null != preState && preState != reflect && preState != menuState) {
            getSubState().update();
        }
    }

    public String toString() {
        return "MainGame[" + getSubState().toString() + ']';
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public void setMenuState(IGMenu menuState) {
        this.menuState = menuState;
    }

    public void setReflect(State reflect) {
        this.reflect = reflect;
    }

    public void setHelp(Component help) {
        this.help = help;
        help.setData(new String[] { "Welcome!", "" });
    }

    public Component getHelp() {
        if (null == help) {
            help = new NullComponent();
        }
        return help;
    }

    public void script(TableSubState tableSubState, GanttSubState ganttSubState) {
        tableSubState.setScriptNext(ganttSubState);
        ganttSubState.setScriptNext(tableSubState);
    }
}
