package xxl.core.xxlinq.usecases;

import static xxl.core.xxlinq.columns.ColumnUtils.*;
import java.util.Arrays;
import java.util.List;
import xxl.core.xxlinq.AdvPredicate;
import xxl.core.xxlinq.AdvTupleCursor;

public class SelectMany1 extends XXLinqExample {

    @Override
    public void executeXXLinq() {
        List<Integer> number1 = Arrays.asList(0, 2, 4, 5, 6, 8, 9);
        List<Integer> number2 = Arrays.asList(1, 3, 5, 7, 8);
        AdvTupleCursor number1Cursor = new AdvTupleCursor(number1, "number1");
        AdvTupleCursor number2Cursor = new AdvTupleCursor(number2, "number2");
        AdvTupleCursor tupleCursor;
        tupleCursor = number1Cursor.join("pairs", number2Cursor, AdvPredicate.TRUE).where(col("number1.value").LT(col("number2.value")));
        printExample(tupleCursor);
    }
}
