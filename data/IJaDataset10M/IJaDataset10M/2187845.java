package qs;

import java.io.*;
import java.math.*;
import java.rmi.*;
import java.util.*;
import mathutils.BigIntegerFunctions;
import dist.*;
import client.*;

public class QSWorkUnit implements WorkUnit, Serializable {

    private static PrintStream Console = System.out;

    /** Constants. **/
    private QSWorkUnitConstants constants = null;

    /** Input data. **/
    private QSWorkUnitInput in = null;

    /** Output data. **/
    private QSWorkUnitOutput out = null;

    /** Temporary work space. **/
    private QSWorkUnitWorkSpace space = null;

    /** Run length. **/
    private long runLength = -1L;

    /** Cost. **/
    private int cost = 0;

    /** Hash code. **/
    private int hashCode;

    public QSWorkUnit(Constants remoteConstants, int constantsHashCode, int[] aFactorsRef, int cost) {
        in = new QSWorkUnitInput(remoteConstants, constantsHashCode, aFactorsRef);
        this.cost = cost;
        generateHashCode();
    }

    public void generateHashCode() {
        this.hashCode = (int) (((1 << 31) * Math.random())) << (Math.random() > 0.5 ? 0 : 1);
    }

    public void run(Cache cache) {
        constants = (QSWorkUnitConstants) cache.getObject(in.constantsHashCode);
        if (constants == null) {
            try {
                constants = (QSWorkUnitConstants) in.remoteConstants.download();
            } catch (RemoteException e) {
                throw new RuntimeException("Unable to retrieve constants.");
            }
            cache.putObject(constants.hashCode(), constants);
        }
        space = new QSWorkUnitWorkSpace();
        out = new QSWorkUnitOutput();
        space.sieveInterval = new float[constants.M << 1 + 1];
        long starttime = System.currentTimeMillis();
        Vector foundRelations = new Vector();
        acceptA(foundRelations);
        out.relations = new BigInteger[foundRelations.size()];
        foundRelations.copyInto(out.relations);
        Console.println("Found " + out.relations.length + " relations");
        runLength = System.currentTimeMillis() - starttime;
        constants = null;
        in = null;
        space = null;
    }

    public int cost() {
        return cost;
    }

    /**
     * @see lima.distribution.WorkUnit#runtime()
     */
    public int runtime() throws IllegalStateException {
        if (runLength > 0) return (int) runLength; else throw new IllegalStateException();
    }

    /**
     * @see lima.distribution.WorkUnit#hasRun()
     */
    public boolean hasRun() {
        return runLength > -1;
    }

    public BigInteger[] getRelations() {
        return out.relations;
    }

    public String toString() {
        return "QSwu(" + (cost() / 1000) + ")";
    }

    public int hashCode() {
        return hashCode;
    }

    public boolean equals(Object other) {
        if (other instanceof QSWorkUnit) {
            return (this.hashCode() == other.hashCode());
        }
        return false;
    }

