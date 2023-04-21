package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.AnagraficaObjectAreaDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.dto.ValoreDTOPropertyEditor;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.widget.WidgetCombo;
import java.beans.PropertyEditor;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.MessageSourceAware;
import org.springframework.validation.Errors;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class RealAnagraficaDTOEditingAction<P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, EO extends AnagraficaObject<P, TP>> extends BaseFormAction implements MessageSourceAware {

    protected Log logger = LogFactory.getLog(RealAnagraficaDTOEditingAction.class);

    private FormulaManager formulaManager;

    private ITabService applicationService;

    private Class<TP> clazzTipologiaProprieta;

    private Class<EO> clazzAnagraficaObject;

    private Class<T> clazzTab;

    private Class<H> clazzBox;

    private String baseDetailURL;

    public String getBaseDetailURL() {
        return baseDetailURL;
    }

    public void setBaseDetailURL(String baseDetailURL) {
        this.baseDetailURL = baseDetailURL;
    }

    public void setClazzAnagraficaObject(Class<EO> clazzAnagraficaObject) {
        this.clazzAnagraficaObject = clazzAnagraficaObject;
    }

    public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta) {
        this.clazzTipologiaProprieta = clazzTipologiaProprieta;
    }

    public void setClazzTab(Class<T> clazzTab) {
        this.clazzTab = clazzTab;
    }

    public void setClazzBox(Class<H> clazzBox) {
        this.clazzBox = clazzBox;
    }

    public void setApplicationService(ITabService applicationService) {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService() {
        return applicationService;
    }

    public Event referenceData(RequestContext context) throws Exception {
        AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) getFormObject(context);
        EO epiObject = applicationService.get(clazzAnagraficaObject, anagraficaObjectDTO.getObjectId());
        Map breadCumbs = getBreadcrumbs(anagraficaObjectDTO);
        context.getFlashScope().put("breadCumbs", breadCumbs);
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(clazzTab, anagraficaObjectDTO.getTabId());
        List<IContainable> tipProprietaInArea = null;
        for (IPropertyHolder<Containable> iph : propertyHolders) {
            tipProprietaInArea = applicationService.findContainableInPropertyHolder(clazzBox, iph.getId());
        }
        context.getFlashScope().put("tipologieProprietaInArea", tipProprietaInArea);
        List<T> aree = applicationService.getList(clazzTab);
        List<T> aree4Edit = new LinkedList<T>();
        aree4Edit = aree;
        List<TP> tipProprietaInAreaWithRenderingFormula = applicationService.getAllTipologieProprietaWithWidgetFormulaInTab(clazzTipologiaProprieta, anagraficaObjectDTO.getTabId());
        if (tipProprietaInAreaWithRenderingFormula != null && !tipProprietaInAreaWithRenderingFormula.isEmpty()) {
            context.getFlashScope().put("mostraPulsanteFormule", Boolean.TRUE);
        } else {
            context.getFlashScope().put("mostraPulsanteFormule", Boolean.FALSE);
        }
        context.getFlashScope().put("areaList", aree4Edit);
        context.getFlashScope().put("simpleNameAnagraficaObject", clazzAnagraficaObject.getSimpleName());
        return success();
    }

    public Map getBreadcrumbs(AnagraficaObjectAreaDTO anagraficaObjectDTO) {
        return null;
    }

    @Override
    protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry servletRequestDataBinder) {
        AnagraficaObjectAreaDTO commandDTO = null;
        try {
            commandDTO = (AnagraficaObjectAreaDTO) getFormObject(context);
        } catch (Exception e) {
            logger.error("Non e' stato possibile estrarre il Command Object dal flusoo", e);
        }
        Set<String> shortNames = (commandDTO == null || commandDTO.getAnagraficaProperties() == null) ? null : commandDTO.getAnagraficaProperties().keySet();
        for (String shortName : commandDTO.getAnagraficaProperties().keySet()) {
            TP tipologiaProprieta = applicationService.findPropertiesDefinitionByShortName(clazzTipologiaProprieta, shortName);
            PropertyEditor propertyEditor = tipologiaProprieta.getRendering().getPropertyEditor(applicationService);
            if (tipologiaProprieta.getRendering() instanceof WidgetCombo) {
                WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tipologiaProprieta.getRendering();
                for (int i = 0; i < 100; i++) {
                    for (TP subtp : combo.getSottoTipologie()) {
                        PropertyEditor subPropertyEditor = subtp.getRendering().getPropertyEditor(applicationService);
                        String path = "anagraficaProperties[" + shortName + "][" + i + "].object.anagraficaProperties[" + subtp.getShortName() + "]";
                        servletRequestDataBinder.registerCustomEditor(ValoreDTO.class, path, new ValoreDTOPropertyEditor(subPropertyEditor));
                        servletRequestDataBinder.registerCustomEditor(Object.class, path + ".object", subPropertyEditor);
                        logger.debug("Registrato Wrapper del property editor: " + propertyEditor + " per il path (combo): " + path);
                        logger.debug("Registrato property editor: " + propertyEditor + " per il path (combo): " + path + ".object");
                    }
                }
            } else {
                String path = "anagraficaProperties[" + shortName + "]";
                servletRequestDataBinder.registerCustomEditor(ValoreDTO.class, path, new ValoreDTOPropertyEditor(propertyEditor));
                servletRequestDataBinder.registerCustomEditor(Object.class, path + ".object", propertyEditor);
                logger.debug("Registrato Wrapper del property editor: " + propertyEditor + " per il path: " + path);
                logger.debug("Registrato property editor: " + propertyEditor + " per il path: " + path + ".object");
            }
        }
    }

    @Override
    protected Object createFormObject(RequestContext context) throws Exception {
        String paramAreaId = context.getFlowScope().getString("areaId");
        String paramAnagraficaObjectId = context.getFlowScope().getString("anagraficaId");
        List<T> aree = applicationService.getList(clazzTab);
        Integer areaId;
        if (paramAreaId == null) {
            areaId = aree.get(0).getId();
        } else {
            areaId = Integer.parseInt(paramAreaId);
        }
        Integer anagraficaId = Integer.parseInt(paramAnagraficaObjectId);
        EO epiObject = applicationService.get(clazzAnagraficaObject, anagraficaId);
        if (epiObject == null) {
            epiObject = clazzAnagraficaObject.newInstance();
        }
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(clazzTab, areaId);
        List<IContainable> tipProprietaInArea = new LinkedList<IContainable>();
        for (IPropertyHolder<Containable> iph : propertyHolders) {
            tipProprietaInArea = applicationService.findContainableInPropertyHolder(clazzBox, iph.getId());
        }
        AnagraficaObjectAreaDTO anagraficaObjectDTO = new AnagraficaObjectAreaDTO();
        anagraficaObjectDTO.setTabId(areaId);
        anagraficaObjectDTO.setObjectId(anagraficaId);
        List<TP> realTPS = new LinkedList<TP>();
        for (IContainable c : tipProprietaInArea) {
            realTPS.add((TP) applicationService.findContainableByDecorable(clazzTipologiaProprieta.newInstance().getDecoratorClass(), c.getId()));
        }
        AnagraficaUtils.fillDTO(anagraficaObjectDTO, epiObject, realTPS);
        return anagraficaObjectDTO;
    }

    @Override
    protected void doValidate(RequestContext context, Object formObject, Errors errors) throws Exception {
        super.doValidate(context, formObject, errors);
        AnagraficaObjectAreaDTO dto = (AnagraficaObjectAreaDTO) formObject;
        EO epiObject = applicationService.get(clazzAnagraficaObject, dto.getObjectId());
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(clazzTab, dto.getTabId());
        List<IContainable> tipProprietaInArea = new LinkedList<IContainable>();
        for (IPropertyHolder<Containable> iph : propertyHolders) {
            tipProprietaInArea = applicationService.findContainableInPropertyHolder(clazzBox, iph.getId());
        }
        List<TP> realTPS = new LinkedList<TP>();
        for (IContainable c : tipProprietaInArea) {
            realTPS.add((TP) applicationService.findContainableByDecorable(clazzTipologiaProprieta.newInstance().getDecoratorClass(), c.getId()));
        }
        AnagraficaUtils.reverseDTO(dto, epiObject, realTPS);
        AnagraficaUtils.fillDTO(dto, epiObject, realTPS);
    }

    public Event persisti(RequestContext context) throws Exception {
        AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) getFormObject(context);
        EO myObject = applicationService.get(clazzAnagraficaObject, anagraficaObjectDTO.getObjectId());
        List<H> propertyHolders = applicationService.findPropertyHolderInTab(clazzTab, anagraficaObjectDTO.getTabId());
        List<IContainable> tipProprietaInArea = new LinkedList<IContainable>();
        for (IPropertyHolder<Containable> iph : propertyHolders) {
            tipProprietaInArea = applicationService.findContainableInPropertyHolder(clazzBox, iph.getId());
        }
        List<TP> realTPS = new LinkedList<TP>();
        for (IContainable c : tipProprietaInArea) {
            realTPS.add((TP) applicationService.findContainableByDecorable(clazzTipologiaProprieta.newInstance().getDecoratorClass(), c.getId()));
        }
        AnagraficaUtils.reverseDTO(anagraficaObjectDTO, myObject, realTPS);
        myObject.pulisciAnagrafica();
        applicationService.saveOrUpdate(clazzAnagraficaObject, myObject);
        T area = applicationService.get(clazzTab, anagraficaObjectDTO.getTabId());
        final String areaTitle = area.getTitle();
        saveMessage(context, getText("action.anagrafica.edited", new Object[] { areaTitle }));
        return success();
    }

    public void setFormulaManager(FormulaManager formulaManager) {
        this.formulaManager = formulaManager;
    }
}
