package com.induslogic.uddi.server.inquiry;

import java.sql.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.*;
import java.io.*;
import java.util.*;
import java.net.URL;

import com.induslogic.uddi.server.util.*;
import com.induslogic.uddi.*;

/**
 * BusinessDetails.java
 * Created:july 2001
 *
 * @author rohit makin
 * @mailto rohit@induslogic.com
 * @version
 */



public class BusinessDetails
{

	Connection con;
	Get_BusinessDetail detaildoc;

	public BusinessDetails(Get_BusinessDetail detaildoc, Connection c)
	{
		this.detaildoc = detaildoc;
		this.con = c;
	}

	public UddiObject getData()
		throws SQLException, IOException, UDDIXmlException, ClassNotFoundException
	{
		String str = "";
		Statement stmt = con.createStatement();

		String detail = "<businessDetail generic=\"1.0\" xmlns=\"urn:uddi-org:api\" operator=\"www.ibm.com/services/uddi\" truncated=\"false\"></businessDetail>";
		ByteArrayInputStream bas = new ByteArrayInputStream(detail.getBytes());
		BusinessDetail businessdetail = new BusinessDetail(bas);

		Enumeration enum = detaildoc.getBusinessKey();
		while(enum.hasMoreElements())
		{
			BusinessKey key = new BusinessKey((UddiObject)enum.nextElement());
			String businesskey = key.getValue();

			BusinessEntity businessentity= new BusinessEntity(new BusinessKey(businesskey));
			int keycheck = 0;
			String  nameofbusiness = "";
			ResultSet rs = stmt.executeQuery("select * from BusinessDetails where uddi_businesskey='" + businesskey + "'");
			while(rs.next())
			{
				keycheck = keycheck + 1;
				businessentity.setAuthorizedName(rs.getString("uddi_authorizedname"));
				businessentity.setOperator(rs.getString("uddi_operator"));
				nameofbusiness = rs.getString("uddi_name");

				break;
			}

			if (keycheck == 0)
			{
				Result result = Result.getResult(10210,"Business Key " + businesskey + " passed did not match with any of the Keys present ");
				return errorOccured(result);
			}


			UddiObject obj1 = new UddiObject(UddiTags.NAME);
			obj1.setValue(nameofbusiness);
			businessentity.addElement(obj1);

			String descQuery = "SELECT * from Descriptions where uddi_key = '" + businesskey + "'";
			Statement stmt01 = con.createStatement();
			ResultSet rs01 = stmt01.executeQuery(descQuery);
			while(rs01.next())
			{
				String desc = rs01.getString("uddi_description");
				String lang = rs01.getString("uddi_lang");
				if( (lang==null)||(lang.equals(""))) {
					lang="en";
				}
				Description description = new Description( desc, lang);
				businessentity.addElement( description);
			}

			DiscoveryUrls discoveryurls = new DiscoveryUrls();
			ResultSet rs1 = stmt.executeQuery("select * from DiscoveryURLs where uddi_businesskey='" + businesskey + "'");
			while(rs1.next())
			{
				String url = rs1.getString("uddi_discoveryURL");
				DiscoveryUrl discoveryurl = new DiscoveryUrl(rs1.getString("uddi_usetype"),new URL(url));
				discoveryurls.addElement(discoveryurl);
			}
			businessentity.addElement(discoveryurls);


// service has to be resolved

			BusinessServices services = new BusinessServices();
			Statement stmt2 = con.createStatement();
			ResultSet rs9 = stmt2.executeQuery("select * from BusinessService where uddi_businesskey='" + businesskey + "'");
			while(rs9.next())
			{
				String servicekey = rs9.getString("uddi_servicekey");
				ServiceDetails servdetails = new ServiceDetails(null, con);
				BusinessService businessservice = new BusinessService(servdetails.getData(servicekey));

				services.addElement(businessservice);
			}

			businessentity.addElement(services);

			Contacts contacts = new Contacts();
			ResultSet rs6 = stmt.executeQuery("select * from Contacts where uddi_businesskey='" + businesskey + "'");
			while(rs6.next())
			{
				Contact contact = new Contact(rs6.getString("uddi_personname"));
				contact.setDescription(new Description("",""));
				String phone = rs6.getString("uddi_phone");
				if( phone!=null) {
					contact.setPhone( new ContactPhone( phone));
				}
				contact.setUseType(rs6.getString("uddi_phoneusetype"));

                contact.setEmail(new ContactEmail(rs6.getString("uddi_email")));
                ContactAddress ca = new ContactAddress();
                ContactAddressLine cal = new ContactAddressLine(rs6.getString("uddi_address"));
                ca.setAddressLine(cal);
                contact.setAddress(ca);
				contacts.addElement(contact);
			}
			businessentity.addElement(contacts);


			CategoryBag Gcategorybag = new CategoryBag();
            Statement stmt4 = con.createStatement();
			ResultSet rs5 = stmt4.executeQuery("select * from CategoryBag where uddi_key='" + businesskey + "'");
			while(rs5.next())
			{
				KeyedReference keyedreference = new KeyedReference();
				String tmodelbuskey = rs5.getString("uddi_tmodelkey");
				keyedreference .setAttribute("tModelKey",tmodelbuskey);
				keyedreference .setAttribute("keyName",rs5.getString("uddi_keyname"));
				keyedreference .setAttribute("keyValue",rs5.getString("uddi_keyvalue"));

				Gcategorybag.addElement(keyedreference);
			}
			businessentity.addElement(Gcategorybag);

			businessdetail.addElement(businessentity);

		}// e nd of the outer while loop

		return businessdetail;

	}// end of the getData() method

	public UddiObject errorOccured(Result result)
	{
		DispositionReport pr = new DispositionReport();
		pr.setResult(result);
		return pr;
	}

	public static void main(String arg[])
	{
/*		try
		{
			String getbusiness = "<get_businessDetail generic='1.0' xmlns='urn:uddi-org:api'><businessKey>87F9F360-7A8D-11D5-B360-A20D2E10987E</businessKey></get_businessDetail>";
			ByteArrayInputStream bas = new  ByteArrayInputStream(getbusiness.getBytes());
			Get_BusinessDetail find = new Get_BusinessDetail(bas);
			BusinessDetails finding = new BusinessDetails(find);

			System.out.println(" return value  " + finding.getData());

		}catch(Exception e)
		{
			e.printStackTrace();
		}
*/	}
}//end of the main class