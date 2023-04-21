package com.rapidminer.operator.io.pmml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.operator.Model;
import com.rapidminer.operator.UserError;
import com.rapidminer.tools.pmml.PMMLTools;

public abstract class AbstractPMMLModelWriter extends AbstractPMMLObjectWriter {

    private Model model;

    public AbstractPMMLModelWriter(Model model) {
        super();
        this.model = model;
    }

    public void appendBody(Document pmmlDoc, Element pmmlRoot, PMMLVersion version) throws UserError {
        appendTrainingHeader(version, pmmlDoc, pmmlRoot);
        Element modelBody = createModelBody(pmmlDoc, version);
        pmmlRoot.appendChild(modelBody);
    }

    protected void appendTrainingHeader(PMMLVersion version, Document pmmlDoc, Element pmmlRoot) throws UserError {
        ExampleSet trainingsSignature = model.getTrainingHeader();
        PMMLTools.createDataDictionary(pmmlDoc, pmmlRoot, trainingsSignature, version);
    }

    protected abstract Element createModelBody(Document pmmlDoc, PMMLVersion version) throws UserError;
}
