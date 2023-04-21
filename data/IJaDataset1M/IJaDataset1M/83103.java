package squirrel_util;

public class Lang {

    public Lang() {
    }

    public static void ASSERT(boolean assertCondition, String conditionViolatedMessage) {
        if (!assertCondition) throw new AssertException(conditionViolatedMessage);
    }

    public static void ASSERT_EQUAL(long longValue1, long longValue2, String value1Name) {
        ASSERT(longValue1 == longValue2, "The \"" + value1Name + "\" must be equal to " + longValue2 + ", but it is " + longValue1 + ".");
    }

    public static void ASSERT_FALSE() {
        ASSERT(false, "This point should never be reached.");
    }

    public static void ASSERT_FALSE(String locationName) {
        ASSERT(false, "This point should never be reached: " + StringUtil.toPrintableString(locationName));
    }

    public static void ASSERT_IN_RANGE(long longValue, long min, long max, String valueName) {
        ASSERT(longValue >= min && longValue <= max, "The \"" + valueName + "\" should be in range of [" + min + "..." + max + "], but it is " + longValue + ".");
    }

    public static void ASSERT_NON_NEGATIVE(long longValue, String valueName) {
        ASSERT(longValue >= 0L, "The \"" + valueName + "\" should always be non-negative here, but it is " + longValue + ".");
    }

    public static void ASSERT_NOT_NULL(Object objectValue, String valueName) {
        ASSERT(objectValue != null, "Assert violated: the " + StringUtil.toPrintableString(valueName) + " should never be null here, but it is.");
    }

    public static void ASSERT_NOT_NULL_NOR_EMPTY(String stringValue, String valueName) {
        ASSERT(!StringUtil.isNullOrEmpty(stringValue), "the string named " + StringUtil.toPrintableString(valueName) + " should never be null or empty, but it is " + StringUtil.toPrintableString(stringValue));
    }

    public static void ASSERT_NOT_NULL_NOR_TRIMMED_EMPTY(String stringValue, String valueName) {
        ASSERT(!StringUtil.isNullOrTrimmedEmpty(stringValue), "The string named " + StringUtil.toPrintableString(valueName) + " should be neither null, empty, nor contain whitespace only, but it is " + StringUtil.toPrintableString(stringValue));
    }

    public static void ASSERT_POSITIVE(long longValue, String valueName) {
        ASSERT(longValue > 0L, "The \"" + valueName + "\" should always be positive here, but it is " + longValue + ".");
    }

    public static void EXPECT(boolean conditionExpected, String errorMessage) throws ExpectException {
        if (!conditionExpected) throw new ExpectException(errorMessage);
    }

    public static void EXPECT_FALSE() throws ExpectException {
        EXPECT(false, ("This point should never be reached."));
    }

    public static void EXPECT_FALSE(String locationName) throws ExpectException {
        EXPECT(false, "This point should never be reached: " + StringUtil.toPrintableString(locationName));
    }

    public static void EXPECT_NON_NEGATIVE(long longValue, String valueName) throws ExpectException {
        EXPECT(longValue >= 0L, ("The value named") + " \"" + valueName + "\" " + ("should be zero or greater than zero, but it is") + " " + longValue + ".");
    }

    public static void EXPECT_NOT_NULL(Object objectValue, String valueName) throws ExpectException {
        EXPECT(objectValue != null, "Non-null object " + StringUtil.toPrintableString(valueName) + " expected, but it is null.");
    }

    public static void EXPECT_NOT_NULL_NOR_EMPTY(String stringValue, String valueName) throws ExpectException {
        EXPECT(!StringUtil.isNullOrEmpty(stringValue), "The string named " + StringUtil.toPrintableString(valueName) + " should be neither null nor empty, but it is " + StringUtil.toPrintableString(stringValue));
    }

    public static void EXPECT_NOT_NULL_NOR_TRIMMED_EMPTY(String stringValue, String valueName) throws ExpectException {
        EXPECT(!StringUtil.isNullOrEmpty(stringValue), "The string named " + StringUtil.toPrintableString(valueName) + " should be neither null, empty, nor contain whitespace only, but it is " + StringUtil.toPrintableString(stringValue));
    }

    public static void EXPECT_POSITIVE(long longValue, String valueName) throws ExpectException {
        EXPECT(longValue > 0L, "The \"" + valueName + "\" should be greater than zero, but it is " + longValue + ".");
    }

    public static void NOT_IMPLEMENTED() {
        ASSERT(false, "Not yet implemented.");
    }

    public static void NOT_IMPLEMENTED(String message) {
        ASSERT(false, "Not yet implemented: " + message);
    }

    public static void NOT_IMPLEMENTED_SOFT() {
        Exception ex = new Exception("Notice: not yet implemented.");
        Logger.printException(ex);
    }

    public static void NOT_IMPLEMENTED_SOFT(String message) {
        Exception ex = new Exception("Notice: not yet implemented: " + message);
        Logger.printException(ex);
    }
}
