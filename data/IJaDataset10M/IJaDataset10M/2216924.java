package test.egu.plugin.testSource;

import egu.plugin.testSource.DataTestFileSource;
import egu.plugin.testSource.interfaces.IFixture;
import egu.plugin.testSource.interfaces.ITestClassStructure;
import egu.plugin.util.interfaces.IFileBuilder;
import egu.plugin.util.interfaces.IVectorInclude;

public class TestFileSourceBuilder {

    private DataTestFileSource dataTestFileSource;

    public TestFileSourceBuilder() {
        dataTestFileSource = new DataTestFileSource();
    }

    public TestFileSourceBuilder(TestFileSourceBuilder orig) {
        dataTestFileSource = orig.dataTestFileSource;
    }

    public DataTestFileSource build() {
        return dataTestFileSource;
    }

    public TestFileSourceBuilder with(IVectorInclude mockClassInclude) {
        dataTestFileSource.mockClassInclude = mockClassInclude;
        return this;
    }

    public TestFileSourceBuilder with(IFileBuilder fileClassBuilder) {
        dataTestFileSource.fileClassBuilder = fileClassBuilder;
        return this;
    }

    public TestFileSourceBuilder with(IFixture emptyFixture) {
        dataTestFileSource.emptyFixture = emptyFixture;
        return this;
    }

    public TestFileSourceBuilder with(ITestClassStructure classOfTest) {
        dataTestFileSource.classOfTest = classOfTest;
        return this;
    }
}
