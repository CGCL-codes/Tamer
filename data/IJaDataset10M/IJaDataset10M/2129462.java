package visreed.extension.regex.swing.nodebar;

import visreed.extension.regex.model.tag.RegexTag;
import visreed.model.VisreedWholeGraph;
import visreed.swing.nodebar.VisreedNodeToolBarIconData;
import visreed.swing.nodebar.VisreedNodeToolBar;

/**
 * The default tool bar does not support drag, so use a JList with custom
 * renderer instead.
 * @author Xiaoyu Guo
 */
public class RegexNodeToolBar extends VisreedNodeToolBar {

    private static final long serialVersionUID = 8675362402938781706L;

    public RegexNodeToolBar(VisreedWholeGraph wg) {
        super(wg, PREDEFINED_NODE_DATA);
    }

    private static final VisreedNodeToolBarIconData[] PREDEFINED_NODE_DATA = new VisreedNodeToolBarIconData[] { new VisreedNodeToolBarIconData("Sequence", "/images/toolbar_node_seq.png", RegexTag.SEQUENCE), new VisreedNodeToolBarIconData("Alternation", "/images/toolbar_node_alt.png", RegexTag.ALTERNATION), new VisreedNodeToolBarIconData("Kleene +", "/images/toolbar_node_kln+.png", RegexTag.KLEENE_PLUS), new VisreedNodeToolBarIconData("Kleene *", "/images/toolbar_node_klnstar.png", RegexTag.KLEENE_STAR), new VisreedNodeToolBarIconData("Terminal", "/images/toolbar_node_ter.png", RegexTag.TERMINAL) };
}
