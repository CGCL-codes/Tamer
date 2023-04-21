package org.mitre.scap.oval.check;

import gov.nist.checklists.xccdf.x12.MessageType;
import gov.nist.checklists.xccdf.x12.MsgSevEnumType;
import gov.nist.checklists.xccdf.x12.ResultEnumType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.mitre.oval.xmlSchema.ovalCommon5.MessageLevelEnumeration;
import org.mitre.oval.xmlSchema.ovalCommon5.ClassEnumeration;
import org.mitre.oval.xmlSchema.ovalResults5.DefinitionType;
import org.mitre.oval.xmlSchema.ovalResults5.ResultEnumeration;

/**
 * Table 4-2 of NIST SP 800-126 states the following for XCCDF Results -> OVAL Results mappings
 *
 * OVAL Definition Result                             XCCDF Result
 * ----------------------                             --------------
 * ERROR                                              ERROR
 * UNKNOWN                                            UNKNOWN
 * NOT APPLICABLE                                     NOT APPLICABLE
 * NOT EVALUATED                                      NOT CHECKED
 *
 * 
 * Definition Class     Definition Result             XCCDF Result
 * -----------------    -----------------             ------------
 * COMPLIANCE           TRUE                          PASS
 * VULNERABILITY        FALSE                         PASS
 * INVENTORY            TRUE                          PASS
 * PATCH                FALSE                         PASS
 *
 * COMPLIANCE           FALSE                         FAIL
 * VULNERABILITY        TRUE                          FAIL
 * INVENTORY            FALSE                         FAIL
 * PATCH                TRUE                          FAIL
 *
 * @author bworrell
 */
public class OVALDefinitionResult {

    private final String id;

    private final ResultEnumType.Enum result;

    private final List<MessageType> messages;

    private final org.mitre.oval.xmlSchema.ovalCommon5.ClassEnumeration.Enum definitionType;

    public OVALDefinitionResult(final DefinitionType definition, org.mitre.oval.xmlSchema.ovalCommon5.ClassEnumeration.Enum definitionClass) {
        this.id = definition.getDefinitionId();
        this.definitionType = definitionClass;
        switch(definition.getResult().intValue()) {
            case ResultEnumeration.INT_ERROR:
                result = ResultEnumType.ERROR;
                break;
            case ResultEnumeration.INT_NOT_APPLICABLE:
                result = ResultEnumType.NOTAPPLICABLE;
                break;
            case ResultEnumeration.INT_NOT_EVALUATED:
                result = ResultEnumType.NOTCHECKED;
                break;
            case ResultEnumeration.INT_FALSE:
                if (definitionClass.equals(ClassEnumeration.VULNERABILITY) || definitionClass.equals(ClassEnumeration.PATCH)) {
                    result = ResultEnumType.PASS;
                } else {
                    result = ResultEnumType.FAIL;
                }
                break;
            case ResultEnumeration.INT_TRUE:
                if (definitionClass.equals(ClassEnumeration.COMPLIANCE) || definitionClass.equals(ClassEnumeration.INVENTORY) || definitionClass.equals(ClassEnumeration.MISCELLANEOUS)) {
                    result = ResultEnumType.PASS;
                } else {
                    result = ResultEnumType.FAIL;
                }
                break;
            case ResultEnumeration.INT_UNKNOWN:
            default:
                result = ResultEnumType.UNKNOWN;
                break;
        }
        if (definition.sizeOfMessageArray() == 0) {
            messages = Collections.emptyList();
        } else {
            messages = new ArrayList<MessageType>(definition.sizeOfMessageArray());
            for (org.mitre.oval.xmlSchema.ovalCommon5.MessageType messageType : definition.getMessageList()) {
                MessageType message = MessageType.Factory.newInstance();
                switch(messageType.getLevel().intValue()) {
                    case MessageLevelEnumeration.INT_DEBUG:
                        message.setSeverity(MsgSevEnumType.INFO);
                        message.setStringValue("DEBUG: " + messageType.getStringValue());
                        break;
                    case MessageLevelEnumeration.INT_ERROR:
                        message.setSeverity(MsgSevEnumType.ERROR);
                        message.setStringValue(messageType.getStringValue());
                        break;
                    case MessageLevelEnumeration.INT_FATAL:
                        message.setSeverity(MsgSevEnumType.ERROR);
                        message.setStringValue("Fatal: " + messageType.getStringValue());
                        break;
                    case MessageLevelEnumeration.INT_INFO:
                        message.setSeverity(MsgSevEnumType.INFO);
                        message.setStringValue(messageType.getStringValue());
                        break;
                    case MessageLevelEnumeration.INT_WARNING:
                        message.setSeverity(MsgSevEnumType.WARNING);
                        message.setStringValue(messageType.getStringValue());
                        break;
                }
            }
        }
    }

    public String getId() {
        return id;
    }

    public ResultEnumType.Enum getResult() {
        return result;
    }

    public List<MessageType> getMessages() {
        return messages;
    }
}
