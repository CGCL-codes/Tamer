package com.rapidminer.gui.actions;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import org.apache.commons.lang.StringUtils;
import com.rapidminer.gui.OperatorDocViewer;
import com.rapidminer.gui.tools.ResourceAction;
import com.rapidminer.gui.tools.SwingTools;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.tools.plugin.Plugin;

public class ShowHelpTextInBrowserAction extends ResourceAction {

    private static final String DOUBLE_POINT = ":";

    private final OperatorDocViewer operatorDocViewer;

    private static final String[] browsers = new String[] { "iexplorer" };

    public static final String WIKI_URL_FOR_OPERATORS = "http://rapid-i.com/wiki/index.php?title=";

    public ShowHelpTextInBrowserAction(boolean smallIcon, String i18nKey, Object[] i18nArgs, OperatorDocViewer operatorDocViewer) {
        super(smallIcon, i18nKey, i18nArgs);
        this.operatorDocViewer = operatorDocViewer;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 909390054503086861L;

    @Override
    public void actionPerformed(ActionEvent e) {
        OperatorDescription operatorDescription = this.operatorDocViewer.getDisplayedOperator().getOperatorDescription();
        Plugin provider = operatorDescription.getProvider();
        String prefix = StringUtils.EMPTY;
        if (provider != null) {
            prefix = provider.getPrefix();
            prefix = Character.toUpperCase(prefix.charAt(0)) + prefix.substring(1) + DOUBLE_POINT;
        }
        String url = WIKI_URL_FOR_OPERATORS + prefix + this.operatorDocViewer.getDisplayedOperatorDescName().replaceAll(" ", "_");
        try {
            Class<?> d = Class.forName("java.awt.Desktop");
            d.getDeclaredMethod("browse", new Class[] { java.net.URI.class }).invoke(d.getDeclaredMethod("getDesktop").invoke(null), new Object[] { java.net.URI.create(url) });
        } catch (Exception ignore) {
            String osName = System.getProperty("os.name");
            if (osName.startsWith("Mac")) {
                try {
                    Class.forName("com.apple.eio.FileManager").getDeclaredMethod("openURL", new Class[] { String.class }).invoke(null, new Object[] { url });
                } catch (IllegalArgumentException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                } catch (SecurityException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                } catch (IllegalAccessException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                } catch (InvocationTargetException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                } catch (NoSuchMethodException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                } catch (ClassNotFoundException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                }
            } else if (osName.startsWith("Windows")) {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, url);
                } catch (IOException e1) {
                    SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", e1, true, url);
                }
            } else {
                String browser = null;
                for (String b : browsers) {
                    try {
                        if (browser == null && Runtime.getRuntime().exec(new String[] { "which", b }).getInputStream().read() != -1) {
                            Runtime.getRuntime().exec(new String[] { browser = b, url });
                            if (browser == null) {
                                SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", null, true, Arrays.toString(browsers));
                            }
                        }
                    } catch (IOException e1) {
                        SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", e1, true, url);
                    } catch (Exception e1) {
                        SwingTools.showFinalErrorMessage("rapid_doc_bot_importer_showInBrowser", e1, true, url);
                    }
                }
            }
        }
    }
}
