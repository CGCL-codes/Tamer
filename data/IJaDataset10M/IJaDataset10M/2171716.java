package org.deft.repository.exception;

import org.deft.repository.datamodel.Folder;
import org.deft.repository.datamodel.Fragment;
import org.deft.repository.datamodel.FragmentType;

public class DeftIncompatibleTypeException extends DeftException {

    private static final long serialVersionUID = 1L;

    public DeftIncompatibleTypeException(Folder folder, String fragmentType) {
        super("Folder " + folder.getName() + " cannot contain fragments " + "of type " + fragmentType);
    }

    public DeftIncompatibleTypeException(Folder folder, FragmentType fragmentType) {
        this(folder, fragmentType.getName());
    }

    public DeftIncompatibleTypeException(Folder folder, Fragment fragment) {
        this(folder, fragment.getFragmentType());
    }
}
