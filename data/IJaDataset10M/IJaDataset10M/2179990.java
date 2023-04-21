package org.kablink.teaming.taglib;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.kablink.teaming.ObjectKeys;
import org.kablink.teaming.context.request.RequestContextHolder;
import org.kablink.teaming.domain.DefinableEntity;
import org.kablink.teaming.domain.Definition;
import org.kablink.teaming.module.definition.DefinitionConfigurationBuilder;
import org.kablink.teaming.module.definition.DefinitionUtils;
import org.kablink.teaming.module.profile.ProfileModule;
import org.kablink.teaming.util.DirPath;
import org.kablink.teaming.util.NLT;
import org.kablink.teaming.util.ResolveIds;
import org.kablink.teaming.util.SZoneConfig;
import org.kablink.teaming.util.SpringContextUtil;
import org.kablink.teaming.util.Utils;
import org.kablink.teaming.web.WebKeys;
import org.kablink.teaming.web.util.DefinitionHelper;
import org.kablink.util.Validator;
import org.kablink.util.search.Constants;
import org.kablink.util.servlet.DynamicServletRequest;
import org.kablink.util.servlet.StringServletResponse;

/**
 * @author Peter Hurley
 *
 */
public class DisplayConfiguration extends BodyTagSupport implements ParamAncestorTag {

    private static final long serialVersionUID = 1L;

    private Document configDefinition;

    private Element configElement;

    private String configJspStyle;

    private boolean processThisItem = false;

    private DefinableEntity entry;

    private Map entryMap;

    private Map _params;

    private String DEFAULT_JSP_BASE = "/WEB-INF/jsp/custom_jsps/";

    private String DEFAULT_EXT_BASE = "/WEB-INF/ext/";

    private ProfileModule profileModule;

    public int doStartTag() {
        return EVAL_BODY_BUFFERED;
    }

