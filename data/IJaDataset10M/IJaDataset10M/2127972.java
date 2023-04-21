package clustering.framework;

import org.w3c.dom.*;

/**
 * @author Tudor.Ionescu@supelec.fr

IClusterTreeComparer

Every cluster tree comparer class has to implement this interface. A cluster tree comparer is a class that computes a distance or another type of similarity measure between two trees, which are the results of a tree construction process.

Method(s):

double Compare(Document xmlTree1, Document xmlTree2, double [][] dMatrix1, double [][] dMatrix2, String [] filesList1, String [] filesList2);

The xmlTree1 and xmlTree2 parameters denote the two trees to be compared, given in XML format. The dMatrix1 and dMatrix2 parameters represent two n x n, respectively m x m distances matrices of type double, where n is the length of filesList1 and m the length of filesList2. The two files lists contain the paths to the files to be clustered. The trees are the results of applying a method for clustering the files from the two lists. The method returns a double value which represents the estimation of a distance or another similarity measure between xmlTree1 and xmlTree2.

 */
public interface IClusterTreeComparer {

    double Compare(Document xmlTree1, Document xmlTree2, double[][] dMatrix1, double[][] dMatrix2, String[] filesList1, String[] filesList2);
}
