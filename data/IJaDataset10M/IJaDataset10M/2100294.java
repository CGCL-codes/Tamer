package org.eclipse.ui.internal.texteditor.rulers;

import java.net.URL;
import org.osgi.framework.Bundle;
import com.ibm.icu.text.MessageFormat;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.internal.texteditor.TextEditorPlugin;

public final class ExtensionPointHelper {

    private final IConfigurationElement fElement;

    private final String fName;

    private final ILog fLog;

    public ExtensionPointHelper(IConfigurationElement element, ILog log) throws InvalidRegistryObjectException {
        Assert.isLegal(element != null);
        Assert.isLegal(log != null);
        fLog = log;
        fElement = element;
        fName = element.getName();
    }

    public String getDefaultAttribute(String attribute, String dflt) throws InvalidRegistryObjectException {
        String value = fElement.getAttribute(attribute);
        return value == null ? dflt : value;
    }

    public String getNonNullAttribute(String attribute) throws InvalidRegistryObjectException {
        String value = fElement.getAttribute(attribute);
        if (value == null) fail(MessageFormat.format(RulerColumnMessages.ExtensionPointHelper_missing_attribute_msg, new Object[] { fName, attribute }));
        return value;
    }

    public float getDefaultAttribute(String attribute, float dflt) {
        String value = getDefaultAttribute(attribute, null);
        if (value == null) return dflt;
        try {
            return Float.valueOf(value).floatValue();
        } catch (NumberFormatException x) {
            fail(MessageFormat.format(RulerColumnMessages.ExtensionPointHelper_invalid_number_attribute_msg, new Object[] { attribute, fName }));
            return dflt;
        }
    }

    public boolean getDefaultAttribute(String attribute, boolean dflt) {
        String value = getDefaultAttribute(attribute, null);
        if (value == null) return dflt;
        try {
            return Boolean.valueOf(value).booleanValue();
        } catch (NumberFormatException x) {
            fail(MessageFormat.format(RulerColumnMessages.ExtensionPointHelper_invalid_number_attribute_msg, new Object[] { fName, attribute }));
            return dflt;
        }
    }

    public void fail(String message) throws InvalidRegistryObjectException {
        String id = findId(fElement);
        String extensionPointId = fElement.getDeclaringExtension().getExtensionPointUniqueIdentifier();
        Object[] args = { fElement.getContributor().getName(), id, extensionPointId };
        String blame = MessageFormat.format(RulerColumnMessages.ExtensionPointHelper_invalid_contribution_msg, args);
        IStatus status = new Status(IStatus.WARNING, TextEditorPlugin.PLUGIN_ID, IStatus.OK, blame + message, null);
        fLog.log(status);
        throw new InvalidRegistryObjectException();
    }

    public static String findId(IConfigurationElement element) {
        String id = null;
        while (element != null && id == null) {
            id = element.getAttribute("id");
            if (id != null) break;
            Object parent = element.getParent();
            if (parent instanceof IExtension) {
                id = ((IExtension) parent).getUniqueIdentifier();
                break;
            } else if (parent instanceof IConfigurationElement) {
                element = (IConfigurationElement) parent;
            } else {
                break;
            }
        }
        return id == null ? "<unknown>" : id;
    }

    public URL getDefaultResourceURL(String attribute, URL dflt) {
        String value = getDefaultAttribute(attribute, null);
        if (value == null) return dflt;
        Bundle bundle = getBundle();
        if (bundle == null) return dflt;
        Path path = new Path(value);
        return FileLocator.find(bundle, path, null);
    }

    private Bundle getBundle() {
        String namespace = fElement.getDeclaringExtension().getContributor().getName();
        Bundle bundle = Platform.getBundle(namespace);
        return bundle;
    }
}
