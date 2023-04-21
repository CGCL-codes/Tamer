package org.lcx.scrumvision.core;

import java.util.Date;
import org.eclipse.mylyn.tasks.core.AbstractAttributeFactory;
import org.eclipse.mylyn.tasks.core.RepositoryTaskAttribute;

/**
 * @author Laurent Carbonnaux
 */
public class ScrumVisionAttributeFactory extends AbstractAttributeFactory {

    private static final long serialVersionUID = 2865601354030381164L;

    @Override
    public String mapCommonAttributeKey(String key) {
        if (key.equals(RepositoryTaskAttribute.USER_ASSIGNED)) {
            return ScrumVisionAttribute.OWNER.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.PRIORITY)) {
            return ScrumVisionAttribute.PRIORITY.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.STATUS)) {
            return ScrumVisionAttribute.STATUS.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.SUMMARY)) {
            return ScrumVisionAttribute.SUMMARY.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.TASK_KEY)) {
            return ScrumVisionAttribute.TASKID.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.DESCRIPTION)) {
            return ScrumVisionAttribute.COMMENT.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.ADD_SELF_CC)) {
            return ScrumVisionAttribute.CC.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.NEW_CC)) {
            return ScrumVisionAttribute.CC.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.REMOVE_CC)) {
            return ScrumVisionAttribute.CC.getKeyString();
        } else if (key.equals(RepositoryTaskAttribute.USER_CC)) {
            return ScrumVisionAttribute.CC.getKeyString();
        } else {
            return key;
        }
    }

    @Override
    public boolean isHidden(String key) {
        try {
            if (key.startsWith(ScrumVisionAttribute.LN.getKeyString())) {
                String num = key.substring(1);
                try {
                    Integer.valueOf(num);
                    return ScrumVisionAttribute.LN.isHidden();
                } catch (NumberFormatException e) {
                }
            }
            return ScrumVisionAttribute.valueOf(key.trim().toUpperCase()).isHidden();
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Override
    public boolean isReadOnly(String key) {
        try {
            if (key.startsWith(ScrumVisionAttribute.LN.getKeyString())) {
                String num = key.substring(1);
                try {
                    Integer.valueOf(num);
                    return ScrumVisionAttribute.LN.isReadOnly();
                } catch (NumberFormatException e) {
                }
            }
            return ScrumVisionAttribute.valueOf(key.trim().toUpperCase()).isReadOnly();
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Override
    public String getName(String key) {
        try {
            if (key.startsWith(ScrumVisionAttribute.LN.getKeyString())) {
                String num = key.substring(1);
                try {
                    Integer.valueOf(num);
                    return "Level " + num + " (L" + num + "):";
                } catch (NumberFormatException e) {
                }
            }
            return ScrumVisionAttribute.valueOf(key.trim().toUpperCase()).getPrettyName();
        } catch (IllegalArgumentException e) {
            return "<unknown>";
        }
    }

    @Override
    public Date getDateForAttributeType(String attributeKey, String dateString) {
        return null;
    }
}
