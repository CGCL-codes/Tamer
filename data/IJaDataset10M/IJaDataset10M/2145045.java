package pl.mn.communicator.packet.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.mn.communicator.GGException;
import pl.mn.communicator.PersonalInfo;
import pl.mn.communicator.PublicDirSearchReply;
import pl.mn.communicator.packet.GGUtils;
import pl.mn.communicator.packet.in.GGPubdirReply;

/**
 * Created on 2004-12-15
 * 
 * @author <a href="mailto:mati@sz.home.pl">Mateusz Szczap</a>
 * @version $Id: GGPubdirReplyPacketHandler.java,v 1.1 2005/11/05 23:34:53 winnetou25 Exp $
 */
public class GGPubdirReplyPacketHandler implements PacketHandler {

    private static final Log logger = LogFactory.getLog(GGPubdirReplyPacketHandler.class);

    /**
	 * @see pl.mn.communicator.packet.handlers.PacketHandler#handle(pl.mn.communicator.packet.handlers.Context)
	 */
    public void handle(PacketContext context) throws GGException {
        if (logger.isDebugEnabled()) {
            logger.debug("Received GGPubdirReply packet.");
            logger.debug("PacketHeader: " + context.getHeader());
            logger.debug("PacketBody: " + GGUtils.prettyBytesToString(context.getPackageContent()));
        }
        GGPubdirReply pubdirReply = new GGPubdirReply(context.getPackageContent());
        int querySeq = pubdirReply.getQuerySeq();
        if (pubdirReply.isPubdirReadReply()) {
            PersonalInfo publicDirInfo = (PersonalInfo) pubdirReply.getPubdirReadReply();
            context.getSessionAccessor().notifyPubdirRead(querySeq, publicDirInfo);
        } else if (pubdirReply.isPubdirWriteReply()) {
            context.getSessionAccessor().notifyPubdirUpdated(querySeq);
        } else if (pubdirReply.isPubdirSearchReply()) {
            PublicDirSearchReply pubDirSearchReply = pubdirReply.getPubdirSearchReply();
            context.getSessionAccessor().notifyPubdirGotSearchResults(querySeq, pubDirSearchReply);
        }
    }
}
