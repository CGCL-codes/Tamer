package org.freeworld.medialauncher.model.launcher;

import java.util.List;
import org.freeworld.medialauncher.model.access.AccessLocator;
import org.freeworld.medialauncher.model.access.AccessType;
import org.freeworld.medialauncher.model.input.InputType;

/**
 * <p>Media player profile provides all the information needed by the system
 * to analyze and execute a media player</p>
 * 
 * @author dchemko
 */
public interface MediaPlayerProfile {

    public boolean isFoundLocally();

    public List<InputType> getSupportedInputTypes();

    public List<AccessType> getSupportedAccessTypes();

    public boolean isResolvedFileSupported(AccessType inputAccessType, InputType inputType);

    public String getExecutableName();

    public String[] createArguments(AccessLocator locator);
}
