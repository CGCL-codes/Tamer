package refactor;

public interface Factorable {

    void renameScenarioName(final String scenarioSourceNamePath, final String scenarioTargetNamePath) throws Exception;

    void renameTestName(final String testSourceNamePath, final String testTargetNamePath) throws Exception;

    void renameTestsParametersNames(final String testSourceNamePath, final String currentParameterName, final String newParameterName) throws Exception;

    void renameTestsBeanParametersNames(final String testSourceNamePath, final String beanSourceNamePath, final String currentParameterName, final String newParameterName) throws Exception;

    void renameMultipleScenariosSuiteExecutionScenarioName(final String multipleSuiteAbsolutePathFileName, final String scenarioSourceNamePath, final String scenarioTargetNamePath) throws Exception;
}
