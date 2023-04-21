package com.ibm.JikesRVM;

/**
 * Generate inline machine instructions for special methods that cannot be implemented
 * in java bytecodes. These instructions are generated whenever we encounter an 
 * "invokestatic" bytecode that calls a method with a signature of 
 * the form "static native VM_Magic.xxx(...)".
 * 23 Jan 1998 Derek Lieber
 *
 * NOTE: when adding a new "methodName" to "generate()", be sure to also consider
 * how it affects the values on the stack and update "checkForActualCall()" accordingly.
 * If no call is actually generated, the map will reflect the status of the 
 * locals (including parameters) at the time of the call but nothing on the 
 * operand stack for the call site will be mapped.
 *
 * @author Janice Shepherd
 * @date 7 Jul 1998 
 */
class VM_MagicCompiler implements VM_BaselineConstants {

    static void generateInlineCode(VM_Compiler compiler, VM_Method methodToBeCalled) {
        VM.sysWrite("VM_MagicCompiler.java: no magic for " + methodToBeCalled + "\n");
        if (VM.VerifyAssertions) VM._assert(NOT_REACHED);
    }

    public static boolean checkForActualCall(VM_Method methodToBeCalled) {
        VM_Atom methodName = methodToBeCalled.getName();
        return methodName == VM_MagicNames.invokeMain || methodName == VM_MagicNames.invokeClassInitializer || methodName == VM_MagicNames.invokeMethodReturningVoid || methodName == VM_MagicNames.invokeMethodReturningInt || methodName == VM_MagicNames.invokeMethodReturningLong || methodName == VM_MagicNames.invokeMethodReturningFloat || methodName == VM_MagicNames.invokeMethodReturningDouble || methodName == VM_MagicNames.invokeMethodReturningObject;
    }
}
