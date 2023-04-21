package com.manning.sdmia.dataaccess.domain.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.manning.sdmia.dataaccess.domain.model.Contact;

public class ContactRowMapper implements RowMapper {

    public Object mapRow(ResultSet rs, int i) throws SQLException {
        Contact contact = new Contact();
        contact.setId(rs.getInt("id"));
        contact.setLastName(rs.getString("last_name"));
        contact.setFirstName(rs.getString("first_name"));
        return contact;
    }
}
