package net.innig.macker.rule;

import net.innig.macker.structure.ClassInfo;

public interface Pattern {

    public static final Pattern ALL = new Pattern() {

        public boolean matches(EvaluationContext context, ClassInfo classInfo) {
            return true;
        }

        public String toString() {
            return "<all>";
        }
    };

    public static final Pattern NONE = new Pattern() {

        public boolean matches(EvaluationContext context, ClassInfo classInfo) {
            return false;
        }

        public String toString() {
            return "<none>";
        }
    };

    public boolean matches(EvaluationContext context, ClassInfo classInfo) throws RulesException;
}
