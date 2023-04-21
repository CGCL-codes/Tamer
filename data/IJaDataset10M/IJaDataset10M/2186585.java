package org.identifylife.util.workflow;

/**
 * @author mrickerby
 *
 * [based on the article by Steve Dodge: http://www.javaworld.com/javaworld/jw-04-2005/jw-0411-spring.html]
 */
public class BaseContext implements Context {

    @Override
    public void setSeedData(Object seedObject) {
    }

    @Override
    public boolean stopProcess() {
        return false;
    }
}
