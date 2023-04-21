package org.monet.kernel.model;

import java.net.URI;
import org.monet.kernel.model.definition.ServiceProviderDefinition;

public class ServiceProvider extends BaseObject {

    private URI serviceURI;

    private String label;

    private String username;

    private String password;

    private ServiceProviderDefinition declaration;

    public ServiceProvider() {
        super();
        this.serviceURI = null;
        this.username = "";
        this.password = "";
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setServiceUri(URI serviceUri) {
        this.serviceURI = serviceUri;
    }

    public URI getServiceUri() {
        return serviceURI;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setDefinition(ServiceProviderDefinition definition) {
        this.declaration = definition;
    }

    public ServiceProviderDefinition getDefinition() {
        return this.declaration;
    }

    public String serializeToJSON() {
        String result;
        Boolean isPartialLoading = this.isPartialLoading();
        URI serviceUri;
        if (isPartialLoading) this.disablePartialLoading();
        serviceUri = this.getServiceUri();
        result = "\"id\":\"" + this.getId() + "\",";
        result += "\"code\":\"" + this.getCode() + "\",";
        result += "\"service\":\"" + this.declaration.getLabel() + "\",";
        result += "\"label\":\"" + this.getLabel() + "\",";
        result += "\"url\":\"" + ((serviceUri != null) ? serviceUri.toString() : "") + "\",";
        result += "\"username\":\"" + this.getUsername() + "\",";
        result += "\"password\":\"" + this.getPassword() + "\"";
        if (isPartialLoading) this.enablePartialLoading();
        return "{" + result + "}";
    }
}
