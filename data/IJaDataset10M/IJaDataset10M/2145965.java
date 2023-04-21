package egu.plugin.testSource;

import egu.plugin.testSource.interfaces.IFixture;
import egu.plugin.testSource.interfaces.ITestClassStructure;
import egu.plugin.util.interfaces.IFileBuilder;
import egu.plugin.util.interfaces.IVectorInclude;

public class DataTestFileSource {

    public IVectorInclude mockClassInclude;

    public IFileBuilder fileClassBuilder;

    public IFixture emptyFixture;

    public ITestClassStructure classOfTest;
}
