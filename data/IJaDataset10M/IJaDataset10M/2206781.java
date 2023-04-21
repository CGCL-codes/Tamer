package org.monet.modelling.ide.builders.errors;

import org.eclipse.core.resources.IResource;
import org.monet.modelling.ide.problems.Error;

public class IncludeFileNotFound extends Error {

    public IncludeFileNotFound(IResource resource, String explanation) {
        super(resource, String.format("File %s not found. %s", resource.getName(), explanation), 0);
    }
}
