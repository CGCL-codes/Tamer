package codesheet;

import org.eclipse.jface.text.*;
import org.eclipse.swt.graphics.Point;
import core.CodeConstants;
import core.Main;
import loaders.LuaSyntaxLoader;
import loaders.BlizzardCommandsXMLLoader;
import luaapi.LuaMethod;
import blizzard.api.BlizzardFunction;
import helper.TextHelper;

public class CodeHover implements ITextHover {

    public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
        if (hoverRegion != null) {
            try {
                String word = WordHoverManager.extractWord(textViewer.getDocument(), hoverRegion.getOffset());
                if (word != null && word.length() > 0) {
                    if (word.equalsIgnoreCase("function")) return null;
                    String show = WordHoverManager.hover(word);
                    if (show == null || show.length() > 0) {
                        setInfoText(show);
                        return "(" + word + ") - " + show;
                    }
                    int line = textViewer.getDocument().getLineOfOffset(hoverRegion.getOffset());
                    IRegion lineRegion = textViewer.getDocument().getLineInformation(line);
                    if (lineRegion.getLength() > 0) {
                        String strLine = textViewer.getDocument().get(lineRegion.getOffset(), lineRegion.getLength());
                        if (strLine != null && strLine.length() > 0) {
                            String methodInfo = getMethodInfo(strLine, word, hoverRegion.getOffset());
                            if (methodInfo != null && methodInfo.length() > 0) {
                                setInfoText(methodInfo);
                                return methodInfo;
                            }
                            BlizzardFunction bf = BlizzardCommandsXMLLoader.findFunction(word);
                            if (bf != null) {
                                int fw = 78;
                                String name = bf.getName();
                                name = TextHelper.fixStringWidth(name, fw);
                                String desc = null;
                                if (bf.getDescription() != null) {
                                    desc = bf.getDescription();
                                    desc = TextHelper.fixStringWidth(desc, fw);
                                }
                                String example = null;
                                if (bf.getExample() != null) {
                                    example = bf.getExample();
                                    example = TextHelper.fixStringWidth(example, fw);
                                }
                                String ret = " " + name + (desc == null ? "" : "\nDescription:\n" + desc) + (example == null ? "" : "\nExample:\n" + example);
                                setInfoText(bf.getName());
                                ret += "\n";
                                ret = ret.replaceAll("\n", "\n ");
                                return ret;
                            }
                            LuaMethod lm = LuaSyntaxLoader.findMethod(word);
                            if (lm != null) {
                                setInfoText(lm.getRaw());
                                return lm.getRaw();
                            }
                        }
                    }
                } else {
                    if (hoverRegion.getLength() > -1) {
                        return null;
                    }
                }
            } catch (Exception e) {
                return "";
            }
        }
        return "";
    }

    private void setInfoText(final String text) {
        Runnable run = new Runnable() {

            public void run() {
                Main.setInformationMessage(text);
            }
        };
        Main.display.syncExec(run);
    }

    private String getMethodInfo(String text, String word, int offset) {
        try {
            text = text.trim();
            int open = text.indexOf("(");
            int close = text.indexOf(")");
            int function = text.indexOf(CodeConstants.FUNCTION_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
        Point selection = textViewer.getSelectedRange();
        if (selection.x <= offset && offset < selection.x + selection.y) return new Region(selection.x, selection.y);
        return new Region(offset, 0);
    }
}
