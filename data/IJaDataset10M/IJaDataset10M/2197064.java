package org.proteored.miapeapi.xml.mzml;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.exceptions.MiapeDatabaseException;
import org.proteored.miapeapi.exceptions.MiapeSecurityException;
import org.proteored.miapeapi.interfaces.ms.MiapeMSDocument;
import org.proteored.miapeapi.interfaces.persistence.PersistenceManager;
import org.proteored.miapeapi.interfaces.xml.MiapeXmlFile;
import org.proteored.miapeapi.spring.SpringHandler;
import org.xml.sax.SAXException;

public class MiapeMzMLFile extends MiapeXmlFile<MiapeMSDocument> {

    private String userName;

    private String password;

    private PersistenceManager dbManager;

    private ControlVocabularyManager cvUtil;

    private String projectName;

    public MiapeMzMLFile(byte[] bytes) throws IOException {
        super(bytes);
    }

    public MiapeMzMLFile(File file) {
        super(file);
    }

    public MiapeMzMLFile(String name) {
        super(name);
    }

    public void initDefault() throws MiapeDatabaseException, MiapeSecurityException {
        this.userName = SpringHandler.getInstance().getUserName();
        this.password = SpringHandler.getInstance().getPassword();
        this.cvUtil = SpringHandler.getInstance().getCVManager();
    }

    public void setUser(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDbManager(PersistenceManager dbManager) {
        this.dbManager = dbManager;
    }

    public void setCvUtil(ControlVocabularyManager cvUtil) {
        this.cvUtil = cvUtil;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public MiapeMSDocument toDocument() throws MiapeDatabaseException, MiapeSecurityException {
        return MSMiapeFactory.getFactory().toDocument(this, dbManager, cvUtil, userName, password, projectName);
    }

    public boolean isValidMzML() {
        boolean retval;
        SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        URL schemaLocation;
        schemaLocation = this.getClass().getClassLoader().getResource("mzML1.1.1-idx.xsd");
        if (schemaLocation == null) return false;
        javax.xml.validation.Schema schema;
        try {
            schema = factory.newSchema(schemaLocation);
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not compile Schema for file: " + schemaLocation);
        }
        Validator validator = schema.newValidator();
        Source source = new StreamSource(file);
        try {
            validator.validate(source);
            retval = true;
        } catch (SAXException ex) {
            System.out.println(file.getName() + " is not valid because ");
            System.out.println(ex.getMessage());
            retval = false;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Could not validate file because of file read problems for source: " + file.getAbsolutePath());
        }
        return retval;
    }
}
