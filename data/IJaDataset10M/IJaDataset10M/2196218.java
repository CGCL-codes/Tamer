package com.apelon.dts.transfer;

import com.apelon.common.xml.XMLException;
import com.apelon.dts.client.attribute.DTSRoleType;
import com.apelon.dts.client.attribute.Kind;
import com.apelon.dts.transfer.KindHandler;
import java.io.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class RoleTypeHandler extends DTSSaxHandler {

    static final String HANDLER_NAME = "roleType";

    static final String NAMES = "roleTypes";

    RoleEntry roleEntry = null;

    protected static class RoleEntry {

        public String id = "";

        public String name = "";

        public String code = "";

        public String namespaceId = "";

        public Kind domainKind = null;

        public Kind rangeKind = null;

        public RoleEntry identityRoleEntry = null;

        public String type = "";
    }

    public RoleTypeHandler() {
        super();
        handlerName = HANDLER_NAME;
        resolver.addToCatalog(com.apelon.dts.dtd.common.DTD.KIND, com.apelon.dts.dtd.common.DTD.class, com.apelon.dts.dtd.common.DTD.KIND_FILE);
        resolver.addToCatalog(com.apelon.dts.dtd.common.DTD.ROLETYPE, com.apelon.dts.dtd.common.DTD.class, com.apelon.dts.dtd.common.DTD.ROLETYPE_FILE);
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        if (localName.equals(HANDLER_NAME)) {
            SaxEntry saxEntry = new SaxEntry(localName, null, START);
            saxEntry.value = roleEntry = new RoleEntry();
            stack.push(saxEntry);
            processSelf(namespaceURI, localName, qName, atts);
            return;
        }
        delegateEvent(namespaceURI, localName, qName, atts);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        super.endElement(namespaceURI, localName, qName);
        if (localName.equals(NAMES)) {
            endElements(NAMES, new DTSRoleType[0]);
        } else if (localName.equals(HANDLER_NAME)) {
            SaxEntry saxEntry = (SaxEntry) stack.pop();
            DTSRoleType identityRoleType = null;
            DTSRoleType parentRoleType = null;
            DTSObjectEntry rangeKind = null;
            DTSObjectEntry domainKind = null;
            if (saxEntry.name.equals(HANDLER_NAME)) {
                Object o = saxEntry.value;
                if (o instanceof RoleEntry) {
                    RoleEntry entry = (RoleEntry) saxEntry.value;
                    targetObject = new DTSRoleType(entry.name, Integer.parseInt(entry.id), entry.code, Integer.parseInt(entry.namespaceId));
                    saxEntry.value = targetObject;
                    saxEntry.mode = END;
                    stack.push(saxEntry);
                    return;
                } else if (o instanceof DTSRoleType) {
                    if (this.roleEntry != null) {
                        if (!this.roleEntry.type.equals("parentRoleType")) {
                            identityRoleType = (DTSRoleType) saxEntry.value;
                            saxEntry = (SaxEntry) stack.pop();
                        } else {
                            parentRoleType = (DTSRoleType) saxEntry.value;
                            saxEntry = (SaxEntry) stack.pop();
                        }
                    }
                }
            }
            Object o = saxEntry.value;
            if (o instanceof DTSRoleType) {
                identityRoleType = (DTSRoleType) saxEntry.value;
                saxEntry = (SaxEntry) stack.pop();
            }
            rangeKind = (DTSObjectEntry) saxEntry.value;
            saxEntry = (SaxEntry) stack.pop();
            domainKind = (DTSObjectEntry) saxEntry.value;
            saxEntry = (SaxEntry) stack.peek();
            RoleEntry entry = (RoleEntry) saxEntry.value;
            targetObject = new DTSRoleType(entry.name, Integer.parseInt(entry.id), entry.code, Integer.parseInt(entry.namespaceId));
            if (domainKind != null) {
                Kind dk = new Kind(domainKind.name, domainKind.id, domainKind.code, domainKind.namespaceId, Boolean.getBoolean(((KindHandler.DTSKindEntry) domainKind).reference));
                ((DTSRoleType) targetObject).setDomainKind(dk);
            }
            if (rangeKind != null) {
                Kind rk = new Kind(rangeKind.name, rangeKind.id, rangeKind.code, rangeKind.namespaceId, Boolean.getBoolean(((KindHandler.DTSKindEntry) rangeKind).reference));
                ((DTSRoleType) targetObject).setRangeKind(rk);
            }
            if (identityRoleType != null) {
                DTSRoleType rt = new DTSRoleType(identityRoleType.getName(), identityRoleType.getId(), identityRoleType.getCode(), identityRoleType.getNamespaceId());
                ((DTSRoleType) targetObject).setRightIdentity(rt);
            }
            if (parentRoleType != null) {
                DTSRoleType rt = new DTSRoleType(parentRoleType.getName(), parentRoleType.getId(), parentRoleType.getCode(), parentRoleType.getNamespaceId());
                ((DTSRoleType) targetObject).setParentRoleType(rt);
            }
            saxEntry.value = targetObject;
            saxEntry.mode = END;
        }
    }

    public static Object getObject(String content) throws XMLException {
        return getObject(content, HANDLER_NAME);
    }

    public static Object getObjects(String content) throws XMLException {
        Object obj = getObject(content, HANDLER_NAME);
        if (obj == null) {
            return new DTSRoleType[0];
        }
        return obj;
    }

    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(args[0])));
        String content = "";
        String line = null;
        while ((line = reader.readLine()) != null) {
            content += line;
        }
        System.out.println(content);
        if (args[0].endsWith("roletypes.xml")) {
            long startTime = System.currentTimeMillis();
            DTSRoleType[] values = (DTSRoleType[]) RoleTypeHandler.getObjects(content);
            long endTime = System.currentTimeMillis();
            System.out.println("Time: " + (endTime - startTime));
            for (int i = 0; i < values.length; i++) {
                System.out.println(values[i]);
            }
        } else {
            long startTime = System.currentTimeMillis();
            DTSRoleType value = (DTSRoleType) RoleTypeHandler.getObject(content);
            long endTime = System.currentTimeMillis();
            System.out.println("Time: " + (endTime - startTime));
            System.out.println(value);
        }
    }

    public void setName(String name) {
        this.roleEntry.name = name;
    }

    public void setCode(String code) {
        this.roleEntry.code = code;
    }

    public void setNamespaceId(String namespaceId) {
        this.roleEntry.namespaceId = namespaceId;
    }

    public void setId(String id) {
        this.roleEntry.id = id;
    }

    public void setIdentityRoleEntry(RoleEntry rt) {
        this.roleEntry.identityRoleEntry = rt;
    }

    public void setType(String type) {
        this.roleEntry.type = type;
    }
}
