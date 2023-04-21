package br.gov.demoiselle.eclipse.main.core.editapp;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xml.sax.SAXException;
import br.gov.demoiselle.eclipse.main.IPluginFacade;
import br.gov.demoiselle.eclipse.util.editapp.DAOHelper;
import br.gov.demoiselle.eclipse.util.utility.EclipseUtil;
import br.gov.demoiselle.eclipse.util.utility.FileUtil;
import br.gov.demoiselle.eclipse.util.utility.FrameworkUtil;
import br.gov.demoiselle.eclipse.util.utility.classwriter.ClassHelper;
import br.gov.demoiselle.eclipse.util.utility.classwriter.ClassRepresentation;
import br.gov.demoiselle.eclipse.util.utility.classwriter.FieldHelper;
import br.gov.demoiselle.eclipse.util.utility.classwriter.MethodHelper;
import br.gov.demoiselle.eclipse.util.utility.classwriter.ParameterHelper;
import br.gov.demoiselle.eclipse.util.utility.plugin.Configurator;
import br.gov.demoiselle.eclipse.util.utility.xml.reader.XMLReader;

/**
 * Implements read/write DAO classes information
 * Reads DAOs from a list in the wizard xml file, locates in selected project
 * Writes BCs configuration and java file 
 * see IPluginFacade
 * @author CETEC/CTJEE
 */
public class DAOFacade implements IPluginFacade<DAOHelper> {

    private String xml;

    private boolean insert = false;

    /**
	 * Try to read information of all DAO of the user application 
	 * 
	 * @return List<DaoHelper> - Returns a list of DAOHelper
	 * @throws SAXException 
	 * @throws IOException 
	 */
    public List<DAOHelper> read() throws IOException, SAXException {
        Configurator reader = new Configurator();
        List<DAOHelper> daoList = reader.readDaos(xml);
        if (daoList != null && daoList.size() > 0) {
            for (DAOHelper dao : daoList) {
                dao.setReadOnly(true);
            }
        }
        return daoList;
    }

    /**
	 * Write a DAO of the parameter
	 * 
	 * @param DAOHelper Dao to be written
	 * @throws Exception 
	 */
    public void write(DAOHelper dao) throws Exception {
        List<DAOHelper> daos = new ArrayList<DAOHelper>();
        daos.add(dao);
        write(daos);
    }

    /**
	 * 
	 * @return if has tag to NODE_DAOS elements in xml file
	 * @throws Exception 
	 */
    public boolean hasDAO() throws Exception {
        if (XMLReader.hasElement(xml, NODE_DAOS)) {
            return true;
        }
        return false;
    }

