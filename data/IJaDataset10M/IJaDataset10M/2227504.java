package uipp.ejb;

import java.util.List;
import uipp.support.Tuple;
import com.flexive.shared.content.FxContent;
import com.flexive.shared.content.FxPK;
import com.flexive.shared.exceptions.FxApplicationException;
import com.flexive.shared.security.UserTicket;

/**
 * Bean manipulating with Files and creating new Files
 * 
 * @author Jindrich Basek (basekjin@fel.cvut.cz, CTU Prague, FEE)
 */
public interface FilesLocal {

    /**
     * Returns Files from database according given parameters
     * 
     * @param userTicket ticket with user permissions
     * @param firstRow first selected content instance (data row)
     * @param rowsPerPage number of selected content instances (rows)
     * @param sortField sort field
     * @param sortAscending ascending or descending sorting
     * @param selectedCategories return content of all categories from this list
     * @return list of tuples - left value of tuple is array of objects
     * representing data of one Content instance (each field of array is one 
     * data column) and right value is edit permission for logged user
     * (true - user can edit content instance)
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    public List<Tuple<Object[], Boolean>> getContent(UserTicket userTicket, int firstRow, int rowsPerPage, String sortField, boolean sortAscending, List<String> selectedCategories) throws FxApplicationException;

    /**
     * Returns count of Files of given categories in database
     * 
     * @param selectedCategories list of selected categories
     * @return count of content instances
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    public int getContentCount(List<String> selectedCategories) throws FxApplicationException;

    /**
     * Modify existing File in database
     * 
     * @param editContent new version of existing content to save
     * @param usersRead users with read permission for content instance
     * @param usersWrite users with write permission for content instance
     * @param changePermissions change content permissions?
     * @return saved content instance if succesffull
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    public FxContent saveExistingContent(FxContent editContent, List<String> usersRead, List<String> usersWrite, boolean changePermissions) throws FxApplicationException;

    /**
     * Removes File
     * 
     * @param pk primary key of content instance to remove
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    public void removeContent(FxPK pk) throws FxApplicationException;

    /**
     * Creates new File
     * 
     * @param content content for newly created content instance
     * @return newly created content instance if successfull
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    public FxContent saveContent(FxContent content) throws FxApplicationException;
}
