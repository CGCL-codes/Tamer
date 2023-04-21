package com.sitescape.team.module.folder.processor;

import java.util.Map;
import com.sitescape.team.domain.Definition;
import com.sitescape.team.domain.Folder;
import com.sitescape.team.domain.FolderEntry;
import com.sitescape.team.module.binder.processor.EntryProcessor;
import com.sitescape.team.module.file.WriteFilesException;
import com.sitescape.team.module.shared.InputDataAccessor;
import com.sitescape.team.security.AccessControlException;

/**
 * <code>ForumCoreProcessor</code> is a model processor for forum, which
 * defines a set of core operations around forum. 
 * 
 * @author Jong Kim
 */
public interface FolderCoreProcessor extends EntryProcessor {

    public FolderEntry addReply(FolderEntry parent, Definition def, InputDataAccessor inputData, Map fileItems, Map options) throws AccessControlException, WriteFilesException;

    public Map getEntryTree(Folder parentFolderId, FolderEntry entry) throws AccessControlException;
}
