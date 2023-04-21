package com.kwoksys.biz.hardware.dao;

import com.kwoksys.biz.admin.dao.AttributeDao;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.admin.dto.AttributeFieldCount;
import com.kwoksys.biz.base.BaseDao;
import com.kwoksys.biz.hardware.dto.Hardware;
import com.kwoksys.biz.hardware.dto.HardwareComponent;
import com.kwoksys.biz.hardware.dto.HardwareSoftwareMap;
import com.kwoksys.biz.software.dto.SoftwareLicense;
import com.kwoksys.biz.system.dto.linking.ObjectLink;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.connection.database.QueryHelper;
import com.kwoksys.framework.exception.DatabaseException;
import com.kwoksys.framework.exception.ObjectNotFoundException;
import com.kwoksys.framework.system.ObjectTypes;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.DatetimeUtils;
import com.kwoksys.framework.util.StringUtils;
import org.apache.struts.action.ActionMessages;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * HardwareDao.
 */
public class HardwareDao extends BaseDao {

    public HardwareDao() {
    }

    public HardwareDao(RequestContext requestContext) {
        super(requestContext);
    }

    public List<Hardware> getHardwareList(QueryBits query, QueryHelper queryHelper) throws DatabaseException {
        Connection conn = getConnection();
        if (queryHelper == null) {
            queryHelper = new QueryHelper(HardwareQueries.selectHardwareListQuery(query));
        }
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                Hardware hardware = new Hardware();
                hardware.setId(rs.getInt("hardware_id"));
                hardware.setName(StringUtils.replaceNull(rs.getString("hardware_name")));
                hardware.setDescription(StringUtils.replaceNull(rs.getString("hardware_description")));
                hardware.setManufacturerName(StringUtils.replaceNull(rs.getString("hardware_manufacturer")));
                hardware.setVendorName(StringUtils.replaceNull(rs.getString("hardware_vendor")));
                hardware.setModelName(StringUtils.replaceNull(rs.getString("hardware_model_name")));
                hardware.setModelNumber(StringUtils.replaceNull(rs.getString("hardware_model_number")));
                hardware.setSerialNumber(StringUtils.replaceNull(rs.getString("hardware_serial_number")));
                hardware.setLastServicedOn(DatetimeUtils.getDate(rs, "hardware_last_service_date"));
                hardware.setLocation(rs.getInt("hardware_location"));
                hardware.setType(rs.getInt("hardware_type"));
                hardware.setStatus(rs.getInt("hardware_status"));
                hardware.setHardwareCost(rs.getString("hardware_cost"));
                hardware.setHardwarePurchaseDate(rs.getDate("hardware_purchase_date"));
                hardware.setWarrantyExpireDate(rs.getDate("hardware_warranty_expire_date"));
                hardware.setOwner(new AccessUser());
                hardware.getOwner().setId(rs.getInt("hardware_owner_id"));
                hardware.getOwner().setUsername(rs.getString("hardware_owner_username"));
                hardware.getOwner().setDisplayName(rs.getString("hardware_owner_display_name"));
                hardware.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                hardware.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
                hardware.setCreator(new AccessUser());
                hardware.getCreator().setId(rs.getInt("creator"));
                hardware.getCreator().setUsername(rs.getString("creator_username"));
                hardware.getCreator().setDisplayName(rs.getString("creator_display_name"));
                hardware.setModifier(new AccessUser());
                hardware.getModifier().setId(rs.getInt("modifier"));
                hardware.getModifier().setUsername(rs.getString("modifier_username"));
                hardware.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                list.add(hardware);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    public int getCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareCountQuery(query));
        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            rs.next();
            return rs.getInt("row_count");
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    public List<Hardware> getLinkedHardwareList(QueryBits query, ObjectLink objectMap) throws DatabaseException {
        QueryHelper queryHelper;
        if (objectMap.getLinkedObjectId() == null || objectMap.getLinkedObjectId() == 0) {
            queryHelper = new QueryHelper(HardwareQueries.selectLinkedHardwareListQuery(query));
            queryHelper.addInputInt(objectMap.getObjectId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
        } else {
            queryHelper = new QueryHelper(HardwareQueries.selectObjectHardwareListQuery(query));
            queryHelper.addInputInt(objectMap.getLinkedObjectId());
            queryHelper.addInputInt(objectMap.getLinkedObjectTypeId());
            queryHelper.addInputInt(objectMap.getObjectTypeId());
        }
        return getHardwareList(query, queryHelper);
    }

    /**
     * Return number of hardware grouped by type.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareTypeCount(QueryBits query) throws DatabaseException {
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareTypeCountQuery(query));
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_type"));
                count.setObjectCount(rs.getInt("hardware_count"));
                list.add(count);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    /**
     * Return number of hardware grouped by status.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareStatusCount(QueryBits query) throws DatabaseException {
        String sqlQuery = HardwareQueries.selectHardwareCountByStatusQuery(query);
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_status"));
                count.setObjectCount(rs.getInt("hardware_count"));
                list.add(count);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    /**
     * Return number of hardware grouped by location.
     *
     * @return ..
     */
    public List<AttributeFieldCount> getHardwareLocationCount(QueryBits query) throws DatabaseException {
        String sqlQuery = HardwareQueries.selectHardwareCountByLocationQuery(query);
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                AttributeFieldCount count = new AttributeFieldCount();
                count.setAttrFieldId(rs.getInt("hardware_location"));
                count.setObjectCount(rs.getInt("hardware_count"));
                list.add(count);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    public Hardware getHardware(Integer hardwareId) throws DatabaseException, ObjectNotFoundException {
        String sqlQuery = HardwareQueries.selectHardwareDetailQuery();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(hardwareId);
        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                Hardware hardware = new Hardware();
                hardware.setId(rs.getInt("hardware_id"));
                hardware.setName(StringUtils.replaceNull(rs.getString("hardware_name")));
                hardware.setSerialNumber(StringUtils.replaceNull(rs.getString("hardware_serial_number")));
                hardware.setModelName(StringUtils.replaceNull(rs.getString("hardware_model_name")));
                hardware.setModelNumber(StringUtils.replaceNull(rs.getString("hardware_model_number")));
                hardware.setDescription(StringUtils.replaceNull(rs.getString("hardware_description")));
                hardware.setManufacturerId(rs.getInt("manufacturer_company_id"));
                hardware.setManufacturerName(StringUtils.replaceNull(rs.getString("hardware_manufacturer")));
                hardware.setVendorId(rs.getInt("vendor_company_id"));
                hardware.setVendorName(StringUtils.replaceNull(rs.getString("hardware_vendor")));
                hardware.setHardwarePurchaseDate(rs.getDate("hardware_purchase_date"));
                if (hardware.getHardwarePurchaseDate() != null) {
                    hardware.setPurchaseYear(DatetimeUtils.toYearString(hardware.getHardwarePurchaseDate()));
                    hardware.setPurchaseMonth(DatetimeUtils.toMonthString(hardware.getHardwarePurchaseDate()));
                    hardware.setPurchaseDate(DatetimeUtils.toDateString(hardware.getHardwarePurchaseDate()));
                }
                hardware.setWarrantyExpireDate(rs.getDate("hardware_warranty_expire_date"));
                if (hardware.getWarrantyExpireDate() != null) {
                    hardware.setWarrantyYear(DatetimeUtils.toYearString(hardware.getWarrantyExpireDate()));
                    hardware.setWarrantyMonth(DatetimeUtils.toMonthString(hardware.getWarrantyExpireDate()));
                    hardware.setWarrantyDate(DatetimeUtils.toDateString(hardware.getWarrantyExpireDate()));
                }
                hardware.setLastServicedOn(DatetimeUtils.getDate(rs, "hardware_last_service_date"));
                hardware.setResetLastServiceDate(0);
                hardware.setHardwareCost(rs.getString("hardware_cost"));
                hardware.setLocation(rs.getInt("hardware_location"));
                hardware.setType(rs.getInt("hardware_type"));
                hardware.setStatus(rs.getInt("hardware_status"));
                hardware.setCountSoftware(rs.getInt("software_count"));
                hardware.setCountFile(rs.getInt("file_count"));
                hardware.setCountComponent(rs.getInt("component_count"));
                hardware.setCreationDate(DatetimeUtils.getDate(rs, "creation_date"));
                hardware.setModificationDate(DatetimeUtils.getDate(rs, "modification_date"));
                hardware.setOwner(new AccessUser());
                hardware.getOwner().setId(rs.getInt("hardware_owner_id"));
                hardware.getOwner().setUsername(rs.getString("hardware_owner_username"));
                hardware.getOwner().setDisplayName(rs.getString("hardware_owner_display_name"));
                hardware.setCreator(new AccessUser());
                hardware.getCreator().setId(rs.getInt("creator"));
                hardware.getCreator().setUsername(rs.getString("creator_username"));
                hardware.getCreator().setDisplayName(rs.getString("creator_display_name"));
                hardware.setModifier(new AccessUser());
                hardware.getModifier().setId(rs.getInt("modifier"));
                hardware.getModifier().setUsername(rs.getString("modifier_username"));
                hardware.getModifier().setDisplayName(rs.getString("modifier_display_name"));
                return hardware;
            }
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        throw new ObjectNotFoundException();
    }

    public List getAvailableSoftware(QueryBits query) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.selectHardwareAvailableSoftwareQuery(query));
        return executeQueryReturnList(queryHelper);
    }

    public List<SoftwareLicense> getAvailableLicense(QueryBits query, Integer softwareId) throws DatabaseException {
        String sqlQuery = HardwareQueries.selectHardwareAvailableLicensesQuery(query);
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(softwareId);
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                SoftwareLicense license = new SoftwareLicense();
                license.setId(rs.getInt("license_id"));
                license.setKey(rs.getString("license_key"));
                license.setNote(StringUtils.replaceNull(rs.getString("license_note")));
                list.add(license);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    /**
     * Returns Software Licenses installed on a Hardware.
     *
     */
    public List<HardwareSoftwareMap> getInstalledLicense(QueryBits query, Integer hardwareId) throws DatabaseException {
        String sqlQuery = HardwareQueries.selectInstalledLicenseQuery(query);
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(hardwareId);
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                HardwareSoftwareMap map = new HardwareSoftwareMap();
                map.setMapId(rs.getInt("map_id"));
                map.setSoftwareId(rs.getInt("software_id"));
                map.getSoftware().setName(rs.getString("software_name"));
                map.setLicenseId(rs.getInt("license_id"));
                map.getLicense().setKey(rs.getString("license_key"));
                map.getLicense().setNote(StringUtils.replaceNull(rs.getString("license_note")));
                list.add(map);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    /**
     * Return Components for a particular Hardware.
     */
    public List<HardwareComponent> getHardwareComponents(QueryBits query, Integer hardwareId) throws DatabaseException {
        String sqlQuery = HardwareQueries.selectHardwareComponentsQuery(query);
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(hardwareId);
        try {
            List list = new ArrayList();
            ResultSet rs = queryHelper.executeQuery(conn);
            while (rs.next()) {
                HardwareComponent component = new HardwareComponent();
                component.setId(rs.getInt("comp_id"));
                component.setTypeName(rs.getString("comp_name"));
                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
                list.add(component);
            }
            return list;
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
    }

    /**
     * Return a specified hardware component.
     */
    public HardwareComponent getHardwareComponentDetail(Integer hardwareId, Integer componentId) throws DatabaseException, ObjectNotFoundException {
        String sqlQuery = HardwareQueries.selectHardwareComponentDetailQuery();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(hardwareId);
        queryHelper.addInputInt(componentId);
        try {
            ResultSet rs = queryHelper.executeQuery(conn);
            if (rs.next()) {
                HardwareComponent component = new HardwareComponent();
                component.setHardwareId(rs.getInt("hardware_id"));
                component.setId(rs.getInt("comp_id"));
                component.setType(rs.getInt("hardware_component_type"));
                component.setDescription(StringUtils.replaceNull(rs.getString("comp_description")));
                return component;
            }
        } catch (Exception e) {
            throw new DatabaseException(e, queryHelper);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        throw new ObjectNotFoundException();
    }

    public ActionMessages addHardware(Hardware hardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        String sqlQuery = HardwareQueries.insertHardwareQuery();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputStringConvertNull(hardware.getName());
        queryHelper.addInputStringConvertNull("");
        queryHelper.addInputStringConvertNull(hardware.getDescription());
        queryHelper.addInputIntegerConvertNull(hardware.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(hardware.getVendorId());
        queryHelper.addInputInt(hardware.getType());
        queryHelper.addInputInt(hardware.getStatus());
        queryHelper.addInputIntegerConvertNull(hardware.getOwnerId());
        queryHelper.addInputInt(hardware.getLocation());
        queryHelper.addInputStringConvertNull(hardware.getModelName());
        queryHelper.addInputStringConvertNull(hardware.getModelNumber());
        queryHelper.addInputStringConvertNull(hardware.getSerialNumber());
        if (hardware.getHardwareCost().isEmpty() || Double.parseDouble(hardware.getHardwareCost()) == 0) {
            queryHelper.addInputDoubleConvertNull(null);
        } else {
            queryHelper.addInputDouble(Double.parseDouble(hardware.getHardwareCost()));
        }
        queryHelper.addInputInt(hardware.getResetLastServiceDate());
        queryHelper.addInputStringConvertNull(hardware.hasHardwarePurchaseDate() ? hardware.getHardwarePurchaseDateString() : null);
        queryHelper.addInputStringConvertNull(hardware.hasHardwareWarrantyExpireDate() ? hardware.getWarrantyExpireDateString() : null);
        queryHelper.addInputInt(requestContext.getUser().getId());
        try {
            CallableStatement cstmt = queryHelper.executeProcedure(conn);
            hardware.setId(cstmt.getInt(1));
            if (!hardware.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao();
                attributeDao.updateAttributeValue(conn, hardware.getId(), hardware.getCustomValues());
            }
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    public ActionMessages update(Hardware hardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareQuery());
        queryHelper.addInputInt(hardware.getId());
        queryHelper.addInputStringConvertNull(hardware.getName());
        queryHelper.addInputStringConvertNull(null);
        queryHelper.addInputStringConvertNull(hardware.getDescription());
        queryHelper.addInputIntegerConvertNull(hardware.getManufacturerId());
        queryHelper.addInputIntegerConvertNull(hardware.getVendorId());
        queryHelper.addInputInt(hardware.getType());
        queryHelper.addInputInt(hardware.getStatus());
        queryHelper.addInputIntegerConvertNull(hardware.getOwnerId());
        queryHelper.addInputInt(hardware.getLocation());
        queryHelper.addInputStringConvertNull(hardware.getModelName());
        queryHelper.addInputStringConvertNull(hardware.getModelNumber());
        queryHelper.addInputStringConvertNull(hardware.getSerialNumber());
        if (hardware.getHardwareCost().isEmpty() || Double.parseDouble(hardware.getHardwareCost()) == 0) {
            queryHelper.addInputDoubleConvertNull(null);
        } else {
            queryHelper.addInputDouble(Double.parseDouble(hardware.getHardwareCost()));
        }
        queryHelper.addInputInt(hardware.getResetLastServiceDate());
        queryHelper.addInputStringConvertNull(hardware.hasHardwarePurchaseDate() ? hardware.getHardwarePurchaseDateString() : null);
        queryHelper.addInputStringConvertNull(hardware.hasHardwareWarrantyExpireDate() ? hardware.getWarrantyExpireDateString() : null);
        queryHelper.addInputInt(requestContext.getUser().getId());
        try {
            queryHelper.executeProcedure(conn);
            if (!hardware.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao();
                attributeDao.updateAttributeValue(conn, hardware.getId(), hardware.getCustomValues());
            }
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    public ActionMessages delete(Hardware hardware) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        String sqlQuery = HardwareQueries.deleteHardwareQuery();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addInputInt(ObjectTypes.HARDWARE);
        queryHelper.addInputInt(hardware.getId());
        try {
            queryHelper.executeProcedure(conn);
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    public ActionMessages assignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        String sqlQuery = HardwareQueries.insertAssignLicenseQuery();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(sqlQuery);
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(hsm.getHardwareId());
        queryHelper.addInputInt(hsm.getSoftwareId());
        queryHelper.addInputInt(hsm.getLicenseId());
        queryHelper.addInputInt(hsm.getLicenseEntitlement());
        try {
            CallableStatement cstmt = queryHelper.executeProcedure(conn);
            hsm.setMapId(cstmt.getInt(1));
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    public ActionMessages unassignSoftwareLicense(HardwareSoftwareMap hsm) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.deleteAssignedLicenseQuery());
        queryHelper.addInputInt(hsm.getMapId());
        try {
            queryHelper.executeProcedure(conn);
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    /**
     * Adds hardware component.
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages addHardwareComponent(HardwareComponent component) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.insertHardwareComponentQuery());
        queryHelper.addOutputParam(Types.INTEGER);
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputStringConvertNull(component.getDescription());
        queryHelper.addInputInt(component.getType());
        queryHelper.addInputInt(component.getCreatorId());
        try {
            CallableStatement cstmt = queryHelper.executeProcedure(conn);
            component.setId(cstmt.getInt(1));
            if (!component.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao();
                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
            }
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    /**
     * Updates hardware component.
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages updateHardwareComponent(HardwareComponent component) throws DatabaseException {
        ActionMessages errors = new ActionMessages();
        Connection conn = getConnection();
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareComponentQuery());
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputInt(component.getId());
        queryHelper.addInputStringConvertNull(component.getDescription());
        queryHelper.addInputInt(component.getType());
        queryHelper.addInputInt(component.getModifierId());
        try {
            queryHelper.executeProcedure(conn);
            if (!component.getCustomValues().isEmpty()) {
                AttributeDao attributeDao = new AttributeDao();
                attributeDao.updateAttributeValue(conn, component.getId(), component.getCustomValues());
            }
        } catch (Exception e) {
            queryHelper.handleError(errors, e);
        } finally {
            queryHelper.close();
            closeConnection(conn, queryHelper);
        }
        return errors;
    }

    /**
     * Deletes hardware component.
     * @param component
     * @return
     * @throws DatabaseException
     */
    public ActionMessages deleteHardwareComponent(HardwareComponent component) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.deleteHardwareComponentQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE_COMPONENT);
        queryHelper.addInputInt(component.getHardwareId());
        queryHelper.addInputInt(component.getId());
        return executeProcedure(queryHelper);
    }

    public ActionMessages resetHardwareSoftwareCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareSoftwareCountQuery());
        queryHelper.addInputInt(hardwareId);
        return executeProcedure(queryHelper);
    }

    /**
     * Resets hardware file count.
     * @param hardwareId
     * @return
     * @throws DatabaseException
     */
    public ActionMessages resetFileCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareFileCountQuery());
        queryHelper.addInputInt(ObjectTypes.HARDWARE);
        queryHelper.addInputInt(hardwareId);
        return executeProcedure(queryHelper);
    }

    /**
     * Resets hardware component count.
     * @param hardwareId
     * @return
     * @throws DatabaseException
     */
    public ActionMessages resetComponentCount(Integer hardwareId) throws DatabaseException {
        QueryHelper queryHelper = new QueryHelper(HardwareQueries.updateHardwareComponentCountQuery());
        queryHelper.addInputInt(hardwareId);
        return executeProcedure(queryHelper);
    }
}
