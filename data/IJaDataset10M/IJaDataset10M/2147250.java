package net.taylor.portal.entity.social;

import net.taylor.testing.AbstractBuilder;

/**
 * Builder for Attachment.
 *
 * @author jgilbert
 * @generated
 */
public class AttachmentBuilder extends AbstractBuilder<Attachment> {

    /** @generated */
    protected String partition = null;

    /** @generated */
    protected String name = null;

    /** @generated */
    protected String url = null;

    /** @generated */
    protected String contentType = null;

    /** @generated */
    protected byte[] content = null;

    /** @generated */
    protected String description = null;

    /** @generated */
    protected Long size = null;

    /** @generated */
    protected String artifact = null;

    /** @generated */
    public AttachmentBuilder() {
    }

    /** @generated */
    public static AttachmentBuilder instance() {
        return new AttachmentBuilder();
    }

    /** @generated */
    @Override
    public void initialize(Attachment instance) {
        instance.setPartition(partition);
        instance.setName(name);
        instance.setUrl(url);
        instance.setContentType(contentType);
        instance.setContent(content);
        instance.setDescription(description);
        instance.setSize(size);
        instance.setArtifact(artifact);
    }

    /** @generated */
    @Override
    protected Attachment find() {
        return null;
    }

    /** @generated */
    public AttachmentBuilder partition(String partition) {
        this.partition = partition;
        return this;
    }

    /** @generated */
    public AttachmentBuilder name(String name) {
        this.name = name;
        return this;
    }

    /** @generated */
    public AttachmentBuilder url(String url) {
        this.url = url;
        return this;
    }

    /** @generated */
    public AttachmentBuilder contentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    /** @generated */
    public AttachmentBuilder content(byte[] content) {
        this.content = content;
        return this;
    }

    /** @generated */
    public AttachmentBuilder description(String description) {
        this.description = description;
        return this;
    }

    /** @generated */
    public AttachmentBuilder size(Long size) {
        this.size = size;
        return this;
    }

    /** @generated */
    public AttachmentBuilder artifact(String artifact) {
        this.artifact = artifact;
        return this;
    }
}
