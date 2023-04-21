package coyousoft.javaee._06_servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import coyousoft.util.lesson.Lesson;

@Lesson(title = "使用HttpServlet处理客户端请求", lastModifed = "2008/09/01", keyword = { "doGet方法", "doPost方法", "解决中文乱码" }, content = { "1 doGet方法 ", "   处理GET请求的Servlet需要覆盖HttpServlet的doGet方法。GET调用在URL里显示正传给Servlet的数据", "，这在系统的安全方面可能带来一些问题。可以通过 methdo=GET 的表单、<a href=url>标签向服务器发", "送GET请求。", "                                                                                         ", "2 doPost方法 ", "   处理POST请求的Servlet需要覆盖HttpServlet的doPost方法。使用POST请求的好处是可以隐藏发送", "给服务器端的任何数据，POST适合发送大量的数据。可以通过 methdo=POST 的表单向服务器发送POST请求。", "                                                                                         ", "3 解决中文乱码 ", "   1 GET请求发送的数据，通过修改WEB容器的配置解决：", "       修改server.xml里Connector元素，增加 URIEncoding 属性 ", "   2 POST请求发送的数据，通过修改request对象获取参数的编码方式解决：", "       request.setCharacterEncoding(编码方式); ", "" })
public class _02_doGet_doPost_GreetingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        System.out.printf("■■■ %s %s %tT %tT%n", "_02_doGet_doPost_GreetingServlet.doGet", session.getId(), session.getCreationTime(), session.getLastAccessedTime());
        response.setContentType("text/html;charset=UTF-8");
        response.setBufferSize(8192);
        PrintWriter out = response.getWriter();
        out.print("<html>");
        out.print("    <head>");
        out.print("       <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        out.print("       <title>Hello</title>");
        out.print("    </head>");
        out.print("    <body bgcolor=\"white\">");
        out.print("        <h3>");
        out.print("            我的名字是浪子燕青。你的名字是？");
        out.print("        </h3>");
        out.print("        <form method=\"post\" action=\"greet\">");
        out.print("            <input type=\"text\" name=\"username\" size=\"25\">");
        out.print("            <input type=\"submit\" value=\"提交\">");
        out.print("            <input type=\"reset\" value=\"清空\">");
        out.print("        </form>");
        String username = request.getParameter("username");
        if ((username != null) && (username.length() > 0)) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/response");
            if (dispatcher != null) {
                dispatcher.include(request, response);
            }
        }
        out.print("    </body>");
        out.print("</html>");
        out.close();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "这个Servlet将为您提供温馨的问候。";
    }
}
