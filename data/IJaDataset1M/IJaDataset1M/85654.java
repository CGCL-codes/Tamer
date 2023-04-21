package wsmg.msg_box;

import org.xmlpull.v1.builder.XmlInfosetBuilder;
import org.xmlpull.v1.builder.XmlNamespace;
import xsul.XmlConstants;
import java.net.URI;

/**
 * Some shared constants
 *
 */
public class MsgBoxConstants {

    private static final XmlInfosetBuilder builder = XmlConstants.BUILDER;

    public static final XmlNamespace MSG_BOX_NS = builder.newNamespace("http://www.extreme.indiana.edu/xgws/msgbox/2004/");

    public static final String MESSAGEBOX_DB_CONFIG_NAME = "db.config";

    public static String EL_BOX_ADDR = "MsgBoxAddr";

    public static String EL_CLIENT_ID = "ClientID";

    public static String OP_CREATE_BOX = "createMsgBox";

    public static String OP_DESTROY_BOX = "destroyMsgBox";

    public static String OP_TAKE_MSG = "takeMessages";

    public static String EL_MSG_STR = "messageAsString";

    public static URI ACTION_CREATE_BOX = URI.create(MSG_BOX_NS.getNamespaceName() + OP_CREATE_BOX);

    public static URI ACTION_DESTROY_BOX = URI.create(MSG_BOX_NS.getNamespaceName() + OP_DESTROY_BOX);

    public static URI ACTION_TAKE_MSG = URI.create(MSG_BOX_NS.getNamespaceName() + OP_TAKE_MSG);
}
