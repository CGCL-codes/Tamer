package edu.ksu.cis.bnj.ver3.core;

public interface Evidence {

    public boolean inDomain(Domain D);

    public Value getEvidenceValue(int q);

    public String getName(Domain D);
}
