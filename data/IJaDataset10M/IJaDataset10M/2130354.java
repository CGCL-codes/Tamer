package securus.action.account;

import java.util.LinkedList;
import java.util.List;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.log.Log;

@Name("bulkUploadsBean")
@Scope(ScopeType.CONVERSATION)
public class BulkUploadsBean {

    @Logger
    private Log log;

    private List<BulkUpload> bulkUploads;

    public BulkUploadsBean() {
        bulkUploads = new LinkedList<BulkUpload>();
        bulkUploads.add(new BulkUpload("package1", "The first bulk upload packege"));
        bulkUploads.add(new BulkUpload("package2", "The second bulk upload packege"));
        bulkUploads.add(new BulkUpload("package3", "The third bulk upload packege"));
    }

    public List<BulkUpload> getBulkUploads() {
        return bulkUploads;
    }

    public void setBulkUploads(List<BulkUpload> bulkUploads) {
        this.bulkUploads = bulkUploads;
    }

    public static class BulkUpload {

        private String name;

        private String description;

        private BulkUpload(String name, String description) {
            super();
            this.name = name;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
