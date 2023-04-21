package genj.source;

import genj.gedcom.Grammar;
import genj.gedcom.TagPath;
import genj.util.swing.ImageIcon;
import genj.view.View;
import genj.view.ViewFactory;

/**
 * A factory for source view
 */
public class SourceViewFactory implements ViewFactory {

    private static final ImageIcon IMG = Grammar.V55.getMeta(new TagPath("SOUR")).getImage();

    public View createView() {
        return new SourceView();
    }

    @Override
    public ImageIcon getImage() {
        return IMG;
    }

    @Override
    public String getTitle() {
        return SourceView.RESOURCES.getString("title");
    }
}
