package org.monet.modelling.ide.builders.stages;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.monet.modelling.ide.agents.AgentFilesystem;
import org.monet.modelling.ide.builders.IGlobalData;
import org.monet.modelling.ide.builders.Stage;
import org.monet.modelling.ide.builders.StageState;
import org.monet.modelling.ide.builders.errors.InternalFatalError;
import org.monet.modelling.ide.library.LibraryFile;

public class Packaging extends Stage {

    private String packagePath;

    private String outputPath;

    private String themePath;

    private String helpPath;

    @Override
    public Object getOutData() {
        return packagePath;
    }

    @Override
    public void setInData(Object data) {
    }

    @Override
    public void execute() {
        System.out.println("Packaging");
        this.state = StageState.COMPLETE;
        ZipOutputStream zip;
        String projectName;
        File oDestFile;
        try {
            outputPath = (String) this.globalData.getData(IGlobalData.PROJECT_OUTPUT_PATH);
            themePath = (String) this.globalData.getData(IGlobalData.THEME_PATH);
            helpPath = (String) this.globalData.getData(IGlobalData.HELP_PATH);
            projectName = (String) this.globalData.getData(IGlobalData.PROJECT_NAME);
            oDestFile = new File(String.format(outputPath + "/%s.zip", projectName));
            AgentFilesystem.removeFile(oDestFile.getAbsolutePath());
            oDestFile.createNewFile();
            zip = new ZipOutputStream(new FileOutputStream(oDestFile));
            AgentFilesystem.copyDir(helpPath, outputPath + "/" + LibraryFile.getFilename(helpPath));
            compress(outputPath, zip);
            compressAuxFiles(themePath, zip);
            zip.close();
        } catch (Exception e) {
            this.state = StageState.COMPLETE_WITH_ERRORS;
            this.problems.add(new InternalFatalError(null, e.getMessage()));
            e.printStackTrace();
        }
    }

    private void compressAuxFiles(String path, ZipOutputStream zip) throws IOException {
        String[] dirList = AgentFilesystem.listDir(path);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (String filename : dirList) {
            File file = new File(path, filename);
            if (file.isDirectory()) {
                if (file.getPath().contains("_images") || file.getPath().contains("_styles")) compressAuxFiles(file.getPath(), zip);
                continue;
            }
            FileInputStream input = new FileInputStream(file);
            String entryPath = file.getCanonicalPath().substring(themePath.length() + 1, file.getCanonicalPath().length());
            ZipEntry entry = new ZipEntry(entryPath);
            zip.putNextEntry(entry);
            while ((bytesIn = input.read(readBuffer)) != -1) {
                zip.write(readBuffer, 0, bytesIn);
            }
            input.close();
        }
    }

    public void compress(String dir2zip, ZipOutputStream zip) throws IOException {
        String[] dirList = AgentFilesystem.listDir(dir2zip);
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (String filename : dirList) {
            File file = new File(dir2zip, filename);
            if (file.isDirectory()) {
                String filepath = file.getPath();
                compress(filepath, zip);
                continue;
            }
            if (LibraryFile.getExtension(file.getName()).equals("zip")) continue;
            FileInputStream input = new FileInputStream(file);
            String entryPath = file.getCanonicalPath().substring(outputPath.length() + 1, file.getCanonicalPath().length());
            ZipEntry entry = new ZipEntry(entryPath);
            zip.putNextEntry(entry);
            while ((bytesIn = input.read(readBuffer)) != -1) {
                zip.write(readBuffer, 0, bytesIn);
            }
            input.close();
        }
    }
}
