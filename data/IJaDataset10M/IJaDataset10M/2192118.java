package org.gvsig.jogr;

/** 
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */
public class OGRFeatureDefn extends JNIBase {

    private native String getNameNat(long cPtr);

    private native int getGeomTypeNat(long cPtr);

    private native void FreeOGRFeatureDefnNat(long cPtr);

    private native long getFieldDefnNat(long cPtr, int i);

    private native int getFieldIndexNat(long cPtr, String indice);

    private native void addFieldDefnNat(long cPtr, long fd);

    private native void setGeomTypeNat(long cPtr, String gtype);

    private native long cloneFeatureDefnNat(long cPtr);

    private static native long createFeatureDefnNat(String pszName);

    private static native void destroyFeatureDefnNat(long fd);

    /**
	 * Constructor
	 * @param cPtr	direcci�n de memoria al objeto OGRFeatureDefn de C. 
	 */
    public OGRFeatureDefn(long cPtr) {
        this.cPtr = cPtr;
    }

    /**
	 * Obtiene el nombre de la feature
	 * @throws OGRException
	 * @return Nombre de la feature
	 */
    public String getName() throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en getName(). El constructor ha fallado.");
        String name = getNameNat(cPtr);
        if (name == null) throw new OGRException("Error en getName(). No se ha podido obtener el nombre.");
        return name;
    }

    /**
	 * Obtiene el tipo de geometria
	 * @throws OGRException
	 * @return Tipo de geometria 
	 */
    public String getGeomType() throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en getGeomType(). El constructor ha fallado.");
        int tipo = getGeomTypeNat(cPtr);
        if (tipo < 0 || tipo > 16) throw new OGRException("Error en getGeomType(). No se ha podido obtener un tipo de geometria valido.");
        String tipogeom = null;
        switch(tipo) {
            case 0:
                tipogeom = "wkbUnknown";
                break;
            case 1:
                tipogeom = "wkbPoint";
                break;
            case 2:
                tipogeom = "wkbLineString";
                break;
            case 3:
                tipogeom = "wkbPolygon";
                break;
            case 4:
                tipogeom = "wkbMultiPoint";
                break;
            case 5:
                tipogeom = "wkbMultiLineString";
                break;
            case 6:
                tipogeom = "wkbMultiPolygon";
                break;
            case 7:
                tipogeom = "wkbGeometryCollection";
                break;
            case 8:
                tipogeom = "wkbNone";
                break;
            case 9:
                tipogeom = "wkbLinearRing";
                break;
            case 10:
                tipogeom = "wkbPoint25D";
                break;
            case 11:
                tipogeom = "wkbLineString25D";
                break;
            case 12:
                tipogeom = "wkbPolygon25D";
                break;
            case 13:
                tipogeom = "wkbMultiPoint25D";
                break;
            case 14:
                tipogeom = "wkbMultiLineString25D";
                break;
            case 15:
                tipogeom = "wkbMultiPolygon25D";
                break;
            case 16:
                tipogeom = "wkbGeometryCollection25D";
                break;
        }
        return tipogeom;
    }

    /**
	 * Obtiene el n�mero de campos
	 * @throws OGRException
	 * @return N�mero de campos
	 */
    public int getFieldCount() throws OGRException {
        String msg1 = "Error en getFieldCount. El constructor ha fallado.";
        String msg2 = "Error obteniendo el n�mero de campos";
        return baseSimpleFunctions(3, msg1, msg2);
    }

    /**
	 * Obtiene el campo referenciado por el �ndice
	 * @throws OGRException
	 * @return Objeto que contiene el campo seleccionado
	 */
    public OGRFieldDefn getFieldDefn(int i) throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en getFieldDefn(). El constructor ha fallado.");
        long id = getFieldDefnNat(cPtr, i);
        if (id == 0) throw new OGRException("Error en getGeomType(). No se ha podido obtener un tipo de geometria valido.");
        OGRFieldDefn field = new OGRFieldDefn(id);
        return field;
    }

    /**
	 * Destructor 
	 */
    protected void finalize() throws OGRFailureException {
        if (cPtr == 0) throw new OGRFailureException("Fallo al acceder al dato.");
        FreeOGRFeatureDefnNat(cPtr);
    }

    /**
	 * 
	 */
    public int getFieldIndex(String field) throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en getFieldIndex(). El constructor ha fallado.");
        if (field == null) throw new OGRException("Error en getFieldIndex(). Par�metro invalido.");
        int index = getFieldIndexNat(cPtr, field);
        if (index == 0) throw new OGRException("Error en getFieldIndex(). No se ha podido obtener un �ndice valido.");
        return index;
    }

    /**
	 * 
	 */
    public void addFieldDefn(OGRFieldDefn fd) throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en addFieldDefn(). El constructor ha fallado.");
        if (fd == null || fd.getPtro() <= 0) throw new OGRException("Error en addFieldDefn(). La referencia del par�metro OGRFieldDefn no es valida.");
        addFieldDefnNat(cPtr, fd.getPtro());
    }

    /**
	 * 
	 */
    public void setGeomType(String gtype) throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en setGeomType(). El constructor ha fallado.");
        boolean ok = false;
        if (gtype != null && (gtype.equals("wkbUnknown") || gtype.equals("wkbPoint") || gtype.equals("wkbLineString") || gtype.equals("wkbPolygon") || gtype.equals("wkbMultiPoint") || gtype.equals("wkbMultiLineString") || gtype.equals("wkbMultiPolygon") || gtype.equals("wkbGeometryCollection") || gtype.equals("wkbNone") || gtype.equals("wkbLinearRing") || gtype.equals("wkbPoint25D") || gtype.equals("wkbLineString25D") || gtype.equals("wkbPolygon25D") || gtype.equals("wkbMultiPoint25D") || gtype.equals("wkbMultiLineString25D") || gtype.equals("wkbMultiPolygon25D") || gtype.equals("wkbGeometryCollection25D"))) ok = true;
        if (!ok) throw new OGRException("Error en setGeomType(). Par�metro invalido");
        setGeomTypeNat(cPtr, gtype);
    }

    /**
	 * 
	 */
    public OGRFeatureDefn cloneFeatureDefn() throws OGRException {
        if (cPtr == 0) throw new OGRException("Error en cloneFeatureDefn(). El constructor ha fallado.");
        long ptr_fd = cloneFeatureDefnNat(cPtr);
        if (ptr_fd == 0) throw new OGRException("Error en cloneFeatureDefn(). No se ha podido obtener un valor de referencia valido para OGRFeatureDefn.");
        return new OGRFeatureDefn(ptr_fd);
    }

    /**
	 * 
	 */
    public int reference() throws OGRException {
        String msg1 = "Error en reference(). El constructor no tuvo exito.";
        String msg2 = "Error en reference().";
        return baseSimpleFunctions(10, msg1, msg2);
    }

    /**
	 * 
	 */
    public int dereference() throws OGRException {
        String msg1 = "Error en dereference(). El constructor no tuvo exito.";
        String msg2 = "Error en dereference().";
        return baseSimpleFunctions(11, msg1, msg2);
    }

    /**
	 * 
	 */
    public int getReferenceCount() throws OGRException {
        String msg1 = "Error en getReferenceCount(). El constructor no tuvo exito.";
        String msg2 = "Error en getReferenceCount(). No se ha obtenido un n�mero de referencias valido.";
        return baseSimpleFunctions(12, msg1, msg2);
    }

    /**
	 * 
	 */
    public static OGRFeatureDefn createFeatureDefn(String pszName) throws OGRException {
        if (pszName == null) throw new OGRException("Error en createFeatureDefn(). Par�metro invalido.");
        long ptr_fd = createFeatureDefnNat(pszName);
        if (ptr_fd == 0) throw new OGRException("Error en cloneFeatureDefn(). No se ha podido obtener un valor de referencia valido para OGRFeatureDefn.");
        return new OGRFeatureDefn(ptr_fd);
    }

    /**
	 * 
	 */
    public static void destroyFeatureDefn(OGRFeatureDefn fd) throws OGRException {
        if (fd == null || fd.getPtro() == 0) throw new OGRException("Error en destroyFeatureDefn(). Par�metro invalido.");
        destroyFeatureDefnNat(fd.getPtro());
    }
}
