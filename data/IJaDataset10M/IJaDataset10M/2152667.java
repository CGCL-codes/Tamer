package com.jgeppert.struts2.jquery.mobile.showcase;

import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import java.util.LinkedList;
import java.util.List;

@ParentPackage(value = "showcase")
public class ListView extends ActionSupport {

    private List<ListEntry> myList;

    @Actions({ @Action(value = "/list-view", results = { @Result(location = "list-view.jsp", name = "success") }), @Action(value = "/list-view-counter", results = { @Result(location = "list-view-counter.jsp", name = "success") }) })
    public String execute() throws Exception {
        myList = new LinkedList<ListEntry>();
        myList.add(new ListEntry("entry1", "Entry One", 13));
        myList.add(new ListEntry("entry2", "Entry Two", 3));
        myList.add(new ListEntry("entry3", "Entry Three", 9));
        myList.add(new ListEntry("entry4", "Entry Four", 2));
        myList.add(new ListEntry("entry5", "Entry Five", 11));
        return SUCCESS;
    }

    public List<ListEntry> getMyList() {
        return myList;
    }

    public class ListEntry {

        private String key;

        private String title;

        private int count;

        public ListEntry(String key, String title, int count) {
            this.key = key;
            this.title = title;
            this.count = count;
        }

        public String getKey() {
            return key;
        }

        public String getTitle() {
            return title;
        }

        public int getCount() {
            return count;
        }
    }
}
