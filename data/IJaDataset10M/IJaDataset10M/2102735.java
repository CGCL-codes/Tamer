package clavicom.core.keygroup.keyboard.command;

import java.util.ArrayList;
import java.util.List;
import org.jdom.Element;
import clavicom.gui.language.UIString;
import clavicom.tools.TXMLNames;

public class CCommand implements Comparable<CCommand> {

    String caption;

    int id;

    String search_string;

    List<CCode> CodeList;

    public CCommand(int myId, String mySearchString, String myCaption) {
        caption = myCaption;
        id = myId;
        search_string = mySearchString;
        CodeList = new ArrayList<CCode>();
    }

    public CCommand() {
        caption = "";
        id = -1;
        search_string = "";
        CodeList = new ArrayList<CCode>();
    }

    /**
	 * Ajoute un code à la liste des codes
	 * Lance une exception si l'order n'est pas bon
	 */
    public void AddCode(int order, CCode code) {
        CodeList.add(order, code);
    }

    public String GetCaption() {
        return caption;
    }

    public int GetID() {
        return id;
    }

    public String GetSearchString() {
        return search_string;
    }

    /**
	 * Donne le code correspondant à l'order donné
	 * Lance une exception si l'order n'est pas bon
	 * @param order
	 * @return
	 */
    public CCode GetCode(int order) {
        return CodeList.get(order);
    }

    public int Size() {
        return CodeList.size();
    }

    public static CCommand BuildCommand(Element node) throws Exception {
        if (node == null) {
            throw new Exception("[" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "] : " + UIString.getUIString("EX_KEYGROUP_NOT_FIND_NODE"));
        }
        String s_id = node.getAttributeValue(TXMLNames.CM_ATTRIBUTE_ID);
        if (s_id == null) {
            throw new Exception("[" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "] : " + UIString.getUIString("EX_KEYGROUP_NOT_FIND_ATTRIBUTE") + TXMLNames.CM_ATTRIBUTE_ID);
        }
        int i_id;
        try {
            i_id = Integer.parseInt(s_id);
        } catch (Exception ex) {
            throw new Exception("[" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "] : " + UIString.getUIString("EX_KEYGROUP_CAN_NOT_CONVERT") + s_id + UIString.getUIString("EX_KEYGROUP_TO_INTEGER"));
        }
        String search_string = node.getAttributeValue(TXMLNames.CM_ATTRIBUTE_SEARCH_STRING);
        if (search_string == null) {
            throw new Exception("[" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "]" + "[ id:" + s_id + "] : " + UIString.getUIString("EX_KEYGROUP_NOT_FIND_ATTRIBUTE") + TXMLNames.CM_ATTRIBUTE_SEARCH_STRING);
        }
        String caption = node.getAttributeValue(TXMLNames.CM_ATTRIBUTE_CAPTION);
        if (caption == null) {
            throw new Exception("[" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "]" + "[ id:" + s_id + "] : " + UIString.getUIString("EX_KEYGROUP_NOT_FIND_ATTRIBUTE") + TXMLNames.CM_ATTRIBUTE_CAPTION);
        }
        CCommand command = new CCommand(i_id, search_string, caption);
        for (Object object : node.getChildren(TXMLNames.CS_ELEMENT_CODE)) {
            if (object instanceof Element) {
                Element element = (Element) object;
                if (element != null) {
                    String order = element.getAttributeValue(TXMLNames.CM_ATTRIBUTE_ORDER);
                    if (order == null) {
                        throw new Exception("[id : " + i_id + "][" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "] : " + UIString.getUIString("EX_KEYGROUP_NOT_FIND_ATTRIBUTE") + TXMLNames.CM_ATTRIBUTE_ORDER);
                    }
                    int orderInt = 0;
                    try {
                        orderInt = Integer.parseInt(order);
                    } catch (Exception ex) {
                        throw new Exception("[id : " + i_id + "][order : " + order + "][" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "] : " + UIString.getUIString("EX_KEYGROUP_CAN_NOT_CONVERT") + order + UIString.getUIString("EX_KEYGROUP_TO_INTEGER"));
                    }
                    CCode code = null;
                    try {
                        code = CCode.BuildCode(element);
                    } catch (Exception ex) {
                        throw new Exception("[id : " + i_id + "][order : " + orderInt + "]" + ex.getMessage());
                    }
                    try {
                        command.AddCode(orderInt, code);
                    } catch (Exception ex) {
                        throw new Exception("[id : " + i_id + "]order : " + orderInt + "][" + UIString.getUIString("EX_COMMAND_BUILD_COMMANDE") + "] : " + UIString.getUIString("EX_COMMAND_CAN_NOT_ADD_CODE") + orderInt + UIString.getUIString("EX_COMMAND_IN_THE_COMMANDE") + caption);
                    }
                }
            }
        }
        return command;
    }

    @Override
    public String toString() {
        return caption;
    }

    public int compareTo(CCommand arg0) {
        return arg0.GetCaption().compareTo(caption) * -1;
    }
}
