package br.ufmg.dcc.vod.remoteworkers.processor;

public class NotIdleException extends Exception {

    public NotIdleException() {
        super("not idle");
    }
}
