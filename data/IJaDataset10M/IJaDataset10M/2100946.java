package com.divosa.eformulieren.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import com.divosa.eformulieren.constant.InitStaticValues;
import com.divosa.eformulieren.core.cache.ToolkitCache;
import com.divosa.eformulieren.core.parse.sax.ParserFactory;
import com.divosa.eformulieren.core.parse.sax.XmlParser;
import com.divosa.eformulieren.core.service.FormService;
import com.divosa.eformulieren.core.service.WidgetService;
import com.divosa.eformulieren.domain.domeinobject.Scheme;
import com.divosa.eformulieren.domain.domeinobject.Widget;
import com.divosa.eformulieren.domain.domeinobject.WidgetAttribute;
import com.divosa.eformulieren.domain.domeinobject.WidgetStruct;
import com.divosa.eformulieren.domain.domeinobject.WidgetStructDef;
import com.divosa.eformulieren.domain.domeinobject.WidgetStructLoc;
import com.divosa.eformulieren.domain.domeinobject.WidgetType;
import com.divosa.eformulieren.domain.domeinobject.WidgetTypeStruct;
import com.divosa.eformulieren.domain.repository.StructDAO;
import com.divosa.eformulieren.domain.util.DomainUtil;
import com.divosa.eformulieren.util.constant.Constants;
import com.divosa.eformulieren.util.exception.DivosaUtilException;
import com.divosa.security.exception.AuthenticationException;
import com.divosa.security.exception.ObjectNotFoundException;
import com.divosa.security.exception.RepositoryLayerException;

public class FormServiceImpl extends BaseServiceImpl implements FormService {

    private Logger LOGGER = Logger.getLogger(this.getClass());

    private WidgetService widgetService;

