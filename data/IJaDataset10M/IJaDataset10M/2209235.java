package bahrie.tokobukuapp.interfaces;

import bahrie.tokobukuapp.entity.Penjualan;
import java.sql.SQLException;

/**
 *
 * @author bahrie
 */
public interface InterPenjualan {

    Penjualan insert(Penjualan o) throws SQLException;

    void update(Penjualan o) throws SQLException;
}
