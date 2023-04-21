package controller;

import components.Util;
import components.exceptions.BinaryToIntException;
import components.exceptions.IntToBinaryException;
import components.exceptions.UnexpectedNotifyReceivedException;
import components.memory.Memory;
import controller.notify.Notify;

/**
 * @author Chz
 * 
 */
public class PCController extends Controller {

    private static PCController self;

    private PCController() {
    }

    public static PCController getInstance() {
        if (self == null) self = new PCController();
        return self;
    }

    @Override
    public void notify(Notify n) throws UnexpectedNotifyReceivedException {
        int pc = 0;
        try {
            pc = Util.binaryToInt(regs.getPC());
        } catch (BinaryToIntException e) {
            e.printStackTrace();
        }
        switch(n.getType()) {
            case Notify.DONE:
                pc++;
                try {
                    regs.setPC(Util.intToBinary(pc, 16));
                } catch (IntToBinaryException e) {
                    e.printStackTrace();
                }
                break;
            case Notify.NOINC:
                break;
            default:
                throw new UnexpectedNotifyReceivedException("fatal error in PC, undefined Notify type " + "received!!!!!!!!!!!!!!!!");
        }
        bus.sendNotify(new Notify(Notify.FETCH), MemoryController.getInstance());
    }
}
