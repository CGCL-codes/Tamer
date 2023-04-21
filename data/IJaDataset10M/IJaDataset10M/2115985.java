package com.threerings.jpkg.ant.dpkg.info;

import com.threerings.antidote.field.text.SingleLineTextField;
import com.threerings.antidote.property.IntegerProperty;
import com.threerings.antidote.property.StringProperty;
import com.threerings.jpkg.debian.ControlDataInvalidException;
import com.threerings.jpkg.debian.PackageVersion;
import static com.threerings.antidote.MutabilityHelper.areMutablesSet;
import static com.threerings.antidote.MutabilityHelper.requiresValidation;

/**
 * Stores the &lt;info&gt; &lt;version&gt; field, which is the package version.
 * @see PackageVersion
 */
public class Version extends SingleLineTextField {

    public String getFieldName() {
        return "version";
    }

    /**
     * Ant setter field: epoch.
     */
    public void setEpoch(String value) {
        _epoch.setValue(value);
    }

    /**
     * Ant setter field: debianVersion.
     */
    public void setDebianVersion(String value) {
        _debianVersion.setValue(value);
    }

    /**
     * Returns the user data converted into a {@link PackageVersion}. Cannot be called before validate().
     */
    public PackageVersion getPackageVersion() {
        requiresValidation(_packageVersion);
        return _packageVersion;
    }

    @Override
    protected void validateTextField() {
        try {
            switch(areMutablesSet(_epoch, _debianVersion)) {
                case ALL_UNSET:
                    _packageVersion = new PackageVersion(getText());
                    return;
                case SOME_UNSET:
                    reportUnsetDependentProperties(_epoch, _debianVersion);
                    reportUnsetDependentProperties(_debianVersion, _epoch);
                    return;
                case ALL_SET:
                    break;
            }
            switch(validateProperties(_debianVersion, _epoch)) {
                case SOME_INVALID:
                case ALL_INVALID:
                    return;
                case ALL_VALID:
                    _packageVersion = new PackageVersion(getText(), _debianVersion.getValue(), _epoch.getValue());
                    return;
            }
        } catch (final ControlDataInvalidException cdie) {
            appendViolation(new ControlDataViolation(this, cdie));
        }
    }

    /** The PackageVersion object representing the user supplied data. */
    private PackageVersion _packageVersion;

    /** Ant adder/setter fields. */
    private final StringProperty _debianVersion = new StringProperty("debianversion", this);

    private final IntegerProperty _epoch = new IntegerProperty("epoch", this);
}
