package com.javector.adaptive.util.dto;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: javector
 * Date: May 14, 2006
 * Time: 1:41:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SoajServiceDTO {

    private String serviceName;

    private List<SoajPortDTO> soajPortDTOs;

    public List<SoajPortDTO> getSoajPortDTOs() {
        return soajPortDTOs;
    }

    public void setSoajPortDTOs(List<SoajPortDTO> soajPortDTOs) {
        this.soajPortDTOs = soajPortDTOs;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
