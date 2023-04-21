package org.vikamine.kernel.xpdl;

public abstract class MConstraintBuilder {

    public static IConstraint build(String value, String x) {
        EConstraintTyp typ = EConstraintTyp.valueOf(x);
        try {
            if (typ.equals(EConstraintTyp.maxK)) {
                return new MConstraintNumeric(Double.parseDouble(value), typ);
            } else if (typ.equals(EConstraintTyp.minQuality)) {
                return new MConstraintNumeric(Double.parseDouble(value), typ);
            } else if (typ.equals(EConstraintTyp.maxSelectors)) {
                if (value.contains(",")) {
                    return new MConstraintIntEnumeration(parseIntEnumeration(value), typ);
                } else {
                    return new MConstraintNumeric(Double.parseDouble(value), typ);
                }
            } else if (typ.equals(EConstraintTyp.minSubgroupSize)) {
                return new MConstraintNumeric(Double.parseDouble(value), typ);
            } else if (typ.equals(EConstraintTyp.minTPSupportRelative)) {
                return new MConstraintNumeric(Double.parseDouble(value), typ);
            } else if (typ.equals(EConstraintTyp.minTPSupportAbsolute)) {
                return new MConstraintNumeric(Double.parseDouble(value), typ);
            } else if (typ.equals(EConstraintTyp.relevantSubgroupsOnly)) {
                return new MConstraintBoolean(Boolean.parseBoolean(value), typ);
            } else if (typ.equals(EConstraintTyp.weightedCovering)) {
                return new MConstraintBoolean(Boolean.parseBoolean(value), typ);
            } else if (typ.equals(EConstraintTyp.ignoreDefaultValues)) {
                return new MConstraintBoolean(Boolean.parseBoolean(value), typ);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            throw new XMLException("ungueltiger Constrainwert");
        }
    }

    private static int[] parseIntEnumeration(String value) {
        String[] ints = value.split(",");
        int[] result = new int[ints.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Integer.parseInt(ints[i].trim());
        }
        return result;
    }
}