    public int doAfterBody() {
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        profileModule = (ProfileModule) SpringContextUtil.getBean("profileModule");
        DefinitionConfigurationBuilder configBuilder = DefinitionHelper.getDefinitionBuilderConfig();
        try {
            HttpServletRequest httpReq = (HttpServletRequest) pageContext.getRequest();
            HttpServletResponse httpRes = (HttpServletResponse) pageContext.getResponse();
            if (this.configDefinition == null) {
                throw new JspException("No configuration definition available for this item.");
            } else if (this.configElement != null) {
                List<Element> itemList;
                if (processThisItem == true) {
                    itemList = new ArrayList();
                    itemList.add(this.configElement);
                } else {
                    itemList = this.configElement.elements("item");
                }
                if (itemList != null) {
                    String extensionName = configElement.getDocument().getRootElement().attributeValue(ObjectKeys.XTAG_ATTRIBUTE_EXTENSION);
                    String jspBase = null;
                    if (Validator.isNotNull(extensionName)) jspBase = DEFAULT_EXT_BASE + Utils.getZoneKey() + File.separator + extensionName + File.separator + "jsp" + File.separator;
                    for (Element nextItem : itemList) {
                        String itemType = nextItem.attributeValue("name", "");
                        String formItem = nextItem.attributeValue("formItem", "");
                        String customJsp = null;
                        Boolean fieldModificationAllowed = Boolean.FALSE;
                        String s_fieldModificationAllowed = DefinitionUtils.getPropertyValue(nextItem, "fieldModificationAllowed");
                        if (s_fieldModificationAllowed != null && "true".equals(s_fieldModificationAllowed)) fieldModificationAllowed = true;
                        Boolean inherit = Boolean.FALSE;
                        Boolean perUserVersionsAllowed = Boolean.FALSE;
                        Element jspEle = (Element) nextItem.selectSingleNode("./jsps/jsp[@name='custom']");
                        if (jspEle != null) {
                            String jspName = jspEle.attributeValue("value");
                            if ("true".equals(jspEle.attributeValue("inherit"))) inherit = Boolean.TRUE;
                            if (!inherit && Validator.isNotNull(jspName)) {
                                if (Validator.isNotNull(jspBase)) customJsp = jspBase + jspName; else customJsp = DEFAULT_JSP_BASE + jspName;
                            }
                        }
                        Element itemDefinition = configBuilder.getItem(configDefinition, itemType);
                        if (itemDefinition != null) {
                            String jspName;
                            String defaultJsp = configBuilder.getItemJspByStyle(itemDefinition, itemType, this.configJspStyle);
                            if (itemType.equals("customJsp")) {
                                jspName = DefinitionUtils.getPropertyValue(nextItem, "formJsp");
                                if (Validator.isNotNull(jspName)) customJsp = DEFAULT_JSP_BASE + jspName;
                            } else if (customJsp == null && "dataView".equals(nextItem.attributeValue("type")) && (inherit || formItem.equals("customJsp"))) {
                                Element entryFormItem = (Element) configDefinition.getRootElement().selectSingleNode("item[@type='form']");
                                if (entryFormItem != null) {
                                    String nameValue = DefinitionUtils.getPropertyValue(nextItem, "name");
                                    if (Validator.isNotNull(nameValue)) {
                                        Element itemEle = (Element) entryFormItem.selectSingleNode(".//item/properties/property[@name='name' and @value='" + nameValue + "']");
                                        if (itemEle != null) {
                                            itemEle = itemEle.getParent().getParent();
                                            if (formItem.equals("customJsp")) {
                                                String jspType = "viewJsp";
                                                if (configJspStyle.equals(Definition.JSP_STYLE_MOBILE)) jspType = "mobileJsp";
                                                jspName = DefinitionUtils.getPropertyValue(nextItem, jspType);
                                                if (Validator.isNotNull(jspName)) customJsp = DEFAULT_JSP_BASE + jspName;
                                            }
                                            if (Validator.isNull(customJsp) && inherit) {
                                                jspEle = (Element) itemEle.selectSingleNode("./jsps/jsp[@name='custom']");
                                                if (jspEle != null) {
                                                    jspName = jspEle.attributeValue("value");
                                                    if (Validator.isNotNull(jspName)) customJsp = DEFAULT_JSP_BASE + jspName;
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (itemType.equals("customJspView")) {
                                String jspType = "viewJsp";
                                if (configJspStyle.equals(Definition.JSP_STYLE_MOBILE)) jspType = "mobileJsp";
                                jspName = DefinitionUtils.getPropertyValue(nextItem, jspType);
                                if (Validator.isNotNull(jspName)) customJsp = DEFAULT_JSP_BASE + jspName;
                            }
                            if ("dataView".equals(nextItem.attributeValue("type"))) {
                                Element entryFormItem = (Element) configDefinition.getRootElement().selectSingleNode("item[@type='form']");
                                if (entryFormItem != null) {
                                    Element itemEle = (Element) entryFormItem.selectSingleNode(".//item/properties/property[@name='userVersionAllowed']");
                                    if (itemEle != null) {
                                        if ("true".equals(itemEle.attributeValue("value"))) {
                                            perUserVersionsAllowed = true;
                                        }
                                    }
                                }
                            }
                            String jsp = customJsp;
                            if (Validator.isNull(jsp)) jsp = defaultJsp;
                            if (!Validator.isNull(jsp) && !jsp.contains("./")) {
                                RequestDispatcher rd = httpReq.getRequestDispatcher(jsp);
                                ServletRequest req = new DynamicServletRequest(httpReq);
                                if (_params != null) {
                                    Iterator _it = _params.entrySet().iterator();
                                    while (_it.hasNext()) {
                                        Map.Entry me = (Map.Entry) _it.next();
                                        req.setAttribute((String) me.getKey(), (String) me.getValue());
                                    }
                                }
                                req.setAttribute("item", nextItem);
                                req.setAttribute(WebKeys.CONFIG_DEFINITION, this.configDefinition);
                                req.setAttribute(WebKeys.CONFIG_ELEMENT, this.configElement);
                                req.setAttribute(WebKeys.CONFIG_JSP_STYLE, this.configJspStyle);
                                req.setAttribute(WebKeys.CONFIG_FALLBACK_JSP, defaultJsp);
                                req.setAttribute(WebKeys.FIELD_MODIFICATIONS_ALLOWED, fieldModificationAllowed);
                                req.setAttribute("property_name", "");
                                req.setAttribute("property_caption", "");
                                req.setAttribute("property_required", "false");
                                List<Element> itemDefinitionProperties = new ArrayList<Element>();
                                Map propertyValuesMap = new HashMap();
                                if ("dataView".equals(nextItem.attributeValue("type"))) {
                                    Element entryFormItem = (Element) configDefinition.getRootElement().selectSingleNode("item[@type='form']");
                                    if (entryFormItem != null) {
                                        String nameValue = DefinitionUtils.getPropertyValue(nextItem, "name");
                                        if (Validator.isNotNull(nameValue)) {
                                            Element itemEle = (Element) entryFormItem.selectSingleNode(".//item/properties/property[@name='name' and @value='" + nameValue + "']");
                                            if (itemEle != null) {
                                                itemDefinitionProperties.addAll(itemEle.selectNodes("../../properties/property"));
                                            }
                                        }
                                    }
                                }
                                itemDefinitionProperties.addAll(itemDefinition.selectNodes("properties/property"));
                                Map savedReqAttributes = new HashMap();
                                String propertyName = "";
                                String propertyConfigType = "";
                                String propertyValue = "";
                                for (Element property : itemDefinitionProperties) {
                                    propertyName = property.attributeValue("name", "");
                                    if (Validator.isNull(propertyName)) continue;
                                    propertyConfigType = property.attributeValue("type", "text");
                                    propertyValue = "";
                                    if (propertyConfigType.equals("selectbox")) {
                                        List<Element> selProperties = nextItem.selectNodes("properties/property[@name='" + propertyName + "']");
                                        if (selProperties == null) continue;
                                        List propertyValues = new ArrayList();
                                        for (Element selItem : selProperties) {
                                            String selValue = NLT.getDef(selItem.attributeValue("value", ""));
                                            if (Validator.isNotNull(selValue)) propertyValues.add(selValue);
                                        }
                                        propertyValuesMap.put("propertyValues_" + propertyName, propertyValues);
                                        propertyValuesMap.put("property_" + propertyName, "");
                                    } else if (propertyConfigType.equals("workflowStatesList")) {
                                        Element statesProperty = (Element) nextItem.selectSingleNode("properties/property[@name='states']");
                                        if (statesProperty == null) continue;
                                        List propertyValues = new ArrayList();
                                        for (Element stateEle : (List<Element>) statesProperty.selectNodes("./workflowState")) {
                                            String state = NLT.getDef(stateEle.attributeValue("name", ""));
                                            if (Validator.isNotNull(state)) propertyValues.add(state);
                                        }
                                        propertyValuesMap.put("propertyValues_" + propertyName, propertyValues);
                                        propertyValuesMap.put("property_" + propertyName, statesProperty.attributeValue("workflowDefinitionId"));
                                    } else {
                                        Element selItem = (Element) nextItem.selectSingleNode("properties/property[@name='" + propertyName + "']");
                                        if (selItem == null) selItem = property;
                                        if (propertyConfigType.equals("textarea")) {
                                            propertyValue = selItem.getText();
                                        } else {
                                            propertyValue = NLT.getDef(selItem.attributeValue("value", ""));
                                        }
                                        String reqAttrName = property.attributeValue("setAttribute", "");
                                        if (Validator.isNotNull(reqAttrName)) {
                                            savedReqAttributes.put(reqAttrName, req.getAttribute(reqAttrName));
                                            req.setAttribute(reqAttrName, propertyValue);
                                        }
                                        if (Validator.isNull(propertyValue)) {
                                            propertyValue = property.attributeValue("default", "");
                                            if (!Validator.isNull(propertyValue)) propertyValue = NLT.getDef(propertyValue);
                                        }
                                        propertyValuesMap.put("property_" + propertyName, propertyValue);
                                    }
                                }
                                if (formItem.equals("remoteApp")) {
                                    Element entryFormItem = (Element) configDefinition.getRootElement().selectSingleNode("item[@type='form']");
                                    if (entryFormItem != null) {
                                        String nameValue = DefinitionUtils.getPropertyValue(nextItem, "name");
                                        if (Validator.isNotNull(nameValue)) {
                                            Element itemEle = (Element) entryFormItem.selectSingleNode(".//item/properties/property[@name='name' and @value='" + nameValue + "']");
                                            if (itemEle != null) {
                                                itemEle = itemEle.getParent();
                                                Element remoteAppEle = (Element) itemEle.selectSingleNode("./property[@name='remoteApp']");
                                                if (remoteAppEle != null) {
                                                    String remoteAppId = remoteAppEle.attributeValue("value", "");
                                                    if (!remoteAppId.equals("")) propertyValuesMap.put("property_remoteApp", new Long(remoteAppId));
                                                }
                                            }
                                        }
                                    }
                                }
                                if (perUserVersionsAllowed) {
                                    List<String> perUserNames = new ArrayList<String>();
                                    Iterator<String> itKeys = null;
                                    if (this.entryMap != null) {
                                        itKeys = this.entryMap.keySet().iterator();
                                    } else if (this.entry != null) {
                                        itKeys = entry.getCustomAttributes().keySet().iterator();
                                    }
                                    if (itKeys != null) {
                                        while (itKeys.hasNext()) {
                                            String key = itKeys.next();
                                            String[] keyParts = key.split("\\.");
                                            if (keyParts.length == 2 && propertyValue.equals(keyParts[0])) {
                                                try {
                                                    if (entry.getCustomAttribute(key) != null) perUserNames.add(keyParts[1]);
                                                } catch (Exception e) {
                                                }
                                            }
                                        }
                                    }
                                    req.setAttribute("ss_userVersionPrincipals", ResolveIds.getPrincipalsByName(perUserNames, true));
                                }
                                Iterator itPropertyValuesMap = propertyValuesMap.entrySet().iterator();
                                while (itPropertyValuesMap.hasNext()) {
                                    Map.Entry entry = (Map.Entry) itPropertyValuesMap.next();
                                    req.setAttribute((String) entry.getKey(), entry.getValue());
                                }
                                if (this.entryMap != null) {
                                    req.setAttribute(WebKeys.DEFINITION_ENTRY, this.entryMap);
                                } else if (this.entry != null) {
                                    req.setAttribute(WebKeys.DEFINITION_ENTRY, this.entry);
                                }
                                if (itemType.equals(ObjectKeys.DEFINITION_WORKSPACE_REMOTE_APPLICATION) || itemType.equals(ObjectKeys.DEFINITION_FOLDER_REMOTE_APPLICATION) || itemType.equals(ObjectKeys.DEFINITION_ENTRY_REMOTE_APPLICATION)) {
                                    Map options = new HashMap();
                                    options.put(ObjectKeys.SEARCH_SORT_BY, Constants.SORT_TITLE_FIELD);
                                    options.put(ObjectKeys.SEARCH_SORT_DESCEND, Boolean.FALSE);
                                    options.put(ObjectKeys.SEARCH_MAX_HITS, Integer.MAX_VALUE - 1);
                                    Document searchFilter = DocumentHelper.createDocument();
                                    Element field = searchFilter.addElement(Constants.FIELD_ELEMENT);
                                    field.addAttribute(Constants.FIELD_NAME_ATTRIBUTE, Constants.ENTRY_TYPE_FIELD);
                                    Element child = field.addElement(Constants.FIELD_TERMS_ELEMENT);
                                    child.setText(Constants.ENTRY_TYPE_APPLICATION);
                                    options.put(ObjectKeys.SEARCH_FILTER_AND, searchFilter);
                                    Map searchResults = profileModule.getApplications(options);
                                    List remoteAppList = (List) searchResults.get(ObjectKeys.SEARCH_ENTRIES);
                                    req.setAttribute(WebKeys.REMOTE_APPLICATION_LIST, remoteAppList);
                                }
                                StringServletResponse res = new StringServletResponse(httpRes);
                                try {
                                    rd.include(req, res);
                                    pageContext.getOut().print("\n<!-- " + jsp + " -->\n");
                                    pageContext.getOut().print(res.getString());
                                    pageContext.getOut().print("\n<!-- end " + jsp.substring(jsp.lastIndexOf('/') + 1) + " -->\n");
                                } catch (Exception e) {
                                    pageContext.getOut().print("\n<!-- " + jsp + " -->\n");
                                    pageContext.getOut().print("<!-- Error: itemType=" + itemType);
                                    pageContext.getOut().print(", formType=" + formItem);
                                    pageContext.getOut().print(", propertyName=" + propertyName);
                                    pageContext.getOut().print(", propertyValue=" + propertyValue);
                                    pageContext.getOut().print(", error=" + e.toString() + " -->\n");
                                    pageContext.getOut().print("<!-- end " + jsp.substring(jsp.lastIndexOf('/') + 1) + " -->\n");
                                }
                                for (Element property : itemDefinitionProperties) {
                                    String reqAttrName = property.attributeValue("setAttribute", "");
                                    if (Validator.isNotNull(reqAttrName)) {
                                        savedReqAttributes.put(reqAttrName, req.getAttribute(reqAttrName));
                                        req.setAttribute(reqAttrName, req.getAttribute(reqAttrName));
                                    }
                                }
                            } else {
                                if (!"mail".equals(configJspStyle)) {
                                }
                            }
                        } else {
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new JspException(e);
        } finally {
            this.configDefinition = null;
            this.configElement = null;
            this.configJspStyle = null;
            this.processThisItem = false;
            this.entry = null;
            this.entryMap = null;
            this._params = null;
        }
        return EVAL_PAGE;
    }

    public void setConfigDefinition(Document configDefinition) {
        this.configDefinition = configDefinition;
    }

    public void setConfigElement(Element configElement) {
        this.configElement = configElement;
    }

    public void setConfigJspStyle(String configJspStyle) {
        this.configJspStyle = configJspStyle;
    }

    public void setProcessThisItem(boolean flag) {
        this.processThisItem = flag;
    }

    public void setEntry(Object entry) {
        if (entry instanceof Map) this.entryMap = (Map) entry;
        if (entry instanceof DefinableEntity) this.entry = (DefinableEntity) entry;
    }

    public void addParam(String name, String value) {
        if (_params == null) {
            _params = new HashMap();
        }
        _params.put(name, value);
    }
}
