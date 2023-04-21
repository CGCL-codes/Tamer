package student.web.internal;

import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadCleanup;
import cloudspace.vm.VM;

/**
 * 
 * @author mjw87
 * 
 */
public class VMTagCleanup implements EventThreadCleanup {

    public void cleanup(Component comp, Event evt, @SuppressWarnings("rawtypes") List errs) throws Exception {
        for (Object t : errs) {
            Throwable et = (Throwable) t;
            try {
                et = org.webcat.exceptiondoctor.ExceptionDoctor.addExplanation((Throwable) t);
            } catch (Exception e) {
            }
            et.printStackTrace();
        }
        VM.leave();
    }

    public void complete(Component comp, Event evt) throws Exception {
        ;
    }
}
