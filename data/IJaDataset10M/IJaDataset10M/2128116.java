package javaapplication;

import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

public class XItemPlancheCaseACocherCarree extends XItemPlancheCaseACocher {

    public static BufferedImage imgCocheOui;

    public static BufferedImage imgCocheNon;

    public XItemPlancheCaseACocherCarree(String nom) {
        super(nom);
        if (imgCocheOui == null) {
            try {
                imgCocheOui = ImageIO.read(new File("casecarree1.gif"));
                imgCocheNon = ImageIO.read(new File("casecarree0.gif"));
            } catch (IOException e) {
                System.out.println("Erreur dans l'ouverture des images de case � cocher.");
            }
        }
    }

    protected void clicSouris() {
        cocher(!estCochee());
    }

    protected BufferedImage imgCocheOui() {
        return imgCocheOui;
    }

    protected BufferedImage imgCocheNon() {
        return imgCocheNon;
    }
}
