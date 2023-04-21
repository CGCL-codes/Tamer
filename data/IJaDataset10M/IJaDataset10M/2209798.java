package cz.cuni.mff.ksi.jinfer.autoeditor.automatonvisualizer.layouts.vyhnanovska;

import cz.cuni.mff.ksi.jinfer.base.automaton.Automaton;
import cz.cuni.mff.ksi.jinfer.base.automaton.State;
import cz.cuni.mff.ksi.jinfer.base.automaton.Step;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import org.apache.commons.collections15.Transformer;
import edu.uci.ics.jung.graph.Graph;
import org.apache.log4j.Logger;

/** 
 * Transforms {@link State} to a {@link Point2D} on which it should be plotted.
 *
 * Most of the code is the original code by Julie Vyhnanovska. May need some
 * refactoring to fit our conventions.
 *
 * @author Julie Vyhnanovska, rio
 *
 */
public class AutomatonLayoutTransformer<T> implements Transformer<State<T>, Point2D> {

    private static final Logger LOG = Logger.getLogger(AutomatonLayoutTransformer.class);

    private final int minXsize;

    private final int minYsize;

    private final int squareSize;

    private final Dimension gridDimension;

    private final StateMapping<T> stateGridMapping;

    private static final double FILL_FACTOR = 3;

    private final Automaton<T> automaton;

    /**
   * Constructs transformer according to specified values.
   *
   * @param minXsize Minimal vertical size of a grid in squares.
   * @param minYsize Minimal horizontal size of a grid in squares.
   * @param square_size Size of one square in the grid in pixels.
   * @param graph Graph created from Automaton.
   * @param automaton
   */
    public AutomatonLayoutTransformer(final int minXsize, final int minYsize, final int square_size, final Graph<State<T>, Step<T>> graph, final Automaton<T> automaton) {
        this.automaton = automaton;
        this.minXsize = minXsize;
        this.minYsize = minYsize;
        this.squareSize = square_size;
        final int vertexCount = graph.getVertexCount();
        gridDimension = computeGridDimension((int) Math.round(vertexCount * FILL_FACTOR));
        stateGridMapping = new StateMapping<T>(vertexCount);
    }

    /**
   * Getter for a dimension of the grid
   *
   * @return Dimension of the grid in pixels.
   */
    public Dimension getDimension() {
        return new Dimension(gridDimension.width * squareSize, gridDimension.height * squareSize);
    }

    @Override
    public Point2D transform(final State<T> state) {
        LOG.info("transform location of " + state);
        if (state.equals(automaton.getInitialState())) {
            final Coordinate statePosition = new Coordinate(1, 1);
            stateGridMapping.addStateCoordinate(state, statePosition);
            return getPoint(statePosition);
        }
        Coordinate prev = new Coordinate(1, 1);
        for (final Step<T> backEdge : automaton.getReverseDelta().get(state)) {
            final Coordinate drawnStateCoordinate = stateGridMapping.getStateCoordinate(backEdge.getSource());
            if (drawnStateCoordinate != null) {
                prev = drawnStateCoordinate;
                break;
            }
        }
        Coordinate nextI = prev;
        LOG.info("nextI: " + nextI.getX() + " " + nextI.getY());
        while (stateGridMapping.getStateAtCoordinate(nextI) != null || (nextI.equals(new Coordinate(1, 1)))) {
            LOG.info("obsazeno!");
            nextI = getNextCoordinate(prev, nextI, gridDimension);
            if (nextI != null) {
                LOG.info("nextI: " + nextI.getX() + " " + nextI.getY());
                if (nextI.getX() < 1 || nextI.getY() < 1) {
                    LOG.error("invalid grid index: " + nextI.getX() + " " + nextI.getY());
                    return null;
                }
            }
        }
        if (nextI != null) {
            stateGridMapping.addStateCoordinate(state, nextI);
            return getPoint(nextI);
        }
        return null;
    }

    private Point2D getPoint(final Coordinate i) {
        final double x = i.getX() * squareSize - squareSize / 2;
        final double y = i.getY() * squareSize - squareSize / 2;
        return new Point2D.Double(x, y);
    }

    private Dimension computeGridDimension(final int minGridSize) {
        final Dimension dimension = new Dimension();
        if (minGridSize < (minXsize * minYsize)) {
            dimension.setSize(minXsize, minXsize);
        } else {
            dimension.width = (int) Math.round(Math.sqrt(minGridSize * minYsize / minXsize));
            dimension.height = (int) Math.floor(minGridSize / dimension.width) + 1;
        }
        return dimension;
    }

