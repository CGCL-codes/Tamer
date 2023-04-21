package geomss.geom.reader.iges;

import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import geomss.geom.GeomElement;
import geomss.geom.Transformable;
import geomss.geom.GTransform;
import geomss.geom.GeometryList;
import geomss.geom.GeomList;

/**
* <b><i>ASSOCIATIVITY ENTITY</i></b> - This entity defines an associativity
* relationship between other entities.
*
*  <p>  Modified by:  Joseph A. Huwaldt   </p>
*
* @author JDN, AED, Version 1.0
* @version September 6, 2010
*/
public abstract class Entity402 extends GeomSSEntity {

    protected List<Integer> pointers = new ArrayList();

    protected GeometryList geom = GeomList.newInstance();

    /**
	* Default constructor.
	*
	* @param p part to which this entity is contained
	* @param de Directory Entry for this entity
	*/
    public Entity402(Part p, DirEntry de) {
        super(p, de);
    }

    /**
	* Checks to see if the entity is correct.  No restrictions are imposed.
	*/
    public void check() {
    }

    /**
	* Read the Parameter Data from the String read in by the superclass.
	*
	* @param in input file
	*/
    public void read(RandomAccessFile in) throws IOException {
        super.read(in);
        String s = getPDString();
        if (Constants.DEBUG) {
            System.out.println("PD String = \"" + s + "\"");
        }
        int n = getInt(s);
        for (int i = 0; i < n; ++i) pointers.add(getInt(s));
        super.read_additional();
    }

    /**
	*  The GeomSS geometry element is created from the IGES parameters when this method is called.
	**/
    void createGeometry() {
        Part part = getPart();
        int n = pointers.size();
        for (int i = 0; i < n; ++i) {
            int deNum = pointers.get(i);
            Entity entity = part.getEntity(deNum);
            if (entity instanceof GeomSSEntity) {
                GeomSSEntity geomEntity = (GeomSSEntity) entity;
                geomEntity.setUsedInList(true);
                GeomElement element = geomEntity.getGeomElement(GTransform.IDENTITY);
                geom.add(element);
            }
        }
    }

    /**
	*  Return a reference to the Transformable GeomElement contained in this IGES Entity.
	**/
    protected Transformable getGeomElement() {
        return geom;
    }

    /**
	* Dump to String.
	*
	* @return String containing the resulting text.
	*/
    public String toString() {
        StringBuffer outStr = new StringBuffer(super.toString());
        outStr.append("\n");
        int n = pointers.size();
        outStr.append("n  = ");
        outStr.append(n);
        outStr.append("\n");
        for (int i = 0; i < n; i++) {
            outStr.append("entity(");
            outStr.append(i);
            outStr.append(") = ");
            outStr.append(pointers.get(i));
            outStr.append("\n");
        }
        return outStr.toString();
    }
}
