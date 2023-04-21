package org.gnupaste.internal.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.gnupaste.external.editorinfo.IEditorInfoPlugin;
import org.gnupaste.external.hosters.IHosterPlugin;
import org.gnupaste.internal.exceptions.IllegalEditorInfoPluginException;
import org.gnupaste.internal.exceptions.IllegalHosterPluginException;
import org.gnupaste.internal.interfaces.IEditorInfoPluginDescriptor;
import org.gnupaste.internal.interfaces.IHosterPluginDescriptor;

public class EditorInfoPluginDescriptorImpl implements IEditorInfoPluginDescriptor {

    private String name;

    private String id;

    private String infoClassName;

    private String editorClassName;

    private IConfigurationElement editorinfo;

    private final IExtension extension;

    public EditorInfoPluginDescriptorImpl(IExtension plugin) throws IllegalEditorInfoPluginException {
        this.extension = plugin;
        for (IConfigurationElement e : plugin.getConfigurationElements()) {
            if (e.getName().equals("editorinfo")) {
                editorinfo = e;
            }
        }
        if (editorinfo == null) {
            throw new IllegalEditorInfoPluginException();
        }
        id = plugin.getUniqueIdentifier();
        name = editorinfo.getAttribute("name");
        infoClassName = editorinfo.getAttribute("infoClass");
        editorClassName = editorinfo.getAttribute("editorClass");
        if (name == null || id == null || infoClassName == null || editorClassName == null) {
            throw new IllegalEditorInfoPluginException();
        }
    }

    @Override
    public IEditorInfoPlugin getInstance() {
        try {
            return (IEditorInfoPlugin) editorinfo.createExecutableExtension("infoClass");
        } catch (CoreException e) {
            return null;
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public IExtension getIExtension() {
        return extension;
    }

    @Override
    public String getEditorClassName() {
        return editorClassName;
    }

    @Override
    public String getInfoClassName() {
        return infoClassName;
    }
}
