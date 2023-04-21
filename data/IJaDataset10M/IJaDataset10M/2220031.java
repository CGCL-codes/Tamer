package org.zeroexchange.web.components.resource.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.NumberTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.zeroexchange.model.resource.Resource;
import org.zeroexchange.model.resource.participant.ResourceTender;
import org.zeroexchange.model.resource.participant.Supply;
import org.zeroexchange.model.resource.participant.TenderType;
import org.zeroexchange.resource.write.ResourceWriter;
import org.zeroexchange.security.AuthorizedUserService;
import org.zeroexchange.web.UIUtils;

/**
 * @author black
 *
 */
public class SubmitTenderPanel extends ResourceActionPanel {

    private static final long serialVersionUID = 1L;

    private static final String CKEY_TENDER_FORM = "tenderForm";

    private static final String CKEY_TENDER_INFO = "info";

    private static final String CKEY_TENDER_TYPE_COMBO = "tenderType";

    private static final String CKEY_WORKING_HOURS_FRAGNEMT = "workingHoursFragment";

    private static final String CKEY_WORKING_HOURS_PANEL = "workingHoursPanel";

    private static final String CKEY_WORKING_HOURS_INPUT = "workingHours";

    private static final String CKEY_SUBMIT_TENDER_BUTTON = "submitTenderButton";

    private static final String MKEY_TENDER_TYPE_PREFIX = "tender.";

    private static final String MKEY_TENDER_SUBMIT = "tender.submit";

    @SpringBean
    private ResourceWriter resourceWriter;

    @SpringBean
    private AuthorizedUserService authorizedUserService;

    private Form<TenderData> tenderForm;

    private DropDownChoice<TenderType> typeChoice;

    private Resource resource;

    private class TenderData implements Serializable {

        private static final long serialVersionUID = 1L;

        private String info = "";

        private TenderType tenderType;

        private BigDecimal workingHours;

        /**
         * @return the info
         */
        public String getInfo() {
            return info;
        }

        /**
         * @return the tenderType
         */
        public TenderType getTenderType() {
            return tenderType;
        }

        /**
         * @return the workingHours
         */
        public BigDecimal getWorkingHours() {
            return workingHours;
        }
    }

    /**
     * Constructor.
     * @param resource 
     */
    public SubmitTenderPanel(String id, Resource resource, ResourceModificationListener resourceModificationListener) {
        super(id, resourceModificationListener);
        this.resource = resource;
        intiUI();
    }

    /**
     * Initializes the UI.
     */
    private void intiUI() {
        setOutputMarkupId(true);
        tenderForm = new Form<TenderData>(CKEY_TENDER_FORM, new CompoundPropertyModel<TenderData>(new TenderData())) {

            private static final long serialVersionUID = 1L;

            /**
             * {@inheritDoc}
             */
            @Override
            protected void onSubmit() {
                saveSubmittedData(getModelObject());
                resourceModified();
            }
        };
        final Fragment workingHoursFragment = new Fragment(CKEY_WORKING_HOURS_PANEL, CKEY_WORKING_HOURS_FRAGNEMT, this);
        workingHoursFragment.setVisible(false);
        List<TenderType> tenderTypes = new ArrayList<TenderType>();
        Collections.addAll(tenderTypes, TenderType.values());
        typeChoice = new DropDownChoice<TenderType>(CKEY_TENDER_TYPE_COMBO, tenderTypes, new IChoiceRenderer<TenderType>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Object getDisplayValue(TenderType type) {
                return getString(MKEY_TENDER_TYPE_PREFIX + type.name());
            }

            @Override
            public String getIdValue(TenderType type, int index) {
                return type.name();
            }
        });
        typeChoice.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                TenderType tenderType = (TenderType) typeChoice.getDefaultModelObject();
                workingHoursFragment.setVisible(tenderType == TenderType.SUPPLY);
                target.add(SubmitTenderPanel.this);
            }
        });
        typeChoice.setRequired(true);
        tenderForm.add(UIUtils.createValidatableField(typeChoice));
        workingHoursFragment.add(new NumberTextField<BigDecimal>(CKEY_WORKING_HOURS_INPUT));
        tenderForm.add(workingHoursFragment);
        tenderForm.add(UIUtils.createValidatableField(new TextArea<String>(CKEY_TENDER_INFO).setRequired(true)));
        tenderForm.add(new Button(CKEY_SUBMIT_TENDER_BUTTON, new ResourceModel(MKEY_TENDER_SUBMIT)));
        add(tenderForm);
    }

    /**
     * Saves the submitted data.
     */
    private void saveSubmittedData(TenderData tenderData) {
        ResourceTender tender = null;
        boolean isSupply = tenderData.getTenderType() == TenderType.SUPPLY;
        if (isSupply) {
            tender = new Supply();
            ((Supply) tender).setHours(tenderData.getWorkingHours());
        } else {
            tender = new ResourceTender();
        }
        tender.setResource(resource);
        tender.setUser(authorizedUserService.getCurrentUser());
        tender.setAdditionalInfo(tenderData.getInfo());
        tender.setType(tenderData.getTenderType());
        resourceWriter.save(tender);
    }
}
