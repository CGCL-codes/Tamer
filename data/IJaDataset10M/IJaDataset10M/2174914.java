package org.xmlcml.textalign.algorithm;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import nu.xom.Builder;
import nu.xom.Document;

/**
 * Aligns markup of named entities in OSCAR
 * 
 * typical input:
 *  <?xml version="1.0" encoding="UTF-8" ?> 
- <PAPER currentID="s2">
  <TITLE>ScrapBook: polymerKatayama-preparation-10</TITLE> 
  <scrapbook mode="show" name="polymerKatayama-preparation-10" hasDoc="yes" /> 
- <BODY>
- <DIV>
- <P>
- <snippet XPoint="/1/2/1/1" id="s1">
  <ne type="Synthesize">Synthesis of ABC -Type Triblock Copolymer PCL</ne> 
  - 
  </snippet>
  </P>
- <P>
- <snippet XPoint="/1/2/2/1" id="s2">
- <ne type="Degass">
  <ne type="Add">A mixture of PNBE - ( 184 mg , 42.8 mmol ) , styrene ( 444 mg , 4.26 mmol ) , CuCl ( 8.4 mg , 85 mmol ) , 1,1,4,7,10,10-hexamethyltriethylenetetramine ( 59 mg , 0.26 mmol ) , and diphenyl ether ( 0.85 mL )</ne> 
  was degassed by three freeze-pump-thaw cycles 
  </ne>
  , and then 
  <ne type="Heat">heated at 130 �C for 22 h</ne> 
  . The resulting 
- <ne type="Add">
  viscous mixture was slowly added to 
  <ne type="Stir">vigorously stirred MeOH ( ca . 30 mL )</ne> 
  </ne>

 * @author pm286
 *
 */
public class OscarScrapbookAligner {

    Document[] documents = new Document[2];

    private KeywordMatrix keywordMatrix;

    private AlphabetMatrix alphabetMatrix;

    private AnnotationList annotationLists[];

    private List<String> keywords[];

    private List<String> phrases[];

    public OscarScrapbookAligner() {
        annotationLists = new AnnotationList[2];
        alphabetMatrix = new AlphabetMatrix();
    }

    public AlphabetMatrix getAlphabetMatrix() {
        return alphabetMatrix;
    }

    public void setAlphabetMatrix(AlphabetMatrix alphabetMatrix) {
        this.alphabetMatrix = alphabetMatrix;
    }

    public KeywordMatrix getKeywordMatrix() {
        return keywordMatrix;
    }

    public void setKeywordMatrix(List<String> keywordList) {
        this.keywordMatrix = new KeywordMatrix();
        keywordMatrix.addKeywordsToMap(keywordList);
    }

    public void readXML(String filename, int serial) {
        if (serial < 0 || serial > 1) {
            throw new RuntimeException("bad serial number");
        }
        if (filename == null) {
            throw new RuntimeException("null filename");
        }
        try {
            documents[serial] = readXMLDocument(filename);
        } catch (Exception e) {
            throw new RuntimeException("Cannot read XML file " + filename, e);
        }
        annotationLists[serial] = new AnnotationList(documents[serial]);
    }

    public void readXMLString(String xmlString, int serial) {
        if (serial < 0 || serial > 1) {
            throw new RuntimeException("bad serial number");
        }
        if (xmlString == null) {
            throw new RuntimeException("null xml string");
        }
        try {
            documents[serial] = new Builder().build(new StringReader(xmlString));
        } catch (Exception e) {
            throw new RuntimeException("Cannot parse XML file " + xmlString, e);
        }
        annotationLists[serial] = new AnnotationList(documents[serial]);
    }

    private Document readXMLDocument(String filename) throws Exception {
        File file = new File(filename);
        Document doc = new Builder().build(new FileReader(file));
        return doc;
    }

    public IntegerAlignment alignKeywords() {
        if (keywordMatrix == null) {
            throw new RuntimeException("No keyword matrix");
        }
        keywords = new ArrayList[2];
        keywords[0] = annotationLists[0].getKeywords();
        keywords[1] = annotationLists[1].getKeywords();
        if (keywordMatrix == null || keywordMatrix.getMatrix() == null) {
            throw new RuntimeException("Must create and initialize keyword matrix");
        }
        IntegerAlignment alignment = keywordMatrix.alignListsOfKeywordsAgainstMap(keywords[0], keywords[1]);
        return alignment;
    }

    public IntegerAlignment alignPhrases() {
        phrases = new ArrayList[2];
        phrases[0] = annotationLists[0].getPhrases();
        phrases[1] = annotationLists[1].getPhrases();
        IntegerAlignment alignment = alphabetMatrix.alignListsOfStrings(phrases[0], phrases[1]);
        return alignment;
    }

    public List<String>[] getPhrases() {
        return phrases;
    }
}
