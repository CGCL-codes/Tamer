package com.g2inc.scap.library.validators.xccdf;

import java.util.List;
import com.g2inc.scap.library.domain.Anomaly;
import com.g2inc.scap.library.domain.SCAPElement;
import com.g2inc.scap.library.domain.Anomaly.ErrorLevel;
import com.g2inc.scap.library.domain.xccdf.Item;
import com.g2inc.scap.library.domain.xccdf.Profile;
import com.g2inc.scap.library.domain.xccdf.XCCDFBenchmark;

public class Validate10 extends AbstractValidatorLang {

    @Override
    public void applyRule(List<Anomaly> anomList, SCAPElement scapElement) {
        if (scapElement instanceof XCCDFBenchmark) {
            XCCDFBenchmark benchmark = (XCCDFBenchmark) scapElement;
            checkList(benchmark.getDescriptionList(), benchmark, anomList);
        } else if (scapElement instanceof Profile) {
            Profile profile = (Profile) scapElement;
            checkList(profile.getDescriptionList(), profile, anomList);
        } else if (scapElement instanceof Item) {
            Item item = (Item) scapElement;
            checkList(item.getDescriptionList(), item, anomList);
        }
    }

    @Override
    public Anomaly anomaly(String message) {
        Anomaly anomaly = new Anomaly();
        anomaly.setErrorLevel(ErrorLevel.ERROR);
        anomaly.setRequirementId(10);
        anomaly.setSection("4.1.1");
        anomaly.setMessage(message);
        return anomaly;
    }
}
