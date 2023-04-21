package com.triplea.packagemanager.controller;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import com.triplea.dao.Cube;
import com.triplea.dao.Database;
import com.triplea.packagemanager.PluginManagerConnector;

/**
 * Servlet implementation class for Servlet: RuleAjaxServlet
 *
 */
public class RuleAjaxServlet extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {

    static final long serialVersionUID = 1L;

    public RuleAjaxServlet() {
        super();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (null != action) {
            if (action.equalsIgnoreCase("loadcubes")) {
                this.loadCubes(request, response);
            } else if (action.equalsIgnoreCase("loaddimensions")) {
                this.loadDimensions(request, response);
            }
        }
    }

    private void loadCubes(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String output = "";
        String databaseName = request.getParameter("database");
        try {
            Database database = PluginManagerConnector.getInstance().getServerConnection().getDatabaseByName(databaseName);
            ArrayList<String> cubes = new ArrayList<String>();
            for (int i = 0; i < database.getCubeCount(); i++) {
                String cubeName = database.getCubeAt(i).getName();
                cubes.add(cubeName);
            }
            Collections.sort(cubes);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element cubesElement = doc.createElement("cubes");
            for (int i = 0; i < cubes.size(); i++) {
                Element cubeElement = doc.createElement("cube");
                cubeElement.setTextContent(cubes.get(i));
                cubesElement.appendChild(cubeElement);
            }
            doc.appendChild(cubesElement);
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLSerializer out = new XMLSerializer(outputStream, format);
            out.serialize(doc);
            output = outputStream.toString();
            outputStream.close();
            this.writeResponse(response, output);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void loadDimensions(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String output = "";
        String databaseName = request.getParameter("database");
        String cubeName = request.getParameter("cube");
        try {
            Database database = PluginManagerConnector.getInstance().getServerConnection().getDatabaseByName(databaseName);
            Cube cube = database.getCubeByName(cubeName);
            ArrayList<String> dimensions = new ArrayList<String>();
            for (int i = 0; i < cube.getDimensionCount(); i++) {
                String dimName = cube.getDimensionAt(i).getName();
                dimensions.add(dimName);
            }
            Collections.sort(dimensions);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element dimensionsElement = doc.createElement("dimensions");
            for (int i = 0; i < dimensions.size(); i++) {
                Element dimensionElement = doc.createElement("dimension");
                dimensionElement.setTextContent(dimensions.get(i));
                dimensionsElement.appendChild(dimensionElement);
            }
            doc.appendChild(dimensionsElement);
            OutputFormat format = new OutputFormat(doc);
            format.setIndenting(true);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            XMLSerializer out = new XMLSerializer(outputStream, format);
            out.serialize(doc);
            output = outputStream.toString();
            outputStream.close();
            this.writeResponse(response, output);
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
    }

    private void writeResponse(HttpServletResponse response, String output) throws IOException {
        response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write(output);
    }
}
