package com.FSS.util;

import javax.swing.JOptionPane;
import com.FSS.FileList.FileList;
import com.FSS.Query.ResultsManager;
import com.FSS.Query.SearchCondition;
import com.FSS.ui.SearchAndResultPanel;

public class SearchThread extends Thread {

    ResultsManager rm;

    SearchAndResultPanel sarp;

    SearchCondition sc;

    public SearchThread(ResultsManager rm, SearchAndResultPanel sarp, SearchCondition sc) {
        this.rm = rm;
        this.sarp = sarp;
        this.sc = sc;
    }

    public void run() {
        FileList result = rm.getResult(sc);
        if (result != null) {
            sarp.getResTable().setData(result);
            sarp.getSp().setComponentsEnable(true);
            if (result.isEmpty()) JOptionPane.showMessageDialog(null, "���ź�,û������Ҫ�Ľ��");
        } else {
            sarp.getSp().setComponentsEnable(true);
        }
    }
}
