package com.quesofttech.web.pages;

import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;
import java.util.List;
import java.sql.Timestamp;
import javax.annotation.Resource;
import com.quesofttech.business.common.exception.BusinessException;
import com.quesofttech.business.domain.general.*;
import com.quesofttech.business.domain.general.iface.*;
import com.quesofttech.business.domain.security.iface.ISecurityFinderServiceRemote;
import com.quesofttech.web.base.SimpleBasePage;
import com.quesofttech.web.base.SecureBasePage;
import com.quesofttech.web.state.Visit;
import org.apache.tapestry.commons.components.DateTimeField;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Retain;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.corelib.components.DateField;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Submit;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Checkbox;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.corelib.components.Grid;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.omg.CosTransactions._SubtransactionAwareResourceStub;
import org.slf4j.Logger;
import org.apache.tapestry5.annotations.ApplicationState;

public class BomDetailMaintenance extends SecureBasePage {

    private String _strMode = "";

    private BomDetail BomDetailDetail;

    private BomDetail _BomDetail;

    @Persist
    private List<BomDetail> _BomDetails;

    @Persist
    private Long _headeridBOM;

    @Inject
    private Logger _logger;

    @Inject
    private Block blockFormView;

    @Persist
    private long lng_CurrentID;

    private String viewDisplayText = "", viewEditText = "";

    public String getViewDisplayText() {
        return viewDisplayText;
    }

    public String getviewEditText() {
        return viewEditText;
    }

    @Component(id = "BomDetailForm")
    private Form _form;

    @Persist
    private int int_SelectedRow;

    @ApplicationState
    private String myState;

    @Component(id = "GRID")
    private Grid _Grid;

    @Component(id = "id")
    private TextField _id;

    private Long id;

    public Long getid() {
        return id;
    }

    public void setid(Long id) {
        this.id = id;
    }

    @Component(id = "Location")
    private TextField _Location;

    private String Location;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    @Component(id = "quantityRequired")
    private TextField _quantityRequired;

    private Double quantityRequired;

    public Double getquantityRequired() {
        return quantityRequired;
    }

