package com.germinus.portlet.scriblog.model;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import com.germinus.portlet.scriblog.ScriblogPublishException;
import com.germinus.portlet.scriblog.ScriblogSaveException;
import com.germinus.portlet.scriblog.ScriblogUpdateException;
import com.germinus.xpression.cms.contents.ContentManager;
import com.germinus.xpression.cms.contents.ContentNotFoundException;
import com.germinus.xpression.cms.contents.DraftContent;
import com.germinus.xpression.cms.contents.MalformedContentException;
import com.germinus.xpression.cms.contents.PublishedContent;
import com.germinus.xpression.cms.directory.DirectoryFolder;
import com.germinus.xpression.cms.directory.MalformedDirectoryItemException;
import com.germinus.xpression.cms.model.Tag;
import com.germinus.xpression.cms.model.Tags;
import com.germinus.xpression.cms.util.ManagerRegistry;
import com.germinus.xpression.cms.worlds.World;

public class ScriblogContent {

    private static final String EMPTY_BODY = "";

    private DraftContent draftContent;

    private boolean previousDraft;

    private String newTags;

    public ScriblogContent(DraftContent draftContent, boolean previousDraft) {
        super();
        this.draftContent = draftContent;
        this.previousDraft = previousDraft;
    }

    public DraftContent getDraftContent() {
        return draftContent;
    }

    public boolean isPreviousDraft() {
        return previousDraft;
    }

    public String getBody() {
        try {
            return PropertyUtils.getNestedProperty(draftContent, bodyProperty()).toString();
        } catch (Exception e) {
            return EMPTY_BODY;
        }
    }

    private String bodyProperty() {
        return "contentData.body";
    }

    private String tagsProperty() {
        return "manifest.metadata.lom.general.keyword";
    }

    public String getTitle() {
        return draftContent.getName();
    }

    public String getTags() {
        return Tag.format(Tags.fromContent(draftContent).getTags());
    }

    public void setTags(String tags) {
        newTags = tags;
    }

    public void update(ScriblogEntry entry) throws ScriblogUpdateException {
        try {
            BeanUtils.setProperty(draftContent, bodyProperty(), entry.getBody());
        } catch (Exception e) {
            throw new ScriblogUpdateException(e);
        }
        draftContent.setName(entry.getTitle());
        try {
            BeanUtils.setProperty(draftContent, tagsProperty(), newTags);
        } catch (Exception e) {
            throw new ScriblogUpdateException(e);
        }
    }

    public void save(ContentManager contentManager) throws ScriblogSaveException {
        try {
            contentManager.saveAsDraftContent(draftContent);
        } catch (ContentNotFoundException e) {
            throw new ScriblogSaveException(e);
        } catch (MalformedContentException e) {
            throw new ScriblogSaveException(e);
        }
    }

    public PublishedContent publish(ContentManager contentManager) throws ScriblogPublishException {
        DirectoryFolder folder;
        try {
            folder = ManagerRegistry.getDirectoryPersister().getOwnerFolder(draftContent);
            World world = ManagerRegistry.getWorldManager().getOwnerWorld(draftContent);
            return contentManager.publishContent(draftContent, world, folder);
        } catch (MalformedDirectoryItemException e) {
            throw new ScriblogPublishException(e);
        }
    }

    public void setDraftContent(DraftContent draftContent) {
        this.draftContent = draftContent;
    }
}
