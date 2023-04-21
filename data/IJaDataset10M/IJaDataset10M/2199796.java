package com.michael.common.mvc;

import com.michael.common.Factory;
import android.app.Activity;
import android.view.MenuItem;
import android.view.View;

/**
 * 视图基类
 * 
 * @author Administrator
 * 
 */
public class BaseActivityView extends Activity {

    protected String controllerPackage = "com.michael.gps.controller";

    private ViewClickController contorller;

    protected Class<?> clazz;

    public BaseActivityView() {
        initController();
    }

    private void initController() {
        try {
            if (clazz == null) {
                String viewName = this.getClass().getName();
                String controllerName = controllerPackage + viewName.substring(viewName.lastIndexOf(".")).replace("View", "Controller");
                clazz = Class.forName(controllerName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        contorller = (ViewClickController) Factory.getInstance(clazz);
    }

    /**
	 * 绑定onclick事件到控制器
	 * 
	 * @param id
	 *            控件id
	 * @param clazz
	 *            控制器类
	 */
    protected void bindClickController(int id) {
        contorller.setActivity(this);
        ((View) this.findViewById(id)).setOnClickListener(contorller);
    }

    /**
	 * 绑定menu事件到控制器
	 */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        contorller.setActivity(this);
        contorller.onClick(featureId, item);
        return true;
    }
}
