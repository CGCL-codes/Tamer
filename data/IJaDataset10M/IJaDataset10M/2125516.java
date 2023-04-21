package meraner81.jets.processing.parser.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;
import meraner81.jets.processing.apps.AutoShader;
import meraner81.jets.shared.util.OperatingSystem;

public class ShaderParser {

    private ShaderInfo shaderInfo;

    private AutoShaderInfo autoShaderInfo;

    public ShaderParser(ShaderInfo info) {
        if (info instanceof AutoShaderInfo) {
            autoShaderInfo = (AutoShaderInfo) info;
        } else {
            shaderInfo = info;
        }
    }

    public boolean isAutoShaderInfo() {
        return (autoShaderInfo != null);
    }

    public static Set<String> extractShaderNames(String fileName) {
        ShaderItemList shaderItemList = new ShaderItemList();
        return extractShaderNames(fileName, shaderItemList);
    }

    public static Set<String> extractShaderNames(String fileName, ShaderItemList shaderItemList) {
        try {
            Set<String> s = new HashSet<String>();
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            StringBuffer contentCache = new StringBuffer();
            ShaderItem item = null;
            String line;
            String trimedLine;
            int parCount = 0;
            while ((line = br.readLine()) != null) {
                trimedLine = line.trim();
                if (trimedLine.startsWith("//")) {
                } else if (trimedLine.equals("{")) {
                    contentCache.append(line);
                    contentCache.append(OperatingSystem.getLineSeparator());
                    parCount++;
                } else if (trimedLine.equals("}")) {
                    contentCache.append(line);
                    contentCache.append(OperatingSystem.getLineSeparator());
                    parCount--;
                    if (parCount == 0 && item != null) {
                        item.setContent(contentCache.toString());
                        shaderItemList.add(item);
                        contentCache = new StringBuffer();
                    }
                } else {
                    if (parCount == 0) {
                        if (!trimedLine.equals("")) {
                            String shaderName = trimedLine.replaceAll("\\\\", "/");
                            s.add(shaderName);
                            item = new ShaderItem(shaderName);
                        }
                    } else {
                        contentCache.append(line);
                        contentCache.append(OperatingSystem.getLineSeparator());
                    }
                }
            }
            return s;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(extractShaderNames("resources/basefiles/shader/header.txt"));
    }

    public ShaderItemList extractShaderItems() {
        ShaderItemList shaderItemList = new ShaderItemList();
        String fileName = (isAutoShaderInfo()) ? (autoShaderInfo.getHeaderFile()) : (shaderInfo.getInputShaderPath());
        if (fileName != null) {
            extractShaderNames(fileName, shaderItemList);
        }
        if (isAutoShaderInfo()) {
            AutoShader autoShader = new AutoShader();
            autoShader.autoShade(autoShaderInfo, shaderItemList);
        }
        return shaderItemList;
    }
}
