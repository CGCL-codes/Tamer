package org.lightframework.mvc.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Home Controller
 * @author fenghm (live.fenghm@gmail.com)
 * @since 1.0.0
 */
public class Home {

    private static final Logger log = LoggerFactory.getLogger(Home.class);

    /**
	 * index action , mapping request path '/' and view '/index.jsp' 
	 */
    public void index() {
        log.info("** index action of home controller executed **");
    }

    public void throwError() {
        throw new RuntimeException("an error");
    }
}
