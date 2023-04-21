package hu.uszeged.inf.wlab.netspotter.common.port;

import hu.uszeged.inf.wlab.netspotter.common.device.SwitchRole;

public class STPPortRole {

    public static enum StpPortRole {
    }

    public static enum StpPortState {

        disabled, blocking, listening, learning, forwarding, broken
    }

    boolean enabled;

    int vlan;

    int priority;

    StpPortRole portRole;

    StpPortState portState;

    String designatedRootAddress;

    int designatedCost;

    String designatedBridgeAddress;

    int designatedPortNum;

    public int getVlan() {
        return vlan;
    }

    public void setVlan(int vlan) {
        this.vlan = vlan;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public StpPortRole getPortRole() {
        return portRole;
    }

    public void setPortRole(StpPortRole portRole) {
        this.portRole = portRole;
    }

    public StpPortState getPortState() {
        return portState;
    }

    public void setPortState(StpPortState portState) {
        this.portState = portState;
    }

    public String getDesignatedRootAddress() {
        return designatedRootAddress;
    }

    public void setDesignatedRootAddress(String designatedRootAddress) {
        this.designatedRootAddress = designatedRootAddress;
    }

    public int getDesignatedCost() {
        return designatedCost;
    }

    public void setDesignatedCost(int designatedCost) {
        this.designatedCost = designatedCost;
    }

    public String getDesignatedBridgeAddress() {
        return designatedBridgeAddress;
    }

    public void setDesignatedBridgeAddress(String designatedBridgeAddress) {
        this.designatedBridgeAddress = designatedBridgeAddress;
    }

    public int getDesignatedPortNum() {
        return designatedPortNum;
    }

    public void setDesignatedPortNum(int designatedPortNum) {
        this.designatedPortNum = designatedPortNum;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String toString() {
        String result;
        if (enabled) {
            result = "Enabled\n";
        } else {
            result = "Disabled\n";
        }
        result = result + "Designated bridge address: " + designatedBridgeAddress + "\n";
        result = result + "Designated port number: " + designatedPortNum + "\n";
        return result;
    }
}
