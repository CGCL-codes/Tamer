package com.springrts.ai.crans.defense;

import gnu.trove.TObjectIntHashMap;
import java.util.Formatter;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.vecmath.Point2i;
import org.apache.log4j.Logger;
import com.springrts.ai.crans.AIClasses;
import com.springrts.ai.crans.Defines;
import com.springrts.ai.crans.Maths;
import com.springrts.ai.crans.Defines.UnitCategory;
import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.clb.MoveData;
import com.springrts.ai.oo.clb.Unit;
import com.springrts.ai.oo.clb.UnitDef;

public class DefenseMatrix {

    private static final Logger LOG = Logger.getLogger(DefenseMatrix.class);

    private final AIClasses ai;

    private float[][] chokeMapsByMovetype;

    private float[] chokePointArray;

    private int[] buildMaskArray;

    private SpotFinder spotFinder;

    public DefenseMatrix(final AIClasses ai) {
        this.ai = ai;
    }

    public void init() {
        this.chokePointArray = new float[this.ai.pather.totalcells];
        this.buildMaskArray = new int[this.ai.pather.totalcells];
        chokeMapsByMovetype = this.createDefenseMatrix();
        for (int index = 0; index < chokeMapsByMovetype.length; index++) {
            MoveData moveData = ai.ut.getMoveData(index);
            final String name = moveData == null ? "NULL" : moveData.getName();
            final Formatter formatter = new Formatter();
            formatter.format("%02d-%s", index, name);
            ai.dh.save("chokeMapForMoveType" + formatter, this.ai.pather.pathMapWidth, this.ai.pather.pathMapHeight, chokeMapsByMovetype[index]);
            ai.dh.save("moveArray" + formatter, this.ai.pather.pathMapWidth, this.ai.pather.pathMapHeight, this.ai.pather.getMoveArray(index));
        }
        this.spotFinder = new SpotFinder(this.ai.pather.pathMapHeight, this.ai.pather.pathMapWidth, this.chokePointArray);
    }

    public float[][] createDefenseMatrix() {
        final long start = System.nanoTime();
        final int moveTypeCount = this.ai.pather.getMoveTypeCount();
        final float[][] chokeMapsByMovetype = new float[moveTypeCount][];
        final int range = (int) Math.max(1.0f, Math.sqrt((this.ai.pather.pathMapWidth * this.ai.pather.pathMapHeight)) / Defines.THREATRES / 3);
        final int squarerange = range * range;
        final int maskwidth = 2 * range + 1;
        final float[] costmask = new float[maskwidth * maskwidth];
        for (int x = 0; x < maskwidth; x++) {
            for (int y = 0; y < maskwidth; y++) {
                final int distance = (x - range) * (x - range) + (y - range) * (y - range);
                if (distance <= squarerange) {
                    costmask[y * maskwidth + x] = (distance - squarerange) * (distance - squarerange) / squarerange * 2;
                } else {
                    costmask[y * maskwidth + x] = 0;
                }
            }
        }
        this.ai.ccb.enableCheating();
        final List<Unit> enemycomms = this.ai.ccb.getEnemyUnits();
        final int numberofenemyplayers = enemycomms.size();
        final AIFloat3[] enemyposes = new AIFloat3[16];
        for (int i = 0; i < numberofenemyplayers; i++) {
            enemyposes[i] = this.ai.ccb.getUnitPos(enemycomms.get(i));
        }
        this.ai.ccb.disableCheating();
        for (int moveTypeIndex = 0; moveTypeIndex < moveTypeCount; moveTypeIndex++) {
            final AIFloat3 mypos = this.ai.uh.allUnitsByCat.get(UnitCategory.CAT_BUILDER).get(0).getPos();
            chokeMapsByMovetype[moveTypeIndex] = new float[this.ai.pather.totalcells];
            final int reruns = 35;
            this.ai.pather.setMapData(this.ai.pather.getMoveArray(moveTypeIndex), 0.0f, chokeMapsByMovetype[moveTypeIndex]);
            for (int i = 0; i < this.ai.pather.totalcells; i++) {
                chokeMapsByMovetype[moveTypeIndex][i] = 1;
            }
            if (numberofenemyplayers <= 0) {
                continue;
            }
            if (moveTypeIndex != this.ai.pather.getHackMoveType()) {
            }
            for (int r = 0; r < reruns; r++) {
                for (int startpos = 0; startpos < numberofenemyplayers; startpos++) {
                    final List<Point2i> path = this.ai.pather.findPath(enemyposes[startpos], mypos);
                    if (path.isEmpty()) {
                        continue;
                    }
                    final int pathSize = path.size();
                    for (int pathIndex = 12; pathIndex < pathSize - 12; pathIndex++) {
                        if (pathIndex % 2 == 0) {
                            continue;
                        }
                        final Point2i pos = path.get(pathIndex);
                        for (int myx = -range; myx <= range; myx++) {
                            final int actualx = pos.x + myx;
                            if (actualx < 0 || actualx >= this.ai.pather.pathMapWidth) {
                                continue;
                            }
                            for (int myy = -range; myy <= range; myy++) {
                                final int actualy = pos.y + myy;
                                if (actualy < 0 || actualy >= this.ai.pather.pathMapHeight) {
                                    continue;
                                }
                                chokeMapsByMovetype[moveTypeIndex][actualy * this.ai.pather.pathMapWidth + actualx] += costmask[(myy + range) * maskwidth + myx + range];
                            }
                        }
                    }
                }
            }
        }
        final long end = System.nanoTime();
        final long durationMilis = TimeUnit.NANOSECONDS.toMillis(end - start);
        DefenseMatrix.LOG.debug("Creating 'defense matrix' took " + durationMilis + " milis.");
        return chokeMapsByMovetype;
    }

