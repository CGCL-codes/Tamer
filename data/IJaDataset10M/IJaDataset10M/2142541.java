package alice.respect.core;

import java.util.TimerTask;
import alice.tuplecentre.core.InputEvent;

public class RespectTimerTask extends TimerTask {

    private RespectVMContext vm;

    private RespectOperation op;

    public RespectTimerTask(RespectVMContext vm, RespectOperation op) {
        this.vm = vm;
        this.op = op;
    }

    @Override
    public void run() {
        vm.notifyInputEvent(new InputEvent(this.vm.getId(), op, this.vm.getId(), this.vm.getCurrentTime()));
    }
}
