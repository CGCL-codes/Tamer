package com.james.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author James liu
 */
public class string {

    public String slice(String src, int len) {
        String rel = src;
        try {
            rel = subString(src, len);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(string.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rel;
    }

    public String subString(String source, int len) throws UnsupportedEncodingException {
        String temp;
        String dest = "";
        byte[] b;
        for (int i = 0; i < source.length(); i++) {
            temp = source.substring(0, i);
            b = temp.getBytes("gbk");
            if (b.length > len) {
                dest = source.substring(0, i - 1);
                break;
            } else {
                continue;
            }
        }
        if (dest.equals("")) dest = source;
        return dest;
    }

    /**
 *字符串按限定长度截取返回
 *自动处理双字节字，自动加省略号
 */
    public String breviary(String src, int len) {
        String rel = src;
        try {
            rel = subString(src, len - 3) + "...";
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(string.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rel;
    }

    /**
     *返回HTML文档中的所有图片地址
     */
    public List getImages(String htmlStr) {
        String regex_hasimg = "<img[^>]*>";
        String regex_ct = "src=[\"]?[^\\s]*[\"]?\\s";
        String regex_picaddr = "=[\"]?[^\\s]*[\"]?\\s";
        List list = new ArrayList();
        Pattern p_hasimg = Pattern.compile(regex_hasimg, Pattern.CASE_INSENSITIVE);
        Matcher m_hasimg = p_hasimg.matcher(htmlStr);
        while (m_hasimg.find()) {
            for (int i = 0; i <= m_hasimg.groupCount(); i++) {
                Pattern p_ct = Pattern.compile(regex_ct, Pattern.CASE_INSENSITIVE);
                Matcher m_ct = p_ct.matcher(m_hasimg.group(i));
                boolean rs_ct = m_ct.find();
                Pattern p_picaddr = Pattern.compile(regex_picaddr, Pattern.CASE_INSENSITIVE);
                Matcher m_picaddr = p_picaddr.matcher(m_ct.group(0));
                boolean rs_picaddr = m_picaddr.find();
                String finalstr = m_picaddr.group(0).replaceAll("=", "").replaceAll("\"", "");
                list.add(finalstr.trim());
            }
        }
        return list;
    }

    /**
 *判断HTML文档中是否有图片
 */
    public boolean hasImg(String htmlStr) {
        String regex_hasimg = "<img[^>]*>";
        Pattern p_hasimg = Pattern.compile(regex_hasimg, Pattern.CASE_INSENSITIVE);
        Matcher m_hasimg = p_hasimg.matcher(htmlStr);
        boolean rs_hasimg = m_hasimg.find();
        if (rs_hasimg) return true; else return false;
    }

    /**
 *过滤html&script字符
 */
    public String filterHtml(String htmlStr) {
        return htmlStr.replaceAll("<[.[^<]]*>", "").replaceAll("\\s", "").replaceAll("&[^;]*;", "").replaceAll("\\u3000{1,}", "");
    }

    /**
 *为字符串过滤危险内容
 */
    public String filterDanger(String htmlStr) {
        htmlStr = filterScript(htmlStr);
        htmlStr = filterIframe(htmlStr);
        htmlStr = filterFrameset(htmlStr);
        htmlStr = filterEvent(htmlStr);
        htmlStr = filterLinkEvent(htmlStr);
        return htmlStr;
    }

    /**
 * 过滤js脚本
 * @param htmlStr
 * @return
 */
    public String filterScript(String htmlStr) {
        String newstring = htmlStr;
        Pattern p = Pattern.compile("<(/?script[^>]*)>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlStr);
        while (m.find()) {
            newstring = m.replaceFirst("&lt;" + m.group(1) + "&gt;");
            m = p.matcher(newstring);
        }
        return newstring;
    }

    /**
 * 过滤iframe标签
 * @param htmlStr
 * @return
 */
    public String filterIframe(String htmlStr) {
        String newstring = htmlStr;
        Pattern p = Pattern.compile("<(/?iframe[^>]*)>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlStr);
        while (m.find()) {
            newstring = m.replaceFirst("&lt;" + m.group(1) + "&gt;");
            m = p.matcher(newstring);
        }
        return newstring;
    }

    /**
 * 过滤frameset标签
 * @param htmlStr
 * @return
 */
    public String filterFrameset(String htmlStr) {
        String newstring = htmlStr;
        Pattern p = Pattern.compile("<(/?frameset[^>]*)>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlStr);
        while (m.find()) {
            newstring = m.replaceFirst("&lt;" + m.group(1) + "&gt;");
            m = p.matcher(newstring);
        }
        return newstring;
    }

    /**
 * 过滤<a href=javascript: 中的事件
 * @param htmlStr
 * @return
 */
    public String filterLinkEvent(String htmlStr) {
        String newstring = htmlStr;
        Pattern p = Pattern.compile("(\\s+href+\\s*=+\\s*[\"|\']?)(javascript:+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlStr);
        while (m.find()) {
            newstring = m.replaceFirst(m.group(1) + "#" + m.group(2));
            m = p.matcher(newstring);
        }
        return newstring;
    }

    /**
 * 过滤on**事件
 * @param htmlStr
 * @return
 */
    public String filterEvent(String htmlStr) {
        String newstring = htmlStr;
        Pattern p = Pattern.compile("\\s+(on[a-zA-Z]+\\s*=+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlStr);
        if (m.find()) {
            newstring = m.replaceAll(" _forbidden_" + m.group(1));
        }
        return newstring;
    }

    /**
 * 过滤电话号码
 * @param htmlStr
 * @return
 */
    public String filterPhone(String htmlStr) {
        Pattern p = Pattern.compile("(\\d{11})|(\\d{7})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))");
        Matcher m = p.matcher(htmlStr);
        String newstring = m.replaceAll("***");
        return newstring;
    }

    /**
 * 过滤QQ号码
 * @param htmlStr
 * @return
 */
    public String filterQQ(String htmlStr) {
        Pattern p = Pattern.compile("\\d{5,12}");
        Matcher m = p.matcher(htmlStr);
        String newstring = m.replaceAll("***");
        return newstring;
    }

    /**
 * 过滤邮件地址
 * @param htmlStr
 * @return
 */
    public String filterEmail(String htmlStr) {
        Pattern p = Pattern.compile("[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+");
        Matcher m = p.matcher(htmlStr);
        String newstring = m.replaceAll("***");
        return newstring;
    }

    /**
 *根据url得出domain
 */
    public String getDomain(String url) {
        if (url == null || url.equals("")) return "";
        String domain_patter = "(?<=http://)[\\w\\.]+[^/:]";
        Pattern p_doman = Pattern.compile(domain_patter, Pattern.CASE_INSENSITIVE);
        Matcher m_domain = p_doman.matcher(url);
        if (m_domain.find()) return m_domain.group(0); else return "";
    }

    /**
 *List 转成字符串，需指定分隔符，例如','
 */
    public String list2string(List list, String splitSign) {
        String result = "";
        if (!list.isEmpty()) {
            int xc = 0;
            for (Iterator i = list.listIterator(); i.hasNext(); xc++) {
                if (xc > 0) result += splitSign;
                result += (String) i.next();
            }
        }
        return result;
    }

    /**
 * 划词标记
 * @param src 原文
 * @param splt 分词词组,来自分词系统
 * @param color 标注颜色
 * @param headSize 前缀字符数
 * @param endSize 后缀字符数
 * @return
 */
    public String mark(String src, List splt, String color, int headSize, int endSize) {
        String rex = "([^\\p{Punct}|^[\\u3000-\\u303F]]{0," + headSize + "}[";
        String result = "";
        for (int i = 0; i < splt.size(); i++) {
            if (i > 0) rex += "|";
            rex += "" + splt.get(i).toString() + "";
        }
        rex += "]{2,}.{0," + endSize + "})";
        Pattern p_inlstr = Pattern.compile(rex);
        Matcher m_inlstr = p_inlstr.matcher(src);
        while (m_inlstr.find()) {
            for (int i = 0; i < m_inlstr.groupCount(); i++) {
                result += m_inlstr.group(i) + "...";
            }
        }
        for (int i = 0; i < splt.size(); i++) {
            Pattern p = Pattern.compile(splt.get(i).toString());
            Matcher m = p.matcher(result);
            result = m.replaceAll("<font color=\"" + color + "\">" + splt.get(i).toString() + "</font>");
        }
        if (result.equals("")) result = slice(src, headSize + endSize);
        return result;
    }
}
