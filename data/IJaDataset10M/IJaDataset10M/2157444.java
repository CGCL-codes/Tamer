package com.ocsico.hmng.server.ldap;

import java.util.ArrayList;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import com.ocsico.hmng.shared.BeanFromLdap;

/**
 * @author Solo
 * 
 */
public class ResponseDataFromDomain {

    public static String ldapUri = "";

    public static String usersContainer = "";

    public static String username = "";

    public static String password = "";

    private static String test;

    private static boolean flagLDAP = false;

    /**
   * 
   * @param userName
   *          - login
   * @param pass
   *          - password
   * @param urlUsersContainer
   *          - адрес 'папки' пользователя
   */
    public static void responseData(BeanFromLdap bean, String urlUsersContainer) {
        ArrayList<Attribute> listAttributeFromLDAP = new ArrayList<Attribute>();
        ArrayList<String> listAttributeValues = new ArrayList<String>();
        ldapUri = "ldap://10.128.1.11:389/";
        usersContainer = "CN=Java Test Login,OU=users,OU=Technical,OU=Accounts,DC=ocsico,DC=local";
        username = bean.getLogin();
        password = bean.getPassword();
        test = "sAMAccountName=" + username;
        Properties env = new Properties();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, ldapUri);
        env.put(Context.SECURITY_PRINCIPAL, usersContainer);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.SECURITY_PROTOCOL, "GSS-API");
        env.put("java.naming.ldap.version", "3");
        usersContainer = urlUsersContainer;
        try {
            DirContext ctx = new InitialDirContext(env);
            SearchControls ctls = new SearchControls();
            String[] attrIDs = new String[bean.getListNames().size()];
            for (int i = 0; i < bean.getListNames().size(); i++) {
                attrIDs[i] = bean.getListNames().get(i);
            }
            ctls.setReturningAttributes(attrIDs);
            ctls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            NamingEnumeration answer = ctx.search(urlUsersContainer, test, ctls);
            System.out.println("eeee");
            while (answer.hasMore()) {
                System.out.println("answer");
                SearchResult rslt = (SearchResult) answer.next();
                Attributes attrs = rslt.getAttributes();
                System.out.println("\n-------------" + attrs.get("name") + "------------\n");
                for (int i = 0; i < bean.getListNames().size(); i++) {
                    listAttributeFromLDAP.add(attrs.get(bean.getListNames().get(i)));
                }
                bean.setFlagLDAP(true);
                listAttributeValues = conversionData(listAttributeFromLDAP, bean);
                toBean(bean, listAttributeValues);
            }
            ctx.close();
        } catch (NamingException e) {
            System.out.println("exeption in LDAP");
            throw new RuntimeException(e);
        }
    }

    private static void showString(ArrayList<String> list) {
        System.out.println("++++++++++showString()+++++++++++++");
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + " - " + list.get(i));
        }
    }

    private static ArrayList<String> conversionData(ArrayList<Attribute> listDataFromLDAP, BeanFromLdap bean) {
        ArrayList<String> list = new ArrayList<String>();
        String attributeToString = "";
        for (int i = 0; i < listDataFromLDAP.size(); i++) {
            String str = bean.getListNames().get(i) + ": ";
            attributeToString = listDataFromLDAP.get(i).toString();
            attributeToString = attributeToString.replaceAll(str, "");
            list.add(attributeToString);
        }
        return list;
    }

    private static void toBean(BeanFromLdap bean, ArrayList<String> listAttributeValues) {
        bean.setCn(listAttributeValues.get(0));
        bean.setSn(listAttributeValues.get(1));
        bean.setL(listAttributeValues.get(2));
        bean.setDescription(listAttributeValues.get(3));
        bean.setPhysicalDeliveryOfficeName(listAttributeValues.get(4));
        bean.setDistinguishedName(listAttributeValues.get(5));
        bean.setDepartment(listAttributeValues.get(6));
        bean.setName(listAttributeValues.get(7));
        bean.setsAMAccountName(listAttributeValues.get(8));
        bean.setUserPrincipalName(listAttributeValues.get(9));
        bean.setObjectCategory(listAttributeValues.get(10));
        bean.setMail(listAttributeValues.get(11));
        bean.setMobile(listAttributeValues.get(12));
    }
}
