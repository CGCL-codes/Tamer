package org.remus.infomngmnt.core.ref.tags;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.remus.infomngmnt.AvailableTags;
import org.remus.infomngmnt.InfomngmntFactory;
import org.remus.infomngmnt.InformationUnit;
import org.remus.infomngmnt.InformationUnitListItem;
import org.remus.infomngmnt.Tag;
import org.remus.infomngmnt.core.extension.ISaveParticipant;
import org.remus.infomngmnt.core.model.ApplicationModelPool;

/**
 * @author Tom Seidel <tom.seidel@remus-software.org>
 */
public class TagSaveParticipant implements ISaveParticipant {

    /**
	 * 
	 */
    public TagSaveParticipant() {
    }

    private void handleOldTags(final List<Tag> tagsByInfoUnitCopy, final InformationUnit unit) {
        for (Tag tag : tagsByInfoUnitCopy) {
            tag.getInfoUnits().remove(unit.getId());
            handleEmptyTag(tag);
        }
    }

    private void handleNewTags(final List<String> splitCopy, final InformationUnit unit) {
        for (String newTag : splitCopy) {
            Tag tagByName = getTagByName(newTag);
            if (tagByName == null) {
                tagByName = InfomngmntFactory.eINSTANCE.createTag();
                tagByName.setName(newTag);
                ApplicationModelPool.getInstance().getModel().getAvailableTags().getTags().add(tagByName);
            }
            tagByName.getInfoUnits().add(((InformationUnitListItem) unit.getAdapter(InformationUnitListItem.class)).getId());
        }
    }

    void handleEmptyTag(final Tag tag) {
        if (tag.getInfoUnits().size() == 0) {
            AvailableTags eContainer = (AvailableTags) tag.eContainer();
            if (eContainer != null) {
                eContainer.getTags().remove(tag);
            }
        }
    }

    private List<Tag> getTagsByInfoUnit(final InformationUnit unit) {
        List<Tag> returnValue = new ArrayList<Tag>();
        InformationUnitListItem adapter = (InformationUnitListItem) unit.getAdapter(InformationUnitListItem.class);
        EList<Tag> availableTags = ApplicationModelPool.getInstance().getModel().getAvailableTags().getTags();
        for (Tag tag : availableTags) {
            if (tag.getInfoUnits().contains(adapter.getId())) {
                returnValue.add(tag);
            }
        }
        return returnValue;
    }

    private Tag getTagByName(final String name) {
        EList<Tag> tags = ApplicationModelPool.getInstance().getModel().getAvailableTags().getTags();
        for (Tag tag : tags) {
            if (tag.getName().equals(name)) {
                return tag;
            }
        }
        return null;
    }

    public void handleChanged(final InformationUnit oldValue, final InformationUnit newValue) {
        List<String> splitCopy = Collections.EMPTY_LIST;
        List<String> split = Collections.EMPTY_LIST;
        if (newValue.getKeywords() != null) {
            split = new ArrayList<String>(Arrays.asList(StringUtils.split(newValue.getKeywords(), " ")));
            splitCopy = new ArrayList<String>(split);
        }
        List<Tag> tagsByInfoUnit = getTagsByInfoUnit(newValue);
        List<Tag> tagsByInfoUnitCopy = new ArrayList<Tag>(tagsByInfoUnit);
        for (Tag tag : tagsByInfoUnit) {
            if (split.contains(tag.getName())) {
                tagsByInfoUnitCopy.remove(tag);
                splitCopy.remove(tag.getName());
            }
        }
        handleNewTags(splitCopy, newValue);
        handleOldTags(tagsByInfoUnitCopy, newValue);
        for (Tag tag : tagsByInfoUnitCopy) {
            handleEmptyTag(tag);
        }
    }

    public void handleCreated(final InformationUnit newValue) {
        if (newValue.getKeywords() != null) {
            handleNewTags(new ArrayList<String>(Arrays.asList(StringUtils.split(newValue.getKeywords(), " "))), newValue);
        }
    }

    public void handleDeleted(final String informationUnitId) {
        List<Tag> tags = new ArrayList<Tag>(ApplicationModelPool.getInstance().getModel().getAvailableTags().getTags());
        for (Tag tag : tags) {
            List<String> infoUnits = new ArrayList<String>(tag.getInfoUnits());
            for (String string2 : infoUnits) {
                if (string2.equals(informationUnitId)) {
                    tag.getInfoUnits().remove(string2);
                    handleEmptyTag(tag);
                }
            }
        }
    }

    public void handleClean(final IProject project) {
        List<Tag> tags = new ArrayList<Tag>(ApplicationModelPool.getInstance().getModel().getAvailableTags().getTags());
        for (Tag tag : tags) {
            List<String> arrayList = new ArrayList<String>(tag.getInfoUnits());
            for (String string : arrayList) {
                InformationUnitListItem itemById = ApplicationModelPool.getInstance().getItemById(string, null);
                if (itemById == null || itemById.eResource() == null || project.equals(ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(itemById.eResource().getURI().toPlatformString(true))).getProject())) {
                    tag.getInfoUnits().remove(string);
                }
            }
            handleEmptyTag(tag);
        }
    }
}
