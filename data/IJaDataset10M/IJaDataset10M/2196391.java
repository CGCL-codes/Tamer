package ru.susu.algebra.chartable.writer;

import java.util.ArrayList;
import java.util.List;
import ru.susu.algebra.chartable.constructor.AlternatingGroupCharTableConstructor;
import ru.susu.algebra.matrix.Matrix;
import ru.susu.algebra.operation.IOperation;
import ru.susu.algebra.partition.Partition;
import ru.susu.algebra.partition.PartitionsCacheConstructor;
import ru.susu.algebra.partition.comparators.AlternatingByCharDegreeComparator;
import ru.susu.algebra.partition.filter.AlternatingPartitionsFilter;
import ru.susu.algebra.partition.filter.OnlyFirstAssocPartitionFilter;
import ru.susu.algebra.util.CollectionUtils;

/**
 * Конструирует матрицу, соответствующую таблице характеров An.
 * Причем объектами являются опять же матрицы, в специальных строчках и столбцах
 * может быть не одно значение.
 *
 * @author akargapolov
 * @since: 14.06.2010
 */
public class AlternatingGroupCharTableExtractor implements IOperation<Matrix> {

    @Override
    public Matrix performOperation(Object... sources) {
        Integer number = (Integer) sources[0];
        List<Partition> partitions1 = new PartitionsCacheConstructor().getAscendingPartitions(number, new OnlyFirstAssocPartitionFilter());
        partitions1 = CollectionUtils.sort(partitions1, true, new AlternatingByCharDegreeComparator());
        Matrix result = null;
        try {
            for (Partition p1 : partitions1) {
                result = addCharacter(p1, result);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Matrix addCharacter(Partition p1, Matrix matrix) throws Exception {
        List<Partition> partitions2 = new PartitionsCacheConstructor().getAscendingPartitions(p1.getPresentedNumber(), new AlternatingPartitionsFilter());
        AlternatingGroupCharTableConstructor constructor = AlternatingGroupCharTableConstructor.getInstance();
        ArrayList<Object> row = new ArrayList<Object>();
        for (Partition p2 : partitions2) {
            Matrix value = constructor.getValue(p1, p2);
            row.add(value);
        }
        if (matrix == null) {
            matrix = new Matrix(0, row.size());
        } else {
            matrix.addRow(row);
        }
        return matrix;
    }
}
