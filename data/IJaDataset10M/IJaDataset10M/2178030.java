package persistence.mybatis.mappers;

import java.util.List;
import model.Studente;

public interface StudenteMapper {

    public List<Studente> findAll();
}
