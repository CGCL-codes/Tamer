package net.sf.xsnapshot.ant;

import java.util.Collection;
import java.util.Iterator;
import net.sf.xsnapshot.tagshandler.XSnapshotTagsHandler;
import xdoclet.TemplateSubTask;
import xdoclet.XDocletException;
import xjavadoc.XClass;
import xjavadoc.XTag;

/**
 * Superclass for XSnapshot subtasks tasks that iterate over source
 * classes and snapshot-class tags for each source file.  May generates
 * multiple outputs for one input class file, one per snapshot-class tag.
 *
 * <p> subtasks extend this class and must define the method
 * getTemplate.
 *
 * @author        Bill Schneider
 */
public abstract class XSnapshotSubTask extends TemplateSubTask {

    private String m_currentSnapshotClass;

    private String m_currentSnapshotName;

    private boolean m_isCollection;

    /**
   * Default Constructor
   */
    public XSnapshotSubTask() {
        setDestinationFile("{0}Snapshot.java");
        setTemplateURL(getClass().getResource(getTemplate()));
    }

    /**
   * This method will be called for each class processed.  
   * This method will iterate over all of the xsnapshot.snapshot-class
   * tags and run the template engine for each one.
   *
   * @param clazz                 Describe what the parameter does
   * @exception XDocletException
   */
    protected void generateForClass(XClass clazz) throws XDocletException {
        Collection snapshots = getCurrentClass().getDoc().getTags(XSnapshotTagsHandler.SNAPSHOT_CLASS_TAG);
        for (Iterator i = snapshots.iterator(); i.hasNext(); ) {
            XTag tag = (XTag) i.next();
            XSnapshotTagsHandler.setCurrentClassTag(tag);
            m_currentSnapshotClass = XSnapshotTagsHandler.getSnapshotClass(tag);
            m_currentSnapshotName = XSnapshotTagsHandler.getSnapshotName(tag);
            if (!skipSnapshot(tag)) {
                super.generateForClass(clazz);
            }
        }
    }

    /**
   * Determines whether an individual snapshot should be skipped, based on 
   * the Javadoc tag attributes.   This implementation always returns
   * false.  Subclasses may extend and override with their own logic. 
   *
   * @param tag the Javadoc tag 
   * @return always false; subclasses may override to return true if
   *   the snapshot described by this tag should be skipped
   */
    protected boolean skipSnapshot(XTag tag) {
        return false;
    }

    protected abstract String getTemplate();

    public String getCurrentSnapshotClass() {
        return m_currentSnapshotClass;
    }

    public String getCurrentSnapshotName() {
        return m_currentSnapshotName;
    }
}
