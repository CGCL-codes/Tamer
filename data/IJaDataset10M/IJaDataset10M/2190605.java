package org.swfparser.tests;

import java.io.File;

public class ASBatchPrinter extends ASBatchDumper {

    @Override
    protected void writeSWFActionScript(File swfFile) {
    }

    public static void main(String[] args) {
        new ASBatchPrinter().start();
    }
}
