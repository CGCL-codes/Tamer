package com.sitescape.team.module.profile.remoting.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import com.sitescape.team.ObjectKeys;
import com.sitescape.team.domain.Principal;
import com.sitescape.team.module.file.WriteFilesException;
import com.sitescape.team.remoting.RemotingException;
import com.sitescape.team.remoting.ws.BaseService;
import com.sitescape.team.remoting.ws.util.DomInputData;
import com.sitescape.team.util.stringcheck.StringCheckUtil;

public class ProfileServiceImpl extends BaseService implements ProfileService {

    public String getAllPrincipalsAsXML(int firstRecord, int maxRecords) {
        Document doc = DocumentHelper.createDocument();
        Map options = new HashMap();
        options.put(ObjectKeys.SEARCH_OFFSET, new Integer(firstRecord));
        options.put(ObjectKeys.SEARCH_MAX_HITS, new Integer(maxRecords));
        Map results = getProfileModule().getUsers(getProfileModule().getProfileBinder().getId(), options);
        List users = (List) results.get(ObjectKeys.SEARCH_ENTRIES);
        Element rootElement = doc.addElement("principals");
        rootElement.addAttribute("first", "" + firstRecord);
        rootElement.addAttribute("count", ((Integer) results.get(ObjectKeys.TOTAL_SEARCH_RECORDS_RETURNED)).toString());
        rootElement.addAttribute("total", ((Integer) results.get(ObjectKeys.SEARCH_COUNT_TOTAL)).toString());
        for (Object searchEntry : users) {
            Map user = (Map) searchEntry;
            addPrincipalToDocument(rootElement, user);
        }
        String xml = rootElement.asXML();
        return xml;
    }

    public String getPrincipalAsXML(long binderId, long principalId) {
        Long bId = new Long(binderId);
        Long pId = new Long(principalId);
        Principal entry = getProfileModule().getEntry(bId, pId);
        Document doc = DocumentHelper.createDocument();
        Element entryElem = addPrincipalToDocument(doc, entry);
        addCustomElements(entryElem, entry);
        String xml = doc.getRootElement().asXML();
        return xml;
    }

    public long addUser(long binderId, String definitionId, String inputDataAsXML) {
        inputDataAsXML = StringCheckUtil.check(inputDataAsXML);
        Document doc = getDocument(inputDataAsXML);
        try {
            return getProfileModule().addUser(new Long(binderId), definitionId, new DomInputData(doc, getIcalModule()), null).longValue();
        } catch (WriteFilesException e) {
            throw new RemotingException(e);
        }
    }

    public long addGroup(long binderId, String definitionId, String inputDataAsXML) {
        inputDataAsXML = StringCheckUtil.check(inputDataAsXML);
        Document doc = getDocument(inputDataAsXML);
        try {
            return getProfileModule().addGroup(new Long(binderId), definitionId, new DomInputData(doc, getIcalModule()), null).longValue();
        } catch (WriteFilesException e) {
            throw new RemotingException(e);
        }
    }

    public void addUserToGroup(long userId, String username, long groupId) {
        getProfileModule().addUserToGroup(userId, username, groupId);
    }

    public void modifyPrincipal(long binderId, long principalId, String inputDataAsXML) {
        inputDataAsXML = StringCheckUtil.check(inputDataAsXML);
        Document doc = getDocument(inputDataAsXML);
        try {
            getProfileModule().modifyEntry(new Long(binderId), new Long(principalId), new DomInputData(doc, getIcalModule()));
        } catch (WriteFilesException e) {
            throw new RemotingException(e);
        }
    }

    public void deletePrincipal(long binderId, long principalId) {
        try {
            getProfileModule().deleteEntry(new Long(binderId), new Long(principalId), false);
        } catch (WriteFilesException e) {
            throw new RemotingException(e);
        }
    }
}
