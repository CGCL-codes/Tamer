package edu.cmu.ece.agora.core;

public abstract class AbstractAsyncWriter implements AsyncWriter {

    @Override
    public void print(String format, Object... args) {
        print(String.format(format, args));
    }

    @Override
    public void printLine(String line) {
        print(line + "\n");
    }
}
