package org.ocallahan.chronomancer;

public interface IExportSink {

    public void beginContainer(String name, String header, String separator, String trailer);

    public void endContainer();

    public void writeLeaf(String name, String s);
}
