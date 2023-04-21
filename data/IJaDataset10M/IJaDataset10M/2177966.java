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
public class LearnerHandler extends AbstractPaxosHandler {

    static Logger logger = Logger.getLogger("ch.usi.jpat.da.proj");

    public LearnerHandler(ProcessContext pCtx) {
        super(pCtx);
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        String incoming = (String) e.getMessage();
        Proposal propIn = PaxosUtils.createProposalFromString(incoming);
        PaxosProcess sourceProc = pCtx.getAcceptors().get(propIn.getSrcId());
        logger.info("Got proposal,value: " + propIn.getBallot() + ", " + propIn.getValue() + " from Acceptor " + sourceProc.getId());
        if (propIn.getBallot() > pCtx.getBallot()) {
            pCtx.getQuorum().clear();
            pCtx.getQuorum().add(sourceProc);
            logger.info("Quorum size : " + pCtx.getQuorum().size());
            pCtx.setBallot(propIn.getBallot());
        } else if (propIn.getBallot() == pCtx.getBallot()) {
            pCtx.getQuorum().add(sourceProc);
            if (pCtx.getQuorum().size() > pCtx.getAcceptors().size() / 2) {
                logger.info("Got majority; learned " + propIn.getValue());
                System.out.println(propIn.getValue());
                pCtx.getProposalHistory().put(propIn.getBallot(), propIn);
                pCtx.getQuorum().clear();
            }
        } else {
            logger.info("Something weird: got out of order ballot from acceptor");
        }
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        Channel ch = e.getChannel();
        ch.close();
    }
}
