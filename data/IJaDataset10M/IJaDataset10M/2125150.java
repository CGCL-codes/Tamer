package bagaturchess.search.api;

public interface IEvalConfig {

    public boolean useLazyEval();

    public boolean useEvalCache();

    public String getEvaluatorFactoryClassName();

    public String getPawnsCacheFactoryClassName();
}
