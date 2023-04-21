package net.liveseeds.eye.domination.model;

import net.liveseeds.eye.detailsview.model.ActionsNode;
import net.liveseeds.eye.detailsview.model.ScenarioNode;
import net.liveseeds.eye.detailsview.model.WorldTreeNode;
import net.liveseeds.eye.domination.Amount2LiveSeed;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class RatingPositionNode extends DefaultMutableTreeNode {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(RatingPositionNode.class.getName());

    private final Amount2LiveSeed amount2LiveSeed;

    public RatingPositionNode(final DefaultTreeModel model, final int position, final Amount2LiveSeed amount2LiveSeed, final long ratingCreationTime, final int ratingCreationPopulation) {
        super(getName(position, amount2LiveSeed, ratingCreationPopulation, ratingCreationTime));
        this.amount2LiveSeed = amount2LiveSeed;
        add(new ActionsNode(model, amount2LiveSeed.getLiveSeed().getActionSlotSet()));
        add(new ScenarioNode(model, amount2LiveSeed.getLiveSeed().getScenario()));
    }

    private static String getName(final int position, final Amount2LiveSeed amount2LiveSeed, final int ratingCreationPopulation, final long ratingCreationTime) {
        final String attributesString = amount2LiveSeed.getLiveSeed().getGenotype().getAttributesString();
        if (attributesString != null && attributesString.length() > 0) {
            return MessageFormat.format(RESOURCE_BUNDLE.getString("name.tagged"), new Object[] { new Integer(position), new Integer(amount2LiveSeed.getAmount()), new Double(amount2LiveSeed.getAmount() * 100d / ratingCreationPopulation), new Long(getAge(ratingCreationTime, amount2LiveSeed)), new Integer(amount2LiveSeed.getLiveSeed().getScenario().getLength()), attributesString });
        } else {
            return MessageFormat.format(RESOURCE_BUNDLE.getString("name"), new Object[] { new Integer(position), new Integer(amount2LiveSeed.getAmount()), new Double(amount2LiveSeed.getAmount() * 100d / ratingCreationPopulation), new Long(getAge(ratingCreationTime, amount2LiveSeed)), new Integer(amount2LiveSeed.getLiveSeed().getScenario().getLength()) });
        }
    }

    public Amount2LiveSeed getAmount2LiveSeed() {
        return amount2LiveSeed;
    }

    private static long getAge(final long ratingCreationTime, final Amount2LiveSeed amount2LiveSeed) {
        long age = ratingCreationTime - amount2LiveSeed.getLiveSeed().getGenotype().getCreationTime();
        if (age < 0) {
            age = 0;
        }
        return age;
    }

    public void cleanUp() {
        for (int i = 0; i < getChildCount(); i++) {
            ((WorldTreeNode) getChildAt(i)).cleanUp();
        }
    }
}
