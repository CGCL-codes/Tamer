package org.springframework.web.servlet.view;

import java.util.Map;
import org.springframework.beans.TestBean;

/**
 * used for VTL and FTL macro tests
 * 
 * @author Darren Davison
 * @since 25-Jan-05
 */
public class DummyMacroRequestContext {

    Map msgMap;

    String contextPath;

    TestBean command;

    public String getMessage(String code) {
        return (String) msgMap.get(code);
    }

    public String getMessage(String code, String defaultMsg) {
        String msg = (String) msgMap.get(code);
        return (msg == null) ? defaultMsg : msg;
    }

    public DummyBindStatus getBindStatus(String path) throws IllegalStateException {
        return new DummyBindStatus();
    }

    public DummyBindStatus getBindStatus(String path, boolean htmlEscape) throws IllegalStateException {
        return new DummyBindStatus();
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public void setMsgMap(Map msgMap) {
        this.msgMap = msgMap;
    }

    public TestBean getCommand() {
        return command;
    }

    public void setCommand(TestBean command) {
        this.command = command;
    }

    public class DummyBindStatus {

        public String getExpression() {
            return "name";
        }

        public String getValue() {
            return "Darren";
        }
    }
}
