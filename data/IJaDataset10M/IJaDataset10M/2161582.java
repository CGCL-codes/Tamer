package com.apelon.apps.dts.treebrowser.search.actions;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import com.apelon.apps.dts.treebrowser.search.beans.SearchBean;
import com.apelon.apps.dts.treebrowser.CookieManager;
import com.apelon.apps.dts.treebrowser.resources.BrowserUtilities;
import com.apelon.dts.client.subset.SubsetQuery;
import com.apelon.dts.client.DTSException;
import com.apelon.dts.common.subset.Subset;
import com.apelon.common.log4j.Categories;
import com.apelon.common.log4j.StackTracePrinter;

/**
 * Manages cookies and parameters for quick and 'regular' searches.
 * <p>
 *
 * @author    All source code copyright (c) 2003 Apelon, Inc.  All rights reserved.
 *
 * @version   Apelon Search Widget 1.0
 */
public class SearchActionUtilities {

    String namespace = null;

    String namespaceSubsetRadio = null;

    String subset = null;

    String exactMatch = null;

    String name = null;

    String synonyms = null;

    String[] roles = new String[0];

    String[] inverseRoles = new String[0];

    String[] properties = new String[0];

    String propertiesWordMatch = null;

    String[] silos = new String[0];

    String siloMatchType = null;

    String siloSpellChecking = null;

    String siloBestMatch = null;

    String[] subsets = new String[0];

    String[] associations = new String[0];

    String[] inverseAssociations = new String[0];

    String maxResult = null;

    boolean tooManySearchParms = false;

    boolean noSiloParms = false;

    boolean noParms = false;

    public static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;

    /**
   * Set the default cookie to be a concept name search with max results of '50'.
   * <p>
   * Also sets the max age of the cookie to be one year.
   * <p>
   *
   * @param response  adds the cookie to the response
   */
    public void addQuickDefaultCookie(HttpServletResponse response, HttpServletRequest request) {
        Cookie[] cookies;
        cookies = request.getCookies();
        int i = 0;
        if (cookies != null) {
            while (cookies.length > i) {
                String name = cookies[i].getName();
                int isSearchCookie = name.indexOf("quick");
                if (!(isSearchCookie == -1)) {
                    cookies[i].setMaxAge(1);
                    cookies[i].setPath("/");
                    response.addCookie(cookies[i]);
                }
                i++;
            }
        }
        String contents = "<em>true</em><n>true</n><ns>All Namespaces</ns><nsubr>namespace</nsubr><mr>50</mr>";
        Cookie userCookie = new Cookie(CookieManager.QUICK_SEARCH_COOKIE_NAME, contents);
        userCookie.setMaxAge(SECONDS_PER_YEAR);
        userCookie.setPath("/");
        response.addCookie(userCookie);
    }

