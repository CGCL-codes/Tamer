package org.adapit.wctoolkit.infrastructure.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.adapit.wctoolkit.infrastructure.DefaultApplicationFrame;
import org.adapit.wctoolkit.infrastructure.util.ResourceMessage;
import org.adapit.wctoolkit.infrastructure.util.SpringResourceMessage;
import org.adapit.wctoolkit.models.config.ApplicationConfiguration;

public class DefaultLanguageMenu extends AbstractDefaultMenu {

    private boolean ptBr = true, enUs = true, spSp = false, frFr = false;

    public DefaultLanguageMenu() {
        super();
        setText(messages.getMessage("org.adapit.wctoolkit.tool.design.LanguagesMenu"));
        setName("languages");
        initialize();
    }

    private void initialize() {
        if (ApplicationConfiguration.getInstance().getCowntry().equalsIgnoreCase("BR")) {
            if (enUs) add(getEnglishMenuItem());
            if (spSp) add(getSpanishMenuItem());
            if (frFr) add(getFrenchMenuItem());
        } else if (ApplicationConfiguration.getInstance().getCowntry().equalsIgnoreCase("US")) {
            if (ptBr) add(getPortugueseMenuItem());
            if (spSp) add(getSpanishMenuItem());
            if (frFr) add(getFrenchMenuItem());
        } else if (ApplicationConfiguration.getInstance().getCowntry().equalsIgnoreCase("FR")) {
            if (ptBr) add(getPortugueseMenuItem());
            if (enUs) add(getEnglishMenuItem());
            if (spSp) add(getSpanishMenuItem());
        } else if (ApplicationConfiguration.getInstance().getCowntry().equalsIgnoreCase("SP")) {
            if (ptBr) add(getPortugueseMenuItem());
            if (enUs) add(getEnglishMenuItem());
            if (frFr) add(getFrenchMenuItem());
        }
    }

    private JMenuItem englishMenuItem;

    public JMenuItem getEnglishMenuItem() {
        if (englishMenuItem == null) {
            englishMenuItem = new JMenuItem(messages.getMessage("english"));
            englishMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int value = JOptionPane.showConfirmDialog(null, messages.getMessage("org.adapit.wctoolkit.tool.design.InternationalizationRestart"), messages.getMessage("org.adapit.wctoolkit.tool.design.RestartApplication"), JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        ApplicationConfiguration.getInstance().setLanguage("en");
                        ApplicationConfiguration.getInstance().setCowntry("US");
                        ApplicationConfiguration.getInstance().save();
                        DefaultApplicationFrame.getInstance().exit();
                        DefaultApplicationFrame.getInstance().dispose();
                        Thread t = new Thread(new Runnable() {

                            public void run() {
                                showNewFrame();
                            }
                        });
                        t.run();
                    }
                }
            });
        }
        return englishMenuItem;
    }

    private void showNewFrame() {
        if (false) try {
            JFrame jf = DefaultApplicationFrame.getInstance();
            JFrame frame = (JFrame) jf.getClass().newInstance();
            frame.setTitle(jf.getTitle());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private JMenuItem portugueseMenuItem;

    public JMenuItem getPortugueseMenuItem() {
        if (portugueseMenuItem == null) {
            portugueseMenuItem = new JMenuItem(messages.getMessage("portuguese"));
            portugueseMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int value = JOptionPane.showConfirmDialog(null, messages.getMessage("org.adapit.wctoolkit.tool.design.InternationalizationRestart"), messages.getMessage("org.adapit.wctoolkit.tool.design.RestartApplication"), JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        ApplicationConfiguration.getInstance().setLanguage("pt");
                        ApplicationConfiguration.getInstance().setCowntry("BR");
                        ApplicationConfiguration.getInstance().save();
                        DefaultApplicationFrame.getInstance().exit();
                        DefaultApplicationFrame.getInstance().dispose();
                        Thread t = new Thread(new Runnable() {

                            public void run() {
                                showNewFrame();
                            }
                        });
                        t.run();
                    }
                }
            });
        }
        return portugueseMenuItem;
    }

    private JMenuItem frenchMenuItem;

    public JMenuItem getFrenchMenuItem() {
        if (frenchMenuItem == null) {
            frenchMenuItem = new JMenuItem(messages.getMessage("french"));
            frenchMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int value = JOptionPane.showConfirmDialog(null, messages.getMessage("org.adapit.wctoolkit.tool.design.InternationalizationRestart"), messages.getMessage("org.adapit.wctoolkit.tool.design.RestartApplication"), JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        ApplicationConfiguration.getInstance().setLanguage("fr");
                        ApplicationConfiguration.getInstance().setCowntry("FR");
                        ApplicationConfiguration.getInstance().save();
                        DefaultApplicationFrame.getInstance().exit();
                        DefaultApplicationFrame.getInstance().dispose();
                        Thread t = new Thread(new Runnable() {

                            public void run() {
                                showNewFrame();
                            }
                        });
                        t.run();
                    }
                }
            });
        }
        return frenchMenuItem;
    }

    private JMenuItem spanishMenuItem;

    public JMenuItem getSpanishMenuItem() {
        if (spanishMenuItem == null) {
            spanishMenuItem = new JMenuItem(messages.getMessage("spanish"));
            spanishMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    int value = JOptionPane.showConfirmDialog(null, messages.getMessage("org.adapit.wctoolkit.tool.design.InternationalizationRestart"), messages.getMessage("org.adapit.wctoolkit.tool.design.RestartApplication"), JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        ApplicationConfiguration.getInstance().setLanguage("sp");
                        ApplicationConfiguration.getInstance().setCowntry("SP");
                        ApplicationConfiguration.getInstance().save();
                        DefaultApplicationFrame.getInstance().exit();
                        DefaultApplicationFrame.getInstance().dispose();
                        Thread t = new Thread(new Runnable() {

                            public void run() {
                                showNewFrame();
                            }
                        });
                        t.run();
                    }
                }
            });
        }
        return spanishMenuItem;
    }
}
