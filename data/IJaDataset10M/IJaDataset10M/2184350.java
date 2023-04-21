package ch.usi.jpat.da.proj;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import ch.usi.jpat.da.proj.Proposal;

@ChannelPipelineCoverage("one")
public class ProposerMessageHandler extends AbstractPaxosHandler {

    static Logger logger = Logger.getLogger("ch.usi.jpat.da.proj");

    public ProposerMessageHandler(ProcessContext pCtx) {
        super(pCtx);
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String incoming = (String) e.getMessage();
        Proposal p = PaxosUtils.createProposalFromString(incoming);
        PaxosProcess sourceProc = pCtx.getAcceptors().get(p.getSrcId());
        if (p.getValue() == null && pCtx.getState() == ProcessContext.WAITING) {
            pCtx.getQuorum().add(sourceProc);
        } else if (p.getValue() != null && pCtx.getState() == ProcessContext.BALLOT_ACCEPTED) {
            pCtx.getQuorum().add(sourceProc);
        } else {
            logger.info("Out of order message; not adding to quorom");
            return;
        }
        logger.info("Got message from id : " + p.getSrcId() + " and ballot : " + p.getBallot());
        logger.info("Current quorom size: " + pCtx.getQuorum().size());
        if (pCtx.getQuorum().size() > pCtx.getAcceptors().size() / 2) {
            synchronized (pCtx) {
                logger.info("SYNCHRONISED quorom size: " + pCtx.getQuorum().size() + " Current process state: " + pCtx.getState());
                if (pCtx.getState() == ProcessContext.WAITING) {
                    pCtx.setState(ProcessContext.BALLOT_ACCEPTED);
                } else if (pCtx.getState() == ProcessContext.BALLOT_ACCEPTED) {
                    pCtx.setState(ProcessContext.LEARNED);
                    pCtx.getProposalHistory().put(p.getBallot(), p);
                } else {
                    logger.warning("Bad state transition?");
                }
                pCtx.notify();
            }
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
}
