package com.bcurtu.amigo.db;

import java.util.List;
import com.bcurtu.amigo.pojo.Amigo;
import com.bcurtu.amigo.pojo.Grupo;

public interface GrupoDao {

    public Integer save(Grupo group) throws Exception;

    public void save(Amigo amigo) throws Exception;

    public Grupo getGroup(Integer id) throws Exception;

    public List<Amigo> getAmigos(Integer grupo) throws Exception;

    public Amigo getAmigo(Integer amigoId) throws Exception;
}
