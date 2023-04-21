package DAO;

import java.sql.ResultSet;
import java.util.List;
import VO.EMPBEAN;

public interface EMPDAO {

    /**
	 * @return
	 */
    public List select();

    public void insert(EMPBEAN empbean);

    public void update(EMPBEAN empBean);

    public void delete(String name);

    public EMPBEAN check(String name);
}
