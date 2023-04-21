package com.arcucomp.xmlcommand;

import com.arcucomp.xmgel.XMgeLEnvironment;

public class XCPresentationPutCommand_i extends XCCommand_i {

    public XCPresentationPutCommand_i(XMgeLEnvironment cm) {
        super(cm);
        (cm.logger()).println(5, "XCPresentationPutCommand(XCCustomCommandManager cm)<");
        this.cm = cm;
        (cm.logger()).println(5, "XCPresentationPutCommand()>");
    }

    public void finalize() {
        (cm.logger()).println(5, "XCPresentationPutCommand::finalize ends ");
    }

    public int processCommand() {
        (cm.logger()).println(5, "XCPresentationPutCommand_i::processCommand>");
        int status = 0;
        try {
            String sPosition = (String) m_subCommands.get("position");
            (cm.logger()).println(2, "XCPresentationPutCommand_i::processCommand-sPosition=[" + sPosition + "]");
            if (!sPosition.equals("")) {
                Integer tmpInt = new Integer(sPosition);
                int position = tmpInt.intValue();
                (cm.logger()).println(5, "XCPresentationPutCommand_i::processCommand-position=" + position);
                String xslStringToBePut = new String("");
                xslStringToBePut = (String) m_subCommands.get("XSLBuffer");
                (cm.logger()).println(2, "XCPresentationPutCommand_i::processCommand-xslStringToBePut=[" + xslStringToBePut + "]");
                String sURL = new String("");
                sURL = (String) m_subCommands.get("URL");
                if (sURL.equals("")) {
                    cm.setXSLAt(xslStringToBePut, position);
                    (cm.logger()).println(5, "XCPresentationPutCommand_i::processCommand-002 after String xslStringToBePut  =[" + xslStringToBePut + "] at  position=" + position + "");
                } else {
                    cm.setXSLAt(sURL, "true", position);
                    (cm.logger()).println(5, "XCPresentationPutCommand_i::processCommand-002 after String xslStringToBePut  =[" + xslStringToBePut + "] at  position=" + position + "");
                }
            } else {
                m_outgoingXSLBuffer = "";
                status = XC_ERROR_XSL_PUT_POSITION_REQUIRED;
            }
        } catch (Exception xmlEx) {
            (cm.logger()).println(1, "XCPresentationPutCommand_i::processCommand--Not a Command or not fomated properly" + "XML Exception: " + xmlEx.getMessage());
            status = XC_ERROR_XSL_PUT_COMMAND_FUNCTION_OR_POSITION;
        }
        super.setOutGoingXSLBuffer();
        (cm.logger()).println(5, "XCPresentationPutCommand::processCommand>");
        return status;
    }

    public String createPresPut(String storeName, String storeDescription, String storeURL) {
        (cm.logger()).println(5, "XCPresentationPutCommand::createPresPut<");
        String tmpstoreId = "";
        (cm.logger()).println(5, "XCPresentationPutCommand::createPresPut>");
        return tmpstoreId;
    }

    public String getPresPutFromPresPutId(String storeId) {
        (cm.logger()).println(5, "XCPresentationPutCommand::getPresPutFromPresPutId<");
        String return_store_string = new String("");
        (cm.logger()).println(5, "XCPresentationPutCommand::getPresPutFromPresPutId>");
        return return_store_string;
    }

    public String getPresPutUsingPresPutName(String storeName) {
        (cm.logger()).println(5, "XCPresentationPutCommand::getPresPutUsingPresPutName<");
        String return_store_string = new String("");
        (cm.logger()).println(5, "XCPresentationPutCommand::getPresPutUsingPresPutName> [" + return_store_string + "]");
        return return_store_string;
    }

    public String removePresPutUsingPresPutName(String storeName) {
        String return_store_string = new String("");
        return return_store_string;
    }
}
