package net.sf.insim4j.insim.messages;

import net.sf.insim4j.insim.InSimRequestPacket;
import net.sf.insim4j.insim.messages.utils.LfsMessage;

/**
 * InSim Message Extended packet. <br />
 * To LFS. Message extended - like InSimMsgType but longer (not for commands)
 * 
 * @author Jiří Sotona
 */
public interface InSimMsgExt extends InSimRequestPacket {

    /**
	 * Getter. Message[63]
	 * 
	 * @return the msg
	 */
    public LfsMessage getMsg();
}
