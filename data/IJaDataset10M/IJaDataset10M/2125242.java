package org.melati.poem.test;

import org.melati.poem.test.generated.EverythingNormalTableBase;
import org.melati.poem.DefinitionSource;
import org.melati.poem.Database;
import org.melati.poem.PoemException;

/**
 * Melati POEM generated, programmer modifiable stub 
 * for a <code>EverythingNormalTable</code> object.
 * <p>
 * Description: 
 *   Every datatype in one table. 
 * </p>
 *
 * 
 * <table> 
 * <tr><th colspan='3'>
 * Field summary for SQL table <code>EverythingNormal</code>
 * </th></tr>
 * <tr><th>Name</th><th>Type</th><th>Description</th></tr>
 * <tr><td> id </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> binaryfield </td><td> byte[] </td><td> &nbsp; </td></tr> 
 * <tr><td> stringfield </td><td> String </td><td> &nbsp; </td></tr> 
 * <tr><td> passwordfield </td><td> String </td><td> &nbsp; </td></tr> 
 * <tr><td> booleanfield </td><td> Boolean </td><td> &nbsp; </td></tr> 
 * <tr><td> datefield </td><td> Date </td><td> &nbsp; </td></tr> 
 * <tr><td> doublefield </td><td> Double </td><td> &nbsp; </td></tr> 
 * <tr><td> integerfield </td><td> Integer </td><td> &nbsp; </td></tr> 
 * <tr><td> longfield </td><td> Long </td><td> &nbsp; </td></tr> 
 * <tr><td> bigdecimalfield </td><td> BigDecimal </td><td> &nbsp; </td></tr> 
 * <tr><td> timestampfield </td><td> Timestamp </td><td> &nbsp; </td></tr> 
 * </table> 
 * 
 * @generator  org.melati.poem.prepro.TableDef#generateTableMainJava 
 */
public class EverythingNormalTable extends EverythingNormalTableBase {

    /**
  * Constructor.
  * 
  * @generator org.melati.poem.prepro.TableDef#generateTableMainJava 
  * @param database          the POEM database we are using
  * @param name              the name of this <code>Table</code>
  * @param definitionSource  which definition is being used
  * @throws PoemException    if anything goes wrong
  */
    public EverythingNormalTable(Database database, String name, DefinitionSource definitionSource) throws PoemException {
        super(database, name, definitionSource);
    }
}
