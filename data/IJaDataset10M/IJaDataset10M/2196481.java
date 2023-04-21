package icescrum2.presentation.advice;

import icescrum2.dao.model.IEntity;
import icescrum2.presentation.model.ModelCache;
import icescrum2.presentation.scrumos.model.ScrumOSobject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import javax.faces.context.FacesContext;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class AdapterAdvice implements MethodInterceptor {

    @SuppressWarnings("unchecked")
    public Object invoke(MethodInvocation methodInvoke) throws Throwable {
        Object[] args = methodInvoke.getArguments();
        int i;
        for (i = 0; i < args.length; i++) {
            if (args[i] instanceof ScrumOSobject<?>) {
                args[i] = this.entityUIToEntityBase(args[i]);
            } else if (args[i] instanceof Collection<?>) {
                this.entityUIToEntityBase((Collection) args[i]);
            }
        }
        Object returnValue = null;
        try {
            returnValue = methodInvoke.proceed();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (i = 0; i < args.length; i++) {
            if (args[i] instanceof Collection<?>) {
                this.entityBaseToEntityUI((Collection) args[i]);
            }
        }
        if (returnValue instanceof IEntity) {
            return this.entityBaseToEntityUI((IEntity) returnValue);
        }
        if (returnValue instanceof Collection) {
            this.entityBaseToEntityUI((Collection) returnValue);
        }
        return returnValue;
    }

    private Object entityUIToEntityBase(Object o) {
        return ((ScrumOSobject<?>) o).getEntity();
    }

    @SuppressWarnings("unchecked")
    private void entityUIToEntityBase(Collection returnCollection) {
        Iterator<?> it = returnCollection.iterator();
        List<Object> auxList = new ArrayList<Object>();
        Object currentO;
        while (it.hasNext()) {
            currentO = it.next();
            if (currentO instanceof Collection) {
                this.entityUIToEntityBase((Collection) currentO);
            } else if (currentO instanceof ScrumOSobject<?>) {
                auxList.add(this.entityUIToEntityBase(currentO));
            }
        }
        if (!auxList.isEmpty()) {
            returnCollection.clear();
            for (Object o : auxList) {
                returnCollection.add(o);
            }
        }
    }

    private Object entityBaseToEntityUI(IEntity returnValue) {
        return ((ModelCache) FacesContext.getCurrentInstance().getApplication().createValueBinding("#{modelCache}").getValue(FacesContext.getCurrentInstance())).updateEntity(returnValue);
    }

    @SuppressWarnings("unchecked")
    private void entityBaseToEntityUI(Collection returnCollection) {
        Iterator<?> it = returnCollection.iterator();
        List<Object> auxList = new ArrayList<Object>();
        Object currentO;
        while (it.hasNext()) {
            currentO = it.next();
            if (currentO instanceof Collection) {
                this.entityBaseToEntityUI((Collection) currentO);
            } else if (currentO instanceof IEntity) {
                auxList.add(this.entityBaseToEntityUI((IEntity) currentO));
            }
        }
        if (!auxList.isEmpty()) {
            returnCollection.clear();
            for (Object o : auxList) {
                returnCollection.add(o);
            }
        }
    }
}