    /**
     * @param structDAO the DAO for repository access
     */
    public FormServiceImpl(final StructDAO structDAO) {
        dao = structDAO;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws AuthenticationException
     * 
     * @see com.divosa.eformulieren.core.service.FormService#loadWidgetStruct(java.lang.Long, java.lang.Long, int,
     * java.lang.String)
     */
    public WidgetStruct loadWidgetStruct(Long parentId, Long formId, int version, String widgetState) throws ObjectNotFoundException, DivosaUtilException, AuthenticationException {
        StopWatch sw = null;
        if (LOGGER.isInfoEnabled()) {
            sw = new StopWatch();
            sw.start();
        }
        WidgetStruct loadedWidgetStruct = null;
        boolean widgetStructIsInCache = ToolkitCache.isWidgetStructActive(getCacheKey(), parentId, formId, version, widgetState);
        if (widgetStructIsInCache) {
            loadedWidgetStruct = ToolkitCache.getActiveWidgetStruct(getCacheKey());
        } else {
            Widget formWidget = (Widget) getPersitentObject(Widget.class, formId);
            Widget parentWidget = (Widget) getPersitentObject(Widget.class, parentId);
            if (version == 0) {
                version = getLatestWidgetStructVersionForParentFormAndState(parentWidget, formWidget, widgetState);
            }
            List<WidgetStruct> widgetStructsPerFormAndVersion = ((StructDAO) dao).getAllWidgetStructsByFormWidgetStateAndVersion(formWidget, widgetState, version);
            Map<Long, List<WidgetStruct>> widgetStructsMap = constructWidgetStructMap(widgetStructsPerFormAndVersion);
            WidgetStruct widgetStruct = null;
            if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
                widgetStruct = new WidgetStructDef(parentWidget, version);
            } else {
                widgetStruct = new WidgetStructLoc(parentWidget, version);
            }
            widgetStruct.setFormWidget(formWidget);
            populateAllChildWidgetStructs(widgetStruct, widgetStructsMap, parentWidget);
            loadedWidgetStruct = widgetStruct;
            ToolkitCache.updateActiveWidgetStruct(getCacheKey(), loadedWidgetStruct);
        }
        if (LOGGER.isDebugEnabled()) {
            sw.stop();
            LOGGER.debug("loadWidgetStruct for " + loadedWidgetStruct.getChildWidget().getDescription() + " took " + sw.getTime());
        }
        return loadedWidgetStruct;
    }

    /**
     * {@inheritDoc}
     * 
     * @throws AuthenticationException
     * 
     * @see com.divosa.eformulieren.core.service.FormService#loadWidgetStruct(java.lang.Long, java.lang.Long,
     * java.lang.String)
     */
    public WidgetStruct loadWidgetStruct(Long id, Long formId, String widgetState) throws ObjectNotFoundException, DivosaUtilException, AuthenticationException {
        return loadWidgetStruct(id, formId, 0, widgetState);
    }

    /**
     * @throws AuthenticationException
     * @see com.divosa.eformulieren.core.service.FormService#loadAllWidgetStructs(java.lang.Long, java.lang.Long,
     * java.lang.String, int)
     */
    public List<WidgetStruct> loadAllWidgetStructs(Long widgetTypeId, Long formId, String widgetState, int version) throws ObjectNotFoundException, AuthenticationException {
        WidgetType widgetType = (WidgetType) getPersitentObject(WidgetType.class, widgetTypeId);
        Widget form = null;
        if (formId != null) {
            form = (Widget) getPersitentObject(Widget.class, formId);
        } else {
            form = getDefaultForm(widgetState);
        }
        List<WidgetStruct> widgetStructs = new ArrayList<WidgetStruct>();
        try {
            if (version == 0) {
                widgetStructs = ((StructDAO) dao).getAllWidgetStructsByWidgetTypeFormAndWidgetState(widgetType, form, widgetState);
            } else {
                widgetStructs = ((StructDAO) dao).getAllWidgetStructsByWidgetTypeFormWidgetStateAndVersion(widgetType, form, widgetState, version);
            }
        } catch (ObjectNotFoundException e) {
            LOGGER.error(e.getMessage());
        }
        return widgetStructs;
    }

    public List<WidgetStruct> loadAllWidgetStructs(Long widgetTypeId, Long formId, String widgetState, List<WidgetAttribute> widgetAttributes) throws ObjectNotFoundException, AuthenticationException {
        List<WidgetStruct> allWidgetStructs = loadAllWidgetStructs(widgetTypeId, formId, widgetState);
        if (allWidgetStructs != null && !allWidgetStructs.isEmpty()) {
            allWidgetStructs = DomainUtil.filterWidgetStructsOnWidgetAttributes(allWidgetStructs, widgetAttributes);
        }
        return allWidgetStructs;
    }

    public List<WidgetStruct> loadAllWidgetStructs(Long widgetTypeId, Long formId, String widgetState, List<WidgetAttribute> widgetAttributes, int version) throws ObjectNotFoundException, AuthenticationException {
        List<WidgetStruct> allWidgetStructs = loadAllWidgetStructs(widgetTypeId, formId, widgetState, version);
        if (allWidgetStructs != null && !allWidgetStructs.isEmpty()) {
            allWidgetStructs = DomainUtil.filterWidgetStructsOnWidgetAttributes(allWidgetStructs, widgetAttributes);
        }
        return allWidgetStructs;
    }

    public List<WidgetStruct> loadAllWidgetStructs(Long widgetTypeId, Long formId, String widgetState) throws ObjectNotFoundException, AuthenticationException {
        return loadAllWidgetStructs(widgetTypeId, formId, widgetState, 0);
    }

    public void removeWidgetStruct(Long id, Long formId, String widgetState, int version) throws AuthenticationException, DivosaUtilException, RepositoryLayerException {
        WidgetStruct upperWidgetStruct = loadWidgetStruct(id, formId, version, widgetState);
        Set<WidgetStruct> widgetStructs = new HashSet<WidgetStruct>();
        widgetStructs.add(upperWidgetStruct);
        for (WidgetStruct struct : upperWidgetStruct.getChildWidgetStructs()) {
            collectAllWidgetStructs(struct, widgetStructs);
        }
        if (!id.equals(formId)) {
            try {
                List<WidgetStruct> widgetStructsExtra = ((StructDAO) dao).getAllWidgetStructsByChildFormWidgetStateAndVersion((Widget) dao.get(Widget.class, id), (Widget) dao.get(Widget.class, formId), widgetState, upperWidgetStruct.getVersion());
                widgetStructs.addAll(widgetStructsExtra);
            } catch (Exception e) {
                String message = "Caught a(n) " + e.getClass().getSimpleName() + " :" + e.getMessage();
                LOGGER.info(message);
            }
        }
        Set<Widget> widgets = new HashSet<Widget>();
        widgets.add(upperWidgetStruct.getChildWidget());
        widgets.add(upperWidgetStruct.getParentWidget());
        for (WidgetStruct struct : upperWidgetStruct.getChildWidgetStructs()) {
            collectAllWidgets(struct, widgets);
        }
        Iterator<WidgetStruct> iteratorWS = widgetStructs.iterator();
        while (iteratorWS.hasNext()) {
            WidgetStruct ws = iteratorWS.next();
            if (ws.getId() != null) {
                dao.remove(ws);
            }
        }
        Iterator<Widget> iteratorW = widgets.iterator();
        while (iteratorW.hasNext()) {
            Widget w = iteratorW.next();
            if (w != null && !WidgetType.FORM.getName().equals(w.getWidgetType().getName()) && !w.getId().equals(id)) {
                LOGGER.info("remove WIdget " + w.getId());
                dao.remove(w);
            }
        }
    }

    /**
     * @throws RepositoryLayerException
     * @throws AuthenticationException
     * @see com.divosa.eformulieren.core.service.FormService#insertWidgetStruct(org.dom4j.Document, java.lang.Long,
     * java.lang.String, java.lang.Integer)
     */
    public Long insertWidgetStruct(Document document, Long formId, String widgetState, Integer version) throws RepositoryLayerException, DivosaUtilException, AuthenticationException {
        StopWatch sw = null;
        if (LOGGER.isInfoEnabled()) {
            sw = new StopWatch();
            sw.start();
        }
        ToolkitCache.clear(getCacheKey());
        List<WidgetStruct> widgetStructs = getWidgetStructTree(document, widgetState);
        if (widgetStructs != null && !widgetStructs.isEmpty()) {
            WidgetStruct upperWidgetStruct = widgetStructs.get(widgetStructs.size() - 1);
            if (upperWidgetStruct.getFormWidget() == null || (formId != null && formId.equals(getDefaultForm(widgetState).getId()))) {
                setForm(widgetStructs, formId, widgetState);
            }
            List<WidgetAttribute> was = (List<WidgetAttribute>) dao.loadAll(WidgetAttribute.class);
            for (WidgetStruct widgetStruct : widgetStructs) {
                widgetStruct.getParentWidget().setId(null);
                widgetStruct.getChildWidget().setId(null);
            }
            Widget formW = null;
            boolean newForm = false;
            if (formId == null || formId.equals(getDefaultForm(widgetState).getId())) {
                formW = upperWidgetStruct.getFormWidget();
            } else {
                List<Widget> forms = widgetService.loadAllFormWidgets(upperWidgetStruct.getFormWidget().getDescription(), Scheme.XFORMS);
                if (forms != null && !forms.isEmpty()) {
                    formW = forms.get(0);
                    formW.setWidgetAttributes(upperWidgetStruct.getFormWidget().getWidgetAttributes());
                    widgetService.collectSaveOrUpdateWidget(formW, was, formW, formW.getVersion());
                }
                if (formW == null) {
                    upperWidgetStruct.getFormWidget().setId(null);
                    widgetService.collectSaveOrUpdateWidget(upperWidgetStruct.getFormWidget(), was, null, 0);
                    newForm = true;
                    formW = upperWidgetStruct.getFormWidget();
                }
            }
            formId = formW.getId();
            if (version == null) {
                version = 0;
                if (!newForm) {
                    try {
                        version = getLatestWidgetStructVersionForParentFormAndState(formW, formW, widgetState);
                    } catch (Exception e) {
                        LOGGER.warn(e.getMessage());
                    }
                    version++;
                } else {
                    version++;
                }
            }
            for (WidgetStruct widgetStruct : widgetStructs) {
                Widget pWidget = widgetStruct.getParentWidget();
                if (!pWidget.getWidgetType().getName().equals(WidgetType.FORM.getName())) {
                    pWidget = widgetService.collectSaveOrUpdateWidget(pWidget, was, formW, version);
                } else {
                    pWidget = formW;
                }
                Widget cWidget = widgetStruct.getChildWidget();
                if (!cWidget.getWidgetType().getName().equals(WidgetType.FORM.getName())) {
                    cWidget = widgetService.collectSaveOrUpdateWidget(cWidget, was, formW, version);
                } else {
                    cWidget = formW;
                }
                WidgetTypeStruct wts = InitStaticValues.getWidgetTypeStruct(pWidget.getWidgetType().getName(), cWidget.getWidgetType().getName());
                if (pWidget.getId().equals(cWidget.getId())) {
                    continue;
                }
                WidgetStruct widgetStructNew = null;
                if (Constants.WIDGET_STATE_DEF.equals(widgetState)) {
                    widgetStructNew = new WidgetStructDef(wts, pWidget, cWidget, formW);
                } else {
                    widgetStructNew = new WidgetStructLoc(wts, pWidget, cWidget, formW);
                }
                if (widgetStructNew != null) {
                    if (widgetStruct.getSequence() != null) {
                        widgetStructNew.setSequence(widgetStruct.getSequence());
                    }
                    widgetStructNew.setVersion(version);
                    saveOrUpdate(widgetStructNew);
                }
            }
        }
        if (LOGGER.isDebugEnabled()) {
            sw.stop();
            LOGGER.debug("saveWidgetStruct of " + ((WidgetStruct) widgetStructs.get(widgetStructs.size() - 1)).getParentWidget().getDescription() + " took " + sw.getTime());
        }
        return formId;
    }

    /**
     * @param widgetService the widgetService to set
     */
    public void setWidgetService(WidgetService widgetService) {
        this.widgetService = widgetService;
    }

    /**
     * Get the latest version number of a WidgetStruct, based on the widgetState (either LOCal of pre-DEFined).
     * 
     * @param parent the parent Widget
     * @param form the form Widget
     * @param widgetState the state; either DEFined of LOCal
     * @return the WidgetStruct
     * @throws ObjectNotFoundException the Exception thrown if an object is not found in the repository
     */
    private int getLatestWidgetStructVersionForParentFormAndState(Widget parent, Widget form, String widgetState) throws ObjectNotFoundException {
        int version = 0;
        if (parent.getId() != null && form.getId() != null) {
            List<WidgetStruct> widgetStructs = ((StructDAO) dao).getAllWidgetStructsByParentFormAndWidgetState(parent, form, widgetState);
            WidgetStruct latestWidgetStruct = widgetStructs.get(0);
            version = latestWidgetStruct.getVersion();
        }
        return version;
    }

    /**
     * Collect a list of WidgetStuct objects from the specified DOM4J Document and with the specified state; either LOCal or
     * pre-DEFined. Uses SAX parsing (directed via ParserFactory) for parsing the document.
     * 
     * @param document the DOM4J Document representing the WidgetStruct
     * @param widgetState the state; either LOCal or pre-DEFined.
     * @return a list of WidgetStruct objects, representing the parent-child connections present in the specified document
     */
    private List<WidgetStruct> getWidgetStructTree(Document document, String widgetState) {
        Element widgets = (Element) document.selectObject("widget");
        String widgetTreeString = widgets.asXML();
        XmlParser parser = ParserFactory.getParser(Constants.WIDGET_TREE + widgetState);
        List<WidgetStruct> widgetStructs = (List) parser.readXML(widgetTreeString);
        return widgetStructs;
    }

    /**
     * Loads the complete WidgetStruct model representing a Widget-tree, starting at the childWidget of the current
     * widgetStruct. Sets per WidgetStruct 1. childWidgetStructs 2. childWidgetTypeTag (belonging to the childWidget)
     * 
     * @param widgetStruct the 'upper' WidgetStruct.
     * @param version the version of the form
     */
    private void populateAllChildWidgetStructs(WidgetStruct widgetStruct, Map<Long, List<WidgetStruct>> widgetStructsMap, Widget childWidget) {
        widgetStruct.setChildWidgetTypeTag(InitStaticValues.getWidgetTypeTag(childWidget.getScheme().getName(), childWidget.getWidgetType().getName()));
        List<WidgetStruct> widgetStructs = widgetStructsMap.get(childWidget.getId());
        widgetStruct.setChildWidgetStructs(widgetStructs);
        if (widgetStructs != null && !widgetStructs.isEmpty()) {
            for (WidgetStruct childWidgetStruct : widgetStructs) {
                populateAllChildWidgetStructs(childWidgetStruct, widgetStructsMap, childWidgetStruct.getChildWidget());
            }
        }
    }

    private void setForm(List<WidgetStruct> widgetStructs, Long formId, String widgetState) throws ObjectNotFoundException, AuthenticationException {
        Widget formWidget = null;
        if (formId != null) {
            formWidget = (Widget) getPersitentObject(Widget.class, formId);
        } else {
            formWidget = getDefaultForm(widgetState);
        }
        for (WidgetStruct widgetStruct : widgetStructs) {
            widgetStruct.setFormWidget(formWidget);
        }
    }

    private Widget getDefaultForm(String widgetState) throws ObjectNotFoundException, AuthenticationException {
        WidgetType form = (WidgetType) getPersitentObject(WidgetType.class, new Long(1));
        List<Widget> formWidgets = widgetService.getWidgetsByDescription("form-" + widgetState);
        Widget defaultForm = formWidgets.get(0);
        return defaultForm;
    }

    private Map<Long, List<WidgetStruct>> constructWidgetStructMap(List<WidgetStruct> widgetStructsPerFormAndVersion) {
        Map<Long, List<WidgetStruct>> widgetStructsMap = new HashMap<Long, List<WidgetStruct>>();
        List<WidgetStruct> widgetStructsPerParent = new ArrayList<WidgetStruct>();
        Long parentId = null;
        Long previousParentId = new Long(-999);
        for (WidgetStruct widgetStruct : widgetStructsPerFormAndVersion) {
            parentId = widgetStruct.getParentWidget().getId();
            if (parentId.longValue() != previousParentId.longValue()) {
                if (previousParentId > -999) {
                    widgetStructsMap.put(previousParentId, widgetStructsPerParent);
                }
                widgetStructsPerParent = new ArrayList<WidgetStruct>();
            }
            widgetStructsPerParent.add(widgetStruct);
            previousParentId = parentId;
        }
        widgetStructsMap.put(previousParentId, widgetStructsPerParent);
        return widgetStructsMap;
    }

    private void collectAllWidgetStructs(WidgetStruct widgetStruct, Set<WidgetStruct> widgetStructs) {
        widgetStructs.add(widgetStruct);
        for (WidgetStruct struct : widgetStruct.getChildWidgetStructs()) {
            collectAllWidgetStructs(struct, widgetStructs);
        }
    }

    private void collectAllWidgets(WidgetStruct widgetStruct, Set<Widget> widgets) {
        widgets.add(widgetStruct.getChildWidget());
        widgets.add(widgetStruct.getParentWidget());
        for (WidgetStruct struct : widgetStruct.getChildWidgetStructs()) {
            collectAllWidgets(struct, widgets);
        }
    }
}
