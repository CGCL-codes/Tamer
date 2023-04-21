package br.com.sisacad.interceptor;

import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.sisacad.annotation.Transactional;

@Intercepts
public class TransactionInterceptor implements Interceptor {

    private final Session session;

    public TransactionInterceptor(Session session) {
        this.session = session;
    }

    public boolean accepts(ResourceMethod method) {
        return method.getMethod().isAnnotationPresent(Transactional.class) || method.getResource().getType().isAnnotationPresent(Transactional.class);
    }

    public void intercept(InterceptorStack stack, ResourceMethod method, Object instance) {
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            stack.next(method, instance);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw new InterceptionException(e);
        }
    }
}
