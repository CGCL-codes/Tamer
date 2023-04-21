package org.powerstone.ca.service;

import java.util.*;
import org.powerstone.ca.model.*;

public interface ResourceManager {

    public String[][] getWebModules();

    public List getAllModules();

    public WebModule createWebModule(WebModule webModule);

    public Resource createResource(Resource resource);

    public void removeWebModule(String webModuleID);

    public void removeResource(String rsID);

    public WebModule findWebModule(String webModuleID);

    public Resource findResource(String rsID);

    public Resource findResourceByResourceID(String resourceID);

    public Resource findResourceByName(String resourceName);

    public WebModule updateWebModule(WebModule webModule);

    public Resource updateResource(Resource resource);

    public List getAllResources();

    public void addResourceToWebModule(String rsID, String webModuleID);
}
