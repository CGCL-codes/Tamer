package com.itstherules.mediacentre.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import com.itstherules.mediacentre.Extension;
import com.itstherules.mediacentre.model.FileLinesList;
import com.itstherules.mediacentre.model.FileMp3Converter;
import com.itstherules.stream.model.DirectoriesModel;
import com.itstherules.stream.model.ItemsModel;

public class XmlPlaylistController extends BaseController {

    @Override
    public void list(Map<String, Object> parameters, HttpServletResponse response) throws IOException {
        List<File> files = new ArrayList<File>();
        if (null != parameters.get("fileName")) {
            files = asFiles(new FileLinesList().values((String) parameters.get("fileName")));
        } else {
            files = new ItemsModel("music", (String) parameters.get("directory"), Extension.mp3.toString()).asList();
        }
        parameters.put("items", new FileMp3Converter().convert(files));
        try {
            templateEngine.mergeNaked("xml/playlist.freemarker", parameters, response.getWriter());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<File> asFiles(List<String> values) {
        List<File> files = new ArrayList<File>();
        for (String line : values) {
            files.add(new File(new DirectoriesModel("music").getDirectoryPath() + line));
        }
        return files;
    }
}