    public void maskBadBuildSpot(final AIFloat3 pos) {
        if (ai.math.isInBounds(pos)) {
            final int f3multiplier = 8 * Defines.THREATRES;
            final int x = (int) (pos.x / f3multiplier);
            final int y = (int) (pos.z / f3multiplier);
            this.buildMaskArray[y * this.ai.pather.pathMapWidth + x] = 1;
        }
    }

    public AIFloat3 getDefensePos(final UnitDef def, final AIFloat3 builderpos) {
        this.updateChokePointArray();
        final int f3multiplier = 8 * Defines.THREATRES;
        final int Range = (int) (this.ai.ut.getMaxRange(def) / f3multiplier);
        int bestspotx = 0;
        int bestspoty = 0;
        final float averagemapsize = (float) Math.sqrt((this.ai.pather.pathMapWidth * this.ai.pather.pathMapHeight)) * f3multiplier;
        float bestscore_fast = 0.0f;
        int bestspotx_fast = 0;
        int bestspoty_fast = 0;
        final long startNano = System.nanoTime();
        this.spotFinder.setRadius(Range);
        final float[] sumMap = this.spotFinder.getSumMap();
        ai.dh.save("sumMap", this.ai.pather.pathMapWidth, this.ai.pather.pathMapHeight, sumMap);
        final float bestThreatAtThisPoint = 0.01f + this.ai.tm.getAverageThreat();
        {
            final int x = (int) (builderpos.x / f3multiplier);
            final int y = (int) (builderpos.z / f3multiplier);
            final float fastSumMap = sumMap[y * this.ai.pather.pathMapWidth + x];
            final AIFloat3 spotpos = new AIFloat3(x * f3multiplier, 0, y * f3multiplier);
            final float myscore = (float) (fastSumMap / (Maths.distance2D(builderpos, spotpos) + averagemapsize / 8) * (this.ai.pather.heightMap[y * this.ai.pather.pathMapWidth + x] + 200) / (this.ai.pather.averageHeight + 10) / (this.ai.tm.threatAtThisPoint(spotpos) + bestThreatAtThisPoint));
            bestscore_fast = myscore;
            bestspotx_fast = x;
            bestspoty_fast = y;
        }
        int skipCount = 0;
        int testCount = 0;
        for (int cacheX = 0; cacheX < this.ai.pather.pathMapWidth / Defines.CACHEFACTOR; cacheX++) {
            for (int cacheY = 0; cacheY < this.ai.pather.pathMapHeight / Defines.CACHEFACTOR; cacheY++) {
                final CachePoint cachePoint = this.spotFinder.getCachePoint(cacheX, cacheY);
                final float bestScoreInThisBox = cachePoint.maxValueInBox;
                float bestX = builderpos.x / f3multiplier;
                float bestY = builderpos.z / f3multiplier;
                if (bestX > cacheX * Defines.CACHEFACTOR) {
                    if (bestX > cacheX * Defines.CACHEFACTOR + Defines.CACHEFACTOR) {
                        bestX = cacheX * Defines.CACHEFACTOR + Defines.CACHEFACTOR;
                    }
                } else {
                    bestX = cacheX * Defines.CACHEFACTOR;
                }
                if (bestY > cacheY * Defines.CACHEFACTOR) {
                    if (bestY > cacheY * Defines.CACHEFACTOR + Defines.CACHEFACTOR) {
                        bestY = cacheY * Defines.CACHEFACTOR + Defines.CACHEFACTOR;
                    }
                } else {
                    bestY = cacheY * Defines.CACHEFACTOR;
                }
                final AIFloat3 bestPosibleSpotpos = new AIFloat3(bestX * f3multiplier, 0, bestY * f3multiplier);
                final float bestDistance = Maths.distance2D(builderpos, bestPosibleSpotpos);
                final float bestHeight = this.ai.pather.heightMap[cachePoint.y * this.ai.pather.pathMapWidth + cachePoint.x] + 200;
                final float bestPosibleMyScore = bestScoreInThisBox / (bestDistance + averagemapsize / 4) * (bestHeight + 200) / bestThreatAtThisPoint;
                if (bestPosibleMyScore > bestscore_fast) {
                    testCount++;
                    for (int sx = cacheX * Defines.CACHEFACTOR; sx < this.ai.pather.pathMapWidth && sx < cacheX * Defines.CACHEFACTOR + Defines.CACHEFACTOR; sx++) {
                        for (int sy = cacheY * Defines.CACHEFACTOR; sy < this.ai.pather.pathMapHeight && sy < cacheY * Defines.CACHEFACTOR + Defines.CACHEFACTOR; sy++) {
                            final float fastSumMap = sumMap[sy * this.ai.pather.pathMapWidth + sx];
                            final AIFloat3 spotpos = new AIFloat3(sx * f3multiplier, 0, sy * f3multiplier);
                            final float myscore = fastSumMap / (Maths.distance2D(builderpos, spotpos) + averagemapsize / 4) * (this.ai.pather.heightMap[sy * this.ai.pather.pathMapWidth + sx] + 200) / (this.ai.tm.threatAtThisPoint(spotpos) + bestThreatAtThisPoint);
                            if (myscore > bestscore_fast && this.buildMaskArray[sy * this.ai.pather.pathMapWidth + sx] == 0 && this.ai.cb.getMap().isPossibleToBuildAt(def, spotpos, 0)) {
                                bestscore_fast = myscore;
                                bestspotx_fast = sx;
                                bestspoty_fast = sy;
                            }
                        }
                    }
                } else {
                    skipCount++;
                }
            }
        }
        bestspotx = bestspotx_fast;
        bestspoty = bestspoty_fast;
        ai.cons.sendTextMsg("Def pos time: " + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano));
        return new AIFloat3(bestspotx * f3multiplier, 0, bestspoty * f3multiplier);
    }

    private void updateChokePointArray() {
        final TObjectIntHashMap<UnitDef> enemiesOfType = new TObjectIntHashMap<UnitDef>();
        for (final UnitDef def : this.ai.cb.getUnitDefs()) {
            enemiesOfType.put(def, 0);
        }
        float totalCost = 1.0f;
        this.ai.ccb.enableCheating();
        final List<Unit> enemyUnits = this.ai.ccb.getEnemyUnits();
        for (int i = 0; i < this.ai.pather.totalcells; i++) {
            chokePointArray[i] = 0;
        }
        final float[] enemyCostsByMoveType = new float[this.ai.pather.getMoveTypeCount()];
        for (int i = 0; i < this.ai.pather.getMoveTypeCount(); i++) {
            enemyCostsByMoveType[i] = 0;
        }
        for (final Unit enemyUnit : enemyUnits) {
            final UnitDef udef = this.ai.ccb.getUnitDef(enemyUnit);
            enemiesOfType.adjustValue(udef, 1);
        }
        this.ai.ccb.disableCheating();
        for (final UnitDef enemyUnitDef : enemiesOfType.keys(new UnitDef[0])) {
            if (!enemyUnitDef.isAbleToFly() && enemyUnitDef.getSpeed() > 0) {
                final float currentcosts = (enemyUnitDef.getCost(this.ai.rh.getMetal()) * Defines.METAL2ENERGY + enemyUnitDef.getCost(this.ai.rh.getEnergy())) * enemiesOfType.get(enemyUnitDef);
                enemyCostsByMoveType[enemyUnitDef.getMoveData().getPathType()] += currentcosts;
                totalCost += currentcosts;
            }
        }
        for (int i = 0; i < enemyCostsByMoveType.length; i++) {
            enemyCostsByMoveType[i] /= totalCost;
            for (int c = 0; c < this.ai.pather.totalcells; c++) {
                chokePointArray[c] += chokeMapsByMovetype[i][c] * enemyCostsByMoveType[i];
            }
        }
        ai.dh.save("chokePointArray", this.ai.pather.pathMapWidth, this.ai.pather.pathMapHeight, chokePointArray);
    }

    public void addDefense(final AIFloat3 pos, final UnitDef def) {
        final int f3multiplier = 8 * Defines.THREATRES;
        final int Range = (int) (this.ai.ut.getMaxRange(def) / f3multiplier);
        final int squarerange = Range * Range;
        final Point2i p = this.ai.math.F32XY(pos, 8);
        for (int myx = p.x - Range; myx <= p.x + Range; myx++) {
            if (myx >= 0 && myx < this.ai.pather.pathMapWidth) {
                for (int myy = p.y - Range; myy <= p.y + Range; myy++) {
                    final int distance = (int) ((p.x - myx) * (p.x - myx) + (p.y - myy) * (p.y - myy) - 0.5);
                    if (myy >= 0 && myy < this.ai.pather.pathMapHeight && distance <= squarerange) {
                        for (int i = 0; i < chokeMapsByMovetype.length; i++) {
                            this.chokeMapsByMovetype[i][myy * this.ai.pather.pathMapWidth + myx] /= 2;
                        }
                    }
                }
            }
        }
        this.spotFinder.invalidateSumMap(p.x, p.y, Range + 1);
    }

    public void removeDefense(final AIFloat3 pos, final UnitDef def) {
        final int f3multiplier = 8 * Defines.THREATRES;
        final int Range = (int) (this.ai.ut.getMaxRange(def) / f3multiplier);
        final int squarerange = Range * Range;
        final Point2i p = this.ai.math.F32XY(pos, 8);
        for (int myx = p.x - Range; myx <= p.x + Range; myx++) {
            if (myx >= 0 && myx < this.ai.pather.pathMapWidth) {
                for (int myy = p.y - Range; myy <= p.y + Range; myy++) {
                    final int distance = (int) ((p.x - myx) * (p.x - myx) + (p.y - myy) * (p.y - myy) - 0.5);
                    if (myy >= 0 && myy < this.ai.pather.pathMapHeight && distance <= squarerange) {
                        for (int i = 0; i < chokeMapsByMovetype.length; i++) {
                            this.chokeMapsByMovetype[i][myy * this.ai.pather.pathMapWidth + myx] *= 2;
                        }
                    }
                }
            }
        }
        this.spotFinder.invalidateSumMap(p.x, p.y, Range);
    }
}
