package br.com.petrobras.persistence.ibatis;

import java.util.LinkedHashMap;
import java.util.List;
import br.com.petrobras.model.GrupoVO;
import br.com.petrobras.persistence.Cleanup;
import br.com.petrobras.persistence.ColetarDatabaseTestCase;
import br.com.petrobras.persistence.GrupoDAO;

public class IbatisGrupoDAOImplTest extends ColetarDatabaseTestCase {

    public void testFindAll() throws Exception {
        System.out.println("findAll");
        GrupoDAO instance = new IbatisGrupoDAOImpl();
        GrupoVO grupo = null;
        List<GrupoVO> result = instance.findAll(grupo);
        assertEquals(1, result.size());
    }

    @Override
    protected LinkedHashMap<String, Cleanup> getDirtyTables() {
        return null;
    }
}
