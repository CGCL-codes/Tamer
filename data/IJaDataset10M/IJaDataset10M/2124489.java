package org.tridentproject.repository.api;

import org.tridentproject.repository.fedora.mgmt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.restlet.Context;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.data.Status;
import org.restlet.resource.DomRepresentation;
import org.restlet.resource.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.StringRepresentation;
import org.restlet.resource.Variant;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.dom4j.DocumentHelper;

/**
 * Resource that manages groups.  GET and POST are implemented
 *
 */
public class GroupsResource extends BaseResource {

    private static Logger log = Logger.getLogger(GroupsResource.class);

    public GroupsResource(Context context, Request request, Response response) {
        super(context, request, response);
        getVariants().add(new Variant(MediaType.TEXT_XML));
    }

    /**
     * Returns a representation of a group list.  ListGroups API method.
     */
    @Override
    public Representation getRepresentation(Variant variant) {
        if (!boolAuthorized) {
            challenge();
            return getResponse().getEntity();
        }
        if (MediaType.TEXT_XML.equals(variant.getMediaType())) {
            DomRepresentation representation = null;
            try {
                representation = new DomRepresentation(MediaType.TEXT_XML);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            if (udc == null) {
                Representation rep = SetRepositoryMessage(Status.SERVER_ERROR_INTERNAL, null, "InternalError", "Unable to establish connection to User database", null);
                return rep;
            }
            GroupList grouplist = new GroupList(udc);
            List<Group> groups = null;
            try {
                groups = grouplist.getGroupList();
            } catch (SQLException e) {
                Representation rep = SetRepositoryMessage(Status.SERVER_ERROR_INTERNAL, null, "InternalError", "Error attempting to retrieve group list: " + e.getMessage(), null);
                return rep;
            }
            try {
                Document d = representation.getDocument();
                Element root = d.createElement("groups");
                d.appendChild(root);
                for (Group group : groups) {
                    Element groupElem = d.createElement("group");
                    root.appendChild(groupElem);
                    groupElem.setAttribute("id", group.getGroupName());
                    groupElem.setAttribute("href", strRepoURI + "/groups/" + group.getGroupName());
                }
                d.normalizeDocument();
                return representation;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
