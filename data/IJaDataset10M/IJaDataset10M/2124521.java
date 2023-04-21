package org.dcm4chee.web.war.tc.keywords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteSettings;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.convert.IConverter;
import org.dcm4chee.web.war.tc.TCPanel;
import org.dcm4chee.web.war.tc.TCPanel.PopupCloseables;
import org.dcm4chee.web.war.tc.TCPanel.PopupCloseables.IPopupCloseable;
import org.dcm4chee.web.war.tc.keywords.TCKeywordCatalogue.TCKeywordInput;

/**
 * @author Bernhard Ableitinger <bernhard.ableitinger@agfa.com>
 * @version $Revision$ $Date$
 * @since June 20, 2011
 */
public class TCKeywordListInput extends Panel implements TCKeywordInput {

    private static final long serialVersionUID = 1L;

    public TCKeywordListInput(final String id, List<TCKeyword> keywords) {
        this(id, null, keywords);
    }

    public TCKeywordListInput(final String id, TCKeyword selectedKeyword, final List<TCKeyword> keywords) {
        super(id, selectedKeyword != null ? new Model<TCKeyword>(selectedKeyword) : new Model<TCKeyword>());
        final AutoCompleteTextField<TCKeyword> text = new AutoCompleteTextField<TCKeyword>("text", getModel(), TCKeyword.class, new AutoCompleteSettings()) {

            private static final long serialVersionUID = 1L;

            final Map<String, TCKeyword> keywordMap = new HashMap<String, TCKeyword>();

            @Override
            public IConverter getConverter(Class<?> type) {
                if (TCKeyword.class.equals(type)) {
                    if (keywordMap.isEmpty() && keywords != null && !keywords.isEmpty()) {
                        for (TCKeyword keyword : keywords) {
                            keywordMap.put(keyword.toString(), keyword);
                        }
                    }
                    return new IConverter() {

                        private static final long serialVersionUID = 1L;

                        @Override
                        public String convertToString(Object o, Locale locale) {
                            return o != null ? o.toString() : null;
                        }

                        @Override
                        public TCKeyword convertToObject(String s, Locale locale) {
                            if (s != null) {
                                TCKeyword keyword = keywordMap.get(s);
                                if (keyword == null) {
                                    keyword = new TCKeyword(s, null, false);
                                }
                                return keyword;
                            }
                            return null;
                        }
                    };
                }
                return getConverter(type);
            }

            @Override
            protected Iterator<TCKeyword> getChoices(String s) {
                List<TCKeyword> match = new ArrayList<TCKeyword>();
                if (s.length() >= 3) {
                    for (TCKeyword keyword : keywords) {
                        if (keyword.toString().toUpperCase().contains(s.toUpperCase())) {
                            match.add(keyword);
                        }
                    }
                }
                return match.iterator();
            }
        };
        text.setOutputMarkupId(true);
        final ListChoice<TCKeyword> keywordList = new ListChoice<TCKeyword>("keyword-list", new Model<TCKeyword>(selectedKeyword), keywords) {

            private static final long serialVersionUID = 1L;

            @Override
            protected String getNullValidKey() {
                return "tc.search.null.text";
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("onmousedown", "return false;");
            }
        };
        keywordList.setOutputMarkupId(true);
        keywordList.setNullValid(true);
        final ListPopupCloseable popup = new ListPopupCloseable(keywordList, text);
        keywordList.add(new AjaxFormComponentUpdatingBehavior("onchange") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onUpdate(AjaxRequestTarget target) {
                popup.close(target, keywordList.getModelObject(), true);
            }
        });
        popup.add(keywordList);
        popup.add(new AjaxButton("popup-close-button") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                popup.close(target, null, false);
            }
        });
        PopupCloseables.getInstance().addCloseable(popup);
        add(text);
        add(new AjaxButton("chooser-button", new Model<String>("...")) {

            private static final long serialVersionUID = 1L;

            @Override
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                keywordList.setModelObject(getSelectedKeyword());
                PopupCloseables.getInstance().closeAll(target);
                popup.setVisible(true);
                target.addComponent(popup);
                target.addComponent(keywordList);
                target.appendJavascript("setPositionRelativeToParent('" + getMarkupId() + "','" + popup.getMarkupId() + "')");
            }
        });
        add(popup);
    }

    @Override
    public TCKeyword getSelectedKeyword() {
        TCKeyword keyword = getModel().getObject();
        return keyword == null || keyword.isAllKeywordsPlaceholder() ? null : keyword;
    }

    @Override
    public void resetSelectedKeyword() {
        getModel().setObject(null);
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @SuppressWarnings({ "unchecked" })
    private Model<TCKeyword> getModel() {
        return (Model) getDefaultModel();
    }

    private class ListPopupCloseable extends WebMarkupContainer implements IPopupCloseable {

        private ListChoice<TCKeyword> list;

        private TextField<TCKeyword> text;

        public ListPopupCloseable(ListChoice<TCKeyword> list, TextField<TCKeyword> text) {
            super("popup-keyword-list");
            this.list = list;
            this.text = text;
            setOutputMarkupId(true);
            setOutputMarkupPlaceholderTag(true);
            setVisible(false);
        }

        @Override
        public boolean isClosed() {
            return !isVisible();
        }

        @Override
        public void close(AjaxRequestTarget target) {
            close(target, this != null ? list.getModelObject() : null, true);
        }

        public void close(AjaxRequestTarget target, TCKeyword selectedKeyword, boolean updateSelection) {
            if (updateSelection) {
                TCKeywordListInput.this.getModel().setObject(selectedKeyword != null && selectedKeyword.isAllKeywordsPlaceholder() ? null : selectedKeyword);
            }
            MarkupContainer parent = TCKeywordListInput.this.getParent();
            setVisible(false);
            target.addComponent(this);
            if (parent != null) {
                target.addComponent(parent);
            } else {
                target.addComponent(text);
            }
        }
    }
}
