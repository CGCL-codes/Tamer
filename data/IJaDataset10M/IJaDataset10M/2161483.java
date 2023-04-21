package org.displaytag.pn.decorators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import org.displaytag.decorator.TableDecorator;
import com.kn.core.struts.base.BaseAction;
import com.pn.bo.Ngoaite;
import com.pn.bo.Ngoaitegiangay;
import com.pn.utils.StringUtils;

public class NgoaiTeDecorators extends TableDecorator {

    public String getNgoaiTeGiaNgayList() {
        StringBuffer result = new StringBuffer();
        Ngoaite object = (Ngoaite) getCurrentRowObject();
        Set<Ngoaitegiangay> ngoaiTeGiaNgaySet = object.getNgoaitegiangays();
        Iterator<Ngoaitegiangay> iter = ngoaiTeGiaNgaySet.iterator();
        boolean checkAuthor = this.checkAuthentication();
        result.append("<table border=\"0\">");
        while (iter.hasNext()) {
            Ngoaitegiangay ngoaiTeGiaNgay = iter.next();
            result.append("<tr>");
            result.append("<td style=\"text-align: left;\" width=\"20%\" >");
            String ngayHieuLuc = StringUtils.formatDatePerVietnamPattern(ngoaiTeGiaNgay.getNgay());
            result.append(ngayHieuLuc);
            result.append("</td>");
            result.append("<td style=\"text-align: right;\" width=\"30%\" >");
            result.append(ngoaiTeGiaNgay.getGiaNgoaiTe());
            result.append("</td>");
            if (checkAuthor) {
                result.append("<td style=\"text-align: center;\" width=\"50%\" >");
                result.append("<button type=\"button\" onclick=\"suaNgoaiTeGiaNgay('" + ngoaiTeGiaNgay.getId() + "');\">Sửa</button>");
                result.append("</td>");
            }
            result.append("</tr>");
        }
        result.append("</table>");
        return result.toString();
    }

    public String getThemGiaNgoaiTe() {
        boolean checkAuthor = this.checkAuthentication();
        StringBuffer result = new StringBuffer();
        Ngoaite object = (Ngoaite) getCurrentRowObject();
        if (checkAuthor) {
            result.append("<button type=\"button\" onclick=\"themNgoaiTeGiaNgay('" + object.getId() + "');\">Thêm giá</button>");
        }
        return result.toString();
    }

    public boolean checkAuthentication() {
        ArrayList<String> arrayList = BaseAction.getUserAuthorize();
        Iterator<String> iter = arrayList.iterator();
        while (iter.hasNext()) {
            String author = iter.next();
            if (author.equals("NGOAI_TE_WRITE")) {
                return true;
            }
        }
        return false;
    }
}
