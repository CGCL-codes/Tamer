package org.nightlabs.jfire.web.demoshop.producttype;

import java.io.IOException;
import java.util.Map;
import javax.jdo.FetchPlan;
import javax.jdo.JDOHelper;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.nightlabs.jdo.NLJDOHelper;
import org.nightlabs.jfire.accounting.Price;
import org.nightlabs.jfire.accounting.PriceFragmentType;
import org.nightlabs.jfire.accounting.dao.PriceFragmentTypeDAO;
import org.nightlabs.jfire.accounting.id.CurrencyID;
import org.nightlabs.jfire.accounting.id.TariffID;
import org.nightlabs.jfire.store.ProductType;
import org.nightlabs.jfire.store.id.ProductTypeID;
import org.nightlabs.jfire.voucher.accounting.VoucherPriceConfig;
import org.nightlabs.jfire.voucher.dao.VoucherTypeDAO;
import org.nightlabs.jfire.voucher.store.VoucherType;
import org.nightlabs.jfire.web.demoshop.BaseServlet;
import org.nightlabs.jfire.web.demoshop.WebShopServlet;
import org.nightlabs.progress.NullProgressMonitor;

/**
 * @author Attapol Thomprasert - Attapol[at]nightlabs[dot]de
 */
public class VoucherTypeDetailServlet extends WebShopServlet {

    /**
	 * The serial version of this class.
	 **/
    private static final long serialVersionUID = 1L;

    public static final String[] VOUCHER_DETAIL_FETCH_GROUPS = new String[] { FetchPlan.DEFAULT, ProductType.FETCH_GROUP_NAME, ProductType.FETCH_GROUP_PACKAGE_PRICE_CONFIG, VoucherPriceConfig.FETCH_GROUP_PRICES };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            ProductTypeID productTypeID = new ProductTypeID(requireParameter("productTypeID"));
            VoucherType voucherType = VoucherTypeDAO.sharedInstance().getVoucherType(productTypeID, VOUCHER_DETAIL_FETCH_GROUPS, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT, new NullProgressMonitor());
            setAttribute("productType", voucherType);
            CurrencyID currencyID = CurrencyID.create(BaseServlet.getConfig().getCurrencyId());
            org.nightlabs.jfire.accounting.Currency currency = null;
            long priceValue = 0;
            VoucherPriceConfig voucherPriceConfig = (VoucherPriceConfig) voucherType.getPackagePriceConfig();
            for (Map.Entry<org.nightlabs.jfire.accounting.Currency, Long> me : voucherPriceConfig.getPrices().entrySet()) {
                if (JDOHelper.getObjectId(me.getKey()).equals(currencyID)) {
                    currency = me.getKey();
                    priceValue = me.getValue().longValue();
                    break;
                }
            }
            Price voucherPrice = new Price(voucherPriceConfig.getOrganisationID(), voucherPriceConfig.getPriceConfigID(), voucherPriceConfig.createPriceID(), currency);
            voucherPrice.setAmount(PriceFragmentTypeDAO.sharedInstance().getPriceFragmentType(PriceFragmentType.PRICE_FRAGMENT_TYPE_ID_TOTAL, new String[] { FetchPlan.DEFAULT }, NLJDOHelper.MAX_FETCH_DEPTH_NO_LIMIT, new NullProgressMonitor()), priceValue);
            setAttribute("voucherPrice", voucherPrice);
        } catch (Exception e) {
            throw new ServletException("Failed to resolve productTypeID", e);
        }
        request.setAttribute("voucherTariff", new TariffID());
        showPage("/jsp-products/voucherDetails.jsp");
    }
}
