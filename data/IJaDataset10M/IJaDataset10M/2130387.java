package org.makagigafx.media;

import org.makagigafx.Document;
import org.makagigafx.DocumentHandler;
import org.makagigafx.DocumentPane;

public final class MediaDocumentHandler extends DocumentHandler {

    public MediaDocumentHandler() {
        super("Media", "avi", "mp3");
        setReadOnly(true);
    }

    @Override
    protected DocumentPane createDocumentPane(final Document document) {
        return new MediaDocumentPane(document);
    }
}