    /**
   * Get the search configuration parameters, encode in XML and save in the contents of the cookie.
   * <p>
   *
   * @param request   gets the search configuration parameters from the request
   * @param response  adds the cookie to the response
   *
   * @return tooManySearchParms boolean indicates if there are more than 5 search parameters
   */
    public boolean addQuickCustomizedCookie(HttpServletRequest request, HttpServletResponse response) {
        if (!tooManySearchParms) {
            String contents = null;
            StringBuffer sb = new StringBuffer();
            int i = 0;
            sb.append("<ns>" + BrowserUtilities.encodeUrl(namespace) + "</ns>");
            sb.append("<nsubr>" + namespaceSubsetRadio + "</nsubr>");
            sb.append("<sub>" + BrowserUtilities.encodeUrl(subset) + "</sub>");
            sb.append("<em>" + exactMatch + "</em>");
            sb.append("<n>" + name + "</n>");
            sb.append("<sy>" + synonyms + "</sy>");
            i = 0;
            if (!(roles == null)) {
                while (roles.length > i) {
                    sb.append("<r" + (i + 1) + ">").append(roles[i]).append("</r" + (i + 1) + ">");
                    i++;
                }
            }
            i = 0;
            if (!(inverseRoles == null)) {
                while (inverseRoles.length > i) {
                    sb.append("<ir" + (i + 1) + ">").append(inverseRoles[i]).append("</ir" + (i + 1) + ">");
                    i++;
                }
            }
            i = 0;
            if (!(properties == null)) {
                while (properties.length > i) {
                    sb.append("<p" + (i + 1) + ">").append(properties[i]).append("</p" + (i + 1) + ">");
                    i++;
                }
            }
            sb.append("<pm>" + propertiesWordMatch + "</pm>");
            i = 0;
            if (!(silos == null)) {
                while (silos.length > i) {
                    sb.append("<s" + (i + 1) + ">").append(silos[i]).append("</s" + (i + 1) + ">");
                    i++;
                }
            }
            sb.append("<sm>" + siloMatchType + "</sm>");
            sb.append("<ssc>" + siloSpellChecking + "</ssc>");
            sb.append("<sbm>" + siloBestMatch + "</sbm>");
            if (!(subsets == null)) {
                while (subsets.length > i) {
                    sb.append("<sub" + (i + 1) + ">").append(subsets[i]).append("</sub" + (i + 1) + ">");
                    i++;
                }
            }
            i = 0;
            if (!(associations == null)) {
                while (associations.length > i) {
                    sb.append("<a" + (i + 1) + ">").append(associations[i]).append("</a" + (i + 1) + ">");
                    i++;
                }
            }
            i = 0;
            if (!(inverseAssociations == null)) {
                while (inverseAssociations.length > i) {
                    sb.append("<ia" + (i + 1) + ">").append(inverseAssociations[i]).append("</ia" + (i + 1) + ">");
                    i++;
                }
            }
            sb.append("<mr>" + maxResult + "</mr>");
            contents = sb.toString();
            Cookie userCookie = new Cookie(CookieManager.QUICK_SEARCH_COOKIE_NAME, contents);
            userCookie.setMaxAge(SECONDS_PER_YEAR);
            userCookie.setPath("/");
            response.addCookie(userCookie);
        }
        return tooManySearchParms;
    }

    public void addSearchBean(HttpServletRequest request) {
        if (!tooManySearchParms) {
            HttpSession session = request.getSession();
            SearchBean searchEntity = new SearchBean();
            Vector rolesVec = new Vector(0);
            Vector invRolesVec = new Vector(0);
            Vector propsVec = new Vector(0);
            Vector silosVec = new Vector(0);
            Vector subsetsVec = new Vector(0);
            Vector assocsVec = new Vector(0);
            Vector invAssocsVec = new Vector(0);
            searchEntity.setNamespace(namespace);
            searchEntity.setSearchInRadio(namespaceSubsetRadio);
            searchEntity.setSubset(subset);
            searchEntity.setExactMatch(exactMatch + "");
            searchEntity.setName(name + "");
            searchEntity.setSynonyms(synonyms + "");
            if (roles != null) {
                for (int i = 0; i < roles.length; i++) {
                    rolesVec.addElement(roles[i]);
                }
            }
            searchEntity.setRoles(rolesVec);
            if (inverseRoles != null) {
                for (int i = 0; i < inverseRoles.length; i++) {
                    invRolesVec.addElement(inverseRoles[i]);
                }
            }
            searchEntity.setInverseRoles(invRolesVec);
            if (properties != null) {
                for (int i = 0; i < properties.length; i++) {
                    propsVec.addElement(properties[i]);
                }
            }
            searchEntity.setProperties(propsVec);
            searchEntity.setPropertiesWordMatch(propertiesWordMatch + "");
            if (associations != null) {
                for (int i = 0; i < associations.length; i++) {
                    assocsVec.addElement(associations[i]);
                }
            }
            searchEntity.setAssociations(assocsVec);
            if (inverseAssociations != null) {
                for (int i = 0; i < inverseAssociations.length; i++) {
                    invAssocsVec.addElement(inverseAssociations[i]);
                }
            }
            searchEntity.setInverseAssociations(invAssocsVec);
            if (silos != null) {
                for (int i = 0; i < silos.length; i++) {
                    silosVec.addElement(silos[i]);
                }
            }
            searchEntity.setSilos(silosVec);
            searchEntity.setSiloMatchType(siloMatchType + "");
            searchEntity.setSiloSpellChecking(siloSpellChecking + "");
            searchEntity.setSiloBestMatch(siloBestMatch + "");
            if (subsets != null) {
                for (int i = 0; i < subsets.length; i++) {
                    subsetsVec.addElement(subsets[i]);
                }
            }
            searchEntity.setSubsets(subsetsVec);
            searchEntity.setMaxResult(maxResult);
            session.setAttribute("searchEntity", searchEntity);
        }
    }

