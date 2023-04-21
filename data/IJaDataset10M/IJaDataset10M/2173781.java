package de.sendorian.app.forumArchive.renamer;

import org.apache.commons.lang.StringUtils;
import de.sendorian.app.forumArchive.domain.File;

public class CapitalizeFirstRenamer implements FileRenamer {

    public File rename(File file) {
        file.setName(StringUtils.capitalize(file.getName()));
        return file;
    }
}