    private Coordinate getNextCoordinate(final Coordinate startingCoordinate, final Coordinate actualCoordinate, final Dimension gridDimension) {
        final Coordinate distance = new Coordinate(actualCoordinate.getX() - startingCoordinate.getX(), actualCoordinate.getY() - startingCoordinate.getY());
        LOG.info("distance: " + distance.getX() + ":" + distance.getY());
        final int index = Math.max(Math.abs(distance.getX()), Math.abs(distance.getY()));
        LOG.info("index: " + index);
        if (distance.equals(new Coordinate(index, -index))) {
            return goNewIndex(startingCoordinate, gridDimension, index + 1);
        } else {
            return goNextI(startingCoordinate, actualCoordinate, gridDimension);
        }
    }

    private Coordinate goNextI(final Coordinate startingCoordinate, final Coordinate actualCoordinate, final Dimension gridDimension) {
        LOG.info("goNextI");
        final Coordinate point = new Coordinate(actualCoordinate.getX() - startingCoordinate.getX(), actualCoordinate.getY() - startingCoordinate.getY());
        LOG.info("point: " + point.getX() + ":" + point.getY());
        final int index = Math.max(Math.abs(point.getX()), Math.abs(point.getY()));
        LOG.info("index: " + index);
        if (point.getX() == index && point.getY() < index) {
            LOG.info("prava hrana, postupujeme dolu");
            if (actualCoordinate.getY() == gridDimension.height) {
                LOG.info("nemuzu dolu, zkusim doleva");
                if (actualCoordinate.getX() <= startingCoordinate.getX() - index || actualCoordinate.getX() <= 1) {
                    LOG.info("nemuzu ani doleva, zkusim nahoru");
                    return goUp(startingCoordinate, gridDimension, index);
                }
                final Coordinate ret = new Coordinate(actualCoordinate.getX() - 1, actualCoordinate.getY());
                LOG.info("ret: " + ret);
                return ret;
            }
            final Coordinate ret = new Coordinate(actualCoordinate.getX(), actualCoordinate.getY() + 1);
            LOG.info("ret: " + ret);
            return ret;
        }
        if (point.getY() == index && point.getX() > -index) {
            LOG.info("spodni hrana, postupuju doleva");
            if (actualCoordinate.getX() == 1) {
                LOG.info("nemuzu jit doleva, skocim nahoru");
                return goUp(startingCoordinate, gridDimension, index);
            }
            final Coordinate ret = new Coordinate(actualCoordinate.getX() - 1, actualCoordinate.getY());
            LOG.info("ret: " + ret);
            return ret;
        }
        if (point.getX() == -index && point.getY() > -index) {
            LOG.info("leva hrana, postupujeme nahoru");
            if (actualCoordinate.getY() == 1) {
                LOG.info("nemuzu jit nahoru, zacinam s novym indexem");
                return goNewIndex(startingCoordinate, gridDimension, index + 1);
            }
            final Coordinate ret = new Coordinate(actualCoordinate.getX(), actualCoordinate.getY() - 1);
            LOG.info("ret: " + ret);
            return ret;
        }
        if (point.getY() == -index && point.getX() < index) {
            LOG.info("horni hrana, jdeme doprava");
            if (actualCoordinate.getX() == gridDimension.width) {
                LOG.info("nemuzu jit doleva, zacinam s novym indexem");
                return goNewIndex(startingCoordinate, gridDimension, index + 1);
            }
            if (point.getX() + 1 == index) {
                LOG.info("jsem na konci, zacinam novy index");
                return goNewIndex(startingCoordinate, gridDimension, index + 1);
            }
            final Coordinate ret = new Coordinate(actualCoordinate.getX() + 1, actualCoordinate.getY());
            LOG.info("ret: " + ret);
            return ret;
        }
        return null;
    }

    private Coordinate goUp(final Coordinate i, final Dimension gridDimension, final int index) {
        LOG.info("goUp");
        if (i.getY() - index <= 0) {
            LOG.info("nemuzu jit ani nahoru, zacinam s novym indexem");
            return goNewIndex(i, gridDimension, index + 1);
        }
        final Coordinate ret = new Coordinate(1, i.getY() - index);
        LOG.info("ret: " + ret);
        return ret;
    }

    private Coordinate goNewIndex(final Coordinate i, final Dimension gridDimension, final int index) {
        LOG.info("goNewIndex: " + index);
        if (index >= Math.max(gridDimension.width, gridDimension.height)) {
            LOG.info("Go new index - Uz neni kam dal jit");
            return null;
        }
        int actualX;
        int actualY;
        if (i.getX() + index > gridDimension.width) {
            if (i.getY() + index > gridDimension.height) {
                if (i.getX() - index < 1) {
                    actualY = i.getY() - index;
                    if (actualY < 1) {
                        return null;
                    }
                    actualX = 0;
                } else {
                    actualX = i.getX() - index;
                    actualY = gridDimension.height + 1;
                }
            } else {
                actualX = gridDimension.width + 1;
                actualY = i.getY() + index;
            }
        } else {
            actualX = i.getX() + index;
            actualY = i.getY() - index;
        }
        final Coordinate actual = new Coordinate(actualX, (actualY < 0) ? 0 : actualY);
        return goNextI(i, actual, gridDimension);
    }
}
