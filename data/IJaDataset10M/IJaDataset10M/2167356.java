package mnf;

import android.util.Log;

/**
 * @class Room 
 * @brief GameLobby���� ����� Room class
 *
 * Lobby���� ����� Room�� �����Ѵ�.
 *
 * @author 
 */
public class onRoom {

    /**
	 *  Room�� ����.
	 */
    public onRoom(onConnector pConnector, onUser pPlayer) {
        m_pConnector = pConnector;
        m_pPlayerMe = pPlayer;
        m_pRoomInformation = new onRoomInformation();
        m_pPlayerList = new onUserList();
        m_pSendMessageBuffer = new onMessageBuffer();
    }

    /**
	 *  Room�� �Ҹ���.
	 */
    public void destruct() {
        if (null != m_pPlayerList) {
            m_pPlayerList.destruct();
            m_pPlayerList = null;
        }
        if (null != m_pRoomInformation) {
            m_pRoomInformation.destruct();
            m_pRoomInformation = null;
        }
    }

    private onConnector m_pConnector;

    private Object m_pObject;

    private onUser m_pPlayerMe;

    private onUser m_pPlayerOwner;

    private onUserList m_pPlayerList;

    private onRoomInformation m_pRoomInformation;

    private onMessageBuffer m_pSendMessageBuffer = null;

    /**
	 * ���� ������ ����Ѵ�.
	 * @param pPlayer Player * Player ��ü
	 */
    public void SetPlayerOwner(onUser pPlayer) {
        m_pPlayerOwner = pPlayer;
    }

    /**
	 * ����� ��ü�� �����Ѵ�.
	 * @param pObject void * ����� ��ü
	 */
    public void SetUserObject(Object pObject) {
        m_pObject = pObject;
    }

    /**
	 * ���� ������ Player ��ü�� ��ȯ�Ѵ�.
	 * @return Player * ��ü
	 */
    public onUser GetPlayerOwner() {
        return m_pPlayerOwner;
    }

    /**
	 * ����� ��ü�� ��ȯ�Ѵ�.
	 * @return void * ��ü
	 */
    public Object GetUserObject() {
        return m_pObject;
    }

    /**
	 * ���� �Ӽ� RoomInformation ��ü�� ��ȯ�Ѵ�.
	 * @return RoomInformation * ��ü
	 */
    public onRoomInformation GetRoomInformation() {
        return m_pRoomInformation;
    }

    /**
	 * ���� Player ����� ������ �ִ� PlayerList ��ü�� ��ȯ�Ѵ�.
	 * @return PlayerList * ��ü
	 */
    public onUserList GetPlayerList() {
        return m_pPlayerList;
    }

    /**
	 * ���� �� ���� ���� �Ӽ��� ���� ��û�Ѵ�.
	 * @param nMapID byte ���� ���� ������ MapID
	 * @param nBattleType byte ���� ���� ������ BattleType
	 * @param nOption byte ���� ���� ������ Option
	 */
    public void RequestModifyRoomProperty(byte nMapID, byte nBattleType, byte nBattleOption) {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notification_ModifyRoomProperty);
        m_pSendMessageBuffer.Put(nMapID);
        m_pSendMessageBuffer.Put(nBattleType);
        m_pSendMessageBuffer.Put(nBattleOption);
        m_pSendMessageBuffer.Flip();
        Log.d("Chat", "RequestModifyRoomProperty:::::::::");
        m_pConnector.Send(m_pSendMessageBuffer);
    }

    /**
	 * ���� �濡 ������ Player�� ���� ���� ��û�Ѵ�.
	 * @param nUserKey int ���� ���� ��ų Player�� Key
	 */
    public void RequestKickOutGamePlayer(int gamePlayerKey) {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notification_KickOutGamePlayer);
        m_pSendMessageBuffer.PutInt(gamePlayerKey);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
    }

    /**
	 * ���� �濡�� ���� ��û�Ѵ�.
	 */
    public void RequestExitRoom() {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notification_ExitRoom);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
        m_pRoomInformation.Initialize();
        m_pPlayerOwner = null;
        m_pPlayerList.RemoveAllWithItems(m_pPlayerMe);
        m_pPlayerList.Initialize();
    }

    /**
	 * ���� ���� ���� ���� ��û�Ѵ�.
	 * @param nPlayState byte ������ PlayState
	 */
    public void RequestSetPlayState(byte nPlayState) {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notification_SetPlayState);
        m_pSendMessageBuffer.Put(nPlayState);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
    }

    /**
	 * ���� ���� Player�鿡�� message ��� ��û�Ѵ�.
	 * @param nMessageFlag byte ��� ���� 0 : ���� ���� ��� Player, 1 : �ش� Player ����
	 * @param pMessage MessageBuffer * ����� MessageBuffer
	 */
    public void RequestSendGameData(byte nMessageFlag, int nUserKey, onMessageBuffer pMessage) {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notification_SendGameData);
        m_pSendMessageBuffer.Put(nMessageFlag);
        if (nMessageFlag == onGAME_DEF.SEND_TARGET_USER) {
            m_pSendMessageBuffer.PutInt(nUserKey);
        }
        m_pSendMessageBuffer.Put((onMessageBuffer) pMessage);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
    }

    /**
	 * ���� Lobby�� ��� ���� ����ڿ��� �ʴ� message�� ���� �� �ְ� ��û�Ѵ�.
	 * @param nPlayerKey int �ʴ��� Player�� Key
	 */
    public void RequestInvitePlayer(int nPlayerKey) {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notification_InvitePlayer);
        m_pSendMessageBuffer.PutInt(nPlayerKey);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
    }

    /**
	 * ���� Lobby�� Ư�� ����� Ȥ�� ��� ����ڿ��� chatting message�� ���� �� �ְ� ��û�Ѵ�.
	 * @param nMessageFlag byte ���� ����� ���� 1 : ������� ��� Player, 2 : ����� ��� Player, 3 : ����� ������ Team, 4 : Ư�� Player
	 * @param strMessage String ���� message
	 * @param nPlayerKey int nMessageFlag�� 4�� ����� Player�� Key
	 */
    public void RequestSendChattingMessage(byte nMessageFlag, String strMessage, int nPlayerKey) {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Notifacation_SendChattingMessage);
        m_pSendMessageBuffer.Put(nMessageFlag);
        byte[] temp = strMessage.getBytes();
        m_pSendMessageBuffer.Put((byte) temp.length);
        m_pSendMessageBuffer.Put(temp, temp.length);
        m_pSendMessageBuffer.PutInt(nPlayerKey);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
    }

    /**
	 * �ӽ÷� �߰� ���� ��׶��带 �������� ���� ��
	 */
    public void RequestRoomProperty() {
        m_pSendMessageBuffer.Init();
        m_pSendMessageBuffer.PutShort(onMessageID.Request_RoomProperty);
        m_pSendMessageBuffer.Flip();
        m_pConnector.Send(m_pSendMessageBuffer);
    }
}
