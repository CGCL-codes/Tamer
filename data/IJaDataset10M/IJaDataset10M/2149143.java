package plecotus.models.bwin.doc.treegross;

/** TreeGrOSS : class species defines the drived species information
 *  http://treegross.sourceforge.net
 *   
 *  @version 	1.5  20-Feb-2003 
 *  @author		Juergen Nagel  
 */
class species {

    /** Species Code according to Lower Saxony */
    int code;

    /** age of the dominant trees to calculate the siteindex */
    double h100age;

    /** diameter and height of middle stem and dominant stem; site index age 100 */
    double dg, hg, d100, h100, hbon;

    /** volume, basal area, number of stems per hectare */
    double vol, gha, nha;

    /** volume, basal area, number of stems per hectare of removal & dying that year*/
    double vhaout, ghaout, nhaout;

    /** percentage of basal area, of crown surface of living trees */
    double percBA, percCSA;
}
