package edu.rice.cs.javalanglevels;

import edu.rice.cs.javalanglevels.tree.*;
import edu.rice.cs.javalanglevels.parser.*;
import java.util.*;
import java.io.*;
import junit.framework.TestCase;

public class BodyBodyElementaryVisitor extends ElementaryVisitor {

    /**The MethodData of this method. */
    private BodyData _bodyData;

    public BodyBodyElementaryVisitor(BodyData bodyData, File file, String packageName, LinkedList<String> importedFiles, LinkedList<String> importedPackages, LinkedList<String> classDefsInThisFile, Hashtable<String, Pair<SourceInfo, LanguageLevelVisitor>> continuations) {
        super(file, packageName, importedFiles, importedPackages, classDefsInThisFile, continuations);
        _bodyData = bodyData;
    }

    public void forMethodDefDoFirst(MethodDef that) {
        _addError("Methods definitions cannot appear within the body of another method or block.", that);
    }

    public void forInstanceInitializer(InstanceInitializer that) {
        forBlock(that.getCode());
    }

    public void forBlock(Block that) {
        forBlockDoFirst(that);
        if (_checkError()) {
            return;
        }
        BlockData bd = new BlockData(_bodyData);
        _bodyData.addBlock(bd);
        that.getStatements().visit(new BodyBodyElementaryVisitor(bd, _file, _package, _importedFiles, _importedPackages, _classNamesInThisFile, continuations));
    }

    public void forVariableDeclarationOnly(VariableDeclaration that) {
        if (!_bodyData.addFinalVars(_variableDeclaration2VariableData(that, _bodyData))) {
            _addAndIgnoreError("You cannot have two variables with the same name.", that);
        }
    }

    /**
   * Call the super method to convert these to a VariableData array, then make sure that
   * each VariableData gets set to be final, as required at the Elementary level.
   * @param enclosingData  The Data which contains the variables
   */
    protected VariableData[] _variableDeclaration2VariableData(VariableDeclaration vd, Data enclosingData) {
        VariableData[] vds = llVariableDeclaration2VariableData(vd, enclosingData);
        for (int i = 0; i < vds.length; i++) {
            if (vds[i].getMav().getModifiers().length > 0) {
                StringBuffer s = new StringBuffer("the keyword(s) ");
                String[] modifiers = vds[i].getMav().getModifiers();
                for (int j = 0; j < modifiers.length; j++) {
                    s.append("\"" + modifiers[j] + "\" ");
                }
                _addAndIgnoreError("You cannot use " + s.toString() + "to declare a local variable at the Elementary level", vd);
            }
            vds[i].setFinal();
        }
        return vds;
    }

    /**
    * Test most of the methods declared above right here:
    */
    public static class BodyBodyElementaryVisitorTest extends TestCase {

        private BodyBodyElementaryVisitor _bbv;

        private SymbolData _sd1;

        private MethodData _md1;

        private ModifiersAndVisibility _publicMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[] { "public" });

