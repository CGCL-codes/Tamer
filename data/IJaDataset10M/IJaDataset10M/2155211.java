package pi.mojo.cle;

import java.util.List;
import pi.cle.exec.CupConfig;

/**
 * @goal cup
 * @phase generate-sources
 * @author Pappy Răzvan STĂNESCU <a href="mailto:pappy&#64;clarmon.com">&lt;pappy&#64;clarmon.com&gt;</a>
 */
public class CupMojo extends AbstractCupMojo {

    /**
	 * @parameter name="cup"
	 */
    private CupConfig cup;

    /**
	 * @parameter name="sourceDirectory" default-value="${basedir}/src/main/etc"
	 */
    private String sourceDirectory;

    /**
	 * @parameter name="outputDirectory" default-value="${project.build.directory}/generated-sources/etc"
	 */
    private String outputDirectory;

    @Override
    CupConfig config() {
        if (this.cup == null) {
            this.cup = new CupConfig();
        }
        return this.cup;
    }

    @Override
    String outputDirectory() {
        return this.outputDirectory;
    }

    @Override
    String sourceDirectory() {
        return this.sourceDirectory;
    }

    @Override
    List sourceRoots() {
        return this.project.getCompileSourceRoots();
    }
}
