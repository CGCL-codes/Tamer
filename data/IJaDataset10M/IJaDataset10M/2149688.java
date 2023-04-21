package it.hotel.aspects;

import it.hotel.model.hotel.IStructureObject;
import java.util.ArrayList;
import java.util.List;
import org.aopalliance.intercept.MethodInvocation;

/**
 * Advice che filtra le liste di oggetti restituite
 * dai metodi configurati. Viene effettuato un filtraggio
 * in base all'hotel associato all'utente.
 * 
 * @author ricky
 *
 */
public class SecurityListInterceptor extends AbstractSecurityInterceptor {

    /**
	 * @param
	 * @throws
	 * @return
	 */
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object rval = null;
        rval = invocation.proceed();
        if (!"admin".equals(getRoleName()) && rval != null) {
            List<IStructureObject> elements = (List) rval;
            List<IStructureObject> returned = new ArrayList<IStructureObject>();
            for (IStructureObject object : elements) {
                boolean permitted = object.isFromStructure(getUserHotelId());
                if (permitted) returned.add(object);
            }
            rval = returned;
        }
        return rval;
    }
}
