package com.volantis.mcs.runtime;

import com.volantis.mcs.assets.ScriptAsset;
import com.volantis.mcs.dom.Element;
import com.volantis.mcs.project.Project;
import com.volantis.mcs.protocols.DOMOutputBuffer;
import com.volantis.mcs.protocols.DOMProtocol;
import com.volantis.mcs.protocols.ScriptAttributes;
import com.volantis.mcs.protocols.VolantisProtocol;
import com.volantis.mcs.protocols.assets.ScriptAssetReference;
import com.volantis.mcs.protocols.assets.implementation.DefaultComponentScriptAssetReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Manager class for script libraries. It has two phases:
 *
 * <ul>
 * <li>in the first phase it collects script libraries (as SelectedVariant
 * objects) and renders them into the head of the protocol used by the page
 * context when writeScriptElements is called,</li>
 * <li>in the second phase scripts are immediately rendered into the document on
 * the first time they are introduced to the manager. Script libraries won't be
 * rendered again if they are rendered in the first phase.</li>
 * </ul>
 *
 * <p>The manager must be closed if it is no longer needed. After closing the
 * manager no scripts can be added to it.</p>
 *
 * <p>This class is not thread-safe.</p>
 * 
 * @mock.generate
 */
public class ScriptLibraryManager {

    /**
     * The set of the policy names of the added script variants.
     */
    private final Set policyKeys;

    /**
     * The ordered list of script variants.
     */
    private final List scriptReferences;

    /**
     * Flag to indicate if this manager can accept more script variants.
     */
    private boolean closed;

    /**
     * Helper element to insert script elements to the head after
     * writeScriptElements is called.
     */
    private Element markerElement;

    private DOMProtocol protocol;

    /**
     * Creates a script library manager instance.
     *
     * @param protocol the asset resolver to be used
     */
    public ScriptLibraryManager(final VolantisProtocol protocol) {
        this.protocol = (DOMProtocol) protocol;
        policyKeys = new HashSet();
        scriptReferences = new LinkedList();
        closed = false;
    }

    /**
     * Adds the script asset reference to the collected references if it is not
     * already present (the policy name and the project object is used to
     * differentiate between script asset references).
     *
     * <p>If this ScriptLibraryManager already contains the specified element,
     * this manager remains unchanged and returns false, otherwise returns
     * true.<p>
     *
     * <p>Note:ATM only accepts DefaultComponentScriptAssetReferences.</p>
     *
     * @param scriptReference the script asset reference to add
     * @return true iff the reference is added to the manager as a new element
     * @throws IllegalStateException if the manager is closed
     */
    public boolean addScript(final ScriptAssetReference scriptReference) {
        checkClosed();
        final String name = ((DefaultComponentScriptAssetReference) scriptReference).getPolicyReference().getName();
        final ScriptAsset scriptAsset = scriptReference.getScriptAsset();
        final Project project = scriptAsset.getProject();
        final boolean isNew = policyKeys.add(new PolicyKey(project, name));
        if (isNew) {
            if (markerElement == null) {
                scriptReferences.add(scriptReference);
            } else {
                final ScriptAttributes attributes = assetToAttributes(scriptReference);
                final Element scriptElement = protocol.createScriptElement(attributes);
                scriptElement.insertBefore(markerElement);
            }
        }
        return isNew;
    }

    /**
     * Stores the attributes from a script variant in a ScriptAttributes object
     * @param scriptReference
     * @return the created ScriptAttributes object
     */
    private ScriptAttributes assetToAttributes(final ScriptAssetReference scriptReference) {
        final ScriptAsset scriptAsset = scriptReference.getScriptAsset();
        final ScriptAttributes attributes = new ScriptAttributes();
        attributes.setCharSet(scriptAsset.getCharacterSet());
        attributes.setLanguage(scriptAsset.getProgrammingLanguage());
        attributes.setType(scriptAsset.getMimeType());
        attributes.setScriptReference(scriptReference);
        return attributes;
    }

    /**
     * Writes out the collected script references to the head buffer of the
     * protocol used.
     *
     * @throws IllegalStateException if the manager is closed
     */
    public void writeScriptElements() {
        checkClosed();
        for (Iterator iter = scriptReferences.iterator(); iter.hasNext(); ) {
            final ScriptAssetReference scriptVariant = (ScriptAssetReference) iter.next();
            final ScriptAttributes attributes = assetToAttributes(scriptVariant);
            protocol.pushHeadBuffer();
            protocol.writeOpenScript(attributes);
            protocol.writeCloseScript(attributes);
            protocol.popHeadBuffer();
        }
        markerElement = protocol.getDOMFactory().createElement("DELETE_ME");
        DOMOutputBuffer outputBuffer = ((DOMOutputBuffer) protocol.getPageHead().getHead());
        outputBuffer.addElement(markerElement);
    }

    /**
     * Closes the manager.
     */
    public void close() {
        checkClosed();
        if (markerElement == null) {
            if (!policyKeys.isEmpty()) {
                throw new IllegalStateException("There are script elements waiting to be written out.");
            }
        } else {
            markerElement.remove();
        }
        closed = true;
    }

    /**
     * Checks if the manager has been closed. If it is , it throws an
     * IllegalStateException.
     */
    private void checkClosed() {
        if (closed) {
            throw new IllegalStateException("This ScriptLibraryManager has already been closed.");
        }
    }

    private static class PolicyKey {

        private Project project;

        private String name;

        public PolicyKey(final Project project, final String name) {
            this.project = project;
            this.name = name;
        }

        public int hashCode() {
            return (project == null ? 0 : project.hashCode()) + (name == null ? 0 : name.hashCode());
        }

        public boolean equals(final Object obj) {
            if (obj == null) {
                return false;
            }
            if (!obj.getClass().equals(getClass())) {
                return false;
            }
            final PolicyKey other = (PolicyKey) obj;
            return (project == null && other.project == null || project != null && project.equals(other.project)) && (name == null && other.name == null || name != null && name.equals(other.name));
        }
    }
}