    public void setquantityRequired(Double quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    @Component(id = "scrapFactor")
    private TextField _scrapFactor;

    private Double scrapFactor;

    public Double getscrapFactor() {
        return scrapFactor;
    }

    public void setscrapFactor(Double scrapFactor) {
        this.scrapFactor = scrapFactor;
    }

    @Component(id = "endDate")
    private DateField _endDate;

    private Date endDate;

    public Date getendDate() {
        return endDate;
    }

    public void setendDate(Date endDate) {
        this.endDate = endDate;
    }

    @Component(id = "startDate")
    private DateField _startDate;

    private Date startDate;

    public Date getstartDate() {
        return startDate;
    }

    public void setstartDate(java.sql.Date startDate) {
        this.startDate = startDate;
    }

    @Component(id = "BOM")
    private TextField _BOM;

    private Long BOM;

    public Long getBOM() {
        return BOM;
    }

    public void setBOM(Long BOM) {
        this.BOM = BOM;
    }

    void RefreshRecords() {
        try {
            _BomDetails = getBomDetailService().findBomDetailsByBomId(_headeridBOM);
        } catch (BusinessException be) {
        }
        if (_BomDetails != null && !_BomDetails.isEmpty()) {
            if (int_SelectedRow == 0) {
                BomDetailDetail = _BomDetails.get(int_SelectedRow);
            } else {
                BomDetailDetail = _BomDetails.get(int_SelectedRow - 1);
            }
            myState = "U";
            viewDisplayText = "Block";
            viewEditText = "none";
            assignToLocalVariable(BomDetailDetail);
        }
    }

    private int getRcdLocation(Long id) throws BusinessException {
        int int_return, int_i;
        int_i = 0;
        int_return = 0;
        _BomDetails = getBomDetailService().findBomDetailsByBomId(_headeridBOM);
        for (BomDetail p : _BomDetails) {
            int_i++;
            if ((long) p.getId().intValue() == id) {
                int_return = int_i;
            }
        }
        return int_return;
    }

    public Block getBlock() {
        return blockFormView;
    }

    Object onSuccess() {
        _form.clearErrors();
        RefreshRecords();
        return blockFormView;
    }

    void setupRender() {
        RefreshRecords();
    }

    void onValidateForm() {
        try {
            if ("U" == myState) {
                _UpdateRecord();
            } else if ("A" == myState) {
                _AddRecord();
            }
        } catch (Exception e) {
            _form.recordError("Error Description: " + e.getMessage());
        }
    }

    void assignToDatabase(BomDetail bomDetail) {
        bomDetail.setId(id);
        bomDetail.setEndDate(new java.sql.Timestamp(endDate.getTime()));
        bomDetail.setLocation(Location);
        bomDetail.setQuantityRequired(quantityRequired);
        bomDetail.setScrapFactor(scrapFactor);
        bomDetail.setScrapFactor(scrapFactor);
        bomDetail.setStartDate(new java.sql.Timestamp(startDate.getTime()));
        bomDetail.setRecordStatus("A");
    }

    void assignToLocalVariable(BomDetail bomDetail) {
        this.id = bomDetail.getId();
        this.endDate = bomDetail.getEndDate();
        this.Location = bomDetail.getLocation();
        this.quantityRequired = bomDetail.getQuantityRequired();
        this.scrapFactor = bomDetail.getScrapFactor();
        this.startDate = bomDetail.getStartDate();
    }

    void _AddRecord() {
        BomDetail bomDetail = new BomDetail();
        try {
            bomDetail.setCreateLogin(getVisit().getMyLoginId());
            bomDetail.setModifyLogin(getVisit().getMyLoginId());
            assignToDatabase(bomDetail);
            getBomDetailService().addBomDetail(_headeridBOM, bomDetail);
        } catch (Exception e) {
            _logger.info("BomDetail_Add_problem");
            e.printStackTrace();
            _form.recordError(getMessages().get("BomDetail_add_problem"));
        }
    }

    void _UpdateRecord() {
        BomDetail bomDetail = new BomDetail();
        try {
            bomDetail = getBomDetailService().findBomDetail(id);
        } catch (BusinessException be) {
        }
        if (bomDetail != null) {
            try {
                bomDetail.setModifyLogin(getVisit().getMyLoginId());
                assignToDatabase(bomDetail);
                getBomDetailService().updateBomDetail(bomDetail);
            } catch (BusinessException e) {
                _form.recordError(_BOM, e.getLocalizedMessage());
            } catch (Exception e) {
                _logger.info("BomDetail_update_problem");
                e.printStackTrace();
                _form.recordError(getMessages().get("BomDetail_update_problem"));
            }
        }
    }

    void _DeleteRecord(Long id) {
        BomDetail bomDetail = new BomDetail();
        try {
            bomDetail.setModifyLogin(getVisit().getMyLoginId());
            bomDetail = getBomDetailService().findBomDetail(id);
        } catch (BusinessException be) {
        }
        if (bomDetail != null) {
            try {
                getBomDetailService().logicalDeleteBomDetail(bomDetail);
                if (int_SelectedRow != 0) {
                    int_SelectedRow--;
                }
                RefreshRecords();
            } catch (BusinessException e) {
                _form.recordError(_BOM, e.getLocalizedMessage());
            } catch (Exception e) {
                _logger.info("BomDetail_Delete_problem");
                e.printStackTrace();
                _form.recordError(getMessages().get("BomDetail_Delete_problem"));
            }
        }
    }

    Object onActionFromtoolbarDel(Long id) {
        _form.clearErrors();
        myState = "D";
        _strMode = "D";
        _DeleteRecord(id);
        return blockFormView;
    }

    Object onActionFromToolbarAdd() {
        _form.clearErrors();
        myState = "A";
        _strMode = "A";
        return blockFormView;
    }

    Object onActionFromSelect(long id) {
        myState = "U";
        _strMode = "U";
        lng_CurrentID = id;
        try {
            _form.clearErrors();
            BomDetailDetail = getBomDetailService().findBomDetail(id);
            int_SelectedRow = getRcdLocation(id);
        } catch (BusinessException be) {
        }
        if (BomDetailDetail != null) {
            viewDisplayText = "Block";
            viewEditText = "none";
            assignToLocalVariable(BomDetailDetail);
            return blockFormView;
        }
        return null;
    }

    private IBOMServiceRemote getBomDetailService() {
        return getBusinessServicesLocator().getBOMServiceRemote();
    }

    public List<BomDetail> getBomDetails() {
        return _BomDetails;
    }

    public BomDetail getBomDetail() throws BusinessException {
        return _BomDetail;
    }

    public void setBomDetail(BomDetail tb) {
        _BomDetail = tb;
    }

    public Long getHeaderID() {
        return _headeridBOM;
    }

    public void setHeaderID(Long id) {
        _headeridBOM = id;
    }
}
