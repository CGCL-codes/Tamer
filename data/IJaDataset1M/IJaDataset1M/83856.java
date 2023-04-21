package uniriotec.pm.empresa.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import uniriotec.pm.empresa.model.Empregado;

/**
 *
 * @author albertoapr
 */
public interface EmpregadoDao {

    void create(Empregado empregado) throws Exception;

    void remove(String cpf) throws Exception;

    void update(Empregado empregado) throws Exception;

    ArrayList<Empregado> listAll() throws Exception;

    public Empregado searchByCpf(String cpf) throws Exception;

    public Empregado searchById(int empregadoId) throws Exception;
}
