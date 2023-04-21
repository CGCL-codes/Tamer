package clear.train;

import java.io.PrintStream;
import clear.model.AbstractModel;
import clear.train.algorithm.IAlgorithm;
import clear.train.kernel.AbstractKernel;

/**
 * Abstract trainer.
 * @author Jinho D. Choi
 * <b>Last update:</b> 11/8/2010
 */
public abstract class AbstractTrainer {

    public static final byte ST_BINARY = 0;

    public static final byte ST_ONE_VS_ALL = 1;

    protected String s_modelFile;

    protected PrintStream f_out;

    protected IAlgorithm a_algorithm;

    protected AbstractKernel k_kernel;

    protected int i_numThreads;

    public AbstractTrainer(String modelFile, IAlgorithm algorithm, AbstractKernel kernel, int numThreads) {
        init(modelFile, null, algorithm, kernel, numThreads);
    }

    public AbstractTrainer(PrintStream fout, IAlgorithm algorithm, AbstractKernel kernel, int numThreads) {
        init(null, fout, algorithm, kernel, numThreads);
    }

    protected void init(String modelFile, PrintStream fout, IAlgorithm algorithm, AbstractKernel kernel, int numThreads) {
        s_modelFile = modelFile;
        f_out = fout;
        a_algorithm = algorithm;
        k_kernel = kernel;
        i_numThreads = numThreads;
        initModel();
        train();
    }

    protected abstract void initModel();

    protected abstract void train();

    public abstract AbstractModel getModel();
}