        private ModifiersAndVisibility _protectedMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[] { "protected" });

        private ModifiersAndVisibility _privateMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[] { "private" });

        private ModifiersAndVisibility _packageMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[0]);

        private ModifiersAndVisibility _abstractMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[] { "abstract" });

        private ModifiersAndVisibility _finalMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[] { "final" });

        private ModifiersAndVisibility _finalPrivateMav = new ModifiersAndVisibility(JExprParser.NO_SOURCE_INFO, new String[] { "final", "private" });

        public BodyBodyElementaryVisitorTest() {
            this("");
        }

        public BodyBodyElementaryVisitorTest(String name) {
            super(name);
        }

        public void setUp() {
            _sd1 = new SymbolData("i.like.monkey");
            _md1 = new MethodData("methodName", _publicMav, new TypeParameter[0], SymbolData.INT_TYPE, new VariableData[0], new String[0], _sd1, null);
            errors = new LinkedList<Pair<String, JExpressionIF>>();
            symbolTable = new Symboltable();
            visitedFiles = new LinkedList<Pair<LanguageLevelVisitor, edu.rice.cs.javalanglevels.tree.SourceFile>>();
            _hierarchy = new Hashtable<String, TypeDefBase>();
            _classesToBeParsed = new Hashtable<String, Pair<TypeDefBase, LanguageLevelVisitor>>();
            _bbv = new BodyBodyElementaryVisitor(_md1, new File(""), "", new LinkedList<String>(), new LinkedList<String>(), new LinkedList<String>(), new Hashtable<String, Pair<SourceInfo, LanguageLevelVisitor>>());
            _bbv.continuations = new Hashtable<String, Pair<SourceInfo, LanguageLevelVisitor>>();
            _bbv._resetNonStaticFields();
            _bbv._importedPackages.addFirst("java.lang");
            _errorAdded = false;
        }

        public void testForMethodDefDoFirst() {
            ConcreteMethodDef cmd = new ConcreteMethodDef(JExprParser.NO_SOURCE_INFO, _packageMav, new TypeParameter[0], new PrimitiveType(JExprParser.NO_SOURCE_INFO, "int"), new Word(JExprParser.NO_SOURCE_INFO, "methodName"), new FormalParameter[0], new ReferenceType[0], new BracedBody(JExprParser.NO_SOURCE_INFO, new BodyItemI[0]));
            cmd.visit(_bbv);
            assertEquals("There should be one error.", 1, errors.size());
            assertEquals("The error message should be correct.", "Methods definitions cannot appear within the body of another method or block.", errors.get(0).getFirst());
        }

        public void testForVariableDeclarationOnly() {
            VariableDeclaration vdecl = new VariableDeclaration(JExprParser.NO_SOURCE_INFO, _packageMav, new VariableDeclarator[] { new UninitializedVariableDeclarator(JExprParser.NO_SOURCE_INFO, new PrimitiveType(JExprParser.NO_SOURCE_INFO, "double"), new Word(JExprParser.NO_SOURCE_INFO, "field1")), new UninitializedVariableDeclarator(JExprParser.NO_SOURCE_INFO, new PrimitiveType(JExprParser.NO_SOURCE_INFO, "boolean"), new Word(JExprParser.NO_SOURCE_INFO, "field2")) });
            VariableData vd1 = new VariableData("field1", _finalMav, SymbolData.DOUBLE_TYPE, false, _bbv._bodyData);
            VariableData vd2 = new VariableData("field2", _finalMav, SymbolData.BOOLEAN_TYPE, false, _bbv._bodyData);
            vdecl.visit(_bbv);
            assertEquals("There should not be any errors.", 0, errors.size());
            assertTrue("field1 was added.", _md1.getVars().contains(vd1));
            assertTrue("field2 was added.", _md1.getVars().contains(vd2));
            VariableDeclaration vdecl2 = new VariableDeclaration(JExprParser.NO_SOURCE_INFO, _packageMav, new VariableDeclarator[] { new UninitializedVariableDeclarator(JExprParser.NO_SOURCE_INFO, new PrimitiveType(JExprParser.NO_SOURCE_INFO, "double"), new Word(JExprParser.NO_SOURCE_INFO, "field3")), new UninitializedVariableDeclarator(JExprParser.NO_SOURCE_INFO, new PrimitiveType(JExprParser.NO_SOURCE_INFO, "int"), new Word(JExprParser.NO_SOURCE_INFO, "field3")) });
            VariableData vd3 = new VariableData("field3", _finalMav, SymbolData.DOUBLE_TYPE, false, _bbv._bodyData);
            vdecl2.visit(_bbv);
            assertEquals("There should be one error.", 1, errors.size());
            assertEquals("The error message should be correct", "You cannot have two variables with the same name.", errors.get(0).getFirst());
            assertTrue("field3 was added.", _md1.getVars().contains(vd3));
        }
    }
}
