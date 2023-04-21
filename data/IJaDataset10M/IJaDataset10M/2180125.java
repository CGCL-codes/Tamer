package com.ontotext.gate.edlin;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.persist.PersistenceException;
import gate.util.InvalidOffsetException;
import gate.util.OffsetComparator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import types.Alphabet;
import types.SparseVector;
import com.ontotext.gate.edlin.sequence.Element;
import com.ontotext.gate.edlin.sequence.ElementSequence;

public class NER extends SkeletonTagger implements Labeler {

    public NER(Document doc, String inputASName, String outputASName, String loadPath, URL path2xgapp) throws ClassNotFoundException, IOException, PersistenceException, ResourceInstantiationException {
        super(doc, inputASName, outputASName, loadPath, path2xgapp);
    }

    @SuppressWarnings("unchecked")
    public void label(String sequenceType, String elementType, String labelType) throws ExecutionException, InvalidOffsetException, ResourceInstantiationException {
        String datType = "Date";
        String locType = "Location";
        String orgType = "Organisation";
        String perType = "Person";
        Corpus corpus = Factory.newCorpus("Edlin Corpus");
        corpus.add(document);
        pipeline.setCorpus(corpus);
        pipeline.execute();
        Factory.deleteResource(corpus);
        Factory.deleteResource(pipeline);
        AnnotationSet sentences = inputAS.get(sequenceType);
        ArrayList<Element<Object>> elements = new ArrayList<Element<Object>>();
        Alphabet labelAlphabet = tagger.getYAlphabet();
        OffsetComparator oc = new OffsetComparator();
        SparseVector[] x;
        Object[] y;
        int[] labels;
        ElementSequence<Element<Object>> sequence;
        Element<Object> element;
        ArrayList<Annotation> tokens;
        String chunk, label;
        for (Annotation sentence : sentences) {
            tokens = new ArrayList(inputAS.get(sentence.getStartNode().getOffset(), sentence.getEndNode().getOffset()).get(elementType));
            Collections.sort(tokens, oc);
            for (Annotation token : tokens) {
                chunk = document.getContent().getContent(token.getStartNode().getOffset(), token.getEndNode().getOffset()).toString();
                label = "?";
                element = new Element<Object>(chunk, label, token);
                elements.add(element);
            }
            x = new SparseVector[elements.size()];
            y = new Object[elements.size()];
            for (int i = 0; i < y.length; i++) y[i] = "?";
            sequence = new ElementSequence(elements, xAlphabet, yAlphabet, x, y, document, inputAS);
            elements = new ArrayList<Element<Object>>();
            labels = tagger.label(extractor.process(sequence).x);
            FeatureMap features = Factory.newFeatureMap();
            long start, end;
            if (labels.length == tokens.size()) {
                for (int i = 0; i < tokens.size(); i++) {
                    if (labels[i] == labelAlphabet.lookupObject("B-DAT")) {
                        start = tokens.get(i).getStartNode().getOffset();
                        end = tokens.get(i).getEndNode().getOffset();
                        while ((i + 1 < tokens.size()) && (labels[i + 1] == labelAlphabet.lookupObject("I-DAT"))) {
                            end = tokens.get(i + 1).getEndNode().getOffset();
                            i++;
                        }
                        outputAS.add(start, end, datType, features);
                    } else if (labels[i] == labelAlphabet.lookupObject("B-LOC")) {
                        start = tokens.get(i).getStartNode().getOffset();
                        end = tokens.get(i).getEndNode().getOffset();
                        while ((i + 1 < tokens.size()) && (labels[i + 1] == labelAlphabet.lookupObject("I-LOC"))) {
                            end = tokens.get(i + 1).getEndNode().getOffset();
                            i++;
                        }
                        outputAS.add(start, end, locType, features);
                    } else if (labels[i] == labelAlphabet.lookupObject("B-ORG")) {
                        start = tokens.get(i).getStartNode().getOffset();
                        end = tokens.get(i).getEndNode().getOffset();
                        while ((i + 1 < tokens.size()) && (labels[i + 1] == labelAlphabet.lookupObject("I-ORG"))) {
                            end = tokens.get(i + 1).getEndNode().getOffset();
                            i++;
                        }
                        outputAS.add(start, end, orgType, features);
                    } else if (labels[i] == labelAlphabet.lookupObject("B-PER")) {
                        start = tokens.get(i).getStartNode().getOffset();
                        end = tokens.get(i).getEndNode().getOffset();
                        while ((i + 1 < tokens.size()) && (labels[i + 1] == labelAlphabet.lookupObject("I-PER"))) {
                            end = tokens.get(i + 1).getEndNode().getOffset();
                            i++;
                        }
                        outputAS.add(start, end, perType, features);
                    }
                }
            } else System.err.println("Unexpected number of tokens in sentence!");
        }
        document.getNamedAnnotationSets().put(outputAS.getName(), outputAS);
    }
}
