package gov.lanl.xmltape.identifier.index.record.didl;

import java.util.Properties;

public class DidlProperties extends Properties {

    public DidlProperties() {
        loadDefaults();
    }

    public void loadDefaults() {
        this.put("profile.name", "didl");
        this.put("profile.namespace.1", "urn:mpeg:mpeg21:2002:02-DIDL-NS");
        this.put("profile.namespace.prefix.1", "didl");
        this.put("profile.namespace.2", "urn:mpeg:mpeg21:2002:01-DII-NS");
        this.put("profile.namespace.prefix.2", "dii");
        this.put("profile.namespace.3", "http://library.lanl.gov/2005-08/aDORe/DIDLextension/");
        this.put("profile.namespace.prefix.3", "diext");
        this.put("profile.record.xpath", "//didl:DIDL/@DIDLDocumentId");
        this.put("profile.datestamp.xpath", "//didl:DIDL/@diext:DIDLDocumentCreated");
        this.put("profile.field.name.0", "sourceContentId");
        this.put("profile.field.xpath.0", "//didl:DIDL/didl:Item/didl:Descriptor/didl:Statement/dii:Identifier");
        this.put("profile.field.name.1", "localContentId");
        this.put("profile.field.xpath.1", "//didl:DIDL/didl:Item/didl:Item/didl:Descriptor/didl:Statement/dii:Identifier");
        this.put("profile.field.name.2", "datastreamId");
        this.put("profile.field.xpath.2", "//didl:Component/didl:Descriptor/didl:Statement/dii:Identifier");
    }
}
