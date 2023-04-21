package ru.ksu.niimm.cll.mocassin.crawl.parser.gate;

import java.util.List;
import ru.ksu.niimm.cll.mocassin.rdf.ontology.MocassinOntologyClasses;
import ru.ksu.niimm.cll.mocassin.crawl.parser.arxmliv.ArxmlivStructureElementTypes;

/**
 * Structural element is an element that belongs to the rhetorical structure
 * (section, subsection) or mathematical structure (theorem, proof) of a
 * document
 * 
 * @see ArxmlivStructureElementTypes
 * 
 * @author nzhiltsov
 * 
 */
public interface StructuralElement {

    /**
	 * 
	 * @return identifier (added for compatibility with GATE)
	 */
    int getId();

    /**
	 * 
	 * @return URI
	 */
    String getUri();

    /**
	 * 
	 * @return start offset of an element in the GATE representation
	 */
    long getGateStartOffset();

    /**
	 * 
	 * @return end offset of an element in the GATE representation
	 */
    long getGateEndOffset();

    /**
	 * 
	 * @return start line of an element in the Latex source file
	 */
    int getLatexStartLine();

    /**
	 * 
	 * @return end line of an element in the Latex source file
	 */
    int getLatexEndLine();

    void setLatexStartLine(int latexStartLine);

    void setLatexEndLine(int latexEndLine);

    /**
	 * 
	 * @return label values of an element to make references to it
	 */
    List<String> getLabels();

    void setLabels(List<String> labels);

    /**
	 * 
	 * @return name of element according to its type (e.g. 'section', 'thm',
	 *         'example')
	 */
    String getName();

    List<String> getContents();

    void setContents(String... contents);

    /**
	 * 
	 * @return tokens of a title (e.g. 'Introduction', 'Theorem 1.1'), return
	 *         null if the title is absent
	 */
    String getTitle();

    void setPredictedClass(MocassinOntologyClasses clazz);

    MocassinOntologyClasses getPredictedClass();

    /**
	 * returns the number of the page where the beginning of a segment is
	 * located; the value starts from 1.
	 * 
	 * 
	 * @return
	 */
    int getStartPageNumber();

    void setStartPageNumber(int startPageNumber);
}
