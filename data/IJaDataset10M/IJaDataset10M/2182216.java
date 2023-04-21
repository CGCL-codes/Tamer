package org.apache.mina.example.sumup;

import org.apache.mina.common.IdleStatus;
import org.apache.mina.common.IoHandler;
import org.apache.mina.common.IoHandlerAdapter;
import org.apache.mina.common.IoSession;
import org.apache.mina.example.sumup.message.AddMessage;
import org.apache.mina.example.sumup.message.ResultMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link IoHandler} for SumUp server.
 *
 * @author The Apache MINA Project (dev@mina.apache.org)
 * @version $Rev: 616100 $, $Date: 2008/05/29 07:04:19 $
 */
public class ServerSessionHandler extends IoHandlerAdapter {

    private static final String SUM_KEY = "sum";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sessionOpened(IoSession session) {
        session.getConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60);
        session.setAttribute(SUM_KEY, new Integer(0));
    }

    @Override
    public void messageReceived(IoSession session, Object message) {
        AddMessage am = (AddMessage) message;
        int sum = ((Integer) session.getAttribute(SUM_KEY)).intValue();
        int value = am.getValue();
        long expectedSum = (long) sum + value;
        if (expectedSum > Integer.MAX_VALUE || expectedSum < Integer.MIN_VALUE) {
            ResultMessage rm = new ResultMessage();
            rm.setSequence(am.getSequence());
            rm.setOk(false);
            session.write(rm);
        } else {
            sum = (int) expectedSum;
            session.setAttribute(SUM_KEY, new Integer(sum));
            ResultMessage rm = new ResultMessage();
            rm.setSequence(am.getSequence());
            rm.setOk(true);
            rm.setValue(sum);
            session.write(rm);
        }
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) {
        logger.info("Disconnecting the idle.");
        session.close();
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        session.close();
    }
}
