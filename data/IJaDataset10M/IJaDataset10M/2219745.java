package com.googlecode.mycontainer.kernel.deploy;

import java.net.URL;
import java.util.List;
import com.googlecode.mycontainer.kernel.Command;

public abstract class ScannableDeployer extends Command {

    public abstract void deploy(List<Class<?>> classes, List<URL> resources);
}
