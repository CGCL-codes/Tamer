package jssia.database.mysql.columns;

public class MediumTextColumn extends StringColumn {

    private static final int MAX_CHARS = 16777215;

    public MediumTextColumn() {
    }

    @Override
    public long getMaxLength() {
        return MAX_CHARS;
    }
}
