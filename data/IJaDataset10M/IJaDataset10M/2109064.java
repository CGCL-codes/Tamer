package com.threerings.jpkg.ant.dpkg.info.description;

import java.util.Collections;
import java.util.List;
import com.threerings.antidote.Violation;
import com.threerings.antidote.field.BaseField;
import com.threerings.jpkg.ant.dpkg.info.Description;
import com.threerings.jpkg.debian.PackageDescription;

/**
 * A {@link DescriptionAction} field that inserts a blank line into the {@link Description}.
 * @see PackageDescription
 */
public class Blank extends BaseField implements DescriptionAction {

    public String getFieldName() {
        return "blank";
    }

    public List<Violation> apply(PackageDescription description) {
        description.addBlankLine();
        return Collections.emptyList();
    }

    @Override
    protected void validateField() {
    }
}
