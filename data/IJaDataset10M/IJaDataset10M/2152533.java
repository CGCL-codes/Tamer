package es.gva.cit.jmrsid;

/**
 * Clase base para todas las clases que contienen funcionalidades JNI.
 * 
 * @author Nacho Brodin <brodin_ign@gva.es>.<BR> Equipo de desarrollo gvSIG.<BR> http://www.gvsig.gva.es
 * @version 0.0
 * @link http://www.gvsig.gva.es
 */
public class JNIBase {

    protected long cPtr;

    private native int getIndexCountNat(long cPtr);

    private native int initializeNat(long cPtr);

    private native int getNumLevelsNat(long cPtr);

    private native int getWidthNat(long cPtr);

    private native int getHeightNat(long cPtr);

    private native int getStripHeightNat(long cPtr);

    private native int getNumBandsNat(long cPtr);

    private native int getColorSpaceNat(long cPtr);

    private native int getDataTypeNat(long cPtr);

    /**
	 * Funci�n que sirve como base para funcionalidades de mrsid que admiten como par�metro un entero y devuelven un entero.
	 * 
	 * @throws MrSIDException.
	 * @param msg1	Mensaje de error que se muestra cuando el puntero a objeto pasado es vacio.
	 * @param msg2	Mensaje de error que se muestra cuando el resultado de la llamada a la funci�n de gdal es menor o igual que 0.
	 */
    protected int baseSimpleFunction(int n, String msg1, String msg2) throws MrSIDException {
        int res = 0;
        if (cPtr <= 0) throw new MrSIDException(msg1);
        switch(n) {
            case 0:
                res = getIndexCountNat(cPtr);
                break;
            case 1:
                res = initializeNat(cPtr);
                break;
            case 2:
                res = getNumLevelsNat(cPtr);
                break;
            case 3:
                res = getWidthNat(cPtr);
                break;
            case 4:
                res = getHeightNat(cPtr);
                break;
            case 5:
                res = getStripHeightNat(cPtr);
                break;
            case 6:
                res = getNumBandsNat(cPtr);
                break;
            case 7:
                res = getColorSpaceNat(cPtr);
                break;
            case 8:
                res = getDataTypeNat(cPtr);
                break;
        }
        if (res < 0) throw new MrSIDException(msg2); else return res;
    }

    /**
	 * Devuelve el puntero a memoria del objeto en C.
	 */
    public long getPtr() {
        return cPtr;
    }

    /**
	 * Carga de la libreria jmrsid  
	 */
    static {
        System.loadLibrary("jmrsid");
    }
}
