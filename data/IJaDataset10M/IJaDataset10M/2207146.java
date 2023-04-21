package com.museum4j.dao;

import java.util.List;
import com.museum4j.modelo.*;

public interface IdiomaDAO {

    public List getIdiomas();

    Idioma getIdioma(String codigo);
}
