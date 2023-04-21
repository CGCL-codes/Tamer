package com.missingmatter.common.xml;

import javax.xml.parsers.*;
import java.net.URL;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import java.io.FileReader;
import java.io.FilterReader;
import java.io.StringReader;
import java.io.Reader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;

/**
 * 
 * Takes a xml document, and builds and internal data structure for the document
 * that is then naviagated and can have various operations applied against it.
 * 
 * The set of operations include - generating java stubs, validating a component
 * def, generating stubs that are used for scripting etc.
 * 
 * @copyright Missing Matter Productions Pty Ltd 2004, 2005
 * @author awhitesi
 *  
 */
public class GenericParser implements IComponentParser {

    private GenericContentHandler m_ContentHandler = new GenericContentHandler();

    private boolean m_bValidating = false;

    private SAXParser m_SAXParser = null;

    public GenericParser() {
    }

    /**
	 * Based on the settings defined against the GenericCompiler instance
	 * initialise the parser etc. (ie. load the xml parser).
	 * 
	 * Note :
	 * setResultType and setValidation should be called before calling
	 * initialise().
	 * 
	 * @throws Exception
	 */
    public void initialise() throws Exception {
        SAXParserFactory l_ParserFactory = SAXParserFactory.newInstance();
        l_ParserFactory.setValidating(isValidating());
        m_SAXParser = l_ParserFactory.newSAXParser();
    }

    /**
	 * Register a TagHandler with the ContentHandler.
	 * @param p_ElementPath
	 * @param p_TagHandler
	 */
    public void addTagHandler(String p_sElementPath, ITagHandler p_TagHandler) {
        getContentHandler().addTagHandler(p_sElementPath, p_TagHandler);
    }

    /**
	  * Return the instance of the content handler associated with the
	  * instance of the generic compiler.
	  * 
	  * @param p_ContentHandler
	  * @return
	  */
    public GenericContentHandler getContentHandler() {
        return m_ContentHandler;
    }

    /**
	 * Parse the document into an object hierarchy. The resultant object
	 * hierarchy may then have various operations applied to it. The returned
	 * object is the root of the object hierarchy.
	 * 
	 * @param p_Document
	 * @return
	 */
    public Object parseDocument(String p_Document) throws FrameworkParserException {
        Object l_Result = null;
        if (p_Document != null) {
            StringReader l_Reader = new StringReader(p_Document);
            l_Result = parseDocument(l_Reader);
            l_Reader = null;
        }
        return l_Result;
    }

    /**
	 * Parse the document into an object hierarchy. The resultant object
	 * hierarchy may then have various operations applied to it. The returned
	 * object is the root of the object hierarchy.
	 * 
	 * @param p_Document
	 * @return
	 */
    public Object parseDocument(URL p_Document) throws FrameworkParserException {
        Object l_Result = null;
        if (p_Document != null) {
            try {
                InputStream l_tmpInputStream = p_Document.openStream();
                InputStreamReader l_Reader = new InputStreamReader(l_tmpInputStream);
                l_tmpInputStream = null;
                l_Result = parseDocument(l_Reader);
                l_Reader = null;
            } catch (FrameworkParserException l_Ex1) {
                throw l_Ex1;
            } catch (Exception l_Ex2) {
                throw FrameworkParserException.createGeneralCompileException(l_Ex2);
            }
        }
        return l_Result;
    }

    /**
	 * Parse the document into an object hierarchy. The resultant object
	 * hierarchy may then have various operations applied to it. The returned
	 * object is the root of the object hierarchy.
	 * 
	 * @param p_Document
	 * @return
	 */
    public Object parseDocument(File p_Document) throws FrameworkParserException {
        Object l_Result = null;
        if (p_Document != null) {
            try {
                FileReader l_Reader = new FileReader(p_Document);
                l_Result = parseDocument(l_Reader);
                l_Reader = null;
            } catch (FrameworkParserException l_Ex1) {
                throw l_Ex1;
            } catch (Exception l_Ex2) {
                throw FrameworkParserException.createGeneralCompileException(l_Ex2);
            }
        }
        return l_Result;
    }

    /**
	 * Parse the document into an object hierarchy. The resultant object
	 * hierarchy may then have various operations applied to it. The returned
	 * object is the root of the object hierarchy.
	 * 
	 * @param p_Document
	 * @return
	 */
    public Object parseDocument(Reader p_Document) throws FrameworkParserException {
        Object l_Result = null;
        if (m_SAXParser != null) {
            try {
                InputSource l_InputSource = new InputSource(p_Document);
                GenericContentHandler l_ContentHandler = getContentHandler();
                l_ContentHandler.initialise();
                m_SAXParser.parse(l_InputSource, l_ContentHandler);
                l_Result = l_ContentHandler.getResult();
                l_InputSource = null;
                l_ContentHandler = null;
            } catch (Exception l_Ex) {
                throw FrameworkParserException.createGeneralCompileException(l_Ex);
            } finally {
                try {
                    p_Document.close();
                } catch (Exception l_Ex2) {
                }
            }
        }
        return l_Result;
    }

    /**
	 * @return Returns the m_bValidating.
	 */
    public boolean isValidating() {
        return m_bValidating;
    }

    /**
	 * @para
	 * m validating
	 *            The m_bValidating to set.
	 */
    public void setValidating(boolean p_bValidating) {
        m_bValidating = p_bValidating;
    }

    /**
	 * 
	 * @param p_sResultTypeName
	 */
    public void setResultType(String p_sResultTypeName) {
        getContentHandler().setResultType(p_sResultTypeName);
    }
}
