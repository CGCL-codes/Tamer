package com.zhongkai.service.config;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.zhongkai.dao.config.MenuDAO;
import com.zhongkai.model.config.Menu;

@Component
@Transactional
public class MenuService {

    private MenuDAO menuDAO;

    public void save(Menu menu) throws Exception {
        menuDAO.insert(menu);
    }

    public boolean delete(Menu menu) throws Exception {
        List<Menu> childs = this.findChildById(menu.getMenuId());
        if (childs == null || childs.size() < 1) {
            menuDAO.delete(menu);
            return true;
        } else return false;
    }

    @SuppressWarnings("unchecked")
    public boolean isExistActionKey(String actionKey) throws Exception {
        if (actionKey == null || "".equals(actionKey)) return false;
        List<Menu> menus = menuDAO.select("from Menu m where m.actionKey='" + actionKey + "'");
        if (menus == null || menus.size() < 1) return false; else return true;
    }

    @SuppressWarnings("unchecked")
    public List<Menu> findChildById(Integer menu_id) throws Exception {
        return menuDAO.select("from Menu m where m.menuParent=" + menu_id);
    }

    @SuppressWarnings("unchecked")
    public Menu findBytActionKey(String actionKey) throws Exception {
        if (actionKey == null || "".equals(actionKey)) return null;
        List<Menu> menus = menuDAO.select("from Menu m where m.actionKey='" + actionKey + "'");
        if (menus != null && menus.size() > 0) {
            return menus.get(0);
        }
        return null;
    }

    public void update(Menu menu) throws Exception {
        menuDAO.update(menu);
    }

    public Menu findById(Integer menu_id) throws Exception {
        return (Menu) menuDAO.selectById(Menu.class, menu_id);
    }

    public Integer count() throws Exception {
        return menuDAO.count("from Menu");
    }

    @SuppressWarnings("unchecked")
    public List<Menu> findAll() throws Exception {
        return menuDAO.select("from Menu");
    }

    @SuppressWarnings("unchecked")
    public boolean isExist(String menu_name, Integer menu_parent) throws Exception {
        List<Menu> menus = menuDAO.select("from Menu m where m.menuName='" + menu_name + "' and m.menuParent=" + menu_parent);
        if (menus == null || menus.size() < 1) return false; else return true;
    }

    public MenuDAO getMenuDAO() {
        return menuDAO;
    }

    @Resource
    public void setMenuDAO(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }
}
