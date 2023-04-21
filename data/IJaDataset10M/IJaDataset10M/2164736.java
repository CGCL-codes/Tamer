package com.google.gdata.data.appsforyourdomain.provisioning;

import com.google.gdata.data.BaseEntry;
import com.google.gdata.data.Category;
import com.google.gdata.data.ExtensionDescription;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;
import com.google.gdata.data.appsforyourdomain.Login;
import com.google.gdata.data.appsforyourdomain.Nickname;
import com.google.gdata.util.Namespaces;

/**
 * 
 * 
 */
@Kind.Term(NicknameEntry.NICKNAME_KIND)
public class NicknameEntry extends BaseEntry<NicknameEntry> {

    /**
   * Kind term value for Nickname category labels.
   */
    public static final String NICKNAME_KIND = com.google.gdata.data.appsforyourdomain.Namespaces.APPS_PREFIX + "nickname";

    /**
   * Kind category used to label feeds or entries that have Nickname
   * extension data.
   */
    public static final Category NICKNAME_CATEGORY = new Category(Namespaces.gKind, NICKNAME_KIND);

    /**
   * Constructs a new empty NicknameEntry with the appropriate kind category
   * to indicate that it is an nickname.
   */
    public NicknameEntry() {
        super();
        getCategories().add(NICKNAME_CATEGORY);
    }

    /**
   * Constructs a new NicknameEntry by doing a shallow copy of data from an
   * existing BaseEntry intance.
   */
    public NicknameEntry(BaseEntry sourceEntry) {
        super(sourceEntry);
        getCategories().add(NICKNAME_CATEGORY);
    }

    @Override
    public void declareExtensions(ExtensionProfile extensionProfile) {
        ExtensionDescription description = Nickname.getDefaultDescription();
        description.setRequired(true);
        extensionProfile.declare(NicknameEntry.class, description);
        description = Login.getDefaultDescription();
        description.setRequired(true);
        extensionProfile.declare(NicknameEntry.class, description);
        extensionProfile.declareAdditionalNamespace(com.google.gdata.data.appsforyourdomain.Namespaces.APPS_NAMESPACE);
    }

    public Nickname getNickname() {
        return getExtension(Nickname.class);
    }

    public Login getLogin() {
        return getExtension(Login.class);
    }
}
