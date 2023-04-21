package com.yilan.rmi.service;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import com.yilan.component.runtime.ExternalReference;
import com.yilan.contract.Contract;
import com.yilan.resource.binding.ServiceBinding;
import com.yilan.rmi.util.RMIConstant;
import com.yilan.rmi.util.RmiInvocatonObject;

public class RmiServiceBinding implements ServiceBinding, RmiRemote {

    private static final long serialVersionUID = 7560620917843678130L;

    private transient Registry registry;

    private RmiServiceContainer container;

    public RmiServiceBinding() {
        super();
    }

    public RmiServiceBinding(Registry registry) {
        super();
        this.registry = registry;
    }

    @Override
    public String getBindingType() {
        return "yilan.java.rmi";
    }

    @Override
    public Object invoke(Object obj) throws Exception {
        return this.container.invoke(obj);
    }

    @Override
    public void start() {
        try {
            this.registry.bind(RMIConstant.REMOTE_OBJ, this);
            this.container = new RmiServiceContainer();
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
    }

    @Override
    public RmiInvocatonObject invoke(RmiInvocatonObject rio) throws RemoteException {
        return (RmiInvocatonObject) this.container.invoke(rio);
    }

    public RmiServiceContainer getContainer() {
        return container;
    }

    @Override
    public Contract getContract(String fullName) throws RemoteException {
        ExternalReference er = this.container.getReference(fullName);
        if (er != null) {
            return er.getContract();
        } else {
            return null;
        }
    }
}
