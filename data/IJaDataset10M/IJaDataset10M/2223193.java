package com.goodcodeisbeautiful.archtea.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.goodcodeisbeautiful.archtea.config.ArchteaMainConfig;
import com.goodcodeisbeautiful.archtea.config.DefaultConfigManager;
import com.goodcodeisbeautiful.archtea.search.ArchteaIndexer;

/**
 * @author hata
 *
 */
public class ArchteaIndexerServlet extends HttpServlet {

    /**
     * <code>serialVersionUID</code> comment.
     */
    private static final long serialVersionUID = -7466878695709149846L;

    public static final String ARCHTEA_MAIN_CONFIG_KEY = "archtea.main.config";

    public static final String ARCHTEA_DOCS_PATH_KEY = "archtea.docs.path";

    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String mainConfigPath = System.getProperty(ArchteaIndexerServlet.ARCHTEA_MAIN_CONFIG_KEY);
        if (mainConfigPath == null) throw new ServletException(ARCHTEA_MAIN_CONFIG_KEY + " Parameter is null.");
        try {
            DefaultConfigManager.getInstance().setMainConfigPath(mainConfigPath);
            DefaultConfigManager.getInstance().verifyConfigs();
            ArchteaMainConfig mainConfig = DefaultConfigManager.getInstance().getMainConfig();
            int count = mainConfig.getFolderConfig().size();
            for (int i = 0; i < count; i++) {
                copySampleDocs(mainConfig.getFolderConfig().getFolderConfig(i).getAbsoluteRootPath());
            }
            ArchteaIndexer indexer = ArchteaIndexer.newInstance(mainConfig);
            res.getWriter().println("ArchteaIndexerServlet for " + mainConfigPath + " started.");
            indexer.runIndexer();
            res.getWriter().println("ArchteaIndexerServlet finished.");
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        this.doGet(req, res);
    }

    private void copySampleDocs(String folderRootPath) throws ServletException, IOException {
        File folderRootFile = new File(folderRootPath);
        if (!folderRootFile.exists()) {
            if (!folderRootFile.mkdirs()) {
                throw new ServletException("Failed to create directory " + folderRootPath);
            }
            String docsPath = System.getProperty(ArchteaIndexerServlet.ARCHTEA_DOCS_PATH_KEY);
            if (docsPath == null) throw new ServletException("Sample docs path is null. ");
            File docsFile = new File(docsPath);
            if (!docsFile.exists()) throw new ServletException("Sample docs file doesn't exist. docs path is " + docsPath);
            FileInputStream in = null;
            FileOutputStream out = null;
            try {
                in = new FileInputStream(docsFile);
                out = new FileOutputStream(folderRootPath + File.separator + docsFile.getName());
                byte[] buff = new byte[1024];
                int len = in.read(buff);
                while (len != -1) {
                    out.write(buff, 0, len);
                    len = in.read(buff);
                }
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Exception e) {
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
}
