package mnf;

public interface onRESULT_CODE {

    public static final short Login_SUCCESS = 5001;

    public static final short Login_UNKNOWN_USER_ID = 5101;

    public static final short Login_MISS_PASSWORD = 5102;

    public static final short Login_UNKNOW_FAIL = 5199;

    /** 
	 * [Response]
	 * Name				| Type	| Size(Bytes)	| Description
	 * Header			| Int16	| 2				| 0x1011
	 * RESULT_CODE		| Int16	| 2				| ��� CODE
	 * 					| 		| 				| 5001: ����
	 * 					| 		| 				| 5101: ��ϵ��� ���� Game Id
	 * 					| 		| 				| 5102: ��ϵ��� ���� Game Lobby Id
	 * 					| 		| 				| 5103: ����� ���� ����
	 * 					| 		| 				| 5104:�ߺ��� Login
	 * 					| 		| 				| 5105: �������������
	 * 					| 		| 				| 5197:DB ó�� ����
	 * 					| 		| 				| 5198: �ִ� �����ο� �ʰ�
	 * 					| 		| 				| 5199: Unknow
	 * USER_KEY			| Int32	| 4				| [OPTION]RESULT_CODE�� ������ ��쿡�� ��۵�	
	 */
    public final short Connect_SUCCESS = 5001;

    public final short Connect_UNREGISTERD_GAMEID = 5101;

    public final short Connect_UNREGISTERD_LOBBYID = 5102;

    public final short Connect_AUTHOENTICATION_FAIL = 5103;

    public final short Connect_OVERLAPPING_LOGIN = 5104;

    public final short Connect_DB_ERROR = 5197;

    public final short Connect_EXCEED_MAXUSER = 5198;

    public final short Connect_UNKNOW_FAIL = 5199;

    /** 
	 * [Response]
	 * Name				| Type	| Size(Bytes)	| Description
	 * Header			| Int16	| 2				| 0x2031
	 * RESULT_CODE		| Int16	| 2				| ����ڵ�
	 * 					| 		| 				| 5001: ����
	 * 					| 		| 				| 5201: �游��� ����
	 * 					| 		| 				| 5299: Unknown
	 * ROOM_ID			| Int16	| 2				| 
	 */
    public final short CreateRoom_SUCCESS = 5001;

    public final short CreateRoom_FAIL = 5201;

    public final short CreateRoom_UNKNOWN = 5299;

    /** 
	 * [Response]
	 * Name				| Type				| Size(Bytes)			| Description
	 * Header			| Int16				| 2						| 0x2061
	 * RESULT_CODE		| Int16				| 2						| ��� Code
	 * 					| 					| 						| 5001: ����
	 * 					| 					| 						| 5202: �������� �ʴ� ��
	 * 					| 					| 						| 5203: ��й�ȣ Ʋ��
	 * 					| 					| 						| 5204: ��� �ο� �ʰ�
	 * 					| 					| 						| 5205: ������
	 * 					| 					| 						| 5206: JOIN ����
	 * 					| 					| 						| 5299: Unknow
	 * ROOM_PROPERTY	| ROOM_PROPERTY		| 						| 	
	 * PLAYER_DATA_SIZE	| Int8				| 1						| �濡 ������ �ִ� User��
	 * PLAYER_DATA		| PLAYER_DATA[n]	| n = PLAYER_DATA_SIZE	| 
	 */
    public final short JoinRoom_SUCCESS = 5001;

    public final short JoinRoom_NOT_EXIST_ROOM = 5202;

    public final short JoinRoom_WRONG_PASSWORD = 5203;

    public final short JoinRoom_EXCEED_MAX_USER = 5204;

    public final short JoinRoom_PLAYING = 5205;

    public final short JoinRoom_FAIL = 5206;

    public final short JoinRoom_UNKNOWN = 5299;
}
