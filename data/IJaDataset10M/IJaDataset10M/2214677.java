package mx.ipn.persistencia.dao;

import java.util.ArrayList;
import mx.ipn.to.DescuentoPuestoTO;

public interface DescuentoPuestoDAO {

    public boolean insertDescuentoPuesto(DescuentoPuestoTO descuentoPuestoTO);

    public short updateDescuentoPuesto(DescuentoPuestoTO descuentoPuestoTO, DescuentoPuestoTO descuentoPuestoTONuevo);

    public DescuentoPuestoTO findDescuentoPuestoByDescuentoPuesto(int idDescuento, short idPuesto);

    public ArrayList<DescuentoPuestoTO> selectDescuentoPuesto();

    public DescuentoPuestoTO selectDescuentoPuestoByDescuento(int idDescuento);

    public DescuentoPuestoTO selectDescuentoPuestoByPuesto(short idPuesto);
}
