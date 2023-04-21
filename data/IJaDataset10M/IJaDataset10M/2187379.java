package net.sf.brightside.luxurycruise.core.commandsp;

public interface Command<ExecutionResponse> {

    ExecutionResponse execute();
}
