package ru.susu.algebra.centralunits.alternating.initializers;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import ru.susu.algebra.centralunits.alternating.period.IAlternatingPeriodExtractor;
import ru.susu.algebra.centralunits.alternating.period.MatrixPowAlternatingPeriodExtractor;
import ru.susu.algebra.centralunits.alternating.tex.SpecialCharactersUnionMathMethod;
import ru.susu.algebra.centralunits.alternating.tex.local.MainDivisibilityLemma;
import ru.susu.algebra.chartable.constructor.AlternatingCharTableUtil;
import ru.susu.algebra.field.quadratic.QuadraticFieldGeneralForm;
import ru.susu.algebra.field.quadratic.QuadraticGeneralUnitExtractor;
import ru.susu.algebra.field.quadratic.QuadraticInteger;
import ru.susu.algebra.jtex.ITexElement;
import ru.susu.algebra.pair.Pair;
import ru.susu.algebra.partition.Partition;
import ru.susu.algebra.properties.IPropertySource;
import ru.susu.algebra.util.NumberUtilities;

/**
 * @author akargapolov
 * @since: 13.09.2010
 */
public class LocalUnitsInitializer extends SpecialCharactersUnionMathMethod {

    private static final Logger _log = Logger.getLogger("LocalUnitsInitializer");

    private static final Class[] DEPENDENCIES = { SpecialRowsInitializer.class, QuadraticFieldsInitializer.class, ExponentsInitializer.class };

    private static final String POW_PARAM = LocalUnitsInitializer.class.getSimpleName() + "_PowsMap";

    private static final String MODS_PARAM = LocalUnitsInitializer.class.getSimpleName() + "_ModsMap";

    private static final String ALPHA_COEFFICIENTS = LocalUnitsInitializer.class.getSimpleName() + "_AplhaCoefficients";

    private static final String BETA_COEFFICIENTS = LocalUnitsInitializer.class.getSimpleName() + "_BetaCoefficients";

    private static final String REDUCED_PERIODS = LocalUnitsInitializer.class.getSimpleName() + "_ReducedPeriods";

    private static IAlternatingPeriodExtractor _periodExtractor = new MatrixPowAlternatingPeriodExtractor();

