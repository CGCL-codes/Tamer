package com.google.inject.tools.suite;

import junit.framework.TestCase;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.tools.suite.SampleModuleScenario.BlueService;
import com.google.inject.tools.suite.SampleModuleScenario.BrokenModule;
import com.google.inject.tools.suite.SampleModuleScenario.CreditCard;
import com.google.inject.tools.suite.SampleModuleScenario.CreditCardPaymentService;
import com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterface;
import com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterface2;
import com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterface2Impl;
import com.google.inject.tools.suite.SampleModuleScenario.MockInjectedInterfaceImpl;
import com.google.inject.tools.suite.SampleModuleScenario.ModuleWithArguments;
import com.google.inject.tools.suite.SampleModuleScenario.One;
import com.google.inject.tools.suite.SampleModuleScenario.PaymentService;
import com.google.inject.tools.suite.SampleModuleScenario.Red;
import com.google.inject.tools.suite.SampleModuleScenario.RedService;
import com.google.inject.tools.suite.SampleModuleScenario.Service;
import com.google.inject.tools.suite.SampleModuleScenario.WorkingModule;
import com.google.inject.tools.suite.SampleModuleScenario.WorkingModule2;
import com.google.inject.tools.suite.module.ModuleContextRepresentation;
import com.google.inject.tools.suite.snippets.BindingCodeLocation;
import com.google.inject.tools.suite.snippets.ModuleContextSnippet;
import com.google.inject.tools.suite.snippets.bindings.KeyRepresentation;
import com.google.inject.tools.suite.snippets.problems.CreationProblem;
import com.google.inject.tools.suite.snippets.problems.InvalidModuleProblem;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * Unit test the {@link ModuleContextRepresentation} object.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class ModuleContextSnippetTest extends TestCase {

    private class ThreadWithStream extends Thread {

        private final OutputStream stream;

        private final String[] args;

        public ThreadWithStream(OutputStream stream, String[] args) {
            this.stream = stream;
            this.args = args;
        }

        @Override
        public void run() {
            ModuleContextSnippet.runSnippet(stream, args);
        }
    }

    private Object runASnippet(String[] args) throws Exception {
        PipedInputStream is = new PipedInputStream();
        PipedOutputStream os = new PipedOutputStream(is);
        new ThreadWithStream(os, args).start();
        ObjectInputStream ois = new ObjectInputStream(is);
        return ois.readObject();
    }

    public void testModuleContextSnippetModuleRepresentation() throws Exception {
        ModuleContextSnippet.ModuleRepresentation module = new ModuleContextSnippet.ModuleRepresentation(WorkingModule.class, null, null);
        assertNotNull(module.getInstance());
    }

    /**
   * Test that constructing a working module context happens without problems
   * and that the correct binding location is constructed.
   */
    public void testConstructWorkingModuleContext() throws Exception {
        String[] args = new String[4];
        args[0] = "Working Module Context";
        args[1] = "1";
        args[2] = WorkingModule.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + MockInjectedInterface.class.getName(), null);
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + MockInjectedInterfaceImpl.class.getName()));
        assertTrue(location.file().equals(WorkingModuleBindFile));
        assertTrue(location.location() == WorkingModuleBindLocation);
    }

    private static final int WorkingModuleBindLocation = 45;

    private static final String WorkingModuleBindFile = "SampleModuleScenario.java";

    private static final int WorkingModuleBindLocation2 = 80;

    private static final String WorkingModuleBindFile2 = "SampleModuleScenario.java";

    /**
   * Test that constructing a broken module context causes a
   * {@link com.google.inject.tools.suite.snippets.problems.CreationProblem}.
   */
    public void testConstructBrokenModuleContext() throws Exception {
        String[] args = new String[4];
        args[0] = "Broken Module Context";
        args[1] = "1";
        args[2] = BrokenModule.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertFalse(result.getProblems().isEmpty());
        assertTrue(result.getInjector().bindings().size() == 0);
        assertTrue(result.getProblems().iterator().next() instanceof CreationProblem);
    }

    /**
   * Test that constructing an invalid module context fails.
   */
    public void testConstructInvalidModule() throws Exception {
        String[] args = new String[4];
        args[0] = "Invalid Module Context";
        args[1] = "1";
        args[2] = ModuleWithArguments.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertFalse(result.getProblems().isEmpty());
        assertTrue(result.getProblems().iterator().next() instanceof InvalidModuleProblem);
    }

    /**
   * Test that using multiple modules in a single context works correctly.
   */
    public void testMultipleModulesInjector() throws Exception {
        String[] args = new String[6];
        args[0] = "Working Module Context";
        args[1] = "2";
        args[2] = WorkingModule.class.getName();
        args[3] = "0";
        args[4] = WorkingModule2.class.getName();
        args[5] = "0";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + MockInjectedInterface.class.getName(), null);
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + MockInjectedInterfaceImpl.class.getName()));
        assertTrue(location.file().equals(WorkingModuleBindFile));
        assertTrue(location.location() == WorkingModuleBindLocation);
        KeyRepresentation key2 = new KeyRepresentation("interface " + MockInjectedInterface2.class.getName(), null);
        BindingCodeLocation location2 = new BindingCodeLocation(result.getName(), key2, result.getInjector().bindings().get(key2));
        assertNotNull(location);
        assertTrue(location2.bindTo().equals("class " + MockInjectedInterface2Impl.class.getName()));
        assertTrue(location2.file().equals(WorkingModuleBindFile2));
        assertTrue(location2.location() == WorkingModuleBindLocation2);
    }

    public void testCustomModuleContext() throws Exception {
        String[] args = new String[4];
        args[0] = "Custom Context";
        args[1] = String.valueOf(-1);
        args[2] = SampleModuleScenario.CustomContextBuilder.class.getName();
        args[3] = "getModules";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getModules().contains(WorkingModule.class.getName()));
        assertTrue(result.getAllProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + MockInjectedInterface.class.getName(), null);
        assertNotNull(result.getInjector().bindings().get(key));
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + MockInjectedInterfaceImpl.class.getName()));
        assertTrue(location.file().equals(WorkingModuleBindFile));
        assertTrue(location.location() == WorkingModuleBindLocation);
    }

    public void testStaticCustomModuleContext() throws Exception {
        String[] args = new String[4];
        args[0] = "Custom Context";
        args[1] = String.valueOf(-1);
        args[2] = SampleModuleScenario.StaticCustomContextBuilder.class.getName();
        args[3] = "getModules";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + MockInjectedInterface.class.getName(), null);
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + MockInjectedInterfaceImpl.class.getName()));
        assertTrue(location.file().equals(WorkingModuleBindFile));
        assertTrue(location.location() == WorkingModuleBindLocation);
    }

    public void testNamedBindingCodeLocation() throws Exception {
        String[] args = new String[4];
        args[0] = "Working Module Context";
        args[1] = "1";
        args[2] = WorkingModule.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + Service.class.getName(), "@" + Named.class.getName() + "(value=blue)");
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + BlueService.class.getName()));
    }

    public void testAnnotatedBindingCodeLocation() throws Exception {
        String[] args = new String[4];
        args[0] = "Working Module Context";
        args[1] = "1";
        args[2] = WorkingModule.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + Service.class.getName(), "@" + Red.class.getName());
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + RedService.class.getName()));
    }

    public void testBindAnnotatedConstant() throws Exception {
        String[] args = new String[4];
        args[0] = "Working Module Context";
        args[1] = "1";
        args[2] = WorkingModule.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("class java.lang.Integer", "@" + One.class.getName());
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindToInstance().equals(String.valueOf(1)));
    }

    public void testBindTypeLiteral() throws Exception {
        String[] args = new String[4];
        args[0] = "Working Module Context";
        args[1] = "1";
        args[2] = WorkingModule.class.getName();
        args[3] = "0";
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getProblems().isEmpty());
        assertNotNull(result.getInjector());
        TypeLiteral<PaymentService<CreditCard>> creditCardPaymentService = new TypeLiteral<PaymentService<CreditCard>>() {
        };
        KeyRepresentation key = new KeyRepresentation(creditCardPaymentService.toString(), null);
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + CreditCardPaymentService.class.getName()));
    }

    public void testGuiceIDEContextDef() throws Exception {
        String[] args = new String[3];
        args[0] = "Custom Context";
        args[1] = String.valueOf(-1);
        args[2] = SampleModuleScenario.Helper.class.getName();
        Object obj = runASnippet(args);
        assertTrue(obj instanceof ModuleContextSnippet.ModuleContextResult);
        ModuleContextSnippet.ModuleContextResult result = (ModuleContextSnippet.ModuleContextResult) obj;
        assertTrue(result.getName().equals("Custom Context"));
        assertTrue(result.getAllProblems().isEmpty());
        assertNotNull(result.getInjector());
        KeyRepresentation key = new KeyRepresentation("interface " + MockInjectedInterface.class.getName(), null);
        BindingCodeLocation location = new BindingCodeLocation(result.getName(), key, result.getInjector().bindings().get(key));
        assertNotNull(location);
        assertTrue(location.bindTo().equals("class " + MockInjectedInterfaceImpl.class.getName()));
        assertTrue(location.file().equals(WorkingModuleBindFile));
        assertTrue(location.location() == WorkingModuleBindLocation);
    }
}
