package it.southdown.avana.alignment.io;

import it.southdown.avana.alignment.Sequence;

public abstract class SequenceSource {

    public abstract Sequence[] getSequences() throws SequenceSourceException;

    public abstract String getName();
}
