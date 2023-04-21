package plcopen.inf.type.group.common;

import plcopen.inf.model.IGraphicElement;

/**
 * 5가지의 common object에 대한 최상위 Interface.
 * 
 * @author swkim
 * 
 */
public interface ICommonObject extends IGraphicElement {

    String ID_CONTENT = "content";

    public static final String ID_COMMENT = "comment";

    public static final String ID_ERROR = "error";

    public static final String ID_CONNECTOR = "connector";

    public static final String ID_CONTINUATION = "continuation";

    public static final String ID_ACTIONBLK = "actionBlock";
}
