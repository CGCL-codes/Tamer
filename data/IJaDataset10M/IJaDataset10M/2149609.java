package serene.validation.schema.active;

import java.util.List;
import serene.validation.schema.active.components.AListPattern;

public interface StructuredDataActiveType extends DataActiveType {

    boolean allowsListPatternContent();

    List<AListPattern> getListPatterns(List<AListPattern> listPatterns);
}
