package com.liferay.portal.upgrade.v4_3_0.util;

import com.liferay.portal.upgrade.util.BaseUpgradeColumnImpl;
import com.liferay.portal.upgrade.util.UpgradeColumn;
import com.liferay.portal.upgrade.util.ValueMapper;

/**
 * <a href="JournalTemplateSmallImageIdUpgradeColumnImpl.java.html"><b><i>View
 * Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class JournalTemplateSmallImageIdUpgradeColumnImpl extends BaseUpgradeColumnImpl {

    public JournalTemplateSmallImageIdUpgradeColumnImpl(UpgradeColumn companyIdColumn, UpgradeColumn groupIdColumn, UpgradeColumn templateIdColumn, ValueMapper imageIdMapper) {
        super("smallImageId");
        _companyIdColumn = companyIdColumn;
        _groupIdColumn = groupIdColumn;
        _templateIdColumn = templateIdColumn;
        _imageIdMapper = imageIdMapper;
    }

    public Object getNewValue(Object oldValue) throws Exception {
        String companyId = (String) _companyIdColumn.getOldValue();
        Long groupId = (Long) _groupIdColumn.getOldValue();
        String oldTemplateId = (String) _templateIdColumn.getOldValue();
        String oldImageId = companyId + ".journal.template." + groupId + "." + oldTemplateId + ".small";
        return _imageIdMapper.getNewValue(oldImageId);
    }

    private UpgradeColumn _companyIdColumn;

    private UpgradeColumn _groupIdColumn;

    private UpgradeColumn _templateIdColumn;

    private ValueMapper _imageIdMapper;
}
