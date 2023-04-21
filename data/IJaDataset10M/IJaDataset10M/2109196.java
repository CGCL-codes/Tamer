package ar.com.arkios.kfcon4d.dao;

import ar.com.arkios.kfcon4d.util.RegisterBuilder;
import ar.com.arkios.kfconmodelo.modelo.Orden;
import ar.com.arkios.j4d.opDataArray;
import java.util.List;
import org.apache.log4j.Logger;

public class OrdenDAO extends GenericOperDAO<Orden> {

    private static final Logger miLogger = Logger.getLogger(OrdenDAO.class);

    /** Nombre de la tabla OrdenesSAP */
    private static final String ORDENESSAP = "OrdenesSAP";

    private static final String ORDENESSAP_ORDEN = "Orden";

    private static final String ORDENESSAP_MATERIAL = "Material";

    private static final String ORDENESSAP_ESPECIE = "Especie";

    private static final String ORDENESSAP_VARIEDAD = "Variedad";

    private static final String ORDENESSAP_EMBALAJE = "Embalaje";

    private static final String ORDENESSAP_ENVASE = "Envase";

    private static final String ORDENESSAP_MARCA = "Marca";

    private static final String ORDENESSAP_CALIDAD = "Calidad";

    private static final String ORDENESSAP_CANTIDAD = "Cantidad";

    private static final String ORDENESSAP_CAJASXPALLET = "Cajas_x_Pallet";

    private static final String ORDENESSAP_FECHA_INI = "FechaInicio";

    private static final String ORDENESSAP_FECHA_FIN = "FechaFin";

    private static final String ORDENESSAP_ACTIVO = "Activa";

    /** Nombres de las columnas a ser utilizadas en el alta de la tabla OrdenesSAP. */
    private static final String[] ORDENESSAP_COLUMNAS = { ORDENESSAP_ORDEN, ORDENESSAP_MATERIAL, ORDENESSAP_ESPECIE, ORDENESSAP_VARIEDAD, ORDENESSAP_EMBALAJE, ORDENESSAP_ENVASE, ORDENESSAP_MARCA, ORDENESSAP_CALIDAD, ORDENESSAP_CANTIDAD, ORDENESSAP_CAJASXPALLET, ORDENESSAP_FECHA_INI, ORDENESSAP_FECHA_FIN, ORDENESSAP_ACTIVO };

    /** Nombres de las columnas a ser utilizada como PK de OrdenesSAP. */
    private static final String[] ORDENESSAP_PK = { ORDENESSAP_ORDEN };

    /** Nombres de las columnas a modificar en reescritura. */
    private static final String[] ORDENESSAP_MOD = { ORDENESSAP_ORDEN, ORDENESSAP_ACTIVO };

    @Override
    protected String getEntidadTableName() {
        return ORDENESSAP;
    }

    @Override
    protected String getPKProperty() {
        return "miCodigo";
    }

    @Override
    protected String getActiveFieldName() {
        return ORDENESSAP_ACTIVO;
    }

    @Override
    protected opDataArray[] mapListEntidadTo4D(List<Orden> laLista) {
        opDataArray[] lasOrdenesSAP = new opDataArray[getMiEntidadFields().mSize];
        for (int i = 0; i < lasOrdenesSAP.length; i++) {
            lasOrdenesSAP[i] = new opDataArray(laLista.size());
        }
        int i = 0;
        for (Orden orden : laLista) {
            RegisterBuilder ordenRegisterBuilder = new RegisterBuilder(getMiEntidadFields());
            ordenRegisterBuilder.setData(ORDENESSAP_ORDEN, orden.getMiCodigo());
            ordenRegisterBuilder.setData(ORDENESSAP_MATERIAL, orden.getMiMaterialCod());
            ordenRegisterBuilder.setData(ORDENESSAP_ESPECIE, orden.getMiEspecieCod());
            ordenRegisterBuilder.setData(ORDENESSAP_VARIEDAD, orden.getMiVariedadCod());
            ordenRegisterBuilder.setData(ORDENESSAP_EMBALAJE, orden.getMiEmbalajeCod());
            ordenRegisterBuilder.setData(ORDENESSAP_ENVASE, orden.getMiEnvaseCod());
            ordenRegisterBuilder.setData(ORDENESSAP_MARCA, orden.getMiMarcaCod());
            ordenRegisterBuilder.setData(ORDENESSAP_CALIDAD, orden.getMiCalidadCod());
            ordenRegisterBuilder.setData(ORDENESSAP_CAJASXPALLET, orden.getMisCajasPorPallet());
            ordenRegisterBuilder.setData(ORDENESSAP_FECHA_INI, orden.getMiFechaInicio());
            ordenRegisterBuilder.setData(ORDENESSAP_FECHA_FIN, orden.getMiFechaFin());
            ordenRegisterBuilder.setData(ORDENESSAP_ACTIVO, true);
            opDataArray dataAux = ordenRegisterBuilder.getDataArray();
            for (int j = 0; j < getMiEntidadFields().mSize; j++) {
                lasOrdenesSAP[j].mDataArray[i] = dataAux.mDataArray[j];
            }
            i += 1;
        }
        return lasOrdenesSAP;
    }

    @Override
    protected opDataArray[] mapListEntidadModTo4D(List<Orden> laLista) {
        opDataArray[] lasOrdenesSAP = new opDataArray[getMiEntidadModFields().mSize];
        for (int i = 0; i < lasOrdenesSAP.length; i++) {
            lasOrdenesSAP[i] = new opDataArray(laLista.size());
        }
        int i = 0;
        for (Orden orden : laLista) {
            RegisterBuilder ordenRegisterBuilder = new RegisterBuilder(getMiEntidadModFields());
            ordenRegisterBuilder.setData(ORDENESSAP_ORDEN, orden.getMiCodigo());
            ordenRegisterBuilder.setData(ORDENESSAP_ACTIVO, true);
            opDataArray dataAux = ordenRegisterBuilder.getDataArray();
            for (int j = 0; j < getMiEntidadModFields().mSize; j++) {
                lasOrdenesSAP[j].mDataArray[i] = dataAux.mDataArray[j];
            }
            i += 1;
        }
        return lasOrdenesSAP;
    }

    @Override
    protected String[] getEntidadFieldsNames() {
        return ORDENESSAP_COLUMNAS;
    }

    @Override
    protected String[] getEntidadPKFieldsNames() {
        return ORDENESSAP_PK;
    }

    @Override
    protected String[] getEntidadModFieldsNames() {
        return ORDENESSAP_MOD;
    }
}
