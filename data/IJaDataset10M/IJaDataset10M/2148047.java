package com.beam.impl.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.beam.base.BaseAction;
import com.beam.base.Service;
import com.beam.impl.bookmark.BookmarkService;
import com.beam.impl.permission.Permission;
import com.beam.impl.permission.PermissionService;

/**
 * 
 * ������������ҳ����
 * 
 * @author beam
 * 2010-12-30  ����03:36:19
 */
@Controller
public class LayoutAction extends BaseAction<User> {

    @Autowired
    UserService userService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    BookmarkService bookmarkService;

    @Override
    public Service<User> getService() {
        return userService;
    }

    /**
	 * ��ҳ
	 * @return
	 */
    @RequestMapping("/main")
    public String main() {
        return "main";
    }

    /**
	 * ���˵�
	 * @return
	 */
    @RequestMapping("/layout/leftmenu")
    public String leftMenu(Model model) {
        menu(model);
        return "layout/leftmenu";
    }

    /**
	 * �ϲ�˵�
	 */
    @RequestMapping("/layout/topmenu")
    public String topMenu(Model model) {
        menu(model);
        return "layout/topmenu";
    }

    /**
	 * �Ҳ�˵�
	 * @param model
	 * @return
	 */
    @RequestMapping("/layout/rightmenu")
    public String rightMenu(Model model) {
        model.addAttribute("bookmarks", bookmarkService.findAllByUser(getSessionUser().getId()));
        return "layout/rightmenu";
    }

    /**
	 * �˵���ȡ
	 * @param model
	 */
    public void menu(Model model) {
        User user = getSessionUser();
        String groups = user.getGroups();
        List<Permission> permissionList = permissionService.findMenuByGroupIds(groups);
        model.addAttribute("permissions", permissionList);
    }
}
