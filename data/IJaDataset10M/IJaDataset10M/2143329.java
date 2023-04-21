package fitgoodies.parsers;

public class LongParserMock implements Parser<Long> {

    @Override
    public final Long parse(final String s, final String parameter) throws Exception {
        if (parameter != null) {
            return Long.valueOf(7);
        } else {
            return Long.valueOf(2);
        }
    }

    @Override
    public final Class<Long> getType() {
        return Long.class;
    }
}
