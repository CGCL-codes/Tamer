package net.sf.traser.facade;

import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.sf.traser.configuration.ConfigurationException;
import net.sf.traser.databinding.base.Item;
import net.sf.traser.databinding.base.Status;
import net.sf.traser.databinding.management.Authorize;
import net.sf.traser.databinding.management.GetAuthorization;
import net.sf.traser.databinding.management.ListPartners;
import net.sf.traser.databinding.management.Manage;
import net.sf.traser.databinding.management.PartnersList;
import net.sf.traser.databinding.management.SetEndpoint;
import net.sf.traser.databinding.management.SetPartner;
import net.sf.traser.numbering.Resolver;
import net.sf.traser.service.AuthorizationFault;
import net.sf.traser.service.ExistenceFault;
import net.sf.traser.service.GeneralFault;
import net.sf.traser.service.Management;
import net.sf.traser.service.ManagementService;
import net.sf.traser.service.ManagementStub;
import net.sf.traser.storage.MetaDataStorager.Existence;
import net.sf.traser.storage.Storager;
import org.apache.axis2.AxisFault;

/**
 * The facade that directs the requests to the appropriate destination automatically.
 * @author Marcell Szathmári
 */
public class ManagementFacade extends AbstractFacade<Management> implements Management {

    /** The local service implementation. */
    protected ManagementService local;

    @Override
    public void configure() throws ConfigurationException {
        super.configure();
        local = depend(ManagementService.class);
        decorator = new ManagementServiceDecorator();
    }

    @Override
    protected Management createStub(String target) throws AxisFault {
        return new ManagementStubDecorator(factory.getClient(ManagementStub.class, target));
    }

    public Status authorize(Authorize authorize) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
        Item item = authorize.getItem();
        manager.get(Resolver.class).resolve(item);
        Storager storager = manager.get(Storager.class);
        if (storager != null) {
            Existence ex = storager.itemExists(item);
            if (ex == Existence.LOCAL) {
                return decorator.authorize(authorize);
            } else if (ex == Existence.AUGMENTED) {
                LinkedList<Authorize.Property> locals = new LinkedList<Authorize.Property>();
                ListIterator<Authorize.Property> iter = authorize.getProperties().listIterator();
                while (iter.hasNext()) {
                    Authorize.Property p = iter.next();
                    Manage.Property.Level level = storager.getPropertyDuplicationStatus(item, p.getName());
                    if (level == Manage.Property.Level.AUGMENT || level == Manage.Property.Level.OVERRIDE) {
                        locals.add(p);
                        iter.remove();
                    }
                }
                if (locals.size() > 0) {
                    List<Authorize.Property> backup = authorize.getProperties();
                    authorize.setProperties(locals);
                    decorator.authorize(authorize).getStatus();
                    authorize.setProperties(backup);
                }
                return getStub(item).authorize(authorize);
            } else {
                return getStub(item).authorize(authorize);
            }
        } else {
            return getStub(item).authorize(authorize);
        }
    }

    public Status setPartner(SetPartner setPartner) throws RemoteException, GeneralFault, AuthorizationFault {
        return local.setPartner(setPartner);
    }

    public Status manage(Manage manage) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
        return getInterface(manage.getHost()).manage(manage);
    }

    public PartnersList listPartners(ListPartners listPartners) throws RemoteException, GeneralFault, AuthorizationFault {
        return local.listPartners(listPartners);
    }

    public Status setEndpoint(SetEndpoint setEndpoint) throws RemoteException, GeneralFault, AuthorizationFault {
        return local.setEndpoint(setEndpoint);
    }

    public Authorize getAuthorization(GetAuthorization getAuthorization) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
        Item item = getAuthorization.getItem();
        manager.get(Resolver.class).resolve(item);
        Storager storager = manager.get(Storager.class);
        Management i = null;
        if (storager != null) {
            Existence ex = storager.itemExists(item);
            boolean locality = false;
            if (ex == Existence.LOCAL) {
                i = decorator;
            } else if (ex == Existence.AUGMENTED) {
                Manage.Property.Level level = storager.getPropertyDuplicationStatus(item, getAuthorization.getProperty());
                if (level == Manage.Property.Level.AUGMENT || level == Manage.Property.Level.OVERRIDE) {
                    i = decorator;
                }
            }
        }
        if (i == null) {
            i = getStub(item);
        }
        return i.getAuthorization(getAuthorization);
    }

    /** Decorator to easily access the local service. Delegates all methods to the local field of the encapsulating class. */
    private class ManagementServiceDecorator implements Management {

        public Status authorize(Authorize authorize) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
            return local.authorize(authorize);
        }

        public Status setPartner(SetPartner setPartner) throws RemoteException, GeneralFault, AuthorizationFault {
            return local.setPartner(setPartner);
        }

        public Status manage(Manage manage) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
            return local.manage(manage);
        }

        public PartnersList listPartners(ListPartners listPartners) throws RemoteException, GeneralFault, AuthorizationFault {
            return local.listPartners(listPartners);
        }

        public Status setEndpoint(SetEndpoint setEndpoint) throws RemoteException, GeneralFault, AuthorizationFault {
            return local.setEndpoint(setEndpoint);
        }

        public Authorize getAuthorization(GetAuthorization getAuthorization) throws GeneralFault, AuthorizationFault, ExistenceFault {
            return local.getAuthorization(getAuthorization);
        }
    }

    /** Decorator to easily access the client stubs. Delegates all methods to the local field of the encapsulating class. */
    private class ManagementStubDecorator implements Management {

        private final ManagementStub stub;

        public ManagementStubDecorator(ManagementStub stub) {
            this.stub = stub;
        }

        public synchronized Status setPartner(SetPartner setPartner) throws RemoteException, GeneralFault, AuthorizationFault {
            return stub.setPartner(setPartner);
        }

        public synchronized Status setEndpoint(SetEndpoint setEndpoint) throws RemoteException, GeneralFault, AuthorizationFault {
            return stub.setEndpoint(setEndpoint);
        }

        public synchronized Status manage(Manage manage) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
            return stub.manage(manage);
        }

        public synchronized PartnersList listPartners(ListPartners listPartners) throws RemoteException, GeneralFault, AuthorizationFault {
            return stub.listPartners(listPartners);
        }

        public synchronized Authorize getAuthorization(GetAuthorization getAuthorization) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
            return stub.getAuthorization(getAuthorization);
        }

        public synchronized Status authorize(Authorize authorize) throws RemoteException, GeneralFault, AuthorizationFault, ExistenceFault {
            return stub.authorize(authorize);
        }
    }
}
