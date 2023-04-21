package javax.time;

/**
 * Mock rules that returns a leap second on day 1000.
 *
 * @author Stephen Colebourne
 */
public class MockUTCRulesLeapOn1000 extends UTCRules {

    @Override
    public String getName() {
        return "Mock1000";
    }

    @Override
    public int getLeapSecondAdjustment(long mjDay) {
        return (mjDay == 1000 ? 1 : 0);
    }

    @Override
    public int getTAIOffset(long mjDay) {
        return (mjDay <= 1000 ? 10 : 11);
    }

    @Override
    public long[] getLeapSecondDates() {
        return new long[] { 1000 };
    }

    @Override
    public TAIInstant convertToTAI(UTCInstant utcInstant) {
        return null;
    }

    @Override
    public UTCInstant convertToUTC(TAIInstant taiInstant) {
        return null;
    }
}
