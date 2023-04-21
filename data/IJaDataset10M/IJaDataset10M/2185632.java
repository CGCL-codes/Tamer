package org.apache.harmony.unpack200.bytecode.forms;

import org.apache.harmony.unpack200.bytecode.ByteCode;
import org.apache.harmony.unpack200.bytecode.OperandManager;

public class LookupSwitchForm extends SwitchForm {

    public LookupSwitchForm(int opcode, String name) {
        super(opcode, name);
    }

    public LookupSwitchForm(int opcode, String name, int[] rewrite) {
        super(opcode, name, rewrite);
    }

    public void setByteCodeOperands(ByteCode byteCode, OperandManager operandManager, int codeLength) {
        final int case_count = operandManager.nextCaseCount();
        final int default_pc = operandManager.nextLabel();
        final int case_values[] = new int[case_count];
        for (int index = 0; index < case_count; index++) {
            case_values[index] = operandManager.nextCaseValues();
        }
        final int case_pcs[] = new int[case_count];
        for (int index = 0; index < case_count; index++) {
            case_pcs[index] = operandManager.nextLabel();
        }
        final int[] labelsArray = new int[case_count + 1];
        labelsArray[0] = default_pc;
        for (int index = 1; index < case_count + 1; index++) {
            labelsArray[index] = case_pcs[index - 1];
        }
        byteCode.setByteCodeTargets(labelsArray);
        final int padLength = 3 - (codeLength % 4);
        final int rewriteSize = 1 + padLength + 4 + 4 + (4 * case_values.length) + (4 * case_pcs.length);
        final int[] newRewrite = new int[rewriteSize];
        int rewriteIndex = 0;
        newRewrite[rewriteIndex++] = byteCode.getOpcode();
        for (int index = 0; index < padLength; index++) {
            newRewrite[rewriteIndex++] = 0;
        }
        newRewrite[rewriteIndex++] = -1;
        newRewrite[rewriteIndex++] = -1;
        newRewrite[rewriteIndex++] = -1;
        newRewrite[rewriteIndex++] = -1;
        final int npairsIndex = rewriteIndex;
        setRewrite4Bytes(case_values.length, npairsIndex, newRewrite);
        rewriteIndex += 4;
        for (int index = 0; index < case_values.length; index++) {
            setRewrite4Bytes(case_values[index], rewriteIndex, newRewrite);
            rewriteIndex += 4;
            newRewrite[rewriteIndex++] = -1;
            newRewrite[rewriteIndex++] = -1;
            newRewrite[rewriteIndex++] = -1;
            newRewrite[rewriteIndex++] = -1;
        }
        byteCode.setRewrite(newRewrite);
    }
}
