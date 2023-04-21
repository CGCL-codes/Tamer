package com.google.gdata.data.appsforyourdomain.provisioning;

import com.google.gdata.data.BaseFeed;
import com.google.gdata.data.ExtensionProfile;
import com.google.gdata.data.Kind;

/**
 * 
 * 
 */
@Kind.Term(UserEntry.USER_KIND)
public class UserFeed extends BaseFeed<UserFeed, UserEntry> {

    /**
   * Constructs a new {@code UserFeed} instance that is parameterized to
   * contain {@code UserEntry} instances.
   */
    public UserFeed() {
        super(UserEntry.class);
        getCategories().add(UserEntry.USER_CATEGORY);
    }

    /**
   * Constructs a new {@code UserFeed} instance that is initialized using
   * data from another BaseFeed instance.
   */
    public UserFeed(BaseFeed sourceFeed) {
        super(UserEntry.class, sourceFeed);
        getCategories().add(UserEntry.USER_CATEGORY);
    }

    @Override
    public void declareExtensions(ExtensionProfile extensionProfile) {
        super.declareExtensions(extensionProfile);
    }
}
