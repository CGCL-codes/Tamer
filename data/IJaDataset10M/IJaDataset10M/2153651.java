package org.comptahome.metier;

import org.comptahome.model.Affectation;
import org.comptahome.model.Categorie;
import org.comptahome.model.Operation;
import org.comptahome.util.ExceptionApplicative;

public interface IGererAffectation {

    Affectation creer(Operation operation, Categorie categorie, int pourcentage) throws ExceptionApplicative;

    void supprimer(Affectation affectation);

    Affectation modifier(Affectation affectation);

    Affectation lire(Long id) throws ExceptionApplicative;
}
