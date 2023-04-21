package org.seamantics.session.api;

import java.util.List;
import org.jboss.seam.annotations.AutoCreate;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.international.StatusMessage.Severity;
import org.jboss.seam.log.Log;
import org.seamantics.dao.SectionDao;
import org.seamantics.model.impl.AbstractArticle;
import org.seamantics.model.impl.Section;
import org.seamantics.session.gui.SectionTree;
import org.seamthebits.measure.MeasureCalls;

@Name("sectionService")
@AutoCreate
@MeasureCalls
public class SectionService extends AbstractFolderService<Section> {

    @Logger
    Log log;

    @In
    SectionTree sectionTree;

    @In
    SectionDao sectionDao;

    public void removeSection(Section section) {
        log.info("Deleting section {0}", section.getPath());
        sectionDao.remove(section.getPath());
        if (sectionTree.getSelectedSection().equals(section)) {
            sectionTree.setSelectedSection(section.getParent());
        }
    }

    public void removeArticle(Section section, AbstractArticle article) {
        log.info("Removing article {0} from section {1}", article.getName(), section.getPath());
        try {
            section.getArticles().remove(article);
            sectionDao.update(section, "*", 0);
        } catch (Exception e) {
            StatusMessages.instance().addFromResourceBundle(Severity.ERROR, "error.delete.article", e.getLocalizedMessage());
            e.printStackTrace();
        }
    }

    public void rename(Section section) {
        Section beforeSection = null;
        if (section.getParent() != null) {
            List<Section> containingFolder = section.getParent().getSections();
            int indexOfSelectedFolder = containingFolder.indexOf(section);
            if (indexOfSelectedFolder < containingFolder.size() - 1) {
                beforeSection = containingFolder.get(indexOfSelectedFolder + 1);
            }
        }
        sectionDao.update(section, "none", 0);
        if (beforeSection != null) {
            sectionDao.orderBefore(section, beforeSection);
        }
        log.info("Folder renamed: {0}", section.getName());
    }

    public void add() {
        Section section = new Section("NewSection");
        sectionTree.getSelectedSection().getFolders().add(section);
        log.info("Adding folder {0}", section.getName());
        sectionDao.create(sectionTree.getSelectedSection(), section);
    }
}
