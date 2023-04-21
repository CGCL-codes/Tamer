package mirrormonkey.util.annotations.parsing.classlevel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import junit.framework.Assert;
import mirrormonkey.util.annotations.control.DefinePreset;
import mirrormonkey.util.annotations.control.IRClass;
import mirrormonkey.util.annotations.parsing.AnnotationParser;
import mirrormonkey.util.annotations.parsing.ClassIR;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Contains test cases concerning the definition, parsing and collection of
 * preset annotations for classes.
 * 
 * @author Philipp Christian Loewner
 * 
 */
public class PresetClassTest {

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface CustomAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface CustomAnnotation2 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface PresetAnnotation {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface PresetAnnotation2 {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface PresetBoth {
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface TwoLevelPreset {
    }

    public static class PresetDefinitionContainer {

        @DefinePreset(PresetAnnotation.class)
        @CustomAnnotation
        public class PresetDefined {
        }

        @DefinePreset(PresetAnnotation2.class)
        @CustomAnnotation2
        public class PresetDefined2 {
        }

        @DefinePreset(PresetBoth.class)
        @CustomAnnotation
        @CustomAnnotation2
        public class PresetDefined3 {
        }

        @DefinePreset(TwoLevelPreset.class)
        @CustomAnnotation2
        @PresetAnnotation
        public class PresetDefined4 {
        }
    }

    public static AnnotationParser parser;

    @BeforeClass
    public static void setUpBeforeClass() {
        parser = new AnnotationParser();
        parser.parsePresetClass(PresetDefinitionContainer.class);
    }

    /**
	 * Tests if a single preset on a class will be collected.
	 */
    @Test
    public void testParsePreset() {
        @IRClass(startCollect = CustomAnnotation.class)
        @PresetAnnotation
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation.class));
    }

    /**
	 * Tests if the preset is not parsed if it is not requested.
	 */
    @Test
    public void testNoDeclareRejectPreset() {
        @IRClass(startCollect = CustomAnnotation.class)
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNull(ir.getCollectedAnnotation(CustomAnnotation.class));
    }

    /**
	 * Tests if the preset is parsed but annotations that are not marked to be
	 * collected aren't.
	 */
    @Test
    public void testNoCollectReject() {
        @PresetAnnotation
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNull(ir.getCollectedAnnotation(CustomAnnotation.class));
    }

    /**
	 * Tests if single preset annotations that were defined for different preset
	 * annotation classes will be collected.
	 */
    @Test
    public void testMultiPresets() {
        @IRClass(startCollect = { CustomAnnotation.class, CustomAnnotation2.class })
        @PresetAnnotation
        @PresetAnnotation2
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation.class));
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation2.class));
    }

    /**
	 * Tests if multiple annotations can be defined for a single preset class.
	 */
    @Test
    public void testMultiPerPreset() {
        @IRClass(startCollect = { CustomAnnotation.class, CustomAnnotation2.class })
        @PresetBoth
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation.class));
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation2.class));
    }

    /**
	 * Tests if preset annotations themselves can be collected.
	 */
    @Test
    public void testCollectPreset() {
        @IRClass(startCollect = PresetAnnotation.class)
        @PresetAnnotation
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNotNull(ir.getCollectedAnnotation(PresetAnnotation.class));
    }

    /**
	 * Tests if it is possible to use preset annotations on preset definitions.
	 */
    @Test
    public void testMultiLevelPreset() {
        @IRClass(startCollect = { CustomAnnotation.class, CustomAnnotation2.class })
        @TwoLevelPreset
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation2.class));
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation.class));
    }

    @Retention(RetentionPolicy.RUNTIME)
    public static @interface PresetAnnotation3 {
    }

    public static class AnotherPresetDefinitionClass {

        @DefinePreset(PresetAnnotation3.class)
        @CustomAnnotation2
        class CustomDefinition {
        }
    }

    /**
	 * Tests if it is possible to parse a second preset definition class without
	 * losing the data from the first one.
	 */
    @Test
    public void testMultiDefinitionClasses() {
        parser.parsePresetClass(AnotherPresetDefinitionClass.class);
        @IRClass(startCollect = { CustomAnnotation.class, CustomAnnotation2.class })
        @PresetAnnotation
        @PresetAnnotation3
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation.class));
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation2.class));
        parser.parsePresetClass(PresetDefinitionContainer.class);
    }

    public static class OverrideFirst {

        @DefinePreset(PresetAnnotation.class)
        @CustomAnnotation2
        class CustomDefinition {
        }
    }

    /**
	 * Tests if it is possible to override presets when parsing another class
	 * containing a preset for the same preset annotation.
	 */
    @Test
    public void testMultiDefinitionClassOverride() {
        parser.parsePresetClass(OverrideFirst.class);
        @IRClass(startCollect = { CustomAnnotation.class, CustomAnnotation2.class })
        @PresetAnnotation
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertNull(ir.getCollectedAnnotation(CustomAnnotation.class));
        Assert.assertNotNull(ir.getCollectedAnnotation(CustomAnnotation2.class));
        parser.parsePresetClass(PresetDefinitionContainer.class);
    }

    public static class ExtendingClassIR extends ClassIR {

        public ExtendingClassIR(AnnotationParser parser, Class<?> forClass) {
            super(parser, forClass);
        }

        public ExtendingClassIR(ClassIR previous, Class<?> forClass) {
            super(previous, forClass);
        }
    }

    public static class ExtendIRPreset {

        @DefinePreset(PresetAnnotation.class)
        @IRClass(value = ExtendingClassIR.class)
        public class CustomDefinition {
        }
    }

    /**
	 * Tests if it is possible to use <tt>IRClass</tt> in preset definitions to
	 * replace the <tt>ClassIR</tt>.
	 */
    @Test
    public void testPresetOverrideIRClass() {
        parser.parsePresetClass(ExtendIRPreset.class);
        @PresetAnnotation
        class Declare {
        }
        ClassIR ir = parser.parseClass(Declare.class, null);
        Assert.assertEquals(ExtendingClassIR.class, ir.getClass());
        parser.parsePresetClass(PresetDefinitionContainer.class);
    }
}
