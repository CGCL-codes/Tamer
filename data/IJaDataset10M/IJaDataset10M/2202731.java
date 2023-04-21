package com.papyrus.alf2java.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;
import java.util.Iterator;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.common.edit.domain.UML2AdapterFactoryEditingDomain;
import org.apache.commons.lang.StringUtils;

public class ModelGetter {

    final ArrayList<Model> models;

    final EditingDomain editingDomain;

    public ModelGetter() {
        models = new ArrayList<Model>();
        editingDomain = new UML2AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), new HashMap<Resource, Boolean>());
    }

    public Model getFirstOpenModel(final IProject project) {
        final Iterable<Model> result = getAssociatedModels(project);
        if (result == null) {
            return null;
        } else {
            return result.iterator().next();
        }
    }

    public Iterable<Model> getAssociatedModels(final IProject project) {
        if (project == null) {
            return null;
        }
        try {
            for (final IResource resource : project.members()) {
                if (resource.getType() == IResource.FILE && StringUtils.equals(resource.getFileExtension(), "uml")) {
                    models.add(getModel(editingDomain, resource));
                }
            }
            return models;
        } catch (CoreException e) {
            e.printStackTrace();
        }
        return models;
    }

    public void removeAllBehaviors() {
    }

    private static Model getModel(final EditingDomain editingDomain, final IResource resource) {
        return (Model) editingDomain.getResourceSet().getResource(URI.createPlatformResourceURI(resource.getFullPath().toString(), true), true).getContents().get(0);
    }
}
