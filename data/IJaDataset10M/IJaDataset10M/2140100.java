package org.personalsmartspace.cm.db.impl.model;

import org.personalsmartspace.cm.model.api.pss3p.CtxModelType;
import org.personalsmartspace.cm.model.api.pss3p.ICtxAssociationIdentifier;

public class StubCtxAssociationIdentifier extends StubBaseCtxIdentifier implements ICtxAssociationIdentifier {

    private static final long serialVersionUID = 194620725587064141L;

    public StubCtxAssociationIdentifier(String operatorId, String localServiceId, String type, Long objectNumber) {
        super(operatorId, localServiceId, type, objectNumber);
    }

    @Override
    public CtxModelType getModelType() {
        return CtxModelType.ASSOCIATION;
    }
}
