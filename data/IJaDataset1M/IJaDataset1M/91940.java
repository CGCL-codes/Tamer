package org.endeavour.mgmt.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.endeavour.mgmt.model.persistence.PersistenceManager;
import org.endeavour.mgmt.view.IViewConstants;

public class Document implements Comparable<Document> {

    private Integer id = null;

    private String description = null;

    private String fileName = null;

    private Project project = null;

    private List<Version> versions = null;

    private Set<WorkProduct> workProducts = null;

    public static final String ID = "ID";

    public static final String DESCRIPTION = "DESCRIPTION";

    public static final String FILE_NAME = "FILE_NAME";

    public static final String VERSION = "VERSION";

    public static final String FILE = "FILE";

    public static final String VERSIONS = "VERSIONS";

    public Document() {
    }

    public Document(Project aProject) {
        this.project = aProject;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return this.description;
    }

    public Project getProject() {
        return this.project;
    }

    public void setId(Integer aId) {
        this.id = aId;
    }

    public void setDescription(String aName) {
        this.description = aName;
    }

    public void setProject(Project aProject) {
        this.project = aProject;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String aType) {
        this.fileName = aType;
    }

    public Set<WorkProduct> getWorkProducts() {
        if (this.workProducts == null) {
            this.workProducts = new HashSet<WorkProduct>();
        }
        return this.workProducts;
    }

    public void setWorkProducts(Set<WorkProduct> aWorkProducts) {
        this.workProducts = aWorkProducts;
    }

    public Map<String, Object> getData() {
        Map<String, Object> theData = new HashMap<String, Object>();
        theData.put(ID, this.getId());
        theData.put(DESCRIPTION, this.getDescription());
        theData.put(FILE_NAME, this.getFileName());
        theData.put(VERSIONS, this.getVersions());
        return theData;
    }

    public void delete() {
        this.getProject().removeDocument(this);
        for (WorkProduct theWorkProduct : this.getWorkProducts()) {
            theWorkProduct.removeDocument(this);
        }
        PersistenceManager.getInstance().delete(this);
    }

    public void save(Map<String, Object> aData) {
        if (this.getId() == null) {
            Integer theId = (Integer) PersistenceManager.getInstance().findBy("select max(document.id) from " + Document.class.getSimpleName() + " document");
            if (theId == null) {
                theId = 0;
            } else {
                theId++;
            }
            this.setId(theId);
        }
        String theDescription = (String) aData.get(DESCRIPTION);
        if (theDescription != null) {
            this.setDescription(theDescription);
        }
        byte[] theFile = (byte[]) aData.get(FILE);
        if (theFile != null) {
            Version theVersion = new Version(this);
            theVersion.update(theFile);
            this.addVersion(theVersion);
            String theFileName = (String) aData.get(FILE_NAME);
            this.setFileName(theFileName);
        }
        this.getProject().addDocument(this);
    }

    public void addVersion(Version aVersion) {
        List<Version> theVersions = this.getVersions();
        if (!theVersions.contains(aVersion)) {
            theVersions.add(aVersion);
        }
    }

    public List<String> validate(Map<String, Object> aData) {
        List<String> theErrors = new ArrayList<String>();
        String theDescription = (String) aData.get(DESCRIPTION);
        if (theDescription == null || theDescription.trim().length() == 0) {
            theErrors.add(IViewConstants.RB.getString("description_not_empty.msg"));
        }
        byte[] theFile = (byte[]) aData.get(FILE);
        if (theFile == null && !this.hasVersions()) {
            theErrors.add(IViewConstants.RB.getString("file_not_empty.msg"));
        }
        return theErrors;
    }

    public boolean hasVersions() {
        return this.getVersions().size() > 0;
    }

    public List<Version> getVersions() {
        if (this.versions == null) {
            this.versions = new ArrayList<Version>();
        }
        return this.versions;
    }

    public void setVersions(List<Version> aVersions) {
        this.versions = aVersions;
    }

    public Version retrieveVersionBy(Integer aVersionNumber) {
        Version theVersion = null;
        for (Version theCurrentVersion : this.getVersions()) {
            if (theCurrentVersion != null) {
                if (theCurrentVersion.getNumber().equals(aVersionNumber)) {
                    theVersion = theCurrentVersion;
                    break;
                }
            }
        }
        return theVersion;
    }

    public boolean isAssigned(WorkProduct aWorkProduct) {
        boolean isAssigned = false;
        for (Document theDocument : aWorkProduct.getDocuments()) {
            if (this.equals(theDocument)) {
                isAssigned = true;
                break;
            }
        }
        return isAssigned;
    }

    public void addWorkProduct(WorkProduct aWorkProduct) {
        Set<WorkProduct> theWorkProducts = this.getWorkProducts();
        if (!theWorkProducts.contains(aWorkProduct)) {
            theWorkProducts.add(aWorkProduct);
        }
    }

    public void removeWorkProduct(WorkProduct aWorkProduct) {
        Set<WorkProduct> theWorkProducts = this.getWorkProducts();
        if (theWorkProducts.contains(aWorkProduct)) {
            theWorkProducts.remove(aWorkProduct);
        }
    }

    public static List<Document> getUnassignedDocumentsDataFor(Object aDocumentAssignee, Project aProject) {
        List<Document> theUnAssignedDocuments = new ArrayList<Document>();
        for (Document theDocument : aProject.getDocuments()) {
            if (theDocument != null) {
                boolean isAssigned = aDocumentAssignee == null ? false : theDocument.isAssigned((WorkProduct) aDocumentAssignee);
                if (!isAssigned) {
                    theUnAssignedDocuments.add(theDocument);
                }
            }
            theDocument = null;
        }
        return theUnAssignedDocuments;
    }

    public Version getCurrentVersion() {
        Version theCurrentVersion = null;
        for (Version theVersion : this.getVersions()) {
            if (theVersion != null) {
                theCurrentVersion = theVersion;
                break;
            }
        }
        return theCurrentVersion;
    }

    public boolean equals(Object anObj) {
        boolean isEquals = false;
        if (anObj != null && anObj instanceof Document) {
            Document theDocument = (Document) anObj;
            if (this.getId() != null) {
                isEquals = this.getId().equals(theDocument.getId());
            }
        }
        return isEquals;
    }

    public int compareTo(Document aDocument) {
        int theResult = -1;
        if (aDocument != null) {
            theResult = this.getFileName().compareTo(aDocument.getFileName());
        }
        return theResult;
    }
}
