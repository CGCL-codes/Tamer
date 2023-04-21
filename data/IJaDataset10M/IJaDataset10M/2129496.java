package com.ibtech;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class DodoDNA implements DNA {

    private static final Logger logger = Logger.getLogger(DodoDNA.class);

    private static int fSize = 10;

    private Integer[] fValues = new Integer[fSize];

    public DNA combine(DNA other) {
        if (!(other instanceof DodoDNA)) {
            return null;
        }
        DodoDNA dodo = (DodoDNA) other;
        DodoDNA result = new DodoDNA();
        for (int i = 0; i < fSize; i++) {
            result.fValues[i] = (i % 2 == 0) ? fValues[i] : dodo.fValues[i];
        }
        return result;
    }

    private DodoDNA() {
    }

    private DodoDNA(Integer[] seq) {
        fValues = seq;
    }

    public static List<DodoDNA> generateRandom0(int numInstances) {
        Random randomGenerator = new Random(System.currentTimeMillis());
        List<DodoDNA> instances = new ArrayList<DodoDNA>();
        for (int numInst = 0; numInst < numInstances; numInst++) {
            DodoDNA dna = new DodoDNA();
            List<Integer> availPos = new ArrayList<Integer>();
            for (int i = 0; i < fSize; i++) {
                availPos.add(i);
            }
            for (int i = 0; i < fSize; i++) {
                int rand = randomGenerator.nextInt(availPos.size());
                Integer pos = availPos.remove(rand);
                dna.fValues[pos.intValue()] = i;
            }
            instances.add(dna);
        }
        return instances;
    }

    public static List<DodoDNA> generateRandom(int numInstances) {
        Random randomGenerator = new Random(System.currentTimeMillis());
        List<DodoDNA> instances = new ArrayList<DodoDNA>();
        for (int numInst = 0; numInst < numInstances; numInst++) {
            DodoDNA dna = new DodoDNA();
            for (int i = 0; i < fSize; i++) {
                dna.fValues[i] = randomGenerator.nextInt(fSize);
            }
            instances.add(dna);
        }
        return instances;
    }

    public int getQuality() {
        int q = 0;
        for (int i = 0; i < fValues.length; i++) {
            if (fValues[i] == i) {
                q++;
            }
        }
        return q;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DNA [");
        for (int i = 0; i < fValues.length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(fValues[i]);
        }
        sb.append("] (");
        sb.append(getQuality()).append(")");
        return sb.toString();
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        List<DodoDNA> dnaInstances = generateRandom(2);
        for (DodoDNA dna : dnaInstances) {
            logger.info("DNA : " + dna);
        }
        DodoDNA child = (DodoDNA) dnaInstances.get(0).combine(dnaInstances.get(1));
        logger.info("Child : " + child);
    }
}
