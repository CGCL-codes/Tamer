package com.FSS.Query;

import javax.swing.JOptionPane;
import com.FSS.File.IFile;

public class FinderOfMonth extends Finder {

    public FinderOfMonth(ResultsManager rm) {
        this.rm = rm;
    }

    private boolean isMonth(String month) {
        try {
            int mon = Integer.valueOf(month.trim());
            return mon >= 1 && mon <= 12;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void differentFind(SearchCondition sc) {
        if (!isMonth(sc.getContent())) {
            result = null;
            JOptionPane.showMessageDialog(null, sc.getCondition() + "���ǺϷ����·�,����������");
            return;
        }
        for (IFile file : this.whichList) {
            if (file.getMonth() == Integer.valueOf(sc.getContent().trim())) {
                result.add(file);
            }
        }
    }
}
