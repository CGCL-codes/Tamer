package de.tuberlin.cs.cis.ocl.type;

/**
 * This exception is thrown when a feature can not be resolved 
 * on a particular type. In the context of OCL 1.5 that can be 
 * the case, if the feature is not defined, the feature is
 * ambigious or if the feature is not a query.
 * 
 * @author fchabar
 *
 */
public class UndefinedFeatureException extends Exception {
}
