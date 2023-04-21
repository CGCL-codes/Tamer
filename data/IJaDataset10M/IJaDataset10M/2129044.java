package org.tagunit.test;

import org.tagunit.*;
import org.tagunit.taginfo.TagAttributeInfo;
import org.tagunit.taginfo.TagInfo;

/**
 * Wraps up the automatic tests for a tag handler.
 *
 * @author    Simon Brown
 */
public class AttributeTestPackage extends AbstractTagTestPackage {

    private String name;

    private String type;

    private boolean required;

    private boolean rtexprvalue;

    /**
   * Creates a new test package for the specified tag.
   *
   * @param tagInfo   a TagInfo instance
   */
    public AttributeTestPackage(TagInfo tagInfo, String name, String type, boolean required, boolean rtexprvalue) {
        super(tagInfo);
        this.name = name;
        this.type = type;
        this.required = required;
        this.rtexprvalue = rtexprvalue;
    }

    /**
   * Runs the tests that are part of this test package.
   *
   * @param testContext   the TestContext to which these tests are to be run
   */
    public void executeTest(TestContextContainer testContext) {
        TagAttributeInfo attr = getTagInfo().getAttribute(name);
        TestContext ctx;
        ctx = new DefaultTestContext("Attribute named " + name + " exists");
        testContext.add(ctx);
        boolean result = (attr != null);
        if (result) {
            ctx.setStatus(TestContext.PASS);
        } else {
            ctx.setStatus(TestContext.FAIL);
            ctx.setMessage("Attribute named " + name + " does not exist.");
            return;
        }
        if (type != null && type.length() > 0) {
            testType(attr, testContext);
        }
        testRequired(attr, testContext);
        testRequestTimeExpression(attr, testContext);
    }

    private void testType(TagAttributeInfo attr, TestContextContainer testContext) {
        TestContext ctx;
        ctx = new DefaultTestContext("Attribute named " + name + " is of type " + type);
        testContext.add(ctx);
        boolean result = type.equals(attr.getType());
        if (result) {
            ctx.setStatus(TestContext.PASS);
        } else {
            ctx.setStatus(TestContext.FAIL);
            ctx.setMessage("Attribute named " + name + " is of type " + attr.getType());
        }
    }

    private void testRequired(TagAttributeInfo attr, TestContextContainer testContext) {
        TestContext ctx;
        if (required) {
            ctx = new DefaultTestContext("Attribute named " + name + " is marked as required");
        } else {
            ctx = new DefaultTestContext("Attribute named " + name + " is marked as required");
        }
        testContext.add(ctx);
        boolean result = (required == attr.isRequired());
        if (result) {
            ctx.setStatus(TestContext.PASS);
        } else {
            ctx.setStatus(TestContext.FAIL);
            if (attr.isRequired()) {
                ctx.setMessage("Attribute named " + name + " is marked as required.");
            } else {
                ctx.setMessage("Attribute named " + name + " is marked as optional.");
            }
        }
    }

    private void testRequestTimeExpression(TagAttributeInfo attr, TestContextContainer testContext) {
        TestContext ctx;
        if (rtexprvalue) {
            ctx = new DefaultTestContext("Attribute named " + name + " accepts request time expressions");
        } else {
            ctx = new DefaultTestContext("Attribute named " + name + " doesn't accept request time expressions");
        }
        testContext.add(ctx);
        boolean result = (rtexprvalue == attr.canAcceptRequestTimeExpressions());
        if (result) {
            ctx.setStatus(TestContext.PASS);
        } else {
            ctx.setStatus(TestContext.FAIL);
            if (attr.canAcceptRequestTimeExpressions()) {
                ctx.setMessage("Attribute named " + name + " accepts runtime expressions.");
            } else {
                ctx.setMessage("Attribute named " + name + " doesn't accept runtime expressions.");
            }
        }
    }

    /**
   * Gets the name of this test package.
   *
   * @return  the name of this test package as a String
   */
    public String getName() {
        return "Attribute named " + this.name + " (required = " + required + ")";
    }
}
