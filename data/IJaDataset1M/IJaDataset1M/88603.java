package server.pd.map;

import common.persistence.io.IPersistenceStreetField;

public interface IStreetFieldFactory {

    public StreetField createField(IPersistenceStreetField _objPersField);
}
