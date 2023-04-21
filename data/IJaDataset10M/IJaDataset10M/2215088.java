package rat.analysis;

import java.util.ArrayList;
import java.util.List;
import rat.report.claim.IClaimReporter;

public class MockLicenseMatcher implements IHeaderMatcher {

    public final List lines = new ArrayList();

    public int resets = 0;

    public boolean result = true;

    public boolean match(String subject, String line, IClaimReporter reporter) {
        lines.add(line);
        return result;
    }

    public void reset() {
        resets++;
    }
}
