package consciouscode.seedling.config.properties;

import consciouscode.seedling.NodeInstantiationException;
import consciouscode.seedling.NodePath;
import consciouscode.seedling.SimpleBean;
import java.util.Properties;

/**
 *
 */
public class CustomPropertiesEvaluatorTest extends PropertiesConfigTestCase {

    private static final String NODE_PATH = "/Custom";

    private static final String PARTIAL_EVAL_PATH = "config/evaluators/CustomEval";

    private static final String FULL_EVAL_PATH = NodePath.ROOT_NAME + PARTIAL_EVAL_PATH;

    private static final String MESSAGE = "evaluator injects into the bean";

    private void configureEvaluator() {
        Object eval = globalInstalledPath(FULL_EVAL_PATH);
        assertEquals(null, eval);
        Properties props = myConfigTree.configure(FULL_EVAL_PATH);
        setConstructorProperty(props, CustomPropertiesEvaluator.class);
        setStringProperty(props, "message", MESSAGE);
    }

    public void testCustomEvaluator() throws Exception {
        configureEvaluator();
        Properties props = myConfigTree.configure(NODE_PATH);
        props.setProperty(PropertiesConfigResource.EVALUATOR_METAPROPERTY, FULL_EVAL_PATH);
        SimpleBean node = (SimpleBean) globalRequiredPath(NODE_PATH);
        assertEquals(MESSAGE, node.getText());
        Object eval = globalInstalledPath(FULL_EVAL_PATH);
        assertEquals(CustomPropertiesEvaluator.class, eval.getClass());
    }

    public void testRelativeEvaluatorPath() throws Exception {
        configureEvaluator();
        Properties props = myConfigTree.configure(NODE_PATH);
        props.setProperty(PropertiesConfigResource.EVALUATOR_METAPROPERTY, PARTIAL_EVAL_PATH);
        setConstructorProperty(props, Object.class);
        SimpleBean node = (SimpleBean) globalRequiredPath(NODE_PATH);
        assertEquals(MESSAGE, node.getText());
    }

    public void testBadEvaluatorClass() throws Exception {
        Properties props = myConfigTree.configure(FULL_EVAL_PATH);
        setConstructorProperty(props, Object.class);
        props = myConfigTree.configure(NODE_PATH);
        props.setProperty(PropertiesConfigResource.EVALUATOR_METAPROPERTY, FULL_EVAL_PATH);
        try {
            globalRequiredPath(NODE_PATH);
            fail("expected exception");
        } catch (NodeInstantiationException e) {
        }
    }

    public void testEmptyEvaluatorProperty() throws Exception {
        Properties props = myConfigTree.configure(FULL_EVAL_PATH);
        setConstructorProperty(props, Object.class);
        props = myConfigTree.configure(NODE_PATH);
        props.setProperty(PropertiesConfigResource.EVALUATOR_METAPROPERTY, "");
        try {
            globalRequiredPath(NODE_PATH);
            fail("expected exception");
        } catch (NodeInstantiationException e) {
        }
    }

    public void testBadEvaluatorPath() throws Exception {
        Properties props = myConfigTree.configure(NODE_PATH);
        props.setProperty(PropertiesConfigResource.EVALUATOR_METAPROPERTY, "/bo gus");
        try {
            globalRequiredPath(NODE_PATH);
            fail("expected exception");
        } catch (NodeInstantiationException e) {
        }
    }
}
