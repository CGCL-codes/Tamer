package meraner81.jets.processing.parser.shader;

import java.awt.Color;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.vecmath.Color3f;
import meraner81.jets.shared.util.ColorConverter;
import meraner81.jets.shared.util.OperatingSystem;
import meraner81.utilities.lang.StringUtils;

public class ColorShader {

    private String name;

    private String shaderPath;

    private Map<Color, Set<String>> colorShaderReplacements;

    private Set<String> cacheReplacements;

    public ColorShader() {
        colorShaderReplacements = new LinkedHashMap<Color, Set<String>>();
        cacheReplacements = new HashSet<String>();
        shaderPath = "base_colors/";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShaderName(Color c) {
        String name = "rgb";
        if (StringUtils.notNullOrEmpty(this.name)) {
            name = this.name;
        }
        return shaderPath + name + "_" + c.getRed() + "_" + c.getGreen() + "_" + c.getBlue();
    }

    public String getColorReplacementFor(String texture) {
        if (cacheReplacements.contains(texture)) {
            Set<String> textures;
            for (Color color : colorShaderReplacements.keySet()) {
                textures = colorShaderReplacements.get(color);
                if (textures.contains(texture)) {
                    return getShaderName(color);
                }
            }
        }
        return null;
    }

    public static String createShader(String name, Color3f color) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("textures/");
        buffer.append(name);
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("{");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("  {");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("    map $lightmap");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("    rgbgen identity");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("  }");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("  {");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("    map $whiteimage");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("    rgbgen const ( ");
        buffer.append(color.getX());
        buffer.append(" ");
        buffer.append(color.getY());
        buffer.append(" ");
        buffer.append(color.getZ());
        buffer.append(" )");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("    blendfunc gl_dst_color gl_zero");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("  }");
        buffer.append(OperatingSystem.getLineSeparator());
        buffer.append("}");
        return buffer.toString();
    }

    public static void main(String[] args) {
        ColorShader shader = new ColorShader();
        shader.setName("myrgb");
        Color c = new Color(0, 255, 0);
        Color3f color = ColorConverter.convertRGBToColor3f(c);
        System.out.println(ColorShader.createShader("unibibcolor/" + shader.getShaderName(c), color));
    }

    public void addReplacement(String textureName, Color3f color) {
        Set<String> textureList = colorShaderReplacements.get(color.get());
        if (textureList == null) {
            textureList = new HashSet<String>();
            colorShaderReplacements.put(color.get(), textureList);
        }
        textureList.add(textureName);
        cacheReplacements.add(textureName);
    }

    public StringBuffer getShaderFileContent() {
        StringBuffer buffer = new StringBuffer();
        for (Color color : colorShaderReplacements.keySet()) {
            buffer.append(createShader(getShaderName(color), ColorConverter.convertRGBToColor3f(color)));
            buffer.append(OperatingSystem.getLineSeparator());
        }
        return buffer;
    }

    public String getShaderPath() {
        return shaderPath;
    }

    public void setShaderPath(String shaderPath) {
        this.shaderPath = shaderPath;
    }
}
