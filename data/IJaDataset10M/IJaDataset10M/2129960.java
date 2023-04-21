package org.gocha.gef.storage;

import org.gocha.gef.Dot;
import org.gocha.gef.glyph.LineGlyph;

/**
 * @author Камнев Георгий Павлович
 */
public class LineGlyphWriter implements ObjectLinksBuilder, ObjectLinksReader {

    @Override
    public String genScriptSetLinksOf(Object obj, ScriptWriteHelper helper) {
        if (obj == null || !(obj instanceof LineGlyph)) return null;
        LineGlyph lSrc = (LineGlyph) obj;
        String name = helper.genName(lSrc);
        String endl = helper.endl();
        String script = "";
        for (Dot d : lSrc.getDots()) {
            script += name + ".getDots().add( " + helper.genName(d) + ");";
            script += endl;
        }
        script += helper.callMethodStatement(obj, "setClosedLine", lSrc.isClosedLine());
        script += helper.callMethodStatement(obj, "setFillBody", lSrc.isFillBody());
        return script;
    }

    @Override
    public Iterable readInnerObjectsOf(Object obj) {
        if (obj == null || !(obj instanceof LineGlyph)) return null;
        LineGlyph lGlyph = (LineGlyph) obj;
        return lGlyph.getDots();
    }
}
