package org.deft.repository.repositorymanager.actions;

import org.deft.repository.datamodel.Chapter;
import org.deft.repository.datamodel.Folder;
import org.deft.repository.datamodel.Fragment;
import org.deft.repository.datamodel.Project;
import org.deft.repository.event.FragmentUpdateEvent;
import org.deft.repository.event.RepositoryUpdateEvent.UpdateType;
import org.deft.repository.repositorymanager.ContentManager;
import org.deft.repository.repositorymanager.DataStorageManager;

public class CreateChapterAction extends AbstractRepositoryAction {

    private ContentManager contentManager;

    private DataStorageManager dataStorageManager;

    private Project project;

    private Folder parentFolder;

    private String name;

    private String chapterType;

    public CreateChapterAction(ContentManager cm, DataStorageManager dsm, Project project, String name, String chapterType) {
        this.contentManager = cm;
        this.dataStorageManager = dsm;
        this.project = project;
        this.name = name;
        this.chapterType = chapterType;
    }

    public CreateChapterAction(ContentManager cm, DataStorageManager dsm, Folder parent, String name, String chapterType) {
        this.contentManager = cm;
        this.dataStorageManager = dsm;
        this.parentFolder = parent;
        this.name = name;
        this.chapterType = chapterType;
        this.project = contentManager.getProjectOf(parentFolder);
    }

    @Override
    public void execute() {
        Chapter chapter = contentManager.createChapter(project, name, chapterType);
        if (parentFolder != null) {
            contentManager.setParentFolder(chapter, parentFolder);
        }
        executeSuccessful = dataStorageManager.createChapterContent(chapter);
        if (executeSuccessful) {
            createdObject = chapter;
        } else {
            contentManager.remove(chapter);
        }
    }

    @Override
    public FragmentUpdateEvent createEvent(boolean direct) {
        Fragment fragment = (Fragment) createdObject;
        FragmentUpdateEvent event = new FragmentUpdateEvent(fragment, UpdateType.CREATED, direct);
        return event;
    }
}
