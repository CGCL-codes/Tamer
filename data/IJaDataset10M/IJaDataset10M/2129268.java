package wotlas.common.message.description;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import wotlas.libs.net.NetMessage;

/** 
 * To ask the server to send our player data (Message Sent by Client).
 *
 * @author Aldiss
 */
public class MyPlayerDataPleaseMessage extends NetMessage {

    /** Constructor. Just initializes the message category and type.
     */
    public MyPlayerDataPleaseMessage() {
        super();
    }

    /** This is where we put your message data on the stream. You don't need
     * to invoke this method yourself, it's done automatically.
     *
     * @param ostream data stream where to put your data (see java.io.DataOutputStream)
     * @exception IOException if the stream has been closed or is corrupted.
     */
    @Override
    public void encode(DataOutputStream ostream) throws IOException {
    }

    /** This is where we retrieve our message data from the stream. You don't need
     * to invoke this method yourself, it's done automatically.
     *
     * @param istream data stream where you retrieve your data (see java.io.DataInputStream)
     * @exception IOException if the stream has been closed or is corrupted.
     */
    @Override
    public void decode(DataInputStream istream) throws IOException {
    }
}
