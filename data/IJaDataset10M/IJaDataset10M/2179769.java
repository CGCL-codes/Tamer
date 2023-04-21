package Population.Individuals.Phenotype.TGA.explicitFunction;

/**
 *
 * @author 郝国生  HAO Guo-Sheng, HAO Guo Sheng, HAO GuoSheng
 */
public class F1 extends BaseFitnessFXScalar {

    public F1() {
        super();
    }

    @Override
    public float getFitness(float[] x) {
        for (int i = 0; i < x.length; i++) {
            this.fitness += Math.pow(x[i], 2);
        }
        this.fitness = -this.fitness;
        return this.fitness;
    }
}
