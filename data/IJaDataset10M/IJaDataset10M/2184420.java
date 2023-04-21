package ar.uba.fi.jxmlcoverage.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.transaction.annotation.Transactional;
import ar.uba.fi.jxmlcoverage.SpringBeanLocator;
import ar.uba.fi.jxmlcoverage.model.Application;
import ar.uba.fi.jxmlcoverage.model.DataType;
import ar.uba.fi.jxmlcoverage.model.Framework;
import ar.uba.fi.jxmlcoverage.model.SchemaCoverage;
import ar.uba.fi.jxmlcoverage.model.SubDomain;
import ar.uba.fi.jxmlcoverage.persistence.FrameworkRepository;
import ar.uba.fi.jxmlcoverage.util.TechnicalException;
import ar.uba.fi.jxmlcoverage.util.UserException;

public class FrameworkServiceImpl implements FrameworkService {

    private FrameworkRepository frameworkRepository;

    private static Logger logger = Logger.getLogger(FrameworkServiceImpl.class);

    @Override
    @Transactional
    public void registerFramework(String name) throws UserException, TechnicalException {
        Framework framework = new Framework(name);
        if (!frameworkRepository.exists(framework)) {
            framework.setComplete(false);
            frameworkRepository.storeFramework(framework);
        } else {
            logger.debug("The framework already exists");
            throw new UserException("1002", new Exception());
        }
    }

    @Override
    @Transactional
    public void unregisterFramework(String name) throws UserException, TechnicalException {
        Framework framework = new Framework(name);
        try {
            if (frameworkRepository.exists(framework)) {
                frameworkRepository.deleteFramework(framework);
            } else {
                logger.debug("The framework doesn't exists");
                throw new UserException("1001", new Exception());
            }
        } catch (TechnicalException te) {
            throw new TechnicalException("0002", new Exception());
        }
    }

    public void setFrameworkRepository(FrameworkRepository frameworkRepository) {
        this.frameworkRepository = frameworkRepository;
    }

    @Override
    @Transactional
    public void registerApplications(String frameworkName, ArrayList<String> applications) throws TechnicalException, UserException {
        Framework framework = new Framework(frameworkName);
        if (frameworkRepository.exists(framework)) {
            framework = frameworkRepository.loadFramework(frameworkName);
            List<Application> appList = loadApplications(applications);
            framework.getApplications().addAll(appList);
            framework.setComplete(false);
            frameworkRepository.storeFramework(framework);
        } else {
            logger.debug("The framework doesn't exists");
            throw new UserException("1001", new Exception());
        }
    }

    private List<Application> loadApplications(ArrayList<String> applications) {
        List<Application> appList = new ArrayList<Application>();
        Iterator<String> iter = applications.iterator();
        while (iter.hasNext()) {
            Application app = new Application();
            String appfile = iter.next();
            File file = new File(appfile);
            app.setName(file.getName());
            app.setDirApp(appfile.substring(0, appfile.length() - file.getName().length()));
            appList.add(app);
        }
        return appList;
    }

    @Override
    @Transactional
    public void registerSchemas(String frameworkName, ArrayList<String> schemasCoverage) throws TechnicalException, UserException {
        Framework framework = new Framework(frameworkName);
        if (frameworkRepository.exists(framework)) {
            framework = frameworkRepository.loadFramework(frameworkName);
            List<DataType> dataTypesList = framework.getDataTypes();
            List<SchemaCoverage> schemaList = loadSchemas(schemasCoverage, dataTypesList);
            framework.getSchemas().addAll(schemaList);
            framework.setComplete(false);
            frameworkRepository.storeFramework(framework);
        } else {
            logger.debug("The framework doesn't exists");
            throw new UserException("1001", new Exception());
        }
    }

    private List<SchemaCoverage> loadSchemas(ArrayList<String> schemas, List<DataType> dataTypesList) {
        List<SchemaCoverage> schemaList = new ArrayList<SchemaCoverage>();
        Iterator<String> iter = schemas.iterator();
        while (iter.hasNext()) {
            SchemaCoverage schemaCoverage = new SchemaCoverage();
            String schemafile = iter.next();
            File file = new File(schemafile);
            schemaCoverage.setName(file.getName());
            schemaCoverage.setFilePath(schemafile.substring(0, schemafile.length() - file.getName().length()));
            schemaCoverage.loadConfigureElements(schemafile, dataTypesList);
            schemaList.add(schemaCoverage);
        }
        return schemaList;
    }

    @Override
    @Transactional
    public void registerDataTypes(String frameworkName, String dataTypeFile) throws TechnicalException, UserException {
        Framework framework = new Framework(frameworkName);
        framework = frameworkRepository.loadFramework(frameworkName);
        if (framework != null) {
            List<DataType> dataTypeList = loadDataTypes(dataTypeFile);
            framework.setDataTypes(dataTypeList);
            framework.setComplete(false);
            frameworkRepository.storeFramework(framework);
        } else {
            logger.debug("The framework doesn't exist");
            throw new UserException("1001", new Exception());
        }
    }

    @Override
    @Transactional
    public Framework loadFramework(String frameworkName) throws TechnicalException {
        Framework framework = frameworkRepository.loadFramework(frameworkName);
        return framework;
    }

    @Override
    @Transactional
    public void createPairs(String frameworkName) throws TechnicalException {
        Framework framework = new Framework(frameworkName);
        framework = frameworkRepository.loadFramework(frameworkName);
        PairService pairService = (PairService) SpringBeanLocator.getInstance().getBean("pairService");
        pairService.createPairs(framework);
    }

    private List<DataType> loadDataTypes(String dataTypeFile) {
        List<DataType> dataTypesList = new ArrayList<DataType>();
        SAXReader xmlReader = new SAXReader();
        try {
            File datatypes = new File(dataTypeFile);
            Document doc = xmlReader.read(datatypes);
            List<Node> list = doc.selectNodes("//datatypes/datatype");
            for (Node datatypeNode : list) {
                DataType dataType = new DataType();
                String name = datatypeNode.selectSingleNode("name").getText();
                dataType.setName(name);
                String stringDataType = datatypeNode.selectSingleNode("stringdatatype").getText();
                dataType.setDataType(stringDataType);
                List<Node> subdomainsNodeList = datatypeNode.selectNodes("subdomains/subdomain");
                List<SubDomain> subdomainList = new ArrayList<SubDomain>();
                for (Node subdomainNode : subdomainsNodeList) {
                    SubDomain subdomain = new SubDomain();
                    String subdomainName = subdomainNode.selectSingleNode("name").getText();
                    subdomain.setName(subdomainName);
                    String restriction = subdomainNode.selectSingleNode("restriction").getText();
                    subdomain.setRestriction(restriction);
                    subdomainList.add(subdomain);
                }
                dataType.setSubDomain(subdomainList);
                dataTypesList.add(dataType);
            }
        } catch (DocumentException de) {
            logger.debug("An error occurried trying to load the datatype archive", de);
            return null;
        }
        return dataTypesList;
    }
}
