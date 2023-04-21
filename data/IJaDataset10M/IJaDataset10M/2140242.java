package datagen.groups;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;
import datagen.core.MutatorMap;
import datagen.data.Pair;
import datagen.generators.FirstNameGenerator;
import datagen.generators.GroupIdGenerator;
import datagen.generators.LastNameGenerator;
import datagen.generators.PairIdGenerator;
import datagen.generators.RecordIdGenerator;
import datagen.mutators.CharInserter;
import datagen.mutators.Mutator;
import datagen.mutators.OCRTransformer;
import datagen.mutators.PhoneticTransformer;
import datagen.mutators.Transpositioner;

public class MatchGenerator<T> {

    private final int NO_OF_MODIFICATIONS = 3;

    private ArrayList<String> mixFields = new ArrayList<String>();

    private ArrayList<String> mutateFields = new ArrayList<String>();

    private ArrayList<String> mutatorSet = new ArrayList<String>();

    public ArrayList<String> excludes = new ArrayList<String>();

    private static Logger logger = Logger.getLogger(MatchGenerator.class.getName());

    public MatchGenerator() {
        mutatorSet.add(CharInserter.NAME);
        mutatorSet.add(Transpositioner.NAME);
        mutatorSet.add(PhoneticTransformer.NAME);
        mutatorSet.add(OCRTransformer.NAME);
        mixFields.add(FirstNameGenerator.FIELD_NAME);
        mixFields.add(LastNameGenerator.FIELD_NAME);
        excludes.add(GroupIdGenerator.FIELD_NAME);
        excludes.add(RecordIdGenerator.FIELD_NAME);
        mutateFields.add(FirstNameGenerator.FIELD_NAME);
        mutateFields.add(LastNameGenerator.FIELD_NAME);
    }

    public Vector<Pair<T>> newMatches(Pair<T> recordPair) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        return newMatches(recordPair, NO_OF_MODIFICATIONS);
    }

    public Pair<T> mixPairData(T leftRecord, T rightRecord, ArrayList<String> mixFields) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        @SuppressWarnings("unchecked") T clonedRightRecord = (T) BeanUtils.cloneBean(rightRecord);
        for (String field : mixFields) {
            Method mGetField = leftRecord.getClass().getDeclaredMethod("get" + field);
            Object fieldValue = mGetField.invoke(leftRecord);
            Method mSetField = clonedRightRecord.getClass().getDeclaredMethod("set" + field, mGetField.getReturnType());
            mSetField.invoke(clonedRightRecord, fieldValue);
        }
        return new Pair<T>(leftRecord, clonedRightRecord);
    }

    @SuppressWarnings("unchecked")
    public Pair<T> clonePair(T recordLeft, T recordRight) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        T clonedLeftRecord = (T) BeanUtils.cloneBean(recordLeft);
        T clonedRightRecord = (T) BeanUtils.cloneBean(recordRight);
        Pair<T> differPair = new Pair<T>(clonedLeftRecord, clonedRightRecord);
        return differPair;
    }

    public Pair<T> manglePair(Pair<T> differPair, ArrayList<String> mutateFields) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        T mutatedLeftRecord = mutate(differPair.getLeftRecord(), mutateFields);
        T mutatedRightRecord = mutate(differPair.getRightRecord(), mutateFields);
        return (new Pair<T>(mutatedLeftRecord, mutatedRightRecord));
    }

    public Pair<T> updatePairId(Pair<T> pair, int sequenceNo) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        Pair<T> clonedPair = clonePair(pair.getLeftRecord(), pair.getRightRecord());
        for (T record : clonedPair.getRecords()) {
            Method mSetPairId = record.getClass().getDeclaredMethod("setPairId", String.class);
            Method mGetPairId = record.getClass().getDeclaredMethod("getPairId");
            String orgPairId = (String) mGetPairId.invoke(record);
            if (sequenceNo == -1) {
                sequenceNo = PairIdGenerator.typedPairNo(PairIdGenerator.TYPE.match);
            }
            mSetPairId.invoke(record, PairIdGenerator.duplicate(orgPairId, sequenceNo, PairIdGenerator.TYPE.match));
        }
        return clonedPair;
    }

    public Vector<Pair<T>> newMatches(Pair<T> recordPair, int noOfModifications) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
        T leftRecord = recordPair.getLeftRecord();
        T rightRecord = recordPair.getRightRecord();
        Pair<T> mixedMatchLeftPair = new Pair<T>(leftRecord, (mixPairData(rightRecord, leftRecord, new ArrayList<String>(Arrays.asList(LastNameGenerator.FIELD_NAME))).getRightRecord()));
        Pair<T> mixedMatchRightPair = new Pair<T>(mixPairData(leftRecord, rightRecord, new ArrayList<String>(Arrays.asList(FirstNameGenerator.FIELD_NAME))).getRightRecord(), rightRecord);
        Pair<T> mixedMangledMatchLeftPair = manglePair(mixedMatchLeftPair, mutateFields);
        Pair<T> mixedMangledMatchRightPair = manglePair(mixedMatchRightPair, mutateFields);
        Vector<Pair<T>> vMatchPairs = new Vector<Pair<T>>();
        vMatchPairs.add(updatePairId(mixedMangledMatchLeftPair, 1));
        vMatchPairs.add(updatePairId(mixedMangledMatchRightPair, 2));
        return vMatchPairs;
    }

    private T mutate(T record, ArrayList<String> mutateFields) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException {
        T clonedRecord = (T) BeanUtils.cloneBean(record);
        for (String field : mutateFields) {
            Method mGetField = clonedRecord.getClass().getDeclaredMethod("get" + field);
            Object fieldValue = mGetField.invoke(clonedRecord);
            Random rand = new Random();
            int randomIndex = rand.nextInt(mutatorSet.size());
            Mutator<?> mutator = MutatorMap.getInstance(mutatorSet.get(randomIndex));
            Object mutatedValue;
            if (mutator != null) {
                mutatedValue = mutator.modifyValue(fieldValue);
            } else {
                mutatedValue = fieldValue;
            }
            Method mSetField = clonedRecord.getClass().getDeclaredMethod("set" + field, mGetField.getReturnType());
            mSetField.invoke(clonedRecord, mutatedValue);
        }
        return clonedRecord;
    }
}
