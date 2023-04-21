package watij.elements;

/**
 * Created by IntelliJ IDEA.
 * User:
 * Date: Apr 17, 2006
 * Time: 3:53:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Option extends HtmlElement {

    public void select() throws Exception;

    public boolean selected() throws Exception;

    public void clear() throws Exception;

    public void select(boolean select) throws Exception;
}
