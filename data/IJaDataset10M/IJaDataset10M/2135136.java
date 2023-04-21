package com.ivis.xprocess.core;

import java.util.Set;
import com.ivis.xprocess.core.exceptions.ArtifactException;
import com.ivis.xprocess.framework.Xelement;
import com.ivis.xprocess.framework.annotations.Property;
import com.ivis.xprocess.framework.annotations.Property.CloneState;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.util.LogicalLink;

/**
 * A group of referenced or "owned" artifacts, contained for example in Projects, Tasks etc...
 *
 */
@com.ivis.xprocess.framework.annotations.Element(designator = "AR")
public interface ArtifactReferenceGroup extends Xelement, Nameable {

    public static final String MULTIPLE = "MULTIPLE";

    public static final String ARTIFACTS = "ARTIFACTS";

    /**
     * Whether the group is allowed to contain multiple artifacts.
     * @return true or false
     */
    @Property(name = MULTIPLE, propertyType = PropertyType.BOOLEAN)
    public boolean isMultiple();

    /**
     * Specify whether the group is to be allowed to contain multiple artifacts
     * @param multiple
     */
    public void setMultiple(boolean multiple);

    /**
     * Adds a reference of the passed artifact to this artifact group. If
     * it does not support multiple artifacts, the current one is removed
     * before adding the new artifact.
     *
     * @param artifact
     *            the artifact to add
     */
    public void addArtifact(Artifact artifact);

    /**
     * Removes the specified artifact from this role. Note the artifact itself
     * will be left undeleted.
     * This should be removed by calling the removeArtifact method on the
     * exchange xelement container
     *
     * @param artifact
     *            the artifact to remove
     */
    public void removeArtifact(Artifact artifact);

    /**
     * Returns all the artifacts of the artifact reference group
     *
     * @return all the artifacts
     */
    @Property(name = ARTIFACTS, propertyType = PropertyType.REFERENCESET, toBeCloned = CloneState.FALSE)
    public Set<Artifact> getArtifacts();

    /**
     * Creates a form, owned by this reference
     *
     * @throws ArtifactException
     */
    public Artifact createOwnedForm(String name, FormType type) throws ArtifactException;

    /**
     * Creates a new Managed File, owned by this reference
     *
     * @throws ArtifactException
     */
    public Artifact createOwnedArtifact(String name, FormType formType, String prototypePath) throws ArtifactException;

    /**
     * Creates a new hyperlink, owned by this reference
     *
     * @throws ArtifactException
     */
    public Artifact createOwnedArtifact(String name, FormType formType, LogicalLink link) throws ArtifactException;

    /**
     * Returns the artifacts "owned" by this ArtifactReference. These artifacts are cloned in when instantiating a
     * pattern that contains the ArtifactReferenceGroup. They are also deleted when the group is deleted.
     *
     * @return all the artifacts
     */
    @Property(propertyType = PropertyType.REFERENCESET, toBeCloned = CloneState.DEEP)
    public Set<Artifact> getOwnedArtifacts();

    /**
     * Returns referenced artifacts which are NOT owned by this ArtifactReferenceGroup
     */
    @Property(propertyType = PropertyType.REFERENCESET, toBeCloned = CloneState.SHALLOW)
    public Set<Artifact> getNonOwnedArtifacts();
}
