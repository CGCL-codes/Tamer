package net.sf.leechget.webapp.servlet.filemanager;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.leechget.Leechget;
import net.sf.leechget.webapp.manager.file.FileInstance;
import net.sf.leechget.webapp.manager.file.FileManager;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

/**
 * Servlet implementation class LoginServlet
 */
@RequestScoped
public class FileManagerServlet extends HttpServlet {

    private static final long serialVersionUID = 249928630922430674L;

    private final Leechget leechget;

    @Inject
    public FileManagerServlet(final Leechget leechget) {
        super();
        this.leechget = leechget;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        final FileManager fmanager = FileManager.getFileManager(request, leechget);
        final String pathInfo = request.getPathInfo();
        if (pathInfo == null) {
            fmanager.setCurrentDirectory(".");
        } else {
            fmanager.setCurrentDirectory("." + request.getPathInfo());
        }
        final FileInstance currentFile = fmanager.getCurrentDirectory();
        if (currentFile.isDirectory()) {
            handleListing(request, response, currentFile, fmanager);
        } else {
            handleReader(request, response, currentFile);
        }
    }

    private void handleListing(final HttpServletRequest request, final HttpServletResponse response, final FileInstance file, final FileManager fmanager) throws ServletException, IOException {
        try {
            request.setAttribute("files", fmanager.list());
        } catch (Exception e) {
            request.setAttribute("files", null);
        }
        request.setAttribute("dir", file);
        final RequestDispatcher dispatcher = request.getRequestDispatcher("/view/filemanager/list.jsp");
        if (dispatcher != null) {
            dispatcher.include(request, response);
            return;
        }
    }

    private void handleReader(final HttpServletRequest request, final HttpServletResponse response, final FileInstance file) throws ServletException, IOException {
        request.setAttribute("file", file);
        if (file.getName().endsWith(".txt")) {
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/view/filemanager/reader/text.jsp");
            if (dispatcher != null) {
                dispatcher.include(request, response);
                return;
            }
        } else {
            final RequestDispatcher dispatcher = request.getRequestDispatcher("/view/filemanager/download.jsp");
            if (dispatcher != null) {
                dispatcher.include(request, response);
                return;
            }
        }
    }
}
