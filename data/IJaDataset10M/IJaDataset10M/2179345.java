package com.divosa.eformulieren.web.core.processor;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.joda.time.DateTime;
import org.orbeon.oxf.pipeline.api.PipelineContext;
import org.orbeon.oxf.processor.ProcessorInputOutputInfo;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import com.divosa.eformulieren.util.constant.Constants;
import com.divosa.eformulieren.util.formatter.SimpleDateTimeFormatter;
import com.divosa.eformulieren.web.core.exception.EFormulierenException;
import com.divosa.eformulieren.web.core.signal.FormSubmitHandler;
import com.divosa.eformulieren.web.core.signal.FormSubmitHandlerFactory;

public class BrokerProcessor extends AbstractProcessor {

    private static Logger LOGGER = Logger.getLogger(BrokerProcessor.class);

    public BrokerProcessor() {
        addInputInfo(new ProcessorInputOutputInfo("instance"));
        addOutputInfo(new ProcessorInputOutputInfo(OUTPUT_DATA));
    }

    @Override
    public void generateData(PipelineContext context, ContentHandler contentHandler) throws SAXException, DocumentException {
        Document inputDocument = readInputAsDOM4J(context, "data");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(inputDocument.asXML());
        }
        org.dom4j.Document doc = DocumentHelper.createDocument();
        org.dom4j.Element root = doc.addElement("root");
        org.dom4j.Element result = root.addElement("result");
        org.dom4j.Element failure = root.addElement("failure");
        org.dom4j.Element success = root.addElement("success");
        try {
            FormSubmitHandler formSubmitHandler = FormSubmitHandlerFactory.getFormSubmitHandler(Constants.FORM_SUBMIT_BROKER);
            Document resultDocument = (Document) formSubmitHandler.handle(inputDocument);
            if (resultDocument != null) {
                Node message = resultDocument.selectSingleNode("/formcontentresult/message[1]");
                String code = message.valueOf("@code");
                if (code != null && code.equals("succes")) {
                    success.addText(message.getText());
                } else {
                    failure.addText(message.getText());
                }
            } else {
                throw new EFormulierenException("Document returned from the broker is null");
            }
        } catch (Exception e) {
            failure.addText("Er is een fout opgetreden bij het aanbieden aan de broker: " + e.getMessage());
        }
        populateContentHandler(contentHandler, doc);
    }
}
