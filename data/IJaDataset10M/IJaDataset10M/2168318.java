package org.nexopenframework.context.local;

import java.util.Map;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Data binder class related to thread-scope</p>
 * 
 * @author <a href="mailto:fme@nextret.net">Francesc Xavier Magdaleno</a>
 * @version 1.0
 * @since 1.0
 */
public class ThreadLocalMapDataBinder extends DataBinder {

    public ThreadLocalMapDataBinder(Object target) {
        super(target);
    }

    public void bind() {
        Map context = ThreadLocalMap.getContext();
        MutablePropertyValues mpvs = new MutablePropertyValues(context);
        this.doBind(mpvs);
    }
}
