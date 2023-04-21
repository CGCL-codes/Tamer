package hidb2.kern;

/**
 * Timestamp attribut extension data:
 * - Time min
 * - Time max
 * - format
 */
public class AttrTimeStampChecker extends AttrTemporalChecker {

    @Override
    public String getDefaultFormat() {
        return C_DEFAULT_TIMESTAMPFORMAT;
    }
}
