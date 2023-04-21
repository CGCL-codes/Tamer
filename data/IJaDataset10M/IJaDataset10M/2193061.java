package jpcsp.HLE.kernel.types.interrupts;

import java.util.LinkedList;
import java.util.List;
import jpcsp.Emulator;
import jpcsp.HLE.kernel.managers.IntrManager;
import jpcsp.HLE.kernel.types.IAction;
import jpcsp.scheduler.Scheduler;

public class VBlankInterruptHandler extends AbstractInterruptHandler {

    private List<IAction> vblankActions = new LinkedList<IAction>();

    private List<IAction> vblankActionsOnce = new LinkedList<IAction>();

    private long nextVblankSchedule = IntrManager.VBLANK_SCHEDULE_MICROS;

    @Override
    protected void executeInterrupt() {
        Scheduler scheduler = Emulator.getScheduler();
        scheduler.addAction(nextVblankSchedule, this);
        nextVblankSchedule += IntrManager.VBLANK_SCHEDULE_MICROS;
        long now = Scheduler.getNow();
        while (nextVblankSchedule < now) {
            nextVblankSchedule += IntrManager.VBLANK_SCHEDULE_MICROS;
        }
        for (IAction action : vblankActions) {
            if (action != null) {
                action.execute();
            }
        }
        for (IAction action : vblankActionsOnce) {
            if (action != null) {
                action.execute();
            }
        }
        vblankActionsOnce.clear();
        IntrManager.getInstance().triggerInterrupt(IntrManager.PSP_VBLANK_INTR, null, null);
    }

    public void addVBlankAction(IAction action) {
        vblankActions.add(action);
    }

    public boolean removeVBlankAction(IAction action) {
        return vblankActions.remove(action);
    }

    public void addVBlankActionOnce(IAction action) {
        vblankActionsOnce.add(action);
    }

    public boolean removeVBlankActionOnce(IAction action) {
        return vblankActionsOnce.remove(action);
    }
}
