package it.unibo.cs.ndiff.test.phases;

import it.unibo.cs.ndiff.common.vdom.diffing.Dtree;
import it.unibo.cs.ndiff.core.Nconfig;
import it.unibo.cs.ndiff.core.alternatives.ThreadedBuildDtree;
import it.unibo.cs.ndiff.exceptions.ComputePhaseException;
import it.unibo.cs.ndiff.exceptions.InputFileException;

public class TestThreadedPerformance {

    private static final String fileA = "resources/testset_xml/various_xml/docj_1.xml";

    private static final String fileB = "resources/testset_xml/various_xml/docj_2.xml";

    public static void main(String[] args) throws InputFileException, ComputePhaseException {
        Nconfig config = new Nconfig();
        boolean ltrim = config.getBoolPhaseParam(Nconfig.Normalize, "ltrim");
        boolean rtrim = config.getBoolPhaseParam(Nconfig.Normalize, "rtrim");
        boolean collapse = config.getBoolPhaseParam(Nconfig.Normalize, "collapse");
        boolean emptynode = config.getBoolPhaseParam(Nconfig.Normalize, "emptynode");
        boolean commentNode = config.getBoolPhaseParam(Nconfig.Normalize, "commentnode");
        {
            new Dtree(fileA, ltrim, rtrim, collapse, emptynode, commentNode);
            new Dtree(fileB, ltrim, rtrim, collapse, emptynode, commentNode);
        }
        {
            (new ThreadedBuildDtree()).getDTree(fileA, fileB, ltrim, rtrim, collapse, emptynode, commentNode);
        }
    }
}
