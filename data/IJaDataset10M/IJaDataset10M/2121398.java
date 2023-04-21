package cat.jm.cru.beans;

import cat.jm.cru.common.Utils;
import cat.jm.cru.exception.CRUException;
import cat.jm.cru.common.Constants;
import cat.jm.cru.common.Templates;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

public class CoreProject extends AbstractProject {

    private List<Pojo> daos = new ArrayList<Pojo>();

    private List<Pojo> services = new ArrayList<Pojo>();

    private List<Pojo> facades = new ArrayList<Pojo>();

    boolean coreGenerated = false;

    public CoreProject() {
        setType(CORE_PROJECT);
    }

    public List<Pojo> getDaos() {
        return daos;
    }

    public List<Pojo> getServices() {
        return services;
    }

    public List<Pojo> getFacades() {
        return facades;
    }

    private void addDao(Pojo dao) {
        daos.add(dao);
    }

    private void addService(Pojo service) {
        services.add(service);
    }

    private void addFacade(Pojo facade) {
        facades.add(facade);
    }

    private void createDaoObjects(Pojo pojo) throws CRUException {
        addDao(createObjects(Constants.DAO, pojo, null));
    }

    private void createServiceObjects(Pojo pojo) throws CRUException {
        addService(createObjects(Constants.SERVICE, pojo, getDaos().get(getDaos().size() - 1)));
    }

    private void createFacadeObjects(Pojo pojo) throws CRUException {
        addFacade(createObjects(Constants.FACADE, pojo, getServices().get(getServices().size() - 1)));
    }

    private void createGenerics() throws CRUException {
        createGenerics(getProject(), Constants.DAO, null);
        createGenerics(getProject(), Constants.SERVICE, getDaos());
        createGenerics(getProject(), Constants.FACADE, getServices());
    }

    private void createCoreSpringConf() throws CRUException {
        createConfFile("spring-" + getProject().getName() + "-core.xml", Templates.CORE_TEMPLATE);
        createConfFile("spring-" + getProject().getName() + "-tx.xml", Templates.TX_TEMPLATE);
        createConfFile("/META-INF/" + getProject().getName() + "-orm.xml", Templates.ORM_TEMPLATE);
        createConfFile("/META-INF/" + getProject().getName() + "-persistence.xml", Templates.PERSISTENCE_TEMPLATE);
    }

    protected void doCore() throws CRUException {
        String outDir = getProject().getDirectory();
        getProject().setDirectory(outDir + File.separatorChar + CORE_PROJECT);
        getProject().addDirectoryToCreate(Constants.DAO);
        getProject().addDirectoryToCreate(Constants.SERVICE);
        getProject().addDirectoryToCreate(Constants.FACADE);
        Utils.createDirectories(getProject());
        readEntities();
        for (Pojo entityName : getProject().getEntities()) {
            createDaoObjects(entityName);
            createServiceObjects(entityName);
            createFacadeObjects(entityName);
        }
        createGenerics();
        createCoreSpringConf();
        coreGenerated = true;
        getProject().setDirectory(outDir);
        getProject().getDirectoriesToCreate().clear();
    }

    public void execute() throws CRUException {
        doCore();
    }
}
