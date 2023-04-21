package org.nexopenframework.mock.web.descriptors;

import java.util.Map;
import javax.servlet.Filter;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Holder class which keeps information of a given {@link Filter}.</p>
 * 
 * @see javax.servlet.Filter
 * @author Francesc Xavier Magdaleno
 * @version $Revision ,$Date 03/05/2009 16:57:10
 * @since 2.0.0.GA
 */
public class FilterDescriptor {

    /**{@link Filter} that is managed*/
    public final Filter filter;

    /**init parameters of this {@link Filter}*/
    public final Map<String, String> initParameters;

    /**Where this filter is mapped*/
    public final String mapping;

    /**
	 * <p>Constructor which holds information about Filer</p>
	 * 
	 * @param filter
	 * @param mapping
	 * @param initParameters
	 */
    public FilterDescriptor(final Filter filter, final String mapping, final Map<String, String> initParameters) {
        this.filter = filter;
        this.mapping = mapping;
        this.initParameters = initParameters;
    }
}
