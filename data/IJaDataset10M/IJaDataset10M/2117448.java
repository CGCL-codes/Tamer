package astcentric.editor.common.view.tree;

import astcentric.editor.common.view.text.TextualLayoutArtistFactory;
import astcentric.structure.basic.ValidationContext;
import astcentric.structure.vl.Validator;

/**
 * Context for creating and managing visual tree nodes. 
 *
 */
public class TreeContext {

    private final ContentFactory _contentFactory;

    private final TreeNodeFrameFactory _frameFactory;

    private final TextualLayoutArtistFactory _layoutArtistFactory;

    private final Validator _validator;

    private final ValidationContext _validationContext;

    /**
   * Creates an instance for the specified factories, validator, and
   * validation context.
   * 
   * @param contentFactory Factory for creating the content of 
   *        a {@link TreeNode} from an AST node.
   * @param validator Node validator.
   * @param validationContext Validation context. 
   * @param frameFactory Factory which creates the visual frame of a
   *        a {@link TreeNode}.
   * @param layoutArtistFactory Factory for the 
   *        {@link astcentric.editor.common.view.graphic.LayoutArtist} used
   *        for layout of the content in cases of changed available width.
   */
    public TreeContext(ContentFactory contentFactory, Validator validator, ValidationContext validationContext, TreeNodeFrameFactory frameFactory, TextualLayoutArtistFactory layoutArtistFactory) {
        _contentFactory = contentFactory;
        _validator = validator;
        _validationContext = validationContext;
        _frameFactory = frameFactory;
        _layoutArtistFactory = layoutArtistFactory;
    }

    /**
   * Returns the content factory.
   */
    public ContentFactory getContentFactory() {
        return _contentFactory;
    }

    public Validator getValidator() {
        return _validator;
    }

    public ValidationContext getValidationContext() {
        return _validationContext;
    }

    /**
   * Returns the factory for {@link TreeNodeFrame TreeNodeFrames}.
   */
    public TreeNodeFrameFactory getFrameFactory() {
        return _frameFactory;
    }

    /**
   * Returns the layout artist for the layout of the node content.
   * @return
   */
    public TextualLayoutArtistFactory getLayoutArtistFactory() {
        return _layoutArtistFactory;
    }
}
