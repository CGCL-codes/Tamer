package com.oneinmedia.parser.dsv;

import java.util.ArrayList;
import java.util.List;
import org.testng.Assert;
import com.oneinmedia.parser.SectionHandler;
import com.oneinmedia.parser.dsv.model.DSVFileSection;

public class TestDSVSectionHandler implements SectionHandler {

    private List<DSVFileSection> sections;

    private DSVFileSection currentSection = null;

    /**
     * @param sections List of expected DSV sections in expected sequence.
     */
    public TestDSVSectionHandler(List<DSVFileSection> sections) {
        this.sections = new ArrayList<DSVFileSection>(sections.size());
        this.sections.addAll(sections);
    }

    public void startSection(String sectionId) {
        assert sectionId != null : "Reported sectionId may not be null!";
        assert currentSection == null : "Received notification of section '" + sectionId + "' before the end of the previous section '" + currentSection.name() + "' has been reported!";
        assert !sections.isEmpty() : "Received notification of section '" + sectionId + "' although all expected DSV section have already been reported.";
        assert sectionId.equals(sections.get(0).name()) : "Received notification of section '" + sectionId + "' out of sequence! Expected section '" + sections.get(0).name() + "'.";
        try {
            currentSection = DSVFileSection.valueOf(sectionId);
        } catch (IllegalArgumentException e) {
            Assert.fail("Received notification of a section that is not a DSV section!", e);
        }
    }

    public void endSection(String sectionId) {
        assert sectionId != null : "Reported sectionId may not be null!";
        assert sectionId.equals(currentSection.name());
        currentSection = null;
        sections.remove(0);
    }

    /**
     * @return <code>True</code> if all expected sections have been reported.
     */
    public boolean expectationsMet() {
        return sections.isEmpty();
    }
}
