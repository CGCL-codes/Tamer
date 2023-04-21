package com.maschinenstuermer.profiler.transform;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

@Test(groups = "unit")
public class MethodDescriptorConverterTest {

    @Test
    public void convert() {
        assertEquals(MethodDescriptionConverter.convert("(I)V"), "(int):void");
        assertEquals(MethodDescriptionConverter.convert("(Ljava/lang/String;)V"), "(java.lang.String):void");
        assertEquals(MethodDescriptionConverter.convert("(Ljava/lang/String;Ljava/lang/Object;)V"), "(java.lang.String,java.lang.Object):void");
        assertEquals(MethodDescriptionConverter.convert("([Ljava/lang/String;[[Ljava/lang/Object;)V"), "(java.lang.String[],java.lang.Object[][]):void");
        assertEquals(MethodDescriptionConverter.convert("([Ljava/lang/String;[[Ljava/lang/Object;)[I"), "(java.lang.String[],java.lang.Object[][]):int[]");
    }
}
