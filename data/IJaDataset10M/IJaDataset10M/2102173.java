package com.greentea.relaxation.jnmf.util.data;

import com.greentea.relaxation.jnmf.localization.Localizer;
import com.greentea.relaxation.jnmf.localization.StringId;
import com.greentea.relaxation.jnmf.util.data.tansformation.IDataTransformer;
import com.greentea.relaxation.jnmf.util.data.tansformation.NormalizationDataTransformer;
import org.apache.commons.collections.primitives.ArrayDoubleList;
import org.apache.commons.collections.primitives.DoubleList;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.DoubleRange;
import org.apache.commons.lang.math.Range;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class DataUtils {

    public static final double IN_CLASS_VALUE = 1;

    public static final double OUT_CLASS_VALUE = 0;

    private DataUtils() {
    }

    public static Dataset filter(Dataset data, Set<Integer> filteredColumns, Set<Integer> filteredRows) {
        for (Integer columnIndex : filteredColumns) {
            data.removeColumn(data.getMetadata().getColumn(columnIndex));
        }
        List<Row> rowsToRemove = new ArrayList<Row>();
        for (Integer rowIndex : filteredRows) {
            rowsToRemove.add(data.get(rowIndex));
        }
        for (Row rowToRemove : rowsToRemove) {
            data.remove(rowToRemove);
        }
        return data;
    }

    public static Dataset normalize(Dataset data, double minValue, double maxValue) {
        Dataset result = normalize(data, VariableType.IN, minValue, maxValue);
        return normalize(result, VariableType.OUT, minValue, maxValue);
    }

    public static Dataset normalize(Dataset data, VariableType type, double minValue, double maxValue) {
        Range inputInterval = null;
        Range outputInterval = null;
        if (type.equals(VariableType.IN)) {
            inputInterval = new DoubleRange(minValue, maxValue);
            outputInterval = null;
        } else if (type.equals(VariableType.OUT)) {
            inputInterval = null;
            outputInterval = new DoubleRange(minValue, maxValue);
        }
        IDataTransformer transformer = new NormalizationDataTransformer(inputInterval, outputInterval);
        return transformer.transformForward(data, type);
    }

    public static Dataset join(Dataset... datas) {
        if (datas.length == 0) {
            return null;
        }
        final Metadata metadata = datas[0].getMetadata().clone();
        Dataset result = new Dataset(metadata);
        for (final Dataset data : datas) {
            for (int j = 0; j < data.size(); ++j) {
                Row row = new Row(data.get(j), metadata);
                result.add(row);
            }
        }
        return result;
    }

    public static List<Dataset> splitOn2Units(Dataset data, int persentOfDataInFirst) {
        if (data.isSplitedOnClasses()) {
            return splitOn2UnitsProRataClasses(data, persentOfDataInFirst);
        }
        return splitOn2UnitsOrdinary(data, persentOfDataInFirst);
    }

    public static List<Dataset> splitOn2UnitsOrdinary(Dataset data, int persentOfDataInFirst) {
        return splitOn2UnitsOrdinary(data, persentOfDataInFirst, null);
    }

    public static List<Dataset> splitOn2UnitsOrdinary(Dataset data, int persentOfDataInFirst, Long randomSeed) {
        data.mixRows(randomSeed);
        Dataset firstData = new Dataset(data.getMetadata().clone());
        firstData.setTransformationHistory(data.getTransformationHistoryCopy());
        Dataset secondData = new Dataset(data.getMetadata().clone());
        secondData.setTransformationHistory(data.getTransformationHistoryCopy());
        int countInFirst = (data.size() * persentOfDataInFirst) / 100;
        for (int i = 0; i < countInFirst; ++i) {
            Row row = new Row(data.get(i), firstData.getMetadata());
            firstData.add(row);
        }
        for (int i = countInFirst; i < data.size(); ++i) {
            Row row = new Row(data.get(i), secondData.getMetadata());
            secondData.add(row);
        }
        List<Dataset> result = new ArrayList<Dataset>();
        result.add(firstData);
        result.add(secondData);
        return result;
    }

    public static List<Dataset> splitOn2UnitsProRataClasses(Dataset data, int persentOfDataInFirst) {
        return splitOn2UnitsProRataClasses(data, persentOfDataInFirst, null);
    }

    public static List<Dataset> splitOn2UnitsProRataClasses(Dataset data, int persentOfDataInFirst, Long randomSeed) {
        Validate.isTrue(data.getOutputsCount() > 0, Localizer.getString(StringId.ZERO_OUTPUTS_COUNT));
        data.mixRows(randomSeed);
        List<List<Row>> dataClasses = new ArrayList<List<Row>>();
        for (int i = 0; i < data.size(); ++i) {
            Sample pair = data.get(i).createSample();
            DoubleList output = pair.getOutput();
            if (i == 0) {
                for (int k = 0; k < output.size(); ++k) {
                    dataClasses.add(new ArrayList<Row>());
                }
            }
            for (int j = 0; j < output.size(); ++j) {
                if (output.get(j) == IN_CLASS_VALUE) {
                    dataClasses.get(j).add(data.get(i));
                    break;
                }
            }
        }
        Dataset firstData = new Dataset(data.getMetadata().clone());
        firstData.setTransformationHistory(data.getTransformationHistoryCopy());
        Dataset secondData = new Dataset(data.getMetadata().clone());
        secondData.setTransformationHistory(data.getTransformationHistoryCopy());
        for (List<Row> clazz : dataClasses) {
            int countInFirst = (clazz.size() * persentOfDataInFirst) / 100;
            for (int i = 0; i < countInFirst; ++i) {
                firstData.add(new Row(clazz.get(i), firstData.getMetadata()));
            }
            for (int i = countInFirst; i < clazz.size(); ++i) {
                secondData.add(new Row(clazz.get(i), secondData.getMetadata()));
            }
        }
        firstData.mixRows(randomSeed);
        secondData.mixRows(randomSeed);
        List<Dataset> result = new ArrayList<Dataset>();
        result.add(firstData);
        result.add(secondData);
        return result;
    }

    public static DoubleList asList(double... vals) {
        DoubleList list = new ArrayDoubleList();
        for (int i = 0; i < vals.length; ++i) {
            list.add(vals[i]);
        }
        return list;
    }
}
