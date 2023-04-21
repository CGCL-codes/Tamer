package db;

public class DBRowLanguages extends DBRowAbstract {

    @Override
    public Class<? extends AbstractTableDataModel> getTableClass() {
        return LanguagesDataModel.class;
    }
}
