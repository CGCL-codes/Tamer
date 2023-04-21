package cz.cuni.mff.ksi.jinfer.treeruledisplayer.logic;

import cz.cuni.mff.ksi.jinfer.base.objects.nodes.AbstractNamedNode;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import java.awt.Font;
import org.apache.commons.collections15.Transformer;

/**
 *
 * @author sviro
 */
class VertexFontTransformer implements Transformer<Regexp<? extends AbstractNamedNode>, Font> {

    @Override
    public Font transform(final Regexp<? extends AbstractNamedNode> regexp) {
        if (RegexpType.LAMBDA.equals(regexp.getType())) {
            return new Font(null, Font.BOLD, 20);
        }
        return new Font(null, Font.PLAIN, 12);
    }
}
