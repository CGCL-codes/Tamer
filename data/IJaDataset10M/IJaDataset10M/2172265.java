package org.cheetah.core.language;

public interface Language {

    CompiledExpression compile(String expression) throws Exception;
}
