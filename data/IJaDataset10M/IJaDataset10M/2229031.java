package com.akivasoftware.comp.parser;

// Import statements
import java.io.*;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.apache.xerces.parsers.SAXParser;
import java.util.*;
import com.akivasoftware.comp.*;

/**
 * ADFStartupHandler - Logic to parse %startup%.xml file
 */
public class ADFStartupHandler extends ADFContentHandler {
    // Class/instance attributes
    private Hashtable m_hstProperties=null;              // Hashtable holding name value pairs
    private Vector m_vecTmp=null;                        // Tmp vector
    private Stack m_stack=new Stack();                   // Stack used in parsing
    private ADFParser parser=null;                       // Parent of this content handler
    
    /** Constructor - no arg */
    public ADFStartupHandler(ADFParser parser){
        super(parser);
        // Initialize me
        this.init();
        
        // Grab parser
        this.parser=parser;
        return;
    }
    
    /**
     * init() - Initialization method
     */
    private void init(){
        m_hstProperties=new Hashtable();
        m_vecTmp=new Vector();
    }
    
    /**
     * startElement() - Parse XML element calls helper method parseXMLAttribute
     */
    public void startElement(String URI, String localName, String rawName, Attributes attributes) throws SAXException {
        // Method attributes
        Properties m_propAttrs=new Properties();         // Properties object that holds Attribute names and values

        try {
            // Start processing
            if (!localName.equalsIgnoreCase("model")){
                // Get number of attributes
                int iAttrs = (attributes != null) ? attributes.getLength() : 0;
               
                // Grab attributes
                m_propAttrs.clear();
                for (int q = 0; q < iAttrs; q++){
                    m_propAttrs.put(attributes.getLocalName(q),attributes.getValue(q));
                } 
                
                // Push properties object upon stack
                m_stack.push(m_propAttrs);
            }
        }
        catch (Exception error){
            error.printStackTrace();
        }
    }
    
    
    /**
     * endElement() -  
     */
    public void endElement(String str, String str1, String str2) throws SAXException {
    }
    
    /**
     * endDocument() - Repackage tmp vector into hashtable
     */
    public void endDocument() throws SAXException {
        Vector m_vecTags=new Vector();
        Vector m_vecValues=new Vector();
         Vector m_vecValue=null;
        
        try {
            // Create container
            m_vecValue=new Vector();
            
            // Grab properties objects from stack
            Enumeration enum=m_stack.elements();
            while (enum.hasMoreElements()){
                Properties prop=(Properties)m_stack.pop();
                
                // Process individual properties object
                Enumeration enum2=prop.keys();
                while (enum2.hasMoreElements()){
                    // rePackage
                    m_vecValue.addElement(prop.getProperty((String)enum2.nextElement()));
                }
            }
            
            // Repackage vector
            int loop=m_vecValue.size();
            for (int q=0;q<loop;q++){
                if (q%2==0){
                    m_vecTags.addElement(m_vecValue.elementAt(q));
                }
                else {
                    m_vecValues.addElement(m_vecValue.elementAt(q));
                }
            }
            
            // Repackage counter
            int repack=m_vecTags.size();
            for (int w=0;w<repack;w++){
                m_hstProperties.put(m_vecTags.elementAt(w),m_vecValues.elementAt(w));
                
                // Debug 
                // System.out.println(m_vecTags.elementAt(w)+"="+m_vecValues.elementAt(w));
            }
            
            // Callback to ADFParser
            parser.setConstructs((Object)m_hstProperties);
        }
        catch (Exception error){
            // Error logging goes here
            error.printStackTrace();
        }
    }
    
    /**
     * getContent() - Returns hashtable with initialization name-value pairs
     */
    public Hashtable getContent(){
        return(m_hstProperties);
    }

    
}

