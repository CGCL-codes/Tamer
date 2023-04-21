package pnc.fractal.data;

import java.io.Serializable;
import java.math.*;

/**
 *La classe <code>ComplexBigDecimal</code> repr�sente un nombre complexe 
 *avec une coordonn�e r��lle et une imaginaire en BigDecimal.
 */
public final class ComplexBigDecimal implements Serializable {

    private BigDecimal re;

    private BigDecimal im;

    private int scale;

    /**
   *Le constructeur de ComplexBigDecimal prend deux param�tres.
   *@param re : repr�sente la coordonn�e r��lle en BigDecimal
   *@param im : repr�sente la coordonn�e imaginaire en BigDecimal
   *@param scale : repr�sente le nombre de d�cimal des BigDecimaux
   */
    public ComplexBigDecimal(BigDecimal re, BigDecimal im, int scale) {
        this.scale = scale;
        this.re = new BigDecimal(re.toString());
        this.re = this.re.setScale(this.scale, BigDecimal.ROUND_DOWN);
        this.im = new BigDecimal(im.toString());
        this.im = this.im.setScale(this.scale, BigDecimal.ROUND_DOWN);
    }

    /**
   *La m�thode <code>carre</code>fait le carre de ce nombre complexe
   */
    public void carre() {
        BigDecimal tmp = new BigDecimal(re.toString());
        BigDecimal deux = new BigDecimal(2.0);
        re = (re.multiply(tmp)).subtract(im.multiply(im));
        re = re.setScale(scale, BigDecimal.ROUND_DOWN);
        im = (im.multiply(tmp)).multiply(deux);
        im = im.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
   *La m�thode <code>add</code> ajoute un nombre complexe � celui-ci
   *@param c: nombre complexe en BigDecimal a ajout�
   */
    public void add(ComplexBigDecimal c) {
        this.re = this.re.add(c.re);
        this.re = this.re.setScale(scale, BigDecimal.ROUND_DOWN);
        this.im = this.im.add(c.im);
        this.im = this.im.setScale(scale, BigDecimal.ROUND_DOWN);
    }

    /**
   *La m�thode <code>dist</code> renvoi la distance d'�loignement 
   *du point calcul� � l'it�ration n par rapport au point de d�part.
   *On peut l'assimiler � un indice de convergence
   *@return : un BigDecimal repr�sentant la distance
   */
    public BigDecimal dist() {
        BigDecimal tmp = (re.multiply(re)).add(im.multiply(im));
        tmp = tmp.setScale(scale, BigDecimal.ROUND_DOWN);
        return tmp;
    }

    /**
   * La fonction <code>toString</code> renvoie une cha�ne de caract�res repr�sentant ComplexBigDecimal
   * @return une cha�ne de caract�res repr�sentant ComplexBigDecimal.
   */
    public String toString() {
        String s = "re = " + re.toString() + ", im = " + im.toString();
        return s;
    }
}
