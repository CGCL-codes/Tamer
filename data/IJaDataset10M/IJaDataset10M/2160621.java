package rat.report.claim.util;

import rat.report.RatReportFailedException;
import rat.report.claim.IClaimReporter;

public class ClaimReporterMultiplexer implements IClaimReporter {

    private final IClaimReporter[] reporters;

    public ClaimReporterMultiplexer(final IClaimReporter[] reporters) {
        super();
        this.reporters = reporters;
    }

    public void claim(CharSequence subject, CharSequence predicate, CharSequence object, boolean isLiteral) throws RatReportFailedException {
        final int length = reporters.length;
        for (int i = 0; i < length; i++) {
            reporters[i].claim(subject, predicate, object, isLiteral);
        }
    }
}
