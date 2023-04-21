package test.argument;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.jargument.ArgumentTool;
import org.jargument.annotation.Argument;
import org.jargument.annotation.Argumentable;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings("unused")
public class TestSimpleObjects {

    @Test(description = "simple - non-primitives - defValue", groups = { "simple", "non-primitives", "defValue" })
    public void defaultArgs() {
        class Foo {

            @Argument(defaultValue = "string", description = "String desc")
            private String stringVal;

            @Argument(defaultValue = "true", description = "Boolean desc")
            private Boolean booleanVal;

            @Argument(defaultValue = "1", description = "Byte desc")
            private Byte byteVal;

            @Argument(defaultValue = "a", description = "Character desc")
            private Character characterVal;

            @Argument(defaultValue = "1", description = "Short desc")
            private Short shortVal;

            @Argument(defaultValue = "1", description = "Integer desc")
            private Integer integerVal;

            @Argument(defaultValue = "1", description = "Long desc")
            private Long longVal;

            @Argument(defaultValue = "1", description = "Float desc")
            private Float floatVal;

            @Argument(defaultValue = "1", description = "Double desc")
            private Double doubleVal;

            @Argument(defaultValue = "1", description = "BigDecimal desc")
            private BigDecimal bigDecimalVal;

            @Argument(defaultValue = "1", description = "BigInteger desc")
            private BigInteger bigIntegerVal;
        }
        Foo foo = new Foo();
        ArgumentTool.process(foo, null);
        Assert.assertEquals(foo.stringVal, "string", "different stringVal");
        Assert.assertEquals(foo.booleanVal.booleanValue(), true, "different booleanVal");
        Assert.assertEquals(foo.byteVal.byteValue(), (byte) 1, "different byteVal");
        Assert.assertEquals(foo.characterVal.charValue(), 'a', "different characterVal");
        Assert.assertEquals(foo.shortVal.shortValue(), (short) 1, "different shortVal");
        Assert.assertEquals(foo.integerVal.intValue(), 1, "different integerVal");
        Assert.assertEquals(foo.longVal.longValue(), 1L, "different longVal");
        Assert.assertEquals(foo.floatVal.floatValue(), 1F, "different floatVal");
        Assert.assertEquals(foo.doubleVal.doubleValue(), 1D, "different doubleVal");
        Assert.assertEquals(foo.bigDecimalVal, new BigDecimal(1), "different bigDecimalVal");
        Assert.assertEquals(foo.bigIntegerVal, new BigInteger("1"), "different bigIntegerVal");
    }

    @Test(description = "simple - non-primitives - console", groups = { "simple", "non-primitives", "console" })
    public void consoleArgs() {
        class Foo {

            @Argument(defaultValue = "string", description = "String desc")
            private String stringVal;

            @Argument(defaultValue = "true", description = "Boolean desc")
            private Boolean booleanVal;

            @Argument(defaultValue = "1", description = "Byte desc")
            private Byte byteVal;

            @Argument(defaultValue = "a", description = "Character desc")
            private Character characterVal;

            @Argument(defaultValue = "1", description = "Short desc")
            private Short shortVal;

            @Argument(defaultValue = "1", description = "Integer desc")
            private Integer integerVal;

            @Argument(defaultValue = "1", description = "Long desc")
            private Long longVal;

            @Argument(defaultValue = "1", description = "Float desc")
            private Float floatVal;

            @Argument(defaultValue = "1", description = "Double desc")
            private Double doubleVal;

            @Argument(defaultValue = "1", description = "BigDecimal desc")
            private BigDecimal bigDecimalVal;

            @Argument(defaultValue = "1", description = "BigInteger desc")
            private BigInteger bigIntegerVal;
        }
        String[] args = new String[] { "-stringVal", "stringA", "-booleanVal", "true", "-byteVal", "2", "-characterVal", "b", "-shortVal", "2", "-integerVal", "2", "-longVal", "2", "-floatVal", "2", "-doubleVal", "2", "-bigDecimalVal", "2", "-bigIntegerVal", "2" };
        Foo foo = new Foo();
        ArgumentTool.process(foo, args);
        Assert.assertEquals(foo.stringVal, "stringA", "different stringVal");
        Assert.assertEquals(foo.booleanVal.booleanValue(), true, "different booleanVal");
        Assert.assertEquals(foo.byteVal.byteValue(), (byte) 2, "different byteVal");
        Assert.assertEquals(foo.characterVal.charValue(), 'b', "different characterVal");
        Assert.assertEquals(foo.shortVal.shortValue(), (short) 2, "different shortVal");
        Assert.assertEquals(foo.integerVal.intValue(), 2, "different integerVal");
        Assert.assertEquals(foo.longVal.longValue(), 2L, "different longVal");
        Assert.assertEquals(foo.floatVal.floatValue(), 2F, "different floatVal");
        Assert.assertEquals(foo.doubleVal.doubleValue(), 2D, "different doubleVal");
        Assert.assertEquals(foo.bigDecimalVal, new BigDecimal(2), "different bigDecimalVal");
        Assert.assertEquals(foo.bigIntegerVal, new BigInteger("2"), "different bigIntegerVal");
    }

    @Test(description = "simple - non-primitives - file", groups = { "simple", "non-primitives", "file" })
    public void fileArgs() {
        @Argumentable(filename = "src/test/simpleObjects.txt")
        class Foo {

            @Argument(alias = "string", defaultValue = "string", description = "String desc")
            private String stringVal;

            @Argument(defaultValue = "true", description = "Boolean desc")
            private Boolean booleanVal;

            @Argument(defaultValue = "1", description = "Byte desc")
            private Byte byteVal;

            @Argument(defaultValue = "a", description = "Character desc")
            private Character characterVal;

            @Argument(defaultValue = "1", description = "Short desc")
            private Short shortVal;

            @Argument(defaultValue = "1", description = "Integer desc")
            private Integer integerVal;

            @Argument(defaultValue = "1", description = "Long desc")
            private Long longVal;

            @Argument(defaultValue = "1", description = "Float desc")
            private Float floatVal;

            @Argument(defaultValue = "1", description = "Double desc")
            private Double doubleVal;

            @Argument(defaultValue = "1", description = "BigDecimal desc")
            private BigDecimal bigDecimalVal;

            @Argument(defaultValue = "1", description = "BigInteger desc")
            private BigInteger bigIntegerVal;
        }
        Foo foo = new Foo();
        ArgumentTool.process(foo, null);
        Assert.assertEquals(foo.stringVal, "stringA", "different stringVal");
        Assert.assertEquals(foo.booleanVal.booleanValue(), true, "different booleanVal");
        Assert.assertEquals(foo.byteVal.byteValue(), (byte) 2, "different byteVal");
        Assert.assertEquals(foo.characterVal.charValue(), 'b', "different characterVal");
        Assert.assertEquals(foo.shortVal.shortValue(), (short) 2, "different shortVal");
        Assert.assertEquals(foo.integerVal.intValue(), 2, "different integerVal");
        Assert.assertEquals(foo.longVal.longValue(), 2L, "different longVal");
        Assert.assertEquals(foo.floatVal.floatValue(), 2F, "different floatVal");
        Assert.assertEquals(foo.doubleVal.doubleValue(), 2D, "different doubleVal");
        Assert.assertEquals(foo.bigDecimalVal, new BigDecimal(2), "different bigDecimalVal");
        Assert.assertEquals(foo.bigIntegerVal, new BigInteger("2"), "different bigIntegerVal");
    }
}
