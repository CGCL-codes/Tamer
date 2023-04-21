package net.fuhrparkservice.service;

import net.fuhrparkservice.model.FahrzeugType;
import net.fuhrparkservice.model.Filiale;

public interface IVerleihServiceExtended extends IVerleihService {

    public FahrzeugType getSelectedFahrzeug();

    public Filiale getSelectedFiliale();
}
