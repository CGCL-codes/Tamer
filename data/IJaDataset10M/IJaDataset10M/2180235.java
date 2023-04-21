package br.usp.pulga;

public interface IterationRule {

    void addVariable(String s, double initialValue, String s1);

    String getCode();

    void addMethod(String method);

    Iteration buildIteration();
}
