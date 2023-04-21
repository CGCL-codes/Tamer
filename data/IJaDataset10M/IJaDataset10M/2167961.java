package com.bargain.controller.action.user;

import com.bargain.controller.action.AppRoot;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionErrors;
import java.util.List;
import java.util.Calendar;
import java.io.File;
import com.bargain.controller.actionform.user.item.AddEditItemForm;
import com.bargain.model.schema.Category;
import com.bargain.model.schema.Users;
import com.bargain.utils.UploadFile;
import com.bargain.utils.Common;
import org.apache.struts.upload.FormFile;
import com.bargain.utils.Config;

public class Item extends AppRoot {

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        com.bargain.controller.action.Item.getCategories(request);
        String status = request.getParameter("status");
        if (status == null || status.equals("")) {
            status = "pending";
        }
        Users user = getLoggedInUser(request);
        List items = user.getItems(status);
        request.setAttribute("items", items);
        String[] acceptedStatuses = new String[5];
        acceptedStatuses[0] = "pending";
        acceptedStatuses[1] = "accepted";
        acceptedStatuses[2] = "active";
        acceptedStatuses[3] = "closed";
        acceptedStatuses[4] = "rejected";
        request.setAttribute("currentStatus", status);
        request.setAttribute("acceptedStatuses", acceptedStatuses);
        return mapping.findForward("success");
    }

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List categories = Category.getCategories();
        request.setAttribute("categories", categories);
        request.setAttribute("item", new com.bargain.model.schema.Item());
        request.setAttribute("pictures", null);
        AddEditItemForm addForm = (AddEditItemForm) form;
        ActionErrors errors = addForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        com.bargain.model.schema.Item item = new com.bargain.model.schema.Item();
        item.setCategoryfk(addForm.getCategoryfk());
        item.setClientfk(getLoggedInUserId(request));
        item.setBuyerfk(0);
        item.setName(addForm.getName());
        item.setDescription(addForm.getDescription());
        item.setStartupprice(addForm.getStartupprice());
        item.setCurrentprice(addForm.getStartupprice());
        item.setBuynow(addForm.getBuynow());
        item.setStatus("pending");
        item.setStartauctionts(addForm.getStartauction());
        String ts = String.format("%d-%02d-%02d %02d:%02d:%02d", Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND);
        item.setTs(ts);
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.DAY_OF_MONTH, +10);
        String endAuctionTs = String.format("%d-%02d-%02d %02d:%02d:%02d", currentDate.YEAR, currentDate.MONTH, currentDate.DATE, currentDate.HOUR_OF_DAY, currentDate.MINUTE, currentDate.SECOND);
        item.setEndauctionts(endAuctionTs);
        item.create();
        manageUploadedPictures(addForm, item, "add");
        return mapping.findForward("success");
    }

    private void manageUploadedPictures(AddEditItemForm form, com.bargain.model.schema.Item item, String action) {
        Config appConfig = new Config();
        Config picConfig = appConfig.parseElement("item").parseElement("pictures");
        String uploadDir = picConfig.parseElement("upload_dir").getValue();
        if (form.getImage1() != null || form.getImage2() != null || form.getImage3() != null) {
            try {
                String imgThumbDirectory = String.format("%s\\item_%s\\thumb", uploadDir, item.getItemid());
                File thumbDirectory = new File(imgThumbDirectory);
                thumbDirectory.mkdirs();
                String imgViewDirectory = String.format("%s\\item_%s\\view", uploadDir, item.getItemid());
                File viewDirectory = new File(imgViewDirectory);
                viewDirectory.mkdirs();
                String defaultFileName = String.format("%s\\item_%s\\default.jpg", uploadDir, item.getItemid());
                FormFile defaultImage = null;
                if (form.getImage1() != null) {
                    defaultImage = form.getImage1();
                } else {
                    if (form.getImage2() != null) {
                        defaultImage = form.getImage2();
                    } else {
                        defaultImage = form.getImage3();
                    }
                }
                UploadFile.resizeAndSave(defaultImage, defaultFileName, 200, 200);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (form.getImage1() != null && form.getImage1().getFileSize() > 0) {
            insertAndUploadPicture(form.getImage1(), item.getItemid(), uploadDir);
        }
        if (form.getImage2() != null && form.getImage2().getFileSize() > 0) {
            insertAndUploadPicture(form.getImage2(), item.getItemid(), uploadDir);
        }
        if (form.getImage3() != null && form.getImage3().getFileSize() > 0) {
            insertAndUploadPicture(form.getImage3(), item.getItemid(), uploadDir);
        }
    }

    private void insertAndUploadPicture(FormFile imageFile, int itemId, String uploadDir) {
        com.bargain.model.schema.ItemPicture itemPicture = new com.bargain.model.schema.ItemPicture(itemId);
        itemPicture.create();
        String viewFileName = String.format("%s\\item_%s\\%s\\%s.jpg", uploadDir, itemId, "view", itemPicture.getItempictureid());
        String thumbFileName = String.format("%s\\item_%s\\%s\\%s.jpg", uploadDir, itemId, "thumb", itemPicture.getItempictureid());
        try {
            UploadFile.resizeAndSave(imageFile, viewFileName, 200, 200);
            UploadFile.resizeAndSave(imageFile, thumbFileName, 40, 40);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List categories = Category.getCategories();
        request.setAttribute("categories", categories);
        int item_id = Common.parseInt(String.valueOf(request.getParameter("id")));
        AddEditItemForm editForm = (AddEditItemForm) form;
        if (item_id == 0) {
            item_id = editForm.getItemid();
        }
        if (item_id == 0) {
            return mapping.findForward("error");
        }
        com.bargain.model.schema.Item item = com.bargain.model.schema.Item.getItem(item_id);
        if (item == null) {
            return mapping.findForward("error");
        }
        request.setAttribute("item", item);
        request.setAttribute("pictures", item.getPictures());
        ActionErrors errors = editForm.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            return mapping.getInputForward();
        }
        item.deletePictures(editForm.getDeletedImages());
        item.setCategoryfk(editForm.getCategoryfk());
        item.setName(editForm.getName());
        item.setDescription(editForm.getDescription());
        item.setStartupprice(editForm.getStartupprice());
        item.setBuynow(editForm.getBuynow());
        item.setStartauctionts(editForm.getStartauction());
        item.update();
        manageUploadedPictures(editForm, item, "edit");
        return mapping.findForward("success");
    }
}
