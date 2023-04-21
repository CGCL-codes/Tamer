package uk.ac.manchester.cs.snee.sncb.tos;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import uk.ac.manchester.cs.snee.compiler.OptimizationException;
import uk.ac.manchester.cs.snee.metadata.source.sensornet.Site;
import uk.ac.manchester.cs.snee.compiler.queryplan.SensorNetworkQueryPlan;
import uk.ac.manchester.cs.snee.operators.sensornet.SensornetExchangeOperator;
import uk.ac.manchester.cs.snee.sncb.CodeGenTarget;
import uk.ac.manchester.cs.snee.sncb.TinyOSGenerator;

public class ExchangeProducerComponent extends NesCComponent {

    SensornetExchangeOperator op;

    SensorNetworkQueryPlan plan;

    public ExchangeProducerComponent(final SensornetExchangeOperator op, final SensorNetworkQueryPlan plan, final NesCConfiguration fragConfig, boolean tossimFlag, boolean debugLeds, CodeGenTarget target) {
        super(fragConfig, tossimFlag, debugLeds, target);
        this.op = op;
        this.plan = plan;
        this.id = CodeGenUtils.generateOperatorInstanceName(op, this.site);
    }

    @Override
    public String toString() {
        return this.getID();
    }

    @Override
    public void writeNesCFile(final String outputDir) throws CodeGenerationException {
        try {
            final HashMap<String, String> replacements = new HashMap<String, String>();
            replacements.put("__OPERATOR_DESCRIPTION__", this.op.toString().replace("\"", ""));
            replacements.put("__OUTPUT_TUPLE_TYPE__", CodeGenUtils.generateOutputTupleType(this.op));
            replacements.put("__OUT_QUEUE_CARD__", new Long(op.getOutputQueueCardinality((Site) this.plan.getRT().getSite(this.site.getID()), this.plan.getDAF())).toString());
            replacements.put("__CHILD_TUPLE_PTR_TYPE__", CodeGenUtils.generateOutputTuplePtrType(this.op.getLeftChild()));
            replacements.put("__CHILD_TUPLE_PTR_TYPE__", CodeGenUtils.generateOutputTuplePtrType(this.op.getLeftChild()));
            replacements.put("__HEADER__", this.configuration.generateModuleHeader(this.getID()));
            if (this.op.getLeftChild().getContainingFragment().isLeaf()) {
                replacements.put("__BUFFERING_FACTOR__", "1");
            } else {
                replacements.put("__BUFFERING_FACTOR__", new Long(this.plan.getBufferingFactor()).toString());
            }
            final String outputFileName = generateNesCOutputFileName(outputDir, this.getID());
            writeNesCFile(TinyOSGenerator.NESC_COMPONENTS_DIR + "/producer.nc", outputFileName, replacements);
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }
}
