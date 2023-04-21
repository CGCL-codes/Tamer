package policy;

public class PolicyFamily {

    private static int policyFamilyID = 0;

    private int inputDimensionSize;

    private int outputDimensionSize;

    private int policyCode;

    private int familyID;

    public PolicyFamily() {
        System.out.println("PolicyFamily: Replace with Exception!");
    }

    public PolicyFamily(int policyCode, int inputDimensionSize, int outputDimensionSize) {
        this.familyID = PolicyFamily.getPolicyFamilyCount();
        PolicyFamily.incPolicyFamilyID();
        this.policyCode = policyCode;
        this.inputDimensionSize = inputDimensionSize;
        this.outputDimensionSize = outputDimensionSize;
    }

    public Policy createPolicy() {
        if (policyCode == 0) {
            return this.createDimensionOrderedPolicy();
        } else return null;
    }

    public Policy createDimensionOrderedPolicy() {
        return new DimensionOrderedPolicy(this.inputDimensionSize, this.outputDimensionSize);
    }

    public int getFamilyID() {
        return this.familyID;
    }

    private static void incPolicyFamilyID() {
        PolicyFamily.policyFamilyID += 1;
    }

    public static int getPolicyFamilyCount() {
        return PolicyFamily.policyFamilyID;
    }
}
