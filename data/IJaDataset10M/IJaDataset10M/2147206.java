package net.sf.doolin.sdo.constraint.support;

import org.codehaus.jackson.JsonNode;
import com.google.common.base.Predicate;

public interface JsonConstraintParser<T> {

    Predicate<T> parse(JsonNode jConstraint);
}
