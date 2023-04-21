package eu.fbk.hlt.annotation;

import java.util.ArrayList;
import java.util.List;
import eu.fbk.hlt.common.Configurable;
import eu.fbk.hlt.common.EDITSException;
import eu.fbk.hlt.common.conffile.Configuration;
import eu.fbk.hlt.common.conffile.Type;
import eu.fbk.hlt.common.module.Definition;
import eu.fbk.hlt.common.module.Implementation;
import eu.fbk.hlt.common.module.ModuleInfo;
import eu.fbk.hlt.common.module.OptionInfo;
import eu.fbk.hlt.edits.EDITS;
import eu.fbk.hlt.edits.Etaf;
import eu.fbk.hlt.edits.etaf.AnnotatedText;
import eu.fbk.hlt.edits.etaf.Document;
import eu.fbk.hlt.edits.etaf.EntailmentCorpus;
import eu.fbk.hlt.edits.etaf.EntailmentPair;
import eu.fbk.hlt.edits.etaf.Paragraph;
import eu.fbk.hlt.edits.etaf.ProcessedQuestion;
import eu.fbk.hlt.edits.etaf.QuestionCollection;
import eu.fbk.hlt.edits.etaf.RelationCluster;
import eu.fbk.hlt.edits.etaf.RelationClusters;
import eu.fbk.hlt.edits.etaf.TextCollection;
import eu.fbk.hlt.edits.processor.Interlocutor;

/**
 * @author Milen Kouylekov
 */
public abstract class TextAnnotator extends Configurable {

    public enum Language {

        BULGARIAN, ENGLISH, GERMAN, ITALIAN, SPANNISH
    }

    public static String LANGUAGE_OPTION = "language";

    private Language language = Language.ENGLISH;

    public void annotate(AnnotatedText text) throws EDITSException {
        run(gather(text));
    }

    public void annotate(Document document) throws EDITSException {
        run(gather(document));
    }

    public void annotate(EntailmentCorpus collection) throws EDITSException {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        for (EntailmentPair p : collection.getPair()) {
            if (Etaf.attribute(p, "fail") != null && Etaf.attribute(p, "fail").equals("true")) {
                System.out.print("*");
                continue;
            }
            if (p.getTAnnotation().size() == 0) {
                AnnotatedText t = new AnnotatedText();
                t.setString(p.getT());
                p.getTAnnotation().add(t);
            }
            if (p.getHAnnotation().size() == 0) {
                AnnotatedText t = new AnnotatedText();
                t.setString(p.getH());
                p.getHAnnotation().add(t);
            }
            ss.addAll(p.getTAnnotation());
            ss.addAll(p.getHAnnotation());
        }
        run(ss);
    }

    public void annotate(Interlocutor<EntailmentPair, EntailmentPair> processor) throws EDITSException {
        EntailmentCorpus c = new EntailmentCorpus();
        while (processor.hasNext()) {
            c.getPair().add(processor.next());
            if (c.getPair().size() == 999) {
                annotate(c);
                for (EntailmentPair p : c.getPair()) processor.handle(p);
                c.getPair().clear();
            }
        }
        annotate(c);
        for (EntailmentPair p : c.getPair()) {
            processor.handle(p);
        }
        System.gc();
        System.gc();
        processor.close();
    }

    public void annotate(List<AnnotatedText> list) throws EDITSException {
        run(list);
    }

    public void annotate(QuestionCollection c) throws EDITSException {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        for (ProcessedQuestion d : c.getQuestion()) {
            ss.add(d);
        }
        run(ss);
    }

    public void annotate(RelationClusters clusters) throws EDITSException {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        for (RelationCluster p : clusters.getRelationCluster()) {
            ss.addAll(p.getPattern());
            ss.addAll(p.getQuestion());
        }
        run(ss);
    }

    public AnnotatedText annotate(String text) throws EDITSException {
        AnnotatedText t = new AnnotatedText();
        t.setString(text);
        run(gather(t));
        return t;
    }

    public void annotate(TextCollection collection) throws EDITSException {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        for (Document d : collection.getDocument()) {
            ss.addAll(gather(d));
        }
        run(ss);
    }

    @Override
    public void configure(Configuration conf) throws EDITSException {
        super.configure(conf);
        if (option(LANGUAGE_OPTION) != null) language = Language.valueOf(option(LANGUAGE_OPTION));
    }

    public Language getLanguage() {
        return language;
    }

    @Override
    public ModuleInfo info() {
        ModuleInfo desc = new ModuleInfo();
        desc.subModules().addAll(definition().subModules());
        desc.options().addAll(definition().options());
        return desc;
    }

    public abstract void run(List<AnnotatedText> list) throws EDITSException;

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String type() {
        return EDITS.TEXT_ANONOTATOR;
    }

    public static Definition definition() {
        Definition def = new Definition();
        def.setType(EDITS.TEXT_ANONOTATOR);
        def.setTitle("Text Annotator");
        def.setDescription(EDITS.system().description("text-annotator"));
        def.setAbbreviation("ta");
        OptionInfo o = null;
        o = new OptionInfo();
        o.setName(LANGUAGE_OPTION);
        o.setType(Type.ENUMERATED);
        o.setDescription(EDITS.system().description("language"));
        o.context().add(TextAnnotatorHandler.ANNOTATE_COMMAND);
        o.setAbbreviation("l");
        o.setMultiple(false);
        o.setRequired(false);
        o.setDefault(Language.ENGLISH.toString());
        for (Language l : Language.values()) o.values().add(l.toString());
        def.options().add(o);
        Implementation imple = new Implementation();
        imple.setAlias("simpletok");
        imple.setClassName("eu.fbk.hlt.annotation.simple.SimpleTextAnnotator");
        imple.setDefault(true);
        def.implementations().add(imple);
        return def;
    }

    private static final List<AnnotatedText> gather(AnnotatedText s) {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        ss.add(s);
        return ss;
    }

    private static final List<AnnotatedText> gather(Document d) {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        if (d.getParagraph() == null || d.getParagraph().size() == 0) ss.add(d); else {
            for (Paragraph p : d.getParagraph()) {
                ss.addAll(gather(p));
            }
        }
        return ss;
    }

    private static final List<AnnotatedText> gather(Paragraph p) {
        List<AnnotatedText> ss = new ArrayList<AnnotatedText>();
        if (p.getSentence() == null || p.getSentence().size() == 0) ss.add(p); else ss.addAll(p.getSentence());
        return ss;
    }
}
