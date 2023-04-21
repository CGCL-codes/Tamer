package mx.ipn.persistencia.dao;

import java.util.ArrayList;
import mx.ipn.to.DescuentoTO;

public interface DescuentoDAO {

    public boolean insertDescuento(DescuentoTO descuentoTO);

    public short updateDescuento(DescuentoTO descuentoTO);

    public DescuentoTO findDescuentoById(int idDescuento);

    public DescuentoTO findDescuentoByNombreTipoPD(String nombre, int idTipoPD);

    public ArrayList<DescuentoTO> selectDescuento();

    public ArrayList<DescuentoTO> selectDescuentoByNombre(String nombre);

    public DescuentoTO selectDescuentoByTipoPD(int idTipoPD);
}
