package uk.org.ogsadai.dqp.lqp;

import java.util.Set;
import uk.org.ogsadai.dqp.common.DataNode;
import uk.org.ogsadai.dqp.common.EvaluationNode;
import uk.org.ogsadai.dqp.execute.partition.Partition;
import uk.org.ogsadai.dqp.lqp.udf.Function;

/**
 * The class providing default annotation keys. Any Object can be used as
 * annotation value. This class provides add/get methods for each annotation key
 * and therefore defines the expected value type for each annotation.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class Annotation {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008";

    /** Partition annotation. */
    public static final String PARTITION = "lqp.partition";

    /** Function result name. */
    public static final String RESULT_NAME = "result.name";

    /** Implementation. */
    public static final String IMPLEMENTATION = "implementation";

    /** Temporary attribute. */
    public static final String TEMP_ATTR = "temp.attr";

    /** Correlated attribute. */
    public static final String CORR_ATTR = "corr.attr";

    /** Cardinality. */
    public static final String CARDINALITY = "cardinality";

    public static final String EVALUATION_NODE = "evaluation.node";

    /** Data node. */
    public static final String DATA_NODE = "data.node";

    /** Correlated set. */
    public static final String CORRELATED_SET = "correlated.set";

    /** Read first. */
    public static final String READ_FIRST = "readFirst";

    /** Ordered by attribute. */
    public static final String ORDERED_BY_ATTR = "order.by.attr";

    /**
     * When correlation is detected, a binary operator is annotated with a set
     * of correlated attributes. This annotation is later used by query
     * normalizer to insert APPLY operators.
     * 
     * @param operator
     *            operator to be annotated
     * @param correlatedSet
     *            a set of correlated attributes
     */
    public static void addCorrelatedSetAnnotation(Operator operator, Set<Attribute> correlatedSet) {
        operator.addAnnotation(CORRELATED_SET, correlatedSet);
    }

    /**
     * Gets the value <code>correlated</code> set annotation.
     * 
     * @param operator
     *            annotated operator
     * @return correlated set or <code>null</code> if non existent
     */
    public static Set<Attribute> getCorrelatedSetAnnotation(Operator operator) {
        return (Set<Attribute>) operator.getAnnotation(CORRELATED_SET);
    }

    /**
     * Cardinality estimator uses this annotation to assign estimated
     * cardinality to an operator. The {@link Operator#getResultCardinality()}
     * method check for existence of this annotation before returning the
     * default value.
     * 
     * @param operator
     *            operator to be annotated
     * @param cardinality
     *            estimated cardinality
     */
    public static void addCardinalityAnnotation(Operator operator, Long cardinality) {
        operator.addAnnotation(CARDINALITY, cardinality);
    }

    /**
     * Gets the value of <code>cardinality</code> annotation.
     * 
     * @param operator
     *            annotated operator
     * @return estimated cardinality or <code>null</code> if non existent
     */
    public static Long getCardinalityAnnotation(Operator operator) {
        return (Long) operator.getAnnotation(CARDINALITY);
    }

    /**
     * Adds annotation signifying that an attribute is correlated. The
     * {@link Attribute#isCorrelated()} method uses this annotation.
     * 
     * @param attribute
     *            attribute to be annotated
     * @param correlated
     *            <code>true</code> if correlated, <code>false</code> otherwise
     */
    public static void addCorrAttrAnnotation(Attribute attribute, boolean correlated) {
        attribute.addAnnotation(CORR_ATTR, correlated);
    }

    /**
     * Gets the value of <code>corr.attr</code> annotation.
     * 
     * @param attribute
     *            annotated attribute
     * @return the annotation value or <code>null</code> if non existent
     */
    public static Boolean getCorrAttrAnnotation(Attribute attribute) {
        return (Boolean) attribute.getAnnotation(CORR_ATTR);
    }

    /**
     * Adds annotation signifying that an attribute is temporary and should be
     * projected out from the final result.
     * 
     * @param attribute
     *            attribute to be annotated
     * @param temporary
     *            <code>true</code> if temporary, <code>false</code> otherwise
     */
    public static void addTempAttrAnnotation(Attribute attribute, boolean temporary) {
        attribute.addAnnotation(TEMP_ATTR, temporary);
    }

    /**
     * Adds annotation signifying that if the result of a function is bound to
     * an attribute, then this attribute should be temporary. This is usually
     * used with aggregates in the HAVING clause.
     * 
     * @param function
     *            function to be annotated
     * @param temporary
     *            if temporary
     */
    public static void addTempAttrAnnotation(Function function, boolean temporary) {
        function.addAnnotation(TEMP_ATTR, temporary);
    }

    /**
     * Gets the value of the <code>temp.attr</code> annotation.
     * 
     * @param attribute
     *            annotated attribute
     * @return annotation value or <code>null</code> if non existent
     */
    public static Boolean getTempAttrAnnotation(Attribute attribute) {
        return (Boolean) attribute.getAnnotation(TEMP_ATTR);
    }

    /**
     * Gets the value of the <code>temp.attr</code> annotation.
     * 
     * @param function
     *            annotated function
     * @return annotation value or <code>null</code> if non existent
     */
    public static Boolean getTempAttrAnnotation(Function function) {
        return (Boolean) function.getAnnotation(TEMP_ATTR);
    }

    /**
     * Adds annotation signifying which builder (or alternative implementation)
     * should be used to translate this operator to its physical representation.
     * A matching entry needs to be present in the compiler configuration
     * document.
     * 
     * @param operator
     *            operator to be annotated
     * @param name
     *            name of the alternative implementation (arbitrary string)
     */
    public static void addImplementationAnnotation(Operator operator, String name) {
        operator.addAnnotation(IMPLEMENTATION, name);
    }

    /**
     * Gets the value of the <code>implementation</code> annotation.
     * 
     * @param operator
     *            annotated operator
     * @return annotation value or <code>null</code> if non existent
     */
    public static String getImplementationAnnotation(Operator operator) {
        return (String) operator.getAnnotation(IMPLEMENTATION);
    }

    /**
     * Adds annotation specifying the name of an attribute to which the result
     * of a function should is expected to be bound.
     * 
     * @param function
     *            function to be annotated
     * @param name
     *            attribute name
     */
    public static void addResultNameAnnotation(Function function, String name) {
        function.addAnnotation(RESULT_NAME, name);
    }

    /**
     * Gets the value of the <code>result.name</code> annotation.
     * 
     * @param function
     *            function to be annotated
     * @return annotation value or <code>null</code> if non existent
     */
    public static String getResultNameAnnotation(Function function) {
        return (String) function.getAnnotation(RESULT_NAME);
    }

    /**
     * Annotates an operator with a partition to which it belongs.
     * 
     * @param operator
     *            operator to be annotated
     * @param partition
     */
    public static void addPartinionAnnotation(Operator operator, Partition partition) {
        operator.addAnnotation(PARTITION, partition);
    }

    /**
     * Returns the value of the <code>lqp.partition</code> annotation.
     * 
     * @param operator
     *            annotated operator
     * @return annotation value or <code>null</code> if non existent
     */
    public static Partition getPartitionAnnotation(Operator operator) {
        return (Partition) operator.getAnnotation(PARTITION);
    }

    /**
     * Annotates operator with the evaluation node.
     * 
     * @param operator
     *            operator to be annotated
     * @param evaluationNode
     */
    public static void addEvaluationNodeAnnotation(Operator operator, EvaluationNode evaluationNode) {
        operator.addAnnotation(EVALUATION_NODE, evaluationNode);
    }

    /**
     * If <code>data.node</code> annotation is set returns evaluation node
     * extracted from the data node, if <code>evaluation.node</code> annotation
     * is set returns evaluation node, else return <code>null</code>.
     * 
     * @param operator
     *            annotated operator
     * @return evaluation node
     */
    public static EvaluationNode getEvaluationNodeAnnotation(Operator operator) {
        return (operator.getAnnotation(DATA_NODE) != null) ? ((DataNode) operator.getAnnotation(DATA_NODE)).getEvaluationNode() : (EvaluationNode) operator.getAnnotation(EVALUATION_NODE);
    }

    /**
     * Annotates operator with the data node.
     * 
     * @param operator
     *            operator to be annotated
     * @param dataNode
     */
    public static void addDataNodeAnnotation(Operator operator, DataNode dataNode) {
        operator.addAnnotation(DATA_NODE, dataNode);
    }

    /**
     * Gets the value of the <code>data.node</code> annotation.
     * 
     * @param operator
     *            annotated operator
     * @return annotation value or <code>null</code> if non existent
     */
    public static DataNode getDataNodeAnnotation(Operator operator) {
        return (DataNode) operator.getAnnotation(DATA_NODE);
    }

    /**
     * Annotates operator with which side to read first
     * 
     * @param operator
     *            operator to be annotated
     * @param side 
     *            <tt>"left"</tt> or <tt>"right"</tt>
     */
    public static void addReadFirstAnnotation(Operator operator, String side) {
        operator.addAnnotation(READ_FIRST, side);
    }

    /**
     * Gets the value of the <code>readFirst</code> annotation.
     * 
     * @param operator
     *            annotated operator
     * @return annotation value or <code>null</code> if non existent
     */
    public static String getReadFirstAnnotation(Operator operator) {
        return (String) operator.getAnnotation(READ_FIRST);
    }
}
