package uk.ac.manchester.cs.snee.sncb.tos;

import java.util.HashMap;
import uk.ac.manchester.cs.snee.compiler.queryplan.Fragment;
import uk.ac.manchester.cs.snee.compiler.queryplan.SensorNetworkQueryPlan;
import uk.ac.manchester.cs.snee.metadata.source.sensornet.Site;
import uk.ac.manchester.cs.snee.operators.logical.SelectOperator;
import uk.ac.manchester.cs.snee.operators.sensornet.SensornetSelectOperator;
import uk.ac.manchester.cs.snee.sncb.CodeGenTarget;
import uk.ac.manchester.cs.snee.sncb.TinyOSGenerator;

public class SelectComponent extends NesCComponent {

    SensornetSelectOperator op;

    SensorNetworkQueryPlan plan;

    Fragment frag;

    public SelectComponent(final SensornetSelectOperator op, final SensorNetworkQueryPlan plan, final NesCConfiguration fragConfig, boolean tossimFlag, boolean debugLeds, CodeGenTarget target) {
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
            replacements.put("__SELECT_PREDICATES__", CodeGenUtils.getNescText(((SelectOperator) op.getLogicalOperator()).getPredicate(), "inQueue[inHead].", null, op.getAttributes(), null, target));
            final StringBuffer tupleConstructionBuff = CodeGenUtils.generateTupleConstruction(op, false, target);
            replacements.put("__CONSTRUCT_TUPLE__", tupleConstructionBuff.toString());
            final String outputFileName = generateNesCOutputFileName(outputDir, this.getID());
            writeNesCFile(TinyOSGenerator.NESC_COMPONENTS_DIR + "/select.nc", outputFileName, replacements);
        } catch (Exception e) {
            throw new CodeGenerationException(e);
        }
    }
}
