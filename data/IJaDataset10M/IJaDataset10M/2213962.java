package com.ontotext.ordi.sar.handlers.gate;

import gate.Annotation;
import gate.AnnotationSet;
import gate.FeatureMap;
import gate.Node;
import gate.annotation.AnnotationFactory;
import gate.annotation.NodeImpl;
import gate.util.SimpleFeatureMapImpl;
import org.apache.commons.pipeline.StageException;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.impl.URIImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.ontotext.ordi.sar.NamingUtility;
import com.ontotext.ordi.sar.SARDataIntegrityException;
import com.ontotext.ordi.sar.gate.DocumentAnnotation;
import com.ontotext.platform.iteration.CloseableIterator;
import com.ontotext.platform.rdf.RdfQuery;
import com.ontotext.platform.rdf.RdfQueryException;

/**
 * Loads an annotation.
 * 
 * <b>input:    </b>{@link DocumentAnnotation} instance.
 * <b>emits:    </b>{@link Annotation}
 * <b>terminal: </b>no
 * 
 * @author atanas
 *
 */
public class AnnotationLoadHandler extends GateEntityHandler {

    static Logger log = LoggerFactory.getLogger(AnnotationLoadHandler.class);

    @Override
    public void process(Object entity) throws StageException {
        if (entity instanceof DocumentAnnotation) {
            log.debug("load ENTER");
            DocumentAnnotation annot = (DocumentAnnotation) entity;
            AnnotationSet set = annot.getAnnotationSet();
            String documentUri = NamingUtility.documentUri(set.getDocument());
            String annotationUri = (String) annot.getStoreUri();
            RdfQuery conn = getQueryService();
            URI namedGraph = new URIImpl(documentUri);
            Resource subj = new URIImpl(annotationUri);
            CloseableIterator<? extends Statement> result;
            try {
                result = conn.search(subj, null, null, namedGraph, null, false);
                String type = null;
                Node start = null;
                Node end = null;
                Integer id = null;
                while (result.hasNext()) {
                    Statement statement = result.next();
                    String sstr = statement.getSubject().stringValue();
                    String pstr = statement.getPredicate().stringValue();
                    String ostr = statement.getObject().stringValue();
                    log.debug(String.format("%s %s %s", sstr, pstr, ostr));
                    if (pstr.equals(NamingUtility.ANNOTATION_TYPE)) {
                        if (type != null) {
                            raiseAmbiguityError(NamingUtility.ANNOTATION_TYPE, annotationUri);
                        }
                        type = ostr;
                    } else if (pstr.equals(NamingUtility.HAS_START_OFFSET)) {
                        if (start != null) {
                            raiseAmbiguityError(NamingUtility.HAS_START_OFFSET, annotationUri);
                        }
                        start = new NodeImpl(0, Long.valueOf(ostr));
                    } else if (pstr.equals(NamingUtility.HAS_END_OFFSET)) {
                        if (end != null) {
                            raiseAmbiguityError(NamingUtility.HAS_END_OFFSET, annotationUri);
                        }
                        end = new NodeImpl(0, Long.valueOf(ostr));
                    } else if (pstr.equals(NamingUtility.HAS_ID)) {
                        if (id != null) {
                            raiseAmbiguityError(NamingUtility.HAS_ID, annotationUri);
                        }
                        id = Integer.valueOf(ostr);
                    }
                }
                if (type == null) {
                    raiseMissingDataError(NamingUtility.ANNOTATION_TYPE, annotationUri);
                } else if (start == null) {
                    raiseMissingDataError(NamingUtility.HAS_START_OFFSET, annotationUri);
                } else if (end == null) {
                    raiseMissingDataError(NamingUtility.HAS_END_OFFSET, annotationUri);
                }
                AnnotationFactory af = reuseAnnotationFactory();
                FeatureMap features = new SimpleFeatureMapImpl();
                Annotation annotation = af.createAnnotationInSet(set, id, start, end, type, features);
                annot.load(annotation);
            } catch (RdfQueryException e) {
                throw new StageException(this, e);
            } catch (SARDataIntegrityException e) {
                throw new StageException(this, e);
            }
            log.debug("load EXIT");
        }
        super.process(entity);
    }
}
