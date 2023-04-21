package org.alcibiade.eternity.editor.model;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PatternFactory {

    private static List<Pattern> patterns;

    static {
        patterns = new ArrayList<Pattern>();
        try {
            loadPatterns(new File("EternityII_patterns.txt"));
        } catch (IOException e) {
            patterns.add(new Pattern(0, Pattern.COL_GRAY, Pattern.COL_GRAY, Pattern.SHAPE_NONE));
            patterns.add(new Pattern(1, Pattern.COL_DARKBLUE, Pattern.COL_YELLOW, Pattern.SHAPE_FLOWER));
            patterns.add(new Pattern(2, Pattern.COL_ORANGE, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_ROUNDCROSS));
            patterns.add(new Pattern(3, Pattern.COL_PURPLE, Pattern.COL_YELLOW, Pattern.SHAPE_CROSSBALLS));
            patterns.add(new Pattern(4, Pattern.COL_DARKPURPLE, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_LYS));
            patterns.add(new Pattern(5, Pattern.COL_MAGENTA, Pattern.COL_YELLOW, Pattern.SHAPE_STAR));
            patterns.add(new Pattern(6, Pattern.COL_LIGHTBLUE, Pattern.COL_PURPLE, Pattern.SHAPE_CASTLE));
            patterns.add(new Pattern(7, Pattern.COL_PURPLE, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_HOLLOWCROSS));
            patterns.add(new Pattern(8, Pattern.COL_YELLOW, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_STAR));
            patterns.add(new Pattern(9, Pattern.COL_GREEN, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_BOULON));
            patterns.add(new Pattern(10, Pattern.COL_PINK, Pattern.COL_YELLOW, Pattern.SHAPE_CASTLE));
            patterns.add(new Pattern(11, Pattern.COL_GREEN, Pattern.COL_ORANGE, Pattern.SHAPE_LYS));
            patterns.add(new Pattern(12, Pattern.COL_DARKBLUE, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_SQUARE));
            patterns.add(new Pattern(13, Pattern.COL_DARKPURPLE, Pattern.COL_YELLOW, Pattern.SHAPE_TOXIC));
            patterns.add(new Pattern(14, Pattern.COL_GREEN, Pattern.COL_ORANGE, Pattern.SHAPE_FLOWER));
            patterns.add(new Pattern(15, Pattern.COL_MAGENTA, Pattern.COL_YELLOW, Pattern.SHAPE_ROUNDCROSS));
            patterns.add(new Pattern(16, Pattern.COL_YELLOW, Pattern.COL_DARKBLUE, Pattern.SHAPE_CROSSBALLS));
            patterns.add(new Pattern(17, Pattern.COL_MAGENTA, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_LYS));
            patterns.add(new Pattern(18, Pattern.COL_DARKBLUE, Pattern.COL_ORANGE, Pattern.SHAPE_STAR));
            patterns.add(new Pattern(19, Pattern.COL_GREEN, Pattern.COL_ORANGE, Pattern.SHAPE_CASTLE));
            patterns.add(new Pattern(20, Pattern.COL_LIGHTBLUE, Pattern.COL_ORANGE, Pattern.SHAPE_HOLLOWCROSS));
            patterns.add(new Pattern(21, Pattern.COL_DARKPURPLE, Pattern.COL_PINK, Pattern.SHAPE_STAR));
            patterns.add(new Pattern(22, Pattern.COL_DARKPURPLE, Pattern.COL_GREEN, Pattern.SHAPE_BOULON));
        }
        patterns.add(new Pattern(23, Pattern.COL_MAGENTA, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_TOXIC));
        patterns.add(new Pattern(24, Pattern.COL_GREEN, Pattern.COL_ORANGE, Pattern.SHAPE_SQUARE));
        patterns.add(new Pattern(25, Pattern.COL_LIGHTBLUE, Pattern.COL_PINK, Pattern.SHAPE_SQUARE));
        patterns.add(new Pattern(26, Pattern.COL_YELLOW, Pattern.COL_GREEN, Pattern.SHAPE_FLOWER));
        patterns.add(new Pattern(27, Pattern.COL_PURPLE, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_LYS));
        patterns.add(new Pattern(28, Pattern.COL_PURPLE, Pattern.COL_YELLOW, Pattern.SHAPE_CASTLE));
        patterns.add(new Pattern(29, Pattern.COL_GREEN, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_STAR));
        patterns.add(new Pattern(30, Pattern.COL_YELLOW, Pattern.COL_LIGHTBLUE, Pattern.SHAPE_BOULON));
        patterns.add(new Pattern(31, Pattern.COL_MAGENTA, Pattern.COL_GREEN, Pattern.SHAPE_BOULON));
        patterns.add(new Pattern(32, Pattern.COL_MAGENTA, Pattern.COL_GREEN, Pattern.SHAPE_HOLLOWCROSS));
        patterns.add(new Pattern(33, Pattern.COL_PURPLE, Pattern.COL_MAGENTA, Pattern.SHAPE_ROUNDCROSS));
        patterns.add(new Pattern(34, Pattern.COL_DARKPURPLE, Pattern.COL_ORANGE, Pattern.SHAPE_CROSSBALLS));
        patterns.add(new Pattern(35, Pattern.COL_ORANGE, Pattern.COL_GREEN, Pattern.SHAPE_LYS));
        patterns.add(new Pattern(36, Pattern.COL_LIGHTBLUE, Pattern.COL_DARKBLUE, Pattern.SHAPE_STAR));
        patterns.add(new Pattern(37, Pattern.COL_PURPLE, Pattern.COL_GREEN, Pattern.SHAPE_CASTLE));
        patterns.add(new Pattern(38, Pattern.COL_YELLOW, Pattern.COL_PURPLE, Pattern.SHAPE_HOLLOWCROSS));
        patterns.add(new Pattern(39, Pattern.COL_GREEN, Pattern.COL_YELLOW, Pattern.SHAPE_STAR));
    }

    public static void savePatterns(File file) throws IOException {
        Writer writer = new FileWriter(file);
        for (Pattern pattern : patterns) {
            writer.write(String.format("%3d %8x %8x %3d\n", pattern.getCode(), pattern.getPatternBg().getRGB(), pattern.getPatternFg().getRGB(), pattern.getPatternShape()));
        }
        writer.close();
    }

    public static void loadPatterns(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
            StringTokenizer tokenizer = new StringTokenizer(line);
            int code = Integer.parseInt(tokenizer.nextToken(), 10);
            int bg = Integer.parseInt(tokenizer.nextToken().substring(2), 16);
            int fg = Integer.parseInt(tokenizer.nextToken().substring(2), 16);
            int shape = Integer.parseInt(tokenizer.nextToken(), 10);
            patterns.add(new Pattern(code, new Color(bg), new Color(fg), shape));
            line = reader.readLine();
        }
        reader.close();
    }

    public static List<Pattern> getAllPatterns() {
        return patterns;
    }

    public static List<Pattern> getNonDefaultPatterns() {
        return patterns.subList(1, patterns.size());
    }

    public static Pattern getDefaultPattern() {
        return patterns.get(0);
    }

    public static Pattern getPatternByCode(int pattern_code) {
        Pattern result = null;
        for (Pattern pattern : patterns) {
            if (pattern.getCode() == pattern_code) {
                result = pattern;
            }
        }
        return result;
    }
}
