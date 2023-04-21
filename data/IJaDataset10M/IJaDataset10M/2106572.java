package pkg.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.ValidationErrors;
import pkg.dao.EntityDAO;
import pkg.dao.PagedList;
import pkg.entity.Teacher;
import pkg.entity.User;
import pkg.filter.TeacherFilter;
import pkg.filter.UserFilter;

public class UserActionBean extends BaseActionBean {

    private PagedList<User> users;

    private List<User> userList;

    private List<Teacher> teachers;

    private List<Long> userIds;

    private Integer page;

    private String sort;

    private String dir;

    private String mode;

    private String filterText;

    private User user;

    public UserActionBean() {
    }

    @DefaultHandler
    public Resolution view() {
        loadUsers();
        return new ForwardResolution("/UserList.jsp");
    }

    public void loadUsers() {
        EntityDAO sb;
        sb = new EntityDAO();
        UserFilter f = new UserFilter();
        filterText = (String) getSession().getAttribute("filterText");
        if (filterText != null) {
            f.setSimple(filterText);
        }
        userList = sb.findAllByFilter(f);
        users = new PagedList<User>();
        users.setResults(sb.findAllByFilter(f));
        if (page == null) {
            page = 1;
        }
        users.setPageNumber(page);
    }

    public Resolution addNew() {
        return new RedirectResolution("/User.action?create");
    }

    public Resolution deleteSelected() {
        EntityDAO sb = new EntityDAO();
        if (getUserIds() != null) {
            sb.delete(User.class, userIds);
        }
        loadUsers();
        RedirectResolution result;
        result = new RedirectResolution(this.getClass());
        result.addParameter("page", page);
        return result;
    }

    public Resolution preEdit() {
        EntityDAO sb;
        sb = new EntityDAO();
        user = sb.find(User.class, user.getId());
        loadLookups();
        return new ForwardResolution("/UserEdit.jsp");
    }

    public Resolution save() throws IOException {
        EntityDAO sb = new EntityDAO();
        User origItem = null;
        if (user.getId() != null) {
            origItem = sb.find(User.class, user.getId());
        }
        if (user.getChildName1() == null || (user.getChildTeacherId1() != null && user.getChildTeacherId1() == -1)) {
            user.setChildTeacherId1(null);
        }
        if (user.getChildName2() == null || (user.getChildTeacherId2() != null && user.getChildTeacherId2() == -1)) {
            user.setChildTeacherId2(null);
        }
        if (user.getChildName3() == null || (user.getChildTeacherId3() != null && user.getChildTeacherId3() == -1)) {
            user.setChildTeacherId3(null);
        }
        if (user.getChildName4() == null || (user.getChildTeacherId4() != null && user.getChildTeacherId4() == -1)) {
            user.setChildTeacherId4(null);
        }
        if (user.getAdmin() == null) {
            user.setAdmin(false);
        }
        sb.update(user);
        RedirectResolution result;
        result = new RedirectResolution(this.getClass());
        result.addParameter("page", page);
        return result;
    }

    @After(stages = LifecycleStage.BindingAndValidation)
    public void loadRequestIfErrors() {
        ValidationErrors ve = getContext().getValidationErrors();
        if (ve.size() > 0 && user != null && user.getId() != null) {
            EntityDAO sb = new EntityDAO();
            user = sb.find(User.class, user.getId());
        }
        loadLookups();
    }

    public Resolution create() {
        user = new User();
        mode = "add";
        loadLookups();
        return new ForwardResolution("/UserEdit.jsp");
    }

    public Resolution applyFilter() {
        if (filterText != null) {
            getSession().setAttribute("filterText", filterText);
        } else {
            getSession().removeAttribute("filterText");
        }
        return new RedirectResolution(this.getClass());
    }

    public void loadLookups() {
        teachers = new ArrayList<Teacher>();
        Teacher empty = new Teacher();
        empty.setId(-1L);
        empty.setLastName("None");
        teachers.add(empty);
        EntityDAO dao = new EntityDAO();
        TeacherFilter f = new TeacherFilter();
        teachers.addAll(dao.findAllByFilter(f));
    }

    public User getUser() {
        return user;
    }

    public void setUser(User category) {
        this.user = category;
    }

    public User getItem() {
        return user;
    }

    public void setItem(User item) {
        this.user = item;
    }

    public PagedList<User> getUsers() {
        return users;
    }

    public void setUsers(PagedList<User> items) {
        this.users = items;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> itemList) {
        this.userList = itemList;
    }

    public List<Long> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Long> UserIds) {
        this.userIds = UserIds;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
