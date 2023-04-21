package playground.yu.analysis.forMuc;

import playground.yu.analysis.Analysis;

/**
 * keep some general tools for Analysis for Zurich and respectively Kanton
 * Zurich
 * 
 * @author yu
 * 
 */
public interface Analysis4Muc extends Analysis {

    static final String MUNICH = "Munich", ONLY_MUNICH = "onlyMunich", RIDE = "ride";

    public enum ActTypeMuc implements ActType {

        unknown("unknown"), work("work"), education("education"), business("business"), shopping("shopping"), private_("private"), leisure("leisure"), sports("sports"), home("home"), friends("friends"), pickup("pickup"), with_adult("with adult"), other("other"), pvHome("pvHome"), pvWork("pvWork"), gvHome("gvHome");

        private final String actTypeName;

        public String getActTypeName() {
            return actTypeName;
        }

        ActTypeMuc(String actTypeName) {
            this.actTypeName = actTypeName;
        }
    }
}
