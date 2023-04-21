package org.neodatis.odb.core.server.message;

import org.neodatis.odb.core.layers.layer2.meta.MetaModel;
import org.neodatis.odb.core.layers.layer4.engine.CheckMetaModelResult;
import org.neodatis.odb.core.server.layers.layer3.engine.Command;
import org.neodatis.odb.core.server.layers.layer3.engine.Message;

public class CheckMetaModelCompatibilityMessageResponse extends Message {

    private CheckMetaModelResult result;

    private MetaModel updatedMetaModel;

    public CheckMetaModelCompatibilityMessageResponse(String baseId, String sessionId, CheckMetaModelResult result, MetaModel metaModel) {
        super(Command.CHECK_META_MODEL_COMPATIBILITY, baseId, sessionId);
        this.result = result;
        this.updatedMetaModel = metaModel;
    }

    public CheckMetaModelCompatibilityMessageResponse(String baseId, String sessionId, String error) {
        super(Command.CHECK_META_MODEL_COMPATIBILITY, baseId, sessionId);
        setError(error);
    }

    public String toString() {
        return "CheckMetaModelCompatibility";
    }

    public CheckMetaModelResult getResult() {
        return result;
    }

    public void setResult(CheckMetaModelResult result) {
        this.result = result;
    }

    public MetaModel getUpdatedMetaModel() {
        return updatedMetaModel;
    }

    public void setUpdatedMetaModel(MetaModel updatedMetaModel) {
        this.updatedMetaModel = updatedMetaModel;
    }
}
