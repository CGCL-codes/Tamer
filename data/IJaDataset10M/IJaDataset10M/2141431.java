package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractStructuralNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import java.awt.Color;
import java.awt.Paint;
import java.util.List;
import org.apache.commons.collections15.Transformer;

/**
 * Transformer for Rule Tree Vertex which transform {@link Regexp} into Vertex color.
 * @author sviro
 */
public class VertexColorTransformer implements Transformer<Regexp<? extends AbstractNamedNode>, Paint> {

    private final Color rootColor;

    private final Color tokenColor;

    private final Color concatColor;

    private final Color alterColor;

    private final Color permutColor;

    private final Color lambdaColor;

    private final Color simpleDataColor;

    private final Color attributeColor;

    private final List<Regexp<AbstractStructuralNode>> roots;

    /**
   * Default contructor.
   * @param roots List of root Regexp of each rule tree.
   */
    public VertexColorTransformer(final List<Regexp<AbstractStructuralNode>> roots) {
        this.rootColor = Utils.getColorProperty(Utils.ROOT_COLOR_PROP, Utils.ROOT_COLOR_DEFAULT);
        this.tokenColor = Utils.getColorProperty(Utils.ELEMENT_COLOR_PROP, Utils.ELEMENT_COLOR_DEFAULT);
        this.concatColor = Utils.getColorProperty(Utils.CONCAT_COLOR_PROP, Utils.CONCAT_COLOR_DEFAULT);
        this.alterColor = Utils.getColorProperty(Utils.ALTER_COLOR_PROP, Utils.ALTER_COLOR_DEFAULT);
        this.permutColor = Utils.getColorProperty(Utils.PERMUT_COLOR_PROP, Utils.PERMUT_COLOR_DEFAULT);
        this.lambdaColor = Utils.getColorProperty(Utils.LAMBDA_COLOR_PROP, Utils.LAMBDA_COLOR_DEFAULT);
        this.simpleDataColor = Utils.getColorProperty(Utils.SIMPLE_DATA_COLOR_PROP, Utils.SIMPLE_DATA_COLOR_DEFAULT);
        this.attributeColor = Utils.getColorProperty(Utils.ATTRIBUTE_COLOR_PROP, Utils.ATTRIBUTE_COLOR_DEFAULT);
        this.roots = roots;
    }

    @Override
    public Paint transform(final Regexp<? extends AbstractNamedNode> regexp) {
        switch(regexp.getType()) {
            case LAMBDA:
                return lambdaColor;
            case TOKEN:
                if (roots.contains(regexp)) {
                    return rootColor;
                } else if (regexp.getContent() instanceof AbstractStructuralNode) {
                    if (((AbstractStructuralNode) regexp.getContent()).isSimpleData()) {
                        return simpleDataColor;
                    } else {
                        return tokenColor;
                    }
                } else {
                    return attributeColor;
                }
            case ALTERNATION:
                return alterColor;
            case CONCATENATION:
                return concatColor;
            case PERMUTATION:
                return permutColor;
            default:
                return null;
        }
    }
}
