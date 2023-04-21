package uk.gov.dti.og.fox.command;

import java.util.HashMap;
import uk.gov.dti.og.fox.ContextUCon;
import uk.gov.dti.og.fox.ContextUElem;
import uk.gov.dti.og.fox.FoxModuleComponent;
import uk.gov.dti.og.fox.MapSet;
import uk.gov.dti.og.fox.Mod;
import uk.gov.dti.og.fox.XFUtil;
import uk.gov.dti.og.fox.XThread;
import uk.gov.dti.og.fox.XThread.CallStackTransformation;
import uk.gov.dti.og.fox.dom.DOM;
import uk.gov.dti.og.fox.ex.ExActionFailed;
import uk.gov.dti.og.fox.ex.ExDoSyntax;
import uk.gov.dti.og.fox.ex.ExInternal;

public class Cmd extends AbstractCommand implements Command, FoxModuleComponent {

    static final int ATTACH_CMD = 2;

    static final int APPLY_CMD = 3;

    static final int COPY_CMD = 8;

    static final int MOVE_CMD = 9;

    static final int REFRESH_MAP_SET_CMD = 10;

    static final int ATTACH_TO = 0;

    static HashMap mCommands = new HashMap();

    int f_cmd_code;

    String f_cmd_name;

    DOM f_underlying_xml;

    String f_s1;

    String mStateName = null;

    String mAttachPoint = null;

    /**
   * XML attributes of the command
   */
    private HashMap mAttributes;

    /** Generic Object array used to hold any command data */
    Object[] mData;

    public Cmd(String cmdName) throws ExInternal, ExDoSyntax {
        super(DOM.createDocument(cmdName));
        f_underlying_xml = null;
        f_cmd_name = cmdName;
        Integer i;
        if ((i = (Integer) mCommands.get(cmdName)) != null) {
            f_cmd_code = i.intValue();
        } else {
            throw new ExDoSyntax("Command not known: " + f_cmd_name);
        }
    }

    public Cmd(Mod pMod, DOM pParseUElem) throws ExInternal, ExDoSyntax {
        super(pParseUElem);
        f_underlying_xml = pParseUElem;
        f_cmd_name = pParseUElem.getLocalName();
        if (f_cmd_name.equals("load-document")) {
        } else if (f_cmd_name.equals("apply")) {
            f_cmd_code = APPLY_CMD;
        } else if (f_cmd_name.equals("attach")) {
            f_cmd_code = ATTACH_CMD;
            Object[] cmddata = { pParseUElem.getAttr("to") };
            mData = cmddata;
        } else if (f_cmd_name.equalsIgnoreCase("copy")) {
            f_cmd_code = COPY_CMD;
            mAttributes = new HashMap(pParseUElem.getAttributeMap());
        } else if (f_cmd_name.equalsIgnoreCase("move")) {
            f_cmd_code = MOVE_CMD;
            mAttributes = new HashMap(pParseUElem.getAttributeMap());
        } else if (f_cmd_name.equalsIgnoreCase("refresh-map-set")) {
            f_cmd_code = REFRESH_MAP_SET_CMD;
            mAttributes = new HashMap(pParseUElem.getAttributeMap());
        } else {
            throw new ExDoSyntax("Command not known", pParseUElem);
        }
    }

    public void run(XThread pXThread, ContextUElem pContextUElem, ContextUCon pContextUCon) throws ExInternal, ExActionFailed, CallStackTransformation {
        switch(f_cmd_code) {
            case APPLY_CMD:
                pXThread.fieldSetApplyChanges();
                break;
            case ATTACH_CMD:
                pContextUElem.repositionExtendedXPath(ContextUElem.ATTACH, (String) mData[ATTACH_TO], false);
                pXThread.resetScrollPosition();
                break;
            case COPY_CMD:
                {
                    try {
                        pXThread.copyActiveData(pContextUElem, (String) mAttributes.get("from"), (String) mAttributes.get("to"));
                    } catch (Exception ex) {
                        throw new ExInternal("XDo copy command failed", ex);
                    }
                }
                break;
            case MOVE_CMD:
                {
                    try {
                        pXThread.moveActiveData(pContextUElem, (String) mAttributes.get("from"), (String) mAttributes.get("to"));
                    } catch (Exception ex) {
                        throw new ExInternal("XDo copy command failed", ex);
                    }
                }
                break;
            case REFRESH_MAP_SET_CMD:
                {
                    String lName = (String) mAttributes.get("name");
                    if (XFUtil.isNull(lName)) {
                        throw new ExInternal("Bad syntax: Mapset name not present in refresh-mapset");
                    }
                    MapSet.resetMapSets(lName, pXThread);
                }
                break;
            default:
                throw new ExInternal("XDo cmd code not known: " + String.valueOf(f_cmd_code), f_underlying_xml);
        }
    }

    static {
        mCommands.put("attach", new Integer(ATTACH_CMD));
        mCommands.put("apply", new Integer(APPLY_CMD));
        mCommands.put("copy", new Integer(COPY_CMD));
        mCommands.put("move", new Integer(MOVE_CMD));
    }

    public boolean isCallTransition() {
        return false;
    }
}
