package exspiminator.base;

import java.util.*;

/** The class responsible for creating an InstructionSet corresponding to the PowerPC instruction set
including all the simplified mnemonics. This class would be redundant if we had a proper macro handler.

Note that this class is not intended to be subclassed or instantiated - its only external interface
is meant to be the <code>getInstructionSet</code> method.

The class introduces some new Field IDs required to support the simplified mnemonics. Not that these
are all negative to distinguish them from conventional field ids, and make it illegal to attempt to 
find the field length etc through the conventional InstructionSet interface - the fields *must* 
receive special handling in the assembler or an exception will occur.
@see PowerPCInstructionSet
*/
public abstract class SimplifiedPowerPCInstructionSet extends PowerPCInstructionSet {

    /** This field contains a value to be negated before being placed into the SIMM field.*/
    public static final int NEGSIMM = -1;

    /** This field contains a GPR index to be placed in the D, A, and B fields.*/
    public static final int DAB = -2;

    /** This field contains a GPR index to be placed in the A and B fields.*/
    public static final int AB = -3;

    /** This field contains a GPR index to be placed in the D and B fields.*/
    public static final int DB = -4;

    /** This field contains a value from which one is to be subtracted before insertion into the ME field.*/
    public static final int ME_PLUS_1 = -5;

    /** This field contains a value which is to be subtracted from 32 and then stored in the MB field.*/
    public static final int THIRTY_TWO_MINUS_MB = -6;

    /** This field contains a value which is to be added to the value of thirty-two minus the contents of 
	the MB field, and stored in the SH field.*/
    public static final int COMPLETE1 = -7;

    /** This field contains a value which is to be temporarily stored in ME, but is not the correct value for
	that field.*/
    public static final int TEMP_ME = -8;

    /** This field contains a value which is the MB field, and 32 minus SH, and which is to trigger 
	replacement of the value in the ME field by its current value plus this value minus 1.*/
    public static final int COMPLETE2 = -9;

    /** This field contains a value which is the MB field, and 32 minus (SH plus ME), and which is to 
	trigger replacement of the value in the ME field by its current value plus this value minus 1.*/
    public static final int COMPLETE3 = -10;

    /** This field contains a value which is to be subtracted from 32 and then inserted into the SH field.*/
    public static final int THIRTY_TWO_MINUS_SH = -11;

    /** This field contains a value which is to be put into the SH field and then subtacted from 31 and put
	into the ME field.*/
    public static final int SH_AND_31_MINUS_ME = -12;

    /** This field contains a value which is the MB field, and 32 minus SH.*/
    public static final int MB_AND_32_MINUS_SH = -13;

    /** This field contains a value which is to be subtacted from 31 and put into the ME field.*/
    public static final int THIRTY_ONE_MINUS_ME = -14;

    /** This field contains a value which is the SH field, to set MB to ME minus this, to clear ME and set
	it to 31 minus this value.*/
    public static final int COMPLETE4 = -15;

    /** This field contains a condition register bit to be placed in the CRBA and CRBB fields.*/
    public static final int CRBAB = -16;

    /** This field contains a condition register bit to be placed in the CRBD, CRBA, and CRBB fields.*/
    public static final int CRBDAB = -17;

    private static InstructionSet INSTANCE;

    public static InstructionSet getInstructionSet() {
        if (INSTANCE == null) {
            INSTANCE = createInstance();
        }
        return INSTANCE;
    }

    private static InstructionSet createInstance() {
        InstructionSet is = new InstructionSet(PowerPCInstructionSet.getInstructionSet());
        addSimplified(is.instructions);
        return is;
    }

