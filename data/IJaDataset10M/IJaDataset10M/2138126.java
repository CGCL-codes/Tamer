package org.systemsbiology.apps.corragui.server.executor.specarray;

public class SpecArrayConstants {

    public static enum Step {

        MZXML2DAT, PEPLIST, PEPBOF2APMLCONVERTER, PEPMATCH, PEPARRAY, PEPARRAYTSV2APML, FC, DONE
    }

    public static enum Output {

        LOG("SpecArray.log");

        private String stringVal;

        private Output(String stringVal) {
            this.stringVal = stringVal;
        }

        public String toString() {
            return this.stringVal;
        }
    }
}
