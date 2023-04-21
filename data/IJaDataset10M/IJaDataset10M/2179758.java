package cruise.umple.implementation;

import org.junit.*;

public class ImmutableUnidirectionalMStarTest extends TemplateTest {

    @Test
    public void Aware() {
        assertUmpleTemplateFor("ImmutableUnidirectionalMStarTest.ump", languagePath + "/ImmutableUnidirectionalMStarTest." + languagePath + ".txt", "Student");
    }

    @Test
    public void Unaware() {
        assertUmpleTemplateFor("ImmutableUnidirectionalMStarTest.ump", languagePath + "/ImmutableUnidirectionalTests_Unaware." + languagePath + ".txt", "Mentor");
    }
}
