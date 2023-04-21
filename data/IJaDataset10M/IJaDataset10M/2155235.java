package com.docum.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DualListModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import com.docum.domain.po.common.City;
import com.docum.service.BaseService;

@Controller("testView")
@Scope("session")
public class TestView implements Serializable {

    private static final long serialVersionUID = 5827007837538386824L;

    @Autowired
    BaseService baseService;

    private UploadedFile uploadedFile;

    public Object save() {
        City city = new City("Питер", "Piter", false);
        baseService.save(city);
        return "test";
    }

    public Object go() {
        City city = baseService.getObject(City.class, 1L);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("city", city);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().put("city1", getCity());
        return "test1?faces-redirect=true";
    }

    public City getCity() {
        return baseService.getObject(City.class, 2L);
    }

    public void handleFileUpload() throws IOException {
        String fileName = FilenameUtils.getName(uploadedFile.getName());
        String contentType = uploadedFile.getContentType();
        byte[] bytes = uploadedFile.getBytes();
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(String.format("File '%s' of type '%s' successfully uploaded!", fileName, contentType)));
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public String getLongText() {
        return "Однажды в студёную зимнюю пору я из лесу вышел";
    }

    public void uploadFileNew(FileUploadEvent event) {
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    private DualListModel<City> cityModel;

    public DualListModel<City> getCityModel() {
        if (cityModel == null) {
            cityModel = new DualListModel<City>(baseService.getAll(City.class), new ArrayList<City>());
        }
        return cityModel;
    }

    public void setCityModel(DualListModel<City> cityModel) {
        this.cityModel = cityModel;
    }

    public Object saveSelection() {
        return "test";
    }

    private City selectedCity;

    public void setSelectedCity(City city) {
        this.selectedCity = city;
    }

    public City getSelectedCity() {
        return selectedCity;
    }
}
