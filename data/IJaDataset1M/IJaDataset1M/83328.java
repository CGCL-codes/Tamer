package jmelib.utest;

import jmelib.MobileRuntimeException;
import jmelib.reflection.ReflectionFactory;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import java.util.Vector;

/**
 * @author Dmitry Shyshkin
 *         Date: 5/4/2007 19:12:17
 */
public class TestMIDlet extends MIDlet {

    private TestSuiteInfo[] infos;

    public void startApp() throws MIDletStateChangeException {
        display = Display.getDisplay(this);
        Form runningForm = new Form("Test results");
        StringItem item = new StringItem(null, "Please wait", Item.PLAIN);
        item.setLayout(Item.LAYOUT_CENTER | Item.LAYOUT_2);
        runningForm.append(item);
        runningForm.append(new Gauge("", false, Gauge.INDEFINITE, Gauge.CONTINUOUS_RUNNING));
        display.setCurrent(runningForm);
        display.callSerially(new Runnable() {

            public void run() {
                TestSuite[] suites = collectSuites();
                infos = new TestSuiteInfo[suites.length];
                for (int i = 0; i < suites.length; ++i) {
                    TestSuite suite = suites[i];
                    TestSuiteInfo suiteInfo = new TestSuiteInfo(suite);
                    infos[i] = suiteInfo;
                    Test[] tests = suite.getTests();
                    suiteInfo.setTests(tests);
                    suiteInfo.setFailed(new Throwable[tests.length]);
                    for (int j = 0; j < tests.length; ++j) {
                        Test test = tests[j];
                        try {
                            suite.setUp();
                            try {
                                test.run();
                            } finally {
                                suite.tearDown();
                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                            suiteInfo.getFailed()[j] = t;
                        }
                    }
                }
                display.setCurrent(new SuitesForm(infos, TestMIDlet.this));
            }
        });
    }

    private TestSuite[] collectSuites() {
        Vector suites = new Vector();
        String[] classes = ReflectionFactory.getInstance().getReflectedClasses();
        for (int i = 0; i < classes.length; ++i) {
            String className = classes[i];
            if (className.endsWith("TestSuite")) {
                try {
                    Class clazz = ReflectionFactory.getInstance().forName(className);
                    suites.addElement(clazz.newInstance());
                } catch (InstantiationException e) {
                    throw new MobileRuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new MobileRuntimeException(e);
                }
            }
        }
        TestSuite[] r = new TestSuite[suites.size()];
        suites.copyInto(r);
        return r;
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean b) throws MIDletStateChangeException {
    }

    public static Display display;
}
