package vmp.gate.tbl.examples;

import vmp.gate.tbl.*;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialController;
import gate.util.GateException;

/**
 *
 * @author Administrator
 */
public class StandAloneSpanishPOS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws GateException {
        try {
            gate.Gate.init();
            FeatureMap params = Factory.newFeatureMap();
            String home = System.getProperty("basedir");
            params.put("sourceUrl", "file:/" + home + "/resources/test/train.init");
            params.put("encoding", "utf8");
            params.put("format", "word pos gaze ort chunk tchunk");
            Factory.createResource("vmp.gate.tbl.DocumentConverter", params);
            Document doc = (Document) gate.Gate.getCreoleRegister().getLrInstances("gate.corpora.DocumentImpl").get(0);
            params.clear();
            params.put("lexiconURL", "file:/" + home + "/resources/test/lexiconSpanishNER.out");
            params.put("encoding", "utf8");
            LexiconLR lexicon = (LexiconLR) Factory.createResource("vmp.gate.tbl.LexiconLR", params);
            params.clear();
            params.put("gold", "tchunk");
            params.put("templatesUrl", "file:/" + home + "/resources/test/spanishNERTemplates");
            params.put("threshold", "20");
            params.put("rulesUrl", "file:/" + home + "/resources/test/rulesSpanishNer.out");
            Trainer trainer = (Trainer) Factory.createResource("vmp.gate.tbl.Trainer", params);
            SerialController app = (SerialController) Factory.createResource("gate.creole.SerialController", Factory.newFeatureMap(), Factory.newFeatureMap(), "hola");
            trainer.setDocument(doc);
            trainer.setLexiconLR(lexicon);
            app.add(trainer);
            System.out.println(doc.getAnnotations().get("Sentence").size());
            System.out.println("execute");
            app.execute();
        } catch (ResourceInstantiationException ex) {
            ex.printStackTrace();
        }
    }
}
