package net.taylor.validation;

import junit.framework.TestCase;
import org.drools.DroolsException;
import org.drools.DroolsRuntimeException;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spring.factory.RuleBuilder;
import org.drools.spring.metadata.AccessAndReturnTypeMethodMetadataSource;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.ChainedArgumentMetadataSource;
import org.drools.spring.metadata.ChainedMethodMetadataSource;
import org.drools.spring.metadata.ChainedRuleMetadataSource;
import org.drools.spring.metadata.ClassInferedRuleMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.ParameterInferedTypeArgumentMetadataSource;
import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.RuleMetadataSource;
import org.drools.spring.metadata.annotation.java.AnnotationArgumentMetadataSource;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

public class PojoRulesTest extends TestCase {

    private static RuleBuilder builder = new RuleBuilder();

    private ChainedRuleMetadataSource ruleMetadataSource;

    private ChainedMethodMetadataSource methodMetadataSource;

    private ChainedArgumentMetadataSource argumentMetadataSource;

    public PojoRulesTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
        ruleMetadataSource = new ChainedRuleMetadataSource(new RuleMetadataSource[] { new ClassInferedRuleMetadataSource() });
        methodMetadataSource = new ChainedMethodMetadataSource(new MethodMetadataSource[] { new AccessAndReturnTypeMethodMetadataSource() });
        argumentMetadataSource = new ChainedArgumentMetadataSource(new ArgumentMetadataSource[] { new AnnotationArgumentMetadataSource(), new ParameterInferedTypeArgumentMetadataSource() });
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private RuleSet createRuleSet(String name) throws Exception {
        RuleSet ruleSet = new RuleSet(name);
        Rule rule = createRule(new HelloWorldRules.Hello());
        ruleSet.addRule(rule);
        rule = createRule(new HelloWorldRules.Goodbye());
        ruleSet.addRule(rule);
        rule = createRule(new HelloWorldRules.Debug());
        ruleSet.addRule(rule);
        return ruleSet;
    }

    private Rule createRule(Object pojo) {
        RuleMetadata ruleMetadata = ruleMetadataSource.getRuleMetadata(pojo.getClass());
        Rule rule = new Rule(ruleMetadata.getName());
        setRuleProperties(rule, ruleMetadata);
        builder.setMethodMetadataSource(methodMetadataSource);
        builder.setArgumentMetadataSource(argumentMetadataSource);
        try {
            return builder.buildRule(rule, pojo);
        } catch (DroolsException e) {
            throw new DroolsRuntimeException(e);
        }
    }

    private void setRuleProperties(Rule rule, RuleMetadata ruleMetadata) {
        if (ruleMetadata.getDocumentation() != null) {
            rule.setDocumentation(ruleMetadata.getDocumentation());
        }
        if (ruleMetadata.getSalience() != null) {
            rule.setSalience(ruleMetadata.getSalience().intValue());
        }
        if (ruleMetadata.getDuration() != null) {
            rule.setDuration(ruleMetadata.getDuration().longValue());
        }
        if (ruleMetadata.getNoLoop() != null) {
            rule.setNoLoop(ruleMetadata.getNoLoop().booleanValue());
        }
    }

    public void stHello() throws Exception {
        RuleSet ruleSet = (RuleSet) createRuleSet("hello");
        RuleBaseBuilder builder = new RuleBaseBuilder();
        builder.addRuleSet(ruleSet);
        RuleBase ruleBase = builder.build();
        run(ruleBase);
    }

    public void run(RuleBase ruleBase) throws FactException {
        System.out.println("FIRE RULES(Hello)");
        System.out.println("----------");
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
        workingMemory.assertObject("Hello");
        workingMemory.fireAllRules();
        System.out.println("\n");
        System.out.println("FIRE RULES(GoodBye)");
        System.out.println("----------");
        workingMemory = ruleBase.newWorkingMemory();
        workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
        workingMemory.assertObject("Goodbye");
        workingMemory.fireAllRules();
    }

    public void testNotNull() {
        World world = new World();
        ClassValidator<World> classValidator = new ClassValidator<World>(World.class);
        InvalidValue[] validationMessages = classValidator.getInvalidValues(world);
        assertEquals(1, validationMessages.length);
        System.out.println("\nMessages:");
        for (int i = 0; i < validationMessages.length; i++) {
            System.out.println(validationMessages[i].getMessage());
            System.out.println(validationMessages[i].getBeanClass());
            System.out.println(validationMessages[i].getBean());
            System.out.println(validationMessages[i].getPropertyName());
            System.out.println(validationMessages[i].getValue());
        }
    }

    public void estHello() {
        try {
            World world = new World();
            world.setMessage("Hello");
            ClassValidator<World> classValidator = new ClassValidator<World>(World.class);
            InvalidValue[] validationMessages = classValidator.getInvalidValues(world);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
