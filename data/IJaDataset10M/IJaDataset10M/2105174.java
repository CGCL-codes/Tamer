package gemini.castor.ui.client.service;

import gemini.basic.dto.SearchProductCriteria;
import gemini.basic.model.Product;
import gemini.basic.model.ProductType;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 *
 *
 */
@RemoteServiceRelativePath("searchProductService.rpc")
@Transactional(rollbackFor = Throwable.class, readOnly = false)
public interface SearchProductService extends RemoteService {

    public ArrayList<ProductType> getAllCategory();

    public ArrayList<Product> findProduct(SearchProductCriteria criteria);
}
