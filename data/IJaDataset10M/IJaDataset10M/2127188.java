package FroG.view;

import javax.swing.JComponent;
import FroG.view.tab.XTabPage;

/**
 * @Declaration 
 * @author zhangzuoqiang
 * @version *.0 2011-11-11
 */
public class XTabUtil {

    public static XTabPage createPage(JComponent pageContent) {
        XTabPage page = new XTabPage(pageContent);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/home.png"), "首页", "home", false);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/left.png"), "后退", "left", true);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/right.png"), "前进", "right", true);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/add.png"), "添加", "add", true);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/update.png"), "更新", "update", true);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/refresh.png"), "刷新", "refresh", true);
        page.getToolBar().addButton(XControlUtil.getImageIcon("FroG/view/images/toolbar/print.png"), "打印", "print", true);
        return page;
    }
}
