package br.revista.action;

import br.revista.model.File;
import br.revista.model.Layout;
import br.revista.service.LayoutService;
import br.revista.service.control.AppServiceFactory;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.End;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;

@Name("sendLayouts")
@Scope(ScopeType.CONVERSATION)
public class SendLayoutsAction extends BaseAction {

    private File file;

    private LayoutService layoutService;

    private Layout layout;

    private List<File> lstFiles;

    @In("#{detailLayout}")
    private DetailLayoutAction detailLayout;

    @In("#{manageLayouts}")
    private ManageLayoutsAction manageLayouts;

    public SendLayoutsAction() {
        super("SendLayoutsAction");
    }

    private void initServices() throws Exception {
        layoutService = (LayoutService) AppServiceFactory.getAppService(LayoutService.class);
    }

    @Begin(nested = true)
    public String start(Layout layout) {
        try {
            initServices();
        } catch (Exception ex) {
            return treatException(ex);
        }
        this.layout = layout;
        pageTracking.addVisitedPage();
        return "ok";
    }

    @End
    public String save() {
        try {
            layoutService.saveFormattedFiles(layout, lstFiles);
        } catch (Exception ex) {
            return treatException(ex);
        }
        info(getBundledMessage("sucessSendingFiles"));
        detailLayout.updateData();
        manageLayouts.setDLayouts(null);
        return goBack();
    }

    @Begin(join = true)
    public void addFile() {
        if (StringUtils.isNotEmpty(getFile().getName())) {
            file.setDatePosting(new GregorianCalendar());
            lstFiles.add(getFile());
            file = new File();
        }
    }

    @Begin(join = true)
    public void removeFile(File selectedFile) {
        lstFiles.remove(selectedFile);
    }

    @End
    public String goBack() {
        return pageTracking.goBack();
    }

    public File getFile() {
        if (file == null) {
            file = new File();
        }
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<File> getFiles() {
        if (lstFiles == null) {
            lstFiles = new ArrayList<File>();
        }
        return lstFiles;
    }

    public void setFiles(List<File> lstFiles) {
        this.lstFiles = lstFiles;
    }
}
