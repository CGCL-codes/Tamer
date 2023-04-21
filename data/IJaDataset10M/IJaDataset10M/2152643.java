package br.ufmg.dcc.vod.remoteworkers.scheduler;

public class ProcessorDiedException extends Exception {

    public ProcessorDiedException() {
        super("processor died");
    }
}
