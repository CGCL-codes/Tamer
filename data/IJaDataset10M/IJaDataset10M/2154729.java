package controller;

import java.awt.event.KeyEvent;
import csel.controller.bindings.ActionCommandBindings;
import csel.controller.enums.ActionEnum;
import csel.controller.enums.CommandEnum;
import csel.controller.events.Event;
import csel.controller.bindings.KeyBindings;
import csel.controller.commandutil.CommandPool;
import csel.controller.commandutil.Parameterizable;
import csel.controller.commandutil.ParameterizableCommandFactory;
import csel.controller.commandutil.SimpleCommandFactory;
import csel.controller.dispatchers.Dispatcher;
import csel.model.GameCommand;
import csel.model.Invoker;

public class TestCommandPool {

    public static void main(String[] args) throws InterruptedException {
        KeyBindings kb = KeyBindings.getInstance();
        ActionCommandBindings acb = ActionCommandBindings.getInstance();
        CommandPool pool = new CommandPool();
        pool.addCommandFactory(CommandEnum.MOVE_COMMAND, new ParameterizableCommandFactory<Param>(new Param(7)));
        pool.addCommandFactory(CommandEnum.STOP_COMMAND, new SimpleCommandFactory<GC>(new GC()));
        EventThinger et1 = new EventThinger(-1);
        pool.getCommand(acb.getEnum(et1.getActionEnum()), et1).execute();
        EventThinger et2 = new EventThinger(KeyEvent.VK_NUMPAD1);
        pool.getCommand(acb.getEnum(et2.getActionEnum()), et2).execute();
        EventThinger et3 = new EventThinger(KeyEvent.VK_NUMPAD3);
        pool.getCommand(acb.getEnum(et3.getActionEnum()), et3).execute();
        EventThinger et4 = new EventThinger(KeyEvent.VK_NUMPAD6);
        pool.getCommand(acb.getEnum(et4.getActionEnum()), et4).execute();
    }

    private static class Param implements Parameterizable {

        private ActionEnum ae;

        private int i;

        private int k;

        public Param(int i) {
            this.i = i;
        }

        public void execute() {
            System.out.println(i + "     I done been haccepted.    " + k);
        }

        public Param clone() {
            try {
                Param clone = (Param) super.clone();
                clone.setInt(i);
                return clone;
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return null;
        }

        public void parameterize(ActionEnum actionEnum) {
            this.ae = actionEnum;
            if (ae == ActionEnum.MOVE_SOUTHEAST) {
                this.k = 2;
            } else if (ae == ActionEnum.MOVE_SOUTHEAST) {
                this.k = 4;
            } else {
                this.k = 10;
            }
        }

        private void setInt(int i) {
            this.i = i;
        }
    }

    private static class GC implements GameCommand {

        public void execute() {
            System.out.println("I done been haccepted.");
        }
    }

    private static class EventThinger implements Event {

        Integer k;

        KeyBindings kb = KeyBindings.getInstance();

        public EventThinger(int k) {
            this.k = k;
        }

        public ActionEnum getActionEnum() {
            return kb.getEnum(k);
        }
    }
}
