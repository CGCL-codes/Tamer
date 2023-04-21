package rubbish.db;

/**
 * SQL
 * 
 * @author $Author: winebarrel $
 * @version $Revision: 1.1 $
 */
public interface SQL {

    public CharSequence getSQL();

    public Object[] getParams();
}
