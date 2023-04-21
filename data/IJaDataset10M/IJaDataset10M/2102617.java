package org.freebxml.omar.common.exceptions;

import org.freebxml.omar.common.CommonResourceBundle;

/**
 * The Exception that should be thrown when the RepositoryItem with specified lid and versionName
 * cannot be found.
 *
 * @author Adrian Chong
*/
public class RepositoryItemNotFoundException extends ObjectNotFoundException {

    protected static CommonResourceBundle resourceBundle = CommonResourceBundle.getInstance();

    private String lid;

    private String versionName;

    public RepositoryItemNotFoundException(String lid, String versionName) {
        this(resourceBundle.getString("message.repositoryItemNotFoundException", new String[] { lid, versionName }));
        this.lid = lid;
        this.versionName = versionName;
    }

    public RepositoryItemNotFoundException(String msg) {
        super(msg);
    }

    public String getLid() {
        return lid;
    }

    public String getVersionName() {
        return versionName;
    }
}
