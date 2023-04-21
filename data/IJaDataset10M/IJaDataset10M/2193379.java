package net.liveseeds.base.world;

import net.liveseeds.base.cell.Cell;
import net.liveseeds.base.cell.CellListener;
import net.liveseeds.base.liveseed.LiveSeed;
import net.liveseeds.base.LiveSeedsException;
import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class RadFlowResourceBalance extends AbstractResourceBalance {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(RadFlowResourceBalance.class.getName());

    private World world;

    public void start(final World world) {
        this.world = world;
        for (int i = 0; i < world.getCells().length; i++) {
            final Cell[] row = world.getCells()[i];
            for (int j = 0; j < row.length; j++) {
                final Cell cell = row[j];
                cell.addCellListener(new ResourceBalanceCellListener(cell));
            }
        }
    }

    private final class ResourceBalanceCellListener implements CellListener {

        private final Cell cell;

        ResourceBalanceCellListener(final Cell cell) {
            this.cell = cell;
        }

        public void liveSeedAdded(final LiveSeed liveSeed) {
        }

        public void liveSeedRemoved(final LiveSeed liveSeed) {
        }

        public void resourceChanged() {
            try {
                if (cell.getResourceAmount() > getResourceBalanceTreshold()) {
                    if (cell.isRadioactive()) {
                        WorldUtil.flowRadioactiveCells(world, world.getRadioactiveCellsAmount());
                    }
                }
            } catch (LiveSeedsException e) {
                throw new InternalError(MessageFormat.format(RESOURCE_BUNDLE.getString("exception.internal"), new Object[] { e }));
            }
        }
    }
}
