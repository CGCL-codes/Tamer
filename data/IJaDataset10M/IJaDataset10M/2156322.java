package org.comptahome.metier;

import java.util.Date;
import org.comptahome.model.Compte;
import org.comptahome.model.Operation;

public interface IGererOperation {

    Operation creer(Compte compte);

    Operation creer(Compte compte, Date date, String numCheque, String beneficiaire, int typeOperation, float montant);

    void supprimer(Operation operation);

    Operation modifier(Operation operation);

    Operation lire(Long id);
}
