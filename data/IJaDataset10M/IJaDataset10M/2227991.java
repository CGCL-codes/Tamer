package com.limegroup.gnutella.gui.xml.editor;

public class PublisherMetaDataSaver extends MetaDataSaver {

    public PublisherMetaDataSaver(CCPublisherTabbedPane pane) {
        super(pane);
    }

    public boolean saveMetaData() {
        CCPublisherTabbedPane publisherPane = (CCPublisherTabbedPane) _pane;
        if (!publisherPane.checkInput() || !publisherPane.reserveIdentifier()) return false; else {
            doSave();
            return true;
        }
    }
}
