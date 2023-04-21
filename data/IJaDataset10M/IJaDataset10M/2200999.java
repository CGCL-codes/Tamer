package org.fao.fenix.web.client.text;

import java.util.ArrayList;
import java.util.List;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.event.ShellListenerAdapter;
import net.mygwt.ui.client.widget.Button;
import net.mygwt.ui.client.widget.Info;
import net.mygwt.ui.client.widget.Shell;
import org.fao.fenix.web.client.Fenix;
import org.fao.fenix.web.client.FenixSettings;
import org.fao.fenix.web.client.reportwizard.ListItemBirt;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FenixTextEditor {

    private Button saveResource;

    private Button linkResource;

    private Button saveResourceAs;

    private Button ok;

    private String rptdesign;

    public FenixTextEditor() {
        setTextViewId(null);
        setTitle(null);
        setContent(null);
        setReferenceDate(null);
        setLastUpdated(null);
        setTextViewId(null);
        this.rptdesign = "NoBirt";
    }

    public FenixTextEditor(String rptdesign) {
        setTextViewId(null);
        setTitle(null);
        setContent(null);
        setReferenceDate(null);
        setLastUpdated(null);
        setTextViewId(null);
        this.rptdesign = rptdesign;
    }

    public static String textTitle;

    public static Long textID;

    public static String textContent;

    public static String textReferenceDate;

    public static String textLastUpdated;

    public static String textAreaContent;

    private String index;

    public static Shell window;

    public static Shell viewerWindow;

    public FenixTextEditor(Long id, String index, List textList, Shell window) {
        this.rptdesign = "NoBirt";
        final List ls = (ArrayList) textList;
        this.index = index;
        this.viewerWindow = window;
        setTextViewId(id);
        setTitle((String) ls.get(1));
        setContent((String) ls.get(2));
        setReferenceDate((String) ls.get(3));
        if ((String) ls.get(4) != null) {
            setLastUpdated((String) ls.get(4));
        }
    }

    public Long getTextViewId() {
        return textID;
    }

    public void setTextViewId(Long textID) {
        this.textID = textID;
    }

    public String getTitle() {
        return textTitle;
    }

    public void setTitle(String textTitle) {
        this.textTitle = textTitle;
    }

    public String getContent() {
        return textContent;
    }

    public void setContent(String textContent) {
        this.textContent = textContent;
    }

    public void setReferenceDate(String textReferenceDate) {
        this.textReferenceDate = textReferenceDate;
    }

    public String getReferenceDate() {
        return textReferenceDate;
    }

    public void setLastUpdated(String textLastUpdated) {
        this.textLastUpdated = textLastUpdated;
    }

    public String getLastUpdated() {
        return textLastUpdated;
    }

    public void setViewerWindowIndex(String index) {
        this.index = index;
    }

    public String getViewerWindowIndex() {
        return index;
    }

    public void build() {
        window = new Shell(Style.RESIZE | Style.CLOSE);
        window.setSize("550px", "500px");
        if (getTitle() != null) {
            window.setText(getTitle() + " (" + Fenix.fenixLang.referenceDate() + ": " + getReferenceDate() + ")");
        } else {
            window.setText(Fenix.fenixLang.textEditor());
        }
        DOM.setStyleAttribute(window.getElement(), "backgroundColor", FenixSettings.TOOLS_COLOR);
        VerticalPanel panel = new VerticalPanel();
        panel.setWidth("100%");
        final RichTextArea rta = new RichTextArea();
        rta.setHeight("350px");
        rta.setWidth("100%");
        if (getContent() != null) {
            rta.setHTML(getContent());
        }
        RichTextToolbar toolbar = new RichTextToolbar(rta, rta.getHTML());
        toolbar.setWidth("100%");
        toolbar.setHeight("50px");
        panel.add(toolbar);
        panel.add(rta);
        panel.add(new HTML("<br/><br/>"));
        HorizontalPanel bp = new HorizontalPanel();
        if (rptdesign.equals("NoBirt")) {
            if (getTextViewId() != null) {
                saveResource = new Button(Fenix.fenixLang.save());
                saveResource.setWidth("100px");
                saveResource.addSelectionListener(new SelectionListener() {

                    public void widgetSelected(BaseEvent be) {
                        new SaveText().update(rta.getHTML());
                    }
                });
                bp.add(saveResource);
                bp.add(new HTML("&nbsp;&nbsp;"));
            }
            saveResourceAs = new Button(Fenix.fenixLang.saveAs());
            saveResourceAs.setWidth("100px");
            saveResourceAs.addSelectionListener(new SelectionListener() {

                public void widgetSelected(BaseEvent be) {
                    setContent(rta.getHTML());
                    new SaveText().build(rta.getHTML(), getReferenceDate());
                }
            });
            bp.add(saveResourceAs);
        } else {
            ok = new Button("ok");
            ok.setWidth("60px");
            bp.add(ok);
            ok.addSelectionListener(new SelectionListener() {

                public void widgetSelected(BaseEvent be) {
                    Fenix.reportWizard.report.setHTML("<div style='height:600px; margin:0px auto; padding:0pt; position:relative; width:715px;'><div style='height:33px; left:207px; position:absolute; top:283px; width:300px;'><img src='loading.gif'></div></div>");
                    window.close();
                    AsyncCallback callback = new AsyncCallback() {

                        public void onSuccess(Object result) {
                            Fenix.reportWizard.rptDesign = (String) ((List) result).get(0);
                            Fenix.reportWizard.report.setHTML("<iframe src='/" + Fenix.reportWizard.nameBirtApp + "/FenixBirtServletByFile?report=" + Fenix.reportWizard.rptDesign + "&servletType=frameset' width='100%' height='100%' />");
                            ListItemBirt newItem = new ListItemBirt();
                            newItem.setValue((String) ((List) result).get(1));
                            newItem.setText(Fenix.fenixLang.newText());
                            newItem.setIconStyle("noteIcon");
                            Fenix.reportWizard.listObject.add(newItem);
                        }

                        public void onFailure(Throwable caught) {
                            Info.show("Service call failed!", "Service call to {0} failed", "addTextToReport");
                        }
                    };
                    Fenix.birtService.addTextToReport(rptdesign, rta.getHTML(), callback);
                }
            });
        }
        panel.add(bp);
        panel.setCellHorizontalAlignment(bp, HorizontalPanel.ALIGN_CENTER);
        panel.setCellWidth(bp, "100%");
        panel.add(new HTML("<br/>"));
        window.getContent().add(panel);
        window.addShellListener(new ShellListenerAdapter() {

            public void shellClosed(BaseEvent event) {
                if (getViewerWindowIndex() != null && viewerWindow != null) {
                    Fenix.workspace.removeResourceFromWorkspaceManager(getViewerWindowIndex());
                    viewerWindow.close();
                }
                setTextViewId(null);
            }
        });
        window.open();
    }
}
