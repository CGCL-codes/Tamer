package com.FSS.Query;

import javax.swing.JOptionPane;
import com.FSS.File.IFile;

public class FinderOfNum extends Finder {

    public FinderOfNum(ResultsManager rm) {
        this.rm = rm;
    }

    @Override
    public void differentFind(SearchCondition sc) {
        try {
            int num = Integer.valueOf(sc.getContent());
            for (IFile file : whichList) {
                if (file.getNum() == num) result.add(file);
            }
        } catch (NumberFormatException e) {
            result = null;
            JOptionPane.showMessageDialog(null, sc.getCondition() + "���ǺϷ����·�,����������");
            return;
        }
    }
}
