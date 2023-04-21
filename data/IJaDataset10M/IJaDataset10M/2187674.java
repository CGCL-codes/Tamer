package com.corratech.opensuite.web.administration.actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.struts2.interceptor.SessionAware;
import com.corratech.opensuite.web.administration.utils.CoreServiceUtil;
import com.corratech.opensuite.web.administration.utils.Utils;
import com.opensuite.bind.endpoint.Endpoint;
import com.opensuite.bind.endpoint.EndpointProperty;
import com.opensuite.bind.endpoint.EndpointType;
import com.opensymphony.xwork2.ActionSupport;

public class EndpointManagementAction extends ActionSupport implements SessionAware {

    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    private Map session;

    private List<Endpoint> endpoints;

    private List<String> endpointTypes;

    private Endpoint endpoint;

    private String endpointType;

    private List<Integer> ids;

    private String location;

    private String host;

    private String login;

    private String password;

    public String execute() throws Exception {
        if (session.get(Utils.CORESERVICEUTIL_SESSION_KEY) != null) {
            CoreServiceUtil util = (CoreServiceUtil) session.get(Utils.CORESERVICEUTIL_SESSION_KEY);
            endpoints = util.getEndpoints();
        }
        return SUCCESS;
    }

    public String edit() throws Exception {
        if (session.get(Utils.CORESERVICEUTIL_SESSION_KEY) != null) {
            CoreServiceUtil util = (CoreServiceUtil) session.get(Utils.CORESERVICEUTIL_SESSION_KEY);
            endpointTypes = new ArrayList<String>();
            List<EndpointType> types = util.getEndpointTypes();
            for (EndpointType type : types) {
                endpointTypes.add(type.value());
            }
            if (ids != null && !ids.isEmpty()) {
                endpoint = util.getEndpoint(ids.get(0));
                endpointType = endpoint.getType().value();
                List<EndpointProperty> properties = util.getEndpointProperties(endpoint.getId());
                if (properties != null) {
                    for (EndpointProperty property : properties) {
                        if (property.getKey().compareTo("LOCATION") == 0) {
                            location = property.getValue();
                            continue;
                        }
                        if (property.getKey().compareTo("HOST") == 0) {
                            host = property.getValue();
                            continue;
                        }
                        if (property.getKey().compareTo("LOGIN") == 0) {
                            login = property.getValue();
                            continue;
                        }
                        if (property.getKey().compareTo("PASSWORD") == 0) {
                            password = property.getValue();
                            continue;
                        }
                    }
                }
            }
        }
        return SUCCESS;
    }

    public String save() throws Exception {
        if (endpoint != null && session.get(Utils.CORESERVICEUTIL_SESSION_KEY) != null) {
            CoreServiceUtil util = (CoreServiceUtil) session.get(Utils.CORESERVICEUTIL_SESSION_KEY);
            endpoint.setType(EndpointType.fromValue(endpointType));
            List<EndpointProperty> properties = new ArrayList<EndpointProperty>();
            EndpointProperty property;
            if (location != "") {
                property = new EndpointProperty();
                property.setKey("LOCATION");
                property.setValue(location);
                properties.add(property);
            }
            if (host != "") {
                property = new EndpointProperty();
                property.setKey("HOST");
                property.setValue(host);
                properties.add(property);
            }
            if (login != "") {
                property = new EndpointProperty();
                property.setKey("LOGIN");
                property.setValue(login);
                properties.add(property);
            }
            if (password != "") {
                property = new EndpointProperty();
                property.setKey("PASSWORD");
                property.setValue(password);
                properties.add(property);
            }
            util.saveEndpoint(endpoint, properties);
        }
        return SUCCESS;
    }

    public String remove() throws Exception {
        if (ids != null && ids.size() > 0 && session.get(Utils.CORESERVICEUTIL_SESSION_KEY) != null) {
            CoreServiceUtil util = (CoreServiceUtil) session.get(Utils.CORESERVICEUTIL_SESSION_KEY);
            util.removeEndpoints(ids);
        }
        return SUCCESS;
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpointType() {
        return endpointType;
    }

    public void setEndpointType(String endpointType) {
        this.endpointType = endpointType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public List<String> getEndpointTypes() {
        return endpointTypes;
    }

    public void setEndpointTypes(List<String> endpointTypes) {
        this.endpointTypes = endpointTypes;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    @SuppressWarnings("unchecked")
    public void setSession(Map session) {
        this.session = session;
    }
}
