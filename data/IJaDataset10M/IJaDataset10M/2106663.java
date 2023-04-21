package tm.clc.analysis;

import junit.framework.*;
import tm.cpp.analysis.Cpp_ScopedName;
import tm.interfaces.SourceCoords;

public class DeclarationSetTest extends TestCase {

    private Declaration dvar, dtype;

    private DeclarationSetMulti empty, singleV, singleT, multiple;

    public DeclarationSetTest() {
        super("DeclarationSetTest");
    }

    public DeclarationSetTest(String name) {
        super(name);
    }

    protected void setUp() {
        dvar = new Declaration(new LFlags(LFConst.VARIABLE), new Cpp_ScopedName("dud"));
        dvar.setDefinition(dvar);
        dtype = new Declaration(new LFlags(LFConst.CLASS), new Cpp_ScopedName("A"));
        dtype.setDefinition(new ScopeHolderTestImpl());
        empty = new DeclarationSetMulti();
        singleV = new DeclarationSetMulti();
        singleV.addElement(dvar);
        singleT = new DeclarationSetMulti();
        singleT.addElement(dtype);
        multiple = new DeclarationSetMulti();
        multiple.addElement(dvar);
        multiple.addElement(dtype);
    }

    public void testSingleMember() {
        assertTrue(empty.getSingleMember() == null);
        assertTrue(singleV.getSingleMember() == dvar);
        assertTrue(singleT.getSingleMember() == dtype);
        assertTrue(multiple.getSingleMember() == null);
    }

    public void testGetScopeHolder() {
        assertTrue(singleT.getScopeHolder() != null);
    }

    public void testEmpty() {
        assertTrue(!singleT.isEmpty());
        assertTrue(empty.isEmpty());
    }

    public void testAppend() {
        int origSize = multiple.size();
        DeclarationSetMulti multipleCopy = new DeclarationSetMulti();
        multipleCopy.append(multiple);
        assertTrue(multipleCopy.size() == origSize);
        multiple.append(multipleCopy);
        assertTrue(multiple.size() == 2 * origSize);
        multiple.append(singleT);
        assertTrue(multiple.size() == 2 * origSize + 1);
        assertTrue(multiple.lastElement() == dtype);
        assertTrue(multiple.lastElement() == singleT.getSingleMember());
    }
}
