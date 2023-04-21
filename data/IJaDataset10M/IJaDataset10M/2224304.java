package uk.ac.wlv.clg.services;

import java.util.Iterator;
import java.util.TreeSet;
import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebServiceContext;
import uk.ac.wlv.clg.nlp.entityannotator.StandardEntityAnnotator;
import uk.ac.wlv.clg.nlp.misc.AnnotationPosition;
import net.sf.qallme.WebServiceTools;
import net.sf.qallme.gen.ws.AnnotatedSentence;
import net.sf.qallme.gen.ws.InternalServiceFault;
import net.sf.qallme.gen.ws.ObjectFactory;
import net.sf.qallme.gen.ws.AnnotatedSentence.Annotation;
import net.sf.qallme.gen.ws.entityannotation.EntityAnnotator;

/**
 * Provides a web service implementation for the {@link EntityAnnotator} web
 * service interface.
 * <p>
 * 
 * @author dinel
 * @version SVN $Rev$ by $Author$
 */
@WebService(name = "EntityAnnotator", portName = "EntityAnnotatorPort", serviceName = "EntityAnnotatorWS", targetNamespace = "http://qallme.sf.net/wsdl/entityannotation.wsdl")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public class GBEntityAnnotatorWS implements EntityAnnotator {

    /** the web service context is injected by the application server */
    @Resource
    private WebServiceContext wsContext = null;

    /**
	 * Annotates entities in the given NL question.
	 * 
	 * @param inputSentence
	 *            the NL question string to annotate with entities
	 * @return the annotated question
	 * @see EntityAnnotator#annotateEntities(String)
	 */
    @Override
    @WebMethod
    @WebResult(name = "AnnotatedSentence", targetNamespace = "http://qallme.sf.net/xsd/qallmeshared.xsd", partName = "annotatedSentence")
    public AnnotatedSentence annotateEntities(@WebParam(name = "string", targetNamespace = "http://qallme.sf.net/wsdl/qallmeshared.wsdl", partName = "inputSentence") String inputSentence) throws InternalServiceFault {
        if (inputSentence == null) inputSentence = "";
        ObjectFactory annotationFactory = new ObjectFactory();
        AnnotatedSentence _retSent = annotationFactory.createAnnotatedSentence();
        TreeSet<AnnotationPosition> annotations = new TreeSet<AnnotationPosition>();
        StandardEntityAnnotator annotator;
        try {
            annotator = new StandardEntityAnnotator(WebServiceTools.getServletContext(wsContext).getRealPath("/res"));
            annotations = annotator.annotateInstances(inputSentence);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        Iterator<AnnotationPosition> itAnn = annotations.iterator();
        int pos = 0;
        while (itAnn.hasNext()) {
            AnnotationPosition ap = itAnn.next();
            if (pos < ap.getStartPos()) {
                _retSent.getContent().add(" " + inputSentence.substring(pos, ap.getStartPos() - 1).trim() + " ");
                pos = ap.getStartPos();
            }
            if (pos == ap.getStartPos()) {
                _retSent.getContent().add(" ");
                Annotation ann = annotationFactory.createAnnotatedSentenceAnnotation();
                ann.setType(ap.getType());
                ann.setValue(inputSentence.substring(pos, pos + ap.getLength()));
                ann.setCanonicalForm(ap.getNormalised());
                _retSent.getContent().add(annotationFactory.createAnnotatedSentenceAnnotation(ann));
                pos = pos + ap.getLength();
                _retSent.getContent().add(new String(" "));
            }
        }
        if (pos < inputSentence.length()) {
            _retSent.getContent().add(" " + inputSentence.substring(pos).trim() + " ");
        }
        return _retSent;
    }
}
