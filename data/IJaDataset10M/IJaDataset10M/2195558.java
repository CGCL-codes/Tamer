package uk.co.pointofcare.echobase.snosled.api;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import uk.co.pointofcare.echobase.snosled.api.SctConcept.SctAttributeConcept;
import uk.co.pointofcare.echobase.snosled.api.SctConcept.SctAttributeValue;
import uk.co.pointofcare.echobase.snosled.api.SctConcept.SctConceptInstance;

public interface SctExpressionInstance extends SctAttributeValue, SctResource {

    public boolean isSubsumedBy(SctAttributeValue other);

    public boolean isEquivalentTo(SctAttributeValue other);

    public boolean subsumes(SctAttributeValue other);

    public boolean isContextWrapped();

    public SctConceptInstance getFocus();

    public List<? extends Set<? extends SctAttribute>> getRoleGroups();

    public Set<? extends SctAttribute> getAttributes();

    public Set<? extends SctAttribute> getRefinableAttributes();

    public UUID getInstanceIdentifier();

    public interface SctAttribute {

        public SctAttributeConcept getAttributeConcept();

        public SctAttributeValue getAttributeValue();

        public void refineValue(SctAttributeValue newValue);
    }
}
