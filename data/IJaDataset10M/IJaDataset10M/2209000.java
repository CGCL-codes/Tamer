package com.liferay.portal.upgrade.v4_4_0;

import com.liferay.portal.upgrade.UpgradeException;
import com.liferay.portal.upgrade.UpgradeProcess;
import com.liferay.portal.upgrade.util.BaseUpgradeColumnImpl;
import com.liferay.portal.upgrade.util.DefaultUpgradeTableImpl;
import com.liferay.portal.upgrade.util.TempUpgradeColumnImpl;
import com.liferay.portal.upgrade.util.UpgradeColumn;
import com.liferay.portal.upgrade.util.UpgradeTable;
import com.liferay.portal.upgrade.v4_4_0.util.DLFileEntryTitleColumnImpl;
import com.liferay.portal.upgrade.v4_4_0.util.DLFolderNameColumnImpl;
import com.liferay.portlet.documentlibrary.model.impl.DLFileEntryModelImpl;
import com.liferay.portlet.documentlibrary.model.impl.DLFolderModelImpl;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <a href="UpgradeDocumentLibrary.java.html"><b><i>View Source</i></b></a>
 *
 * @author Alexander Chow
 *
 */
public class UpgradeDocumentLibrary extends UpgradeProcess {

    public void upgrade() throws UpgradeException {
        _log.info("Upgrading");
        try {
            doUpgrade();
        } catch (Exception e) {
            throw new UpgradeException(e);
        }
    }

    protected void doUpgrade() throws Exception {
        UpgradeColumn groupIdColumn = new TempUpgradeColumnImpl("groupId");
        UpgradeColumn parentFolderIdColumn = new TempUpgradeColumnImpl("parentFolderId");
        DLFolderNameColumnImpl dlFolderNameColumn = new DLFolderNameColumnImpl(groupIdColumn, parentFolderIdColumn);
        UpgradeTable upgradeTable = new DefaultUpgradeTableImpl(DLFolderModelImpl.TABLE_NAME, DLFolderModelImpl.TABLE_COLUMNS, groupIdColumn, parentFolderIdColumn, dlFolderNameColumn);
        upgradeTable.updateTable();
        Set<String> distinctNames = dlFolderNameColumn.getDistintNames();
        UpgradeColumn folderIdColumn = new TempUpgradeColumnImpl("folderId");
        UpgradeColumn nameColumn = new TempUpgradeColumnImpl("name");
        BaseUpgradeColumnImpl dlFileEntryTitleColumn = new DLFileEntryTitleColumnImpl(groupIdColumn, folderIdColumn, nameColumn, distinctNames);
        upgradeTable = new DefaultUpgradeTableImpl(DLFileEntryModelImpl.TABLE_NAME, DLFileEntryModelImpl.TABLE_COLUMNS, folderIdColumn, nameColumn, dlFileEntryTitleColumn);
        upgradeTable.updateTable();
    }

    private static Log _log = LogFactory.getLog(UpgradeDocumentLibrary.class);
}
