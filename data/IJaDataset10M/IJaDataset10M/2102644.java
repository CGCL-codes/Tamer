package org.openscience.cdk.test.modulesuites;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.openscience.cdk.test.ReactionCoverageTest;
import org.openscience.cdk.test.reaction.type.BreakingBondReactionTest;
import org.openscience.cdk.test.reaction.type.CarbonylEliminationReactionTest;
import org.openscience.cdk.test.reaction.type.CleavageBondMultiReactionTest;
import org.openscience.cdk.test.reaction.type.CleavageBondReactionTest;
import org.openscience.cdk.test.reaction.type.DisplacementChargeFromAcceptorReactionTest;
import org.openscience.cdk.test.reaction.type.DisplacementChargeFromDonorReactionTest;
import org.openscience.cdk.test.reaction.type.ElectronImpactNBEReactionTest;
import org.openscience.cdk.test.reaction.type.ElectronImpactPDBReactionTest;
import org.openscience.cdk.test.reaction.type.HydrogenRearrangementDeltaReactionTest;
import org.openscience.cdk.test.reaction.type.HydrogenRearrangementGammaReactionTest;
import org.openscience.cdk.test.reaction.type.HyperconjugationReactionTest;
import org.openscience.cdk.test.reaction.type.RadicalSiteInitiationHReactionTest;
import org.openscience.cdk.test.reaction.type.RadicalSiteInitiationReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementAnion1ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementAnion2ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementAnion3ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementCation1ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementCation2ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementCation3ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementRadical1ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementRadical2ReactionTest;
import org.openscience.cdk.test.reaction.type.RearrangementRadical3ReactionTest;
import org.openscience.cdk.test.tools.StructureResonanceGeneratorTest;

/**
 * TestSuite that runs all the tests for the CDK reaction module.
 *
 * @cdk.module  test-reaction
 * @cdk.depends log4j.jar
 * @cdk.depends junit.jar
 */
public class MreactionTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("CDK standard Tests");
        suite.addTest(ReactionCoverageTest.suite());
        suite.addTest(BreakingBondReactionTest.suite());
        suite.addTest(CarbonylEliminationReactionTest.suite());
        suite.addTest(CleavageBondReactionTest.suite());
        suite.addTest(CleavageBondMultiReactionTest.suite());
        suite.addTest(DisplacementChargeFromAcceptorReactionTest.suite());
        suite.addTest(DisplacementChargeFromDonorReactionTest.suite());
        suite.addTest(ElectronImpactPDBReactionTest.suite());
        suite.addTest(ElectronImpactNBEReactionTest.suite());
        suite.addTest(HydrogenRearrangementDeltaReactionTest.suite());
        suite.addTest(HydrogenRearrangementGammaReactionTest.suite());
        suite.addTest(HyperconjugationReactionTest.suite());
        suite.addTest(RadicalSiteInitiationHReactionTest.suite());
        suite.addTest(RadicalSiteInitiationReactionTest.suite());
        suite.addTest(RearrangementAnion1ReactionTest.suite());
        suite.addTest(RearrangementAnion2ReactionTest.suite());
        suite.addTest(RearrangementAnion3ReactionTest.suite());
        suite.addTest(RearrangementCation1ReactionTest.suite());
        suite.addTest(RearrangementCation2ReactionTest.suite());
        suite.addTest(RearrangementCation3ReactionTest.suite());
        suite.addTest(RearrangementRadical1ReactionTest.suite());
        suite.addTest(RearrangementRadical2ReactionTest.suite());
        suite.addTest(RearrangementRadical3ReactionTest.suite());
        suite.addTest(StructureResonanceGeneratorTest.suite());
        return suite;
    }
}
