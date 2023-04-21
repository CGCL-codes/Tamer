package CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis;

public class AddressType implements java.io.Serializable {

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressTypeCodeType addressTypeCode;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressFormatCodeType addressFormatCode;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostboxType postbox;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.FloorType floor;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RoomType room;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.StreetNameType streetName;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AdditionalStreetNameType additionalStreetName;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BlockNameType blockName;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNameType buildingName;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNumberType buildingNumber;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.InhouseMailType inhouseMail;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DepartmentType department;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkAttentionType markAttention;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkCareType markCare;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PlotIdentificationType plotIdentification;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CitySubdivisionNameType citySubdivisionName;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CityNameType cityName;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostalZoneType postalZone;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityType countrySubentity;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityCodeType countrySubentityCode;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RegionType region;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DistrictType district;

    private CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.TimezoneOffsetType timezoneOffset;

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AddressLineType[] addressLine;

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.CountryType country;

    private CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationCoordinateType locationCoordinate;

    private long hjid;

    public AddressType() {
    }

    public AddressType(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressTypeCodeType addressTypeCode, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressFormatCodeType addressFormatCode, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostboxType postbox, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.FloorType floor, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RoomType room, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.StreetNameType streetName, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AdditionalStreetNameType additionalStreetName, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BlockNameType blockName, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNameType buildingName, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNumberType buildingNumber, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.InhouseMailType inhouseMail, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DepartmentType department, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkAttentionType markAttention, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkCareType markCare, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PlotIdentificationType plotIdentification, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CitySubdivisionNameType citySubdivisionName, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CityNameType cityName, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostalZoneType postalZone, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityType countrySubentity, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityCodeType countrySubentityCode, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RegionType region, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DistrictType district, CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.TimezoneOffsetType timezoneOffset, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AddressLineType[] addressLine, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.CountryType country, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationCoordinateType locationCoordinate, long hjid) {
        this.ID = ID;
        this.addressTypeCode = addressTypeCode;
        this.addressFormatCode = addressFormatCode;
        this.postbox = postbox;
        this.floor = floor;
        this.room = room;
        this.streetName = streetName;
        this.additionalStreetName = additionalStreetName;
        this.blockName = blockName;
        this.buildingName = buildingName;
        this.buildingNumber = buildingNumber;
        this.inhouseMail = inhouseMail;
        this.department = department;
        this.markAttention = markAttention;
        this.markCare = markCare;
        this.plotIdentification = plotIdentification;
        this.citySubdivisionName = citySubdivisionName;
        this.cityName = cityName;
        this.postalZone = postalZone;
        this.countrySubentity = countrySubentity;
        this.countrySubentityCode = countrySubentityCode;
        this.region = region;
        this.district = district;
        this.timezoneOffset = timezoneOffset;
        this.addressLine = addressLine;
        this.country = country;
        this.locationCoordinate = locationCoordinate;
        this.hjid = hjid;
    }

    /**
     * Gets the ID value for this AddressType.
     * 
     * @return ID
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType getID() {
        return ID;
    }

    /**
     * Sets the ID value for this AddressType.
     * 
     * @param ID
     */
    public void setID(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.IDType ID) {
        this.ID = ID;
    }

    /**
     * Gets the addressTypeCode value for this AddressType.
     * 
     * @return addressTypeCode
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressTypeCodeType getAddressTypeCode() {
        return addressTypeCode;
    }

    /**
     * Sets the addressTypeCode value for this AddressType.
     * 
     * @param addressTypeCode
     */
    public void setAddressTypeCode(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressTypeCodeType addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
    }

    /**
     * Gets the addressFormatCode value for this AddressType.
     * 
     * @return addressFormatCode
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressFormatCodeType getAddressFormatCode() {
        return addressFormatCode;
    }

    /**
     * Sets the addressFormatCode value for this AddressType.
     * 
     * @param addressFormatCode
     */
    public void setAddressFormatCode(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AddressFormatCodeType addressFormatCode) {
        this.addressFormatCode = addressFormatCode;
    }

    /**
     * Gets the postbox value for this AddressType.
     * 
     * @return postbox
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostboxType getPostbox() {
        return postbox;
    }

    /**
     * Sets the postbox value for this AddressType.
     * 
     * @param postbox
     */
    public void setPostbox(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostboxType postbox) {
        this.postbox = postbox;
    }