    private void acceptA(Vector foundRelations) {
        space.trialDividedTotal = 0;
        space.logMaxLargePrime = Math.log(constants.maxLargePrime);
        space.M2 = 2 * constants.M;
        space.a = constants.factorBase[in.aFactorsRef[0]];
        Console.print("\na factors: " + space.a);
        for (int i = 1; i < in.aFactorsRef.length; i++) {
            Console.print(" * " + constants.factorBase[in.aFactorsRef[i]]);
            space.a = space.a.multiply(constants.factorBase[in.aFactorsRef[i]]);
        }
        Console.println("");
        space.B = new BigInteger[in.aFactorsRef.length];
        space.B2 = new BigInteger[in.aFactorsRef.length];
        space.aINV = new int[constants.factorBase.length - in.aFactorsRef.length];
        space.aINV2B = new int[in.aFactorsRef.length][constants.factorBase.length - in.aFactorsRef.length];
        space.b = QSWorkUnitConstants.ZERO;
        space.roots = new int[constants.factorBase.length - in.aFactorsRef.length];
        space.roots2 = new int[constants.factorBase.length - in.aFactorsRef.length];
        space.sieveBase = new int[constants.factorBase.length - in.aFactorsRef.length];
        int counter = 0;
        for (int j = 0; j < constants.factorBase.length; j++) {
            try {
                space.aINV[j - counter] = space.a.modInverse(constants.factorBase[j]).intValue();
                space.sieveBase[j - counter] = j;
            } catch (Exception exce) {
                counter++;
            }
        }
        space.GOODENOUGH = (float) (Math.log(constants.M) + 0.5 * BigIntegerFunctions.log(constants.n) - 0.8 * space.logMaxLargePrime - constants.logError);
        space.minge = (float) (Math.log(constants.M) + 0.5 * BigIntegerFunctions.log(constants.n) - 0.9 * space.logMaxLargePrime - constants.logError);
        space.maxge = (float) (Math.log(constants.M) + 0.5 * BigIntegerFunctions.log(constants.n) - 0.7 * space.logMaxLargePrime - constants.logError);
        space.step = (float) (0.05 * space.logMaxLargePrime);
        Console.println("\nStarting threshold value is " + space.GOODENOUGH);
        Console.println("Min/max : " + space.minge + "/" + space.maxge);
        for (int i = 0; i < in.aFactorsRef.length; i++) {
            BigInteger gamma = BigInteger.valueOf(constants.sqrtNmodP[in.aFactorsRef[i]]).multiply((space.a.divide(constants.factorBase[in.aFactorsRef[i]])).modInverse(constants.factorBase[in.aFactorsRef[i]]));
            if (gamma.compareTo(constants.factorBase[in.aFactorsRef[i]].divide(QSWorkUnitConstants.TWO)) > 0) gamma = constants.factorBase[in.aFactorsRef[i]].subtract(gamma);
            space.B[i] = space.a.divide(constants.factorBase[in.aFactorsRef[i]]).multiply(gamma);
            space.B2[i] = space.B[i].multiply(QSWorkUnitConstants.TWO);
            for (int j = 0; j < space.sieveBase.length; j++) {
                space.aINV2B[i][j] = BigInteger.valueOf(space.aINV[j]).multiply(space.B2[i]).mod(constants.factorBase[space.sieveBase[j]]).intValue();
            }
            space.b = space.b.add(space.B[i]);
        }
        for (int k = constants.smallPrime; k < space.sieveBase.length; k++) {
            space.roots[k] = (BigInteger.valueOf(constants.sqrtNmodP[space.sieveBase[k]]).subtract(space.b)).multiply(BigInteger.valueOf(space.aINV[k])).mod(constants.factorBase[space.sieveBase[k]]).intValue();
            space.roots2[k] = (constants.factorBase[space.sieveBase[k]].subtract(BigInteger.valueOf(constants.sqrtNmodP[space.sieveBase[k]])).subtract(space.b)).multiply(BigInteger.valueOf(space.aINV[k])).mod(constants.factorBase[space.sieveBase[k]]).intValue();
        }
        space.passedTotal = 0;
        space.trialDividedTotal = 0;
        space.timeTotal = System.currentTimeMillis();
        sieve(constants.n, foundRelations, 0);
        space.combs = 1 << (in.aFactorsRef.length - 1);
        space.check = space.combs / 32;
        for (int j = 1; j <= space.combs; j++) {
            int i;
            for (i = 1; (j & (0x1 << (i - 1))) == 0; i++) {
            }
            boolean evenPower = ((int) (Math.ceil(j / Math.pow(2, i))) % 2 == 0);
            short K;
            if (evenPower) {
                space.b = space.b.add(space.B2[i - 1]);
                K = 1;
            } else {
                space.b = space.b.subtract(space.B2[i - 1]);
                K = -1;
            }
            for (int k = constants.smallPrime; k < space.sieveBase.length; k++) {
                space.roots[k] -= K * space.aINV2B[i - 1][k];
                space.roots[k] %= constants.factorBase[space.sieveBase[k]].intValue();
                space.roots2[k] -= K * space.aINV2B[i - 1][k];
                space.roots2[k] %= constants.factorBase[space.sieveBase[k]].intValue();
            }
            sieve(constants.n, foundRelations, j);
        }
        Console.println("\n*****************************************************************");
        Console.println("Trial divided " + space.trialDividedTotal + " and passed " + space.passedTotal + ", ratio " + (space.trialDividedTotal != 0 ? "" + (100 * space.passedTotal / space.trialDividedTotal) : "N/A") + "%");
        Console.println("Time per relation = " + (space.passedTotal == 0 ? "N/A" : "" + (1000 * (System.currentTimeMillis() - space.timeTotal) / space.passedTotal)) + "micros");
        Console.println("*****************************************************************\n");
    }

