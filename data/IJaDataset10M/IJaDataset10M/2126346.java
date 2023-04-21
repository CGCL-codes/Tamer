package com.iclotho.eshop.domain.merchant;

import java.sql.SQLException;
import java.util.List;
import com.iclotho.eshop.web.base.vo.MerchantStatusVO;
import com.iclotho.eshop.web.base.vo.MerchantVO;
import com.iclotho.eshop.web.base.vo.NewsCategoryVO;
import com.iclotho.eshop.web.base.vo.NewsVO;
import com.iclotho.eshop.web.base.vo.OrderVO;
import com.iclotho.eshop.web.base.vo.ProductCategoryVO;
import com.iclotho.eshop.web.base.vo.OperatorVO;
import com.iclotho.eshop.web.base.vo.ProductVO;
import com.iclotho.foundation.pagination.PaginationContext;
import com.iclotho.foundation.pub.exception.AppException;

public interface MerchantDomain {

    public MerchantStatusVO getMerchantStatusById(Integer statusId) throws AppException;

    public MerchantVO getMerchantById(String merchantId) throws AppException;

    public OperatorVO getOperator(String merchantId, String operatorId) throws AppException;

    public List getMerchantLevelProductCategoryList(String merchantId) throws AppException;

    public PaginationContext getMerchantLevelProductCategoryListForPagination(String merchantId, PaginationContext paginationContext);

    public int getProductCategorySize(String merchantId);

    public ProductCategoryVO getMerchantProductCategoryById(String merchantId, Long categoryId) throws AppException;

    public List getUserLevelProductCategoryList(String merchantId) throws AppException;

    public List getProducts(String merchantId) throws AppException;

    public List getProductsForUser(String merchantId) throws AppException;

    public List getProducts() throws AppException;

    public List getProductsForUser() throws AppException;

    public ProductVO getProduct(Long tid) throws AppException;

    public void updateProduct(ProductVO productVO) throws AppException, SQLException;

    public void deleteProduct(ProductVO productVO) throws AppException, SQLException;

    public void addProductCategory(ProductCategoryVO productCategoryVO) throws AppException;

    public int updateMerchantProductCategoryByExample(ProductCategoryVO productCategoryVO) throws AppException;

    public void addMerchantProduct(ProductVO productVO) throws AppException, SQLException;

    public List getNews(String merchantId) throws AppException;

    public List getNewsAvailable(String merchantId, Long categoryId) throws AppException;

    public PaginationContext getNewsAvailableForPagination(String merchantId, Long categoryId, PaginationContext paginationContext) throws AppException;

    public PaginationContext getNewsAvailableForPagination(Long categoryId, PaginationContext paginationContext) throws AppException;

    public void addNews(NewsVO newsVO) throws AppException, SQLException;

    public PaginationContext getNewsForPagination(String merchantId, PaginationContext paginationContext) throws AppException;

    public List getNewsCategoryAvailable(String merchantId) throws AppException;

    public List getNewsCategoryAll(String merchantId) throws AppException;

    public void addNewsCategory(NewsCategoryVO newsCategoryVO) throws AppException, SQLException;

    public NewsVO getNews(Long tid) throws AppException;

    public int updateNews(NewsVO newsVO) throws AppException;

    public int getOrdersSize(String merchantId) throws AppException;

    public PaginationContext getOrdersForPagination(String merchantId, PaginationContext paginationContext) throws AppException;

    public OrderVO getOrder(Long orderId) throws AppException;

    public void updateOrder(OrderVO orderVO) throws AppException;

    public void updateMerchant(MerchantVO merchantVO) throws AppException, SQLException;
}
