package com.uprightpath.idylls.collections;

/**
 * This class is intended to provide the basis of both dense and sparse
 * three-dimensional matrix of various sorts. The intent is to provide a unified
 * set of methods that can be used for both sparse and dense matrices.
 *
 * In this implementation the row, column and layer sizes of the matrix is fixed at the
 * time of creation.
 *
 * NOTE:
 * The current implementation is more for storage than to provide access to
 * mathematical operations that occur on matrixes.
 *
 * @param <Element>
 * @author Geo
 */
public class ThreeDimensionalMatrix<Element> {

    private final int rows;

    private final int columns;

    private final int layers;

    private final TwoDimensionalMatrix<Element>[] matrixLayers;

    private final MatrixDensity density;

    /**
     *
     * @param rows
     * @param columns
     * @param layers
     * @param density
     */
    protected ThreeDimensionalMatrix(int rows, int columns, int layers, MatrixDensity density) {
        this.rows = rows;
        this.columns = columns;
        this.layers = layers;
        this.matrixLayers = new TwoDimensionalMatrix[this.layers];
        this.density = density;
        for (int i = 0; i < layers; i++) {
            this.setMatrixLayer(i, new TwoDimensionalMatrix<Element>(rows, columns, density));
        }
    }

    /**
     * Performs a shallow copy of the current matrix over the one provided.
     *
     * Requires that the matrices are of the same dimensions.
     *
     * @param newMatrix a non-null matrix to attempt to copy over.
     */
    public void copyOver(ThreeDimensionalMatrix<Element> newMatrix) {
        if (rows == newMatrix.getRows() && columns == newMatrix.getColumns() && layers == newMatrix.getLayers()) {
            TwoDimensionalMatrix<Element> matrix;
            for (int i = 0; i < layers; i++) {
                matrix = new TwoDimensionalMatrix<Element>(rows, columns, density);
                this.matrixLayers[i].copyOver(matrix);
                newMatrix.setMatrixLayer(i, matrix);
            }
        } else {
            throw new IllegalArgumentException("Matrix dimensions do not match.");
        }
    }

    /**
     * Returns the number of columns in the matrix.
     * @return the number of columns matrix.
     */
    public int getColumns() {
        return columns;
    }

    /**
     * Returns the number of layers in the matrix.
     * @return the number of layers matrix.
     */
    public int getLayers() {
        return layers;
    }

    /**
     * Returns the TwoDimensionalMatrix that comprises a given layer.
     * @param layer the layer index
     * @return the TwoDimensionalMatrix at a give layer
     */
    public TwoDimensionalMatrix<Element> getMatrixLayer(int layer) {
        if (layer >= 0 && layer < layers) {
            return matrixLayers[layer];
        } else {
            throw new IndexOutOfBoundsException("Index: [" + layer + "], Size: [" + this.getLayers() + "]");
        }
    }

    /**
     * Returns the rows of the matrix.
     * @return the rows of the matrix.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the density type of the matrix.
     * @return the density of the matrix.
     */
    public MatrixDensity getDensity() {
        return density;
    }

    /**
     * Returns the number of elements in the matrix.
     *
     * In the general case, this should be the number of non-null intersections
     * of row and column.
     * @return the number of elements in the matrix.
     */
    public final int getElementCount() {
        int count = 0;
        for (TwoDimensionalMatrix matrix : matrixLayers) {
            count += matrix.getElementCount();
        }
        return count;
    }

    /**
     * Returns true if the matrix is empty.
     * @return true if the matrix is empty.
     */
    public final boolean isEmptyMatrix() {
        return getElementCount() == 0;
    }

    /**
     * Returns true if the row and column intersect contains no element.
     * @param row
     * @param column
     * @param layer
     * @return true if the row and column intersect contains no element.
     */
    public final boolean isEmpty(int row, int column, int layer) {
        if (isInBounds(row, column, layer)) {
            return matrixLayers[layer].isEmpty(row, column);
        } else {
            throw new IndexOutOfBoundsException("Index: [" + row + "][" + column + "][" + layer + "], Size: [" + this.getRows() + "][" + this.getColumns() + "][" + this.getLayers() + "]");
        }
    }

