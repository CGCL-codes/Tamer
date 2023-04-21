package com.controltier.ctl.authorization;

import com.controltier.ctl.Constants;
import org.apache.log4j.Category;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;
import java.io.IOException;
import java.util.regex.PatternSyntaxException;

/**
 * AclValidator, acls validator handler
 * Validates the acls.xml file from the acls.dtd both located in ${ctl.base}/etc directory
 * In addition to verifying appropiate CDATA, this validator will do additional type checking
 * via implementing ContentHandler interface's startElement() method and validating ranges, etc
 * specified in attributes.
 * @author  Chuck Scott <a href="mailto:chuck@controltier.com">chuck@controltier.com</a>
 */
public class AclValidator extends DefaultHandler {

    static Category logger = Category.getInstance(AclValidator.class.getName());

    private Locator locator;

    private boolean inRole = false;

    private void setInRole(boolean inRole) {
        this.inRole = inRole;
    }

    private boolean getInRole() {
        return this.inRole;
    }

    private boolean inCommand = false;

    private void setInCommand(boolean inCommand) {
        this.inCommand = inCommand;
    }

    private boolean getInCommand() {
        return this.inCommand;
    }

    private boolean inContext = false;

    private void setInContext(boolean inContext) {
        this.inContext = inContext;
    }

    private boolean getInContext() {
        return this.inContext;
    }

    private boolean inTimeandday = false;

    private void setInTimeandday(boolean inTimeandday) {
        this.inTimeandday = inTimeandday;
    }

    private boolean getInTimeandday() {
        return this.inTimeandday;
    }

    private String aclsXml;

    private void setAclsXml(String aclsXml) {
        this.aclsXml = aclsXml;
    }

    /**
    * returns the path to the acls.xml file
    *
    * @return String
    */
    public String getAclsXml() {
        return this.aclsXml;
    }

    private String aclsXmlUri;

    private void setAclsXmlUri(String aclsXmlUri) {
        this.aclsXmlUri = aclsXmlUri;
    }

    /**
    * returns the file based uri to the acls.xml file
    *
    * @return String
    */
    public String getAclsXmlUri() {
        return this.aclsXmlUri;
    }

    private int errorCode = 0;

    /**
    * errorCode setter if exception or other error occurs
    *
    * @param errorCode
    */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
    * returns the error code if exception or other error code occurs
    *
    * @return int
    */
    public int getErrorCode() {
        return this.errorCode;
    }

    private String errorMsg;

    /**
    * errorMsg setter if exception or other error occurs
    *
    * @param errorMsg
    */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
    * returns the error msg if exception or other error code occurs
    *
    * @return String
    */
    public String getErrorMsg() {
        return this.errorMsg;
    }

    /**
    * unimplemented warning method
    */
    public void warning(SAXParseException ex) {
        handleParseException(ex);
    }

    /**
    * unimplemented error method
    */
    public void error(SAXParseException ex) {
        handleParseException(ex);
    }

    /**
    * unimplemented fatalError method
    */
    public void fatalError(SAXParseException ex) {
        handleParseException(ex);
    }

    /**
     * contructor, takes ctlBase as parameter to find acls.xml file. 
     * @param ctlBase
     * @throws IOException
     */
    public AclValidator(String ctlBase) throws IOException {
        this(new File(Constants.getAclsXml(ctlBase)));
    }

    protected AclValidator(File aclsFile) throws IOException {
        super();
        logger.debug("AclValidator(), constructing");
        String aclsXml = aclsFile.getAbsolutePath();
        if (!aclsFile.exists()) {
            throw new IOException("file " + aclsXml + " does not exist");
        }
        if (!aclsFile.isFile()) {
            throw new IOException("file " + aclsXml + " is not a regular file");
        }
        if (aclsFile.canWrite()) {
            logger.warn("file " + aclsXml + " cannot be writable");
        }
        this.setAclsXml(aclsXml);
        this.setAclsXmlUri(aclsFile.toURL().toExternalForm());
    }

