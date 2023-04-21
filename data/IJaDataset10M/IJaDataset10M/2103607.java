package remesaBancaria.dao;

import java.util.*;
import remesaBancaria.RemesaBancariaParametros;
import remesaBancaria.RemesaBancariaVO;

public interface RemesaBancariaDAO {

    public int insertar(RemesaBancariaVO remesaBancaria);

    public int eliminar(int id);

    public int actualizar(RemesaBancariaVO remesaBancaria);

    public ArrayList<RemesaBancariaVO> listadoRemesasBancarias(RemesaBancariaParametros parametros);
}
