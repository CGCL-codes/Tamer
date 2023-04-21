package meta.codeanywhere.servlet;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import meta.codeanywhere.bean.SourceFile;
import meta.codeanywhere.bean.User;
import meta.codeanywhere.manager.FileManager;

public class DownloadServlet extends HttpServlet {

    private static final long serialVersionUID = 6559247326693923211L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FileManager fileManager = FileManager.getManager();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        SourceFile[] files = fileManager.getFilesByOwner(user);
        String[] names = new String[files.length];
        byte[][] contents = new byte[files.length][];
        for (int i = 0; i < files.length; i++) {
            names[i] = files[i].getFileName();
            Blob source = files[i].getSource();
            try {
                contents[i] = source.getBytes(1, (int) source.length());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        fileManager.outputZipPackage(names, contents, response.getOutputStream());
    }
}
