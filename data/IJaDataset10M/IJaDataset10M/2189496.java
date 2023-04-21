package uk.org.ogsadai.dqp.execute.workflow;

import java.util.List;
import uk.org.ogsadai.client.toolkit.SingleActivityOutput;
import uk.org.ogsadai.dqp.execute.ActivityConstructionException;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.lqp.Annotation;
import uk.org.ogsadai.dqp.lqp.Operator;
import uk.org.ogsadai.dqp.lqp.operators.PushExchangeProducerOperator;
import uk.org.ogsadai.resource.ResourceID;

/**
 * Builds activities for operator PUSH EXCHANGE PRODUCER.
 *
 * @author The OGSA-DAI Project Team.
 */
public class PushExchangeProducerBuilder implements ActivityPipelineBuilder {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009";

    /**
     * {@inheritDoc}
     */
    public SingleActivityOutput build(Operator op, List<SingleActivityOutput> outputs, PipelineWorkflowBuilder builder) throws ActivityConstructionException {
        PushExchangeProducerOperator operator = (PushExchangeProducerOperator) op;
        ResourceID dataSink = new ResourceID(operator.getDataSinkID());
        TupleSerialiser tupleSerialiser = operator.getTupleSerialiser();
        SingleActivityOutput output = tupleSerialiser.addSerialiser(outputs.get(0), builder);
        DQPDeliverToDataSink deliver = new DQPDeliverToDataSink("uk.org.ogsadai.DeliverToDataSink");
        deliver.createInput("mode");
        deliver.createInput("numberOfBlocks");
        deliver.createInput("resourceID");
        deliver.createInput("data");
        deliver.addInput("mode", "BLOCK");
        deliver.addInput("numberOfBlocks", operator.getNumBlocks());
        deliver.addInput("resourceID", dataSink.toString());
        deliver.connectInput("data", output);
        Partition destination = Annotation.getPartitionAnnotation(operator.getParent());
        deliver.setEvaluationNode(destination.getEvaluationNode());
        builder.add(deliver);
        return null;
    }
}
