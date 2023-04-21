package com.hydra.eu.device.xbee.service;

import java.util.LinkedList;
import com.rapplogic.xbee.api.XBeeResponse;
import com.rapplogic.xbee.api.zigbee.ZNetRemoteAtResponse;
import com.rapplogic.xbee.util.ByteUtils;

public class DigitalOuts extends Service {

    private final String SERVICE_NAME = "DigitalOuts";

    private int ID = 200;

    private LinkedList<ZNetRemoteAtResponse> numOfResp = new LinkedList<ZNetRemoteAtResponse>();

    public DigitalOuts() {
        super.setServiceName(this.SERVICE_NAME);
    }

    public DigitalOuts(String[] commands, int[][] values) {
        this();
        super.setCommandAndValue(commands, values);
        super.setNumOfOut(commands.length);
    }

    public DigitalOuts(String name, String[] commands, int[][] values) {
        for (int i = 0; i < name.length(); i++) {
            this.ID += name.charAt(i);
        }
        super.setServiceName(name);
        super.setCommandAndValue(commands, values);
        super.setNumOfOut(commands.length);
    }

    public void setPinAndState(String[] commands, int[][] values) {
        super.setCommandAndValue(commands, values);
    }

    @Override
    public int getID() {
        return this.ID;
    }

    public void setID(int id) {
        this.ID = id;
    }

    @Override
    public void processData(XBeeResponse response) {
        if (this.numOfResp.size() < super.getNumOfOut()) {
            ZNetRemoteAtResponse res = (ZNetRemoteAtResponse) response;
            this.numOfResp.add(res);
            if (this.numOfResp.size() == super.getNumOfOut()) {
                LinkedList<CommandAndValue> commandsList = (LinkedList<CommandAndValue>) super.getCommandAndValue();
                String processedData = "";
                for (ZNetRemoteAtResponse resOnList : this.numOfResp) {
                    for (CommandAndValue cmdVal : (LinkedList<CommandAndValue>) commandsList) {
                        if (resOnList.getCommandName().compareTo(cmdVal.getCommand()) == 0) {
                            processedData += "\nCommand " + cmdVal.getCommand() + " set to " + ByteUtils.toBase10(cmdVal.getValue());
                        }
                    }
                }
                super.setServiceData(processedData);
                this.numOfResp = null;
                this.numOfResp = new LinkedList<ZNetRemoteAtResponse>();
            }
        }
    }
}
