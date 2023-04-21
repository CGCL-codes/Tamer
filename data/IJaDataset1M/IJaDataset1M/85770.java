package net.fuhrparkservice.service;

import java.util.List;
import net.fuhrparkservice.model.FahrzeugType;

public interface IFahrzeugCRUD extends IFahrzeugService {

    public FahrzeugType getFahrzeugById(String id);

    public List<FahrzeugType> getFahrzeugeByFabrikat(String fabrikat);

    public void insert(FahrzeugType fahrzeug2Insert);

    public void update(FahrzeugType fahrzeug2Update);

    public void remove(String id);
}
