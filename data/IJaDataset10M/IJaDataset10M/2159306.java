package org.punit.runner;

import java.io.*;
import org.punit.type.*;

public class RunnerProperties implements Serializable {

    private static final long serialVersionUID = -4832579362013257784L;

    public String vmName;

    public boolean isIntermediate;

    public boolean isParent;

    public VM[] vms;
}
