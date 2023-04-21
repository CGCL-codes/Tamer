package br.ufal.ic.nexos.arcolive.service;

import br.ufal.ic.nexos.arcolive.descriptors.ProtocolDescriptor;

public class ArCoLIVEService {

    private int serviceId;

    private String name;

    private String description;

    private ProtocolDescriptor protocolDescriptor = null;

    public ArCoLIVEService(int serviceId) {
        this.serviceId = serviceId;
    }

    public ArCoLIVEService(int serviceId, String protocolClasspath) {
        this.serviceId = serviceId;
        this.protocolDescriptor = new ProtocolDescriptor(protocolClasspath);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getServiceId() {
        return serviceId;
    }

    @Override
    public int hashCode() {
        return this.serviceId;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof ArCoLIVEService) {
            return (this.serviceId == ((ArCoLIVEService) object).getServiceId());
        }
        return false;
    }

    public ProtocolDescriptor getProtocolDescriptor() {
        return protocolDescriptor;
    }
}
