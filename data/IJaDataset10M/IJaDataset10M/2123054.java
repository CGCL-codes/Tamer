package com.threerings.jpkg.ant.dpkg.info;

import org.junit.Test;
import com.threerings.antidote.AntTestHelper;
import com.threerings.antidote.RequiresValidationException;
import com.threerings.antidote.field.text.EmptyTextFieldViolation;
import com.threerings.antidote.field.text.UnsetTextFieldViolation;
import static com.threerings.antidote.ValidationTestHelper.assertNoViolations;
import static com.threerings.antidote.ValidationTestHelper.assertOneViolation;

public class SectionTest extends AntTestHelper {

    @Test
    public void testSection() {
        final Section section = newSection();
        section.addText("misc");
        assertNoViolations(section);
    }

    @Test
    public void testEmptyData() {
        Section section = newSection();
        assertOneViolation(section, UnsetTextFieldViolation.class);
        section = newSection();
        section.addText("");
        assertOneViolation(section, EmptyTextFieldViolation.class);
    }

    @Test(expected = RequiresValidationException.class)
    public void testRequiresValidation() {
        final Section section = newSection();
        section.addText("misc");
        section.getPackageSection();
    }

    private Section newSection() {
        final Section section = new Section();
        section.setProject(createProject());
        return section;
    }
}
