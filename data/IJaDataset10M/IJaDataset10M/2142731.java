package game.data.extractors;

import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.example.table.DataRow;
import com.rapidminer.example.table.DataRowFactory;
import com.rapidminer.example.table.MemoryExampleTable;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorCreationException;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.ports.OutputPort;
import com.rapidminer.operator.ports.InputPort;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.Attribute;
import com.rapidminer.operator.ports.metadata.PassThroughRule;
import com.rapidminer.operator.preprocessing.transformation.AggregationOperator;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.OperatorService;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AttributeCountExtractor extends AbstractExtractor {

    public AttributeCountExtractor(OperatorDescription description) {
        super(description);
    }

    @Override
    public void doWork() throws OperatorException {
        ExampleSet exampleSet = input.getData();
        MemoryExampleTable met = new MemoryExampleTable();
        AttributeFactory af = new AttributeFactory();
        DataRowFactory drf = new DataRowFactory(DataRowFactory.TYPE_DOUBLE_ARRAY, ',');
        Attribute a1 = af.createAttribute("attribute count", Ontology.NUMERICAL);
        met.addAttribute(a1);
        Attribute[] atr = { a1 };
        Integer[] hodnota = { exampleSet.getAttributes().allSize() };
        DataRow dr = drf.create(hodnota, atr);
        met.addDataRow(dr);
        ExampleSet es = met.createExampleSet();
        output.deliver(es);
        exa.deliver(exampleSet);
    }
}
