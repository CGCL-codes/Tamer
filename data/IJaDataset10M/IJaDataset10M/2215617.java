package ch.oblivion.comix.plugin.providers;

import org.eclipse.jface.viewers.ISelection;
import ch.oblivion.comix.model.ComixProfile;

/**
 * A comix profile selection.
 * @author mima
 */
public class ComixSelection implements ISelection {

    private final ComixProfile profile;

    public ComixSelection(ComixProfile profile) {
        this.profile = profile;
    }

    @Override
    public boolean isEmpty() {
        return profile == null;
    }

    public ComixProfile getProfile() {
        return profile;
    }
}
