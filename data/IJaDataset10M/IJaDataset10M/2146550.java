package tests;

import java.util.Vector;
import visualisationRadar.VisualisationRadar;
import metiers.FondCarte;
import metiers.PointFond;

public class FondCarteTest {

    public static void main(String[] args) {
        System.out.println("FondCarteTest");
        VisualisationRadar.repertoire = MainProgram.path;
        VisualisationRadar.repertoireRessources = VisualisationRadar.repertoire + "ressources/";
        FondCarte fondCarteTest = FondCarte.getInstance();
        String radarNameTest = "Cherves";
        fondCarteTest.setFondCarte(radarNameTest);
        if (fondCarteTest.getPoints() != null) System.out.println("FondCarteTest 1/2 : ok"); else System.err.println("FondCarteTest 1/2 : erreur");
        Vector<PointFond> vecteurPointsTest = fondCarteTest.getPoints();
        for (int i = 0; i < vecteurPointsTest.size(); i++) {
            if (!(vecteurPointsTest.get(i).getX() >= 0 && vecteurPointsTest.get(i).getX() <= 511 && vecteurPointsTest.get(i).getY() >= 0 && vecteurPointsTest.get(i).getY() <= 511)) {
                System.err.println("FondCarteTest 2/2 : erreur");
                break;
            } else if (vecteurPointsTest.size() == i) System.out.println("FondCarteTest 2/2 : ok");
        }
        System.out.println("FondCarteTest fin\n");
    }
}