    /**
     * Removes all elements from this matrix.
     *
     * Every intersection of row and column should now have a null value.
     */
    public final void clear() {
        for (TwoDimensionalMatrix matrix : matrixLayers) {
            matrix.clear();
        }
    }

    /**
     * Removes all elements from a specific layer of the matrix.
     *
     * Every intersection of row and column in that layer should now have a null
     * value.
     *
     * @param layer
     */
    public final void clearLayer(int layer) {
        if (layer > 0 && layer < this.layers) {
            matrixLayers[layer].clear();
        } else {
            throw new IndexOutOfBoundsException("Index: [" + layer + "], Size: [" + this.getLayers() + "]");
        }
    }

    /**
     * Method used to set the intersection of row and column to a be a specific
     * element.
     *
     * This method is exposed. However, beyond checking responding to a row and
     * column that was outside of the matrix's range it does nothing, instead
     * allowing setElementImplement(int, int, Element) handle the actual
     * operation.
     *
     * Setting an element to null causes an
     *
     * @param row the row
     * @param column the column
     * @param layer the layer
     * @param element the element to be placed at the intersection of row, column and layer
     * column
     */
    public final void setElement(int row, int column, int layer, Element element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null.");
        } else if (isInBounds(row, column, layer)) {
            matrixLayers[layer].setElement(row, column, element);
        } else {
            throw new IndexOutOfBoundsException("Index: [" + row + "][" + column + "][" + layer + "], Size: [" + this.getRows() + "][" + this.getColumns() + "][" + this.getLayers() + "]");
        }
    }

    /**
     * Method that sets a matrix layer to a specific value.
     *
     * @param layer the layer to be set
     * @param matrixLayer the new Layer value
     */
    protected final void setMatrixLayer(int layer, TwoDimensionalMatrix<Element> matrixLayer) {
        matrixLayers[layers] = matrixLayer;
    }

    /**
     * Method used to get the intersection of row and column to a be a specific
     * element.
     *
     * This method is exposed. However, beyond checking responding to a row and
     * column that was outside of the matrix's range it does nothing, instead
     * allowing setElementImplement(int, int, Element) handle the actual
     * operation.
     *
     * @param row the row
     * @param column the column
     * @param layer
     * @layer the layer
     * @return the Element at the intersection of row, column and layer (Or null if none exists)
     */
    public final Element getElement(int row, int column, int layer) {
        if (isInBounds(row, column, layer)) {
            return matrixLayers[layer].getElement(row, column);
        } else {
            throw new IndexOutOfBoundsException("Index: [" + row + "][" + column + "][" + layer + "], Size: [" + this.getRows() + "][" + this.getColumns() + "][" + this.getLayers() + "]");
        }
    }

    /**
     * Method used to retrieve and remove the intersection of row and column to a be a specific
     * element.
     *
     * This method is exposed. However, beyond checking responding to a row and
     * column that was outside of the matrix's range it does nothing, instead
     * allowing setElementImplement(int, int, Element) handle the actual
     * operation.
     *
     *
     *
     * @param row the row
     * @param column the column
     * @param layer
     * @layer the layer
     * @return the Element at the intersection of row, column and layer (Or null if none exists)
     */
    public final Element removeElement(int row, int column, int layer) {
        if (isInBounds(row, column, layer)) {
            return matrixLayers[layer].removeElement(row, column);
        } else {
            throw new IndexOutOfBoundsException("Index: [" + row + "][" + column + "][" + layer + "], Size: [" + this.getRows() + "][" + this.getColumns() + "][" + this.getLayers() + "]");
        }
    }

    /**
     * Method to check whether a given row, column and layer intersection is within the
     * bounds of the matrix. It does not check if it has a value.
     * @param row the row
     * @param column the column
     * @param layer the layer
     * @return true if the row, column and layer are inside the matrix
     */
    public final boolean isInBounds(int row, int column, int layer) {
        return layer >= 0 && layer < layers && matrixLayers[layer].isInBounds(row, column);
    }
}
