package ebadat.dao;

import ebadat.domain.Personal;
import java.util.List;

/**
 *
 * @author Sonia
 */
public interface PersonasDAO {

    int insert(Personal c1);

    int update(Personal c1);

    int delete(Personal c1);

    void close();

    Personal getByName(String string);

    List<Personal> getAll();

    List getByCodigo(int string);

    int delete(int intCodPersona);
}
