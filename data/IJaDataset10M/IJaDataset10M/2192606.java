package com.companyname.common.util.mvc.to;

import java.util.Vector;

/**
 * module 1...n model 1...n service 1...n view
 *                                  1...n formbean 1...field 1...n bc 1...n bc
 *                                                                    1...n validator
 */
public class ServiceTO {

    private String id;

    private String name;

    private String classPath;

    private ModuleTO moduleTO;

    private ModelTO modelTO;

    private Vector viewTOs;

    private boolean logSwitch;

    public ServiceTO(ModuleTO moduleTO, ModelTO modelTO, String id, String name, String classPath, boolean logSwitch) {
        this.moduleTO = moduleTO;
        this.modelTO = modelTO;
        this.id = id;
        this.name = name;
        this.classPath = classPath;
        this.logSwitch = logSwitch;
    }

    public ServiceTO(String id, String name, String classPath) {
        this.id = id;
        this.name = name;
        this.classPath = classPath;
        this.logSwitch = true;
    }

    public boolean isLogged() {
        return this.logSwitch;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getClassPath() {
        return this.classPath;
    }

    public Vector getViewTOs() {
        return this.viewTOs;
    }

    public ModuleTO getModuleTO() {
        return this.moduleTO;
    }

    public ModelTO getModelTO() {
        return this.modelTO;
    }

    public Object getServiceInst() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class cls = Class.forName(this.classPath);
        return cls.newInstance();
    }

    public String toString() {
        return "ServiceTO( id:" + this.id + ",name: " + this.name + " ,class:" + this.getClassPath() + " )";
    }
}
