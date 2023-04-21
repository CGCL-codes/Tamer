package com.sri.emo.dbobj;

import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.controller.Input;
import com.jcorporate.expresso.core.controller.Output;
import com.jcorporate.expresso.core.controller.Transition;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.DBObject;
import com.sri.emo.commandline.defaults.AbstractNodeExportSupport;
import org.dom4j.Element;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for custom handling of parts, when display and storage of a part
 * is out of the ordinary.  possible to generalize to a customization for
 * relations, but right now is focused on custom owned attributes
 *
 * @author Larry Hamel
 */
public interface IPartHandler extends Cloneable {

    /**
     * <tt>Special Case</tt> for functions that take no parameters as
     * an alternative for passing in null.
     */
    Map NO_PARAMETERS = new HashMap(0);

    /**
     * constant for indicating that default XML should be generated for
     * all custom parts if they have no saved settings
     */
    String FULL_XML = "FULL_XML";

    /**
     * Get the View Transition.
     *
     * @param params map of ControllerRequest params.
     * @return the transition for viewing this part
     * @throws DBException upon error.
     */
    public Transition getViewTransition(Map params) throws DBException;

    /**
     * Get the part 'edit' transition.
     *
     * @param params map of ControllerRequest params.
     * @return the transition for editing this part
     * @throws DBException upon error.
     */
    public Transition getEditTransition(Map params) throws DBException;

    /**
     * Retrieve the view comment as an output.
     *
     * @param params map of ControllerRequest params.
     * @return Output the resulting comment.
     * @throws DBException upon Output construction error.
     */
    public Output getViewComment(Map params) throws DBException;

    /**
     * Add any necessary xml attributes and elements to root for this attribute.
     *
     * @param request ControllerRequest the ControllerRequest object.
     * @param attrib  The attribute value for the part.
     * @param root    The XML Element to add for the part.
     * @throws DBException upon database error.
     */
    public void addXML(Attribute attrib, Element root, ControllerRequest request) throws DBException;

    /**
     * Parse xml into this attribute format.
     *
     * @param request    ControllerRequest the ControllerRequest object.
     * @param allNodeMap ?
     * @param attrib     the specifying format.
     * @param elem       The XML Element we're parsing.
     * @throws DBException upon database error.
     */
    public void parseXML(Element elem, Attribute attrib, Map allNodeMap, ControllerRequest request) throws DBException;

    /**
     * Clone data from existing to clone.
     *
     * @param existing The source attribute.
     * @param clone    The target Attribute.
     * @throws DBException upon database error.
     */
    public void clone(Attribute existing, Attribute clone) throws DBException;

    /**
     * Delete a part.
     *
     * @param attribute Attribute the attribute to delete.
     * @throws DBException upon database error.
     */
    public void delete(Attribute attribute) throws DBException;

    /**
     * Public implementation of the <tt>Clone</tt> object.
     *
     * @return Object
     * @throws CloneNotSupportedException
     * @see java.lang.Object#clone
     */
    public Object clone() throws CloneNotSupportedException;

    /**
     * @return true if this value is correct with respect to menu (category) limitations
     */
    public boolean validate(String key, String value);

    /**
     * given an input, save the value in the custom manner.
     */
    public void saveInput(Input key) throws DBException;

    /**
     * @return an array of SQL Insert statements that are equivalent to the contents of this attribute; can be empty but never null
     */
    public String[] getInsertStatements(AbstractNodeExportSupport support, DBObject attribute) throws DBException;

    /**
     * flag as to whether this attribute is required to make sense of XML.
     * If true,
     * the handler should output a default matrix or whatever even if there has been
     * no saved values from user.
     */
    public boolean isNeededInFullXML();

    /**
     * insert this html into view or edit table
     * @return html string to put into output, or null if no representation available 
     */
    public String getHtml(Attribute attrib, Node src);

    /**
     * Return true unless display is view only (usually can edit)
     * @return true if the custom display is editable, false if it is display-only
     */
    public boolean isEditable();
}
