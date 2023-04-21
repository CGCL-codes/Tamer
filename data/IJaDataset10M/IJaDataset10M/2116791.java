package com.nokia.ats4.appmodel.model.domain.usecase.impl;

import com.nokia.ats4.appmodel.model.domain.usecase.impl.UseCasePathImpl;
import com.nokia.ats4.appmodel.MainApplication;
import com.nokia.ats4.appmodel.model.KendoProject;
import com.nokia.ats4.appmodel.model.KendoSystemModel;
import com.nokia.ats4.appmodel.model.domain.traversal.DepthFirstTransitionIterator;
import com.nokia.ats4.appmodel.util.DummyProjectBuilder;
import junit.framework.*;
import com.nokia.ats4.appmodel.model.domain.FunctionalModel;
import com.nokia.ats4.appmodel.model.domain.State;
import com.nokia.ats4.appmodel.model.domain.Transition;
import com.nokia.ats4.appmodel.model.domain.impl.SystemModelImpl;
import com.nokia.ats4.appmodel.model.domain.usecase.UseCasePath;
import com.nokia.ats4.appmodel.util.Iterators;
import com.nokia.ats4.appmodel.util.Settings;
import com.nokia.ats4.appmodel.util.KendoResources;
import java.io.IOException;
import com.nokia.ats4.appmodel.util.KendoResources;
import com.nokia.ats4.appmodel.util.Settings;
import java.io.IOException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * UseCasePathImplTest
 *
 * @author Kimmo Tuukkanen
 * @version $Revision: 2 $
 */
public class UseCasePathImplTest extends TestCase {

    public UseCasePathImplTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        KendoResources.load(MainApplication.FILE_LANGUAGE);
        Settings.load(MainApplication.FILE_PROPERTIES);
    }

    protected void tearDown() throws Exception {
    }

    /**
     * Test of constructor, of class com.nokia.kendo.model.domain.usecase.impl.UseCasePathImpl.
     */
    public void testConstructor() {
        System.out.println("constructor");
        try {
            UseCasePath path = new UseCasePathImpl(null, null, -1);
            fail("did not throw exception as expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("did not throw IllegalArgumentExeception as expected");
        }
        try {
            UseCasePath path = new UseCasePathImpl(null, new ArrayList<Transition>(), -1);
            fail("did not throw exception as expected");
        } catch (IllegalArgumentException e) {
        } catch (Exception e) {
            fail("did not throw IllegalArgumentExeception as expected");
        }
    }

    /**
     * Test of getFirstState method, of class com.nokia.kendo.model.domain.usecase.impl.UseCasePathImpl.
     */
    public void testGetFirstAndLastState() {
        System.out.println("getFirstAndLastState");
        FunctionalModel fm = new SystemModelImpl(null);
        State[] states = new State[5];
        states[0] = fm.getStartState();
        for (int i = 1; i < states.length; i++) {
            states[i] = fm.addSystemState(null);
        }
        Transition[] transitions = new Transition[4];
        transitions[0] = fm.addTransition(states[0], states[1], null);
        transitions[1] = fm.addTransition(states[1], states[2], null);
        transitions[2] = fm.addTransition(states[2], states[3], null);
        transitions[3] = fm.addTransition(states[3], states[4], null);
        DepthFirstTransitionIterator df = new DepthFirstTransitionIterator(states[1]);
        UseCasePathImpl instance = new UseCasePathImpl(null, Iterators.asList(df), -1);
        assertEquals(states[1], instance.getFirstState());
        assertEquals(states[4], instance.getLastState());
    }

    /**
     * Test of iterator method, of class com.nokia.kendo.model.domain.usecase.impl.UseCasePathImpl.
     */
    public void testIterator() {
        System.out.println("iterator");
        try {
            Settings.load(MainApplication.FILE_PROPERTIES);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        KendoProject prj = DummyProjectBuilder.createProject();
        KendoSystemModel sysmodel = prj.getDefaultModel();
        DepthFirstTransitionIterator df = new DepthFirstTransitionIterator(sysmodel.getStartState());
        List<Transition> transitions = Iterators.asList(df);
        UseCasePath ucase = new UseCasePathImpl(null, transitions, -1);
        List<Transition> caseTransitions = Iterators.asList(ucase.iterator());
        assertEquals(transitions.size(), caseTransitions.size());
        for (int i = 0; i < transitions.size(); i++) {
            Transition a = transitions.get(i);
            Transition b = caseTransitions.get(i);
            assertTrue(a == b);
        }
    }
}
