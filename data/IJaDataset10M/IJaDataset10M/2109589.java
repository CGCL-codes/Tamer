package dk.highflier.airlog.dataaccess;

import dk.highflier.airlog.common.*;
import dk.highflier.airlog.entity.*;
import dk.highflier.airlog.utility.*;

public class PilotDA extends DataAccessDelegator {

    public PilotDA() {
        setKeyField("id");
        setMutableFields(new String[] { "name", "address1", "address2", "zip", "city", "countryState", "country", "birthdate", "thePFTTheory", "thePFT", "medical", "passenger", "instructor", "localeLanguage", "localeCountry", "localeVariant", "username", "password" });
        setProperties(PropertiesManager.getInstance().getDBProperties("PilotDA"));
    }

    public EntityObject instantiate(SmartResultSet resultSet) throws java.sql.SQLException, java.lang.IllegalArgumentException {
        return (PilotImpl) new PilotHome().instantiate(resultSet.getLong(resolveAlias("id")), resultSet.getString(resolveAlias("name")), resultSet.getString(resolveAlias("address1")), resultSet.getString(resolveAlias("address2")), resultSet.getString(resolveAlias("zip")), resultSet.getString(resolveAlias("city")), resultSet.getString(resolveAlias("countryState")), resultSet.getString(resolveAlias("country")), resultSet.getDate(resolveAlias("birthdate")), resultSet.getDate(resolveAlias("thePFTTheory")), resultSet.getDate(resolveAlias("thePFT")), resultSet.getDate(resolveAlias("medical")), resultSet.getBoolean(resolveAlias("passenger")), resultSet.getBoolean(resolveAlias("instructor")), resultSet.getString(resolveAlias("localeLanguage")), resultSet.getString(resolveAlias("localeCountry")), resultSet.getString(resolveAlias("localeVariant")), resultSet.getString(resolveAlias("username")), resultSet.getString(resolveAlias("password")));
    }

    public void fillInsertStatement(SmartPreparedStatement stmt, EntityObject object) throws java.sql.SQLException, java.lang.IllegalArgumentException {
        if (!(object instanceof Pilot)) {
            log.error("Object not a Pilot");
            throw new java.lang.IllegalArgumentException("Object not a Pilot");
        }
        Pilot workObject = (Pilot) object;
        stmt.set(getParameterIndex("name", SQLOperation.INSERT), workObject.getName());
        stmt.set(getParameterIndex("address1", SQLOperation.INSERT), workObject.getAddress1());
        stmt.set(getParameterIndex("address2", SQLOperation.INSERT), workObject.getAddress2());
        stmt.set(getParameterIndex("zip", SQLOperation.INSERT), workObject.getZip());
        stmt.set(getParameterIndex("city", SQLOperation.INSERT), workObject.getCity());
        stmt.set(getParameterIndex("countryState", SQLOperation.INSERT), workObject.getCountryState());
        stmt.set(getParameterIndex("country", SQLOperation.INSERT), workObject.getCountry());
        stmt.set(getParameterIndex("birthdate", SQLOperation.INSERT), workObject.getBirthdate());
        stmt.set(getParameterIndex("thePFTTheory", SQLOperation.INSERT), workObject.getPFTTheory());
        stmt.set(getParameterIndex("thePFT", SQLOperation.INSERT), workObject.getPFT());
        stmt.set(getParameterIndex("medical", SQLOperation.INSERT), workObject.getMedical());
        stmt.set(getParameterIndex("passenger", SQLOperation.INSERT), workObject.isPassenger());
        stmt.set(getParameterIndex("instructor", SQLOperation.INSERT), workObject.isInstructor());
        stmt.set(getParameterIndex("localeLanguage", SQLOperation.INSERT), workObject.getLocaleLanguage());
        stmt.set(getParameterIndex("localeCountry", SQLOperation.INSERT), workObject.getLocaleCountry());
        stmt.set(getParameterIndex("localeVariant", SQLOperation.INSERT), workObject.getLocaleVariant());
        stmt.set(getParameterIndex("username", SQLOperation.INSERT), workObject.getUsername());
        stmt.set(getParameterIndex("password", SQLOperation.INSERT), workObject.getPassword());
    }

    public void fillUpdateStatement(SmartPreparedStatement stmt, EntityObject object) throws java.sql.SQLException, java.lang.IllegalArgumentException {
        if (!(object instanceof Pilot)) {
            log.error("Object not a Pilot");
            throw new java.lang.IllegalArgumentException("Object not a Pilot");
        }
        Pilot workObject = (Pilot) object;
        stmt.set(getParameterIndex("name", SQLOperation.UPDATE), workObject.getName());
        stmt.set(getParameterIndex("address1", SQLOperation.UPDATE), workObject.getAddress1());
        stmt.set(getParameterIndex("address2", SQLOperation.UPDATE), workObject.getAddress2());
        stmt.set(getParameterIndex("zip", SQLOperation.UPDATE), workObject.getZip());
        stmt.set(getParameterIndex("city", SQLOperation.UPDATE), workObject.getCity());
        stmt.set(getParameterIndex("countryState", SQLOperation.UPDATE), workObject.getCountryState());
        stmt.set(getParameterIndex("country", SQLOperation.UPDATE), workObject.getCountry());
        stmt.set(getParameterIndex("birthdate", SQLOperation.UPDATE), workObject.getBirthdate());
        stmt.set(getParameterIndex("thePFTTheory", SQLOperation.UPDATE), workObject.getPFTTheory());
        stmt.set(getParameterIndex("thePFT", SQLOperation.UPDATE), workObject.getPFT());
        stmt.set(getParameterIndex("medical", SQLOperation.UPDATE), workObject.getMedical());
        stmt.set(getParameterIndex("passenger", SQLOperation.UPDATE), workObject.isPassenger());
        stmt.set(getParameterIndex("instructor", SQLOperation.UPDATE), workObject.isInstructor());
        stmt.set(getParameterIndex("localeLanguage", SQLOperation.UPDATE), workObject.getLocaleLanguage());
        stmt.set(getParameterIndex("localeCountry", SQLOperation.UPDATE), workObject.getLocaleCountry());
        stmt.set(getParameterIndex("localeVariant", SQLOperation.UPDATE), workObject.getLocaleVariant());
        stmt.set(getParameterIndex("username", SQLOperation.UPDATE), workObject.getUsername());
        stmt.set(getParameterIndex("password", SQLOperation.UPDATE), workObject.getPassword());
        stmt.set(getParameterIndex("id", SQLOperation.UPDATE), workObject.getId());
    }
}
