package org.jcvi.glk.elvira.report.success;

/**
 * <code>PcrProbablitySuccess</code> uses nonlinear
 * algrebra to compute
 * the Probabilities that a PCR run was successful.
 * The formulas used are:
 * <pre>
 *P(seq F+R+) = P(PCR+) * P(seq F+) * P(seq R+)
   P(seq F+R-) = P(PCR+) * P(seq F+) * (1 - P(seq R+))
   P(seq F-R+) = P(PCR+) * (1 - P(seq F+)) * P(seq R+)
   P(seq F-R-) = (1 - P(PCR+)) + [P(PCR+) * (1 - P(seq F+))  * (1 - P(seq R+))]
 * </pre>
 *  where <code>+</code> is success and <code>-</code> is failure.
 *
 * @author jsitz
 * @author dkatzel
 */
public interface PcrProbablitySuccess {

    Double getProbabilityOfPcrSuccess();

    Double getProbabilityOfForwardSequenceSuccess();

    Double getProbabilityOfReverseSequenceSuccess();

    Double getProbabilityOfPcrFailure();
}
