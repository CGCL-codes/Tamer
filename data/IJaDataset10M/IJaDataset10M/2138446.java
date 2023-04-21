package org.rubypeople.rdt.refactoring.tests;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.rubypeople.rdt.refactoring.tests.classnodeprovider.TS_ClassNodeProvider;
import org.rubypeople.rdt.refactoring.tests.core.TS_Core;
import org.rubypeople.rdt.refactoring.tests.core.convertlocaltofield.TS_LocalToField;
import org.rubypeople.rdt.refactoring.tests.core.encapsulatefield.TS_EncapsulateField;
import org.rubypeople.rdt.refactoring.tests.core.extractconstant.TS_ExtractConstant;
import org.rubypeople.rdt.refactoring.tests.core.extractmethod.TS_ExtractMethod;
import org.rubypeople.rdt.refactoring.tests.core.formatsource.TS_FormatSource;
import org.rubypeople.rdt.refactoring.tests.core.generateaccessors.TS_GenerateAccessors;
import org.rubypeople.rdt.refactoring.tests.core.generateconstructor.TS_GenerateConstructor;
import org.rubypeople.rdt.refactoring.tests.core.inlineclass.TS_InlineClass;
import org.rubypeople.rdt.refactoring.tests.core.inlinelocal.TS_InlineLocal;
import org.rubypeople.rdt.refactoring.tests.core.inlinemethod.TS_InlineMethod;
import org.rubypeople.rdt.refactoring.tests.core.mergeclasspartsinfile.TS_MergeClassPartsInFile;
import org.rubypeople.rdt.refactoring.tests.core.mergewithexternalclassparts.TS_MergeWithExternalClassParts;
import org.rubypeople.rdt.refactoring.tests.core.movefield.TS_MoveField;
import org.rubypeople.rdt.refactoring.tests.core.movemethod.TS_MoveMethod;
import org.rubypeople.rdt.refactoring.tests.core.nodewrapper.TS_NodeWrapper;
import org.rubypeople.rdt.refactoring.tests.core.overridemethod.TS_OverrideMethod;
import org.rubypeople.rdt.refactoring.tests.core.pushdown.TS_PushDown;
import org.rubypeople.rdt.refactoring.tests.core.rename.TS_Rename;
import org.rubypeople.rdt.refactoring.tests.core.renameclass.TS_RenameClass;
import org.rubypeople.rdt.refactoring.tests.core.renamefield.TS_RenameField;
import org.rubypeople.rdt.refactoring.tests.core.renamelocal.TS_RenameLocal;
import org.rubypeople.rdt.refactoring.tests.core.renamemethod.TS_RenameMethod;
import org.rubypeople.rdt.refactoring.tests.core.renamemodule.TS_RenameModule;
import org.rubypeople.rdt.refactoring.tests.core.splitlocal.TS_SplitLocal;
import org.rubypeople.rdt.refactoring.tests.util.TS_Util;

public class TS_All {

    public static Test suite() {
        TestSuite suite = new TestSuite("All Refactoring Tests");
        suite.addTest(TS_GenerateAccessors.suite());
        suite.addTest(TS_GenerateConstructor.suite());
        suite.addTest(TS_OverrideMethod.suite());
        suite.addTest(TS_PushDown.suite());
        suite.addTest(TS_RenameLocal.suite());
        suite.addTest(TS_Util.suite());
        suite.addTest(TS_ClassNodeProvider.suite());
        suite.addTest(TS_FormatSource.suite());
        suite.addTest(TS_LocalToField.suite());
        suite.addTest(TS_Core.suite());
        suite.addTest(TS_ExtractMethod.suite());
        suite.addTest(TS_ExtractConstant.suite());
        suite.addTest(TS_MergeWithExternalClassParts.suite());
        suite.addTest(TS_MergeClassPartsInFile.suite());
        suite.addTest(TS_InlineLocal.suite());
        suite.addTest(TS_SplitLocal.suite());
        suite.addTest(TS_EncapsulateField.suite());
        suite.addTest(TS_InlineMethod.suite());
        suite.addTest(TS_RenameField.suite());
        suite.addTest(TS_RenameClass.suite());
        suite.addTest(TS_RenameMethod.suite());
        suite.addTest(TS_RenameModule.suite());
        suite.addTest(TS_InlineClass.suite());
        suite.addTest(TS_MoveMethod.suite());
        suite.addTest(TS_MoveField.suite());
        suite.addTest(TS_Rename.suite());
        suite.addTest(TS_NodeWrapper.suite());
        return suite;
    }
}
