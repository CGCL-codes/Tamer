package com.rapidminer.operator;

import java.util.Iterator;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.tools.LoggingHandler;
import com.rapidminer.tools.Ontology;
import edu.udo.cs.wvtool.main.WVTDocumentInfo;
import edu.udo.cs.wvtool.main.WVTWordVector;
import edu.udo.cs.wvtool.util.WVToolException;
import edu.udo.cs.wvtool.wordlist.WVTWordList;

public class ExampleSetOutputFilter implements RapidMinerOutputFilter {

    private Attribute sourceAtt = null;

    private Attribute typeAtt = null;

    private Attribute encodingAtt = null;

    private Attribute languageAtt = null;

    private Attribute[] wordAttributes;

    private ExampleSet exampleSet;

    private Iterator<Example> exampleIterator;

    private List<Attribute> toRemoveAttributes;

    public ExampleSetOutputFilter(ExampleSet exampleSet, WVTWordList wordList, boolean useSpecialAttributes, List<Attribute> toRemoveAttributes, LoggingHandler logger) {
        this.exampleSet = exampleSet;
        this.toRemoveAttributes = toRemoveAttributes;
        this.wordAttributes = new Attribute[wordList.getNumWords()];
        for (int i = 0; i < wordAttributes.length; i++) {
            String attributeName = wordList.getWord(i);
            if (exampleSet.getAttributes().get(attributeName) != null) {
                logger.logWarning("The original example example set already contains an attribute named \"" + wordList.getWord(i) + "\". Renaming the new word attribute into " + wordList.getWord(i) + "_");
                while (exampleSet.getAttributes().get(attributeName) != null) {
                    attributeName += "_";
                }
            }
            Attribute attribute = AttributeFactory.createAttribute(attributeName, Ontology.REAL);
            this.wordAttributes[i] = attribute;
            this.exampleSet.getExampleTable().addAttribute(attribute);
            this.exampleSet.getAttributes().addRegular(attribute);
        }
        if (useSpecialAttributes) {
            sourceAtt = AttributeFactory.createAttribute("text_source", Ontology.NOMINAL);
            typeAtt = AttributeFactory.createAttribute("content_type", Ontology.NOMINAL);
            encodingAtt = AttributeFactory.createAttribute("content_encoding", Ontology.NOMINAL);
            languageAtt = AttributeFactory.createAttribute("content_language", Ontology.NOMINAL);
            this.exampleSet.getExampleTable().addAttribute(sourceAtt);
            this.exampleSet.getExampleTable().addAttribute(typeAtt);
            this.exampleSet.getExampleTable().addAttribute(encodingAtt);
            this.exampleSet.getExampleTable().addAttribute(languageAtt);
            this.exampleSet.getAttributes().addRegular(sourceAtt);
            this.exampleSet.getAttributes().addRegular(typeAtt);
            this.exampleSet.getAttributes().addRegular(encodingAtt);
            this.exampleSet.getAttributes().addRegular(languageAtt);
        }
        this.exampleIterator = exampleSet.iterator();
    }

    public void write(WVTWordVector wordVector) throws WVToolException {
        if (exampleIterator.hasNext()) {
            WVTDocumentInfo docInfo = wordVector.getDocumentInfo();
            Example e = exampleIterator.next();
            double[] values = wordVector.getValues();
            for (int i = 0; i < values.length; i++) {
                if (Double.isInfinite(values[i]) || Double.isNaN(values[i])) {
                    e.setValue(wordAttributes[i], 0.0);
                } else {
                    e.setValue(wordAttributes[i], values[i]);
                }
            }
            if (sourceAtt != null) {
                e.setValue(sourceAtt, sourceAtt.getMapping().mapString(docInfo.getSourceName()));
                e.setValue(typeAtt, typeAtt.getMapping().mapString(docInfo.getContentType()));
                e.setValue(encodingAtt, encodingAtt.getMapping().mapString(docInfo.getContentEncoding()));
                e.setValue(languageAtt, languageAtt.getMapping().mapString(docInfo.getContentLanguage()));
            }
        } else {
            exampleIterator = null;
            wordAttributes = null;
        }
    }

    public ExampleSet createExampleSet() {
        if (toRemoveAttributes != null) {
            for (Attribute attribute : toRemoveAttributes) {
                this.exampleSet.getAttributes().remove(attribute);
            }
        }
        return this.exampleSet;
    }

    public void cleanUp() {
        this.exampleSet = null;
        this.exampleIterator = null;
        this.wordAttributes = null;
        this.sourceAtt = null;
        this.typeAtt = null;
        this.encodingAtt = null;
        this.languageAtt = null;
    }
}
