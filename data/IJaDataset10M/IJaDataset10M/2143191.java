package de.excrawler.server.Webcrawler.Html;

/**
 * Static class for safe html elements
 * @author Yves Hoppe <info at yves-hoppe.de>
 * @author Karpouzas George <www.webnetsoft.gr>
 */
public class SafeElements extends Thread {

    public static String check(String element) {
        element = element.toLowerCase();
        if (element.equals("h1")) return "h1";
        if (element.equals("h2")) return "h2";
        if (element.equals("h3")) return "h3";
        if (element.equals("h4")) return "h4";
        if (element.equals("h5")) return "h5";
        if (element.equals("h6")) return "h6";
        if (element.equals("p")) return "p";
        if (element.equals("pre")) return "pre";
        if (element.equals("blockquote")) return "blockquote";
        if (element.equals("b")) return "b";
        if (element.equals("ul")) return "ul";
        if (element.equals("ol")) return "ol";
        if (element.equals("li")) return "li";
        if (element.equals("dl")) return "dl";
        if (element.equals("dt")) return "dd";
        if (element.equals("table")) return "table";
        if (element.equals("thead")) return "thead";
        if (element.equals("body")) return "tbody";
        if (element.equals("tr")) return "tr";
        if (element.equals("th")) return "th";
        if (element.equals("td")) return "td";
        return null;
    }
}
