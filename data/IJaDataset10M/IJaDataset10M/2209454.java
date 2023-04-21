package net.sourceforge.mipa.eca;

import static config.Debug.DEBUG;
import java.rmi.RemoteException;
import java.util.ArrayList;
import net.sourceforge.mipa.ResultCallback;
import net.sourceforge.mipa.components.ContextRegister;
import net.sourceforge.mipa.components.MIPAResource;
import net.sourceforge.mipa.naming.Naming;
import net.sourceforge.mipa.predicatedetection.LocalPredicate;
import net.sourceforge.mipa.predicatedetection.PredicateType;
import net.sourceforge.mipa.test.DemoListener;

/**
 * 
 * @author Jianping Yu <jianp.yue@gmail.com>
 */
public class ECAManagerImp implements ECAManager {

    /** name of ECAManager */
    private String ecaManagerName;

    private ContextRegister contextRegister;

    private DataSource dataSource;

    public ECAManagerImp(ContextRegister contextRegister, DataSource dataSource, String ecaManagerName) {
        this.setContextRegister(contextRegister);
        this.ecaManagerName = ecaManagerName;
        this.dataSource = dataSource;
    }

    /**
     * @param ecaManagerName
     *            the ecaManagerName to set
     */
    public void setECAManagerName(String ecaManagerName) {
        this.ecaManagerName = ecaManagerName;
    }

    /**
     * @return the ecaManagerName
     */
    public String getECAManagerName() {
        return this.ecaManagerName;
    }

    /**
     * @param contextRegister
     *            the contextRegister to set
     */
    public void setContextRegister(ContextRegister contextRegister) {
        this.contextRegister = contextRegister;
    }

    /**
     * @return the contextRegister
     */
    public ContextRegister getContextRegister() {
        return contextRegister;
    }

    @Override
    public void registerLocalPredicate(LocalPredicate localPredicate, String groupId, PredicateType type) throws RemoteException {
        ResultCallback callback = null;
        try {
            Naming server = (Naming) java.rmi.Naming.lookup(MIPAResource.getNamingAddress() + "Naming");
            callback = (ResultCallback) server.lookup(groupId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Listener listener = new DemoListener(callback);
        Condition everything = new EmptyCondition(listener, localPredicate);
        if (DEBUG) {
            System.out.println("local predicate name is " + localPredicate.getName());
        }
        dataSource.attach(everything, localPredicate.getName());
    }

    /**
     * registers local resource to resource manager.
     * 
     * @param resources
     *            local resources
     */
    public void registerResources(ArrayList<SensorAgent> resources) {
        try {
            for (int i = 0; i < resources.size(); i++) {
                SensorAgent resource = resources.get(i);
                contextRegister.registerResource(resource.getName(), resource.getValueType(), ecaManagerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
