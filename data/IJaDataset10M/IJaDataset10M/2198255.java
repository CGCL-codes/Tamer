package $packageName$;

import edu.clarkson.serl.psf.analysis.AbstractDriver;
import edu.clarkson.serl.psf.analysis.IFinalizer;

public class $FinalizerImpl$ implements IFinalizer {

    public void execute(AbstractDriver driver) {
        driver.out().println("Analysis of project completed by " + driver.getName());
    }
}
