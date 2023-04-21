package ar.com.oddie.web.actions;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import ar.com.oddie.core.directoryparser.DirectoryManager;
import ar.com.oddie.core.directoryparser.DocumentProcessor;
import ar.com.oddie.core.entities.Document;
import ar.com.oddie.core.exception.DocumentProcessorException;
import ar.com.oddie.core.indexer.DocumentIndexer;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;

@SuppressWarnings("serial")
public class DocumentsProcessorAction extends ActionSupport {

    private String absoluteInputPath;

    private boolean addAllInDictionary;

    private static final Logger logger = Logger.getLogger(ProcesarDirectorioAction.class);

    public boolean isAddAllInDictionary() {
        return addAllInDictionary;
    }

    public void setAddAllInDictionary(boolean addAllInDictionary) {
        this.addAllInDictionary = addAllInDictionary;
    }

    public String getAbsoluteInputPath() {
        return absoluteInputPath;
    }

    public void setAbsoluteInputPath(String absoluteInputPath) {
        this.absoluteInputPath = absoluteInputPath;
    }

    @Override
    @SkipValidation
    public String execute() throws Exception {
        return ActionSupport.INPUT;
    }

    @RequiredStringValidator(fieldName = "absoluteInputPath", message = "Debe indicar un directorio a procesar.", trim = true)
    public String process() {
        DirectoryManager directoryManager = new DirectoryManager(absoluteInputPath);
        DocumentProcessor documentProcessor = null;
        DocumentIndexer documentIndexer = null;
        try {
            documentProcessor = new DocumentProcessor();
            documentIndexer = new DocumentIndexer(true);
        } catch (IOException e1) {
            logger.error("Se produjo un error al crear el entorno.");
        }
        documentProcessor.setAutomaticAddInDictionary(true);
        while (directoryManager.hasNext()) {
            Document docToProcess = directoryManager.next();
            Document documentProcessed = null;
            logger.info(String.format("Procesando el archivo: %s", docToProcess.getPath()));
            try {
                documentProcessed = documentProcessor.process(docToProcess);
            } catch (DocumentProcessorException e) {
                logger.error("Error al intentar procesar el documento: " + docToProcess.getId());
            }
            if (documentProcessed != null) {
                documentIndexer.indexDocument(documentProcessed);
            }
        }
        return ActionSupport.SUCCESS;
    }
}
