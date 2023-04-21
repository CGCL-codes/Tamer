package org.freedom.bmps;

import java.net.URL;
import javax.swing.ImageIcon;

public class Icone {

    public static String dirImages = "/org/freedom/images/";

    public Icone() {
    }

    public static ImageIcon novo(String nome) {
        ImageIcon retorno = null;
        try {
            URL url = Icone.class.getResource(dirImages + nome);
            if (url == null) System.out.println("N�o foi poss�vel carregar a imagem: '" + nome + "'"); else retorno = new ImageIcon(java.awt.Toolkit.getDefaultToolkit().getImage(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retorno;
    }
}
