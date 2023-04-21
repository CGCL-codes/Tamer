package xades4j.verification;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import xades4j.properties.QualifyingProperty;
import xades4j.properties.data.PropertiesDataObjectsStructureVerifier;
import xades4j.properties.data.PropertyDataObject;
import xades4j.properties.data.PropertyDataStructureException;

/**
 *
 * @author Luís
 */
class QualifyingPropertiesVerifierImpl implements QualifyingPropertiesVerifier {

    private final QualifyingPropertyVerifiersMapper propertyVerifiersMapper;

    private final PropertiesDataObjectsStructureVerifier dataObjectsStructureVerifier;

    @Inject
    QualifyingPropertiesVerifierImpl(QualifyingPropertyVerifiersMapper propertyVerifiersMapper, PropertiesDataObjectsStructureVerifier dataObjectsStructureVerifier) {
        this.propertyVerifiersMapper = propertyVerifiersMapper;
        this.dataObjectsStructureVerifier = dataObjectsStructureVerifier;
    }

    @Override
    public Collection<PropertyInfo> verifyProperties(Collection<PropertyDataObject> unmarshalledProperties, QualifyingPropertyVerificationContext ctx) throws PropertyDataStructureException, InvalidPropertyException, QualifyingPropertyVerifierNotAvailableException {
        dataObjectsStructureVerifier.verifiyPropertiesDataStructure(unmarshalledProperties);
        Collection<PropertyInfo> props = new ArrayList<PropertyInfo>(unmarshalledProperties.size());
        for (PropertyDataObject propData : unmarshalledProperties) {
            QualifyingPropertyVerifier propVerifier = this.propertyVerifiersMapper.getVerifier(propData);
            QualifyingProperty p = propVerifier.verify(propData, ctx);
            if (null == p) throw new PropertyVerifierErrorException(propData.getClass().getName());
            props.add(new PropertyInfo(propData, p));
        }
        return Collections.unmodifiableCollection(props);
    }
}
