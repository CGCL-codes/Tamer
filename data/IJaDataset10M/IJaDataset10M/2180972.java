package org.systemsbiology.apps.corragui.server.executor.corrastats;

public class CorraStatsConstants {

    public static final String EXE_NAME = "R";

    public static enum Input {

        SAMPLE_INFO("SampleInfo.tsv"), CONSTRASTFILE("ContrastFile.tsv");

        private String stringVal;

        private Input(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }

    public static enum Output {

        FC_PDF("_fc.pdf"), FC_PNG("_fc.png"), HC_PDF("_hc.pdf"), HC_PNG("_hc.png"), VOLCANO_PDF("_volcano.pdf"), VOLCANO_PNG("_volcano.png"), VOLCANO_TSV("_volcano.tsv");

        private String stringVal;

        private Output(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }

    public static enum Script {

        CORRASTATS("CorraStatistics.R"), FEATCOUNT("FeatureCount.R");

        private String stringVal;

        private Script(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }
}
