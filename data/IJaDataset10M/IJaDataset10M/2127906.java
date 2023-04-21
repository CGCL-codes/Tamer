package es.gavab.jmh.approx.constructive;

import java.util.List;
import es.gavab.jmh.Instance;
import es.gavab.jmh.Solution;
import es.gavab.jmh.util.Descriptive;
import es.gavab.jmh.util.Properties;

public interface Constructive<S extends Solution<I>, I extends Instance> extends Descriptive {

    public void initSolutionCreation();

    public void initSolutionCreationByNum(int numSolutions);

    public void initSolutionCreationByTime(long millis);

    public S createSolution();

    public boolean isDeterminist();

    public List<S> createSolutions(int numSolutions);

    public List<S> createSolutionsInTime(long millis);

    public I getInstance();

    public void setInstance(I instance);

    public void removeInstance();

    public Properties getProperties();
}
