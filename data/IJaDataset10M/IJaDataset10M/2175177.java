package dbtools.structures;

public class DBFieldSmallInt extends DBField<Integer> {

    private static final long serialVersionUID = 1L;

    @Override
    public String getSqlType() {
        return "SMALLINT";
    }
}
