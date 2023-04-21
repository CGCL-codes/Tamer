package com.pl.itsense.ftsm.common.impl;

import java.lang.management.ManagementFactory;
import java.security.KeyStore.Builder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import com.pl.itsense.ftsm.common.IAdaptable;
import com.pl.itsense.ftsm.common.IJMXModel;
import com.pl.itsense.ftsm.common.IMarketInfo;
import com.pl.itsense.ftsm.common.IModel;
import com.pl.itsense.ftsm.common.IParameter;
import com.pl.itsense.ftsm.common.IParametrizable;
import com.pl.itsense.ftsm.common.IPlatform;
import com.pl.itsense.ftsm.common.IPlatformLifecycle;
import com.pl.itsense.ftsm.common.IPlatformState;
import com.pl.itsense.ftsm.common.IPlatformVariable;

public class Platform implements IPlatform {

    private final ArrayList<IModel> models = new ArrayList<IModel>();

    private final HashMap<String, IPlatformVariable> platformVariables = new HashMap<String, IPlatformVariable>();

    private final boolean registerMBeans;

    public Platform(boolean registerMBeans) {
        this.registerMBeans = registerMBeans;
        platformVariables.put(IPlatformLifecycle.class.getName(), new PlatformLifecycle());
        platformVariables.put(IMarketInfo.class.getName(), new MarketInfo());
        platformVariables.put(IPlatformState.class.getName(), new PlatformState());
    }

    @Override
    public List<IModel> getModels() {
        return models;
    }

    @Override
    public void add(IModel model) {
        models.add(model);
        model.setPlatform(this);
    }

    @Override
    public void remove(IModel model) {
        models.remove(model);
        model.setPlatform(null);
    }

    @Override
    public void addPlatformVariable(String name, IPlatformVariable platformVariable) {
        platformVariables.put(name, platformVariable);
    }

    @Override
    public IPlatformVariable getPlatformVariable(String name) {
        return platformVariables.get(name);
    }

    private void registerCommonMBeans(final MBeanServer mbs) {
        try {
            mbs.registerMBean(new FTSMCommonJMX(), new ObjectName("com.pl.itsense.ftsm.common", "type", "FTSMCommonJMX"));
        } catch (InstanceAlreadyExistsException e) {
            e.printStackTrace();
        } catch (MBeanRegistrationException e) {
            e.printStackTrace();
        } catch (NotCompliantMBeanException e) {
            e.printStackTrace();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void registerJMXModels() {
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        registerCommonMBeans(mbs);
        for (IModel model : models) {
            if (model instanceof IAdaptable) {
                final IJMXModel jmxModel = ((IAdaptable) model).getExtension(IJMXModel.class);
                if (jmxModel != null) {
                    try {
                        final ObjectName objectName = jmxModel.getObjectName();
                        final Object mBeanImpl = jmxModel.getMBeanImpl();
                        if ((objectName != null) && (mBeanImpl != null)) {
                            mbs.registerMBean(mBeanImpl, objectName);
                        }
                    } catch (InstanceAlreadyExistsException e) {
                        e.printStackTrace();
                    } catch (MBeanRegistrationException e) {
                        e.printStackTrace();
                    } catch (NotCompliantMBeanException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (IModel model : models) {
            builder.append(model + "\n");
        }
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    @Override
    public void init() {
        if (registerMBeans) {
            registerJMXModels();
        }
    }
}
