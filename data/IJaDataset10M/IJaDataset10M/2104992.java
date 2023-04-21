package net.internetrail.service.server.gamefield;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import net.internetrail.db.CDBConnectionPool;
import net.internetrail.network.AService;
import net.internetrail.network.CServerMessage;
import net.internetrail.network.EMessageSending;
import net.internetrail.network.EMessageTooLarge;
import net.internetrail.service.server.gamefield.messages.CMessageFactory;
import net.internetrail.service.server.gamefield.messages.IMessage;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldInhabitantsRequest;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldInhabitantsResponse;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldPrivilegeRequest;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldPrivilegeResponse;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldRenderingInformationRequest;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldRenderingInformationResponse;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldSizeRequest;
import net.internetrail.service.server.gamefield.messages.MsgGamefieldSizeResponse;

/** <p>Server-side service for gamefield information.</p>
 * 
 * @author Bjoern Wuest, Germany
 */
public class CGamefieldService extends AService {

    /** <p>Message to respond for requesting the gamefield size.</p> */
    private byte[] m_GamefieldSizeResponse;

    /** <p>Create instance of the service.</p>
	 * 
	 * <p>Upon instantiation, the gamefield size will be read and stored.</p>
	 */
    public CGamefieldService() {
        super(SERVICE_GAMEFIELD);
        m_GamefieldSizeResponse = respondMsgGamefieldSizeRequest().serialize();
    }

    /** <p>Send a message to the client.</p>
	 * 
	 * @param NetworkMessage The message received from the client/mediator
	 * @param Message The serialized message to send.
	 */
    private void sendMessage(net.internetrail.network.IMessage NetworkMessage, byte[] Message) {
        try {
            p_Dispatcher.sendMessage(new CServerMessage(NetworkMessage.getSenderServiceAddress(), getServiceAddress(), ((CServerMessage) NetworkMessage).getMediatorAddress(), ((CServerMessage) NetworkMessage).getClientAddress(), Message));
        } catch (EMessageSending T) {
            p_LOG.throwing(getClass().getName(), "sendMessage(Message)", T);
        } catch (EMessageTooLarge T) {
            p_LOG.throwing(getClass().getName(), "sendMessage(Message)", T);
        }
    }

    /** <p>Create and return response for a {@link MsgGamefieldSizeRequest}.</p>
	 * 
	 * <p>The method reads the information from the database, i.e. it reads the
	 * maximum values for {@code x} and {@code y} in the {@code gamefield} table.</p>
	 * 
	 * @return Response for a {@link MsgGamefieldSizeRequest}.
	 */
    private MsgGamefieldSizeResponse respondMsgGamefieldSizeRequest() {
        int x = 0;
        int y = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rSet = null;
        try {
            conn = CDBConnectionPool.getConnection();
            stmt = conn.createStatement();
            rSet = stmt.executeQuery("select max(x), max(y) from gamefield");
            rSet.next();
            x = rSet.getInt(1) + 1;
            y = rSet.getInt(2) + 1;
        } catch (SQLException E) {
            p_LOG.throwing(getClass().getName(), "respondMsgGamefieldSizeRequest()", E);
        } finally {
            try {
                rSet.close();
            } catch (Throwable Ignore) {
            }
            try {
                stmt.close();
            } catch (Throwable Ignore) {
            }
            if (conn != null) {
                CDBConnectionPool.returnConnection(conn);
            }
        }
        return new MsgGamefieldSizeResponse(x, y);
    }

    /** <p>Return the rendering information for a particular field.</p>
	 * 
	 * @param X The X position of the field.
	 * @param Y The Y position of the field.
	 * @return A message containing the rendering information.
	 */
    private MsgGamefieldRenderingInformationResponse respondMsgGamefieldRenderingInformationRequest(int X, int Y) {
        int imageCode = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rSet = null;
        try {
            conn = CDBConnectionPool.getConnection();
            stmt = conn.createStatement();
            rSet = stmt.executeQuery("select image from gamefield where x=" + X + " and y=" + Y);
            rSet.next();
            imageCode = rSet.getInt(1);
        } catch (SQLException E) {
            p_LOG.throwing(getClass().getName(), "respondMsgGamefieldRenderingInformationRequest()", E);
        } finally {
            try {
                rSet.close();
            } catch (Throwable Ignore) {
            }
            try {
                stmt.close();
            } catch (Throwable Ignore) {
            }
            if (conn != null) {
                CDBConnectionPool.returnConnection(conn);
            }
        }
        return new MsgGamefieldRenderingInformationResponse(X, Y, imageCode);
    }

