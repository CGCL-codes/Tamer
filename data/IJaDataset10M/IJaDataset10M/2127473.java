package jssia.database.mysql.columns;

public class TextColumn extends StringColumn {

    private static final int MAX_CHARS = 65535;

    public TextColumn() {
    }

    @Override
    public long getMaxLength() {
        return MAX_CHARS;
    }
}
