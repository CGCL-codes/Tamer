package org.amhm.ui.controler;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.rpc.ServiceException;
import org.amhm.persistence.PhoneType;
import org.amhm.services.persistence.PersistenceService;
import org.amhm.services.persistence.PersistenceServiceLocator;
import org.amhm.services.persistence.PersistenceServicePortType;

public class PhoneTypeControler implements Initializable {

    private static final PhoneTypeControler instance = new PhoneTypeControler();

    private final Set<PhoneType> phoneType = new HashSet<PhoneType>();

    private final Map<Integer, PhoneType> phoneTypeMap = new HashMap<Integer, PhoneType>();

    public static PhoneTypeControler getInstance() {
        return instance;
    }

    private PhoneTypeControler() {
    }

    public static Set<PhoneType> getPhoneTypes() {
        return instance.phoneType;
    }

    public static PhoneType getPhoneTypes(int value) {
        return instance.phoneTypeMap.get(Integer.valueOf(value));
    }

    public static java.math.BigInteger update(PhoneType type) {
        instance.phoneType.add(type);
        return new java.math.BigInteger("4");
    }

    @Override
    public String getControlerName() {
        return "Phone Type Controler";
    }

    @Override
    public void initialize() throws InitializationException {
        PersistenceService service = new PersistenceServiceLocator();
        try {
            PersistenceServicePortType port = service.getPersistenceServicePort();
            PhoneType[] ret = port.getPhoneTypes();
            for (PhoneType phonetype : ret) {
                phoneType.add(phonetype);
                phoneTypeMap.put(Integer.valueOf(phonetype.getTypeId()), phonetype);
            }
        } catch (ServiceException e) {
            throw new InitializationException(e);
        } catch (RemoteException e) {
            throw new InitializationException(e);
        }
    }
}