    /**
   * Gets the search configuration parameters from the request object and
   * stores in instance variables.
   * <p>
   *
   * @param request gets the search configuration parameters from the request
   */
    public void getQuickCustomizedValues(HttpServletRequest request) {
        namespace = BrowserUtilities.decodeUrl(request.getParameter("namespace"));
        namespaceSubsetRadio = request.getParameter("namespace_subset_radio");
        subset = BrowserUtilities.decodeUrl(request.getParameter("subset"));
        exactMatch = request.getParameter("exact_match");
        name = request.getParameter("name");
        synonyms = request.getParameter("synonyms");
        roles = request.getParameterValues("roles");
        inverseRoles = request.getParameterValues("inverse_roles");
        properties = request.getParameterValues("properties");
        propertiesWordMatch = request.getParameter("word_match");
        silos = request.getParameterValues("silos");
        siloMatchType = request.getParameter("match_type");
        siloSpellChecking = request.getParameter("spell_check");
        siloBestMatch = request.getParameter("best_match");
        associations = request.getParameterValues("associations");
        inverseAssociations = request.getParameterValues("inverse_associations");
        maxResult = request.getParameter("max_results");
        if (maxResult.equals("Enter a number")) {
            maxResult = request.getParameter("entered_max_results");
        }
        checkForTooManySearchParms();
    }

