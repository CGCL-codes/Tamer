package com.platonov.model;

import com.platonov.dao.ModelDataDAO;
import com.platonov.utils.Converter;
import com.platonov.utils.jdbc.JDBCTemplate;
import com.platonov.utils.jdbc.JDBCType;
import javax.swing.*;
import java.awt.*;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: User
 * Date: 03.03.11   /    Time: 18:22
 */
public class Children {

    protected int BIRTH_CERTIFICATE_NUM = 0;

    protected int CHILD_NAME_COL_NUM = 1;

    protected int CHILD_BIRTH_DATE_COL_NUM = 2;

    protected int EMPL_NUM_COL_NUM = 3;

    protected BigInteger birthCertificateNum;

    protected String childName;

    protected Date childBirthDate;

    protected Long emplNum;

    public BigInteger getBirthCertificateNum() {
        return birthCertificateNum;
    }

    public void setBirthCertificateNum(BigInteger birthCertificateNum) {
        this.birthCertificateNum = birthCertificateNum;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public Date getChildBirthDate() {
        return childBirthDate;
    }

    public void setChildBirthDate(Date childBirthDate) {
        this.childBirthDate = childBirthDate;
    }

    public Long getEmplNum() {
        return emplNum;
    }

    public void setEmplNum(Long emplNum) {
        this.emplNum = emplNum;
    }

    public Children(BigInteger birthCertificateNum) {
        this.birthCertificateNum = birthCertificateNum;
        refresh();
    }

    public Children(BigInteger birthCertificateNum, String childName, Date childBirthDate, Long emplNum) {
        this.birthCertificateNum = birthCertificateNum;
        this.childName = childName;
        this.childBirthDate = childBirthDate;
        this.emplNum = emplNum;
    }

    public void refresh() {
        Object[] result = JDBCTemplate.getInstance().executeSelectForObjectOrNull(ModelDataDAO.getChildren, new Object[][] { { JDBCType.NUMBER, birthCertificateNum } });
        if (null != result && result.length > 0) {
            childName = (String) result[CHILD_NAME_COL_NUM];
            childBirthDate = Converter.toDate(result[CHILD_BIRTH_DATE_COL_NUM]);
            emplNum = Converter.toLong(result[EMPL_NUM_COL_NUM]);
        } else JOptionPane.showMessageDialog(new Frame(), "Запись \"" + birthCertificateNum + "\" в базе не найдена.", "Запись не найдена", JOptionPane.WARNING_MESSAGE);
    }

    public static Children create(BigInteger birthCertificateNum, String childName, Date childBirthDate, Long emplNum) {
        int res = JDBCTemplate.getInstance().executeUpdate(ModelDataDAO.insertChilderen, new Object[][] { { JDBCType.NUMBER, birthCertificateNum }, { JDBCType.VARCHAR2, childName }, { JDBCType.DATE, childBirthDate }, { JDBCType.NUMBER, emplNum } });
        if (res > 0) {
            return new Children(birthCertificateNum, childName, childBirthDate, emplNum);
        } else {
            JOptionPane.showMessageDialog(new Frame(), "Не удалось создась запись \"" + birthCertificateNum + "\" в базе.", "Ошибка при создании записи", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static Collection<Children> getAll() {
        Collection<Children> result = new ArrayList<Children>();
        ArrayList<ArrayList> res = JDBCTemplate.getInstance().executeSelect(ModelDataDAO.getChildrens, new Object[][] {});
        for (ArrayList arrayList : res) {
            result.add(new Children(Converter.toBigInteger(arrayList.get(0)), arrayList.get(1).toString(), Converter.toDate(arrayList.get(2)), Converter.toLong(arrayList.get(3))));
        }
        return result;
    }

    public static Collection<Children> getAllForEmpl(Long emplNum) {
        Collection<Children> result = new ArrayList<Children>();
        ArrayList<ArrayList> res = JDBCTemplate.getInstance().executeSelect(ModelDataDAO.getChldrenByEmpl, new Object[][] { { JDBCType.NUMBER, emplNum } });
        for (ArrayList arrayList : res) {
            result.add(new Children(Converter.toBigInteger(arrayList.get(0)), arrayList.get(1).toString(), Converter.toDate(arrayList.get(2)), Converter.toLong(arrayList.get(3))));
        }
        return result;
    }

    public int update() {
        return JDBCTemplate.getInstance().executeUpdate(ModelDataDAO.updateChildren, new Object[][] { { JDBCType.NUMBER, birthCertificateNum }, { JDBCType.VARCHAR2, childName }, { JDBCType.DATE, childBirthDate }, { JDBCType.NUMBER, emplNum }, { JDBCType.NUMBER, birthCertificateNum } });
    }

    public int delete() {
        return JDBCTemplate.getInstance().executeUpdate(ModelDataDAO.deleteChildren, new Object[][] { { JDBCType.NUMBER, birthCertificateNum } });
    }

    @Override
    public String toString() {
        return childName;
    }
}
