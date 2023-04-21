package lechuga.container;

import lechuga.contextelems.LeContext;
import lechuga.contextelems.LeContextBean;
import lechuga.contextelems.LeContextProp;
import lechuga.dl.ComponentFactory;
import lechuga.exceptions.LechugaContextException;
import lechuga.exceptions.LechugaInjectorException;
import lechuga.exceptions.LechugaWrongIdBeanException;
import lechuga.reflect.Reflechuga;

/**
 * <p>Maintains a provided context definition structure, and serves any
 * requested Bean instance, solving his dependencies automatically,
 * performing the Dependency Injection features of Lechuga Container.</p>
 * 
 * <p>This services are abstracted to any context aquirement implementation,
 * such as XML parsed context, annotation-based definitions, etc.</p>
 * 
 * 
 * @author mhoms
 */
public class LechugaContext {

    /** context structure instance */
    protected LeContext context = null;

    /** 
	 * protected default constructor, avoiding specific 
	 * implementation constructing 
	 */
    public LechugaContext(final LeContext context) {
        super();
        this.context = context;
    }

    public LechugaContext(final String contextMedia) throws LechugaContextException {
        super();
        String contextCode = ComponentFactory.getContextObtainer().obtainContextDefinition(contextMedia);
        this.context = ComponentFactory.getContextParser(contextCode, contextMedia).parse();
    }

    /**
	 * performs the construction of the requested Bean, solving any 
	 * declared dependency injection in a recursive way.
	 * 
	 * @param idBean
	 * @return the requested Bean instance
	 * @throws LechugaInjectorException
	 * @throws LechugaWrongIdBeanException
	 */
    public Object getBeanInstance(final String idBean) throws LechugaInjectorException, LechugaWrongIdBeanException {
        Object returningObject = null;
        final Reflechuga reflechuga = Reflechuga.getInstance();
        final LeContextBean contextBean = this.context.getBean(idBean);
        if (contextBean == null) {
            throw new LechugaWrongIdBeanException(idBean, this.context);
        }
        if (contextBean.isUnique()) {
            if (contextBean.getUniqueInstance() == null) {
                contextBean.setUniqueInstance(reflechuga.getInstance(contextBean.getClassRef()));
            }
            returningObject = contextBean.getUniqueInstance();
        } else {
            returningObject = reflechuga.getInstance(contextBean.getClassRef());
        }
        if (contextBean.getProps() != null) {
            for (LeContextProp prop : contextBean.getProps()) {
                Object propBean = null;
                switch(prop.getInjType()) {
                    case Reflechuga.SETTER:
                        if (prop.getBean() != null) {
                            propBean = getBeanInstance(prop.getBean());
                            reflechuga.setterInjection(returningObject, prop.getName(), propBean);
                        }
                        if (prop.getBean() == null) {
                            reflechuga.setterInjectionWithStringConstruction(returningObject, prop.getName(), prop.getValue(), prop.getType());
                        }
                        break;
                    case Reflechuga.METHOD:
                        if (prop.getBean() != null) {
                            propBean = getBeanInstance(prop.getBean());
                            reflechuga.methodInvoker(returningObject, prop.getName(), propBean);
                        }
                        if (prop.getBean() == null) {
                            reflechuga.methodInvokerWithStringConstruction(returningObject, prop.getName(), prop.getValue(), prop.getType());
                        }
                        break;
                    case Reflechuga.FIELD:
                        if (prop.getBean() != null) {
                            propBean = getBeanInstance(prop.getBean());
                            reflechuga.publicFieldInjection(returningObject, prop.getName(), propBean);
                        }
                        if (prop.getBean() == null) {
                            reflechuga.publicFieldInjectionWithStringConstruction(returningObject, prop.getName(), prop.getValue(), prop.getType());
                        }
                        break;
                }
            }
        }
        return returningObject;
    }

    public LeContext getContext() {
        return context;
    }

    public void setContext(final LeContext context) {
        this.context = context;
    }
}