    /**
   * Gets the cookie from the request, decodes search configurations from XML
   * and saves in the SearchBean.
   * <p>
   *
   * @param request       gets the cookie from the request
   * @param searchEntity  SearchBean used to store search configuration variables
   */
    public void getQuickCustomizedCookieValues(HttpServletRequest request, SearchBean searchEntity) {
        String str = null;
        Vector roles = new Vector(0);
        Vector inverseRoles = new Vector(0);
        Vector properties = new Vector(0);
        Vector silos = new Vector(0);
        Vector associations = new Vector(0);
        Vector inverseAssociations = new Vector(0);
        Cookie[] cookies = request.getCookies();
        int i = 0;
        if (cookies != null) {
            while (cookies.length > i) {
                if (cookies[i].getName().equals(CookieManager.QUICK_SEARCH_COOKIE_NAME)) {
                    str = cookies[i].getValue();
                }
                i++;
            }
        }
        if (!(str.indexOf("<ns>") == -1)) {
            searchEntity.setNamespace(BrowserUtilities.decodeUrl(str.substring((str.indexOf("<ns>") + 4), str.indexOf("</ns>"))));
        }
        if (!(str.indexOf("<nsubr>") == -1)) {
            searchEntity.setSearchInRadio(str.substring((str.indexOf("<nsubr>") + 7), str.indexOf("</nsubr>")));
        }
        if (!(str.indexOf("<sub>") == -1)) {
            searchEntity.setSubset(BrowserUtilities.decodeUrl(str.substring((str.indexOf("<sub>") + 5), str.indexOf("</sub>"))));
        }
        if (!(str.indexOf("<em>") == -1)) {
            searchEntity.setExactMatch(str.substring((str.indexOf("<em>") + 4), str.indexOf("</em>")));
        }
        if (!(str.indexOf("<n>") == -1)) {
            searchEntity.setName(str.substring((str.indexOf("<n>") + 3), str.indexOf("</n>")));
        }
        if (!(str.indexOf("<sy>") == -1)) {
            searchEntity.setSynonyms(str.substring((str.indexOf("<sy>") + 4), str.indexOf("</sy>")));
        }
        i = 0;
        if (!(str.indexOf("<r" + (i + 1) + ">") == -1)) {
            while (!(str.indexOf("<r" + (i + 1) + ">") == -1)) {
                roles.add(str.substring(str.indexOf("<r" + (i + 1) + ">") + (3 + new Integer(i + 1).toString().length()), str.indexOf("</r" + (i + 1) + ">")));
                i++;
            }
        }
        searchEntity.setRoles(roles);
        i = 0;
        if (!(str.indexOf("<ir" + (i + 1) + ">") == -1)) {
            while (!(str.indexOf("<ir" + (i + 1) + ">") == -1)) {
                inverseRoles.add(str.substring(str.indexOf("<ir" + (i + 1) + ">") + (4 + new Integer(i + 1).toString().length()), str.indexOf("</ir" + (i + 1) + ">")));
                i++;
            }
        }
        searchEntity.setInverseRoles(inverseRoles);
        i = 0;
        if (!(str.indexOf("<p" + (i + 1) + ">") == -1)) {
            while (!(str.indexOf("<p" + (i + 1) + ">") == -1)) {
                properties.add(str.substring(str.indexOf("<p" + (i + 1) + ">") + (3 + new Integer(i + 1).toString().length()), str.indexOf("</p" + (i + 1) + ">")));
                i++;
            }
        }
        searchEntity.setProperties(properties);
        if (!(str.indexOf("<pm>") == -1)) {
            searchEntity.setPropertiesWordMatch(str.substring((str.indexOf("<pm>") + 4), str.indexOf("</pm>")));
        }
        i = 0;
        if (!(str.indexOf("<s" + (i + 1) + ">") == -1)) {
            while (!(str.indexOf("<s" + (i + 1) + ">") == -1)) {
                silos.add(str.substring(str.indexOf("<s" + (i + 1) + ">") + (3 + new Integer(i + 1).toString().length()), str.indexOf("</s" + (i + 1) + ">")));
                i++;
            }
        }
        searchEntity.setSilos(silos);
        if (!(str.indexOf("<sm>") == -1)) {
            searchEntity.setSiloMatchType(str.substring((str.indexOf("<sm>") + 4), str.indexOf("</sm>")));
        }
        if (!(str.indexOf("<ssc>") == -1)) {
            searchEntity.setSiloSpellChecking(str.substring((str.indexOf("<ssc>") + 5), str.indexOf("</ssc>")));
        }
        if (!(str.indexOf("<sbm>") == -1)) {
            searchEntity.setSiloBestMatch(str.substring((str.indexOf("<sbm>") + 5), str.indexOf("</sbm>")));
        }
        i = 0;
        if (!(str.indexOf("<a" + (i + 1) + ">") == -1)) {
            while (!(str.indexOf("<a" + (i + 1) + ">") == -1)) {
                associations.add(str.substring(str.indexOf("<a" + (i + 1) + ">") + (3 + new Integer(i + 1).toString().length()), str.indexOf("</a" + (i + 1) + ">")));
                i++;
            }
        }
        searchEntity.setAssociations(associations);
        i = 0;
        if (!(str.indexOf("<ia" + (i + 1) + ">") == -1)) {
            while (!(str.indexOf("<ia" + (i + 1) + ">") == -1)) {
                inverseAssociations.add(str.substring(str.indexOf("<ia" + (i + 1) + ">") + (4 + new Integer(i + 1).toString().length()), str.indexOf("</ia" + (i + 1) + ">")));
                i++;
            }
        }
        searchEntity.setInverseAssociations(inverseAssociations);
        if (!(str.indexOf("<mr>") == -1)) {
            searchEntity.setMaxResult(str.substring((str.indexOf("<mr>") + 4), str.indexOf("</mr>")));
        }
    }

