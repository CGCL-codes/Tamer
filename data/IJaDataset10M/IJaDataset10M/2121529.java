package javaapplication;

public class XPlancheCriteresEnroule extends XPlanche {

    public XPlancheCriteresEnroule() {
        super();
    }

    protected void initComponents() {
        XItemPlancheBouton jPanel1 = new XItemPlancheBoutonMenu("Villes étapes", XPlancheCriteresVillesEtapes.getCmdInstance());
        XItemPlancheBouton jPanel2 = new XItemPlancheBoutonMenu("Crit�res routes", XPlancheCriteresRoutes.getCmdInstance());
        XItemPlancheBouton jPanel3 = new XItemPlancheBoutonMenu("Tourisme", XPlancheCriteresTourisme.getCmdInstance());
        XItemPlancheBouton jPanel4 = new XItemPlancheBoutonMenu("Confirmer crit�res", XPlancheSaisieItineraireVilles.getCmdInstance());
        addItemPlanche(jPanel1);
        addItemPlanche(jPanel2);
        addItemPlanche(jPanel3);
        addSeparator();
        addItemPlanche(jPanel4);
    }

    private static XPlancheCriteresEnroule instance;

    private static CmdInstanciation cmdInstance;

    public static XPlancheCriteresEnroule getInstance() {
        if (instance == null) {
            return instance = new XPlancheCriteresEnroule();
        } else {
            return instance;
        }
    }

    public static CmdInstanciation getCmdInstance() {
        if (cmdInstance == null) {
            return cmdInstance = new CmdInstanciation();
        } else {
            return cmdInstance;
        }
    }

    private static class CmdInstanciation extends XPlanche.CmdInstanciation {

        public XPlanche getInstance() {
            return XPlancheCriteresEnroule.getInstance();
        }
    }
}