    @Override
    protected ITexElement getElement(Partition partition, IPropertySource ps) throws Exception {
        if (!ps.containsKey(POW_PARAM)) {
            ps.setValue(POW_PARAM, new LinkedHashMap<Partition, BigInteger>());
            ps.setValue(MODS_PARAM, new LinkedHashMap<Partition, List<BigInteger>>());
            ps.setValue(ALPHA_COEFFICIENTS, new LinkedHashMap<Partition, BigInteger>());
            ps.setValue(BETA_COEFFICIENTS, new LinkedHashMap<Partition, BigInteger>());
            ps.setValue(REDUCED_PERIODS, new LinkedHashMap<Pair<Partition, BigInteger>, BigInteger>());
        }
        if (getPow(ps, partition) == null) {
            BigInteger bigMod = AlternatingCharTableUtil.calcZX(partition);
            List<BigInteger> mods = Lists.newArrayList();
            List<BigInteger> pows = Lists.newArrayList();
            for (Pair<Integer, Integer> pair : NumberUtilities.factorization(bigMod)) {
                BigInteger mod = BigInteger.valueOf(pair.getKey()).pow(pair.getValue());
                pows.add(getReducedPeriod(ps, partition, mod));
                mods.add(mod);
            }
            getPartition2Mods(ps).put(partition, mods);
            getPows(ps).put(partition, NumberUtilities.lcm(pows));
            checkMinimalPow(partition, ps);
            checkMinimalBDPow(partition, ps);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Map<Partition, BigInteger> getPows(IPropertySource ps) {
        return (Map<Partition, BigInteger>) ps.getValue(POW_PARAM);
    }

    @SuppressWarnings("unchecked")
    private static Map<Partition, List<BigInteger>> getPartition2Mods(IPropertySource ps) {
        return (Map<Partition, List<BigInteger>>) ps.getValue(MODS_PARAM);
    }

    public static BigInteger getPow(IPropertySource ps, Partition partition) {
        return getPows(ps).get(partition);
    }

    public static List<BigInteger> getMods(IPropertySource ps, Partition partition) {
        return getPartition2Mods(ps).get(partition);
    }

    public static List<BigInteger> getReducedPeriods(final IPropertySource ps, final Partition partition) {
        return Lists.newArrayList(Collections2.transform(getMods(ps, partition), new Function<BigInteger, BigInteger>() {

            @Override
            public BigInteger apply(BigInteger mod) {
                return getReducedPeriod(ps, partition, mod);
            }
        }));
    }

    public static List<Pair<BigInteger, BigInteger>> getPeriod(IPropertySource ps, Partition partition, BigInteger mod) {
        return _periodExtractor.getPeriod(ps, partition, mod);
    }

    /**
	 * Возвращает целочисленное значение не приведенного периода
	 * @param ps источник свойств
	 * @param partition разбиение
	 * @param mod модуль
	 */
    public static BigInteger getIntegerPeriod(IPropertySource ps, Partition partition, BigInteger mod) {
        return _periodExtractor.getIntegerPeriod(ps, partition, mod);
    }

    /**
	 * Возвращает приведенный период
	 * @param ps источник свойств
	 * @param partition разбиение
	 * @param mod модуль
	 */
    public static BigInteger getReducedPeriod(IPropertySource ps, Partition partition, BigInteger mod) {
        @SuppressWarnings("unchecked") Map<Pair<Partition, BigInteger>, BigInteger> map = (Map<Pair<Partition, BigInteger>, BigInteger>) ps.getValue(REDUCED_PERIODS);
        if (!map.containsKey(Pair.pair(partition, mod))) {
            BigInteger result;
            Set<BigInteger> indexes = Sets.newHashSet(Collections2.transform(listGoodPeriods(ps, partition, mod), new Pair.KeyFunction<BigInteger>()));
            BigInteger intPeriod = getIntegerPeriod(ps, partition, mod);
            if (indexes.size() == 1) {
                result = intPeriod;
            } else {
                final BigInteger period = intPeriod.divide(BigInteger.valueOf(indexes.size()));
                if (indexes.size() != Collections2.filter(indexes, new Predicate<BigInteger>() {

                    @Override
                    public boolean apply(BigInteger input) {
                        return ObjectUtils.equals(input.remainder(period), BigInteger.ZERO);
                    }
                }).size()) {
                    throw new RuntimeException();
                }
                result = period;
            }
            map.put(Pair.pair(partition, mod), result);
        }
        return map.get(Pair.pair(partition, mod));
    }

    @Override
    protected Class[] getDependentInitializers() {
        return DEPENDENCIES;
    }

    /**
	 * Поиск минимальной степени основной единицы поля, такой что коэффициент перед w кратен z/bd
	 * Оказывается минимальная степень * 2 = ответ для всех n <= 60
	 * @param partition
	 * @param ps
	 * @throws Exception
	 */
    private static void checkMinimalBDPow(Partition partition, IPropertySource ps) throws Exception {
        QuadraticFieldGeneralForm generateUnit = QuadraticFieldsInitializer.getGeneralUnit(ps, partition);
        BigInteger pow = getPow(ps, partition);
        BigInteger mod = AlternatingCharTableUtil.calcZX(partition);
        QuadraticInteger character = QuadraticFieldsInitializer.getCharacter(ps, partition);
        BigInteger zbd = mod.divide(character.getB().multiply(character.getD()));
        _log.info("Begin check bd pow: " + pow);
        BigInteger minPow = pow;
        for (Pair<Integer, Integer> pair : NumberUtilities.factorization(minPow)) {
            for (int index = 0; index < pair.getValue(); index++) {
                QuadraticInteger tempUnit = generateUnit.modPow(minPow.divide(BigInteger.valueOf(pair.getKey())), mod);
                if (tempUnit.getB().remainder(zbd).equals(BigInteger.ZERO)) {
                    minPow = minPow.divide(BigInteger.valueOf(pair.getKey()));
                } else {
                    break;
                }
            }
        }
        QuadraticInteger tempUnit = generateUnit.modPow(minPow, mod);
        _log.info("A = " + tempUnit.getA() + " B = " + tempUnit.getB() + " A / zbd = " + tempUnit.getA().divide(zbd) + " B / zbd = " + tempUnit.getB().divide(zbd));
        QuadraticFieldGeneralForm gunit = new QuadraticGeneralUnitExtractor().getGeneralUnit(new QuadraticInteger(BigInteger.ZERO, BigInteger.ONE, tempUnit.getD()));
        int pow1 = 1;
        for (pow1 = 1; !gunit.pow(BigInteger.valueOf(pow1)).equals(generateUnit); pow1++) ;
        BigInteger tmpPow = minPow.multiply(BigInteger.valueOf(pow1));
        _log.info("zbd = " + zbd + " = " + Arrays.toString(NumberUtilities.factorization(zbd)));
        _log.info("pow = " + tmpPow + " = " + Arrays.toString(NumberUtilities.factorization(tmpPow)));
        _log.info("Minimal bd pow " + minPow + " equals pow " + pow + " / " + pow.divide(minPow));
        if (!pow.divide(minPow).equals(BigInteger.valueOf(2))) {
            throw new RuntimeException("bd pow / minpow != 2");
        }
        _log.info("End check bd pow: " + pow);
    }

    private static void checkMinimalPow(Partition partition, IPropertySource ps) throws Exception {
        QuadraticFieldGeneralForm generateUnit = QuadraticFieldsInitializer.getGeneralUnit(ps, partition);
        BigInteger pow = getPow(ps, partition);
        _log.info("Begin check pow: " + pow);
        if (isGoodPow(partition, ps, pow)) {
            _log.info("pow is good");
        } else {
            throw new RuntimeException("Pow is not good");
        }
        BigInteger minPow = pow;
        for (Pair<Integer, Integer> pair : NumberUtilities.factorization(minPow)) {
            for (int index = 0; index < pair.getValue(); index++) {
                if (isGoodPow(partition, ps, minPow.divide(BigInteger.valueOf(pair.getKey())))) {
                    minPow = minPow.divide(BigInteger.valueOf(pair.getKey()));
                } else {
                    break;
                }
            }
        }
        if (minPow.equals(pow)) {
            _log.info("Minimal pow " + minPow + " equals pow " + pow);
        } else {
            throw new RuntimeException("Minimal pow " + minPow + " less theoretical pow " + pow);
        }
        _log.info("End check pow: " + pow);
    }

    private static boolean isGoodPow(Partition partition, IPropertySource ps, BigInteger pow) throws Exception {
        QuadraticFieldGeneralForm generalUnit = QuadraticFieldsInitializer.getGeneralUnit(ps, partition);
        BigInteger mod = AlternatingCharTableUtil.calcZX(partition);
        QuadraticFieldGeneralForm lambdaM1 = (QuadraticFieldGeneralForm) generalUnit.modPow(pow, mod);
        lambdaM1 = new QuadraticFieldGeneralForm(lambdaM1.getA().subtract(BigInteger.ONE).mod(mod), lambdaM1.getB(), lambdaM1.getD());
        for (QuadraticFieldGeneralForm number : Lists.newArrayList(lambdaM1, (QuadraticFieldGeneralForm) lambdaM1.multiply(QuadraticFieldsInitializer.getCharacter(ps, partition)), (QuadraticFieldGeneralForm) lambdaM1.multiply(QuadraticFieldsInitializer.getCharacter(ps, partition).getConjugateNumber()))) {
            if (!number.getTrace().remainder(mod).equals(BigInteger.ZERO)) {
                return false;
            }
        }
        return true;
    }

    /**
	 * Вычисляет коэффициент перед t в alpha в соответствии с формулами {@link MainDivisibilityLemma}
	 * alpha - 1 = z / 2 * (bd + 1) / bd  * t
	 * @param ps источник свойств
	 * @param partition текущее разбиение
	 */
    public static BigInteger getAlphaCoefficient(IPropertySource ps, Partition partition) {
        @SuppressWarnings("unchecked") Map<Partition, BigInteger> map = (Map<Partition, BigInteger>) ps.getValue(ALPHA_COEFFICIENTS);
        if (!map.containsKey(partition)) {
            QuadraticInteger character = QuadraticFieldsInitializer.getCharacter(ps, partition);
            BigInteger bd = character.getB().multiply(character.getD());
            map.put(partition, AlternatingCharTableUtil.calcZX(partition).multiply(bd.add(BigInteger.ONE)).divide(BigInteger.valueOf(2)).divide(bd));
        }
        return map.get(partition);
    }

    public static BigInteger calcAlpha(IPropertySource ps, Partition partition, BigInteger t, BigInteger mod) {
        return getAlphaCoefficient(ps, partition).multiply(t).add(BigInteger.ONE).mod(mod);
    }

    /**
	 * Вычисляет коэффициент перед t в beta в соответствии с формулами {@link MainDivisibilityLemma}
	 * beta = - z / bd * t
	 * @param ps источник свойств
	 * @param partition текущее разбиение
	 */
    public static BigInteger getBetaCoefficient(IPropertySource ps, Partition partition) {
        @SuppressWarnings("unchecked") Map<Partition, BigInteger> map = (Map<Partition, BigInteger>) ps.getValue(BETA_COEFFICIENTS);
        if (!map.containsKey(partition)) {
            QuadraticInteger character = QuadraticFieldsInitializer.getCharacter(ps, partition);
            BigInteger bd = character.getB().multiply(character.getD());
            map.put(partition, AlternatingCharTableUtil.calcZX(partition).multiply(BigInteger.valueOf(-1)).divide(bd));
        }
        return map.get(partition);
    }

    public static BigInteger calcBeta(IPropertySource ps, Partition partition, BigInteger t, BigInteger mod) {
        return getBetaCoefficient(ps, partition).multiply(t).mod(mod);
    }

    public static List<Pair<BigInteger, BigInteger>> listGoodPeriods(IPropertySource ps, Partition partition, BigInteger mod) {
        return _periodExtractor.listGoodPeriods(ps, partition, mod);
    }
}
