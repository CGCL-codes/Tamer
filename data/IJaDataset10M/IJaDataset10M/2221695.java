package cunei.decode;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Iterator;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import cunei.confusion.ConfusionLattice;
import cunei.corpus.Language;
import cunei.document.NISTDocument;
import cunei.document.Phrase;
import cunei.document.Sentence;
import cunei.hypothesis.Hypothesis;
import cunei.translate.Translation;
import cunei.translate.TranslationLattice;
import cunei.type.Annotation;
import cunei.type.AnnotationSet;
import cunei.type.SequenceType;
import cunei.type.TypeSequence;
import cunei.util.Log;

public class NISTDecoderWriter extends DecoderWriter {

    private static final String ENCODING = "UTF-8";

    private Annotation document;

    private Annotation genre;

    private XMLStreamWriter writer;

    public NISTDecoderWriter(PrintStream output, Language language, String setName) {
        super(output, language);
        if (Decoder.CFG_DECODE_NBEST.getValue() != 1) Log.getInstance().warning("The NIST file format only supports 1-best output (the remainder will be ignored)");
        document = null;
        genre = null;
        try {
            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            writer = factory.createXMLStreamWriter(output, ENCODING);
            writer.writeStartDocument(ENCODING, "1.0");
            writer.writeCharacters("\n");
            writer.writeDTD(NISTDocument.DTD);
            writer.writeCharacters("\n");
            writer.writeStartElement("mteval");
            writer.writeCharacters("\n");
            writer.writeStartElement("tstset");
            writer.writeAttribute("setid", setName);
            writer.writeAttribute("srclang", NISTDocument.CFG_NIST_SOURCE.getValue());
            writer.writeAttribute("trglang", NISTDocument.CFG_NIST_TARGET.getValue());
            writer.writeAttribute("sysid", NISTDocument.CFG_NIST_SYSTEM.getValue());
        } catch (Exception e) {
            Log.getInstance().severe("Error writing NIST file: " + e.getMessage());
        }
    }

    public void complete() {
        try {
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException e) {
            Log.getInstance().severe("Error writing NIST file: " + e.getMessage());
        }
    }

    public void write(Sentence<ConfusionLattice> sentence, TranslationLattice lattice, Collection<Hypothesis> hypotheses, Sentence<Phrase>[] references) {
        String output = "";
        Iterator<Hypothesis> hypothesesIter = hypotheses.iterator();
        if (hypothesesIter.hasNext()) {
            Translation translation = hypothesesIter.next().getTranslation();
            TypeSequence sequence = translation.getTargetSequence(SequenceType.LEXICAL);
            if (sequence != null) output = sequence.toString();
        }
        final AnnotationSet annotations = sentence.getAnnotations();
        Annotation sentenceId = annotations == null ? null : annotations.get(NISTDocument.SENTENCE).iterator().next();
        Annotation sentenceDocument = annotations == null ? null : annotations.get(NISTDocument.DOCUMENT).iterator().next();
        Annotation sentenceGenre = annotations == null ? null : annotations.get(NISTDocument.GENRE).iterator().next();
        try {
            if (document != sentenceDocument || genre != sentenceGenre) {
                if (document != null || genre != null) writer.writeEndElement();
                document = sentenceDocument;
                genre = sentenceGenre;
                writer.writeCharacters("\n");
                writer.writeStartElement("doc");
                if (document != null) writer.writeAttribute("docid", document.getValue().toString());
                if (genre != null) writer.writeAttribute("genre", genre.getValue().toString());
            }
            writer.writeCharacters("\n");
            writer.writeStartElement("seg");
            if (sentenceId != null) writer.writeAttribute("id", sentenceId.getValue().toString());
            writer.writeCharacters(output);
            writer.writeEndElement();
        } catch (XMLStreamException e) {
            Log.getInstance().severe("Error writing NIST file: " + e.getMessage());
        }
    }
}
