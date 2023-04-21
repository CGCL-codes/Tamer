package edu.udo.cs.ai.nemoz.lab.util;

import edu.udo.cs.ai.nemoz.lab.task.LabResultEnvelope;

public interface Callable {

    public void callback(LabResultEnvelope result);
}
