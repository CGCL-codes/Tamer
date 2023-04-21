package gov.nist.scap.xccdf.document.x1_2;

import gov.nist.checklists.xccdf.x12.BenchmarkDocument;
import gov.nist.checklists.xccdf.x12.BenchmarkDocument.Benchmark;
import gov.nist.checklists.xccdf.x12.CPE2IdrefType;
import gov.nist.checklists.xccdf.x12.GroupType;
import gov.nist.checklists.xccdf.x12.ItemType;
import gov.nist.checklists.xccdf.x12.ModelDocument;
import gov.nist.checklists.xccdf.x12.ParamType;
import gov.nist.checklists.xccdf.x12.ProfileType;
import gov.nist.checklists.xccdf.x12.RuleType;
import gov.nist.checklists.xccdf.x12.SelectableItemType;
import gov.nist.checklists.xccdf.x12.ValueType;
import gov.nist.scap.cpe.CPEResolver;
import gov.nist.scap.cpe.language.xmlbeans.XmlBeansPlatformSpecification;
import gov.nist.scap.xccdf.CircularReferenceException;
import gov.nist.scap.xccdf.ExtensionScopeException;
import gov.nist.scap.xccdf.MutableXCCDFResults;
import gov.nist.scap.xccdf.XCCDFOptions;
import gov.nist.scap.xccdf.document.AbstractXCCDFDocument;
import gov.nist.scap.xccdf.document.ApplicableSupport;
import gov.nist.scap.xccdf.document.DefaultScoringModelDeclaration;
import gov.nist.scap.xccdf.document.Group;
import gov.nist.scap.xccdf.document.ProfileSupportImpl;
import gov.nist.scap.xccdf.document.ScoringModelDeclaration;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import scap.check.content.ResultContext;
import scap.check.content.SourceContent;
import scap.check.content.SourceContext;
import scap.check.content.XmlBeansInstance;

public class XCCDFDocumentImpl extends AbstractXCCDFDocument<XCCDFDocumentImpl, ProfileImpl, ValueImpl, GroupImpl, RuleImpl> {

    private static Logger log = Logger.getLogger(XCCDFDocumentImpl.class.getName());

    private final Benchmark data;

    private final ProfileSupportImpl<ProfileImpl> profileSupport = new ProfileSupportImpl<ProfileImpl>();

    Map<ItemType, GroupType> parentMap = new HashMap<ItemType, GroupType>();

    public XCCDFDocumentImpl(SourceContext sourceContext) throws XmlException, IOException {
        super(sourceContext);
        SourceContent sourceContent = sourceContext.getSourceContent();
        XmlBeansInstance xmlBeansInstance = sourceContent.getXmlBeansInstance();
        if (xmlBeansInstance.isDocumentType()) {
            this.data = ((BenchmarkDocument) xmlBeansInstance.getXmlObject().copy()).getBenchmark();
        } else {
            this.data = (Benchmark) xmlBeansInstance.getXmlObject().copy();
        }
        log.info("Parsing checklist: " + sourceContent.getId());
        if (data.isSetPlatformSpecification()) {
            XmlBeansPlatformSpecification spec = new XmlBeansPlatformSpecification(sourceContext, data.getPlatformSpecification());
            setPlatformSpecification(spec);
        }
        for (ModelDocument.Model model : data.getModelList()) {
            addDeclaredScoringModel(XCCDFDocumentImpl.parseModel(model));
        }
        List<ProfileType> profiles = data.getProfileList();
        for (ProfileType node : profiles) {
            ProfileImpl profile = new ProfileImpl(node, this);
            profileSupport.addProfile(profile);
        }
        List<ValueType> values = data.getValueList();
        for (ValueType node : values) {
            ValueImpl value = new ValueImpl(node, this);
            addValueInternal(value);
        }
        if ((data.sizeOfRuleArray() + data.sizeOfGroupArray()) > 0) {
            XmlCursor cursor = data.newCursor();
            if (!cursor.toFirstChild()) {
                throw new RuntimeException("The XCCDF document is empty");
            }
            do {
                XmlObject xmlObject = cursor.getObject();
                if (SelectableItemType.type.isAssignableFrom(xmlObject.schemaType())) {
                    SelectableItemType item = (SelectableItemType) xmlObject;
                    if (item instanceof GroupType) {
                        GroupType node = (GroupType) item;
                        GroupImpl group = new GroupImpl(node, this);
                        addGroupInternal(group);
                    } else if (item instanceof RuleType) {
                        RuleType node = (RuleType) item;
                        RuleImpl rule = new RuleImpl(node, this);
                        addRuleInternal(rule);
                    }
                }
            } while (cursor.toNextSibling());
            cursor.dispose();
        }
    }

    public Collection<ProfileImpl> getProfiles() {
        return profileSupport.getProfiles();
    }

    public ProfileImpl lookupProfile(String id) {
        return profileSupport.getProfile(id);
    }

    public ProfileImpl lookupExtendedProfile(String id) {
        return profileSupport.getProfile(id);
    }

    public XCCDFDocumentImpl getDocument() {
        return this;
    }

    public Group getActualGroup() {
        return null;
    }

    @Override
    protected boolean isResolved() {
        return data.getResolved();
    }

    protected void resolve() throws CircularReferenceException, ExtensionScopeException {
        resolveInternal();
        data.setResolved(true);
    }

    public void write(File file) throws IOException {
        XmlOptions opts = new XmlOptions();
        opts.setSavePrettyPrint();
        data.save(file, opts);
    }

    public String getId() {
        return data.getId();
    }

    @Override
    protected XmlObject getXmlObject() {
        return data;
    }

    public GroupType getParentForItem(final ItemType item) {
        return parentMap.get(item);
    }

    static ScoringModelDeclaration parseModel(ModelDocument.Model model) {
        String systemId = model.getSystem();
        List<ParamType> paramData = model.getParamList();
        Map<String, String> parameters;
        if (paramData.isEmpty()) {
            parameters = Collections.emptyMap();
        } else {
            parameters = new HashMap<String, String>(paramData.size());
            for (ParamType param : paramData) {
                parameters.put(param.getName(), param.getStringValue());
            }
        }
        return new DefaultScoringModelDeclaration(systemId, parameters);
    }

    public boolean isApplicable(CPEResolver cpeResolver) {
        return ApplicableSupport.isApplicable(getPlatformList(), cpeResolver);
    }

    BenchmarkDocument.Benchmark getData() {
        return data;
    }

    public MutableXCCDFResults newXCCDFResults(ResultContext xccdfResultContext, XCCDFOptions options) {
        return new XCCDFResultsImpl(xccdfResultContext, this, options);
    }

    public List<String> getPlatformList() {
        List<String> retval = new ArrayList<String>();
        for (CPE2IdrefType platform : data.getPlatformList()) {
            retval.add(platform.getIdref());
        }
        return retval;
    }
}
