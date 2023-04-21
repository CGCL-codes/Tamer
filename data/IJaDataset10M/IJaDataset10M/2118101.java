package com.lz.service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import com.lz.dao.SearchDAO;
import com.lz.dao.ItemDAO;
import com.lz.form.UploadItemDTO;
import com.lz.util.ConnMgr;

public class UploadItemService {

    public static void uploadGallery(UploadItemDTO uploadImage) {
        ItemDAO itemDAO = new ItemDAO();
        itemDAO.uploadGallery(uploadImage);
    }

    public static UploadItemDTO getItemDetails(UploadItemDTO itemDTO) {
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getItemDetails(itemDTO);
    }

    public static void updateItemDetails(UploadItemDTO imageDTO) {
        ItemDAO itemDAO = new ItemDAO();
        itemDAO.updateItemDetails(imageDTO);
    }

    public static void deleteItem(String shopId, String imageId) {
        ItemDAO itemDAO = new ItemDAO();
        itemDAO.deleteItem(shopId, imageId);
    }

    public static ArrayList getItemsList(String hotelid) {
        ItemDAO itemDAO = new ItemDAO();
        return itemDAO.getItemList(hotelid);
    }

    public static ArrayList getSearchResult(UploadItemDTO uform, String orderByCol, String start, String end) {
        List queryParams = new ArrayList(10);
        String query = buildSQLForBookSearch(uform, queryParams, orderByCol, start, end);
        ArrayList returnList = null;
        Connection conn = ConnMgr.getConnection();
        SearchDAO dao = new SearchDAO();
        System.out.println(query);
        returnList = dao.getSearchResult(conn, query, queryParams);
        try {
            conn.close();
        } catch (Exception e) {
            conn = null;
        } finally {
            conn = null;
        }
        return returnList;
    }

    public static String buildSQLForBookSearch(UploadItemDTO uform, List queryParams, String orderByCol, String startLimit, String endLimit) {
        StringBuffer sb = new StringBuffer("select * from item ");
        boolean isFirstCondition = true;
        if (null != uform.getCategoryid() && !"".equalsIgnoreCase(uform.getCategoryid().trim())) {
            sb.append(getWhereCondition(isFirstCondition, "categoryid", "="));
            queryParams.add(uform.getCategoryid().trim());
            isFirstCondition = false;
        }
        if (null != uform.getIsbn() && !"".equalsIgnoreCase(uform.getIsbn().trim())) {
            sb.append(getWhereCondition(isFirstCondition, "isbn", "like"));
            queryParams.add("%" + uform.getIsbn().trim() + "%");
            isFirstCondition = false;
        }
        if (null != uform.getAuthor() && !"".equalsIgnoreCase(uform.getAuthor().trim())) {
            sb.append(getWhereCondition(isFirstCondition, "author", "like"));
            queryParams.add("%" + uform.getAuthor().trim() + "%");
            isFirstCondition = false;
        }
        if (null != uform.getName() && !"".equalsIgnoreCase(uform.getName().trim())) {
            sb.append(getWhereCondition(isFirstCondition, "name", "like"));
            queryParams.add("%" + uform.getName().trim() + "%");
            isFirstCondition = false;
        }
        if (null != uform.getPublisher() && !"".equalsIgnoreCase(uform.getPublisher().trim())) {
            sb.append(getWhereCondition(isFirstCondition, "publisher", "like"));
            queryParams.add("%" + uform.getPublisher().trim() + "%");
            isFirstCondition = false;
        }
        if (null != uform.getYear() && !"".equalsIgnoreCase(uform.getYear().trim())) {
            sb.append(getWhereCondition(isFirstCondition, "year", "="));
            queryParams.add(uform.getYear().trim());
            isFirstCondition = false;
        }
        sb.append(" order by " + orderByCol + " desc LIMIT " + startLimit + "," + endLimit);
        return sb.toString();
    }

    private static String getWhereCondition(boolean isFirstCondition, String colName, String operator) {
        if (isFirstCondition) {
            return " where " + colName + " " + operator + " ?";
        }
        return " AND " + colName + " " + operator + " ?";
    }
}
