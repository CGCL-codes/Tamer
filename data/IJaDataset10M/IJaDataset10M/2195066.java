package org.proteored.miapeapi.xml.mzml.lightParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.ac.ebi.jmzml.model.mzml.InstrumentConfiguration;

public class TXMLinstrumentConfiguration extends TXMLParamGroup {

    private TXMLcomponentList componentList;

    private TXMLNode softwareRef;

    private final String componentListTag = "componentList";

    private final String softwareRefTag = "softwareRef";

    static final String instrumentConfigurationtag = "instrumentConfiguration";

    public TXMLinstrumentConfiguration() {
        super(instrumentConfigurationtag);
        try {
            componentList = new TXMLcomponentList();
            softwareRef = new TXMLNode();
            softwareRef.createAttributeList();
            softwareRef.setStartTag(softwareRefTag);
        } catch (Exception exception) {
        }
    }

    public TXMLcomponentList getcomponentList() {
        return componentList;
    }

    public TXMLNode getsoftwareRef() {
        return softwareRef;
    }

    @Override
    public int parseNode(Node _mainNode) {
        int _count_aux = 0;
        boolean _userparam;
        boolean _refParam;
        boolean _cvparam = _userparam = _refParam = false;
        NodeList _nodeList = _mainNode.getChildNodes();
        super.addRootAttributes(_mainNode);
        int i = 0;
        _count_aux = 0;
        for (; i < _nodeList.getLength(); i++) {
            Node _labelNode = _nodeList.item(i);
            String _stringlabel = _labelNode.getNodeName();
            if (_stringlabel.compareTo(GetCVParams().getKeyTag()) == 0) _cvparam = true;
            if (_stringlabel.compareTo(GetUserParams().getKeyTag()) == 0) _userparam = true;
            if (_stringlabel.compareTo(GetReferenceableParams().getKeyTag()) == 0) _refParam = true;
            if (_stringlabel.compareTo(softwareRefTag) == 0) this.getsoftwareRef().parseNode(_labelNode);
            if (_stringlabel.compareTo(componentListTag) == 0) this.getcomponentList().parseNode(_labelNode);
        }
        if (_cvparam) _count_aux = GetCVParams().parseNode(_mainNode);
        if (_userparam) _count_aux = GetUserParams().parseNode(_mainNode);
        if (_refParam) _count_aux = GetReferenceableParams().parseNode(_mainNode);
        return _count_aux;
    }

    @Override
    public String getXMLEntry(int _index) {
        String _ret = "";
        _ret += getRootTag(getStartSection(), _index) + "\n";
        if (GetCVParams() != null) {
            _ret += GetCVParams().getXMLEntryWithoutHead(_index);
        }
        if (GetUserParams() != null) {
            _ret += GetUserParams().getXMLEntryWithoutHead(_index);
        }
        if (GetReferenceableParams() != null) {
            _ret += GetReferenceableParams().getXMLEntryWithoutHead(_index);
        }
        _ret += getcomponentList().getXMLEntry(_index + 1);
        _ret += getsoftwareRef().getXMLEntry(_index + 1);
        _ret += getEndTag(getStartSection(), _index).concat("\n");
        return _ret;
    }

    public InstrumentConfiguration translate2InstrumentConfiguration() {
        InstrumentConfiguration ret = new InstrumentConfiguration();
        String id = this.getAttributeList().getAttributeValue("id");
        if (id != null) ret.setId(id);
        String scanSettingsRef = this.getAttributeList().getAttributeValue("scanSettingsRef");
        if (scanSettingsRef != null && !scanSettingsRef.equals("")) ret.setScanSettingsRef(scanSettingsRef);
        final TXMLUserAdditionalInfo userParams = this.GetUserParams();
        for (TXMLAttributeList attributeList : userParams.getParamsList()) {
            ret.getUserParam().add(attributeList.translate2UserParam());
        }
        final TXMLUserAdditionalInfo cvParams = this.GetCVParams();
        for (TXMLAttributeList attributeList : cvParams.getParamsList()) {
            ret.getCvParam().add(attributeList.translate2CVParam());
        }
        final TXMLUserAdditionalInfo refParams = this.GetReferenceableParams();
        for (TXMLAttributeList attributeList : refParams.getParamsList()) {
            ret.getReferenceableParamGroupRef().add(attributeList.translate2RefParamGroup());
        }
        final TXMLcomponentList componentList = this.getcomponentList();
        ret.setComponentList(componentList.translate2ComponentList());
        if (getsoftwareRef() != null) ret.setSoftwareRef(this.getsoftwareRef().translate2SoftwareRef());
        return ret;
    }
}
