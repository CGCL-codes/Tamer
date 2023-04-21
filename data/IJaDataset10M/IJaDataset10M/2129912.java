package meraner81.jets.shared.util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import meraner81.jets.processing.parser.utils.BatchScript;
import meraner81.jets.processing.parser.utils.CompileScript;
import meraner81.utilities.io.FileUtils;

public class Compilation {

    private String enemyTerritoryPath;

    private String q3Map2Executable;

    private static final String q3map2Begin = "\"{0}\"";

    private static final String verbose = " -v";

    private static final String fsBasePath = " -fs_basepath \"{1}\"";

    private static final String q3map2End = " \"{2}\"";

    private String connectAddress;

    private String game;

    private String fsGame;

    private List<CompileScript> compileScripts;

    private List<BatchScript> batchList;

    public Compilation() {
        batchList = new ArrayList<BatchScript>();
        compileScripts = new ArrayList<CompileScript>();
        connectAddress = "127.0.0.1:39000";
        game = "et";
        fsGame = "etmain";
    }

    protected void addIntroClause(StringBuffer buffer) {
        buffer.append(q3map2Begin);
        buffer.append(verbose);
        addConnectAddress(buffer);
        addGame(buffer);
        buffer.append(fsBasePath);
        addFsGame(buffer);
    }

    protected void addTrailerClause(StringBuffer buffer) {
        buffer.append(q3map2End);
    }

    private void addConnectAddress(StringBuffer buffer) {
        buffer.append(" -connect ");
        buffer.append(connectAddress);
    }

    private void addGame(StringBuffer buffer) {
        buffer.append(" -game ");
        buffer.append(game);
    }

    private void addFsGame(StringBuffer buffer) {
        buffer.append(" -fs_game ");
        buffer.append(fsGame);
    }

    public void setEnemyTerritoryPath(String enemyTerritoryPath) {
        this.enemyTerritoryPath = enemyTerritoryPath;
    }

    public void setQ3Map2Executable(String q3map2Executable) {
        this.q3Map2Executable = q3map2Executable;
    }

    public String getEnemyTerritoryPath() {
        return enemyTerritoryPath;
    }

    public String getQ3Map2Executable() {
        return q3Map2Executable;
    }

    public void generateCompiler(String path, String mapPath) throws Exception {
        CompileScript script;
        for (int i = 0; i < compileScripts.size(); i++) {
            script = compileScripts.get(i);
            generateSingleCompiler(path, mapPath, script);
        }
        BatchScript batch;
        for (int i = 0; i < batchList.size(); i++) {
            batch = batchList.get(i);
            generateBatch(path, batch);
        }
    }

    private void generateBatch(String path, BatchScript batch) throws Exception {
        writeFile(path, batch.getFilename(), batch.getContent());
    }

    private void generateSingleCompiler(String path, String mapPath, CompileScript script) throws Exception {
        String compile = generateCaller(script);
        writeFile(path, "c_" + script.getName() + OperatingSystem.getBatchFileExtension(), compile);
        if (mapPath != null && mapPath.length() > 0 && mapPath.charAt(0) == '/') {
            mapPath = mapPath.substring(1);
        }
        if (script.isGenerateMeta()) {
            StringBuffer metaBuffer = new StringBuffer();
            addIntroClause(metaBuffer);
            script.addMetaPhase(metaBuffer);
            addTrailerClause(metaBuffer);
            String metaString = metaBuffer.toString();
            metaString = metaString.replace("{0}", q3Map2Executable);
            metaString = metaString.replace("{1}", enemyTerritoryPath);
            metaString = metaString.replace("{2}", mapPath);
            writeFile(path, script.getMetaPath(), metaString);
        }
        if (script.isGenerateVis()) {
            StringBuffer visBuffer = new StringBuffer();
            addIntroClause(visBuffer);
            script.addVisClause(visBuffer);
            addTrailerClause(visBuffer);
            String visString = visBuffer.toString();
            visString = visString.replace("{0}", q3Map2Executable);
            visString = visString.replace("{1}", enemyTerritoryPath);
            visString = visString.replace("{2}", mapPath);
            writeFile(path, script.getVisPath(), visString);
        }
        if (script.isGenerateLight()) {
            StringBuffer lightBuffer = new StringBuffer();
            addIntroClause(lightBuffer);
            script.addLightClause(lightBuffer);
            addTrailerClause(lightBuffer);
            String lightString = lightBuffer.toString();
            lightString = lightString.replace("{0}", q3Map2Executable);
            lightString = lightString.replace("{1}", enemyTerritoryPath);
            lightString = lightString.replace("{2}", mapPath);
            writeFile(path, script.getLightPath(), lightString);
        }
    }

    private String generateCaller(CompileScript script) {
        StringBuffer compile = new StringBuffer();
        compile.append("@echo off");
        compile.append(OperatingSystem.getLineSeparator());
        compile.append("rem ---------------------------------------------------------------------------");
        compile.append(OperatingSystem.getLineSeparator());
        compile.append("rem Compile script for the generated map");
        compile.append(OperatingSystem.getLineSeparator());
        compile.append("rem ---------------------------------------------------------------------------");
        compile.append(OperatingSystem.getLineSeparator());
        compile.append("echo Wait for this to close and then check the logs.");
        compile.append(OperatingSystem.getLineSeparator());
        if (script.isGenerateMeta()) {
            compile.append("call ");
            compile.append(script.getMetaPath());
            compile.append("   > ");
            compile.append(script.getMetaLogPath());
            compile.append(OperatingSystem.getLineSeparator());
        }
        if (script.isGenerateVis()) {
            compile.append("call ");
            compile.append(script.getVisPath());
            compile.append("    > ");
            compile.append(script.getVisLogPath());
            compile.append(OperatingSystem.getLineSeparator());
        }
        if (script.isGenerateLight()) {
            compile.append("call ");
            compile.append(script.getLightPath());
            compile.append(" > ");
            compile.append(script.getLightLogPath());
            compile.append(OperatingSystem.getLineSeparator());
        }
        return compile.toString();
    }

    private void writeFile(String path, String fileName, String content) throws Exception {
        FileWriter fw = FileUtils.getFileWriterFolderEnsured(path + "\\" + fileName);
        fw.write(content);
        fw.flush();
        fw.close();
    }

    public void addCompileScript(CompileScript compileScript) {
        this.compileScripts.add(compileScript);
    }

    public void addBatchScript(BatchScript batch) {
        batchList.add(batch);
    }
}
