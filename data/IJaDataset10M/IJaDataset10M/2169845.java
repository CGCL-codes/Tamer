package vpfarm.slave;

import java.io.IOException;
import jtools.ai.agenten.AMessage;
import jtools.ai.agenten.CycleBehavior;
import vpfarm.Constants;
import vpfarm.data.RenderTask;

public class ReciveRenderTaskBehavior extends CycleBehavior {

    private Slave slave;

    public ReciveRenderTaskBehavior(Slave slave) {
        this.slave = slave;
    }

    @Override
    public void execute() {
        AMessage msg = reciveMessage(true);
        if (msg.getSubject() == Constants.SEND_RENDER_TASK) {
            RenderTask rt = (RenderTask) msg.getContent();
            try {
                this.slave.render(rt);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else pushBack(msg);
    }

    @Override
    public void onEnd() {
    }

    @Override
    public void onStart() {
    }
}
