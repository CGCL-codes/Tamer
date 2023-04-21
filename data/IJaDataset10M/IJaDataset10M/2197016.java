package fireteam.print;

/**
 * Created by IntelliJ IDEA.
 * User: Администратор
 * Date: 11.10.2007
 * Time: 16:56:10
 */
public interface SVGPageListener {

    public int getPageCount(SVGReader svg);

    public void setVariables(SVGReader svg, int iCurrentPage);
}
