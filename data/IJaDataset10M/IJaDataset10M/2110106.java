package ch.unibe.inkml;

import java.util.Vector;
import org.w3c.dom.Element;
import ch.unibe.inkml.util.MatrixCalculator;

public class InkMatrix extends InkUniqueElement {

    public static final String ID_PREFIX = "affine";

    public static final String INKML_NAME = "affine";

    private double[][] matrix;

    private double[] translation;

    private double[][] inverse;

    private double[] retranslation;

    public InkMatrix(InkInk ink) {
        super(ink);
    }

    public InkMatrix(InkInk ink, String id) throws InkMLComplianceException {
        super(ink, id);
    }

    @Override
    public void buildFromXMLNode(Element node) throws InkMLComplianceException {
        super.buildFromXMLNode(node);
        buildMatrix(node.getTextContent());
    }

    @Override
    public void exportToInkML(Element parent) throws InkMLComplianceException {
        Element tableNode = parent.getOwnerDocument().createElement(INKML_NAME);
        super.exportToInkML(tableNode);
        tableNode.setTextContent(this.matrixToString());
        parent.appendChild(tableNode);
    }

    public void buildMatrix(String content) {
        Vector<Vector<Double>> t = new Vector<Vector<Double>>();
        String[] rows = content.split(",");
        for (String row : rows) {
            row = row.trim();
            if (row.isEmpty()) {
                continue;
            }
            String[] values = row.split(" ");
            Vector<Double> vrow = new Vector<Double>();
            for (String value : values) {
                value = value.trim();
                if (!value.isEmpty()) {
                    vrow.add(this.loadTableElement(value));
                }
            }
            t.add(vrow);
        }
        matrix = new double[t.size()][t.get(0).size() - 1];
        translation = new double[t.size()];
        for (int y = 0; y < t.size(); y++) {
            for (int x = 0; x < t.get(y).size() - 1; x++) {
                matrix[y][x] = t.get(y).get(x);
            }
            translation[y] = t.get(y).get(t.get(y).size() - 1);
        }
    }

    private double loadTableElement(String value) {
        String bool = value.toLowerCase().substring(0, 1);
        if (bool.equals("t")) {
            return 1;
        } else if (bool.equals("f")) {
            return 0;
        } else {
            return Double.parseDouble(value);
        }
    }

    private String matrixToString() {
        StringBuffer result = new StringBuffer();
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                result.append("" + matrix[y][x]);
                result.append(" ");
            }
            result.append("" + translation[y]);
            result.append(",");
        }
        return result.toString();
    }

    public double[] transform(double[] source) {
        double[] res = matrixVectorMultiplication(getMatrix(), source);
        return vectorAddition(res, translation);
    }

    public double[] backTransform(double[] vector) {
        double[][] inv = getInverse();
        double[] res = vectorAddition(vector, retranslation);
        return matrixVectorMultiplication(inv, res);
    }

    public void setMatrix(double[][] matrix, double[] translation) {
        this.matrix = matrix;
        this.translation = translation;
    }

    public double[][] getMatrix() {
        return this.matrix;
    }

    public static double[] matrixVectorMultiplication(double[][] matrix, double[] vector) {
        double[] result = new double[matrix.length];
        for (int y = 0; y < matrix.length; y++) {
            double value = 0;
            for (int x = 0; x < vector.length; x++) {
                value = value + matrix[y][x] * vector[x];
            }
            if (matrix[y].length == vector.length + 1) {
                value = value + matrix[y][vector.length];
            }
            result[y] = value;
        }
        return result;
    }

    public static double[] vectorAddition(double[] base, double[] add) {
        for (int i = 0; i < base.length; i++) {
            base[i] += add[i];
        }
        return base;
    }

    private double[][] getInverse() {
        if (inverse == null) {
            MatrixCalculator m = new MatrixCalculator();
            inverse = m.Inverse(matrix);
            retranslation = new double[translation.length];
            for (int i = 0; i < translation.length; i++) {
                retranslation[i] = translation[i] * -1;
            }
        }
        return inverse;
    }

    public boolean isIdentity() {
        boolean res = true;
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[y].length; x++) {
                res = res && matrix[y][x] == ((x == y) ? 1 : 0);
            }
        }
        return res;
    }

    public void flipAxis(int x, int y) {
        double tmp;
        for (int i = 0; i < matrix[x].length; i++) {
            tmp = matrix[x][i];
            matrix[x][i] = matrix[y][i];
            matrix[y][i] = tmp;
        }
        if (translation != null) {
            tmp = translation[x];
            translation[x] = translation[y];
            translation[y] = tmp;
        }
        retranslation = null;
        inverse = null;
    }

    public void invertAxis(int axis) {
        for (int i = 0; i < matrix[axis].length; i++) {
            matrix[axis][i] = -matrix[axis][i];
        }
        if (translation != null) {
            translation[axis] = -translation[axis];
        }
        retranslation = null;
        inverse = null;
    }

    public boolean isInvertible() {
        MatrixCalculator m = new MatrixCalculator();
        return matrix.length == matrix[0].length && m.Determinant(matrix) != 0;
    }

    /**
     * acctually performs the transformation
     * @param operand
     * @param product
     * @param sourceIndices
     * @param targetIndices
     * @param matrix
     */
    private void acutalTransform(double[][] operand, double[][] product, int[] oI, int[] pI, double[] productTranslation, double[] operandTranslation, double[][] matrix) {
        double[] opv = new double[matrix[0].length];
        for (int i = 0; i < operand.length; i++) {
            for (int x = 0; x < matrix[0].length; x++) {
                opv[x] = operand[i][oI[x]];
                if (operandTranslation != null) {
                    opv[x] += operandTranslation[x];
                }
            }
            for (int y = 0; y < matrix.length; y++) {
                product[i][pI[y]] = 0;
                for (int x = 0; x < matrix[y].length; x++) {
                    product[i][pI[y]] += opv[x] * matrix[y][x];
                }
                if (productTranslation != null) {
                    product[i][pI[y]] += productTranslation[y];
                }
            }
        }
    }

    /**
     * Mulitplies the sourcePoints with the matrix, result is stored in the targetPoints matrix.
     * sourceIndices and targetIndices specify the correct ordering/filtering of the columns of the sourcePoint matrix
     * or the targetPoint matrix
     * @param sourcePoints
     * @param targetPoints
     * @param sourceIndices
     * @param targetIndices
     */
    public void transform(double[][] sourcePoints, double[][] targetPoints, int[] sourceIndices, int[] targetIndices) {
        acutalTransform(sourcePoints, targetPoints, sourceIndices, targetIndices, translation, null, matrix);
    }

    /**
     * @param sourcePoints
     * @param targetPoints
     * @param sourceIndices
     * @param targetIndices
     */
    public void backtransform(double[][] sourcePoints, double[][] targetPoints, int[] sourceIndices, int[] targetIndices) {
        double[][] inverse = getInverse();
        acutalTransform(targetPoints, sourcePoints, targetIndices, sourceIndices, null, retranslation, inverse);
    }

    public InkMatrix clone(InkInk ink) {
        InkMatrix im = new InkMatrix(ink);
        if (matrix != null) im.matrix = matrix.clone();
        if (inverse != null) im.inverse = inverse.clone();
        if (translation != null) im.translation = translation.clone();
        if (retranslation != null) im.retranslation = retranslation.clone();
        return im;
    }

    public double[] getTranslation() {
        return translation;
    }
}
