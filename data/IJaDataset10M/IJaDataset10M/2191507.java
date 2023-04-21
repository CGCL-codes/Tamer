package net.community.chest.net.proto.text.pop3;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 *
 * <P>Interface used to report retrieved message data via TOP or RETR
 * commands</P>
 * 
 * @author Lyor G.
 * @since Sep 19, 2007 11:07:41 AM
 */
public interface POP3MsgDataHandler {

    /**
	 * Called to inform about start/end of message data
	 * @param msgNum message sequence number whose data is starting/ending
	 * @param fStarting if TRUE then data is starting (else ending)
	 * @return 0 if successful - Note: error values cause the retrieval call to be aborted
	 */
    int handleMsgDataStage(int msgNum, boolean fStarting);

    /**
	 * Called to inform about a buffer of data
	 * @param msgNum message sequence number whose data is being supplied
	 * @param data data buffer
	 * @param startPos index in data buffer of valid message data (inclusive)
	 * @param maxLen number valid of characters in data buffer (starting at specified position)
	 * @return 0 if successful - Note: error values cause the retrieval call to be aborted
	 */
    int handleMsgData(int msgNum, char[] data, int startPos, int maxLen);
}
