package com.tinyblog.ajax2;

import java.io.IOException;
import java.util.*;
import org.jdom.*;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewsManager extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final int COUNTAPAGE = 6;

    /**
	 * 服务的总入口
	 */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        this.getNews(request, response);
    }

    /**
	 * 将所获取的新闻列表输出为客户端可视的XML文档
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
    private void getNews(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int currentPage = 0;
        if (request.getParameter("currentPage") != null) currentPage = Integer.parseInt(request.getParameter("currentPage"));
        NewsService service = new NewsService();
        Collection results = new ArrayList();
        int totalPage = 0;
        try {
            News[] newslist = service.getAllMessage();
            if ((newslist != null) && (newslist.length != 0)) {
                totalPage = (newslist.length % COUNTAPAGE == 0) ? (newslist.length / COUNTAPAGE) : (newslist.length / COUNTAPAGE + 1);
                if (currentPage <= 0) currentPage = 1;
                if (currentPage > totalPage) currentPage = totalPage;
                for (int i = ((currentPage - 1) * COUNTAPAGE); i < COUNTAPAGE * currentPage; i++) {
                    if (i >= newslist.length) break;
                    results.add(newslist[i]);
                }
            }
            response.setContentType("application/xml");
            Document doc = new Document();
            Element root = new Element("list");
            Element e_page = new Element("currentPage").addContent(String.valueOf(currentPage));
            root.addContent(e_page);
            Element e_total = new Element("totalPage").addContent(String.valueOf(totalPage));
            ;
            root.addContent(e_total);
            Element e_newslist = new Element("newslist");
            Iterator iterator = results.iterator();
            for (int i = 0; i < results.size(); i++) {
                News news = (News) iterator.next();
                Element e_news = new Element("news").setAttribute("id", news.getId());
                e_news.addContent(new Element("title").addContent(news.getTitle()));
                e_news.addContent(new Element("submittime").addContent(news.getSubmittime()));
                e_newslist.addContent(e_news);
            }
            root.addContent(e_newslist);
            doc.addContent(root);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat().setEncoding("ISO8859-1"));
            outputter.output(doc, response.getWriter());
        } catch (Exception ex) {
            this.handleError(ex);
        }
    }

    /**
	 * 错误处理
	 * @param ex
	 */
    private void handleError(Exception ex) {
        ex.printStackTrace();
        System.out.println(ex.toString());
    }

    /**
     * 处理页面转向
     * @param strForward
     * @param request
     * @param response
     * @throws IOException
     * @throws ServletException
     */
    private void forward(String strForward, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(strForward);
        dispatcher.forward((ServletRequest) request, (ServletResponse) response);
    }
}
