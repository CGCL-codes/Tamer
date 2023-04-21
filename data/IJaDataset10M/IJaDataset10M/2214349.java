package jpicedt.graphic.util;

import jpicedt.graphic.PicPoint;
import jpicedt.graphic.PicVector;
import jpicedt.graphic.model.*;

/**
 * Ensemble de fonctions statiques d'alg�bre lin�aire dans un espace vectoriel
 * r�el de dimension 2.
 *
 * @author <a href="mailto:vincentb1@users.sourceforge.net">Vincent Bela�che</a>
 * @version $Id: LinearAlgebra.java,v 1.3 2010/07/14 14:15:46 vincentb1 Exp $
 * @since jPicEdt 1.6
 */
public class LinearAlgebra {

    /**  Fait le produit d'un matrice 2x2 <code>matrix</code> et d'un vecteur <code>x</code>.  
	  * @param matrix Les points dans <code>matrix</code> sont rang�s comme �a:<br>
	  *  matrix= { {matrix_x_x, matrix_x_y},{matrix_y_x,matrix_y_y}};<br>
	  *  o� le premier indice d�signe la ligne, et le second la colonne 
	  * @param x objet de l'application lin�aire d�finie par matrix.
	  * @return image du point <code>x</code> par l'application lin�raire d�finie par <code>matrix</code>
	  * @since jPicEdt 1.6
	  */
    public static PicVector linearApplication(double[][] matrix, PicPoint x) {
        PicVector ret = new PicVector();
        ret.setLocation(x.getX() * matrix[0][0] + x.getY() * matrix[0][1], x.getX() * matrix[1][0] + x.getY() * matrix[1][1]);
        return ret;
    }

    /**
	 * Renvoie une matrice dont <code>n1</code> transpos� est la premi�re
	 * ligne, et <code>n2</code> transpos� la seconde ligne (o� l'on consid�re
	 * les <code>PicVector</code> comme des vecteurs colonne).
	 *
	 * @param n1 une valeur <code>PicVector</code> pour le premier vecteur normal
	 * @param n2 une valeur <code>PicVector</code> pour le second vecteur normal
	 * @return une valeur <code>double[][]</code> pour la matrice 2x2 renvoy�e
	 */
    public static double[][] normalVectorsToMatrix(PicVector n1, PicVector n2) {
        double[][] ret = { { n1.getX(), n1.getY() }, { n2.getX(), n2.getY() } };
        return ret;
    }

    /**
	 * Calcul l'image de <code>x</code> par une application lin�aire d�finie
	 * par l'inverse <code>matrix</code>.
	 *
	 * Si <code>matrix</code> n'est pas inversible, il se produit une division
	 * par 0.
	 *
	 * Le calcul est faire selon la r�gle de Cramer
	 *
	 * @see <a href="http://fr.wikipedia.org/wiki/R%C3%A8gle_de_Cramer">R�gle de Cramer</a>
	 * @param matrix une valeur <code>double</code> 
	 * @param x une valeur <code>PicVector</code> 
	 * @return la valeur <code>PicVector</code> image de <code>x</code> par
	 * l'inverse de <code>matrix</code>.
	 */
    public static PicVector invLinearApplication(double[][] matrix, PicPoint x) {
        double invDet = 1.0 / (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]);
        PicVector ret = new PicVector();
        ret.setCoordinates((x.getX() * matrix[1][1] - x.getY() * matrix[0][1]) * invDet, (matrix[0][0] * x.getY() - matrix[1][0] * x.getX()) * invDet);
        return ret;
    }

    /** effectue le produit des matrices 2x2 matrix1 et matrix2 
	 * @since jPicEdt 1.6
	 */
    public static double[][] matrixProduct(double[][] matrix1, double[][] matrix2) {
        double[][] ret = new double[2][2];
        ret[0][0] = (matrix1[0][0] * matrix2[0][0] + matrix1[0][1] * matrix2[1][0]);
        ret[0][1] = (matrix1[0][0] * matrix2[0][1] + matrix1[0][1] * matrix2[1][1]);
        ret[1][0] = (matrix1[1][0] * matrix2[0][0] + matrix1[1][1] * matrix2[1][0]);
        ret[1][1] = (matrix1[1][0] * matrix2[0][1] + matrix1[1][1] * matrix2[1][1]);
        return ret;
    }

    /** Application lin�aire sur chaque point de contr�le d'un AbstractElement
	 * @param matrix matrice d�finissant l'application lin�aire
	 * @param abstElt AbstractElement objet de l'application lin�raire.
	 * @since jPicEdt 1.6
	 */
    public static void abstractEltLinAp(double[][] matrix, Element abstElt) {
        PicPoint[] ctrlPoints = new PicPoint[abstElt.getLastPointIndex() - abstElt.getFirstPointIndex() + 1];
        for (int i = abstElt.getFirstPointIndex(); i <= abstElt.getLastPointIndex(); ++i) {
            PicPoint ctrlPt = linearApplication(matrix, abstElt.getCtrlPt(i, null));
            ctrlPoints[i - abstElt.getFirstPointIndex()] = ctrlPt;
        }
        for (int i = abstElt.getFirstPointIndex(); i <= abstElt.getLastPointIndex(); ++i) {
            abstElt.setCtrlPt(i, ctrlPoints[i - abstElt.getFirstPointIndex()]);
        }
    }
}

;
