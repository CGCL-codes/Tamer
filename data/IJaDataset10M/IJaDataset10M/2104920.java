package com.springrts.ai.crans;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import com.springrts.ai.crans.Defines.UnitCategory;
import com.springrts.ai.oo.AIFloat3;
import com.springrts.ai.oo.clb.Unit;
import com.springrts.ai.oo.clb.UnitDef;

public class MetalMap {

    final AIClasses ai;

    final Logger log = Logger.getLogger(this.getClass().getName());

    int NumSpotsFound;

    float AverageMetal;

    List<AIFloat3> vectoredSpots = new ArrayList<AIFloat3>();

    boolean Stopme;

    int MaxSpots;

    int MetalMapHeight;

    int MetalMapWidth;

    int TotalCells;

    int SquareRadius;

    int DoubleSquareRadius;

    int TotalMetal;

    int MaxMetal;

    int TempMetal;

    int coordx;

    int coordy;

    int Minradius;

    int MinMetalForSpot;

    int extractorRadius;

    int DoubleRadius;

    float extractorRadiusOrg;

    int[] MexArrayA;

    int[] MexArrayB;

    int[] MexArrayC;

    int[] TempAverage;

    MetalMap(final AIClasses ai) {
        this.ai = ai;
        MinMetalForSpot = 50;
        MaxSpots = 10000;
        MetalMapHeight = ai.cb.getMap().getHeight() / 2;
        MetalMapWidth = ai.cb.getMap().getWidth() / 2;
        TotalCells = MetalMapHeight * MetalMapWidth;
        extractorRadiusOrg = ai.cb.getMap().getExtractorRadius(ai.rh.getMetal());
        extractorRadius = (int) (ai.cb.getMap().getExtractorRadius(ai.rh.getMetal()) / 16.0f);
        DoubleRadius = extractorRadius * 2;
        SquareRadius = extractorRadius * extractorRadius;
        DoubleSquareRadius = DoubleRadius * DoubleRadius;
        MexArrayA = new int[TotalCells];
        MexArrayB = new int[TotalCells];
        MexArrayC = new int[TotalCells];
        TempAverage = new int[TotalCells];
        TotalMetal = MaxMetal = NumSpotsFound = 0;
        Stopme = false;
    }

    public AIFloat3 getNearestMetalSpot(final Unit builder, final UnitDef extractorDef) {
        final float maxDivergence = 16.0f;
        float bestScore = 0.0f;
        AIFloat3 bestSpot = Defines.ERRORVECTOR;
        for (final AIFloat3 spot : vectoredSpots) {
            final AIFloat3 buildSpot = ai.cb.getMap().findClosestBuildSite(extractorDef, spot, maxDivergence, 2, 0);
            if (buildSpot.x < 0.0f) {
                continue;
            }
            final float spotDistance = Maths.distance2D(buildSpot, builder.getPos());
            final float spotThreat = ai.tm.threatAtThisPoint(buildSpot);
            final float spotScore = spot.y / (spotDistance + 150) / (spotThreat + ai.tm.getAverageThreat() + 1.0f + 10);
            final boolean betterScore = bestScore < spotScore;
            final List<Unit> enemies = ai.ccb.getEnemyUnitsIn(buildSpot, extractorRadiusOrg * 1.5f);
            boolean noEnemiesOnSpot = true;
            for (Unit enemy : enemies) {
                UnitDef ud = ai.ccb.getUnitDef(enemy);
                if (ud != null && !ud.getWeaponMounts().isEmpty()) {
                    noEnemiesOnSpot = false;
                    break;
                }
            }
            boolean notOccupied = true;
            if (NumSpotsFound < 100) {
                final List<Unit> allies = ai.cb.getFriendlyUnitsIn(buildSpot, extractorRadiusOrg * 1.5f);
                for (Unit ally : allies) {
                    if (ai.ut.getCategory(ally.getDef()) == UnitCategory.CAT_MEX) {
                        notOccupied = false;
                        break;
                    }
                }
            }
            final boolean fairlySafe = spotThreat <= 1.5f + ai.tm.getAverageThreat() * 0.5;
            final boolean noBuildPlanOnSpot = !ai.uh.taskPlanExist(buildSpot, extractorDef);
            if (betterScore && noEnemiesOnSpot && fairlySafe && noBuildPlanOnSpot && notOccupied) {
                bestScore = spotScore;
                bestSpot = buildSpot;
                bestSpot.y = spot.y;
            }
        }
        return bestSpot;
    }

    void init() {
        ai.cons.sendTextMsg("KAI Metal Class by Krogothe");
        getMetalPoints();
        debugLog("Number of metal spots found: " + NumSpotsFound);
    }

