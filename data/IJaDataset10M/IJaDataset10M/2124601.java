package udf.string;

import java.io.IOException;
import org.apache.pig.EvalFunc;
import org.apache.pig.data.Tuple;
import org.apache.pig.data.TupleFactory;

public class NormalizeTag extends EvalFunc<Tuple> {

    public Tuple exec(Tuple input) throws IOException {
        if (input.size() != 1 || input.get(0) == null) {
            return null;
        }
        Tuple output = TupleFactory.getInstance().newTuple(1);
        String url = (String) input.get(0);
        String domain = CleanTag.normalizeTag(url);
        output.set(0, domain);
        return output;
    }
}
