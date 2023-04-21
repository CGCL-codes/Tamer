package it.conte.tesi.snmp.beans;

import it.conte.tesi.dao.beans.Switc;
import snmp.SNMPv1CommunicationInterface;

/**
 *
 * @author conte
 */
public class SwitchBean {

    private SystemBean sysBean;

    private InterfaceBean interfaceBean;

    private AtBean atBean;

    private BridgeBean bridgeBean;

    private String errorMessage;

    private SNMPv1CommunicationInterface communicationInterface;

    private Switc daoSwitch;

    public SwitchBean(Switc apparato) {
        this.daoSwitch = apparato;
    }

    public Switc getApparato() {
        return daoSwitch;
    }

    public void setApparato(Switc daoSwitc) {
        this.daoSwitch = daoSwitc;
    }

    public AtBean getAtBean() {
        return atBean;
    }

    public void setAtBean(AtBean atBean) {
        this.atBean = atBean;
    }

    public BridgeBean getBridgeBean() {
        return bridgeBean;
    }

    public void setBridgeBean(BridgeBean bridgeBean) {
        this.bridgeBean = bridgeBean;
    }

    public SNMPv1CommunicationInterface getCommunicationInterface() {
        return communicationInterface;
    }

    public void setCommunicationInterface(SNMPv1CommunicationInterface communicationInterface) {
        this.communicationInterface = communicationInterface;
    }

    public InterfaceBean getInterfaceBean() {
        return interfaceBean;
    }

    public void setInterfaceBean(InterfaceBean interfaceBean) {
        this.interfaceBean = interfaceBean;
    }

    public SystemBean getSysBean() {
        return sysBean;
    }

    public void setSysBean(SystemBean sysBean) {
        this.sysBean = sysBean;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
