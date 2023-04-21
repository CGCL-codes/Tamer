package rql4j.domain.handler;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;
import rql4j.domain.Module;
import rql4j.domain.Template;
import rql4j.domain.User;
import java.util.EnumSet;

public class ModuleServerManagerFlagHandler implements FieldHandler {

    private static final Integer DELETE_USER = 131072;

    private static final Integer CREATE_USER = 262144;

    private static final Integer DELETE_GROUPS = 524288;

    private static final Integer CREATE_GROUPS = 1048576;

    private static final Integer ASSIGN_USER_TO_GROUPS = 2097152;

    private static final Integer EDIT_USER = 4194304;

    private static final Integer ADMINISTER_PLUG_INS = 8388608;

    private static final Integer ADMINISTER_USER_DEFINED_JOBS = 16777216;

    private static final Integer ADMINISTER_DATABASE_SERVER = 33554432;

    private static final Integer ADMINISTER_APPLICATION_SERVER = 67108864;

    private static final Integer ADMINISTER_DIRECTORY_SERVICES = 134217728;

    private static final Integer ADMINISTER_PROJECTS = 268435456;

    private static final Integer ADMINISTER_XCMS_PROJECTS = 536870912;

    private static final Integer ADMINISTER_DELIVERY_SERVER = 1073741824;

    @Override
    public Object getValue(Object o) throws IllegalStateException {
        Module module = (Module) o;
        EnumSet<Module.ServerManagerFlag> serverManagerFlagSet = module.getServerManagerFlagSet();
        if (serverManagerFlagSet != null) {
            Integer base = 0;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.DELETE_USER)) base += DELETE_USER;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.CREATE_USER)) base += CREATE_USER;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.DELETE_GROUPS)) base += DELETE_GROUPS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.CREATE_GROUPS)) base += CREATE_GROUPS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ASSIGN_USER_TO_GROUPS)) base += ASSIGN_USER_TO_GROUPS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.EDIT_USER)) base += EDIT_USER;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_PLUG_INS)) base += ADMINISTER_PLUG_INS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_USER_DEFINED_JOBS)) base += ADMINISTER_USER_DEFINED_JOBS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_DATABASE_SERVER)) base += ADMINISTER_DATABASE_SERVER;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_APPLICATION_SERVER)) base += ADMINISTER_APPLICATION_SERVER;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_DIRECTORY_SERVICES)) base += ADMINISTER_DIRECTORY_SERVICES;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_PROJECTS)) base += ADMINISTER_PROJECTS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_XCMS_PROJECTS)) base += ADMINISTER_XCMS_PROJECTS;
            if (serverManagerFlagSet.contains(Module.ServerManagerFlag.ADMINISTER_DELIVERY_SERVER)) base += ADMINISTER_DELIVERY_SERVER;
            return base.toString();
        }
        return null;
    }

    @Override
    public void setValue(Object o, Object value) throws IllegalStateException, IllegalArgumentException {
    }

    @Override
    public void resetValue(Object o) throws IllegalStateException, IllegalArgumentException {
    }

    @Override
    public void checkValidity(Object o) throws ValidityException, IllegalStateException {
    }

    @Override
    public Object newInstance(Object o) throws IllegalStateException {
        return null;
    }
}
