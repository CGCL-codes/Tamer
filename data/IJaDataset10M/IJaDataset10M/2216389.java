package game.report.srobjects;

import game.report.SRRenderer;

/**
 * Created by IntelliJ IDEA.
 * User: drchaj1
 * Date: Oct 1, 2009
 * Time: 3:46:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class SRHeader extends SRText {

    public SRHeader(String key, String text) {
        super(key, text);
    }

    @Override
    public ISRObjectRenderer getRenderer(SRRenderer renderer) {
        return renderer.createHeaderRenderer(this);
    }
}
