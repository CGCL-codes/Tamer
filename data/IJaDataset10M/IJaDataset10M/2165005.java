package com.googlecode.yamaguchi.officeportal.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.apache.commons.lang.BooleanUtils;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.CompoundPropertyModel;
import com.google.inject.Inject;
import com.googlecode.yamaguchi.officeportal.db.EntityManagerWrapper;
import com.googlecode.yamaguchi.officeportal.entity.Account;
import com.googlecode.yamaguchi.officeportal.entity.LendStatus;
import com.googlecode.yamaguchi.officeportal.entity.SecurityCard;
import com.googlecode.yamaguchi.officeportal.home.Template;
import com.googlecode.yamaguchi.officeportal.validator.RequireWhenEnabledValidator;

/**
 * @author h_yamaguchi
 * 
 */
public class ModifyCardPage extends WebPage {

    @Inject
    EntityManagerWrapper manager;

    /**
	 * @param manageAccount
	 * @param i
	 * @param createBox
	 */
    @SuppressWarnings("unchecked")
    public ModifyCardPage(PageParameters parameters) {
        final Integer id = parameters.getInt("id");
        final Template template;
        add(template = new Template("main", ModifyCardPage.class.getSimpleName()));
        final SecurityCard dto = this.manager.get(SecurityCard.class, id);
        final Form form = new Form("form", new CompoundPropertyModel(dto));
        Component idField = new Label("ID");
        Component cardNumberField = new TextField("cardNumber").setRequired(true);
        Account[] accounts = this.manager.find(Account.class);
        Collection<Account> dtos = Arrays.asList(accounts);
        final DropDownChoice ownerField = new DropDownChoice("owner", new ArrayList<Account>(dtos), new IChoiceRenderer() {

            public Object getDisplayValue(Object object) {
                return ((Account) object).getName();
            }

            public String getIdValue(Object object, int index) {
                return String.valueOf(((Account) object).getID());
            }
        });
        ownerField.setOutputMarkupId(true);
        final RadioChoice sharedCardField = new RadioChoice("sharedCard", Arrays.asList(true, false), new IChoiceRenderer() {

            public Object getDisplayValue(Object object) {
                return Boolean.TRUE.equals(object) ? "共用" : "個人";
            }

            public String getIdValue(Object object, int index) {
                return object.toString();
            }
        }).setSuffix("");
        sharedCardField.setRequired(true);
        ownerField.setEnabled(!dto.getSharedCard());
        sharedCardField.add(new AjaxFormChoiceComponentUpdatingBehavior() {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (BooleanUtils.toBoolean(sharedCardField.getModelObjectAsString())) {
                    ownerField.setEnabled(false);
                } else {
                    ownerField.setEnabled(true);
                }
                target.addComponent(ownerField);
            }
        });
        Component okButton = new SubmitLink("ok") {

            @Override
            public void onSubmit() {
                if (dto.getSharedCard()) {
                    dto.setOwner(null);
                    if (dto.getLendStatus() == null || dto.getLendStatus().equals(LendStatus.unlendable)) {
                        dto.setLendStatus(LendStatus.lendable);
                    }
                } else {
                    Account ownerAccount = ModifyCardPage.this.manager.get(Account.class, dto.getOwner().getID());
                    dto.setOwner(ownerAccount);
                    dto.setLendStatus(LendStatus.unlendable);
                }
                ModifyCardPage.this.manager.save(dto, SecurityCard.class);
                setResponsePage(ManageCardPage.class);
            }
        };
        Component closeButton = new Link("close") {

            @Override
            public void onClick() {
                setResponsePage(ManageCardPage.class);
            }
        };
        form.add(new RequireWhenEnabledValidator(ownerField, sharedCardField, Boolean.toString(false)));
        template.add(form.add(idField).add(cardNumberField).add(sharedCardField).add(ownerField).add(okButton).add(closeButton));
    }

    public EntityManagerWrapper getManager() {
        return this.manager;
    }

    public void setManager(EntityManagerWrapper manager) {
        this.manager = manager;
    }
}
