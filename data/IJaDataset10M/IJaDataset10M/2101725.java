package nl.jamiecraane.patternexample;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;

/**
 * Fitness function for the pattern recognition.
 */
public class PatternFitnessFunction extends FitnessFunction {

    private static boolean[] targetColors;

    public PatternFitnessFunction(boolean[] targetColors) throws Exception {
        super();
        this.targetColors = targetColors;
    }

    /**
     * Compares the genes in the supplied subject, which represent black and white pixels,
     * with the target colors stored in the this.targetColors[]. The difference between
     * every pixel is stored and the square returned. Lower values mean less difference thus a
     * better fitness value.
     * @param a_subject
     * @return
     */
    @Override
    protected double evaluate(IChromosome a_subject) {
        double errors = 0;
        Gene[] genes = a_subject.getGenes();
        for (int i = 0; i < genes.length; i++) {
            Gene gene = genes[i];
            boolean allele = (Boolean) gene.getAllele();
            if (!allele == this.targetColors[i]) {
                errors += 1;
            }
        }
        return errors * errors;
    }
}
