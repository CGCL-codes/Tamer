package org.jactr.core.runtime;

import junit.framework.TestCase;
import org.antlr.runtime.tree.CommonTree;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.core.model.IModel;
import org.jactr.core.runtime.controller.IController;
import org.jactr.core.runtime.controller.DefaultController;
import org.jactr.io.CommonIO;
import org.jactr.io.generator.CodeGeneratorFactory;
import org.jactr.io.resolver.ASTResolver;

/**
 * @author developer
 */
public class GeneralExecutionTest extends TestCase {

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(GeneralExecutionTest.class);

    private boolean _dumpOnStart = true;

    private boolean _dumpOnStop = true;

    /**
   * @see junit.framework.TestCase#setUp()
   */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ACTRRuntime.getRuntime().setController(new DefaultController());
    }

    /**
   * @see junit.framework.TestCase#tearDown()
   */
    @Override
    protected void tearDown() throws Exception {
        ACTRRuntime.getRuntime().setController(null);
        super.tearDown();
    }

    protected IModel load(String fileName) {
        LOGGER.info("Loading " + fileName);
        CommonTree md = CommonIO.parserTest(fileName, true, true);
        assertNotNull(md);
        LOGGER.info("Compiling " + fileName);
        CommonIO.compilerTest(md, true, true);
        LOGGER.info("Constructing " + fileName);
        IModel model = CommonIO.constructorTest(md);
        assertNotNull(model);
        return model;
    }

    protected IModel configureModel(IModel model) {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Attaching logger");
        return model;
    }

    protected void dump(IModel model) {
        CommonTree modelDesc = ASTResolver.toAST(model, true);
        for (StringBuilder line : CodeGeneratorFactory.getCodeGenerator("jactr").generate(modelDesc, true)) LOGGER.debug(line.toString());
    }

    protected void execute(IModel model) throws Exception {
        if (_dumpOnStart) {
            if (LOGGER.isDebugEnabled()) LOGGER.debug("Initial model state");
            dump(model);
        }
        ACTRRuntime.getRuntime().addModel(model);
        IController controller = ACTRRuntime.getRuntime().getController();
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Running");
        controller.start().get();
        controller.complete().get();
        assertFalse(controller.isRunning());
        if (LOGGER.isDebugEnabled()) LOGGER.debug("Terminated");
        assertTrue(controller.getTerminatedModels().contains(model));
        ACTRRuntime.getRuntime().removeModel(model);
        if (_dumpOnStop) dump(model);
        model.dispose();
    }

    protected void test(String modelLocation) throws Exception {
        IModel model = load(modelLocation);
        configureModel(model);
        execute(model);
    }

    public void testBasicSemantic() throws Exception {
        test("org/jactr/core/runtime/semantic-model.jactr");
    }

    public void testFullSemantic() throws Exception {
        test("org/jactr/core/models/semantic-full.jactr");
    }

    public void testAddition() throws Exception {
        test("org/jactr/core/models/addition.jactr");
    }

    public void testCount() throws Exception {
        test("org/jactr/core/models/count.jactr");
    }
}