    /**
	 * Write all DAOs of the parameter List that is not marked with readonly attribute
	 * 
	 * @param List<DAOHelper> Dao list to be written
	 * @throws Exception 
	 */
    public void write(List<DAOHelper> daos) throws Exception {
        if (daos != null) {
            try {
                this.setInsert(!this.hasDAO());
                for (Iterator<?> iterator = daos.iterator(); iterator.hasNext(); ) {
                    DAOHelper dao = (DAOHelper) iterator.next();
                    if (!dao.isReadOnly()) {
                        ClassHelper clazzInterface = generateInterface(dao);
                        FileUtil.writeClassFile(dao.getAbsolutePath(), clazzInterface, true, true);
                        dao.setDaoInterface(new ClassRepresentation(dao.getInterfaceFullName()));
                        File dir = new File(dao.getAbsolutePathImpl());
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        ClassHelper clazzImpl = generateImplementation(dao);
                        FileUtil.writeClassFile(dao.getAbsolutePathImpl(), clazzImpl, true, false);
                    }
                }
                Configurator reader = new Configurator();
                reader.writeDaos(daos, xml, insert);
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        }
    }

    /**
	 * Generate a Interface ClassHelper object with informations of the dao passed by parameter
	 * This object will be used to create the .java file of the DAO Interface
	 * 
	 * @param dao - DaoHelper
	 * @return ClassHelper 
	 */
    private static ClassHelper generateInterface(DAOHelper dao) {
        ClassHelper clazzInterface = new ClassHelper();
        clazzInterface.setModifier(Modifier.PUBLIC | Modifier.INTERFACE);
        clazzInterface.setName(dao.getInterfaceName());
        clazzInterface.setPackageName(dao.getPackageName());
        clazzInterface.setExtendsClass(FrameworkUtil.getIDAO());
        clazzInterface.getExtendsClass().setFullGenericName(dao.getPojo().getFullName());
        if (dao.getMethodList() != null && dao.getMethodList().size() > 0) {
            List<MethodHelper> methods = new ArrayList<MethodHelper>();
            for (MethodHelper method : dao.getMethodList()) {
                MethodHelper newMethod = new MethodHelper();
                newMethod.setModifier(method.getModifier());
                newMethod.setName(method.getName());
                newMethod.setParameters(method.getParameters());
                newMethod.setReturnType(method.getReturnType());
                newMethod.setBody(null);
                methods.add(newMethod);
            }
            clazzInterface.setMethods(methods);
        }
        return clazzInterface;
    }

    /**
	 * Generate a Class ClassHelper object with informations of the dao passed by parameter
	 * This object will be used to create the .java file of the DAO Implementation Class
	 * 
	 * @param dao - DaoHelper
	 * @return ClassHelper 
	 */
    private static ClassHelper generateImplementation(DAOHelper dao) {
        ClassHelper clazzImpl = new ClassHelper();
        clazzImpl.setName(dao.getImplementationName());
        clazzImpl.setPackageName(dao.getImplementationPackageName());
        clazzImpl.setInterfaces(new ArrayList<ClassRepresentation>());
        clazzImpl.getInterfaces().add(dao.getDaoInterface());
        if (dao.getType().equals(TYPE_JDBC)) {
            clazzImpl.setExtendsClass(FrameworkUtil.getJDBCGenericDAO());
            if (clazzImpl.getMethods() == null) {
                clazzImpl.setMethods(new ArrayList<MethodHelper>());
                ParameterHelper param = new ParameterHelper();
                param.setType(dao.getPojo());
                MethodHelper exists = new MethodHelper();
                exists.setName("exists");
                ClassRepresentation returnBoolean = new ClassRepresentation();
                returnBoolean.setName("boolean");
                exists.setReturnType(returnBoolean);
                exists.setBody("return false;");
                exists.setParameters(new ArrayList<ParameterHelper>());
                exists.getParameters().add(param);
                MethodHelper insert = new MethodHelper();
                insert.setName("insert");
                ClassRepresentation returnObject = new ClassRepresentation();
                returnObject.setName("Object");
                insert.setReturnType(returnObject);
                insert.setBody("return null;");
                insert.setParameters(new ArrayList<ParameterHelper>());
                insert.getParameters().add(param);
                MethodHelper remove = new MethodHelper();
                remove.setName("remove");
                remove.setParameters(new ArrayList<ParameterHelper>());
                remove.getParameters().add(param);
                MethodHelper update = new MethodHelper();
                update.setName("update");
                update.setParameters(new ArrayList<ParameterHelper>());
                update.getParameters().add(param);
                clazzImpl.getMethods().add(exists);
                clazzImpl.getMethods().add(insert);
                clazzImpl.getMethods().add(remove);
                clazzImpl.getMethods().add(update);
            }
        } else if (dao.getType().equals(TYPE_JPA)) {
            clazzImpl.setExtendsClass(FrameworkUtil.getJPAGenericDAO());
            FieldHelper field = new FieldHelper();
            field.setName("em");
            field.setAnnotation(FrameworkUtil.getInjectionAnnotation());
            field.setType(FrameworkUtil.getEntityManager());
            field.setModifier(Modifier.PRIVATE);
            clazzImpl.addField(field);
        } else {
            clazzImpl.setExtendsClass(FrameworkUtil.getHibernateGenericDAO());
            clazzImpl.addImport(FrameworkUtil.getCriteria());
        }
        clazzImpl.getExtendsClass().setFullGenericName(dao.getPojo().getFullName());
        if (dao.getMethodList() != null && dao.getMethodList().size() > 0) {
            clazzImpl.addMethods(dao.getMethodList());
        }
        return clazzImpl;
    }

    /**
	 * Search for DAOs artifacts, implemented on selected project, and write it on Demoiselle's configuration file. 
	 */
    public void searchDAOsOnProject() {
        List<String> resultDAOs = null;
        List<String> resultBeans = null;
        String varPackage = null;
        List<DAOHelper> daoHelperList = null;
        String varImpl = null;
        varImpl = ".implementation";
        varPackage = EclipseUtil.getPackageFullName(".bean");
        resultBeans = FileUtil.findJavaFiles(varPackage);
        varPackage = EclipseUtil.getPackageFullName("dao");
        resultDAOs = FileUtil.findJavaFiles(varPackage + varImpl);
        if (resultDAOs != null) {
            for (String dao : resultDAOs) {
                if (!dao.contains("package-info")) {
                    dao = dao.replace("/", ".");
                    dao = dao.substring(dao.indexOf(varImpl) + varImpl.length() + 1, dao.lastIndexOf("DAO.java"));
                    for (String bean : resultBeans) {
                        if (bean.contains(dao + ".java")) {
                            bean = bean.replace("/", ".");
                            bean = bean.substring(0, bean.lastIndexOf(".java"));
                            if (daoHelperList == null) {
                                daoHelperList = new ArrayList<DAOHelper>();
                            }
                            DAOHelper daoH;
                            daoH = new DAOHelper();
                            daoH.setPojo(new ClassRepresentation(bean));
                            daoH.setAbsolutePath(EclipseUtil.getSourceLocation() + "/" + varPackage.replace(".", "/") + "/");
                            daoH.setPackageName(varPackage);
                            daoHelperList.add(daoH);
                        }
                    }
                }
            }
        }
        if (daoHelperList != null && !daoHelperList.isEmpty()) {
            Configurator reader = new Configurator();
            reader.writeDaos(daoHelperList, getXml(), false);
        }
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }
}
