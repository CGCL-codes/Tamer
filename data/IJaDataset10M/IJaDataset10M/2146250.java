package com.west.testEngine.rcp.pages;

import com.netprogress.formclipse.genericTableElements.TGenericOperator;
import com.west.testEngine.rcp.model.Classe;
import com.west.testEngine.rcp.model.Panier;

public class MatiereEnseignantOperator extends TGenericOperator {

    @SuppressWarnings("unchecked")
    public MatiereEnseignantOperator(Integer editStatus, int[] elementsType, Object[] objs) {
        super(editStatus, elementsType, objs);
        Classe classe = (Classe) objs[0];
        Panier panier = (Panier) objs[1];
        java.lang.String libelleMatiere = (String) objs[2];
        java.lang.String volumeHoraire = (String) objs[3];
        this.content.add(classe);
        this.content.add(panier);
        this.content.add(libelleMatiere);
        this.content.add(volumeHoraire);
    }
}
