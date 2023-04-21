package edu.mit.osidimpl.agent.shared;

import org.osidx.registry.PropertyElement;
import org.osidx.registry.ValueElement;
import org.osidx.registry.OsidPropertyRegistry;
import org.osidx.registry.OsidValueRegistry;
import org.osidx.registry.oid.OsidProperties;
import org.osidx.registry.oid.OsidValues;

/**
 *  <p>
 *  Defines a convenience class for managing Agent properties.
 *  </p><p>
 *  CVS $Id: AgentProperty.java,v 1.1 2006/04/26 14:24:06 tom Exp $
 *  </p>
 *  
 *  @author Tom Coppeto
 *  @version $OSID: 2.0$ $Revision: 1.1 $
 *  @see org.osid.shared.Type
 */
public class AgentProperty {

    public static final PropertyElement OBJECT_IDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDDOMAIN_PROPERTY);

    public static final PropertyElement OBJECT_IDDOMAINLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDDOMAINLABEL_PROPERTY);

    public static final PropertyElement OBJECT_ID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_ID_PROPERTY);

    public static final PropertyElement OBJECT_IDAUTHORITY_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDAUTHORITY_PROPERTY);

    public static final PropertyElement OBJECT_IDAUTHORITYNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDAUTHORITYNAME_PROPERTY);

    public static final PropertyElement OBJECT_IDAUTHORITYLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDAUTHORITYLABEL_PROPERTY);

    public static final PropertyElement OBJECT_IDAUTHORITYCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDAUTHORITYCONTACT_PROPERTY);

    public static final PropertyElement OBJECT_IDAUTHORITYURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_IDAUTHORITYURL_PROPERTY);

    public static final PropertyElement OBJECT_TYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.OBJECT_TYPE_PROPERTY);

    public static final PropertyElement AGENT_TYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_TYPE_PROPERTY);

    public static final PropertyElement AGENT_TYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_TYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_NAMEPREFERRED_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEPREFERRED_PROPERTY);

    public static final PropertyElement AGENT_NAMEFULL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEFULL_PROPERTY);

    public static final PropertyElement AGENT_NAMEGIVEN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEGIVEN_PROPERTY);

    public static final PropertyElement AGENT_NAMEMIDDLE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEMIDDLE_PROPERTY);

    public static final PropertyElement AGENT_NAMEFAMILY_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEFAMILY_PROPERTY);

    public static final PropertyElement AGENT_NAMESALUTATION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMESALUTATION_PROPERTY);

    public static final PropertyElement AGENT_NAMESUFFIX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMESUFFIX_PROPERTY);

    public static final PropertyElement AGENT_NAMEAPPELLATIVE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEAPPELLATIVE_PROPERTY);

    public static final PropertyElement AGENT_NAMEINITIALS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEINITIALS_PROPERTY);

    public static final PropertyElement AGENT_NAMEALIASINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEALIASINDEX_PROPERTY);

    public static final PropertyElement AGENT_NAMEALIASTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEALIASTYPE_PROPERTY);

    public static final PropertyElement AGENT_NAMEALIASTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEALIASTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_NAMEALIASADVERTISE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEALIASADVERTISE_PROPERTY);

    public static final PropertyElement AGENT_NAMEALIAS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_NAMEALIAS_PROPERTY);

    public static final PropertyElement AGENT_IMAGEINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGEINDEX_PROPERTY);

    public static final PropertyElement AGENT_IMAGEFORMAT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGEFORMAT_PROPERTY);

    public static final PropertyElement AGENT_IMAGEFORMATLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGEFORMATLABEL_PROPERTY);

    public static final PropertyElement AGENT_IMAGEDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGEDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_IMAGESIZE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGESIZE_PROPERTY);

    public static final PropertyElement AGENT_IMAGEHEIGHT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGEHEIGHT_PROPERTY);

    public static final PropertyElement AGENT_IMAGEWIDTH_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGEWIDTH_PROPERTY);

    public static final PropertyElement AGENT_IMAGE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_IMAGE_PROPERTY);

    public static final PropertyElement AGENT_PRINCIPALINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRINCIPALINDEX_PROPERTY);

    public static final PropertyElement AGENT_PRINCIPALNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRINCIPALNAME_PROPERTY);

    public static final PropertyElement AGENT_PRINCIPALDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRINCIPALDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_PRINCIPALISACTIVE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRINCIPALISACTIVE_PROPERTY);

    public static final PropertyElement AGENT_PRINCIPALCREATED_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRINCIPALCREATED_PROPERTY);

    public static final PropertyElement AGENT_PRINCIPALEXPIRED_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRINCIPALEXPIRED_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSINDEX_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSADVERTISE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSADVERTISE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSFULL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSFULL_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCAREOF_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCAREOF_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSMAILSTOPDIST_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSMAILSTOPDIST_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSMAILSTOPLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSMAILSTOPLABEL_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSMAILSTOP_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSMAILSTOP_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSSTREET_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSSTREET_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSLOCALITYOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSLOCALITYOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSLOCALITYIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSLOCALITYIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSLOCALITYID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSLOCALITYID_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSLOCALITYNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSLOCALITYNAME_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSSTATEPROVINCEOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSSTATEPROVINCEOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSSTATEPROVINCEIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSSTATEPROVINCEIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSSTATEPROVINCEID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSSTATEPROVINCEID_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSSTATEPROVINCENAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSSTATEPROVINCENAME_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSSTATEPROVINCELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSSTATEPROVINCELABEL_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSPOSTALCODE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSPOSTALCODE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCOUNTRYOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCOUNTRYOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCOUNTRYIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCOUNTRYIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCOUNTRYID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCOUNTRYID_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCOUNTRYNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCOUNTRYNAME_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCOUNTRYLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCOUNTRYLABEL_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCONTINENTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCONTINENTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCONTINENTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCONTINENTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCONTINENTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCONTINENTID_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSCONTINENTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSCONTINENTNAME_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSPLANETOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSPLANETOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSPLANETIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSPLANETIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSPLANETID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSPLANETID_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSPLANETNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSPLANETNAME_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSGALAXYOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSGALAXYOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSGALAXYIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSGALAXYIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSGALAXYID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSGALAXYID_PROPERTY);

    public static final PropertyElement AGENT_ADDRESSGALAXYNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ADDRESSGALAXYNAME_PROPERTY);

    public static final PropertyElement AGENT_CONTACTINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTINDEX_PROPERTY);

    public static final PropertyElement AGENT_CONTACTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTTYPE_PROPERTY);

    public static final PropertyElement AGENT_CONTACTTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_CONTACTLOCATION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTLOCATION_PROPERTY);

    public static final PropertyElement AGENT_CONTACTLOCATIONLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTLOCATIONLABEL_PROPERTY);

    public static final PropertyElement AGENT_CONTACTDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_CONTACTADVERTISE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTADVERTISE_PROPERTY);

    public static final PropertyElement AGENT_CONTACTADDRESS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTADDRESS_PROPERTY);

    public static final PropertyElement AGENT_CONTACTGMTOFFSET_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTGMTOFFSET_PROPERTY);

    public static final PropertyElement AGENT_CONTACTTEMPERATURE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_CONTACTTEMPERATURE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTINSTURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCLASS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCLASS_PROPERTY);

    public static final PropertyElement AGENT_STUDENTYEAR_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTYEAR_PROPERTY);

    public static final PropertyElement AGENT_STUDENTPRIMARYADBISOROBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTPRIMARYADBISOROBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTPRIMARYADBISORIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTPRIMARYADBISORIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTPRIMARYADVISORID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTPRIMARYADVISORID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTPRIMARYADVISORNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTPRIMARYADVISORNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTPRIMARYADVISORCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTPRIMARYADVISORCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINDEX_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSETYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSETYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSETYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSETYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSENAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSENAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSELABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSECONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSECONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTLABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTCODE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTCODE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEDEPTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEDEPTURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLLABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSESCHOOLURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSESCHOOLURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEINSTURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEADVISOROBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEADVISOROBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEADVISORIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEADVISORIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEADVISORID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEADVISORID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEADVISORNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEADVISORNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTCOURSEADVISORCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTCOURSEADVISORCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDES_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDES_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCEOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCEOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCEIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCEIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCEID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCEID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCETYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCETYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCETYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCETYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCENAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCENAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCELABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCECONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCECONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGRESIDENCEURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGRESIDENCEURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERINDEX_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGLABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTLGMEMBERORGURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTLGMEMBERORGURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYINDEX_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYID_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYNAME_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYLABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYTYPE_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYCONTACT_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYURL_PROPERTY);

    public static final PropertyElement AGENT_STUDENTACTIVITYPOSITION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_STUDENTACTIVITYPOSITION_PROPERTY);

    public static final PropertyElement AGENT_PRESTUDENTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRESTUDENTID_PROPERTY);

    public static final PropertyElement AGENT_PRESTUDENTENTRYYEAR_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRESTUDENTENTRYYEAR_PROPERTY);

    public static final PropertyElement AGENT_PRESTUDENTENTRYTERM_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRESTUDENTENTRYTERM_PROPERTY);

    public static final PropertyElement AGENT_PRESTUDENTENTRYTERMLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRESTUDENTENTRYTERMLABEL_PROPERTY);

    public static final PropertyElement AGENT_PRESTUDENTINCOMINGYEAR_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_PRESTUDENTINCOMINGYEAR_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINDEX_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSETYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSETYPE_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSETYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSETYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEID_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSENAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSENAME_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSELABEL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSECONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSECONTACT_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEURL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTID_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTNAME_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTLABEL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTCODE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTCODE_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEDEPTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEDEPTURL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLID_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLNAME_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLLABEL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLCONTACT_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSESCHOOLURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSESCHOOLURL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTID_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_INTENDEDCOURSEINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTENDEDCOURSEINSTURL_PROPERTY);

    public static final PropertyElement AGENT_ALUMID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMID_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTID_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_ALUMINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMINSTURL_PROPERTY);

    public static final PropertyElement AGENT_ALUMCLASSYEAR_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMCLASSYEAR_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEINDEX_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREETYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREETYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREETYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREETYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEPROGRAMOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEPROGRAMOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEPROGRAMIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEPROGRAMIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEPROGRAMID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEPROGRAMID_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEPROGRAMNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEPROGRAMNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEPROGRAMLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEPROGRAMLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEDEPTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEDEPTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEDEPTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEDEPTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEDEPTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEDEPTID_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEDEPTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEDEPTNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEDEPTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEDEPTLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEDEPTCODE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEDEPTCODE_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREESCHOOLOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREESCHOOLOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREESCHOOLIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREESCHOOLIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREESCHOOLID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREESCHOOLID_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREESCHOOLNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREESCHOOLNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREESCHOOLLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREESCHOOLLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEINSTID_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMDEGREEYEAR_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMDEGREEYEAR_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGINDEX_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGID_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGYEARSTART_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGYEARSTART_PROPERTY);

    public static final PropertyElement AGENT_ALUMLGYEAREND_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMLGYEAREND_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYINDEX_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYID_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYNAME_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYLABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYTYPE_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYYEARSTART_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYYEARSTART_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYYEAREND_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYYEAREND_PROPERTY);

    public static final PropertyElement AGENT_ALUMACTIVITYPOSITION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_ALUMACTIVITYPOSITION_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEID_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTID_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEINSTURL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEPREFERREDTITLE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEPREFERREDTITLE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEPREFERREDGROUP_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEPREFERREDGROUP_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEESTART_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEESTART_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEEND_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEEND_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEESTATUS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEESTATUS_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEESTATUSLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEESTATUSLABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINDEX_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTTYPE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTTYPELABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTTYPELABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTSTATUS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTSTATUS_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTSTATUSLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTSTATUSLABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTPERIODWEEKS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTPERIODWEEKS_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTPERIODMONTHS_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTPERIODMONTHS_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTSTART_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTSTART_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTEND_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTEND_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTADVERTISE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTADVERTISE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTTITLE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTTITLE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTID_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTNAME_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTLABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTCODE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTCODE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDEPTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDEPTURL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONID_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONNAME_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONLABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONCONTACT_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTDIVISIONURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTDIVISIONURL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTID_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_EMPLOYEEAPPTINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_EMPLOYEEAPPTINSTURL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINDEX_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSID_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSNAME_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSLABEL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSCONTACT_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSURL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTID_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTNAME_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTLABEL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTCODE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTCODE_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSDEPTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSDEPTURL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLID_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLNAME_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLLABEL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLCONTACT_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSSCHOOLURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSSCHOOLURL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTOBJECTTYPE_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTOBJECTTYPE_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTIDDOMAIN_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTIDDOMAIN_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTID_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTID_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTNAME_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTLABEL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_FACULTYCLASSINSTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYCLASSINSTURL_PROPERTY);

    public static final PropertyElement AGENT_FACULTYPROJECTINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYPROJECTINDEX_PROPERTY);

    public static final PropertyElement AGENT_FACULTYPROJECTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYPROJECTNAME_PROPERTY);

    public static final PropertyElement AGENT_FACULTYPROJECTDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYPROJECTDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_FACULTYPROJECTCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYPROJECTCONTACT_PROPERTY);

    public static final PropertyElement AGENT_FACULTYPROJECTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_FACULTYPROJECTURL_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPINDEX_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPORGNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPORGNAME_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPORGLABEL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPORGLABEL_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPORGCONTACT_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPORGCONTACT_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPORGURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPORGURL_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPORGDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPORGDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPPROFESSIONAL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPPROFESSIONAL_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPYEARSTART_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPYEARSTART_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPYEAREND_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPYEAREND_PROPERTY);

    public static final PropertyElement AGENT_MEMBERSHIPPOSITION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_MEMBERSHIPPOSITION_PROPERTY);

    public static final PropertyElement AGENT_INTERESTINDEX_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTERESTINDEX_PROPERTY);

    public static final PropertyElement AGENT_INTERESTNAME_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTERESTNAME_PROPERTY);

    public static final PropertyElement AGENT_INTERESTDESCRIPTION_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTERESTDESCRIPTION_PROPERTY);

    public static final PropertyElement AGENT_INTERESTURL_PROPERTY = OsidPropertyRegistry.getPropertyElement(OsidProperties.AGENT_INTERESTURL_PROPERTY);

    public static final ValueElement OBJECT_TYPEUNKNOWN_VALUE = OsidValueRegistry.getValueElement(OsidValues.OBJECT_TYPEUNKNOWN_VALUE);

    public static final ValueElement OBJECT_TYPEID_VALUE = OsidValueRegistry.getValueElement(OsidValues.OBJECT_TYPEID_VALUE);

    public static final ValueElement OBJECT_TYPEAGENT_VALUE = OsidValueRegistry.getValueElement(OsidValues.OBJECT_TYPEAGENT_VALUE);

    public static final ValueElement OBJECT_TYPEGROUP_VALUE = OsidValueRegistry.getValueElement(OsidValues.OBJECT_TYPEGROUP_VALUE);

    public static final ValueElement OBJECT_TYPECOURSE_VALUE = OsidValueRegistry.getValueElement(OsidValues.OBJECT_TYPECOURSE_VALUE);

    public static final ValueElement ID_DOMAINUNKNOWN_VALUE = OsidValueRegistry.getValueElement(OsidValues.ID_DOMAINUNKNOWN_VALUE);

    public static final ValueElement ID_DOMAINOID_VALUE = OsidValueRegistry.getValueElement(OsidValues.ID_DOMAINOID_VALUE);

    public static final ValueElement AGENT_TYPEPERSON_VALUE = OsidValueRegistry.getValueElement(OsidValues.AGENT_TYPEPERSON_VALUE);

    public static final ValueElement AGENT_TYPESYSTEM_VALUE = OsidValueRegistry.getValueElement(OsidValues.AGENT_TYPESYSTEM_VALUE);

    public static final ValueElement PERSON_NAMETYPEPREFERRED_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_NAMETYPEPREFERRED_VALUE);

    public static final ValueElement PERSON_NAMETYPEALIAS_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_NAMETYPEALIAS_VALUE);

    public static final ValueElement PERSON_NAMETYPEFORMER_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_NAMETYPEFORMER_VALUE);

    public static final ValueElement PERSON_NAMETYPEMAIDEN_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_NAMETYPEMAIDEN_VALUE);

    public static final ValueElement PERSON_NAMETYPEWITNESSPROTECTION_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_NAMETYPEWITNESSPROTECTION_VALUE);

    public static final ValueElement PERSON_ADDRESSTYPE_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_ADDRESSTYPE_VALUE);

    public static final ValueElement PERSON_ADDRESSTYPEWORK_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_ADDRESSTYPEWORK_VALUE);

    public static final ValueElement PERSON_ADDRESSTYPEHOME_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_ADDRESSTYPEHOME_VALUE);

    public static final ValueElement PERSON_ADDRESSTYPECAMPUS_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_ADDRESSTYPECAMPUS_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEOTHER_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEOTHER_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEPHONE_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEPHONE_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEFAX_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEFAX_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEPAGER_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEPAGER_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEEMAIL_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEEMAIL_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEAIM_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEAIM_VALUE);

    public static final ValueElement PERSON_CONTACTTYPEYAHOOIM_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTTYPEYAHOOIM_VALUE);

    public static final ValueElement PERSON_CONTACTLOCATIONOTHER_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTLOCATIONOTHER_VALUE);

    public static final ValueElement PERSON_CONTACTLOCATIONWORK_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTLOCATIONWORK_VALUE);

    public static final ValueElement PERSON_CONTACTLOCATIONHOME_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTLOCATIONHOME_VALUE);

    public static final ValueElement PERSON_CONTACTLOCATIONMOBILE_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTLOCATIONMOBILE_VALUE);

    public static final ValueElement PERSON_CONTACTLOCATIONEMERGENCY_VALUE = OsidValueRegistry.getValueElement(OsidValues.PERSON_CONTACTLOCATIONEMERGENCY_VALUE);

    public static final ValueElement LIVING_GROUPTYPEOTHER_VALUE = OsidValueRegistry.getValueElement(OsidValues.LIVING_GROUPTYPEOTHER_VALUE);

    public static final ValueElement LIVING_GROUPTYPEDORMITORY_VALUE = OsidValueRegistry.getValueElement(OsidValues.LIVING_GROUPTYPEDORMITORY_VALUE);

    public static final ValueElement LIVING_GROUPTYPEFRATERNITY_VALUE = OsidValueRegistry.getValueElement(OsidValues.LIVING_GROUPTYPEFRATERNITY_VALUE);

    public static final ValueElement LIVING_GROUPTYPESORORITY_VALUE = OsidValueRegistry.getValueElement(OsidValues.LIVING_GROUPTYPESORORITY_VALUE);

    public static final ValueElement LIVING_GROUPTYPEINDEPENDENT_VALUE = OsidValueRegistry.getValueElement(OsidValues.LIVING_GROUPTYPEINDEPENDENT_VALUE);

    public static final ValueElement EMPLOYEE_STATUSINDEPENDENT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_STATUSINDEPENDENT_VALUE);

    public static final ValueElement EMPLOYEE_STATUSAFFILIATE_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_STATUSAFFILIATE_VALUE);

    public static final ValueElement EMPLOYEE_STATUSACTIVE_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_STATUSACTIVE_VALUE);

    public static final ValueElement EMPLOYEE_STATUSINACTIVE_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_STATUSINACTIVE_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEOTHER_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEOTHER_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEREGFULL_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEREGFULL_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEREGPART_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEREGPART_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEREGFULLEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEREGFULLEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEREGPARTEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEREGPARTEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEREGFULLNONEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEREGFULLNONEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEREGPARTNONEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEREGPARTNONEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETEMPFULL_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETEMPFULL_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETEMPPART_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETEMPPART_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETEMPFULLEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETEMPFULLEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETEMPPARTEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETEMPPARTEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETEMPFULLNONEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETEMPFULLNONEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETEMPPARTNONEXEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETEMPPARTNONEXEMPT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPECONTINGENT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPECONTINGENT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPECONTRACT_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPECONTRACT_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPEACTING_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPEACTING_VALUE);

    public static final ValueElement EMPLOYEE_APPTTYPETRAINEE_VALUE = OsidValueRegistry.getValueElement(OsidValues.EMPLOYEE_APPTTYPETRAINEE_VALUE);

    public static final ValueElement ACADEMIC_TERMFALL_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_TERMFALL_VALUE);

    public static final ValueElement ACADEMIC_TERMSPRING_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_TERMSPRING_VALUE);

    public static final ValueElement ACADEMIC_TERMSUMMER_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_TERMSUMMER_VALUE);

    public static final ValueElement ACADEMIC_TERMWINTER_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_TERMWINTER_VALUE);

    public static final ValueElement ACADEMIC_TERMIAP_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_TERMIAP_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEOTHER_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEOTHER_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPESECONDARY_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPESECONDARY_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPESECONDARYDIPLOMA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPESECONDARYDIPLOMA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBACHELOR_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBACHELOR_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBARCH_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBARCH_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBAS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBAS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBE_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBE_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBIS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBIS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBMM_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBMM_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBPA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBPA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEBN_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEBN_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMASTER_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMASTER_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPELLM_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPELLM_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMAS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMAS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMBA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMBA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMED_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMED_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMFA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMFA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMIS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMIS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMLIS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMLIS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMM_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMM_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMOT_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMOT_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMPA_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMPA_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMPT_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMPT_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMS_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMS_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMUP_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMUP_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEDOCTORATE_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEDOCTORATE_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEAUD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEAUD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEEDD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEEDD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEJD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEJD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEPHARMD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEPHARMD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEPHD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEPHD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPESCD_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPESCD_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEMISC_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEMISC_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEGC_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEGC_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEPBC_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEPBC_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPEPMC_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPEPMC_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPESCP_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPESCP_VALUE);

    public static final ValueElement ACADEMIC_DEGREETYPETC_VALUE = OsidValueRegistry.getValueElement(OsidValues.ACADEMIC_DEGREETYPETC_VALUE);

    public static final ValueElement IMAGE_FORMATBMP_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATBMP_VALUE);

    public static final ValueElement IMAGE_FORMATDNG_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATDNG_VALUE);

    public static final ValueElement IMAGE_FORMATEPS_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATEPS_VALUE);

    public static final ValueElement IMAGE_FORMATGIF_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATGIF_VALUE);

    public static final ValueElement IMAGE_FORMATJPEG_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATJPEG_VALUE);

    public static final ValueElement IMAGE_FORMATJPEGEXIF_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATJPEGEXIF_VALUE);

    public static final ValueElement IMAGE_FORMATJPEGJFIF_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATJPEGJFIF_VALUE);

    public static final ValueElement IMAGE_FORMATJPEG2000_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATJPEG2000_VALUE);

    public static final ValueElement IMAGE_FORMATPDF_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATPDF_VALUE);

    public static final ValueElement IMAGE_FORMATPICT_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATPICT_VALUE);

    public static final ValueElement IMAGE_FORMATPNG_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATPNG_VALUE);

    public static final ValueElement IMAGE_FORMATPSD_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATPSD_VALUE);

    public static final ValueElement IMAGE_FORMATTIFF_VALUE = OsidValueRegistry.getValueElement(OsidValues.IMAGE_FORMATTIFF_VALUE);
}
