package net.sourceforge.jds.mass.exact;

import net.sourceforge.jds.mass.IMassFunctionTest;

/**
 * @author Thomas Reineking
 *
 */
public class MassFunctionTest extends IMassFunctionTest<MassFunction<Character>, MassFunction<String>> {

    @Override
    protected MassFunction<Character> createCharacterMassFunction() {
        return new MassFunction<Character>();
    }

    @Override
    protected MassFunction<String> createStringMassFunction() {
        return new MassFunction<String>();
    }

    @Override
    protected double getEpsilon() {
        return 1E-10;
    }
}
