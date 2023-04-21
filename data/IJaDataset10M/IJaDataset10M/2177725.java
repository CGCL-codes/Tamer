package com.ontotext.ordi.sar.server.handlers.gate;

import gate.FeatureMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.openrdf.model.URI;
import com.ontotext.ordi.sar.exception.SARException;
import com.ontotext.ordi.sar.gate.GateRepository;
import com.ontotext.ordi.tripleset.TConnection;
import com.ontotext.ordi.tripleset.TFactory;

public class DocumentFeaturesHandler extends FeaturesHandler {

    static Logger log = Logger.getLogger(DocumentFeaturesHandler.class);

    @Override
    public int load(Object entity, TConnection conn, Map<Object, Object> context) throws SARException {
        if (entity instanceof FeatureMap) {
            FeatureMap features = (FeatureMap) entity;
            log.debug("store ENTER");
            String documentUri = getDocumentUri(context);
            TFactory factory = reuseFactory(context);
            URI namedGraph = factory.createURI(documentUri);
            if (Boolean.TRUE.equals(context.get(GateRepository.PARAM_DOCUMENT_FEATURES_IN_BAG))) {
                loadFeaturesFromBag(features, conn, factory, documentUri, namedGraph);
            } else {
                loadFeaturesFromProperties(features, conn, factory, documentUri, namedGraph);
            }
            log.debug("store EXIT");
            return HANDLED_CONTINUE;
        }
        return super.load(entity, conn, context);
    }

    @Override
    public int store(Object entity, TConnection conn, Map<Object, Object> context) throws SARException {
        if (entity instanceof FeatureMap) {
            FeatureMap features = (FeatureMap) entity;
            log.debug("store ENTER");
            String documentUri = getDocumentUri(context);
            TFactory factory = reuseFactory(context);
            URI namedGraph = factory.createURI(documentUri);
            if (Boolean.TRUE.equals(context.get(GateRepository.PARAM_DOCUMENT_FEATURES_IN_BAG))) {
                storeFeaturesAsBag(features, conn, factory, factory.createURI(documentUri), namedGraph);
            } else {
                storeFeaturesAsProperties(features, conn, factory, documentUri, namedGraph);
            }
            log.debug("store EXIT");
            return HANDLED_CONTINUE;
        }
        return NOT_HANDLED;
    }
}
