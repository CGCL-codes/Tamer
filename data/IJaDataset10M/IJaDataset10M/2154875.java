package org.fao.fenix.web.client.re2.catalogue;

import java.util.ArrayList;
import java.util.List;
import net.mygwt.ui.client.Style;
import net.mygwt.ui.client.event.BaseEvent;
import net.mygwt.ui.client.event.SelectionListener;
import net.mygwt.ui.client.widget.Info;
import net.mygwt.ui.client.widget.menu.Menu;
import net.mygwt.ui.client.widget.menu.MenuItem;
import net.mygwt.ui.client.widget.table.TableItem;
import org.fao.fenix.web.client.CommunicationResource;
import org.fao.fenix.web.client.Fenix;
import org.fao.fenix.web.client.FenixResource;
import org.fao.fenix.web.client.chartwizard.FenixChartWizard;
import org.fao.fenix.web.client.layerwizard.FenixLayerWizard;
import org.fao.fenix.web.client.metadata.MetadataViewer;
import org.fao.fenix.web.client.re2.ResourceExplorer;
import org.fao.fenix.web.client.re2.search.ResourceTypeNew;
import org.fao.fenix.web.client.re2.shells.RenameResourceShell;
import org.fao.fenix.web.client.tablewizard.FenixTableWizard;
import org.fao.fenix.web.client.util.FenixDebugShell;
import org.fao.fenix.web.client.util.Loading;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CatalogueContextMenu {

    private Menu contextMenu;

    private MenuItem separator;

    private String birtURL;

    public CatalogueContextMenu() {
        contextMenu = new Menu();
    }

    private void removeResource() {
        final String resourceType = (String) Fenix.newResourceExplorer.catalogueList.table.getSelectedItem().getValues()[1];
        long resourceId = Long.valueOf((String) Fenix.newResourceExplorer.catalogueList.table.getSelectedItem().getValues()[5]).longValue();
        Fenix.reService.deleteResource(resourceId, resourceType, new AsyncCallback() {

            public void onSuccess(Object result) {
                ResourceExplorer.searchEvent.searchStart();
            }

            public void onFailure(Throwable caught) {
                Info.show("Service call failed!", "Service call to {0} failed", "deleteResource()");
            }
        });
    }

    public Menu build(String resourceType) {
        MenuItem open = new MenuItem(Style.PUSH);
        open.setText(Fenix.fenixLang.open());
        open.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                CatalogueOpener.open(Fenix.newResourceExplorer.caller);
            }
        });
        contextMenu.add(open);
        separator = new MenuItem(Style.SEPARATOR);
        contextMenu.add(separator);
        MenuItem openAsChart = new MenuItem(Style.PUSH);
        openAsChart.setText(Fenix.fenixLang.openAsChart());
        openAsChart.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                if (((String) item.getValue(1)).equals(ResourceTypeNew.DATASET)) {
                    List chartList = new ArrayList();
                    chartList.add((String) item.getValue(5));
                    if (Fenix.chartWizard == null) {
                        Fenix.chartWizard = new FenixChartWizard();
                        Fenix.chartWizard.build(chartList);
                    } else {
                        new FenixDebugShell("The chart wizard has been already opened.");
                    }
                    Fenix.newResourceExplorer.window.close();
                } else new FenixDebugShell("Selected resource is not a dataset, please re-try.");
            }
        });
        if (resourceType.equals(ResourceTypeNew.DATASET)) contextMenu.add(openAsChart);
        final MenuItem openAsMap = new MenuItem(Style.PUSH);
        openAsMap.setText(Fenix.fenixLang.openAsMap());
        TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
        openAsMap.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                if (((String) item.getValue(1)).equals(ResourceTypeNew.LAYER)) {
                    FenixResource rsrc = new FenixResource();
                    rsrc.setType((String) item.getValue(1));
                    rsrc.setTitle((String) item.getValue(0));
                    rsrc.setId((String) item.getValue(5));
                    List layerList = new ArrayList();
                    layerList.add(rsrc);
                    if (Fenix.layerWizard == null) {
                        Fenix.layerWizard = new FenixLayerWizard();
                        Fenix.layerWizard.build(layerList);
                    } else {
                        new FenixDebugShell("The layer wizard has been already opened.");
                    }
                    Fenix.newResourceExplorer.window.close();
                } else new FenixDebugShell("Selected resource is not a layer, please re-try.");
            }
        });
        if (resourceType.equals(ResourceTypeNew.LAYER)) {
            contextMenu.add(openAsMap);
            separator = new MenuItem(Style.SEPARATOR);
            contextMenu.add(separator);
        }
        MenuItem openAsTable = new MenuItem(Style.PUSH);
        openAsTable.setText(Fenix.fenixLang.openAsTable());
        openAsTable.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                if (((String) item.getValue(1)).equals(ResourceTypeNew.DATASET)) {
                    List tableList = new ArrayList();
                    tableList.add(CatalogueOpener.cleanTitle(((String) item.getValue(0))));
                    if (Fenix.tableWizard == null) {
                        Fenix.tableWizard = new FenixTableWizard(tableList);
                    } else {
                        new FenixDebugShell("The table wizard has been already opened.");
                    }
                    Fenix.newResourceExplorer.window.close();
                } else new FenixDebugShell("Selected resource is not a dataset, please re-try.");
            }
        });
        if (resourceType.equals(ResourceTypeNew.DATASET)) {
            contextMenu.add(openAsTable);
            separator = new MenuItem(Style.SEPARATOR);
            contextMenu.add(separator);
        }
        MenuItem duplicate = new MenuItem(Style.PUSH);
        duplicate.setText(Fenix.fenixLang.duplicate());
        duplicate.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                Info.show("Catalogue", "You clicked on {0}", Fenix.fenixLang.duplicate());
            }
        });
        duplicate.setEnabled(false);
        contextMenu.add(duplicate);
        separator = new MenuItem(Style.SEPARATOR);
        contextMenu.add(separator);
        MenuItem export = new MenuItem(Style.PUSH);
        export.setText(Fenix.fenixLang.export());
        export.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                Info.show("Catalogue", "You clicked on {0}", Fenix.fenixLang.export());
            }
        });
        export.setEnabled(false);
        contextMenu.add(export);
        MenuItem delete = new MenuItem(Style.PUSH);
        delete.setText(Fenix.fenixLang.delete());
        delete.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                removeResource();
            }
        });
        contextMenu.add(delete);
        MenuItem rename = new MenuItem(Style.PUSH);
        rename.setText(Fenix.fenixLang.rename());
        rename.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                new RenameResourceShell().build();
            }
        });
        contextMenu.add(rename);
        separator = new MenuItem(Style.SEPARATOR);
        contextMenu.add(separator);
        MenuItem viewMetadata = new MenuItem(Style.PUSH);
        viewMetadata.setText(Fenix.fenixLang.viewMetadata());
        viewMetadata.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                Long id = Long.valueOf((String) item.getValues()[5]);
                String type = (String) item.getValues()[1];
                new MetadataViewer().build(id, type);
            }
        });
        contextMenu.add(viewMetadata);
        MenuItem linkedResources = new MenuItem(Style.PUSH);
        linkedResources.setText(Fenix.fenixLang.linkedResources());
        linkedResources.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                Info.show("Catalogue", "You clicked on {0}", Fenix.fenixLang.linkedResources());
            }
        });
        linkedResources.setEnabled(false);
        contextMenu.add(linkedResources);
        separator = new MenuItem(Style.SEPARATOR);
        contextMenu.add(separator);
        MenuItem shareResource = new MenuItem(Style.PUSH);
        shareResource.setText(Fenix.fenixLang.shareResource());
        shareResource.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                final CommunicationResource com = new CommunicationResource();
                com.setHost(Fenix.fenixServicesUrl);
                com.setLocalId((String) item.getValue(5));
                com.setTitle(cleanTitle(((String) item.getValue(0))));
                com.setType((String) item.getValue(1));
                com.setOGroup((String) item.getValue(4));
                final Loading progress = new Loading();
                progress.create();
                Fenix.communicationService.shaDigest(Fenix.fenixServicesUrl, com, new AsyncCallback() {

                    public void onSuccess(Object result) {
                        String digest = (String) result;
                        com.setDigest("DIGEST");
                        Fenix.communicationService.pushInformation(Fenix.fenixServicesUrl, com, new AsyncCallback() {

                            public void onSuccess(Object result) {
                                new FenixDebugShell("<ul><b>" + Fenix.fenixLang.giewsNetwork() + "</b></ul><ul>" + (String) result + "</ul>");
                                progress.destroy();
                            }

                            public void onFailure(Throwable caught) {
                                new FenixDebugShell("<ul><b>CatalogueContextMenu</b></ul><ul>RPC to pushInformation(resource).</ul><ul>" + caught.getMessage() + "</ul>");
                                progress.destroy();
                            }
                        });
                    }

                    public void onFailure(Throwable caught) {
                        new FenixDebugShell("<ul><b>CatalogueContextMenu</b></ul><ul>RPC to pushInformation(resource).</ul><ul>" + caught.getMessage() + "</ul>");
                        progress.destroy();
                    }
                });
            }
        });
        if (resourceType.equals(ResourceTypeNew.DATASET) || resourceType.equals(ResourceTypeNew.LAYER)) {
            if (Fenix.newResourceExplorer.searchMask.scope != null) {
                String scope = Fenix.newResourceExplorer.searchMask.scope.getItemText(Fenix.newResourceExplorer.searchMask.scope.getSelectedIndex());
                if (scope.equals(Fenix.fenixLang.thisNode())) contextMenu.add(shareResource);
            } else {
                contextMenu.add(shareResource);
            }
        }
        MenuItem downloadResource = new MenuItem(Style.PUSH);
        downloadResource.setText(Fenix.fenixLang.downloadResource());
        downloadResource.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                final String localResourceId = (String) item.getValues()[3];
                String scope = Fenix.newResourceExplorer.searchMask.scope.getItemText(Fenix.newResourceExplorer.searchMask.scope.getSelectedIndex());
                if (!scope.equals(Fenix.fenixLang.thisNode())) {
                    final Loading progress = new Loading();
                    progress.create();
                    Fenix.communicationService.downloadResource((String) item.getValues()[2], localResourceId, new AsyncCallback() {

                        public void onSuccess(Object result) {
                            Fenix.communicationService.importDataset(localResourceId, new AsyncCallback() {

                                public void onSuccess(Object result) {
                                    new FenixDebugShell("Resource has been downloaded.");
                                    progress.destroy();
                                }

                                public void onFailure(Throwable caught) {
                                    new FenixDebugShell("<ul><b>CatalogueContextMenu</b></ul><ul>RPC to downloadResource(id).</ul><ul>" + caught.getMessage() + "</ul>");
                                    progress.destroy();
                                }
                            });
                        }

                        public void onFailure(Throwable caught) {
                            new FenixDebugShell("<ul><b>CatalogueContextMenu</b></ul><ul>RPC to downloadResource(id).</ul><ul>" + caught.getMessage() + "</ul>");
                            progress.destroy();
                        }
                    });
                } else new FenixDebugShell("We're sorry, but you can not download a resource from your own node. Please change the search scope to 'This Group' or 'GIEWS Network'. Thank you.");
            }
        });
        Info.show("CATALOGUE", "Resource type {0}", resourceType);
        if (resourceType.equals(ResourceTypeNew.DATASET) || resourceType.equals(ResourceTypeNew.LAYER) || resourceType.equals("dataset")) ;
        contextMenu.add(downloadResource);
        MenuItem birtReportURL = new MenuItem(Style.PUSH);
        birtReportURL.setText(Fenix.fenixLang.reportURL());
        birtReportURL.addSelectionListener(new SelectionListener() {

            public void widgetSelected(BaseEvent be) {
                birtURL = Fenix.fenixServicesUrl;
                final TableItem item = Fenix.newResourceExplorer.catalogueList.table.getSelectedItem();
                int numSlash = 3;
                for (int i = (birtURL.length() - 1); i > 0; i--) {
                    if (birtURL.charAt(i) == '/') {
                        numSlash--;
                        if (numSlash == 0) {
                            birtURL = birtURL.substring(0, i);
                            break;
                        }
                    }
                }
                Fenix.birtService.getBirtApplName(new AsyncCallback() {

                    public void onSuccess(Object result) {
                        new FenixDebugShell("You can add that link in your web site  to view the report<br>" + birtURL + "/" + (String) result + "/FenixBirtServlet?dataViewId=" + (String) item.getValues()[5] + "&servletType=frameset");
                    }

                    public void onFailure(Throwable caught) {
                        Info.show("Service call failed!", "Service call to {0} failed", "getBirtApplName()");
                    }
                });
            }
        });
        if (resourceType.equals(ResourceTypeNew.REPORT)) {
            contextMenu.add(birtReportURL);
        }
        return contextMenu;
    }

    public static String cleanTitle(String title) {
        int index = 0;
        for (int i = 0; i < title.length() - 2; i++) if (title.charAt(i) == '\"' && title.charAt(i + 1) == '>') {
            index = i + 3;
        }
        return title.substring(index);
    }
}
