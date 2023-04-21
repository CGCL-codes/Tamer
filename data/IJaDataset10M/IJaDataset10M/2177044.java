package pos.domain;

import java.util.List;
import pos.data.ISoDAO;
import pos.data.JDBCSoDAO;

public class SoStore implements ISoDAO {

    JDBCSoDAO dao = new JDBCSoDAO();

    @Override
    public List<SO> recuperarTodosLosSo() {
        return dao.recuperarTodosLosSo();
    }
}
