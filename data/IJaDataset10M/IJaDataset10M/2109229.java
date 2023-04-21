package com.nhncorp.cubridqa.cases.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.nhncorp.cubridqa.CUBRIDAdvisor;
import com.nhncorp.cubridqa.cases.execart.ExecuteCartController;
import com.nhncorp.cubridqa.cases.execart.ExecuteCartView;
import com.nhncorp.cubridqa.controller.BaseController;
import com.nhncorp.cubridqa.controller.InjectViewer;
import com.nhncorp.cubridqa.model.Case;
import com.nhncorp.cubridqa.navigation.NavigationView;

/**
 * 
 * Is used to display the cases' names or refresh the names.
 * @ClassName: CaseManagerController
 * @Description: display and refresh the case name by directory.
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class CaseManagerController extends BaseController {

    @InjectViewer(viewId = CaseManagerView.ID, viewClass = CaseManagerView.class)
    private CaseManagerView managerView;

    @InjectViewer(viewId = NavigationView.ID, viewClass = NavigationView.class)
    private NavigationView navigationView;

    public CaseManagerController() {
        super();
    }

    /**
	 * display the case name by directory.
	 * 
	 * @param absPath
	 */
    public void freshListing(String absPath) {
        Case ca = new Case(absPath);
        freshListing(ca);
    }

    public void freshListing(Case ca) {
        String[] ls = null;
        if (ca.isLeaf()) {
            ls = ca.getAllFiles();
            CheckTable.setCheck(true);
        } else {
            ls = ca.listSubCategory();
            CheckTable.setCheck(false);
        }
        showCases(ls, ca);
        navigationView.freshTree(ca);
    }

    public void showCases(String[] ls, Case c) {
        List rs = new ArrayList();
        for (String path : ls) {
            if (path.indexOf(".svn") < 0) {
                Case ca = new Case(path);
                rs.add(ca);
            }
        }
        managerView.reload(rs, c);
    }

    public CaseManagerView getManagerView() {
        return managerView;
    }
}
