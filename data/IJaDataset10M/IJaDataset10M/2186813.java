package de.zefania.api.impl;

import org.w3c.dom.Document;
import de.zefania.api.BibleText;
import de.zefania.api.Position;
import de.zefania.api.Transformer;
import de.zefania.api.Vers;

public class BibleTextImpl implements BibleText {

    private Position position;

    private String modulId;

    private Document doc;

    public BibleTextImpl(final Document doc, final Position pos, final String modulId) {
        this.modulId = modulId;
        this.position = pos;
        this.doc = doc;
    }

    @Override
    public final String getModulId() {
        return this.modulId;
    }

    @Override
    public final Position getPosition() {
        return this.position;
    }

    @Override
    public final Vers[] getVersList() {
        return null;
    }

    @Override
    public final String transform(final Transformer transformer) {
        return transformer.transform(doc);
    }
}
