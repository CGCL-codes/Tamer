package org.xmlvm.proc;

import java.util.Collection;
import java.util.Map;
import org.xmlvm.proc.out.OutputFile;

/**
 * The interface of {@link CompilationBundle} that is used during the first
 * phase.
 */
public interface BundlePhase1 {

    /**
     * Adds the given {@link XmlvmResource} to this bundle.
     */
    public void addResource(XmlvmResource resource);

    /**
     * Adds the given {@link XmlvmResource}s to this bundle
     */
    public void addResources(Collection<XmlvmResource> resources);

    /**
     * Returns all {@link XmlvmResource}s that have been added to the bundle so
     * far.
     */
    public Collection<XmlvmResource> getResources();

    /**
     * Returns a read-only map of the resources, where the key is the resource's
     * full name.
     */
    public Map<String, XmlvmResource> getResourceMap();

    /**
     * Adds a new {@link OutputFile} to this bundle.
     */
    public void addOutputFile(OutputFile file);

    /**
     * Adds a bunch of {@link OutputFile}s to this bundle.
     */
    public void addOutputFiles(Collection<OutputFile> files);

    /**
     * Returns all {@link OutputFile}s that have been added to the bundle so
     * far.
     */
    public Collection<OutputFile> getOutputFiles();

    /**
     * Removes the given {@link OutputFile} from this bundle.
     */
    public void removeOutputFile(OutputFile file);

    /**
     * Removes all {@link OutputFile}s from this bundle.
     */
    public void removeAllOutputFiles();
}
