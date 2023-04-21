package udf.string;

import java.io.IOException;
import java.util.Iterator;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.BagFactory;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class AddRankStatic extends EvalFunc<Tuple> {

    static int index = 0;

    public Tuple exec(Tuple input) throws IOException {
        int i = 1;
        Tuple temp = TupleFactory.getInstance().newTuple();
        temp.append(index);
        index = index + 1;
        return temp;
    }
}