    /**
   * Gets the default search configuration information from the request,
   * and saves in the SearchBean.
   * <p>
   *
   * @param request       gets the default search configuration bean from the request
   * @param searchEntity  SearchBean used to store search configuration variables
   */
    public void getQuickCustomizedDefaultConfigValues(HttpServletRequest request, SearchBean searchEntity) {
        String str = null;
        Vector roles = new Vector(0);
        Vector inverseRoles = new Vector(0);
        Vector properties = new Vector(0);
        Vector silos = new Vector(0);
        Vector associations = new Vector(0);
        Vector inverseAssociations = new Vector(0);
        HttpSession session = request.getSession();
        SubsetQuery subsetQuery = (SubsetQuery) session.getAttribute("subsetQuery");
        Properties props = (Properties) session.getAttribute("configProperties");
        String searchin = props.getProperty("searchin");
        if (searchin != null && !searchin.equals("")) {
            searchEntity.setSearchInRadio(searchin);
        }
        String subsetname = props.getProperty("subset");
        if (subsetname != null && !subsetname.equals("")) {
            try {
                Subset subset = subsetQuery.fetch(subsetname);
                if (subset == null) {
                    Categories.dataServer().error("The subset specified in the dtsbrowserconfig.xml" + "  can't be found in database.");
                    return;
                } else {
                    searchEntity.setSubset(Integer.toString(subset.getId()));
                }
            } catch (DTSException e) {
                Categories.dataServer().error(StackTracePrinter.getStackTrace(e));
            }
        }
        String namespace = props.getProperty("namespace");
        if (namespace != null && !namespace.equals("")) {
            if (namespace.equalsIgnoreCase("all")) {
                namespace = "All Namespaces";
            }
        } else {
            namespace = "All Namespaces";
        }
        searchEntity.setNamespace(namespace);
        String exactMatch = props.getProperty("exactmatch");
        if (exactMatch != null && !exactMatch.equals("")) {
            exactMatch = getValidBooleanStr(exactMatch);
            searchEntity.setExactMatch(exactMatch);
        }
        String conceptName = props.getProperty("conceptname");
        if (conceptName != null && !conceptName.equals("")) {
            conceptName = getValidBooleanStr(conceptName);
            searchEntity.setName(conceptName);
        }
        String synTerms = props.getProperty("synonymousterms");
        if (synTerms != null && !synTerms.equals("")) {
            synTerms = getValidBooleanStr(synTerms);
            searchEntity.setSynonyms(synTerms);
        }
        int i = 0;
        int count = 0;
        String roleValue = props.getProperty("role" + (i + 1));
        while (roleValue != null && !roleValue.equals("")) {
            if (count < 5) {
                roles.add(roleValue);
                count++;
            }
            i++;
            roleValue = props.getProperty("role" + (i + 1));
        }
        searchEntity.setRoles(roles);
        i = 0;
        String invRoleValue = props.getProperty("inverserole" + (i + 1));
        while (invRoleValue != null && !invRoleValue.equals("")) {
            if (count < 5) {
                inverseRoles.add(invRoleValue);
                count++;
            }
            i++;
            invRoleValue = props.getProperty("inverserole" + (i + 1));
        }
        searchEntity.setInverseRoles(inverseRoles);
        i = 0;
        String propValue = props.getProperty("property" + (i + 1));
        while (propValue != null && !propValue.equals("")) {
            if (count < 5) {
                properties.add(propValue);
                count++;
            }
            i++;
            propValue = props.getProperty("property" + (i + 1));
        }
        searchEntity.setProperties(properties);
        String wordMatch = props.getProperty("wordmatch");
        if (wordMatch != null && !wordMatch.equals("")) {
            wordMatch = getValidBooleanStr(wordMatch);
            searchEntity.setPropertiesWordMatch(wordMatch);
        }
        i = 0;
        String assocValue = props.getProperty("conceptassociation" + (i + 1));
        while (assocValue != null && !assocValue.equals("")) {
            if (count < 5) {
                associations.add(assocValue);
                count++;
            }
            i++;
            assocValue = props.getProperty("conceptassociation" + (i + 1));
        }
        searchEntity.setAssociations(associations);
        i = 0;
        String invAssocValue = props.getProperty("inverseconceptassociation" + (i + 1));
        while (invAssocValue != null && !invAssocValue.equals("")) {
            if (count < 5) {
                inverseAssociations.add(invAssocValue);
                count++;
            }
            i++;
            invAssocValue = props.getProperty("inverseconceptassociation" + (i + 1));
        }
        searchEntity.setInverseAssociations(inverseAssociations);
        i = 0;
        String siloValue = props.getProperty("silo" + (i + 1));
        while (siloValue != null && !siloValue.equals("")) {
            silos.add(siloValue);
            i++;
            siloValue = props.getProperty("silo" + (i + 1));
        }
        searchEntity.setSilos(silos);
        String siloMatch = props.getProperty("silomatch");
        if (siloMatch != null && !siloMatch.equals("")) {
            siloMatch = getSiloMatchStr(siloMatch);
            searchEntity.setSiloMatchType(siloMatch);
        }
        String spellCheck = props.getProperty("spellcheck");
        if (spellCheck != null && !spellCheck.equals("")) {
            spellCheck = getValidBooleanStr(spellCheck);
            searchEntity.setSiloSpellChecking(spellCheck);
        }
        String bestMatch = props.getProperty("bestmatchonly");
        if (bestMatch != null && !bestMatch.equals("")) {
            bestMatch = getValidBooleanStr(bestMatch);
            searchEntity.setSiloBestMatch(bestMatch);
        }
        String maxResult = props.getProperty("maxresults");
        maxResult = getValidMaxResult(maxResult);
        searchEntity.setMaxResult(maxResult);
    }

