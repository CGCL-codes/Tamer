package org.apache.tools.ant.taskdefs;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.BuildFileTest;

/**
 */
public class TaskdefTest extends BuildFileTest {

    public TaskdefTest(String name) {
        super(name);
    }

    public void setUp() {
        configureProject("src/etc/testcases/taskdefs/taskdef.xml");
    }

    public void test1() {
        expectBuildException("test1", "required argument not specified");
    }

    public void test2() {
        expectBuildException("test2", "required argument not specified");
    }

    public void test3() {
        expectBuildException("test3", "required argument not specified");
    }

    public void test4() {
        expectBuildException("test4", "classname specified doesn't exist");
    }

    public void test5() {
        expectBuildException("test5", "No public execute() in " + Project.class);
    }

    public void test5a() {
        executeTarget("test5a");
    }

    public void test6() {
        expectLog("test6", "simpletask: worked");
    }

    public void test7() {
        expectLog("test7", "worked");
    }

    public void testGlobal() {
        expectLog("testGlobal", "worked");
    }

    public void testOverride() {
        executeTarget("testOverride");
        String log = getLog();
        assertTrue("override warning sent", log.indexOf("Trying to override old definition of task copy") > -1);
        assertTrue("task inside target worked", log.indexOf("In target") > -1);
        assertTrue("task inside target worked", log.indexOf("In TaskContainer") > -1);
    }
}
