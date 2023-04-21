package de.jlab.ui.modules.panels.dcg.characteristiccurve;

import de.jlab.boards.Board;
import de.jlab.config.DCGParameter;
import de.jlab.config.DCGParameter.TYPE;

public class DCGParam {

    String commChannelName;

    int address;

    DCGParameter.TYPE type;

    public String getCommChannelName() {
        return commChannelName;
    }

    public void setCommChannelName(String commChannelName) {
        this.commChannelName = commChannelName;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public DCGParameter.TYPE getType() {
        return type;
    }

    public void setType(DCGParameter.TYPE type) {
        this.type = type;
    }

    public DCGParam(Board board, TYPE type) {
        super();
        this.commChannelName = board.getCommChannel().getChannelName();
        this.address = board.getAddress();
        this.type = type;
    }

    public DCGParam(String commChannelName, int address, TYPE type) {
        super();
        this.commChannelName = commChannelName;
        this.address = address;
        this.type = type;
    }

    public DCGParam() {
    }

    public boolean equals(Object other) {
        if (!(other instanceof DCGParam)) {
            return false;
        } else {
            DCGParam otherParam = (DCGParam) other;
            return otherParam.address == address && otherParam.type == type && otherParam.getCommChannelName().equals(commChannelName);
        }
    }

    public int hashCode() {
        return (commChannelName + address + type).hashCode();
    }
}