    private void sieve(BigInteger n, Vector foundRelations, int thisNo) {
        BigInteger minArray[] = space.b.negate().divideAndRemainder(space.a);
        int min = minArray[0].intValue();
        int minMinusM = min - constants.M;
        int minAddM = min + constants.M;
        int mMinusMin = constants.M - min;
        int trialDivided = 0;
        int passed = 0;
        long sieveTime = System.currentTimeMillis();
        for (int i = 0; i < space.sieveInterval.length; i++) space.sieveInterval[i] = 0;
        for (int i = constants.smallPrime; i < space.sieveBase.length; i++) {
            int factorIndex = space.sieveBase[i];
            int factor = constants.factorBase[factorIndex].intValue();
            int theseRoots[] = { space.roots[i], space.roots2[i] };
            for (int k = 0; k < 2; k++) {
                if (theseRoots[k] < minMinusM) {
                    while (theseRoots[k] < minMinusM) theseRoots[k] += factor;
                    theseRoots[k] -= factor;
                } else if (theseRoots[k] > minAddM) {
                    while (theseRoots[k] > minAddM) theseRoots[k] -= factor;
                    theseRoots[k] += factor;
                }
                int x = theseRoots[k] + mMinusMin;
                if (x <= space.M2 && x >= 0) {
                    while (x <= space.M2) {
                        space.sieveInterval[x] += constants.logF[factorIndex];
                        x += factor;
                    }
                }
                x = theseRoots[k] - factor + mMinusMin;
                if (x <= space.M2 && x >= 0) {
                    while (x >= 0) {
                        space.sieveInterval[x] += constants.logF[factorIndex];
                        x -= factor;
                    }
                }
            }
        }
        for (int x = 0; x <= space.M2; x++) {
            if (space.sieveInterval[x] > space.GOODENOUGH) {
                BigInteger j = space.a.multiply(BigInteger.valueOf(minMinusM + x)).add(space.b).pow(2).subtract(constants.n);
                trialDivided++;
                if (trial_division(j.abs())) {
                    passed++;
                    foundRelations.add(j);
                }
            }
        }
        space.trialDividedTotal += trialDivided;
        space.passedTotal += passed;
        float newrate;
        if (thisNo == 0) {
            Console.println("Init vars for autoconf");
            space.stopAuto = false;
            space.lastIncUp = true;
            space.changedDirection = true;
            space.GOODENOUGH++;
            space.rate = 0;
            space.intPassed = 0;
            space.intTime = 0;
        } else {
            space.intTime += System.currentTimeMillis() - sieveTime;
            space.intPassed += passed;
            if (thisNo % space.check == 0) {
                newrate = 1000 * space.intPassed / (float) (space.intTime + 0.5 * space.check);
                if (!space.stopAuto && space.rate != 0) {
                    if (newrate > space.rate) {
                        Console.println(thisNo + ": New rate " + space.GOODENOUGH + " better: " + newrate + ">" + space.rate);
                        if (space.lastIncUp) {
                            if (thisNo != space.check) space.changedDirection = false;
                            if (space.GOODENOUGH < space.maxge) space.GOODENOUGH += space.step;
                        } else {
                            if (thisNo != space.check) space.changedDirection = false;
                            if (space.GOODENOUGH > space.minge) space.GOODENOUGH -= space.step;
                        }
                    } else if (newrate < space.rate) {
                        Console.println(thisNo + ": New rate " + space.GOODENOUGH + " worse: " + newrate + "<" + space.rate);
                        if (!space.changedDirection) {
                            if (space.lastIncUp) {
                                space.lastIncUp = false;
                                space.changedDirection = true;
                                space.GOODENOUGH -= space.step;
                            } else {
                                space.lastIncUp = true;
                                space.changedDirection = true;
                                space.GOODENOUGH += space.step;
                            }
                            if (space.lastLock == space.GOODENOUGH) {
                                space.stopAuto = true;
                                Console.println("Locked on to this value: " + space.GOODENOUGH + ".");
                            } else {
                                space.lastLock = space.GOODENOUGH;
                            }
                        } else {
                            if (space.lastIncUp) {
                                space.lastIncUp = false;
                                space.changedDirection = true;
                                if (space.GOODENOUGH > space.minge) space.GOODENOUGH -= space.step;
                            } else {
                                space.lastIncUp = true;
                                if (space.GOODENOUGH < space.maxge) space.GOODENOUGH += space.step;
                                space.changedDirection = true;
                            }
                        }
                    } else {
                        Console.println(thisNo + ": New space.rate " + space.GOODENOUGH + " the same: " + newrate + "<" + space.rate);
                    }
                }
                space.rate = newrate;
                space.intPassed = 0;
                space.intTime = 0;
            }
        }
    }

    private boolean trial_division(BigInteger s) {
        space.trialDividedTotal++;
        int i = constants.factorBase.length - 1;
        BigInteger divRem[];
        if (s.signum() == -1) s = s.abs();
        while (i >= 0) {
            divRem = s.divideAndRemainder(constants.factorBase[i]);
            if (divRem[1].equals(QSWorkUnitConstants.ZERO)) {
                s = divRem[0];
            } else i--;
        }
        if (s.equals(QSWorkUnitConstants.ONE)) {
            return true;
        } else {
            if (s.compareTo(BigInteger.valueOf(constants.maxLargePrime)) < 0) {
                return true;
            }
            return false;
        }
    }
}
