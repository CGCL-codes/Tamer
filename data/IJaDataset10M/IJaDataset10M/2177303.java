package com.mycompany.web.user;

import net.joindesk.api.DeskletShell;
import net.joindesk.api.data.PageListData;
import net.joindesk.datafilter.DataFilter;
import net.joindesk.datafilter.ExpressionData;
import net.joindesk.datafilter.ValueConvert;
import com.mycompany.business.UserNoExistException;
import com.mycompany.database.Expression;
import com.mycompany.database.ExpressionHelper;
import com.mycompany.database.Filter;
import com.mycompany.database.PageList;
import com.mycompany.domain.User;

public class UserManagementDesklet extends AccountDesklet {

    private static final String FILTER_FIELD = "filter";

    private static final String LIST_FIELD = "list";

    private static final String FIELD_INDEX = "selectedIndex";

    private static final String BUTTON_EDIT = "edit_user_btn";

    private static final String BUTTON_REMOVE = "remove_user_btn";

    private static final String FIELD_ROLE = "role";

    private Filter filter;

    private PageList list;

    private User editUser;

    private int usersPageSize = -1;

    private static ValueConvert myConvert = new ValueConvert() {

        public boolean convert(ExpressionData exp) {
            if (exp.getField().equals("regDate") == false) return false;
            Object value = exp.getValue();
            if (value != null) {
                String str = value.toString();
                if (str.trim().equals("") || str.trim().equals("0")) exp.setValue(null); else exp.setValue(Long.valueOf(str));
            }
            return true;
        }
    };

    public void init(DeskletShell shell) {
    }

    public void find(DeskletShell shell) {
        DataFilter dataFilter = DataFilter.create(shell.getFieldValue(FILTER_FIELD));
        dataFilter.convert(new ValueConvert[] { myConvert });
        filter = new Filter();
        filter.setSize(getPageSize(shell));
        filter.setStart(0);
        Expression exp = ExpressionHelper.create(dataFilter);
        filter.add(exp);
        page(shell);
    }

    public String page(DeskletShell shell) {
        if (filter == null) {
            shell.setTipMessage("$ConversationTimeout");
            return "list";
        }
        filter.setSize(shell.getIntParameter("limit", filter.getSize()));
        filter.setStart(shell.getIntParameter("start", filter.getStart()));
        String sort = shell.getParameter("sort");
        if (sort != null && sort.trim().length() != 1) {
            boolean acending = true;
            String dir = shell.getParameter("dir");
            if (dir != null && dir.equalsIgnoreCase("DESC")) acending = false;
            filter.clearOrderBy();
            filter.addOrderBy(sort, acending);
        }
        list = userManager.query(filter);
        PageListData page = new PageListData(list, list.getTotalCount(), list.getStart());
        shell.setFieldDisabled(BUTTON_EDIT, true);
        shell.setFieldDisabled(BUTTON_REMOVE, true);
        shell.setFieldValue(LIST_FIELD, page);
        return null;
    }

    public String edit(DeskletShell shell) {
        if (list == null) {
            shell.setTipMessage("$ConversationTimeout");
            return "list";
        }
        int index = shell.getFieldIntValue(FIELD_INDEX);
        editUser = (User) list.get(index);
        setEdit(shell, true);
        setUserValue(shell, editUser);
        shell.setFieldValue(FIELD_ROLE, editUser.getRole());
        return "add";
    }

    public String add(DeskletShell shell) {
        setEdit(shell, false);
        return "add";
    }

    protected String addConfirm(DeskletShell shell) {
        User user = getUser(shell);
        if (user == null) return null;
        String role = shell.getFieldValue(FIELD_ROLE);
        if (role == null) role = User.ROLE_NORMAL;
        user.setRole(role);
        userManager.add(user);
        if (list == null) list = new PageList();
        list.add(user);
        shell.setTipMessage("$UserAdded");
        PageListData page = new PageListData(list, list.getTotalCount(), list.getStart());
        shell.setFieldDisabled(BUTTON_EDIT, true);
        shell.setFieldDisabled(BUTTON_REMOVE, true);
        shell.setFieldValue(LIST_FIELD, page);
        return "list";
    }

    protected String editConfirm(DeskletShell shell) {
        if (editUser == null) {
            shell.setTipMessage("$ConversationTimeout");
            return "list";
        }
        String admin = shell.getInitParameter("adminId", null);
        if (admin != null && admin.equals(editUser.getEmail())) {
            shell.setTipMessage("$CannotUpdateAdmin");
            return null;
        }
        if (getUserValue(shell, editUser) == false) return null;
        editUser.setRole(shell.getFieldValue(FIELD_ROLE));
        try {
            userManager.update(editUser);
        } catch (UserNoExistException e) {
            shell.setTipMessage("$UserNotExist");
            return null;
        }
        shell.setTipMessage("$UserUpdated");
        PageListData page = new PageListData(list, list.getTotalCount(), list.getStart());
        shell.setFieldDisabled(BUTTON_EDIT, true);
        shell.setFieldDisabled(BUTTON_REMOVE, true);
        shell.setFieldValue(LIST_FIELD, page);
        return "list";
    }

    private void setEdit(DeskletShell shell, boolean b) {
        shell.setRequestAttribute("edit", Boolean.toString(b));
        if (b) shell.setRequestAttribute("WindowTitle", shell.getLocaleMessage("EditUser")); else shell.setRequestAttribute("WindowTitle", shell.getLocaleMessage("AddUser"));
    }

    public String remove(DeskletShell shell) {
        if (list == null) {
            shell.setTipMessage("$ConversationTimeout");
            return "list";
        }
        int index = shell.getFieldIntValue(FIELD_INDEX);
        User user = (User) list.get(index);
        String admin = shell.getInitParameter("adminId", null);
        if (admin != null && admin.equals(user.getEmail())) {
            shell.setTipMessage("$CannotUpdateAdmin");
            return null;
        }
        try {
            userManager.remove(user.getEmail());
        } catch (UserNoExistException e) {
            shell.setTipMessage("$UserNotExist");
            return null;
        }
        shell.setTipMessage(shell.getLocaleMessage("UserRemoved", new Object[] { user.getEmail() }));
        page(shell);
        return null;
    }

    private int getPageSize(DeskletShell shell) {
        if (usersPageSize == -1) usersPageSize = shell.getInitParameter("pageSize", 20);
        return usersPageSize;
    }
}
