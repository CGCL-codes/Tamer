package utilidad.dao;

import utilidad.vo.ItemComboVO;
import java.util.*;

public interface UtilidadDAO {

    public String getFormatoFechaCampoBD(String campoBD);

    public String getFormatoFecha(java.util.Date feha);

    public String getFormatoFecha_Hora(java.util.Date feha);

    public String getFormatoDecimal(Float numero);

    public int siguienteNumero(String nombreTabla, String serie, int idCentroGestion);

    public int siguienteNumeroLibre(String nombreCampo, String nombreTabla);

    public boolean esCodigoUnico(int codigo, int id, String tabla);

    public java.util.Date getFechaBD();

    public ArrayList<ItemComboVO> listadoCamposLVAL(String tipo);

    public int actualizarRegistroLVAL(String tipo, String valor, int posicion);
}