    /**
     * Gets the floor value for this AddressType.
     * 
     * @return floor
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.FloorType getFloor() {
        return floor;
    }

    /**
     * Sets the floor value for this AddressType.
     * 
     * @param floor
     */
    public void setFloor(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.FloorType floor) {
        this.floor = floor;
    }

    /**
     * Gets the room value for this AddressType.
     * 
     * @return room
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RoomType getRoom() {
        return room;
    }

    /**
     * Sets the room value for this AddressType.
     * 
     * @param room
     */
    public void setRoom(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RoomType room) {
        this.room = room;
    }

    /**
     * Gets the streetName value for this AddressType.
     * 
     * @return streetName
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.StreetNameType getStreetName() {
        return streetName;
    }

    /**
     * Sets the streetName value for this AddressType.
     * 
     * @param streetName
     */
    public void setStreetName(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.StreetNameType streetName) {
        this.streetName = streetName;
    }

    /**
     * Gets the additionalStreetName value for this AddressType.
     * 
     * @return additionalStreetName
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AdditionalStreetNameType getAdditionalStreetName() {
        return additionalStreetName;
    }

    /**
     * Sets the additionalStreetName value for this AddressType.
     * 
     * @param additionalStreetName
     */
    public void setAdditionalStreetName(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.AdditionalStreetNameType additionalStreetName) {
        this.additionalStreetName = additionalStreetName;
    }

    /**
     * Gets the blockName value for this AddressType.
     * 
     * @return blockName
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BlockNameType getBlockName() {
        return blockName;
    }

    /**
     * Sets the blockName value for this AddressType.
     * 
     * @param blockName
     */
    public void setBlockName(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BlockNameType blockName) {
        this.blockName = blockName;
    }

    /**
     * Gets the buildingName value for this AddressType.
     * 
     * @return buildingName
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNameType getBuildingName() {
        return buildingName;
    }

    /**
     * Sets the buildingName value for this AddressType.
     * 
     * @param buildingName
     */
    public void setBuildingName(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNameType buildingName) {
        this.buildingName = buildingName;
    }

