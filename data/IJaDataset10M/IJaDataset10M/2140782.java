package com.c4j.xml;

import com.c4j.filetools.IFileTools;
import com.c4j.foldertree.IFolderTree;
import com.c4j.sre.C4JRuntimeException;
import com.c4j.sre.IFacet;
import com.c4j.type.ITypeCreator;
import com.c4j.workspace.IWorkspaceFactory;

/**
 * Implements most of the methods specified in the interface of the appropriate component.
 */
public abstract class XMLBase implements XMLIface, XMLIface.Neighborhood {

    /**
     * Stores the name of this instance as specified at construction time.
     */
    private final String instanceName;

    /**
     * Provides the facet ‘xml’ of the appropriate component and must be implemented by
     * the component implementation.
     *
     * @return an implementation of the interface as specified for the component facet.
     */
    protected abstract IXMLHelper provide_xml();

    /**
     * Stores the facet ‘xml’ of the component.
     */
    private final IFacet<IXMLHelper> facet_xml = new IFacet<IXMLHelper>() {

        @Override
        public IXMLHelper get() {
            return provide_xml();
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
     * Stores the connection to the receptacle ‘type’.
     */
    private IFacet<ITypeCreator> connected_type = null;

    /**
     * Stores the connection to the receptacle ‘workspace’.
     */
    private IFacet<IWorkspaceFactory> connected_workspace = null;

    /**
     * Constructs a new instance of the appropriate component with the given name.
     *
     * @param instanceName
     *         the name of the instance.
     */
    protected XMLBase(final String instanceName) {
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
    public final IFacet<IXMLHelper> getFacet_xml() {
        return facet_xml;
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

    @Override
    public final ITypeCreator use_type() {
        try {
            if (connected_type == null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected.", "type", instanceName));
            return connected_type.get();
        } catch (StackOverflowError e) {
            throw new C4JRuntimeException(String.format("StackOverflowError when calling ‘%s’ of component instance ‘%s’. You possibly " + "created a cycle when connecting components and composites!", "type", instanceName), e);
        }
    }

    @Override
    public final void connect_type(final IFacet<ITypeCreator> facet) {
        if (connected_type != null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is already connected.", "type", instanceName));
        connected_type = facet;
    }

    @Override
    public final void disconnect_type(final IFacet<ITypeCreator> facet) {
        if (connected_type != facet) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected with the given facet.", "type", instanceName));
        connected_type = null;
    }

    @Override
    public final IWorkspaceFactory use_workspace() {
        try {
            if (connected_workspace == null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected.", "workspace", instanceName));
            return connected_workspace.get();
        } catch (StackOverflowError e) {
            throw new C4JRuntimeException(String.format("StackOverflowError when calling ‘%s’ of component instance ‘%s’. You possibly " + "created a cycle when connecting components and composites!", "workspace", instanceName), e);
        }
    }

    @Override
    public final void connect_workspace(final IFacet<IWorkspaceFactory> facet) {
        if (connected_workspace != null) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is already connected.", "workspace", instanceName));
        connected_workspace = facet;
    }

    @Override
    public final void disconnect_workspace(final IFacet<IWorkspaceFactory> facet) {
        if (connected_workspace != facet) throw new C4JRuntimeException(String.format("Receptacle ‘%s’ of component instance ‘%s’ is not connected with the given facet.", "workspace", instanceName));
        connected_workspace = null;
    }
}
