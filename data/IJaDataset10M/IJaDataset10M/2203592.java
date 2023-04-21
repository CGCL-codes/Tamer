package gnu.testlet.java2.lang.Character;

import gnu.testlet.Testlet;
import gnu.testlet.TestHarness;

public class consts implements Testlet {

    public void test(TestHarness harness) {
        harness.check(Character.SPACE_SEPARATOR, 12);
        harness.check(Character.LINE_SEPARATOR, 13);
        harness.check(Character.PARAGRAPH_SEPARATOR, 14);
        harness.check(Character.UPPERCASE_LETTER, 1);
        harness.check(Character.LOWERCASE_LETTER, 2);
        harness.check(Character.TITLECASE_LETTER, 3);
        harness.check(Character.MODIFIER_LETTER, 4);
        harness.check(Character.OTHER_LETTER, 5);
        harness.check(Character.DECIMAL_DIGIT_NUMBER, 9);
        harness.check(Character.LETTER_NUMBER, 10);
        harness.check(Character.OTHER_NUMBER, 11);
        harness.check(Character.NON_SPACING_MARK, 6);
        harness.check(Character.ENCLOSING_MARK, 7);
        harness.check(Character.COMBINING_SPACING_MARK, 8);
        harness.check(Character.DASH_PUNCTUATION, 20);
        harness.check(Character.START_PUNCTUATION, 21);
        harness.check(Character.END_PUNCTUATION, 22);
        harness.check(Character.CONNECTOR_PUNCTUATION, 23);
        harness.check(Character.OTHER_PUNCTUATION, 24);
        harness.check(Character.MATH_SYMBOL, 25);
        harness.check(Character.CURRENCY_SYMBOL, 26);
        harness.check(Character.MODIFIER_SYMBOL, 27);
        harness.check(Character.OTHER_SYMBOL, 28);
        harness.check(Character.CONTROL, 15);
        harness.check(Character.FORMAT, 16);
        harness.check(Character.UNASSIGNED, 0);
        harness.check(Character.PRIVATE_USE, 18);
        harness.check(Character.SURROGATE, 19);
    }
}
