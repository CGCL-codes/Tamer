package uips.interfacesManagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import uips.instances.IAppClient;
import uips.instances.Instance;
import uips.support.Consts;
import uips.support.localization.Messages;
import uips.support.logging.Log;
import uips.tree.enums.ModelUpdateEnum;
import uips.tree.inner.factories.impl.UipTreeInnFactory;
import uips.tree.inner.factories.interfaces.IUIPTreeInnFactory;
import uips.tree.inner.interfaces.IEventInn;
import uips.tree.inner.interfaces.IInterfaceInn;
import uips.tree.inner.interfaces.IPropertyInn;

/**
 * InterfaceManager holds list of interfaces of UIP application, parse interface
 * source file and relays requests for UIP interfaces from client.
 * <br><br>
 * Based on Miroslav Macik's C# version of UIProtocolServer
 *
 * @author Miroslav Macik (macikm1@fel.cvut.cz, CTU Prague, FEE)
 * @author Jindrich Basek (basekjin@fit.cvut.cz, CTU Prague, FIT)
 */
public class InterfaceManager implements IInterfaceManager {

    /**
     * Map of loaded interfaces (key - interface class, value - Interface instance)
     */
    private final Map<String, IInterfaceInn> interfaces;

    /**
     * Reference of Instance to which this InterfaceManager belongs
     */
    private final Instance instanceReference;

    /**
     * Loadin of all interfaces from files is finished (true)
     */
    private boolean loadingFinished;

    private final IUIPTreeInnFactory uipTreeInnFactory = UipTreeInnFactory.UIP_TREE_INN_FACTORY;

    /**
     * InterfaceManager constructor
     *
     * @param instanceReference reference of Instance to which this InterfaceManager belongs
     */
    public InterfaceManager(Instance instanceReference) {
        this.instanceReference = instanceReference;
        this.interfaces = new HashMap<String, IInterfaceInn>();
    }

    @Override
    public Map<String, IInterfaceInn> getInterfaces() {
        return this.interfaces;
    }

    /**
     * Set to true if loading of interfaces from files is finished
     *
     * @param loadingFinished Loadin of all interfaces from files is finished (true)
     */
    public void setLoadingFinished(boolean loadingFinished) {
        this.loadingFinished = loadingFinished;
    }

    @Override
    public IInterfaceInn getInterface(String clazz, IAppClient appClient) {
        while (!this.loadingFinished) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
            }
        }
        IInterfaceInn result = null;
        if (clazz != null) {
            if (appClient != null) {
                synchronized (appClient.getPrivateInterfaces()) {
                    result = appClient.getPrivateInterfaces().get(clazz);
                }
            }
            if (result == null) {
                synchronized (this.interfaces) {
                    result = this.interfaces.get(clazz);
                }
            }
        }
        return result;
    }

    @Override
    public boolean sendInterface(IAppClient appClient, String interfaceClass) {
        boolean result = false;
        IInterfaceInn requestedInterface = getInterface(interfaceClass, appClient);
        if (requestedInterface != null) {
            if (appClient != null) {
                appClient.sendInterface(requestedInterface);
            } else {
                synchronized (this.instanceReference.getClients()) {
                    for (IAppClient instanceXmlClient : this.instanceReference.getClients()) {
                        instanceXmlClient.sendInterface(requestedInterface);
                    }
                }
            }
            result = true;
        }
        return result;
    }

    @Override
    public void updateInterface(IInterfaceInn interfaceInn, IAppClient appClient) {
        if (interfaceInn != null) {
            String clazz = interfaceInn.getClazz();
            if (appClient != null) {
                synchronized (appClient.getPrivateInterfaces()) {
                    appClient.getPrivateInterfaces().put(clazz, interfaceInn);
                    appClient.sendInterface(interfaceInn);
                }
            } else {
                synchronized (this.interfaces) {
                    this.interfaces.put(clazz, interfaceInn);
                }
                synchronized (this.instanceReference.getClients()) {
                    for (IAppClient oneClient : this.instanceReference.getClients()) {
                        synchronized (oneClient.getPrivateInterfaces()) {
                            oneClient.getPrivateInterfaces().remove(interfaceInn.getClazz());
                            oneClient.sendInterface(interfaceInn);
                        }
                    }
                }
            }
        }
    }

    /**
     * Parse interface from Xml Node and creates new interface
     *
     * @param item Xml Node
     * @param path source xml file where is interface located
     */
    public void loadInterface(IInterfaceInn interfaceInn) {
        this.interfaces.put(interfaceInn.getClazz(), interfaceInn);
    }

    @Override
    public void handleInterfaceRequest(IEventInn event, IAppClient appClient) {
        if (event.getId() != null && event.getProperty(Consts.PropertyNameClass) != null && event.getProperty(Consts.PropertyNameClass).getValue() != null && !event.getProperty(Consts.PropertyNameClass).getValue().isEmpty()) {
            String interfaceClasses = event.getProperty(Consts.PropertyNameClass).getValue();
            Log.write(Level.FINE, String.format(Messages.getString("InterfaceRequestCalled"), interfaceClasses), this.instanceReference);
            String interfaceClass[] = interfaceClasses.split(Consts.RequestsIdsSeparator);
            if (interfaceClass.length > 0) {
                for (int i = 0; i < interfaceClass.length; i++) {
                    IInterfaceInn requestedIF = getInterface(interfaceClass[i], appClient);
                    if (requestedIF != null) {
                        appClient.sendInterface(requestedIF);
                    } else {
                        List<IPropertyInn> valuesToUpdate = new ArrayList<IPropertyInn>(1);
                        IPropertyInn propertyInn = this.uipTreeInnFactory.createPropertyInn(interfaceClass[i], null);
                        valuesToUpdate.add(propertyInn);
                        this.instanceReference.getModelManager().updateModel(Consts.ModelPublicInterfaces, valuesToUpdate, ModelUpdateEnum.partial, appClient, 0, null, false);
                        String errorMessage = String.format(Messages.getString("InterfaceWithClassNotFound"), interfaceClass[i]);
                        appClient.sendErrorResponse(Consts.ErrorInterfaceNotFound, Consts.PropertyValueError, errorMessage);
                        Log.write(Level.WARNING, errorMessage, this.instanceReference);
                    }
                }
            } else {
                appClient.sendErrorResponse(Consts.ErrorInterfaceClassMissing, Consts.PropertyValueError, Messages.getString("InterfaceRequestErrorMessage"));
                Log.write(Level.WARNING, Messages.getString("InterfaceRequestErrorMessage"), this.instanceReference);
            }
        } else {
            appClient.sendErrorResponse(Consts.ErrorInterfaceClassMissing, Consts.PropertyValueError, Messages.getString("InterfaceRequestErrorMessage"));
            Log.write(Level.WARNING, Messages.getString("InterfaceRequestErrorMessage"), this.instanceReference);
        }
    }
}
