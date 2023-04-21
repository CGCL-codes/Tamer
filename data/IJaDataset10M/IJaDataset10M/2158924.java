package net.sf.simplecq.web.cq;

import net.sf.simplecq.core.model.ContinuousQuery;
import net.sf.simplecq.core.model.DataSourceDefinition;
import net.sf.simplecq.core.service.ContinuousQueryService;
import net.sf.simplecq.core.service.DataSourceDefinitionService;
import net.sf.simplecq.web.common.BasePage;
import net.sf.simplecq.web.common.DisplayableChoiceRenderer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * Displays an update form for a {@link ContinuousQuery}.
 * 
 * @author Sherif Behna
 */
public class ContinuousQueryUpdatePage extends BasePage {

    @SpringBean
    private HibernateTemplate hibernateTemplate;

    @SpringBean
    private ContinuousQueryService service;

    @SpringBean
    private DataSourceDefinitionService dataSourceDefinitionService;

    public ContinuousQueryUpdatePage(ContinuousQuery query) {
        super();
        ContinuousQuery updatedQuery = (ContinuousQuery) hibernateTemplate.get(ContinuousQuery.class, query.getId());
        init(updatedQuery);
    }

    private void init(final ContinuousQuery query) {
        add(new FeedbackPanel("feedback"));
        final Form<ContinuousQuery> form = new Form<ContinuousQuery>("form", new CompoundPropertyModel<ContinuousQuery>(query));
        add(form);
        form.add(new RequiredTextField("name"));
        form.add(new TextArea("description"));
        form.add(new TextArea("querySql").setRequired(true));
        form.add(new RequiredTextField("repeatInterval", Long.class));
        form.add(new DropDownChoice<DataSourceDefinition>("dataSource", dataSourceDefinitionService.findActive(), new DisplayableChoiceRenderer()).setRequired(true));
        form.add(new CheckBox("active"));
        form.add(new Button("ok") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                super.onSubmit();
                service.save(form.getModelObject());
                setResponsePage(ContinuousQueryListPage.class);
            }
        });
        form.add(new Button("cancel") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit() {
                super.onSubmit();
                setResponsePage(ContinuousQueryListPage.class);
            }
        });
        form.add(new KeyFormPanel("keySection", query));
        form.add(new ListenerFormPanel("listenerSection", query));
    }
}
