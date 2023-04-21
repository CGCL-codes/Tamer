package qa.positive_tests;

public class PassTestHtml extends Object {

    public PassTestHtml() {
        System.out.println("<html>");
        System.out.println("<head>");
        System.out.println("Make sure your project settings are set to view output as Html!");
        System.out.println("</head>");
        System.out.println("<body>");
        System.out.println("<h1>");
        System.out.println("HTML Output example</h1>");
        System.out.println("This is an example of HTML output.");
        System.out.println("<br>&nbsp;");
        System.out.println("<br>&nbsp;");
        System.out.println("<table BORDER NOSAVE >");
        System.out.println("<tr>");
        System.out.println("<td><font color=#3333FF>Result 1</font></td>");
        System.out.println("<td><font color=#CC33CC>Result 2</font></td>");
        System.out.println("</tr>");
        System.out.println("<tr>");
        System.out.println("<td><i>Very nice</i></td>");
        System.out.println("<td><b>Very easy</b></td>");
        System.out.println("</tr>");
        System.out.println("</table>");
        System.out.println("<ol>");
        System.out.println("<li>");
        System.out.println("This should appear first</li>");
        System.out.println("<li>");
        System.out.println("This will be second</li>");
        System.out.println("<li>");
        System.out.println("And finally third...</li>");
        System.out.println("</ol>");
        System.out.println("<font color=#33FF33>End of HTML example!</font>");
        System.out.println("</body>");
        System.out.println("</html>");
    }

    public static void main(String args[]) {
        PassTestHtml p = new PassTestHtml();
    }
}
