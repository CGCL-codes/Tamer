package net.sf.ahtutils.test;

import org.junit.Ignore;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;

public class PrintIgnoreRunListener extends RunListener {

    @Override
    public void testIgnored(Description description) throws Exception {
        super.testIgnored(description);
        Ignore ignore = description.getAnnotation(Ignore.class);
        String ignoreMessage = String.format("\t@Ignore test method %s(): '%s'", description.getMethodName(), ignore.value());
        System.out.println(ignoreMessage);
    }
}
