package com.c4j.builder;

import com.c4j.filetools.IFileTools;
import com.c4j.foldertree.IFolderTree;
import com.c4j.sre.C4JRuntimeException;
import com.c4j.sre.IFacet;

/**
 * Implements most of the methods specified in the interface of the appropriate component.
 */
public abstract class BuilderBase implements BuilderIface, BuilderIface.Neighborhood {

    /**
     * Stores the name of this instance as specified at construction time.
     */
    private final String instanceName;

    /**
     * Provides the facet ‘builder’ of the appropriate component and must be implemented by
     * the component implementation.
     *
     * @return an implementation of the interface as specified for the component facet.
     */
    protected abstract IBuilder provide_builder();

    /**
     * Stores the facet ‘builder’ of the component.
     */
    private final IFacet<IBuilder> facet_builder = new IFacet<IBuilder>() {

        @Override
        public IBuilder get() {
            return provide_builder();
        }
    };

    /**
     * Stores the connection to the receptacle ‘filetools’.
     */
    private IFacet<IFileTools> connected_filetools = null;

    /**
     * Stores the connection to the receptacle ‘folders’.
     */
    private IFacet<IFolderTree> connected_folders = null;

    /**
     * Constructs a new instance of the appropriate component with the given name.
     *
     * @param instanceName
     *         the name of the instance.
     */
    protected BuilderBase(final String instanceName) {
        this.instanceName = instanceName;
    }

    /**
     * The name of the instance as specified at construction time.
     *
     * @return the name of the instance.
     */
    public final String getName() {
        return instanceName;
    }

    @Override
    public final IFacet<IBuilder> getFacet_builder() {
        return facet_builder;
    }

    @Override
    public final IFileTools use_filetools() {
        try {
            if (connected_filetools == null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected.", "filetools", instanceName));
            return connected_filetools.get();
        } catch (StackOverflowError e) {
            throw new C4JRuntimeException(String.format("StackOverflowError when calling ‘%s’ of component instance ‘%s’. You possibly " + "created a cycle when connecting components and composites!", "filetools", instanceName), e);
        }
    }

    @Override
    public final void connect_filetools(final IFacet<IFileTools> facet) {
        if (connected_filetools != null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is already connected.", "filetools", instanceName));
        connected_filetools = facet;
    }

    @Override
    public final void disconnect_filetools(final IFacet<IFileTools> facet) {
        if (connected_filetools != facet) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected with the given facet.", "filetools", instanceName));
        connected_filetools = null;
    }

    @Override
    public final IFolderTree use_folders() {
        try {
            if (connected_folders == null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected.", "folders", instanceName));
            return connected_folders.get();
        } catch (StackOverflowError e) {
            throw new C4JRuntimeException(String.format("StackOverflowError when calling ‘%s’ of component instance ‘%s’. You possibly " + "created a cycle when connecting components and composites!", "folders", instanceName), e);
        }
    }

    @Override
    public final void connect_folders(final IFacet<IFolderTree> facet) {
        if (connected_folders != null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is already connected.", "folders", instanceName));
        connected_folders = facet;
    }

    @Override
    public final void disconnect_folders(final IFacet<IFolderTree> facet) {
        if (connected_folders != facet) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected with the given facet.", "folders", instanceName));
        connected_folders = null;
    }
}
