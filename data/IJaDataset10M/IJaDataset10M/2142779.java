package edu.udo.cs.ai.nemoz.lab.util;

import java.io.IOException;
import edu.udo.cs.yale.Experiment;
import edu.udo.cs.yale.tools.XMLException;

public class MemoryMethodTree implements MethodTree {

    public static MemoryMethodTree EMPTY = new MemoryMethodTree(null);

    private final Experiment experiment;

    public MemoryMethodTree(final Experiment experiment) {
        this.experiment = experiment;
    }

    public Experiment getExperiment() throws IOException, XMLException {
        return this.experiment;
    }
}
