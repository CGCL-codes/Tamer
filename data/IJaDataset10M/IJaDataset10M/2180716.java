package org.jtools.util.props;

public interface PropertyStyle {

    String getReferencePrefix();

    String getReferenceSuffix();

    String getTempKeyPrefix();

    String getLocalKeyPrefix();

    String getCurrentPathProperty();

    String getInitialPathProperty();

    String getDefaultEntriesProperty();

    String getDefaultLinkProperty();

    String getFilenameProperty();
}
