package org.fudaa.dodico.corba.navigation;

/**
   * Reseau fluvial.
   * Pour ajouter un ouvrage, seul le nom est controle.
   */
public interface IReseauFluvialOperations extends org.fudaa.dodico.corba.objet.IObjetOperations {

    /**
     * Accepte si chaine non vide.
     */
    String nom();

    /**
     * Accepte si chaine non vide.
     */
    void nom(String newNom);

    org.fudaa.dodico.corba.navigation.IEcluseFluviale[] ecluses();

    org.fudaa.dodico.corba.navigation.IBiefNavigation[] biefs();

    org.fudaa.dodico.corba.navigation.IGare[] gares();

    String definitEcluses(org.fudaa.dodico.corba.navigation.IEcluseFluviale[] ecluses);

    String definitBiefs(org.fudaa.dodico.corba.navigation.IBiefNavigation[] biefs);

    String definitGares(org.fudaa.dodico.corba.navigation.IGare[] gares);

    String ajouteBief(org.fudaa.dodico.corba.navigation.IBiefNavigation bief);

    String enleveBief(org.fudaa.dodico.corba.navigation.IBiefNavigation bief);

    String ajouteEcluse(org.fudaa.dodico.corba.navigation.IEcluseFluviale ecluse);

    String enleveEcluse(org.fudaa.dodico.corba.navigation.IEcluseFluviale ecluse);

    String ajouteGare(org.fudaa.dodico.corba.navigation.IGare gare);

    String enleveGare(org.fudaa.dodico.corba.navigation.IGare gare);

    /**
     * Renvoie tous les navires generes pour ce reseau.
     */
    org.fudaa.dodico.corba.navigation.INavireType[] naviresGeneres();

    String genere(int nombreSeries);

    /**
     * Renvoie les gares non "utilisees" par un bief ou une ecluse.
     */
    org.fudaa.dodico.corba.navigation.IGare[] garesInutiles();

    org.fudaa.dodico.corba.navigation.IGare[] garesUtiles();

    void majGaresUtiles();

    /**
     * Teste si le graphe est contigu et renvoie les eventuelles gares isolees.
     * Si la fonction gareNonUtiles() renvoie un tableau non vide, il est clair
     * que le graphe n'est pas valide.
     */
    org.fudaa.dodico.corba.navigation.IGare[] garesIsolees();
}
