package com.iver.cit.gvsig.gui.cad.tools.smc;

import com.iver.cit.gvsig.gui.cad.tools.JoinCADTool;
import java.awt.event.InputEvent;
import com.iver.andami.PluginServices;

public final class JoinCADToolContext extends statemap.FSMContext {

    public JoinCADToolContext(JoinCADTool owner) {
        super();
        _owner = owner;
        setState(Join.Execute);
        Join.Execute.Entry(this);
    }

    public void addOption(String s) {
        _transition = "addOption";
        getState().addOption(this, s);
        _transition = "";
        return;
    }

    public void addPoint(double pointX, double pointY, InputEvent event) {
        _transition = "addPoint";
        getState().addPoint(this, pointX, pointY, event);
        _transition = "";
        return;
    }

    public void addValue(double d) {
        _transition = "addValue";
        getState().addValue(this, d);
        _transition = "";
        return;
    }

    public JoinCADToolState getState() throws statemap.StateUndefinedException {
        if (_state == null) {
            throw (new statemap.StateUndefinedException());
        }
        return ((JoinCADToolState) _state);
    }

    protected JoinCADTool getOwner() {
        return (_owner);
    }

    private transient JoinCADTool _owner;

    public abstract static class JoinCADToolState extends statemap.State {

        protected JoinCADToolState(String name, int id) {
            super(name, id);
        }

        protected void Entry(JoinCADToolContext context) {
        }

        protected void Exit(JoinCADToolContext context) {
        }

        protected void addOption(JoinCADToolContext context, String s) {
            Default(context);
        }

        protected void addPoint(JoinCADToolContext context, double pointX, double pointY, InputEvent event) {
            Default(context);
        }

        protected void addValue(JoinCADToolContext context, double d) {
            Default(context);
        }

        protected void Default(JoinCADToolContext context) {
            throw (new statemap.TransitionUndefinedException("State: " + context.getState().getName() + ", Transition: " + context.getTransition()));
        }
    }

    abstract static class Join {

        static Join_Default.Join_Execute Execute;

        private static Join_Default Default;

        static {
            Execute = new Join_Default.Join_Execute("Join.Execute", 0);
            Default = new Join_Default("Join.Default", -1);
        }
    }

    protected static class Join_Default extends JoinCADToolState {

        protected Join_Default(String name, int id) {
            super(name, id);
        }

        protected void addOption(JoinCADToolContext context, String s) {
            JoinCADTool ctxt = context.getOwner();
            if (s.equals(PluginServices.getText(this, "cancel"))) {
                boolean loopbackFlag = context.getState().getName().equals(Join.Execute.getName());
                if (loopbackFlag == false) {
                    (context.getState()).Exit(context);
                }
                context.clearState();
                try {
                    ctxt.end();
                } finally {
                    context.setState(Join.Execute);
                    if (loopbackFlag == false) {
                        (context.getState()).Entry(context);
                    }
                }
            } else {
                boolean loopbackFlag = context.getState().getName().equals(Join.Execute.getName());
                if (loopbackFlag == false) {
                    (context.getState()).Exit(context);
                }
                context.clearState();
                try {
                    ctxt.throwOptionException(PluginServices.getText(this, "incorrect_option"), s);
                } finally {
                    context.setState(Join.Execute);
                    if (loopbackFlag == false) {
                        (context.getState()).Entry(context);
                    }
                }
            }
            return;
        }

        protected void addValue(JoinCADToolContext context, double d) {
            JoinCADTool ctxt = context.getOwner();
            boolean loopbackFlag = context.getState().getName().equals(Join.Execute.getName());
            if (loopbackFlag == false) {
                (context.getState()).Exit(context);
            }
            context.clearState();
            try {
                ctxt.throwValueException(PluginServices.getText(this, "incorrect_value"), d);
            } finally {
                context.setState(Join.Execute);
                if (loopbackFlag == false) {
                    (context.getState()).Entry(context);
                }
            }
            return;
        }

        protected void addPoint(JoinCADToolContext context, double pointX, double pointY, InputEvent event) {
            JoinCADTool ctxt = context.getOwner();
            boolean loopbackFlag = context.getState().getName().equals(Join.Execute.getName());
            if (loopbackFlag == false) {
                (context.getState()).Exit(context);
            }
            context.clearState();
            try {
                ctxt.throwPointException(PluginServices.getText(this, "incorrect_point"), pointX, pointY);
            } finally {
                context.setState(Join.Execute);
                if (loopbackFlag == false) {
                    (context.getState()).Entry(context);
                }
            }
            return;
        }

        private static final class Join_Execute extends Join_Default {

            private Join_Execute(String name, int id) {
                super(name, id);
            }

            protected void Entry(JoinCADToolContext context) {
                JoinCADTool ctxt = context.getOwner();
                ctxt.join();
                ctxt.end();
                return;
            }
        }
    }
}
