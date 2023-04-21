package net.sourceforge.basher.example.tasks.test;

import java.util.Random;
import net.sourceforge.basher.Phase;
import net.sourceforge.basher.annotations.BasherExecuteMethod;
import net.sourceforge.basher.annotations.BasherMaxInvocations;
import net.sourceforge.basher.annotations.BasherPhases;
import org.apache.commons.logging.*;

/**
 * @author Johan Lindquist
 */
@BasherPhases(phases = { Phase.SETUP, Phase.RUN })
@BasherMaxInvocations(max = 1000)
public class AnnotatedExcludedHelloWorldTask {

    private Log _log = LogFactory.getLog(AnnotatedExcludedHelloWorldTask.class);

    @BasherExecuteMethod
    public void doit() {
        _log.debug("AnnotatedExcludedHelloWorldTask");
    }
}
