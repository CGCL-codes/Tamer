package com.googlecode.dni.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import com.googlecode.dni.test.internal.TestDniTypes;
import com.googlecode.dni.test.internal.TestScratchMemory;
import com.googlecode.dni.test.library.TestSymbolMethods;
import com.googlecode.dni.test.type.TestAutoFree;
import com.googlecode.dni.test.type.TestByteNativeStringBuffer;
import com.googlecode.dni.test.type.TestCharsets;
import com.googlecode.dni.test.type.TestIntNativeStringBuffer;
import com.googlecode.dni.test.type.TestShortNativeStringBuffer;
import com.googlecode.dni.test.type.structure.TestBufferStructures;
import com.googlecode.dni.test.type.structure.TestCustomNativeObjectStructure;
import com.googlecode.dni.test.type.structure.TestCyclicStructure;
import com.googlecode.dni.test.type.structure.TestDynamicSizedByteBufferStructures;
import com.googlecode.dni.test.type.structure.TestEnumStructures;
import com.googlecode.dni.test.type.structure.TestOpaqueStructures;
import com.googlecode.dni.test.type.structure.TestSimpleStructures;
import com.googlecode.dni.test.type.structure.TestSizedByteBufferStructures;
import com.googlecode.dni.test.type.structure.TestSizedIntBufferStructures;
import com.googlecode.dni.test.type.structure.TestStringBufferStructures;
import com.googlecode.dni.test.type.structure.TestStructureByValue;
import com.googlecode.dni.test.type.structure.TestStructureLists;
import com.googlecode.dni.test.type.structure.TestStructurePadding;

/**
 * All tests.
 *
 * @author Matthew Wilson
 */
@RunWith(Suite.class)
@SuiteClasses({ TestDniTypes.class, TestScratchMemory.class, TestAutoFree.class, TestByteNativeStringBuffer.class, TestCharsets.class, TestIntNativeStringBuffer.class, TestShortNativeStringBuffer.class, TestBufferStructures.class, TestCustomNativeObjectStructure.class, TestCyclicStructure.class, TestDynamicSizedByteBufferStructures.class, TestEnumStructures.class, TestOpaqueStructures.class, TestSimpleStructures.class, TestSizedByteBufferStructures.class, TestSizedIntBufferStructures.class, TestStringBufferStructures.class, TestStructureByValue.class, TestStructureLists.class, TestStructurePadding.class, TestSymbolMethods.class })
public final class AllTests {

    /**
     * Runs all tests
     *
     * @param args
     *            ignored
     */
    public static void main(final String args[]) {
        org.junit.runner.JUnitCore.main(AllTests.class.getName());
    }
}
