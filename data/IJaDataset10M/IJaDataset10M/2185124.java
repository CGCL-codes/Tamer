package com.jmex.angelfont;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import com.jme.image.Texture.MagnificationFilter;
import com.jme.image.Texture.MinificationFilter;
import com.jme.util.TextureManager;

/**
 * 
 * A Loader class for {@link BitmapFont} objects.
 * 
 * @author dhdd, Andreas Grabner
 * @author Momoko_Fan (enhancements)
 */
public class BitmapFontLoader {

    /**
     * 
     * Loads the jme default {@link BitmapFont}
     * 
     * @return the BitmapFont that is the jme default
     */
    public static BitmapFont loadDefaultFont() {
        URL fontFile = BitmapFontLoader.class.getClassLoader().getResource("com/jmex/angelfont/angelFont.fnt");
        URL textureFile = BitmapFontLoader.class.getClassLoader().getResource("com/jmex/angelfont/angelFont.png");
        try {
            return load(fontFile, textureFile);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 
     * loads the {@link BitmapFont} defined by the two provided URLs
     * 
     * @param fontFile the URL to the .fnt file of the {@link BitmapFont}
     * @param textureFile the URL to the texture file of the {@link BitmapFont}
     * @return the BitmapFont defined by the two provided {@link URL}s
     * @throws IOException if one of the provided {@link URL}s is null
     */
    public static BitmapFont load(URL fontFile, URL textureFile) throws IOException {
        try {
            BitmapCharacterSet charSet = new BitmapCharacterSet();
            BitmapFont font = new BitmapFont();
            if (fontFile == null) {
                throw new IOException("The given URL to the requested font file is null!");
            }
            if (textureFile == null) {
                throw new IOException("The given URL to the requested font texture file is null!");
            }
            font.setFontTexture(TextureManager.loadTexture(textureFile, true));
            font.getFontTexture().setMinificationFilter(MinificationFilter.Trilinear);
            font.getFontTexture().setMagnificationFilter(MagnificationFilter.Bilinear);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fontFile.openStream()));
            String regex = "[\\s=]+";
            font.setCharSet(charSet);
            while (reader.ready()) {
                String line = reader.readLine();
                String[] tokens = line.split(regex);
                if (tokens[0].equals("info")) {
                    for (int i = 1; i < tokens.length; i++) {
                        if (tokens[i].equals("size")) {
                            charSet.setRenderedSize(Math.abs(Integer.parseInt(tokens[i + 1])));
                        }
                    }
                } else if (tokens[0].equals("common")) {
                    for (int i = 1; i < tokens.length; i++) {
                        String token = tokens[i];
                        if (token.equals("lineHeight")) {
                            charSet.setLineHeight(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("base")) {
                            charSet.setBase(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("scaleW")) {
                            charSet.setWidth(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("scaleH")) {
                            charSet.setHeight(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("yoffset")) {
                            charSet.setyOffset(Integer.parseInt(tokens[i + 1]));
                        }
                    }
                } else if (tokens[0].equals("char")) {
                    BitmapCharacter ch = null;
                    for (int i = 1; i < tokens.length; i++) {
                        String token = tokens[i];
                        if (token.equals("id")) {
                            int index = Integer.parseInt(tokens[i + 1]);
                            ch = new BitmapCharacter();
                            charSet.addCharacter(index, ch);
                        } else if (token.equals("x")) {
                            ch.setX(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("y")) {
                            ch.setY(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("width")) {
                            ch.setWidth(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("height")) {
                            ch.setHeight(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("xoffset")) {
                            ch.setXOffset(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("yoffset")) {
                            ch.setYOffset(Integer.parseInt(tokens[i + 1]));
                        } else if (token.equals("xadvance")) {
                            ch.setXAdvance(Integer.parseInt(tokens[i + 1]));
                        }
                    }
                } else if (tokens[0].equals("kerning")) {
                    int index = 0;
                    Kerning k = new Kerning();
                    for (int i = 1; i < tokens.length; i++) {
                        if (tokens[i].equals("first")) {
                            index = Integer.parseInt(tokens[i + 1]);
                        } else if (tokens[i].equals("second")) {
                            k.setSecond(Integer.parseInt(tokens[i + 1]));
                        } else if (tokens[i].equals("amount")) {
                            k.setAmount(Integer.parseInt(tokens[i + 1]));
                        }
                    }
                    BitmapCharacter ch = charSet.getCharacter(index);
                    ch.getKerningList().add(k);
                }
            }
            reader.close();
            return font;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
