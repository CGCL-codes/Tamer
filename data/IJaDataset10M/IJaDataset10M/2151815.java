package com.study.pepper.client.ui.member;

import javax.swing.JDesktopPane;
import com.study.jslib.json.JSONArray;
import com.study.jslib.json.JSONObject;
import com.study.pepper.client.ui.basic.AbstractCallback;
import com.study.pepper.client.ui.basic.BasicList;
import com.study.pepper.client.utils.ServerManager;

/**
 * 
 * @author Миша
 *
 */
public class MembersList extends BasicList {

    public MembersList(String title, JDesktopPane desktopPane) {
        super(title, desktopPane);
    }

    @Override
    protected void addAddButton() {
    }

    @Override
    protected JSONArray fetchData() {
        return new JSONArray(ServerManager.getInstance().getMembersList());
    }

    @Override
    protected ColumnConfig[] getColumnsConfig() {
        ColumnConfig lastname = new ColumnConfig("Фамилия", "lastname", 90);
        ColumnConfig firstname = new ColumnConfig("Имя", "firstname", 90);
        ColumnConfig middlename = new ColumnConfig("Отчество", "middlename", 90);
        ColumnConfig login = new ColumnConfig("Логин", "login", 60);
        ColumnConfig description = new ColumnConfig("Описание", "description", 200);
        return new ColumnConfig[] { lastname, firstname, middlename, login, description };
    }

    @Override
    protected void addRow(AddCallback callback) {
        edit(null, callback);
    }

    @Override
    protected void editRow(JSONObject row, EditCallback callback) {
        edit(row, callback);
    }

    private void edit(JSONObject row, AbstractCallback callback) {
        new MemberCard("Карточка пользователя", row, (JDesktopPane) getContentPane(), callback);
    }

    @Override
    protected void removeRow(JSONArray json, DeleteCallback deleteCallback) {
        ServerManager.getInstance().removeMember(json);
    }
}
