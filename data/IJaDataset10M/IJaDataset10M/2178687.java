package uk.ac.ed.rapid.portlets;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.portlet.ActionRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import uk.ac.ed.rapid.exception.RapidException;

/**
 * Parses a request and retrieves all variables and file uploads
 * @author jos
 */
public class RequestParser {

    private HashMap<String, Vector<String>> valueStore = new HashMap<String, Vector<String>>();

    private HashMap<String, FileItem> fileItemStore = new HashMap<String, FileItem>();

    /**
    * Gets all names of fields in the form
    * @return a set of all the names of all fields, except file uploads
    */
    public Set<String> getFormNames() {
        return valueStore.keySet();
    }

    /**
    * Gets all a list of all values for a certain field name. Ignores file upload fields.
    * @param formName name of a field in the form
    * @return list of values of the field
    */
    public List<String> getFormValueList(String formName) {
        return this.valueStore.get(formName);
    }

    /**
    * Gets one value for the name of a field. Ignores file upload fields.
    * @param formName name of the field.
    * @return value of the field. If the field is multivalued, the first value is returned.
    */
    public String getFormValue(String formName) {
        List<String> valueList = this.valueStore.get(formName);
        return ((valueList == null || valueList.size() == 0) ? null : valueList.get(0));
    }

    /**
    * Gets the names of any file upload elements
    * @return set of names of file uploads
    */
    public Set<String> getFileItemNames() {
        return this.fileItemStore.keySet();
    }

    /**
    * Gets all file uploads in the form.
    * @return Collection of all file uploads
    */
    public Collection<FileItem> getFileItemCollection() {
        return this.fileItemStore.values();
    }

    /**
    * Gets a file for the name of a file upload
    * @param fileItemName name of the file upload.
    * @return File that was uploaded.
    */
    public FileItem getFileItem(String fileItemName) {
        return this.fileItemStore.get(fileItemName);
    }

    /**
    * parses a request and retrieves all fields and files
    * @param request request to be parsed
    * @throws uk.ac.ed.rapid.data.RapidException An error occurred during parsing
    */
    @SuppressWarnings("unchecked")
    public void parse(ActionRequest request) throws RapidException {
        this.fileItemStore.clear();
        this.valueStore.clear();
        if (!PortletFileUpload.isMultipartContent(request)) {
            Enumeration<String> parameterEnum = request.getParameterNames();
            while (parameterEnum.hasMoreElements()) {
                String parameterName = parameterEnum.nextElement();
                String[] parameterValues = request.getParameterValues(parameterName);
                Vector<String> parameterVector = new Vector<String>();
                for (String parameterElement : parameterValues) parameterVector.add(parameterElement);
                this.valueStore.put(parameterName, parameterVector);
            }
        } else {
            FileItemFactory factory = new DiskFileItemFactory();
            PortletFileUpload upload = new PortletFileUpload(factory);
            try {
                List<FileItem> itemList = (List<FileItem>) upload.parseRequest(request);
                for (FileItem item : itemList) {
                    String name = item.getFieldName();
                    if (item.isFormField()) {
                        String value = item.getString();
                        Vector<String> entry = valueStore.get(name);
                        if (entry == null) {
                            entry = new Vector<String>();
                            valueStore.put(name, entry);
                        }
                        entry.add(value);
                    } else {
                        fileItemStore.put(name, item);
                    }
                }
            } catch (FileUploadException ex) {
                throw new RapidException("Error parsing request. File Upload Exception: " + ex.getMessage());
            }
        }
    }
}
