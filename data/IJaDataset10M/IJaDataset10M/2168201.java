package org.nirvana.jsrails.test;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import junit.framework.TestCase;
import com.sun.phobos.script.javascript.RhinoScriptEngineFactory;
import org.nirvana.jsrails.util.ScriptEnginePool;

public class TestScriptEnginePool extends TestCase {

    ScriptEnginePool pool;

    @Override
    protected void setUp() throws Exception {
        pool = new ScriptEnginePool(new RhinoScriptEngineFactory());
    }

    public void testMutlithread() throws Exception {
        for (int i = 0; i < 20; i++) {
            new Thread() {

                public void run() {
                    ScriptEngine e = pool.checkOut();
                    System.out.println("Start: " + e);
                    try {
                        e.eval("print('" + e + " - " + System.currentTimeMillis() + "')");
                    } catch (ScriptException e1) {
                        e1.printStackTrace();
                    }
                    pool.checkIn(e);
                }
            }.start();
        }
        Thread.sleep(2000);
    }
}
