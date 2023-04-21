package pe.com.bn.sach.dao;

import java.util.List;
import pe.com.bn.sach.domain.Bnchf40Tasador;

public interface TasadorDAO {

    public List listTasador() throws Exception;

    public void GuardaTasador(Bnchf40Tasador Bnchf40Tasador) throws Exception;

    public List buscarTasador(Bnchf40Tasador Bnchf40Tasador) throws Exception;

    public Bnchf40Tasador buscarTasadorxId(String idTasador) throws Exception;
}