    void getMetalPoints() {
        int[] xend = new int[DoubleRadius + 1];
        for (int a = 0; a < DoubleRadius + 1; a++) {
            float z = a - extractorRadius;
            float floatsqrradius = SquareRadius;
            xend[a] = (int) Math.sqrt((floatsqrradius - z * z));
        }
        List<Short> metalMap = ai.cb.getMap().getResourceMapRaw(ai.rh.getMetal());
        int[] metalMapArray = new int[metalMap.size()];
        int index = 0;
        for (short val : metalMap) {
            metalMapArray[index++] = 0xFFFF & val;
        }
        double TotalMetalDouble = 0;
        for (int i = 0; i < TotalCells; i++) {
            TotalMetalDouble += MexArrayA[i] = metalMapArray[i];
        }
        AverageMetal = (float) (TotalMetalDouble / TotalCells);
        if (TotalMetalDouble < 0.9) {
            NumSpotsFound = 0;
            return;
        }
        for (int y = 0; y < MetalMapHeight; y++) {
            for (int x = 0; x < MetalMapWidth; x++) {
                TotalMetal = 0;
                if (x == 0 && y == 0) for (int sy = y - extractorRadius, a = 0; sy <= y + extractorRadius; sy++, a++) {
                    if (sy >= 0 && sy < MetalMapHeight) {
                        for (int sx = x - xend[a]; sx <= x + xend[a]; sx++) {
                            if (sx >= 0 && sx < MetalMapWidth) {
                                TotalMetal += MexArrayA[sy * MetalMapWidth + sx];
                            }
                        }
                    }
                }
                if (x > 0) {
                    TotalMetal = TempAverage[y * MetalMapWidth + x - 1];
                    for (int sy = y - extractorRadius, a = 0; sy <= y + extractorRadius; sy++, a++) {
                        if (sy >= 0 && sy < MetalMapHeight) {
                            int addX = x + xend[a];
                            int remX = x - xend[a] - 1;
                            if (addX < MetalMapWidth) TotalMetal += MexArrayA[sy * MetalMapWidth + addX];
                            if (remX >= 0) TotalMetal -= MexArrayA[sy * MetalMapWidth + remX];
                        }
                    }
                } else if (y > 0) {
                    TotalMetal = TempAverage[(y - 1) * MetalMapWidth];
                    int a = extractorRadius;
                    for (int sx = 0; sx <= extractorRadius; sx++, a++) {
                        if (sx < MetalMapWidth) {
                            int remY = y - xend[a] - 1;
                            if (remY >= 0) TotalMetal -= MexArrayA[remY * MetalMapWidth + sx];
                        }
                    }
                    a = extractorRadius;
                    for (int sx = 0; sx <= extractorRadius; sx++, a++) {
                        if (sx < MetalMapWidth) {
                            int addY = y + xend[a];
                            if (addY < MetalMapHeight) TotalMetal += MexArrayA[addY * MetalMapWidth + sx];
                        }
                    }
                    TotalMetal = TotalMetal;
                }
                TempAverage[y * MetalMapWidth + x] = TotalMetal;
                if (MaxMetal < TotalMetal) {
                    MaxMetal = TotalMetal;
                }
            }
        }
        int[] valueDist = new int[256];
        for (int i = 0; i < 256; i++) {
            valueDist[i] = 0;
        }
        for (int i = 0; i < TotalCells; i++) {
            MexArrayB[i] = TempAverage[i] * 255 / MaxMetal;
            MexArrayC[i] = 0;
            int value = MexArrayB[i];
            valueDist[value]++;
        }
        int bestValue = 0;
        int numberOfValues = 0;
        int usedSpots = 0;
        for (int i = 255; i >= 0; i--) {
            if (valueDist[i] != 0) {
                bestValue = i;
                numberOfValues = valueDist[i];
                break;
            }
        }
        if (numberOfValues > 256) numberOfValues = 256;
        int[] bestSpotList = new int[numberOfValues];
        for (int i = 0; i < TotalCells; i++) {
            if (MexArrayB[i] == bestValue) {
                bestSpotList[usedSpots] = i;
                usedSpots++;
                if (usedSpots == numberOfValues) {
                    usedSpots = 0;
                    break;
                }
            }
        }
        for (int a = 0; a < MaxSpots; a++) {
            if (!Stopme) {
                TempMetal = 0;
                int speedTempMetal_x = 0;
                int speedTempMetal_y = 0;
                int speedTempMetal = 0;
                boolean found = false;
                while (!found) {
                    if (usedSpots == numberOfValues) {
                        for (int i = 0; i < 256; i++) {
                            valueDist[i] = 0;
                        }
                        for (int i = 0; i < TotalCells; i++) {
                            int value = MexArrayB[i];
                            valueDist[value]++;
                        }
                        bestValue = 0;
                        numberOfValues = 0;
                        usedSpots = 0;
                        for (int i = 255; i >= 0; i--) {
                            if (valueDist[i] != 0) {
                                bestValue = i;
                                numberOfValues = valueDist[i];
                                break;
                            }
                        }
                        if (numberOfValues > 256) numberOfValues = 256;
                        bestSpotList = new int[numberOfValues];
                        for (int i = 0; i < TotalCells; i++) {
                            if (MexArrayB[i] == bestValue) {
                                bestSpotList[usedSpots] = i;
                                usedSpots++;
                                if (usedSpots == numberOfValues) {
                                    usedSpots = 0;
                                    break;
                                }
                            }
                        }
                    }
                    int spotIndex = bestSpotList[usedSpots];
                    if (MexArrayB[spotIndex] == bestValue) {
                        speedTempMetal_x = spotIndex % MetalMapWidth;
                        speedTempMetal_y = spotIndex / MetalMapWidth;
                        speedTempMetal = bestValue;
                        found = true;
                    }
                    usedSpots++;
                }
                coordx = speedTempMetal_x;
                coordy = speedTempMetal_y;
                TempMetal = speedTempMetal;
            }
            if (TempMetal < MinMetalForSpot) {
                Stopme = true;
            }
            if (!Stopme) {
                AIFloat3 BufferSpot = new AIFloat3();
                BufferSpot.x = coordx * 16 + 8;
                BufferSpot.z = coordy * 16 + 8;
                BufferSpot.y = TempMetal * (ai.cb.getMap().getMaxResource(ai.rh.getMetal())) * MaxMetal / 255;
                vectoredSpots.add(BufferSpot);
                MexArrayC[coordy * MetalMapWidth + coordx] = TempMetal;
                NumSpotsFound += 1;
                for (int sy = coordy - extractorRadius, b = 0; sy <= coordy + extractorRadius; sy++, b++) {
                    if (sy >= 0 && sy < MetalMapHeight) {
                        int clearXStart = coordx - xend[b];
                        int clearXEnd = coordx + xend[b];
                        if (clearXStart < 0) clearXStart = 0;
                        if (clearXEnd >= MetalMapWidth) clearXEnd = MetalMapWidth - 1;
                        for (int xClear = clearXStart; xClear <= clearXEnd; xClear++) {
                            MexArrayA[sy * MetalMapWidth + xClear] = 0;
                            MexArrayB[sy * MetalMapWidth + xClear] = 0;
                            TempAverage[sy * MetalMapWidth + xClear] = 0;
                        }
                    }
                }
                for (int y = coordy - DoubleRadius; y <= coordy + DoubleRadius; y++) {
                    if (y >= 0 && y < MetalMapHeight) {
                        for (int x = coordx - DoubleRadius; x <= coordx + DoubleRadius; x++) {
                            if (x >= 0 && x < MetalMapWidth) {
                                TotalMetal = 0;
                                if (x == 0 && y == 0) for (int sy = y - extractorRadius, c = 0; sy <= y + extractorRadius; sy++, c++) {
                                    if (sy >= 0 && sy < MetalMapHeight) {
                                        for (int sx = x - xend[c]; sx <= x + xend[c]; sx++) {
                                            if (sx >= 0 && sx < MetalMapWidth) {
                                                TotalMetal += MexArrayA[sy * MetalMapWidth + sx];
                                            }
                                        }
                                    }
                                }
                                if (x > 0) {
                                    TotalMetal = TempAverage[y * MetalMapWidth + x - 1];
                                    for (int sy = y - extractorRadius, d = 0; sy <= y + extractorRadius; sy++, d++) {
                                        if (sy >= 0 && sy < MetalMapHeight) {
                                            int addX = x + xend[d];
                                            int remX = x - xend[d] - 1;
                                            if (addX < MetalMapWidth) TotalMetal += MexArrayA[sy * MetalMapWidth + addX];
                                            if (remX >= 0) TotalMetal -= MexArrayA[sy * MetalMapWidth + remX];
                                        }
                                    }
                                } else if (y > 0) {
                                    TotalMetal = TempAverage[(y - 1) * MetalMapWidth];
                                    int e = extractorRadius;
                                    for (int sx = 0; sx <= extractorRadius; sx++, e++) {
                                        if (sx < MetalMapWidth) {
                                            int remY = y - xend[e] - 1;
                                            if (remY >= 0) TotalMetal -= MexArrayA[remY * MetalMapWidth + sx];
                                        }
                                    }
                                    e = extractorRadius;
                                    for (int sx = 0; sx <= extractorRadius; sx++, e++) {
                                        if (sx < MetalMapWidth) {
                                            int addY = y + xend[e];
                                            if (addY < MetalMapHeight) TotalMetal += MexArrayA[addY * MetalMapWidth + sx];
                                        }
                                    }
                                }
                                TempAverage[y * MetalMapWidth + x] = TotalMetal;
                                MexArrayB[y * MetalMapWidth + x] = TotalMetal * 255 / MaxMetal;
                            }
                        }
                    }
                }
            }
        }
    }

    private void debugLog(final String msg) {
        log.log(Level.INFO, ai.frame + " - " + msg);
    }
}
