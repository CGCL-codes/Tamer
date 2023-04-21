package de.simplydevelop.mexs.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.sql.DataSource;

/**
 * Session Bean implementation class XMLMTF_MessageSchemaCRUDBean
 */
@Stateless
@LocalBean
public class XMLMTF_MessageSchemaCRUDBean {

	private static String INSERT_SCHEMA_QUERY = "INSERT INTO MESSAGE_SCHEMA VALUES(?,?,?,?)";

	@Resource(mappedName = "jdbc/meldewesen")
	private DataSource dataSource;

	public void addSchemaData(byte[] messageSchemaData, byte[] setsSchemaData,
			byte[] compositeSchemaData, byte[] fieldsSchemaData) {

		try (Connection con = dataSource.getConnection();
				PreparedStatement s = con.prepareStatement(INSERT_SCHEMA_QUERY)) {
			s.setBytes(1, messageSchemaData);
			s.setBytes(2, setsSchemaData);
			s.setBytes(3, compositeSchemaData);
			s.setBytes(4, fieldsSchemaData);
			int executeUpdate = s.executeUpdate();
			if (executeUpdate != 1)
				throw new IllegalStateException(
						"At least one row has to be updated!");
		} catch (SQLException e) {

			throw new RuntimeException(e);
		}

	}
}
