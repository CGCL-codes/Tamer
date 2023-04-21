package net.fuhrparkservice.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "Dali", date = "2012-01-05T22:08:25.057+0100")
@StaticMetamodel(FahrzeugItem.class)
public class FahrzeugItem_ extends AbstractBusinessObject_ {

    public static volatile SingularAttribute<FahrzeugItem, FahrzeugType> type;

    public static volatile SingularAttribute<FahrzeugItem, Filiale> standort;

    public static volatile ListAttribute<FahrzeugItem, Filiale> standortHistory;
}
