package ces.platform.bbs;

/**
 * ��̳�쳣
 * @author sword
 *
 * �����ﶨ��������̳�׳����쳣
 */
public interface ForumException {

    /**
     * ������ʾ��
     */
    int ERROR_EXAMPLE = 0;

    int BOOKMARK_NOT_FOUND = 1;

    int FORUMBOARD_NOT_FOUND = 2;

    int FORUMMESSAGE_NOT_FOUND = 3;

    int FORUMTHREAD_NOT_FOUND = 4;

    int LINK_NOT_FOUND = 5;

    int LINK_ALREADY_EXIST = 6;

    int NOT_ADMIN_OR_MODERATOR = 7;

    int NOT_ADMIN_OR_MODERATOR_OWNER = 8;

    int CAN_NOT_ACCESS_BOARD = 9;

    int NOT_ADMIN = 10;

    int NOT_ADMIN_OWNER = 11;

    int CAN_NOT_POST = 12;

    int CAN_NOT_REPLY = 13;

    int CAN_NOT_UPLOAD = 14;

    int CAN_NOT_VOTE = 15;

    int USER_NOT_FOUND = 16;

    int USER_LOCKED = 17;

    int NONE_AUTHORIZATION = 18;

    int FORUMCOMMUNITY_NOT_FOUND = 19;

    int USER_IN_BLACKLIST = 20;

    int IP_IN_BLACKLIST = 21;

    int MESSAGE_DETELED = 22;

    int MESSAGE_NOT_PASSED = 23;

    int USERNAME_TOO_SHORT = 24;

    int USERNAME_TOO_LONG = 25;

    int USERNAME_IS_RESERVE = 26;

    int QUESTION_OR_ANSWER_IS_NOT_CORRECT = 27;

    int NOT_LOGIN = 28;

    int SUPER_ADMIN_NOT_AUTHORIZED = 29;

    int NOT_SUPER_ADMIN = 30;

    int NOT_ENOUGH_PARAMS = 31;

    int PARAMS_ERROE = 32;

    int ATTACHMENT_NOT_FOUND = 33;

    /**
     * ȡ�ô�����
     * @return int ������
     */
    public int getErrorCode();
}
