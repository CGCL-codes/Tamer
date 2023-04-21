package org.londonwicket.osiv.phonebook;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.navigation.paging.PagingNavigator;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.londonwicket.osiv.DataRequestCycle;
import org.londonwicket.osiv.jpa.JpaThreadLocal;

public class NumbersPage extends WebPage {

    public NumbersPage() {
        add(new Link("addNumber") {

            @Override
            public void onClick() {
                setResponsePage(AddNumberPage.class);
            }
        });
        DataView listview = new DataView("beanRow", new NumberProvider(), 5) {

            @Override
            protected void populateItem(Item item) {
                PhoneBookEntry entry = (PhoneBookEntry) item.getModelObject();
                item.add(new Label("name", entry.getName()));
                item.add(new Label("number", entry.getNumber()));
                item.add(new UpdateLink("updateNumber", entry));
                item.add(new DeleteLink("deleteNumber", entry));
            }
        };
        add(listview);
        PagingNavigator navigator = new PagingNavigator("navigator", listview);
        add(navigator);
    }

    private class UpdateLink extends Link {

        private PhoneBookEntry entry;

        public UpdateLink(String id, PhoneBookEntry entry) {
            super(id);
            this.entry = entry;
        }

        public void onClick() {
            this.setResponsePage(new UpdateNumberPage(entry));
        }
    }

    private class DeleteLink extends Link {

        private PhoneBookEntry entry;

        public DeleteLink(String id, PhoneBookEntry entry) {
            super(id);
            this.entry = entry;
        }

        public void onClick() {
            entry = (PhoneBookEntry) JpaThreadLocal.get().find(entry.getClass(), entry.getId());
            JpaThreadLocal.get().remove(entry);
            JpaThreadLocal.get().flush();
            ((DataRequestCycle) this.getRequestCycle()).onSubmit();
            this.setResponsePage(NumbersPage.class);
        }
    }
}
