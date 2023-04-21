package net.cmp4oaw.ea_com.repository;

import net.cmp4oaw.ea_com.common.EA_BaseObject;
import net.cmp4oaw.ea_com.common.EA_Collection;
import net.cmp4oaw.ea_com.exception.EA_GetByNameNotSupported;
import net.cmp4oaw.ea_com.exception.EA_InternalError;
import net.cmp4oaw.ea_com.exception.EA_PathNotFoundException;
import net.cmp4oaw.ea_com.factory.EA_ObjectFactory;
import net.cmp4oaw.ea_com.project_interface.EA_Project;
import net.cmp4oaw.ea_com.visitable.EA_RepositoryVisitable;
import net.cmp4oaw.ea_com.visitor.EA_BaseVisitor;
import org.sparx.Connector;
import org.sparx.Diagram;
import org.sparx.Element;
import org.sparx.Method;
import org.sparx.Repository;

public class EA_Repository extends EA_BaseObject implements EA_RepositoryVisitable {

    private Repository rep;

    private static EA_Repository _instance = null;

    private String userName = "";

    private String pwd = "";

    /**
     * This constant marks the first library version of the Version 8.X.
     */
    public static final int EA_LIBRARY_VERSION_8_0 = 855;

    public EA_Collection<EA_Package> models;

    public EA_Collection<EA_ProjectIssue> issues;

    public EA_Collection<EA_Task> tasks;

    public EA_Collection<EA_Term> terms;

    public EA_Collection<EA_Author> authors;

    public EA_Collection<EA_Client> clients;

    public EA_Collection<EA_ProjectResource> resources;

    public static EA_Repository getInstance() {
        if (_instance == null) {
            _instance = new EA_Repository();
        }
        return _instance;
    }

    private EA_Repository() {
        rep = new Repository();
    }

    public void closeModel() {
        if (rep != null) {
            rep.Exit();
            rep = null;
            _instance = null;
        }
    }

    public void reset() {
        setExtObj(null);
        EA_ObjectFactory.clear();
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void openModel(String fileName) {
        if (rep == null) return;
        try {
            if (userName.length() > 0) {
                rep.OpenFile2(fileName, userName, pwd);
            } else {
                rep.OpenFile(fileName);
            }
            rep.ShowWindow(0);
            models = new EA_Collection<EA_Package>(EA_Package.class, rep.GetModels());
            issues = new EA_Collection<EA_ProjectIssue>(EA_ProjectIssue.class, rep.GetIssues());
            terms = new EA_Collection<EA_Term>(EA_Term.class, rep.GetTerms());
            tasks = new EA_Collection<EA_Task>(EA_Task.class, rep.GetTasks());
            clients = new EA_Collection<EA_Client>(EA_Client.class, rep.GetClients());
            authors = new EA_Collection<EA_Author>(EA_Author.class, rep.GetAuthors());
            resources = new EA_Collection<EA_ProjectResource>(EA_ProjectResource.class, rep.GetResources());
        } catch (Exception ex) {
            throw new EA_InternalError("Error in EA_Repository.openModel() [" + fileName + "]: " + ex);
        }
    }

    public EA_Package findPackageByPath(String path) throws EA_PathNotFoundException {
        if (path.length() == 0) return null;
        int pos = path.indexOf('/');
        String str = pos > 0 ? path.substring(0, pos) : path;
        String _path = pos > 0 ? path.substring(pos + 1) : "";
        try {
            EA_Package pkg = (EA_Package) models.getByName(str);
            if (_path.length() > 0) {
                pkg = pkg.findPackageByPath(_path);
            }
            return pkg;
        } catch (EA_GetByNameNotSupported ex) {
            throw new EA_InternalError("Internal Error in EA_Package.findPackgeByPath(): " + ex);
        }
    }

    public EA_Project getProjectInterface() {
        return new EA_Project(rep.GetProjectInterface());
    }

    public Element getElementById(int id) {
        return rep.GetElementByID(id);
    }

    public org.sparx.Package getPackageById(int id) {
        return rep.GetPackageByID(id);
    }

    public Diagram getDiagramById(int id) {
        return rep.GetDiagramByID(id);
    }

    public Connector getConnectorById(int id) {
        return rep.GetConnectorByID(id);
    }

    public Method getMethodById(int id) {
        return rep.GetMethodByID(id);
    }

    public Element getElementByGUID(String guid) {
        return rep.GetElementByGuid(guid);
    }

    public int getLibraryVersion() {
        return rep.GetLibraryVersion();
    }

    public boolean isVersionOrHigher(int referenceVersion) {
        return getLibraryVersion() >= referenceVersion;
    }

    /**
     * @see net.cmp4oaw.ea_com.common.EA_BaseObject#accept(net.cmp4oaw.ea_com.visitor.EA_BaseVisitor)
     */
    public void accept(EA_BaseVisitor iter) {
        iter.getVisitor(this).visit(this);
    }

    public String SQLQuery(String query) {
        return rep.SQLQuery(query);
    }
}
