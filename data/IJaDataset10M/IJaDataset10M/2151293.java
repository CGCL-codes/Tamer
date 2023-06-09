package org.virbo.dataset;

/**
 * Extracts a subset of the source dataset by using a rank 1 subset of indeces on each index.
 * @author jbf
 */
public class SubsetDataSet extends AbstractDataSet {

    QDataSet source;

    QDataSet[] sorts;

    int[] lens;

    boolean nonQube = false;

    public SubsetDataSet(QDataSet source) {
        this.source = source;
        sorts = new QDataSet[QDataSet.MAX_RANK];
        lens = new int[QDataSet.MAX_RANK];
        if (!DataSetUtil.isQube(source)) {
            nonQube = true;
        }
        int[] lenss = DataSetUtil.qubeDims(source);
        if (nonQube) {
            lens[0] = source.length();
            sorts[0] = new IndexGenDataSet(lens[0]);
            for (int i = 1; i < source.rank(); i++) {
                lens[i] = Integer.MAX_VALUE;
                sorts[i] = new IndexGenDataSet(lens[i]);
            }
        } else {
            for (int i = 0; i < lenss.length; i++) {
                lens[i] = lenss[i];
                sorts[i] = new IndexGenDataSet(lenss[i]);
            }
        }
    }

    public void applyIndex(int idim, QDataSet idx) {
        if (nonQube && idim > 0) throw new IllegalArgumentException("unable to applyIndex on non-qube source dataset");
        sorts[idim] = idx;
        lens[idim] = idx.length();
        if (idx.rank() > 1) throw new IllegalArgumentException("rank>1");
        QDataSet dep = (QDataSet) source.property("DEPEND_" + idim);
        if (dep != null) {
            SubsetDataSet dim = new SubsetDataSet((QDataSet) source.property("DEPEND_" + idim));
            if (idim == 0) {
                QDataSet dep1 = (QDataSet) source.property(QDataSet.DEPEND_1);
                if (dep1 != null && dep1.rank() > 1) {
                    throw new IllegalArgumentException("not supported--we need to deal with this at some point");
                }
            }
            dim.applyIndex(0, idx);
            putProperty("DEPEND_" + idim, dim);
        }
    }

    @Override
    public int rank() {
        return source.rank();
    }

    @Override
    public int length() {
        return lens[0];
    }

    @Override
    public int length(int i) {
        return nonQube ? source.length(i) : lens[1];
    }

    @Override
    public int length(int i, int j) {
        return nonQube ? source.length(i, j) : lens[2];
    }

    @Override
    public int length(int i, int j, int k) {
        return nonQube ? source.length(i, j, k) : lens[3];
    }

    @Override
    public double value() {
        return source.value();
    }

    @Override
    public double value(int i) {
        return source.value((int) sorts[0].value(i));
    }

    @Override
    public double value(int i0, int i1) {
        return source.value((int) sorts[0].value(i0), (int) sorts[1].value(i1));
    }

    @Override
    public double value(int i0, int i1, int i2) {
        return source.value((int) sorts[0].value(i0), (int) sorts[1].value(i1), (int) sorts[2].value(i2));
    }

    @Override
    public double value(int i0, int i1, int i2, int i3) {
        return source.value((int) sorts[0].value(i0), (int) sorts[1].value(i1), (int) sorts[2].value(i2), (int) sorts[3].value(i3));
    }

    @Override
    public Object property(String name, int i) {
        Object v = properties.get(name);
        return v != null ? v : source.property(name, i);
    }

    @Override
    public Object property(String name) {
        Object v = properties.get(name);
        return v != null ? v : source.property(name);
    }
}