    /**
     * Gets the buildingNumber value for this AddressType.
     * 
     * @return buildingNumber
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNumberType getBuildingNumber() {
        return buildingNumber;
    }

    /**
     * Sets the buildingNumber value for this AddressType.
     * 
     * @param buildingNumber
     */
    public void setBuildingNumber(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.BuildingNumberType buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    /**
     * Gets the inhouseMail value for this AddressType.
     * 
     * @return inhouseMail
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.InhouseMailType getInhouseMail() {
        return inhouseMail;
    }

    /**
     * Sets the inhouseMail value for this AddressType.
     * 
     * @param inhouseMail
     */
    public void setInhouseMail(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.InhouseMailType inhouseMail) {
        this.inhouseMail = inhouseMail;
    }

    /**
     * Gets the department value for this AddressType.
     * 
     * @return department
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DepartmentType getDepartment() {
        return department;
    }

    /**
     * Sets the department value for this AddressType.
     * 
     * @param department
     */
    public void setDepartment(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DepartmentType department) {
        this.department = department;
    }

    /**
     * Gets the markAttention value for this AddressType.
     * 
     * @return markAttention
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkAttentionType getMarkAttention() {
        return markAttention;
    }

    /**
     * Sets the markAttention value for this AddressType.
     * 
     * @param markAttention
     */
    public void setMarkAttention(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkAttentionType markAttention) {
        this.markAttention = markAttention;
    }

    /**
     * Gets the markCare value for this AddressType.
     * 
     * @return markCare
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkCareType getMarkCare() {
        return markCare;
    }

    /**
     * Sets the markCare value for this AddressType.
     * 
     * @param markCare
     */
    public void setMarkCare(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.MarkCareType markCare) {
        this.markCare = markCare;
    }

    /**
     * Gets the plotIdentification value for this AddressType.
     * 
     * @return plotIdentification
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PlotIdentificationType getPlotIdentification() {
        return plotIdentification;
    }

    /**
     * Sets the plotIdentification value for this AddressType.
     * 
     * @param plotIdentification
     */
    public void setPlotIdentification(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PlotIdentificationType plotIdentification) {
        this.plotIdentification = plotIdentification;
    }

    /**
     * Gets the citySubdivisionName value for this AddressType.
     * 
     * @return citySubdivisionName
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CitySubdivisionNameType getCitySubdivisionName() {
        return citySubdivisionName;
    }

    /**
     * Sets the citySubdivisionName value for this AddressType.
     * 
     * @param citySubdivisionName
     */
    public void setCitySubdivisionName(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CitySubdivisionNameType citySubdivisionName) {
        this.citySubdivisionName = citySubdivisionName;
    }

    /**
     * Gets the cityName value for this AddressType.
     * 
     * @return cityName
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CityNameType getCityName() {
        return cityName;
    }

    /**
     * Sets the cityName value for this AddressType.
     * 
     * @param cityName
     */
    public void setCityName(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CityNameType cityName) {
        this.cityName = cityName;
    }

    /**
     * Gets the postalZone value for this AddressType.
     * 
     * @return postalZone
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostalZoneType getPostalZone() {
        return postalZone;
    }

    /**
     * Sets the postalZone value for this AddressType.
     * 
     * @param postalZone
     */
    public void setPostalZone(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.PostalZoneType postalZone) {
        this.postalZone = postalZone;
    }

    /**
     * Gets the countrySubentity value for this AddressType.
     * 
     * @return countrySubentity
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityType getCountrySubentity() {
        return countrySubentity;
    }

    /**
     * Sets the countrySubentity value for this AddressType.
     * 
     * @param countrySubentity
     */
    public void setCountrySubentity(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityType countrySubentity) {
        this.countrySubentity = countrySubentity;
    }

    /**
     * Gets the countrySubentityCode value for this AddressType.
     * 
     * @return countrySubentityCode
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityCodeType getCountrySubentityCode() {
        return countrySubentityCode;
    }

    /**
     * Sets the countrySubentityCode value for this AddressType.
     * 
     * @param countrySubentityCode
     */
    public void setCountrySubentityCode(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.CountrySubentityCodeType countrySubentityCode) {
        this.countrySubentityCode = countrySubentityCode;
    }

    /**
     * Gets the region value for this AddressType.
     * 
     * @return region
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RegionType getRegion() {
        return region;
    }

    /**
     * Sets the region value for this AddressType.
     * 
     * @param region
     */
    public void setRegion(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.RegionType region) {
        this.region = region;
    }

    /**
     * Gets the district value for this AddressType.
     * 
     * @return district
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DistrictType getDistrict() {
        return district;
    }

    /**
     * Sets the district value for this AddressType.
     * 
     * @param district
     */
    public void setDistrict(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.DistrictType district) {
        this.district = district;
    }

    /**
     * Gets the timezoneOffset value for this AddressType.
     * 
     * @return timezoneOffset
     */
    public CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.TimezoneOffsetType getTimezoneOffset() {
        return timezoneOffset;
    }

    /**
     * Sets the timezoneOffset value for this AddressType.
     * 
     * @param timezoneOffset
     */
    public void setTimezoneOffset(CommonBasicComponents_2.xsd.schema.ubl.specification.names.oasis.TimezoneOffsetType timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    /**
     * Gets the addressLine value for this AddressType.
     * 
     * @return addressLine
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AddressLineType[] getAddressLine() {
        return addressLine;
    }

    /**
     * Sets the addressLine value for this AddressType.
     * 
     * @param addressLine
     */
    public void setAddressLine(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AddressLineType[] addressLine) {
        this.addressLine = addressLine;
    }

    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AddressLineType getAddressLine(int i) {
        return this.addressLine[i];
    }

    public void setAddressLine(int i, CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.AddressLineType _value) {
        this.addressLine[i] = _value;
    }

    /**
     * Gets the country value for this AddressType.
     * 
     * @return country
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.CountryType getCountry() {
        return country;
    }

    /**
     * Sets the country value for this AddressType.
     * 
     * @param country
     */
    public void setCountry(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.CountryType country) {
        this.country = country;
    }

    /**
     * Gets the locationCoordinate value for this AddressType.
     * 
     * @return locationCoordinate
     */
    public CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationCoordinateType getLocationCoordinate() {
        return locationCoordinate;
    }

    /**
     * Sets the locationCoordinate value for this AddressType.
     * 
     * @param locationCoordinate
     */
    public void setLocationCoordinate(CommonAggregateComponents_2.xsd.schema.ubl.specification.names.oasis.LocationCoordinateType locationCoordinate) {
        this.locationCoordinate = locationCoordinate;
    }

    /**
     * Gets the hjid value for this AddressType.
     * 
     * @return hjid
     */
    public long getHjid() {
        return hjid;
    }

    /**
     * Sets the hjid value for this AddressType.
     * 
     * @param hjid
     */
    public void setHjid(long hjid) {
        this.hjid = hjid;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof AddressType)) return false;
        AddressType other = (AddressType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.ID == null && other.getID() == null) || (this.ID != null && this.ID.equals(other.getID()))) && ((this.addressTypeCode == null && other.getAddressTypeCode() == null) || (this.addressTypeCode != null && this.addressTypeCode.equals(other.getAddressTypeCode()))) && ((this.addressFormatCode == null && other.getAddressFormatCode() == null) || (this.addressFormatCode != null && this.addressFormatCode.equals(other.getAddressFormatCode()))) && ((this.postbox == null && other.getPostbox() == null) || (this.postbox != null && this.postbox.equals(other.getPostbox()))) && ((this.floor == null && other.getFloor() == null) || (this.floor != null && this.floor.equals(other.getFloor()))) && ((this.room == null && other.getRoom() == null) || (this.room != null && this.room.equals(other.getRoom()))) && ((this.streetName == null && other.getStreetName() == null) || (this.streetName != null && this.streetName.equals(other.getStreetName()))) && ((this.additionalStreetName == null && other.getAdditionalStreetName() == null) || (this.additionalStreetName != null && this.additionalStreetName.equals(other.getAdditionalStreetName()))) && ((this.blockName == null && other.getBlockName() == null) || (this.blockName != null && this.blockName.equals(other.getBlockName()))) && ((this.buildingName == null && other.getBuildingName() == null) || (this.buildingName != null && this.buildingName.equals(other.getBuildingName()))) && ((this.buildingNumber == null && other.getBuildingNumber() == null) || (this.buildingNumber != null && this.buildingNumber.equals(other.getBuildingNumber()))) && ((this.inhouseMail == null && other.getInhouseMail() == null) || (this.inhouseMail != null && this.inhouseMail.equals(other.getInhouseMail()))) && ((this.department == null && other.getDepartment() == null) || (this.department != null && this.department.equals(other.getDepartment()))) && ((this.markAttention == null && other.getMarkAttention() == null) || (this.markAttention != null && this.markAttention.equals(other.getMarkAttention()))) && ((this.markCare == null && other.getMarkCare() == null) || (this.markCare != null && this.markCare.equals(other.getMarkCare()))) && ((this.plotIdentification == null && other.getPlotIdentification() == null) || (this.plotIdentification != null && this.plotIdentification.equals(other.getPlotIdentification()))) && ((this.citySubdivisionName == null && other.getCitySubdivisionName() == null) || (this.citySubdivisionName != null && this.citySubdivisionName.equals(other.getCitySubdivisionName()))) && ((this.cityName == null && other.getCityName() == null) || (this.cityName != null && this.cityName.equals(other.getCityName()))) && ((this.postalZone == null && other.getPostalZone() == null) || (this.postalZone != null && this.postalZone.equals(other.getPostalZone()))) && ((this.countrySubentity == null && other.getCountrySubentity() == null) || (this.countrySubentity != null && this.countrySubentity.equals(other.getCountrySubentity()))) && ((this.countrySubentityCode == null && other.getCountrySubentityCode() == null) || (this.countrySubentityCode != null && this.countrySubentityCode.equals(other.getCountrySubentityCode()))) && ((this.region == null && other.getRegion() == null) || (this.region != null && this.region.equals(other.getRegion()))) && ((this.district == null && other.getDistrict() == null) || (this.district != null && this.district.equals(other.getDistrict()))) && ((this.timezoneOffset == null && other.getTimezoneOffset() == null) || (this.timezoneOffset != null && this.timezoneOffset.equals(other.getTimezoneOffset()))) && ((this.addressLine == null && other.getAddressLine() == null) || (this.addressLine != null && java.util.Arrays.equals(this.addressLine, other.getAddressLine()))) && ((this.country == null && other.getCountry() == null) || (this.country != null && this.country.equals(other.getCountry()))) && ((this.locationCoordinate == null && other.getLocationCoordinate() == null) || (this.locationCoordinate != null && this.locationCoordinate.equals(other.getLocationCoordinate()))) && this.hjid == other.getHjid();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getID() != null) {
            _hashCode += getID().hashCode();
        }
        if (getAddressTypeCode() != null) {
            _hashCode += getAddressTypeCode().hashCode();
        }
        if (getAddressFormatCode() != null) {
            _hashCode += getAddressFormatCode().hashCode();
        }
        if (getPostbox() != null) {
            _hashCode += getPostbox().hashCode();
        }
        if (getFloor() != null) {
            _hashCode += getFloor().hashCode();
        }
        if (getRoom() != null) {
            _hashCode += getRoom().hashCode();
        }
        if (getStreetName() != null) {
            _hashCode += getStreetName().hashCode();
        }
        if (getAdditionalStreetName() != null) {
            _hashCode += getAdditionalStreetName().hashCode();
        }
        if (getBlockName() != null) {
            _hashCode += getBlockName().hashCode();
        }
        if (getBuildingName() != null) {
            _hashCode += getBuildingName().hashCode();
        }
        if (getBuildingNumber() != null) {
            _hashCode += getBuildingNumber().hashCode();
        }
        if (getInhouseMail() != null) {
            _hashCode += getInhouseMail().hashCode();
        }
        if (getDepartment() != null) {
            _hashCode += getDepartment().hashCode();
        }
        if (getMarkAttention() != null) {
            _hashCode += getMarkAttention().hashCode();
        }
        if (getMarkCare() != null) {
            _hashCode += getMarkCare().hashCode();
        }
        if (getPlotIdentification() != null) {
            _hashCode += getPlotIdentification().hashCode();
        }
        if (getCitySubdivisionName() != null) {
            _hashCode += getCitySubdivisionName().hashCode();
        }
        if (getCityName() != null) {
            _hashCode += getCityName().hashCode();
        }
        if (getPostalZone() != null) {
            _hashCode += getPostalZone().hashCode();
        }
        if (getCountrySubentity() != null) {
            _hashCode += getCountrySubentity().hashCode();
        }
        if (getCountrySubentityCode() != null) {
            _hashCode += getCountrySubentityCode().hashCode();
        }
        if (getRegion() != null) {
            _hashCode += getRegion().hashCode();
        }
        if (getDistrict() != null) {
            _hashCode += getDistrict().hashCode();
        }
        if (getTimezoneOffset() != null) {
            _hashCode += getTimezoneOffset().hashCode();
        }
        if (getAddressLine() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getAddressLine()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getAddressLine(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCountry() != null) {
            _hashCode += getCountry().hashCode();
        }
        if (getLocationCoordinate() != null) {
            _hashCode += getLocationCoordinate().hashCode();
        }
        _hashCode += new Long(getHjid()).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(AddressType.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AddressType"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("hjid");
        attrField.setXmlName(new javax.xml.namespace.QName("", "Hjid"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ID");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "ID"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "IDType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addressTypeCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "AddressTypeCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "AddressTypeCodeType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addressFormatCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "AddressFormatCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "AddressFormatCodeType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postbox");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "Postbox"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "PostboxType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("floor");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "Floor"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "FloorType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("room");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "Room"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "RoomType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("streetName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "StreetName"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "StreetNameType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalStreetName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "AdditionalStreetName"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "AdditionalStreetNameType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("blockName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "BlockName"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "BlockNameType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buildingName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "BuildingName"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "BuildingNameType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("buildingNumber");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "BuildingNumber"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "BuildingNumberType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inhouseMail");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "InhouseMail"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "InhouseMailType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("department");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "Department"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "DepartmentType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("markAttention");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "MarkAttention"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "MarkAttentionType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("markCare");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "MarkCare"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "MarkCareType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("plotIdentification");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "PlotIdentification"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "PlotIdentificationType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("citySubdivisionName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CitySubdivisionName"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CitySubdivisionNameType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cityName");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CityName"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CityNameType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("postalZone");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "PostalZone"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "PostalZoneType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countrySubentity");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CountrySubentity"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CountrySubentityType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("countrySubentityCode");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CountrySubentityCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "CountrySubentityCodeType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("region");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "Region"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "RegionType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("district");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "District"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "DistrictType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("timezoneOffset");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "TimezoneOffset"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "TimezoneOffsetType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addressLine");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AddressLine"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "AddressLineType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("country");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "Country"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "CountryType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("locationCoordinate");
        elemField.setXmlName(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "LocationCoordinate"));
        elemField.setXmlType(new javax.xml.namespace.QName("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "LocationCoordinateType"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}
