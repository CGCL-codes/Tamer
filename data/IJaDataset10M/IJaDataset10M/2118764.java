package atp.reporter.db;

import java.sql.*;

/**
 * @author Shteinke_KE
 * ������ ������ �� ��
 */
public abstract class RDBQuery {

    public abstract void parseData(ResultSet rset) throws SQLException;

    /**
	 * �������� �������� ��������
	 * @return ��������
	 */
    public abstract String getQueryDecription();

    public abstract String getSQL();

    public abstract void fillParameters(RDBParameters parameters);
}
