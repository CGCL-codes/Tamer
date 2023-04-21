package ch.jester.ui.round.form.contentprovider;

import java.util.ArrayList;
import java.util.List;
import messages.Messages;
import ch.jester.model.Category;
import ch.jester.model.Round;
import ch.jester.ui.round.editors.ResultController;
import ch.jester.ui.round.form.ZestDataNode;

/**
 * Hilfklasse um die ZestNodes zu verbinden.
 *
 */
public class CategoryNodeModelContentProvider extends RoundNodeModelContentProvider {

    public CategoryNodeModelContentProvider(ResultController controller) {
        super(controller);
    }

    private ZestUtil util = new ZestUtil();

    private List<ZestDataNode> nodes;

    public void setInput(Object input) {
        mInput = input;
        buildCategory((Category) input);
    }

    private void buildCategory(Category input) {
        nodes = new ArrayList<ZestDataNode>();
        for (Round r : input.getRounds()) {
            ZestDataNode parentRound = new ZestDataNode(r.getId() + "", Messages.TournamentLabelProvider_lbl_round + r.getNumber(), r);
            nodes.add(parentRound);
            super.buildRound(r);
            List<ZestDataNode> roundNodes = super.getParentNodes();
            util.connect(parentRound, roundNodes);
            util.establishConnections();
            nodes.addAll(super.getAllNodes());
        }
        setNode(nodes);
    }
}