    /** <p>Returning the building / service privileges for the gamefield.</p>
	 * 
	 * @param X The X position of the field.
	 * @param Y The Y position of the field.
	 * @return A message containing the privileges of the field.
	 */
    private MsgGamefieldPrivilegeResponse respondMsgGamefieldPrivilegeRequest(int X, int Y) {
        Integer[] privileges = new Integer[16];
        for (int i = 0; i < privileges.length; i++) {
            privileges[i] = null;
        }
        Connection conn = null;
        Statement stmt = null;
        ResultSet rSet = null;
        try {
            conn = CDBConnectionPool.getConnection();
            stmt = conn.createStatement();
            rSet = stmt.executeQuery("select privilege_type, privilege_owner from gamefield_privileges where x=" + X + " and y=" + Y);
            while (rSet.next()) {
                privileges[rSet.getInt(1)] = rSet.getInt(2);
            }
        } catch (SQLException E) {
            p_LOG.throwing(getClass().getName(), "respondMsgGamefieldPrivilegeRequest()", E);
        } finally {
            try {
                rSet.close();
            } catch (Throwable Ignore) {
            }
            try {
                stmt.close();
            } catch (Throwable Ignore) {
            }
            if (conn != null) {
                CDBConnectionPool.returnConnection(conn);
            }
        }
        return new MsgGamefieldPrivilegeResponse(X, Y, privileges);
    }

    /** <p>Returning the inhabitants of the gamefield.</p>
	 * 
	 * @param X The X position of the field.
	 * @param Y The Y position of the field.
	 * @return A message containing the inhabitants of the field.
	 */
    private MsgGamefieldInhabitantsResponse respondMsgGamefieldInhabitantsRequest(int X, int Y) {
        int inhabitants = 0;
        Connection conn = null;
        Statement stmt = null;
        ResultSet rSet = null;
        try {
            conn = CDBConnectionPool.getConnection();
            stmt = conn.createStatement();
            rSet = stmt.executeQuery("select inhabitants from gamefield where x=" + X + " and y=" + Y);
            rSet.next();
            inhabitants = rSet.getInt(1);
        } catch (SQLException E) {
            p_LOG.throwing(getClass().getName(), "respondMsgGamefieldInhabitantsRequest()", E);
        } finally {
            try {
                rSet.close();
            } catch (Throwable Ignore) {
            }
            try {
                stmt.close();
            } catch (Throwable Ignore) {
            }
            if (conn != null) {
                CDBConnectionPool.returnConnection(conn);
            }
        }
        return new MsgGamefieldInhabitantsResponse(X, Y, inhabitants);
    }

    @Override
    protected void p_ProcessMessage(net.internetrail.network.IMessage Message) {
        IMessage msg = CMessageFactory.getMessage(Message.getRawMessage());
        if (msg instanceof MsgGamefieldSizeRequest) {
            sendMessage(Message, m_GamefieldSizeResponse);
        } else if (msg instanceof MsgGamefieldRenderingInformationRequest) {
            MsgGamefieldRenderingInformationRequest req = (MsgGamefieldRenderingInformationRequest) msg;
            sendMessage(Message, respondMsgGamefieldRenderingInformationRequest(req.getGamefieldXPosition(), req.getGamefieldYPosition()).serialize());
        } else if (msg instanceof MsgGamefieldPrivilegeRequest) {
            MsgGamefieldPrivilegeRequest req = (MsgGamefieldPrivilegeRequest) msg;
            sendMessage(Message, respondMsgGamefieldPrivilegeRequest(req.getGamefieldXPosition(), req.getGamefieldYPosition()).serialize());
        } else if (msg instanceof MsgGamefieldInhabitantsRequest) {
            MsgGamefieldInhabitantsRequest req = (MsgGamefieldInhabitantsRequest) msg;
            sendMessage(Message, respondMsgGamefieldInhabitantsRequest(req.getGamefieldXPosition(), req.getGamefieldYPosition()).serialize());
        }
    }
}