    private static void addSimplified(Collection c) {
        putDotForm(c, "mr", new int[] { 2, PRIMARY, 31, EXTENDED, 444 << 1, A, DB });
        putDotForm(c, "not", new int[] { 2, PRIMARY, 31, EXTENDED, 124 << 1, A, DB });
        c.add(new InstructionType("nop", new int[] { 1, PRIMARY, 24 }));
        c.add(new InstructionType("cmpwi", new int[] { 1, PRIMARY, 11, A, SIMM }));
        c.add(new InstructionType("cmplwi", new int[] { 1, PRIMARY, 10, A, UIMM }));
        c.add(new InstructionType("cmpw", new int[] { 2, PRIMARY, 31, EXTENDED, 0 << 1, A, B }));
        c.add(new InstructionType("cmplw", new int[] { 2, PRIMARY, 31, EXTENDED, 32 << 1, A, B }));
        c.add(new InstructionType("li", new int[] { 1, PRIMARY, 14, D, SIMM }));
        c.add(new InstructionType("lis", new int[] { 1, PRIMARY, 15, D, SIMM }));
        c.add(new InstructionType("la", new int[] { 1, PRIMARY, 14, D, SIMM, A0 }));
        putDotForm(c, "sub", new int[] { 2, PRIMARY, 31, EXTENDED, 40 << 1, D, B, A });
        c.add(new InstructionType("subi", new int[] { 1, PRIMARY, 14, D, A, NEGSIMM }));
        c.add(new InstructionType("subis", new int[] { 1, PRIMARY, 15, D, A, NEGSIMM }));
        c.add(new InstructionType("crset", new int[] { 2, PRIMARY, 19, EXTENDED, 289 << 1, CRBDAB }));
        c.add(new InstructionType("crclr", new int[] { 2, PRIMARY, 19, EXTENDED, 193 << 1, CRBDAB }));
        c.add(new InstructionType("crmove", new int[] { 2, PRIMARY, 19, EXTENDED, 449 << 1, CRBD, CRBAB }));
        c.add(new InstructionType("crnot", new int[] { 2, PRIMARY, 19, EXTENDED, 33 << 1, CRBD, CRBAB }));
        c.add(new InstructionType("mtcr", new int[] { 3, PRIMARY, 31, EXTENDED, 144 << 1, CRM, 255, D }));
        c.add(new InstructionType("mtxer", new int[] { 3, PRIMARY, 31, EXTENDED, 467 << 1, SPR, 1 << 5, D }));
        c.add(new InstructionType("mtlr", new int[] { 3, PRIMARY, 31, EXTENDED, 467 << 1, SPR, 8 << 5, D }));
        c.add(new InstructionType("mtctr", new int[] { 3, PRIMARY, 31, EXTENDED, 467 << 1, SPR, 9 << 5, D }));
        c.add(new InstructionType("mfxer", new int[] { 3, PRIMARY, 31, EXTENDED, 339 << 1, SPR, 1 << 5, D }));
        c.add(new InstructionType("mflr", new int[] { 3, PRIMARY, 31, EXTENDED, 339 << 1, SPR, 8 << 5, D }));
        c.add(new InstructionType("mfctr", new int[] { 3, PRIMARY, 31, EXTENDED, 339 << 1, SPR, 9 << 5, D }));
        putDotForm(c, "extlwi", new int[] { 2, PRIMARY, 21, EXTENDED, 0, A, D, ME_PLUS_1, SH });
        putDotForm(c, "extrwi", new int[] { 3, PRIMARY, 21, EXTENDED, 0, ME, 31, A, D, THIRTY_TWO_MINUS_MB, COMPLETE1 });
        putDotForm(c, "inslwi", new int[] { 2, PRIMARY, 20, EXTENDED, 0, A, D, TEMP_ME, COMPLETE2 });
        putDotForm(c, "insrwi", new int[] { 2, PRIMARY, 20, EXTENDED, 0, A, D, TEMP_ME, COMPLETE3 });
        putDotForm(c, "rotlwi", new int[] { 3, PRIMARY, 21, EXTENDED, 0, ME, 31, A, D, SH });
        putDotForm(c, "rotrwi", new int[] { 3, PRIMARY, 21, EXTENDED, 0, ME, 31, A, D, THIRTY_TWO_MINUS_SH });
        putDotForm(c, "rotlw", new int[] { 2, PRIMARY, 23, EXTENDED, 0, ME, 31, A, D, B });
        putDotForm(c, "slwi", new int[] { 2, PRIMARY, 21, EXTENDED, 0, A, D, SH_AND_31_MINUS_ME });
        putDotForm(c, "srwi", new int[] { 3, PRIMARY, 21, EXTENDED, 0, ME, 31, A, D, MB_AND_32_MINUS_SH });
        putDotForm(c, "clrlwi", new int[] { 3, PRIMARY, 21, EXTENDED, 0, ME, 31, A, D, MB });
        putDotForm(c, "clrrwi", new int[] { 2, PRIMARY, 21, EXTENDED, 0, A, D, THIRTY_ONE_MINUS_ME });
        putDotForm(c, "clrlslwi", new int[] { 2, PRIMARY, 21, EXTENDED, 0, A, D, TEMP_ME, COMPLETE4 });
        putBranchForms(c, "blt", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 12, BI, 0, BD });
        putBranchForms(c, "bgt", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 12, BI, 1, BD });
        putBranchForms(c, "beq", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 12, BI, 2, BD });
        putBranchForms(c, "bso", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 12, BI, 3, BD });
        putBranchForms(c, "bnl", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 0, BD });
        putBranchForms(c, "bng", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 1, BD });
        putBranchForms(c, "bne", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 2, BD });
        putBranchForms(c, "bns", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 3, BD });
        putBranchForms(c, "bge", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 0, BD });
        putBranchForms(c, "ble", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 1, BD });
        putBranchForms(c, "bun", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 12, BI, 3, BD });
        putBranchForms(c, "bnu", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 4, BI, 3, BD });
        putBranchForms(c, "bt", new int[] { 3, PRIMARY, 16, BRANCH, 0, BO, 12, BI, BD });
        putBranchForms(c, "bf", new int[] { 3, PRIMARY, 16, BRANCH, 0, BO, 4, BI, BD });
        putBranchForms(c, "bdnz", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 16, BI, 0, BD });
        putBranchForms(c, "bdz", new int[] { 4, PRIMARY, 16, BRANCH, 0, BO, 18, BI, 0, BD });
        putBranchForms(c, "bdnzt", new int[] { 3, PRIMARY, 16, BRANCH, 0, BO, 8, BI, BD });
        putBranchForms(c, "bdnzf", new int[] { 3, PRIMARY, 16, BRANCH, 0, BO, 0, BI, BD });
        putBranchForms(c, "bdzt", new int[] { 3, PRIMARY, 16, BRANCH, 0, BO, 10, BI, BD });
        putBranchForms(c, "bdzf", new int[] { 3, PRIMARY, 16, BRANCH, 0, BO, 2, BI, BD });
        putLinkForms(c, "bltlr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 12, BI, 0 });
        putLinkForms(c, "bgtlr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 12, BI, 1 });
        putLinkForms(c, "beqlr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 12, BI, 2 });
        putLinkForms(c, "bsolr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 12, BI, 3 });
        putLinkForms(c, "bnllr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 0 });
        putLinkForms(c, "bnglr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 1 });
        putLinkForms(c, "bnelr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 2 });
        putLinkForms(c, "bnslr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 3 });
        putLinkForms(c, "bgelr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 0 });
        putLinkForms(c, "blelr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 1 });
        putLinkForms(c, "bunlr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 12, BI, 3 });
        putLinkForms(c, "bnulr", new int[] { 4, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI, 3 });
        putLinkForms(c, "btlr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 12, BI });
        putLinkForms(c, "bflr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 4, BI });
        putLinkForms(c, "blr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 20 });
        putLinkForms(c, "bdnzlr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 16 });
        putLinkForms(c, "bdzlr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 18 });
        putLinkForms(c, "bdnztlr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 8, BI });
        putLinkForms(c, "bdnzflr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 0, BI });
        putLinkForms(c, "bdztlr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 10, BI });
        putLinkForms(c, "bdzflr", new int[] { 3, PRIMARY, 19, EXTENDED, 16 << 1, BO, 2, BI });
        putLinkForms(c, "bltctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 12, BI, 0 });
        putLinkForms(c, "bgtctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 12, BI, 1 });
        putLinkForms(c, "beqctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 12, BI, 2 });
        putLinkForms(c, "bsoctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 12, BI, 3 });
        putLinkForms(c, "bnlctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 0 });
        putLinkForms(c, "bngctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 1 });
        putLinkForms(c, "bnectr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 2 });
        putLinkForms(c, "bnsctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 3 });
        putLinkForms(c, "bgectr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 0 });
        putLinkForms(c, "blectr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 1 });
        putLinkForms(c, "bunctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 12, BI, 3 });
        putLinkForms(c, "bnuctr", new int[] { 4, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI, 3 });
        putLinkForms(c, "btctr", new int[] { 3, PRIMARY, 19, EXTENDED, 528 << 1, BO, 12, BI });
        putLinkForms(c, "bfctr", new int[] { 3, PRIMARY, 19, EXTENDED, 528 << 1, BO, 4, BI });
        putLinkForms(c, "bctr", new int[] { 3, PRIMARY, 19, EXTENDED, 528 << 1, BO, 20 });
    }

    /** Returns a clone of the given array with the given element changed to the given value.*/
    protected static int[] cloneAndModify(int[] data, int index, int value) {
        int[] result = (int[]) data.clone();
        result[index] = value;
        return result;
    }

    /** Populates the collection with all the forms of a given branch.*/
    protected static void putBranchForms(Collection c, String mnemonic, int[] value) {
        c.add(new InstructionType(mnemonic, value));
        c.add(new InstructionType(mnemonic + "l", cloneAndModify(value, 4, 1)));
        c.add(new InstructionType(mnemonic + "a", cloneAndModify(value, 4, 2)));
        c.add(new InstructionType(mnemonic + "la", cloneAndModify(value, 4, 3)));
    }

    /** Populates the collection with the standard and link forms of a given branch.*/
    protected static void putLinkForms(Collection c, String mnemonic, int[] data) {
        c.add(new InstructionType(mnemonic, data));
        c.add(new InstructionType(mnemonic + "l", cloneAndModify(data, 4, data[4] + 1)));
    }
}
