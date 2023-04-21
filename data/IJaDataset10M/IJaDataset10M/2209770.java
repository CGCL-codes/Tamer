package cruise.umple.implementation.cpp;

import org.junit.*;
import cruise.umple.implementation.*;
import cruise.umple.util.SampleFileWriter;

public class CppClassTemplateTest extends ClassTemplateTest {

    @Before
    public void setUp() {
        super.setUp();
        language = "Cpp";
        languagePath = "cpp";
    }

    @After
    public void tearDown() {
        super.tearDown();
        SampleFileWriter.destroy(pathToInput + "/cpp/example");
    }

    @Test
    public void ClassMethods() {
        assertUmpleTemplateFor("ClassTemplateTest_Methods.ump", languagePath + "/ClassTemplateTest_Methods." + languagePath + ".txt", "CodeTranslator");
    }

    @Test
    public void ClassImplementedMethods() {
        assertUmpleTemplateFor("ClassTemplateTest_ImplementedMethods.ump", languagePath + "/ClassTemplateTest_ImplementedMethods." + languagePath + ".txt", "CodeTranslator");
    }

    @Test
    public void Cpp() {
        language = null;
        assertUmpleTemplateFor("cpp/ClassTemplateTest_Cpp.ump", "cpp/ClassTemplateTest_Java.cpp.txt", "Mentor");
    }

    @Test
    public void Generated() {
        assertUmpleTemplateFor("cpp/ClassTemplateTest_Generated.ump", "cpp/ClassTemplateTest_Generated.cpp.txt", "Mentor");
    }

    @Test
    public void ImportAssociations() {
        assertUmpleTemplateFor("cpp/ClassTemplateTest_Import.ump", "cpp/ClassTemplateTest_Import.cpp.txt", "Mentor");
        assertUmpleTemplateFor("cpp/ClassTemplateTest_Import.ump", "cpp/ClassTemplateTest_Import2.cpp.txt", "Student");
        assertUmpleTemplateFor("cpp/ClassTemplateTest_Import.ump", "cpp/ClassTemplateTest_Import3.cpp.txt", "Course");
    }
}