    /**
     * validate method, obtains a SAX Parser, sets the contentHandler and errorHandler,
     * and turns on dtd loading/validation.  
     * If parser detects dtd validation error, catches exception and sets errorMsg and errorCode.
     */
    public boolean validate() {
        logger.debug("AclValidator.validate(), entering");
        final XMLReader parser;
        try {
            parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        } catch (Exception e) {
            this.setErrorMsg("Caught exception creating parser: " + e);
            this.setErrorCode(-1);
            return false;
        }
        parser.setContentHandler(this);
        parser.setErrorHandler(this);
        try {
            ((XMLReader) parser).setFeature("http://xml.org/sax/features/validation", true);
            ((XMLReader) parser).setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", true);
            parser.setFeature("http://apache.org/xml/features/standard-uri-conformant", false);
        } catch (Exception e) {
            this.setErrorMsg("Caught exception setting parser features: " + e);
            this.setErrorCode(-1);
            return false;
        }
        try {
            parser.parse(this.getAclsXmlUri());
        } catch (Exception e) {
            this.handleParseException(e);
        }
        if (this.getErrorCode() != 0) return false;
        return true;
    }

    /**
     * sax ContentHandler implemented method.  
     */
    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
        if (localName.equals("context")) {
            this.setInContext(true);
        }
        if (this.getInContext()) validateAttrs(attrs, "context");
        if (localName.equals("role")) {
            this.setInRole(true);
        }
        if (this.getInRole()) validateAttrs(attrs, "role");
        if (localName.equals("command")) {
            this.setInCommand(true);
        }
        if (this.getInCommand()) validateAttrs(attrs, "command");
        if (localName.equals("timeandday")) {
            this.setInTimeandday(true);
        }
        if (this.getInTimeandday()) validateAttrs(attrs, "timeandday");
    }

    private void validateAttrs(Attributes attrs, String elementType) throws SAXException {
        String expType = null;
        String exp = null;
        try {
            if (elementType.equals("context")) {
                String depotExp = attrs.getValue("", "depot");
                expType = "depot";
                exp = depotExp;
                Regexp.checkExp(depotExp, "");
                String typeExp = attrs.getValue("", "type");
                expType = "type";
                exp = typeExp;
                Regexp.checkExp(typeExp, "");
                String nameExp = attrs.getValue("", "name");
                expType = "name";
                exp = nameExp;
                Regexp.checkExp(nameExp, "");
            } else if (elementType.equals("role")) {
                String name = attrs.getValue("", "name");
            } else if (elementType.equals("command")) {
                String moduleExp = attrs.getValue("", "module");
                expType = "module";
                exp = moduleExp;
                Regexp.checkExp(moduleExp, "");
                String nameExp = attrs.getValue("", "name");
                expType = "name";
                exp = nameExp;
                Regexp.checkExp(nameExp, "");
            } else if (elementType.equals("timeandday")) {
                String dayExp = attrs.getValue("", "day");
                String hourExp = attrs.getValue("", "hour");
                String minuteExp = attrs.getValue("", "minute");
                try {
                    expType = "day";
                    exp = dayExp;
                    TimeanddayExp.checkExp(dayExp, "day");
                    expType = "hour";
                    exp = hourExp;
                    TimeanddayExp.checkExp(hourExp, "hour");
                    expType = "minute";
                    exp = minuteExp;
                    TimeanddayExp.checkExp(minuteExp, "minute");
                } catch (NumberFormatException e) {
                    String msg = "Caught NumberFormatException for " + expType + ": " + exp;
                    Exception e1 = new Exception(msg);
                    this.handleParseException(e1);
                }
            } else {
            }
        } catch (PatternSyntaxException e) {
            String msg = "Caught PatternSyntaxException for " + expType + ": " + exp;
            Exception e1 = new Exception(msg);
            this.handleParseException(e1);
        }
    }

    /**
     * sax ContentHandler implemented method.  
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (localName.equals("role")) {
            this.setInRole(false);
        }
        if (localName.equals("command")) {
            this.setInCommand(false);
        }
        if (localName.equals("context")) {
            this.setInContext(false);
        }
        if (localName.equals("timeandday")) {
            this.setInTimeandday(false);
        }
    }

    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    private void handleParseException(Exception e) {
        this.setErrorCode(-1);
        int ln = this.locator != null ? this.locator.getLineNumber() : -1;
        int cn = this.locator != null ? this.locator.getColumnNumber() : -1;
        String errLoc = "Line #: " + new Integer(ln).toString() + " Column #: " + new Integer(cn).toString();
        this.setErrorMsg(errLoc + ", " + e.getMessage());
        logger.error("Parse Exception", e);
    }
}
