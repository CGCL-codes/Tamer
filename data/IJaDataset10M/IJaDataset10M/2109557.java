package org.freem.love.client.atomwidgets;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.Window;
import com.google.gwt.core.client.GWT;
import org.freem.love.client.UsersService;
import org.freem.love.client.mainwidgets.DatingWidget;
import org.freem.love.client.beans.UserInListDataBean;
import org.freem.love.client.i18n.SearchWidgetRB;
import org.freem.love.client.formfields.*;
import java.util.List;

public class SearchWidget extends WithLocationWidget implements ClickListener {

    private final SearchWidgetRB rb;

    private FormFromToTextField lookingAges;

    private FormDropDownField looking;

    private FormDropDownField i;

    private FormCheckBoxesField options;

    {
        rb = (SearchWidgetRB) GWT.create(SearchWidgetRB.class);
    }

    public SearchWidget() {
        FormSubmitField formSubmit = new FormSubmitField("Поиск");
        formSubmit.addSumbitListener(this);
        form.addField(formSubmit);
        options = new FormCheckBoxesField(rb.optionsTitle(), rb.optionsComment(), rb.options());
        form.insertField(options, 0);
        lookingAges = new FormFromToTextField(rb.ageTilte(), rb.ageComment(), rb.fromAgeText(), rb.toAgeText(), rb.ageText());
        form.insertField(lookingAges, 0);
        looking = new FormDropDownField(rb.lookingFor() + " ", "", new String[] { rb.lookingForMan(), rb.lookingForWoman() });
        form.insertField(looking, 0);
        i = new FormDropDownField(rb.i() + " ", "", new String[] { rb.manSex(), rb.womanSex() });
        form.insertField(i, 0);
    }

    public void begin(String withUser) {
        form.clear();
    }

    public void onClick(Widget sender) {
        SearchDataBean searchData = new SearchDataBean();
        searchData.setFromAge(((FromToWidget) lookingAges.getFieldValue()).getFrom());
        searchData.setToAge(((FromToWidget) lookingAges.getFieldValue()).getTo());
        searchData.setLookingForSex(((ListBox) looking.getFieldValue()).getSelectedIndex() - 1);
        searchData.setMySex(((ListBox) i.getFieldValue()).getSelectedIndex() - 1);
        searchData.setOptions(((CheckBoxesWidget) options.getFieldValue()).getCheckedArray());
        searchData.setCountry(((ListBox) country.getFieldValue()).getSelectedIndex());
        searchData.setRegion(((ListBox) region.getFieldValue()).getSelectedIndex());
        searchData.setCity(((ListBox) city.getFieldValue()).getSelectedIndex());
        UsersService.App.getInstance().findUsers(searchData, new SearchCallback());
    }

    class SearchCallback implements AsyncCallback {

        public void onFailure(Throwable caught) {
            Window.alert(caught.getMessage());
        }

        public void onSuccess(Object result) {
            List usersList = (List) result;
            FlowPanel mainPanel = (FlowPanel) getWidget();
            if (usersList.size() == 0) {
                mainPanel.add(new Label("Никого не нашли"));
            }
            for (int i = 0; i < usersList.size(); i++) {
                UserInListDataBean user = (UserInListDataBean) usersList.get(i);
                Label nickname = new Label("||" + user.getName() + "||");
                nickname.addClickListener(new ShowProfileClickListener(user.getLogin()));
                mainPanel.add(nickname);
                mainPanel.add(new Label("--" + user.getAge() + "--"));
                Label messageLink = new Label("// поговорить //");
                messageLink.addClickListener(new ShowDialogClickListener(user.getLogin()));
                mainPanel.add(messageLink);
            }
        }
    }

    private class ShowDialogClickListener implements ClickListener {

        private String login;

        public ShowDialogClickListener(String login) {
            this.login = login;
        }

        public void onClick(Widget sender) {
            ((DatingWidget) SearchWidget.this.getParent().getParent().getParent()).showMessages(login);
        }
    }

    private class ShowProfileClickListener implements ClickListener {

        private String login;

        public ShowProfileClickListener(String login) {
            this.login = login;
        }

        public void onClick(Widget sender) {
            ((DatingWidget) SearchWidget.this.getParent().getParent().getParent()).showProfile(login);
        }
    }
}
