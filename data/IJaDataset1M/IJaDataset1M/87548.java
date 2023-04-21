package org.doraplatform.web.servlet.http.filter;

import java.util.List;
import javax.servlet.Filter;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;

/**
 * @author Björn Voß
 *
 */
public interface FilterChainFactory extends Ordered {

    List<Filter> getBeforeChain(ApplicationContext ac);

    List<Filter> getMainChain(ApplicationContext ac);

    List<Filter> getAfterChain(ApplicationContext ac);
}
