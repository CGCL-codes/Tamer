package megamek.client.bot.princess;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import megamek.common.Coords;
import megamek.common.Entity;
import megamek.common.IGame;
import megamek.common.MovePath;
import megamek.common.MovePath.MoveStepType;

public class PathSearcher {

    public class WeightedPath {

        public WeightedPath(MovePath a, double w) {
            path = a;
            weight = w;
        }

        ;

        public double weight;

        public MovePath path;
    }

    public class PathState implements Comparable<PathState> {

        public PathState(Coords l, int f) {
            location = l;
            facing = f;
            hulldown = false;
            isjumping = false;
        }

        public PathState(MovePath p) {
            location = p.getFinalCoords();
            facing = p.getFinalFacing();
            hulldown = p.getFinalHullDown() || p.getFinalProne();
            isjumping = p.isJumping();
        }

        public int compareTo(PathState po) {
            if (facing < po.facing) {
                return -1;
            }
            if (po.facing < facing) {
                return 1;
            }
            if (location.x < po.location.x) {
                return -1;
            }
            if (po.location.x < location.x) {
                return 1;
            }
            if (location.y < po.location.y) {
                return -1;
            }
            if (po.location.y < location.y) {
                return 1;
            }
            if (po.hulldown && (!hulldown)) {
                return -1;
            }
            if (!po.hulldown && (hulldown)) {
                return 1;
            }
            if (po.isjumping && (!isjumping)) {
                return -1;
            }
            if (!po.isjumping && isjumping) {
                return 1;
            }
            return 0;
        }

        public Coords location;

        public int facing;

        public boolean hulldown;

        public boolean isjumping;
    }

    /**
     * Breadth first search through all legal moves, with only the shortest path
     * to each location/facing considered
     */
    ArrayList<WeightedPath> seeAllPaths(ArrayList<WeightedPath> start_paths, boolean forward, boolean backward, IGame game, TreeMap<PathState, WeightedPath> pathmap) {
        ArrayList<WeightedPath> next_steps = new ArrayList<WeightedPath>();
        for (WeightedPath sp : start_paths) {
            List<MovePath> nextmoves = sp.path.getNextMoves(backward, forward);
            for (MovePath p : nextmoves) {
                if (!p.getLastStep().isLegal()) {
                    continue;
                }
                if (p.getLastStep().getType() == MovePath.MoveStepType.GET_UP) {
                    if (p.getLastStep().getMpUsed() > sp.path.getEntity().getRunMP()) {
                        continue;
                    }
                }
                PathState mystate = new PathState(p);
                WeightedPath other_wp = pathmap.get(mystate);
                if (other_wp != null) {
                    if (other_wp.path.getMpUsed() <= p.getMpUsed()) {
                        continue;
                    }
                }
                PathSearcher.WeightedPath nextpath = new PathSearcher.WeightedPath(p, ranker.rankPath(p, game));
                pathmap.put(mystate, nextpath);
                next_steps.add(nextpath);
            }
        }
        if (next_steps.size() != 0) {
            ArrayList<WeightedPath> all_paths = seeAllPaths(next_steps, forward, backward, game, pathmap);
            all_paths.addAll(start_paths);
            return all_paths;
        }
        return start_paths;
    }

    ArrayList<WeightedPath> getAllWeightedPaths(Entity entity, IGame game) {
        ranker.initUnitTurn(entity, game);
        TreeMap<PathState, WeightedPath> pathmap = new TreeMap<PathState, WeightedPath>();
        ArrayList<WeightedPath> start_path = new ArrayList<WeightedPath>();
        MovePath empty_path = new MovePath(game, entity);
        double empty_rank = ranker.rankPath(empty_path, game);
        WeightedPath start = new WeightedPath(empty_path, empty_rank);
        pathmap.put(new PathState(start.path), start);
        start_path.add(start);
        if (entity.getJumpMP() > 0) {
            MovePath jump_path = new MovePath(game, entity);
            jump_path.addStep(MoveStepType.START_JUMP);
            WeightedPath startjump = new WeightedPath(jump_path, empty_rank - 0.1);
            pathmap.put(new PathState(startjump.path), startjump);
            start_path.add(startjump);
        }
        ArrayList<WeightedPath> allpaths = seeAllPaths(start_path, true, true, game, pathmap);
        return allpaths;
    }

    ArrayList<WeightedPath> getTopPaths(Entity entity, IGame game, int npaths) {
        ArrayList<WeightedPath> allpaths = getAllWeightedPaths(entity, game);
        return allpaths;
    }

    MovePath getBestPath(Entity entity, IGame game) {
        long start_time = System.currentTimeMillis();
        ArrayList<WeightedPath> allpaths = getAllWeightedPaths(entity, game);
        double min_value = allpaths.get(0).weight;
        MovePath min_path = allpaths.get(0).path;
        double max_value = allpaths.get(0).weight;
        MovePath max_path = allpaths.get(0).path;
        for (WeightedPath wp : allpaths) {
            if (max_value < wp.weight) {
                max_value = wp.weight;
                max_path = wp.path;
            }
            if (min_value > wp.weight) {
                min_value = wp.weight;
                min_path = wp.path;
            }
        }
        return max_path;
    }

    ;

    public PathRanker ranker;
}