    private String getValidBooleanStr(String bool) {
        if (bool.equalsIgnoreCase("true")) {
            bool = "true";
        } else {
            bool = "false";
        }
        return bool;
    }

    private String getSiloMatchStr(String siloMatch) {
        String str = null;
        if (siloMatch.equals("1")) {
            str = "complete_match";
        } else if (siloMatch.equals("2")) {
            str = "under_match";
        } else {
            str = "partial_match";
        }
        return str;
    }

    private String getValidMaxResult(String maxResult) {
        String max = null;
        if (maxResult == null || maxResult.equals("")) {
            max = "50";
            return max;
        }
        try {
            int maxInt = Integer.parseInt(maxResult);
            max = maxResult;
        } catch (NumberFormatException ex) {
            max = "50";
        }
        return max;
    }

    /**
   * Gets the search configuration parameters and saves them in the SearchBean.
   * <p>
   *
   * @param request       gets the search configuration parameters from the request
   * @param searchEntity  SearchBean used to store search configuration variables
   */
    public boolean getAdvancedValues(HttpServletRequest request, SearchBean searchEntity) {
        Vector synonyms = new Vector(0);
        Vector roles = new Vector(0);
        Vector inverseRoles = new Vector(0);
        Vector properties = new Vector(0);
        Vector associations = new Vector(0);
        Vector inverseAssociations = new Vector(0);
        Vector silos = new Vector(0);
        int i = 0;
        String selectType = request.getParameter("advanced_select_type");
        searchEntity.setSelectType(selectType);
        searchEntity.setTerm(request.getParameter("searchTerm"));
        searchEntity.setSearchInRadio(request.getParameter("search_in_radio"));
        searchEntity.setNamespace(BrowserUtilities.decodeUrl(request.getParameter("advanced_select_namespace")));
        searchEntity.setExactMatch(request.getParameter("exact_match"));
        if (!(request.getParameter("max_results").equals(""))) {
            searchEntity.setMaxResult(request.getParameter("max_results"));
        } else if (!(request.getParameter("entered_max_results").equals(""))) {
            searchEntity.setMaxResult(request.getParameter("entered_max_results"));
        } else if (request.getParameter("entered_max_results").equals("")) {
            searchEntity.setMaxResult("50");
        }
        if (selectType.equals("name")) {
            searchEntity.setName("true");
        }
        if (selectType.equals("nameSynonyms")) {
            searchEntity.setNameAndSynonyms("true");
        }
        if (selectType.equals("name") && !(request.getParameter("root_namespace") == null)) {
            searchEntity.setAssociationNamespace(request.getParameter("association_namespace"));
            searchEntity.setAssociationType(request.getParameter("association_type"));
            searchEntity.setRootNamespace(request.getParameter("root_namespace"));
        } else if (selectType.equals("synonyms")) {
            searchEntity.setSynonyms("true");
        } else if (selectType.equals("roles") && !(request.getParameterValues("advanced_search_parms") == null)) {
            i = 0;
            while (request.getParameterValues("advanced_search_parms").length > i) {
                roles.add(request.getParameterValues("advanced_search_parms")[i]);
                i++;
            }
            searchEntity.setRoles(roles);
        } else if (selectType.equals("inverse_roles") && !(request.getParameterValues("advanced_search_parms") == null)) {
            i = 0;
            while (request.getParameterValues("advanced_search_parms").length > i) {
                inverseRoles.add(request.getParameterValues("advanced_search_parms")[i]);
                i++;
            }
            searchEntity.setInverseRoles(inverseRoles);
        } else if (selectType.equals("properties") && !(request.getParameterValues("advanced_search_parms") == null)) {
            i = 0;
            while (request.getParameterValues("advanced_search_parms").length > i) {
                properties.add(request.getParameterValues("advanced_search_parms")[i]);
                i++;
            }
            searchEntity.setProperties(properties);
            searchEntity.setPropertiesWordMatch(request.getParameter("word_match"));
        } else if (selectType.equals("associations") && !(request.getParameterValues("advanced_search_parms") == null)) {
            i = 0;
            while (request.getParameterValues("advanced_search_parms").length > i) {
                associations.add(request.getParameterValues("advanced_search_parms")[i]);
                i++;
            }
            searchEntity.setAssociations(associations);
        } else if (selectType.equals("inverse_associations") && !(request.getParameterValues("advanced_search_parms") == null)) {
            i = 0;
            while (request.getParameterValues("advanced_search_parms").length > i) {
                inverseAssociations.add(request.getParameterValues("advanced_search_parms")[i]);
                i++;
            }
            searchEntity.setInverseAssociations(inverseAssociations);
        } else if (!(request.getParameterValues("silos") == null)) {
            i = 0;
            while (request.getParameterValues("silos").length > i) {
                silos.add(request.getParameterValues("silos")[i]);
                i++;
            }
            searchEntity.setSilos(silos);
            searchEntity.setSiloMatchType(request.getParameter("match_type"));
            searchEntity.setSiloBestMatch(request.getParameter("best_match"));
            searchEntity.setSiloSpellChecking(request.getParameter("spell_check"));
        }
        if (selectType.equals("roles") && roles.size() == 1) {
            noParms = true;
        }
        if (selectType.equals("inverse_roles") && inverseRoles.size() == 1) {
            noParms = true;
        }
        if (selectType.equals("properties") && properties.size() == 1) {
            noParms = true;
        }
        if (selectType.equals("associations") && associations.size() == 1) {
            noParms = true;
        }
        if (selectType.equals("inverse_associations") && inverseAssociations.size() == 1) {
            noParms = true;
        }
        if (selectType.equals("silos") && silos.size() == 1) {
            noParms = true;
        }
        return noParms;
    }

    public void checkForTooManySearchParms() {
        int count = 0;
        if (!(roles == null)) {
            count += roles.length;
        }
        if (!(inverseRoles == null)) {
            count += inverseRoles.length;
        }
        if (!(properties == null)) {
            count += properties.length;
        }
        if (!(associations == null)) {
            count += associations.length;
        }
        if (!(inverseAssociations == null)) {
            count += inverseAssociations.length;
        }
        if (count > 5) {
            tooManySearchParms = true;
        }
    }

    public boolean checkForNoSiloParms() {
        if (!(silos == null) && siloMatchType == null && siloSpellChecking == null && siloBestMatch == null) {
            if (!(silos[0].equals("select silo... "))) {
                noSiloParms = true;
            }
        }
        return noSiloParms;
    }

    public boolean checkForQuickParms() {
        if (roles == null && inverseRoles == null && properties == null && silos == null && associations == null && inverseAssociations == null && name == null && synonyms == null) {
            noParms = true;
        }
        return noParms;
    }
}
