package net.sf.refactorit.cli;

import net.sf.refactorit.cli.actions.Runner;
import net.sf.refactorit.ui.options.profile.ProfileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ArgumentsValidator {

    private List warnings = new ArrayList();

    public static final String PROFILE_NOT_SUPPORTED = "This action does not support the -profile switch, ignoring";

    public void checkArguments(final Arguments arguments) throws ProjectInitException {
        checkUnsupportedFormat(arguments);
        checkActionPresent(arguments);
        checkProfileSupport(arguments);
        if (arguments.getModelBuilder().supportsProfiles()) {
            checkProfile(arguments.getProfile());
        }
        checkWarnings(arguments);
    }

    private void checkWarnings(Arguments a) {
        if (a.getWarning() != null) {
            warnings.add(a.getWarning());
        }
    }

    private void checkUnsupportedFormat(final Arguments c) throws ProjectInitException {
        if (!new Runner().supportsChosenFormat(c)) {
            throw new ProjectInitException("ERROR: Unsupported format: " + c.getFormat());
        }
    }

    public void checkUnknownParameters(String[] args) throws ProjectInitException {
        if (new ArgumentsParser(args).getUnknownTags().size() > 0) {
            throw new ProjectInitException("ERROR: Unknown parameter(s): " + new ArgumentsParser(args).getUnknownTags());
        }
    }

    public void checkProfile(String filename) throws ProjectInitException {
        if ("".equals(filename)) {
            return;
        }
        File file = new File(filename);
        if (!file.exists()) {
            throw new ProjectInitException("ERROR: Profile does not exist: " + filename);
        }
        if (!ProfileUtil.isValidProfile(file)) {
            throw new ProjectInitException("ERROR: Corrupted profile: " + filename);
        }
    }

    public void checkActionPresent(Arguments arguments) throws ProjectInitException {
        if (arguments.getModelBuilder() == null) {
            throw new ProjectInitException("ERROR: Action name missing");
        }
    }

    public void checkProfileSupport(Arguments arguments) {
        if (!"".equals(arguments.getProfile()) && !arguments.getModelBuilder().supportsProfiles()) {
            warnings.add(Arguments.WARNING + ArgumentsValidator.PROFILE_NOT_SUPPORTED);
        }
    }

    public List getWarnings() {
        return warnings;
    }
}
